package com.tudaidai.tuantrip.parser;

import org.json.JSONException;
import org.json.JSONObject;

import com.tudaidai.tuantrip.types.BaseResult;

public class BaseParser extends AbstractParser<BaseResult> {

	@Override
	public BaseResult parse(JSONObject json) throws JSONException {

		BaseResult aListResult = new BaseResult();

		if (json.has("HttpResult")) {
			JSONObject httpResultObject = json.getJSONObject("HttpResult");
			if (httpResultObject.has("Status")) {
				aListResult.mHttpResult.setStat(Integer
						.parseInt(httpResultObject.getString("Status")));
			}
			if (httpResultObject.has("Info")) {
				aListResult.mHttpResult.setMessage(httpResultObject
						.getString("Info"));
			}
		} else if (json.has("httpResult")) {
			JSONObject httpResultObject = json.getJSONObject("httpResult");
			if (httpResultObject.has("status")) {
				aListResult.mHttpResult.setStat(Integer
						.parseInt(httpResultObject.getString("status")));
			}
			if (httpResultObject.has("info")) {
				aListResult.mHttpResult.setMessage(httpResultObject
						.getString("info"));
			}
		}
		return aListResult;
	}

}