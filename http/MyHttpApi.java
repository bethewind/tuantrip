package com.tudaidai.tuantrip.http;

import java.io.IOException;

import org.apache.http.ParseException;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.xmlpull.v1.XmlPullParserException;

import com.tudaidai.tuantrip.error.TuanTripException;
import com.tudaidai.tuantrip.parser.Parser;
import com.tudaidai.tuantrip.types.TuanTripType;

public class MyHttpApi extends AbstractHttpApi {

	public MyHttpApi(DefaultHttpClient httpClient, String clientVersion) {
		super(httpClient, clientVersion);
		// TODO Auto-generated constructor stub
	}

	@Override
	public TuanTripType doHttpRequest(HttpRequestBase httpRequest,
			Parser<? extends TuanTripType> parser) throws IOException,
			ParseException, TuanTripException, XmlPullParserException {
		return super.executeHttpRequest(httpRequest, parser);
	}

	@Override
	public TuanTripType doHttpRequest(HttpRequestBase httpRequest,
			Parser<? extends TuanTripType> parser, int type, String charset)
			throws IOException, ParseException, TuanTripException,
			XmlPullParserException {
		return super.executeHttpRequest(httpRequest, parser, type, charset);
	}
}
