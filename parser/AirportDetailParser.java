package com.tudaidai.tuantrip.parser;

import org.json.JSONException;
import org.json.JSONObject;

import com.tudaidai.tuantrip.types.AirportDetailResult;

public class AirportDetailParser extends AbstractParser<AirportDetailResult> {

	@Override
	public AirportDetailResult parse(JSONObject json) throws JSONException {

		AirportDetailResult vDetailResult = new AirportDetailResult();

		JSONObject httpResultObject = json.getJSONObject("httpResult");
		if (httpResultObject.has("status")) {
			vDetailResult.mHttpResult.setStat(Integer.parseInt(httpResultObject
					.getString("status")));
		}
		if (httpResultObject.has("info")) {
			vDetailResult.mHttpResult.setMessage(httpResultObject
					.getString("info"));
		}

		if (json.has("airport")) {
			JSONObject hotelJsonObject = json.getJSONObject("airport");
			if (hotelJsonObject.has("id")) {
				vDetailResult.aDetail.setId(hotelJsonObject.getString("id"));
			}

			if (hotelJsonObject.has("serve")) {
				vDetailResult.aDetail.setService(hotelJsonObject
						.getString("serve"));
			}

			if (hotelJsonObject.has("jiaotong")) {
				vDetailResult.aDetail.setJiaotong(hotelJsonObject
						.getString("jiaotong"));
			}

			BaselbsParser.parserComment(hotelJsonObject, vDetailResult.aDetail);

		}

		return vDetailResult;
	}

}