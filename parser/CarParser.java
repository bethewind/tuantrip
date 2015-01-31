package com.tudaidai.tuantrip.parser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.tudaidai.tuantrip.types.Car;
import com.tudaidai.tuantrip.types.CarResult;

public class CarParser extends AbstractParser<CarResult> {

	@Override
	public CarResult parse(JSONObject json) throws JSONException {

		CarResult aListResult = new CarResult();

		JSONObject httpResultObject = json.getJSONObject("httpresult");
		if (httpResultObject.has("status")) {
			aListResult.mHttpResult.setStat(Integer.parseInt(httpResultObject
					.getString("status")));
		} else if (httpResultObject.has("Status")) {
			aListResult.mHttpResult.setStat(Integer.parseInt(httpResultObject
					.getString("Status")));
		}
		if (httpResultObject.has("info")) {
			aListResult.mHttpResult.setMessage(httpResultObject
					.getString("info"));
		} else if (httpResultObject.has("Info")) {
			aListResult.mHttpResult.setMessage(httpResultObject
					.getString("Info"));
		}

		if (json.has("cars")) {
			JSONArray jArray = json.getJSONArray("cars");

			for (int i = 0; i < jArray.length(); i++) {
				Car car = new Car();
				JSONObject jObject2 = jArray.getJSONObject(i);
				if (jObject2.has("id")) {
					car.setId(Integer.parseInt(jObject2.getString("id")));
				}
				if (jObject2.has("name")) {
					car.setName(jObject2.getString("name"));
				}
				if (jObject2.has("imageurl")) {
					car.setImageUrl(jObject2.getString("imageurl"));
				}
				if (jObject2.has("address")) {
					car.setAddress(jObject2.getString("address"));
				}
				if (jObject2.has("phone")) {
					car.setPhone(jObject2.getString("phone"));
				}
				if (jObject2.has("city")) {
					car.setCity(jObject2.getString("city"));
				}
				aListResult.mGroup.add(car);
			}

		}

		return aListResult;
	}

}