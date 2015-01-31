package com.tudaidai.tuantrip.parser;

import org.json.JSONException;
import org.json.JSONObject;

import com.tudaidai.tuantrip.types.HotelOrderResult;

public class HotelOrderResultParser extends AbstractParser<HotelOrderResult> {

	@Override
	public HotelOrderResult parse(JSONObject json) throws JSONException {

		HotelOrderResult hResult = new HotelOrderResult();

		JSONObject httpResultObject = json.getJSONObject("httpresult");
		if (httpResultObject.has("status")) {
			hResult.mHttpResult.setStat(Integer.parseInt(httpResultObject
					.getString("status")));
		} else if (httpResultObject.has("Status")) {
			hResult.mHttpResult.setStat(Integer.parseInt(httpResultObject
					.getString("Status")));
		}
		if (httpResultObject.has("info")) {
			hResult.mHttpResult.setMessage(httpResultObject.getString("info"));
		} else if (httpResultObject.has("Info")) {
			hResult.mHttpResult.setMessage(httpResultObject.getString("Info"));
		}

		if (json.has("hotelorderInfo")) {
			JSONObject hotelJsonObject = json.getJSONObject("hotelorderInfo");
			if (hotelJsonObject.has("date")) {
				hResult.sHotelOrderInfo.setDate(hotelJsonObject
						.getString("date"));
			}
			if (hotelJsonObject.has("fangxing")) {
				hResult.sHotelOrderInfo.setFangxing(hotelJsonObject
						.getString("fangxing"));
			}
			if (hotelJsonObject.has("kuandai")) {
				hResult.sHotelOrderInfo.setKuandai(hotelJsonObject
						.getString("kuandai"));
			}
			if (hotelJsonObject.has("city")) {
				hResult.sHotelOrderInfo.setCity(hotelJsonObject
						.getString("city"));
			}
			if (hotelJsonObject.has("price")) {
				hResult.sHotelOrderInfo.setPrice(Double
						.parseDouble(hotelJsonObject.getString("price")));
			}

		}

		return hResult;
	}

}