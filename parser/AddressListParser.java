package com.tudaidai.tuantrip.parser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.tudaidai.tuantrip.types.AddrInfo;
import com.tudaidai.tuantrip.types.AddrListResult;

public class AddressListParser extends AbstractParser<AddrListResult> {

	@Override
	public AddrListResult parse(JSONObject json) throws JSONException {

		AddrListResult aListResult = new AddrListResult();

		JSONObject httpResultObject = json.getJSONObject("HttpResult");
		if (httpResultObject.has("Status")) {
			aListResult.mHttpResult.setStat(Integer.parseInt(httpResultObject
					.getString("Status")));
		}
		if (httpResultObject.has("Info")) {
			aListResult.mHttpResult.setMessage(httpResultObject
					.getString("Info"));
		}

		if (json.has("AddressS")) {
			JSONArray address = json.getJSONObject("AddressS").getJSONArray(
					"Address");
			for (int i = 0; i < address.length(); i++) {
				AddrInfo addrInfo = new AddrInfo();
				JSONObject jObject = address.getJSONObject(i);
				if (jObject.has("Aid")) {
					addrInfo.setAid(Integer.parseInt(jObject.getString("Aid")));
				}
				if (jObject.has("Province")) {
					addrInfo.setProvince(jObject.getString("Province"));
				}
				if (jObject.has("City")) {
					addrInfo.setCity(jObject.getString("City"));
				}
				if (jObject.has("Area")) {
					addrInfo.setArea(jObject.getString("Area"));
				}
				if (jObject.has("House")) {
					addrInfo.setHouse(jObject.getString("House"));
				}
				if (jObject.has("ConsiName")) {
					addrInfo.setConsiName(jObject.getString("ConsiName"));
				}
				if (jObject.has("Phone")) {
					addrInfo.setPhone(jObject.getString("Phone"));
				}
				if (jObject.has("Tel")) {
					addrInfo.setTel(jObject.getString("Tel"));
				}
				if (jObject.has("Zip")) {
					addrInfo.setZip(jObject.getString("Zip"));
				}

				aListResult.mGroup.add(addrInfo);
			}
		}

		return aListResult;
	}

}