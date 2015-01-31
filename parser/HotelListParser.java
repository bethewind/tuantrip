package com.tudaidai.tuantrip.parser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.tudaidai.tuantrip.TuanTripSettings;
import com.tudaidai.tuantrip.types.HotelListResult;
import com.tudaidai.tuantrip.types.HotelShort;

public class HotelListParser extends AbstractParser<HotelListResult> {

	@Override
	public HotelListResult parse(JSONObject json) throws JSONException {

		HotelListResult aListResult = new HotelListResult();
		if (json.has("TuanHotel")) {
			JSONObject jObject = json.getJSONObject("TuanHotel");
			if (jObject.has("Pid")) {
				try {
					aListResult.productShort.setPid(Integer.parseInt(jObject
							.getString("Pid")));
				} catch (NumberFormatException e) {
					aListResult.productShort.setPid(0);
				}
			}
			if (jObject.has("ShortTitle")) {
				aListResult.productShort.setShortTitle(jObject
						.getString("ShortTitle"));
			}
			if (jObject.has("ImageUrl")) {
				String image = jObject.getString("ImageUrl");
				// if(!image.startsWith("http://"))
				// {
				// image = TuanTripSettings.SITE+image;
				// }
				// image = image.replaceAll("upload.tuantrip.com",
				// "upload.tudaidai.com");

				aListResult.productShort.setImageUrl(image);
			}
			if (jObject.has("Price")) {
				aListResult.productShort.setPrice(jObject.getString("Price"));
			}
			if (jObject.has("Bought")) {
				aListResult.productShort.setBought(jObject.getString("Bought"));
			}
		}

		if (json.has("Total")) {
			aListResult.setTotla(Integer.parseInt(json.getString("Total")));
		}

		JSONObject httpResultObject = json.getJSONObject("HttpResult");
		if (httpResultObject.has("Status")) {
			aListResult.mHttpResult.setStat(Integer.parseInt(httpResultObject
					.getString("Status")));
		}
		if (httpResultObject.has("Info")) {
			aListResult.mHttpResult.setMessage(httpResultObject
					.getString("Info"));
		}

		if (json.has("Hotels")) {
			JSONArray address = json.getJSONArray("Hotels");
			for (int i = 0; i < address.length(); i++) {
				HotelShort productShort = new HotelShort();
				JSONObject jObject = address.getJSONObject(i);
				if (jObject.has("Hid")) {
					productShort.setHid(Integer.parseInt(jObject
							.getString("Hid")));
				}
				if (jObject.has("ShortTitle")) {
					productShort.setShortTitle(jObject.getString("ShortTitle"));
				}
				if (jObject.has("ImageUrl")) {
					String image = jObject.getString("ImageUrl");
					if (!image.startsWith("http://")) {
						image = TuanTripSettings.SITE + image;
					}
					image = image.replaceAll("upload.tuantrip.com",
							"upload.tudaidai.com");

					productShort.setImageUrl(image);
				}
				if (jObject.has("Price")) {
					productShort.setPrice(jObject.getString("Price"));
				}
				if (jObject.has("Address")) {
					productShort.setAddress(jObject.getString("Address"));
				}
				if (jObject.has("Jingdu")) {
					try {
						productShort.setJingdu(Double.parseDouble(jObject
								.getString("Jingdu")));
					} catch (NumberFormatException e) {
						// TODO: handle exception
					}
				}
				if (jObject.has("Weidu")) {
					try {
						productShort.setWeidu(Double.parseDouble(jObject
								.getString("Weidu")));
					} catch (NumberFormatException e) {
						// TODO: handle exception
					}
				}

				aListResult.mGroup.add(productShort);
			}
		}

		return aListResult;
	}

}