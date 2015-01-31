package com.tudaidai.tuantrip.util;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Observable;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.zip.GZIPInputStream;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.scheme.SocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.net.Uri;
import android.util.Log;

import com.tudaidai.tuantrip.TuanTripSettings;

class RemoteResourceFetcher extends Observable {
	public static final String TAG = "RemoteResourceFetcher";
	public static final boolean DEBUG = TuanTripSettings.DEBUG;

	private DiskCache mResourceCache;
	private ExecutorService mExecutor;

	private HttpClient mHttpClient;

	private ConcurrentHashMap<String, Callable<Request>> mActiveRequestsMap = new ConcurrentHashMap<String, Callable<Request>>();

	public RemoteResourceFetcher(DiskCache cache) {
		mResourceCache = cache;
		mHttpClient = createHttpClient();
		mExecutor = Executors.newCachedThreadPool();
	}

	@Override
	public void notifyObservers(Object data) {
		setChanged();
		super.notifyObservers(data);
	}

	public Future<Request> fetch(Uri uri, String hash) {
		Request request = new Request(uri, hash);
		synchronized (mActiveRequestsMap) {
			Callable<Request> fetcher = newRequestCall(request);

			// if(request.uri.toString().equals("http://upload.tuantrip.com/20110225/129861544780351400-z.jpg"))
			// {
			// int a = 1;
			// int b = a;
			// }

			if (mActiveRequestsMap.putIfAbsent(request.hash, fetcher) == null) {
				if (DEBUG)
					Log.d(TAG, "issuing new request for: " + uri);
				return mExecutor.submit(fetcher);
			} else {
				if (DEBUG)
					Log.d(TAG, "Already have a pending request for: " + uri);
			}
		}
		return null;
	}

	public void shutdown() {
		mExecutor.shutdownNow();
	}

	private Callable<Request> newRequestCall(final Request request) {
		return new Callable<Request>() {
			public Request call() {
				HttpGet httpGet = null;
				HttpEntity entity = null;
				InputStream is = null;

				try {
					if (DEBUG)
						Log.d(TAG, "Requesting: " + request.uri);

					// if(request.uri.toString().equals("http://upload.tuantrip.com/20110225/129861544780351400-z.jpg"))
					// {
					// int a = 1;
					// a = 1;
					// }

					httpGet = new HttpGet(request.uri.toString());
					httpGet.addHeader("Accept-Encoding", "gzip");
					// httpGet.addHeader("Connection", "Close");
					// mHttpClient.getConnectionManager().closeExpiredConnections();
					HttpResponse response = mHttpClient.execute(httpGet);

					int statusCode = response.getStatusLine().getStatusCode();
					if (statusCode == 200) {
						entity = response.getEntity();

						is = getUngzippedContent(entity);

						try {

							mResourceCache.store(request.hash, is);
							if (DEBUG)
								Log.d(TAG, "Request successful: " + request.uri);

						} catch (Exception e) {
							if (DEBUG)
								Log.d(TAG, "store failed to store: "
										+ request.hash + " with " + e);
						} catch (OutOfMemoryError e) {
							if (DEBUG)
								Log.e(TAG, request.uri + " is too large with "
										+ e);
						}
					} else {
						if (DEBUG)
							Log.d(TAG, "Request failed: " + request.uri
									+ " statusCode=" + statusCode);
					}

					if (is != null) {
						is.close();
					}
					if (entity != null)
						entity.consumeContent();
				} catch (Exception e) {
					if (DEBUG)
						Log.d(TAG, "Request failed: " + request.uri + " with "
								+ e);
					if (httpGet != null)
						httpGet.abort();
				} catch (OutOfMemoryError e) {
					if (DEBUG)
						Log.e(TAG, request.uri + " is too large22 with " + e);
				} finally {
					if (DEBUG)
						Log.d(TAG, "Request finished: " + request.uri);

					mActiveRequestsMap.remove(request.hash);
					notifyObservers(request.uri);

				}
				return request;
			}
		};
	}

	/**
	 * Gets the input stream from a response entity. If the entity is gzipped
	 * then this will get a stream over the uncompressed data.
	 * 
	 * @param entity
	 *            the entity whose content should be read
	 * @return the input stream to read from
	 * @throws IOException
	 */
	public static InputStream getUngzippedContent(HttpEntity entity)
			throws IOException {
		InputStream responseStream = entity.getContent();
		if (responseStream == null) {
			return responseStream;
		}
		Header header = entity.getContentEncoding();
		if (header == null) {
			return responseStream;
		}
		String contentEncoding = header.getValue();
		if (contentEncoding == null) {
			return responseStream;
		}
		if (contentEncoding.contains("gzip")) {
			responseStream = new GZIPInputStream(responseStream);
		}

		responseStream = new FlushedInputStream(responseStream);
		return responseStream;
	}

	/**
	 * Create a thread-safe client. This client does not do redirecting, to
	 * allow us to capture correct "error" codes.
	 * 
	 * @return HttpClient
	 */
	public static final DefaultHttpClient createHttpClient() {
		// Shamelessly cribbed from AndroidHttpClient
		HttpParams params = new BasicHttpParams();

		params.setParameter(
				org.apache.http.conn.params.ConnManagerPNames.TIMEOUT,
				new Long(10 * 1000));

		// Turn off stale checking. Our connections break all the time anyway,
		// and it's not worth it to pay the penalty of checking every time.
		HttpConnectionParams.setStaleCheckingEnabled(params, false);

		// Default connection and socket timeout of 10 seconds. Tweak to taste.
		HttpConnectionParams.setConnectionTimeout(params, 10 * 1000);
		HttpConnectionParams.setSoTimeout(params, 10 * 1000);
		HttpConnectionParams.setSocketBufferSize(params, 8192);

		// Sets up the http part of the service.
		final SchemeRegistry supportedSchemes = new SchemeRegistry();

		// Register the "http" protocol scheme, it is required
		// by the default operator to look up socket factories.
		final SocketFactory sf = PlainSocketFactory.getSocketFactory();
		supportedSchemes.register(new Scheme("http", sf, 80));

		final ClientConnectionManager ccm = new ThreadSafeClientConnManager(
				params, supportedSchemes);
		return new DefaultHttpClient(ccm, params);
	}

	private static class Request {
		Uri uri;
		String hash;

		public Request(Uri requestUri, String requestHash) {
			uri = requestUri;
			hash = requestHash;
		}

		@Override
		public int hashCode() {
			return hash.hashCode();
		}
	}

	private static class FlushedInputStream extends FilterInputStream {
		public FlushedInputStream(InputStream inputStream) {
			super(inputStream);
		}

		@Override
		public long skip(long n) throws IOException {
			long totalBytesSkipped = 0L;
			while (totalBytesSkipped < n) {
				long bytesSkipped = in.skip(n - totalBytesSkipped);
				if (bytesSkipped == 0L) {
					int byte1 = read();
					if (byte1 < 0) {
						break; // we reached EOF
					} else {
						bytesSkipped = 1; // we read one byte
					}
				}
				totalBytesSkipped += bytesSkipped;
			}
			return totalBytesSkipped;
		}
	}

}
