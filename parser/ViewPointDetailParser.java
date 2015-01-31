package com.tudaidai.tuantrip.parser;

import org.json.JSONException;
import org.json.JSONObject;

import com.tudaidai.tuantrip.types.ViewpointDetailResult;

public class ViewPointDetailParser extends
		AbstractParser<ViewpointDetailResult> {

	@Override
	public ViewpointDetailResult parse(JSONObject json) throws JSONException {

		ViewpointDetailResult vDetailResult = new ViewpointDetailResult();

		JSONObject httpResultObject = json.getJSONObject("httpResult");
		if (httpResultObject.has("status")) {
			vDetailResult.mHttpResult.setStat(Integer.parseInt(httpResultObject
					.getString("status")));
		}
		if (httpResultObject.has("info")) {
			vDetailResult.mHttpResult.setMessage(httpResultObject
					.getString("info"));
		}

		if (json.has("viewpoint")) {
			JSONObject hotelJsonObject = json.getJSONObject("viewpoint");
			if (hotelJsonObject.has("vid")) {
				vDetailResult.viewpointDetail.setVid(hotelJsonObject
						.getString("vid"));
			}

			if (hotelJsonObject.has("menpiao")) {
				vDetailResult.viewpointDetail.setMenpiao(hotelJsonObject
						.getString("menpiao"));
			}

			if (hotelJsonObject.has("jiaotong")) {
				vDetailResult.viewpointDetail.setJiaotong(hotelJsonObject
						.getString("jiaotong"));
			}

			BaselbsParser.parserComment(hotelJsonObject,
					vDetailResult.viewpointDetail);

		}

		return vDetailResult;
	}

}