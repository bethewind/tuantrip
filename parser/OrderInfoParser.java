package com.tudaidai.tuantrip.parser;

import org.json.JSONException;
import org.json.JSONObject;

import com.tudaidai.tuantrip.types.OrderInfoResult;

public class OrderInfoParser extends AbstractParser<OrderInfoResult> {

	@Override
	public OrderInfoResult parse(JSONObject json) throws JSONException {

		OrderInfoResult aListResult = new OrderInfoResult();

		JSONObject httpResultObject = json.getJSONObject("HttpResult");
		if (httpResultObject.has("Status")) {
			aListResult.mHttpResult.setStat(Integer.parseInt(httpResultObject
					.getString("Status")));
		}
		if (httpResultObject.has("Info")) {
			aListResult.mHttpResult.setMessage(httpResultObject
					.getString("Info"));
		}

		if (json.has("OrderInfo")) {
			JSONObject jObject = json.getJSONObject("OrderInfo");
			if (jObject.has("ShortTitle")) {
				aListResult.mOrderInfo.setShortTitle(jObject
						.getString("ShortTitle"));
			}
			if (jObject.has("HaveAddress")) {
				aListResult.mOrderInfo.setHaveAddress(Integer.parseInt(jObject
						.getString("HaveAddress")));
			}
			if (jObject.has("IsFreight")) {
				aListResult.mOrderInfo.setIsFreight(Integer.parseInt(jObject
						.getString("IsFreight")));
			}
			if (jObject.has("Freight_num")) {
				aListResult.mOrderInfo.setFreightNum(Integer.parseInt(jObject
						.getString("Freight_num")));
			}
			if (jObject.has("Freight_fee")) {
				aListResult.mOrderInfo.setFreightFee(Double.parseDouble(jObject
						.getString("Freight_fee")));
			}
			if (jObject.has("Travel_date")) {
				aListResult.mOrderInfo.setTravelDate(jObject
						.getString("Travel_date"));
			}
			if (jObject.has("Is_travel_date")) {
				aListResult.mOrderInfo.setIsTravelDate(Integer.parseInt(jObject
						.getString("Is_travel_date")));
			}

			if (jObject.has("User")) {
				JSONObject user = jObject.getJSONObject("User");
				if (user.has("Uname")) {
					aListResult.mOrderInfo.setUname(user.getString("Uname"));
				}
				if (user.has("Phone")) {
					aListResult.mOrderInfo.setPhone(user.getString("Phone"));
				}
				if (user.has("Quantity")) {
					aListResult.mOrderInfo.setQuantity(Integer.parseInt(user
							.getString("Quantity")));
				}
				if (user.has("Remaik")) {
					aListResult.mOrderInfo.setRemaik(user.getString("Remaik"));
				}
				if (user.has("Balance")) {
					aListResult.mOrderInfo.setBalance(Double.parseDouble(user
							.getString("Balance")));
				}
				if (user.has("DateBind")) {
					aListResult.mOrderInfo.setDateBind(user
							.getString("DateBind"));
				}
				if (user.has("Id_type")) {
					aListResult.mOrderInfo.setIdType(Integer.parseInt(user
							.getString("Id_type")));
				}
				if (user.has("Id_number")) {
					aListResult.mOrderInfo.setIdNumber(user
							.getString("Id_number"));
				}

				if (user.has("Address")) {
					JSONObject address = user.getJSONObject("Address");
					if (address.has("AId")) {
						aListResult.mOrderInfo.setAId(Integer.parseInt(address
								.getString("AId")));
					}
					if (address.has("AName")) {
						aListResult.mOrderInfo.setAName(address
								.getString("AName"));
					}
				}
			}

		}

		return aListResult;
	}

}