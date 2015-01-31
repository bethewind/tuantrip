package com.tudaidai.tuantrip.parser;

import org.json.JSONException;
import org.json.JSONObject;

import com.tudaidai.tuantrip.types.HttpResult;

public class HttpResultParser extends AbstractParser<HttpResult> {

	@Override
	public HttpResult parse(JSONObject json) throws JSONException {

		HttpResult mHttpResult = new HttpResult();

		if (json.has("httpResult")) {
			JSONObject httpResultObject = json.getJSONObject("httpResult");
			if (httpResultObject.has("status")) {
				mHttpResult.setStat(Integer.parseInt(httpResultObject
						.getString("status")));
			} else if (httpResultObject.has("Status")) {
				mHttpResult.setStat(Integer.parseInt(httpResultObject
						.getString("Status")));
			}
			if (httpResultObject.has("info")) {
				mHttpResult.setMessage(httpResultObject.getString("info"));
			} else if (httpResultObject.has("Info")) {
				mHttpResult.setMessage(httpResultObject.getString("Info"));
			}
		} else if (json.has("httpresult")) {
			JSONObject httpResultObject = json.getJSONObject("httpresult");
			if (httpResultObject.has("status")) {
				mHttpResult.setStat(Integer.parseInt(httpResultObject
						.getString("status")));
			} else if (httpResultObject.has("Status")) {
				mHttpResult.setStat(Integer.parseInt(httpResultObject
						.getString("Status")));
			}
			if (httpResultObject.has("info")) {
				mHttpResult.setMessage(httpResultObject.getString("info"));
			} else if (httpResultObject.has("Info")) {
				mHttpResult.setMessage(httpResultObject.getString("Info"));
			}
		}

		return mHttpResult;
	}

}