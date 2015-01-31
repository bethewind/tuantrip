package com.tudaidai.tuantrip.parser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.tudaidai.tuantrip.TuanTripSettings;
import com.tudaidai.tuantrip.types.Nearby;
import com.tudaidai.tuantrip.types.NearbyResult;

public class NearbyResultParser extends AbstractParser<NearbyResult> {

	@Override
	public NearbyResult parse(JSONObject json) throws JSONException {

		NearbyResult aListResult = new NearbyResult();

		if (json.has("total")) {
			aListResult.setTotla(Integer.parseInt(json.getString("total")));
		}

		JSONObject httpResultObject = json.getJSONObject("httpresult");
		if (httpResultObject.has("status")) {
			aListResult.mHttpResult.setStat(Integer.parseInt(httpResultObject
					.getString("status")));
		}
		if (httpResultObject.has("info")) {
			aListResult.mHttpResult.setMessage(httpResultObject
					.getString("info"));
		}

		if (json.has("nearbys")) {
			JSONArray address = json.getJSONArray("nearbys");
			for (int i = 0; i < address.length(); i++) {
				Nearby productShort = new Nearby();
				JSONObject jObject = address.getJSONObject(i);
				if (jObject.has("lid")) {
					productShort.setLid(Integer.parseInt(jObject
							.getString("lid")));
				}
				if (jObject.has("fid")) {
					productShort.setFid(Integer.parseInt(jObject
							.getString("fid")));
				}
				if (jObject.has("name")) {
					productShort.setName(jObject.getString("name"));
				}
				if (jObject.has("imageurl")) {
					String image = jObject.getString("imageurl");
					if (!image.startsWith("http://")) {
						image = TuanTripSettings.SITE + image;
					}

					productShort.setImageUrl(image);
				}
				if (jObject.has("type")) {
					productShort.setType(jObject.getString("type"));
				}
				if (jObject.has("address")) {
					productShort.setAddress(jObject.getString("address"));
				}
				if (jObject.has("jingdu")) {
					try {
						productShort.setJingdu(Double.parseDouble(jObject
								.getString("jingdu")));
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
				if (jObject.has("weidu")) {
					try {
						productShort.setWeidu(Double.parseDouble(jObject
								.getString("weidu")));
					} catch (Exception e) {
						// TODO: handle exception
					}
				}

				aListResult.mGroup.add(productShort);
			}
		}

		return aListResult;
	}

}