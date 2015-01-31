package com.tudaidai.tuantrip.parser;

import org.json.JSONException;
import org.json.JSONObject;

import com.tudaidai.tuantrip.types.LogOutResult;

public class LogOutParser extends AbstractParser<LogOutResult> {

	@Override
	public LogOutResult parse(JSONObject json) throws JSONException {

		LogOutResult aListResult = new LogOutResult();

		JSONObject httpResultObject = json.getJSONObject("HttpResult");
		if (httpResultObject.has("Status")) {
			aListResult.mHttpResult.setStat(Integer.parseInt(httpResultObject
					.getString("Status")));
		}
		if (httpResultObject.has("Info")) {
			aListResult.mHttpResult.setMessage(httpResultObject
					.getString("Info"));
		}
		return aListResult;
	}

}