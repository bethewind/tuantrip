package com.tudaidai.tuantrip.parser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.tudaidai.tuantrip.types.HotelOrder;
import com.tudaidai.tuantrip.types.HotelOrderListResult;

public class HotelOrderParser extends AbstractParser<HotelOrderListResult> {

	@Override
	public HotelOrderListResult parse(JSONObject json) throws JSONException {

		HotelOrderListResult aListResult = new HotelOrderListResult();

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

		if (json.has("hotelorders")) {
			JSONObject jObject = json.getJSONObject("hotelorders");
			if (jObject.has("total")) {
				aListResult.setTotla(Integer.parseInt(jObject
						.getString("total")));
			}
			if (jObject.has("order")) {
				JSONArray orderArray = jObject.getJSONArray("order");
				for (int i = 0; i < orderArray.length(); i++) {
					HotelOrder order = new HotelOrder();
					JSONObject jObject2 = orderArray.getJSONObject(i);

					if (jObject2.has("oid")) {
						order.setOid(jObject2.getString("oid"));
					}
					if (jObject2.has("name")) {
						order.setName(jObject2.getString("name"));
					}
					if (jObject2.has("cometime")) {
						order.setCometime(jObject2.getString("cometime"));
					}
					if (jObject2.has("detail")) {
						order.setDetail(jObject2.getString("detail"));
					}

					aListResult.mGroup.add(order);
				}
			}
		}

		return aListResult;
	}

}