package com.tudaidai.tuantrip.location;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Application;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Address;
import android.location.Location;
import android.location.LocationManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.util.Log;

import com.mapabc.mapapi.GeoPoint;
import com.mapabc.mapapi.Geocoder;
import com.tudaidai.tuantrip.TuanTrip;
import com.tudaidai.tuantrip.TuanTripSettings;
import com.tudaidai.tuantrip.location.CellIDInfoManager.CellIDInfo;
import com.tudaidai.tuantrip.location.WifiInfoManager.WifiInfo;

public class LocationUtil {
	private static final int TIMEOUT = 10;
	private static String TAG = "LocationUtil";
	static final boolean DEBUG = TuanTripSettings.DEBUG;
	// //////
	Activity activity;
	Service service;
	ILocation iLocation;
	WifiManager mainWifi;
	WifiReceiver receiverWifi = new WifiReceiver();
	List<ScanResult> wifiList;
	ArrayList<WifiInfo> wifis;
	ArrayList<CellIDInfo> cellIDs;
	private volatile boolean isGps = false;
	Location gpsLocation;
	Location mLocation;
	private SearchLocationObserver mSearchLocationObserver = new SearchLocationObserver();

	// 调用google gears的方法，该方法调用gears来获取经纬度
	public static TuanLocation callGear(ArrayList<WifiInfo> wifi,
			ArrayList<CellIDInfo> cellID) {

		if (cellID == null)
			return null;

		final HttpParams httpParams = createHttpParams();
		DefaultHttpClient client = new DefaultHttpClient(httpParams);

		HttpPost post = new HttpPost("http://www.google.com/loc/json");
		JSONObject holder = new JSONObject();

		try {
			holder.put("version", "1.1.0");
			holder.put("host", "maps.google.com");
			holder.put("home_mobile_country_code",
					cellID.get(0).mobileCountryCode);
			holder.put("home_mobile_network_code",
					cellID.get(0).mobileNetworkCode);
			holder.put("radio_type", cellID.get(0).radioType);
			holder.put("request_address", true);
			if ("460".equals(cellID.get(0).mobileCountryCode))
				holder.put("address_language", "zh_CN");
			else
				holder.put("address_language", "en_US");

			JSONObject data, current_data;

			JSONArray array = new JSONArray();

			current_data = new JSONObject();
			current_data.put("cell_id", cellID.get(0).cellId);
			current_data.put("mobile_country_code",
					cellID.get(0).mobileCountryCode);
			current_data.put("mobile_network_code",
					cellID.get(0).mobileNetworkCode);
			current_data.put("age", 0);
			array.put(current_data);

			if (cellID.size() > 2) {
				for (int i = 1; i < cellID.size(); i++) {
					data = new JSONObject();
					data.put("cell_id", cellID.get(i).cellId);
					data.put("location_area_code",
							cellID.get(0).locationAreaCode);
					data.put("mobile_country_code",
							cellID.get(0).mobileCountryCode);
					data.put("mobile_network_code",
							cellID.get(0).mobileNetworkCode);
					data.put("age", 0);
					array.put(data);
				}
			}
			holder.put("cell_towers", array);

			if (wifi != null) {
				array = new JSONArray();
				for (WifiInfo wifiInfo : wifi) {
					data = new JSONObject();
					data.put("mac_address", wifiInfo.mac);
					data.put("signal_strength", 8);
					data.put("age", 0);

					array.put(data);

				}
				holder.put("wifi_towers", array);
			}

			StringEntity se = new StringEntity(holder.toString());
			Log.e("Location send", holder.toString());
			post.setEntity(se);
			HttpResponse resp = client.execute(post);

			HttpEntity entity = resp.getEntity();

			BufferedReader br = new BufferedReader(new InputStreamReader(
					entity.getContent()));
			StringBuffer sb = new StringBuffer();
			String result = br.readLine();
			while (result != null) {
				Log.e("Locaiton reseive", result);
				sb.append(result);
				result = br.readLine();
			}

			data = new JSONObject(sb.toString());
			data = (JSONObject) data.get("location");

			TuanLocation loc = new TuanLocation(new Location(
					LocationManager.NETWORK_PROVIDER));

			loc.setLatitude((Double) data.get("latitude"));
			loc.setLongitude((Double) data.get("longitude"));
			loc.setAccuracy(Float.parseFloat(data.get("accuracy").toString()));
			loc.setTime(System.currentTimeMillis());

			data = (JSONObject) data.get("address");
			String city = data.getString("city");
			loc.setCity(city.endsWith("市") ? city.substring(0,
					city.length() - 1) : city);
			if (data.has("street")) {
				if (data.has("street_number"))
					loc.setStreet(data.getString("street")
							+ data.getString("street_number"));
				else
					loc.setStreet(data.getString("street"));
			}
			return loc;
		} catch (JSONException e) {
			return null;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static TuanLocation callGearWithGeo2(Location location) {

		if (location == null)
			return null;

		final HttpParams httpParams = createHttpParams();
		DefaultHttpClient client = new DefaultHttpClient(httpParams);

		HttpGet gHttpGet = new HttpGet(
				"http://ditu.google.cn/maps/geo?output=json&hl=zh_cn&q="
						+ location.getLatitude() + ","
						+ location.getLongitude() + "");

		try {
			HttpResponse resp = client.execute(gHttpGet);

			String content = EntityUtils.toString(resp.getEntity(), "utf-8");

			JSONObject data = new JSONObject(content);
			if (data.getJSONObject("Status").getString("code").equals("200")) {
				TuanLocation loc = new TuanLocation(location);

				String address = ((JSONObject) data.getJSONArray("Placemark")
						.get(0)).getString("address");
				loc.setStreet(address.split(" ")[0]);
				String city = ((JSONObject) data.getJSONArray("Placemark").get(
						0)).getJSONObject("AddressDetails")
						.getJSONObject("Country")
						.getJSONObject("AdministrativeArea")
						.getJSONObject("Locality").getString("LocalityName");
				loc.setCity(city.endsWith("市") ? city.substring(0,
						city.length() - 1) : city);

				Log.e("callGearWithGeo2街道：", loc.getStreet());
				Log.e("callGearWithGeo2城市：", loc.getCity());

				return loc;
			}

		} catch (JSONException e1) {
			Log.e("", e1.toString());
		} catch (UnsupportedEncodingException e1) {
			Log.e("", e1.toString());
		} catch (ClientProtocolException e1) {
			Log.e("", e1.toString());
		} catch (Exception e1) {
			Log.e("", e1.toString());
		}
		return null;
	}

	public static TuanLocation callGearWithGeo3(Location location) {

		if (location == null)
			return null;

		final HttpParams httpParams = createHttpParams();
		DefaultHttpClient client = new DefaultHttpClient(httpParams);

		HttpGet gHttpGet = new HttpGet(
				"http://maps.google.com/maps/api/geocode/json?latlng="
						+ location.getLatitude() + ","
						+ location.getLongitude()
						+ "&language=zh-CN&sensor=true");

		try {
			HttpResponse resp = client.execute(gHttpGet);

			String content = EntityUtils.toString(resp.getEntity(), "utf-8");

			JSONObject data = new JSONObject(content);

			if (data.getString("status").equals("OK")) {
				TuanLocation loc = new TuanLocation(location);

				JSONObject jsonObject = (JSONObject) data.getJSONArray(
						"results").get(0);

				String address = jsonObject.getString("formatted_address");
				loc.setStreet(address.split(" ")[0]);

				for (int i = 0; i < jsonObject.getJSONArray(
						"address_components").length(); i++) {
					JSONObject jsonObject1 = (JSONObject) jsonObject
							.getJSONArray("address_components").get(i);
					if (jsonObject1.getJSONArray("types").getString(0)
							.equals("locality")) {
						String city = jsonObject1.getString("short_name");
						loc.setCity(city.endsWith("市") ? city.substring(0,
								city.length() - 1) : city);
						break;
					}
				}

				Log.e("callGearWithGeo3街道：", loc.getStreet());
				Log.e("callGearWithGeo3城市：", loc.getCity());

				return loc;
			}

		} catch (JSONException e1) {
			Log.e("", e1.toString());
		} catch (UnsupportedEncodingException e1) {
			Log.e("", e1.toString());
		} catch (ClientProtocolException e1) {
			Log.e("", e1.toString());
		} catch (Exception e1) {
			Log.e("", e1.toString());
		}
		return null;
	}

	public static TuanLocation callGearWithGeo1(Location location) {

		if (location == null)
			return null;

		final HttpParams httpParams = createHttpParams();
		DefaultHttpClient client = new DefaultHttpClient(httpParams);

		HttpPost post = new HttpPost("http://www.google.com/loc/json");
		JSONObject holder = new JSONObject();

		try {
			holder.put("version", "1.1.0");
			holder.put("host", "maps.google.com");
			holder.put("request_address", true);
			holder.put("address_language", "zh_CN");

			JSONObject locationjObject = new JSONObject();
			locationjObject.put("longitude", location.getLongitude());
			locationjObject.put("latitude", location.getLatitude());

			holder.put("location", locationjObject);

			StringEntity se = new StringEntity(holder.toString());
			Log.e("Location send", holder.toString());
			post.setEntity(se);
			HttpResponse resp = client.execute(post);

			String content = EntityUtils.toString(resp.getEntity(), "utf-8");

			JSONObject data = new JSONObject(content);
			data = (JSONObject) data.get("location");
			data = (JSONObject) data.get("address");

			TuanLocation loc = new TuanLocation(location);
			String city = data.getString("city");
			loc.setCity(city.endsWith("市") ? city.substring(0,
					city.length() - 1) : city);
			if (data.has("street")) {
				if (data.has("street_number"))
					loc.setStreet(city + data.getString("street")
							+ data.getString("street_number"));
				else
					loc.setStreet(city + data.getString("street"));
			}
			Log.e("街道：", loc.getStreet());
			Log.e("城市：", loc.getCity());

			return loc;

		} catch (JSONException e1) {
			Log.e("", e1.toString());
		} catch (UnsupportedEncodingException e1) {
			Log.e("", e1.toString());
		} catch (ClientProtocolException e1) {
			Log.e("", e1.toString());
		} catch (Exception e1) {
			Log.e("", e1.toString());
		}
		return null;
	}

	public static Location callGaoWithGeo(Location location, Context context) {

		if (location == null)
			return null;

		double mLat = location.getLatitude();
		double mLon = location.getLongitude();
		// 用给定的经纬度构造一个GeoPoint，单位是微度 (度 * 1E6)
		GeoPoint geo = new GeoPoint((int) (mLat * 1E6), (int) (mLon * 1E6));
		try {
			if (geo.toString() != "") {
				Geocoder mGeocoder01 = new Geocoder(context, TAG);
				int x = geo.getLatitudeE6(); // 得到geo纬度，单位微度 (度 * 1E6)
				double x1 = ((double) x) / 1000000;
				int y = geo.getLongitudeE6(); // 得到geo经度，单位微度 (度 * 1E6)
				double y1 = ((double) y) / 1000000;
				// 得到逆理编码，参数分别为：纬度，经度，最大结果集
				List<Address> lstAddress = mGeocoder01.getFromRawGpsLocation(
						x1, y1, 5);
				// List<Address> lstAddress = mGeocoder01
				// .getFromLocation(x1, y1, 3);
				if (lstAddress.size() != 0) {
					TuanLocation loc = new TuanLocation(location);

					for (int i = 0; i < lstAddress.size(); ++i) {
						Address adsLocation = lstAddress.get(i);
						String pr = adsLocation.getPremises();
						Log.i(TAG, "Address found = " + adsLocation.toString());
						if (pr.equals(Geocoder.POI)) {
							loc.setLongitude(adsLocation.getLongitude());
							loc.setLatitude(adsLocation.getLatitude());
							String city = adsLocation.getAdminArea();
							loc.setCity(city.endsWith("市") ? city.substring(0,
									city.length() - 1) : city);
							loc.setStreet(city + adsLocation.getFeatureName());
							break;
						}

					}
					return loc;
				} else {
					Log.i(TAG, "Address GeoPoint NOT Found.");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();

		}
		return location;

	}

	private static final HttpParams createHttpParams() {
		final HttpParams params = new BasicHttpParams();

		// Turn off stale checking. Our connections break all the time anyway,
		// and it's not worth it to pay the penalty of checking every time.
		HttpConnectionParams.setStaleCheckingEnabled(params, false);

		HttpConnectionParams.setConnectionTimeout(params, TIMEOUT * 1000);
		HttpConnectionParams.setSoTimeout(params, TIMEOUT * 1000);
		HttpConnectionParams.setSocketBufferSize(params, 8192);

		return params;
	}

	// /////////////
	public LocationUtil(Activity activity, ILocation iLocation) {
		this.activity = activity;
		init(iLocation, activity);
	}

	public LocationUtil(Service service, ILocation iLocation) {
		this.service = service;
		init(iLocation, service);
	}

	private void init(ILocation iLocation, Context context) {
		this.iLocation = iLocation;
		mainWifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
	}

	public void startGetLocation() {
		Void[] as = new Void[0];
		new LocationTask().execute(as);
	}

	class WifiReceiver extends BroadcastReceiver {
		public void onReceive(Context c, Intent intent) {
			wifiList = mainWifi.getScanResults();

		}
	}

	// gps
	class LocationTask extends AsyncTask<Void, Void, Location> {
		private static final String TAG = "LocationTask";
		private Exception mReason;

		public LocationTask() {
		}

		protected Location doInBackground(Void... paramArrayOfString) {
			if (DEBUG)
				Log.d(TAG, "doInBackground()");
			Location location = null;
			try {
				for (int i = 0; i < 3; i++) {
					location = ((TuanTrip) getApplication())
							.getLastKnownLocation();
					if (location != null) {
						long timede = new Date().getTime() - location.getTime();
						if (timede < BestLocationListener.LOCATION_DELTA) {
							break;
						} else if (i == 2) {
							gpsLocation = location;
							location = null;
						} else {
							location = null;
						}
					}

					synchronized (this) {
						this.wait(1000);
					}
					if (!((TuanTrip) getApplication()).isProviderEnabled()) {
						if (DEBUG)
							Log.i(TAG, "gps unable");
						break;
					}
				}
			} catch (Exception e) {
				if (DEBUG)
					Log.i(TAG, e.toString());
			}
			if (location == null) {
				if (DEBUG)
					Log.d(TAG, "gps为空");
				isGps = false;

				try {
					for (int i = 0; i < 2; i++) {
						location = ((TuanTrip) getApplication())
								.getLastKnownLocation1();
						if (location != null) {
							long timede = new Date().getTime()
									- location.getTime();
							if (timede < BestLocationListener.LOCATION_DELTA) {
								if (DEBUG)
									Log.d(TAG, "从android内置网络获取位置信息");
								break;
							} else {
								location = null;
							}
						}

						synchronized (this) {
							this.wait(1000);
						}
						if (!((TuanTrip) getApplication()).isProviderEnabled1()) {
							if (DEBUG)
								Log.i(TAG, "network unable");
							break;
						}
					}
				} catch (Exception e) {
					if (DEBUG)
						Log.i(TAG, e.toString());
				}
			}

			// if (location != null) {
			// Location location11 = getTuanLocation(location);
			// if (location11 != null)
			// location = location11;
			// }

			return location;

		}

		protected void onPostExecute(Location location) {
			if (DEBUG)
				Log.d(TAG, "onPostExecute()");
			if (location == null) {

				// Toast.makeText(NearbyActivity.this,"gps空",Toast.LENGTH_SHORT).show();
				wifis = new WifiInfoManager().getWifiInfo(mainWifi, wifiList);
				cellIDs = new CellIDInfoManager()
						.getCellIDInfo(activity == null ? service : activity);
				Void[] as = new Void[0];
				new CellWifiLocationTask().execute(as);

			} else {

				if (DEBUG)
					Log.d(TAG, "isFinish");

				mLocation = location;
				//
				iLocation.locationTaskPost();
			}

		}

		protected void onCancelled() {
			if (DEBUG)
				Log.d(TAG, "onCancelled()");
			super.onCancelled();
		}

		protected void onPreExecute() {
			if (DEBUG)
				Log.d(TAG, "onPreExecute()");
			//
			iLocation.locationTaskPre();
		}
	}

	class CellWifiLocationTask extends AsyncTask<Void, Void, Location> {
		private static final String TAG = "CellWifiLocationTask";
		private Exception mReason;

		public CellWifiLocationTask() {
		}

		protected Location doInBackground(Void... paramArrayOfString) {
			if (DEBUG)
				Log.d(TAG, "doInBackground()");
			Location location = null;

			TuanLocation location1 = callGear(wifis, cellIDs);
			if (location1 != null) {
				location = new Location(LocationManager.NETWORK_PROVIDER);
				location.setLongitude(location1.getLongitude());
				location.setLatitude(location1.getLatitude());
			}
			if (location != null && location.getAccuracy() > 500) {
				location = null;
			}

			if (location == null) {
				location = gpsLocation;
			} else if (location != null && gpsLocation != null) {
				float distance = gpsLocation.distanceTo(location);
				if (distance < 500)
					location = gpsLocation;
			}

			return location;

		}

		protected void onPostExecute(Location location) {

			if (DEBUG)
				Log.d(TAG, "onPostExecute()");

			if (location == null) {

				if (gpsLocation != null) {
					location = gpsLocation;
					mLocation = location;

					iLocation.locationTaskPost();

				} else {

					iLocation.getNoLocation();

					return;
				}

			} else {

				mLocation = location;

				iLocation.locationTaskPost();

			}

		}

		protected void onCancelled() {
			if (DEBUG)
				Log.d(TAG, "onCancelled()");
			super.onCancelled();
		}

		protected void onPreExecute() {
			if (DEBUG)
				Log.d(TAG, "onPreExecute()");

		}
	}

	public synchronized Location getTuanLocation(Location location) {
		Location location1 = callGaoWithGeo(location,
				activity == null ? service : activity);
		if (location1 == null)
			location1 = location;

		Location location2 = LocationUtil.callGearWithGeo3(location1);
		if (location2 == null)
			location2 = LocationUtil.callGearWithGeo2(location1);

		if (location2 == null)
			location2 = LocationUtil.callGearWithGeo1(location1);

		return location2;

	}

	public synchronized Location getTuanLocationWithGao(Location location) {
		Location location1 = callGaoWithGeo(location,
				activity == null ? service : activity);
		return location1;

	}

	public synchronized Location getTuanLocationWithGear(Location location) {
		Location location2 = LocationUtil.callGearWithGeo3(location);
		if (location2 == null)
			location2 = LocationUtil.callGearWithGeo2(location);
		if (location2 == null)
			location2 = LocationUtil.callGearWithGeo1(location);

		return location2;

	}

	public void requestLocationUpdates() {
		((TuanTrip) getApplication())
				.requestLocationUpdates(mSearchLocationObserver);
		((TuanTrip) getApplication()).requestLocationUpdates1();
		if (activity != null)
			activity.registerReceiver(receiverWifi, new IntentFilter(
					WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
		else
			service.registerReceiver(receiverWifi, new IntentFilter(
					WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
		mainWifi.startScan();
	}

	public void removeLocationUpdates() {
		if (activity != null)
			activity.unregisterReceiver(receiverWifi);
		else
			service.unregisterReceiver(receiverWifi);
		((TuanTrip) getApplication())
				.removeLocationUpdates(mSearchLocationObserver);
		((TuanTrip) getApplication()).removeLocationUpdates1();
	}

	private class SearchLocationObserver implements Observer {

		@Override
		public void update(Observable observable, Object data) {
			final Location location = (Location) data;
			mLocation = location;

			iLocation.updateLocation();

		}
	}

	public Location getmLocation() {
		return this.mLocation;
	}

	public void setmLocation(Location mLocation) {
		this.mLocation = mLocation;
	}

	public boolean getIsGps() {
		return this.isGps;
	}

	public void setIsGps(boolean isGps) {
		this.isGps = isGps;
	}

	public Application getApplication() {
		if (activity != null)
			return activity.getApplication();
		else
			return service.getApplication();
	}
}
