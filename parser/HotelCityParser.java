package com.tudaidai.tuantrip.parser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.tudaidai.tuantrip.types.Cities;
import com.tudaidai.tuantrip.types.City;

public class HotelCityParser extends AbstractParser<Cities> {

	@Override
	public Cities parse(JSONObject json) throws JSONException {

		Cities aListResult = new Cities();

		JSONObject httpResultObject = json.getJSONObject("httpresult");
		if (httpResultObject.has("status")) {
			aListResult.mHttpResult.setStat(Integer.parseInt(httpResultObject
					.getString("status")));
		}
		if (httpResultObject.has("info")) {
			aListResult.mHttpResult.setMessage(httpResultObject
					.getString("info"));
		}

		if (json.has("citys")) {
			JSONArray jArray = json.getJSONObject("citys").getJSONArray("city");

			for (int i = 0; i < jArray.length(); i++) {
				City city = new City();
				String jObject2 = jArray.getString(i);
				city.setCnName(jObject2);

				aListResult.mCities.add(city);
			}

		}

		return aListResult;
	}

}