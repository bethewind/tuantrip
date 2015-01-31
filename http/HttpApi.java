package com.tudaidai.tuantrip.http;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.xmlpull.v1.XmlPullParserException;

import com.tudaidai.tuantrip.error.TuanTripException;
import com.tudaidai.tuantrip.error.TuanTripParseException;
import com.tudaidai.tuantrip.parser.Parser;
import com.tudaidai.tuantrip.types.TuanTripType;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public interface HttpApi {

	abstract public TuanTripType doHttpRequest(HttpRequestBase httpRequest,
			Parser<? extends TuanTripType> parser) throws IOException,
			ParseException, TuanTripException, XmlPullParserException;

	public TuanTripType doHttpRequest(HttpRequestBase httpRequest,
			Parser<? extends TuanTripType> parser, int type, String charset)
			throws IOException, ParseException, TuanTripException,
			XmlPullParserException;

	abstract public String doHttpPost(String url,
			NameValuePair... nameValuePairs) throws TuanTripParseException,
			TuanTripException, IOException;

	abstract public HttpGet createHttpGet(String url,
			NameValuePair... nameValuePairs);

	abstract public HttpPost createHttpPost(String url,
			NameValuePair... nameValuePairs);

	abstract public HttpURLConnection createHttpURLConnectionPost(URL url,
			String boundary) throws IOException;

	public HttpResponse executeHttpRequest(HttpRequestBase httpRequest)
			throws IOException;
}
