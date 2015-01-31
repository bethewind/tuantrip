package com.tudaidai.tuantrip.parser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.tudaidai.tuantrip.TuanTripSettings;
import com.tudaidai.tuantrip.types.Airport;
import com.tudaidai.tuantrip.types.AirportResult;

public class AirportResultParser extends AbstractParser<AirportResult> {

	@Override
	public AirportResult parse(JSONObject json) throws JSONException {

		AirportResult aListResult = new AirportResult();

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

		if (json.has("airports")) {
			JSONArray address = json.getJSONArray("airports");
			for (int i = 0; i < address.length(); i++) {
				Airport productShort = new Airport();
				JSONObject jObject = address.getJSONObject(i);
				if (jObject.has("id")) {
					productShort
							.setId(Integer.parseInt(jObject.getString("id")));
				}

				if (jObject.has("imageurl")) {
					String image = jObject.getString("imageurl");
					if (!image.startsWith("http://")) {
						image = TuanTripSettings.SITE + image;
					}

					productShort.setImageUrl(image);
				}
				if (jObject.has("name")) {
					productShort.setName(jObject.getString("name"));
				}

				if (jObject.has("address")) {
					productShort.setAddress(jObject.getString("address"));
				}
				if (jObject.has("jingdu")) {
					productShort.setJingdu(Double.parseDouble(jObject
							.getString("jingdu")));
				}
				if (jObject.has("weidu")) {
					productShort.setWeidu(Double.parseDouble(jObject
							.getString("weidu")));
				}

				aListResult.mGroup.add(productShort);
			}
		}

		return aListResult;
	}

}