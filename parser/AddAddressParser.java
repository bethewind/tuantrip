package com.tudaidai.tuantrip.parser;

import org.json.JSONException;
import org.json.JSONObject;

import com.tudaidai.tuantrip.types.AddAddressResult;

public class AddAddressParser extends AbstractParser<AddAddressResult> {

	@Override
	public AddAddressResult parse(JSONObject json) throws JSONException {

		AddAddressResult aListResult = new AddAddressResult();

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