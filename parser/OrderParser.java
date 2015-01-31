package com.tudaidai.tuantrip.parser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.tudaidai.tuantrip.types.Order;
import com.tudaidai.tuantrip.types.OrderResult;

public class OrderParser extends AbstractParser<OrderResult> {

	@Override
	public OrderResult parse(JSONObject json) throws JSONException {

		OrderResult aListResult = new OrderResult();

		JSONObject httpResultObject = json.getJSONObject("HttpResult");
		if (httpResultObject.has("Status")) {
			aListResult.mHttpResult.setStat(Integer.parseInt(httpResultObject
					.getString("Status")));
		}
		if (httpResultObject.has("Info")) {
			aListResult.mHttpResult.setMessage(httpResultObject
					.getString("Info"));
		}

		if (json.has("Orders")) {
			JSONObject jObject = json.getJSONObject("Orders");
			if (jObject.has("Total")) {
				aListResult.setTotla(Integer.parseInt(jObject
						.getString("Total")));
			}
			if (jObject.has("Order")) {
				JSONArray orderArray = jObject.getJSONArray("Order");
				for (int i = 0; i < orderArray.length(); i++) {
					Order order = new Order();
					JSONObject jObject2 = orderArray.getJSONObject(i);
					if (jObject2.has("Pid")) {
						order.setPid(jObject2.getString("Pid"));
					}
					if (jObject2.has("Oid")) {
						order.setOid(jObject2.getString("Oid"));
					}
					if (jObject2.has("ShortTitle")) {
						order.setShortTitle(jObject2.getString("ShortTitle"));
					}
					if (jObject2.has("Code")) {
						order.setCode(jObject2.getString("Code"));
					}
					if (jObject2.has("CityName")) {
						order.setCityName(jObject2.getString("CityName"));
					}
					if (jObject2.has("Quantity")) {
						order.setQuantity(jObject2.getString("Quantity"));
					}
					if (jObject2.has("Price")) {
						order.setPrice(jObject2.getString("Price"));
					}
					if (jObject2.has("OrderTime")) {
						order.setOrderTime(jObject2.getString("OrderTime"));
					}
					if (jObject2.has("OrderStatus")) {
						order.setOrderStatus(jObject2.getString("OrderStatus"));
					}
					if (jObject2.has("TravelDate")) {
						order.setTravelDate(jObject2.getString("TravelDate"));
					}
					aListResult.mGroup.add(order);
				}
			}
		}

		return aListResult;
	}

}