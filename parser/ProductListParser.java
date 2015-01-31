package com.tudaidai.tuantrip.parser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.tudaidai.tuantrip.types.ProductListResult;
import com.tudaidai.tuantrip.types.ProductShort;

public class ProductListParser extends AbstractParser<ProductListResult> {

	@Override
	public ProductListResult parse(JSONObject json) throws JSONException {

		ProductListResult aListResult = new ProductListResult();

		JSONObject httpResultObject = json.getJSONObject("HttpResult");
		if (httpResultObject.has("Status")) {
			aListResult.mHttpResult.setStat(Integer.parseInt(httpResultObject
					.getString("Status")));
		}
		if (httpResultObject.has("Info")) {
			aListResult.mHttpResult.setMessage(httpResultObject
					.getString("Info"));
		}

		if (json.has("Products")) {
			JSONArray address = json.getJSONObject("Products").getJSONArray(
					"ProductShort");
			for (int i = 0; i < address.length(); i++) {
				ProductShort productShort = new ProductShort();
				JSONObject jObject = address.getJSONObject(i);
				if (jObject.has("Pid")) {
					productShort.setPid(Integer.parseInt(jObject
							.getString("Pid")));
				}
				if (jObject.has("ShortTitle")) {
					productShort.setShortTitle(jObject.getString("ShortTitle"));
				}
				if (jObject.has("ImageUrl")) {
					productShort.setImageUrl(jObject.getString("ImageUrl"));
				}
				if (jObject.has("Price")) {
					productShort.setPrice(jObject.getString("Price"));
				}
				if (jObject.has("Bought")) {
					productShort.setBought(jObject.getString("Bought"));
				}

				aListResult.mProductList.add(productShort);
			}
		}

		return aListResult;
	}

}