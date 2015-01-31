package com.tudaidai.tuantrip.parser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.tudaidai.tuantrip.types.Cities;
import com.tudaidai.tuantrip.types.City;

public class CityParser extends AbstractParser<Cities> {

	@Override
	public Cities parse(JSONObject json) throws JSONException {

		Cities aListResult = new Cities();

		JSONObject httpResultObject = json.getJSONObject("HttpResult");
		if (httpResultObject.has("Status")) {
			aListResult.mHttpResult.setStat(Integer.parseInt(httpResultObject
					.getString("Status")));
		}
		if (httpResultObject.has("Info")) {
			aListResult.mHttpResult.setMessage(httpResultObject
					.getString("Info"));
		}

		if (json.has("Citys")) {
			JSONArray jArray = json.getJSONObject("Citys").getJSONArray("City");

			for (int i = 0; i < jArray.length(); i++) {
				City city = new City();
				JSONObject jObject2 = jArray.getJSONObject(i);
				if (jObject2.has("Name")) {
					city.setName(jObject2.getString("Name"));
				}
				if (jObject2.has("CnName")) {
					city.setCnName(jObject2.getString("CnName"));
				}
				aListResult.mCities.add(city);
			}

		}

		return aListResult;
	}

}