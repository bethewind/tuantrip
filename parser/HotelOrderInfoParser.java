package com.tudaidai.tuantrip.parser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.tudaidai.tuantrip.types.DatePrice;
import com.tudaidai.tuantrip.types.Group;
import com.tudaidai.tuantrip.types.HotelOrderInfoResult;
import com.tudaidai.tuantrip.types.House;

public class HotelOrderInfoParser extends AbstractParser<HotelOrderInfoResult> {

	@Override
	public HotelOrderInfoResult parse(JSONObject json) throws JSONException {

		HotelOrderInfoResult aListResult = new HotelOrderInfoResult();

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
			if (jObject.has("HouseS")) {
				JSONArray jArrayHouseS = jObject.getJSONArray("HouseS");
				Group<House> houseGroup = new Group<House>();
				for (int i = 0; i < jArrayHouseS.length(); i++) {
					JSONObject jObjectHouse = jArrayHouseS.getJSONObject(i);
					houseGroup.add(getHouse(jObjectHouse));
				}
				aListResult.mOrderInfo.setHouseGroup(houseGroup);
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
				if (user.has("Id_type")) {
					aListResult.mOrderInfo.setIdType(Integer.parseInt(user
							.getString("Id_type")));
				}
				if (user.has("Id_number")) {
					aListResult.mOrderInfo.setIdNumber(user
							.getString("Id_number"));
				}

				if (user.has("House")) {
					JSONObject jObjectHouse = user.getJSONObject("House");
					aListResult.mOrderInfo
							.setUserBuyHouse(getHouse(jObjectHouse));
				}
			}

		}

		return aListResult;
	}

	private House getHouse(JSONObject jObjectHouse) throws JSONException {
		House house = new House();
		if (jObjectHouse.has("Type_id")) {
			house.setTypeId(jObjectHouse.getString("Type_id"));
		}
		if (jObjectHouse.has("Type_name")) {
			house.setTypeName(jObjectHouse.getString("Type_name"));
		}
		if (jObjectHouse.has("PriceS")) {
			JSONArray jArrayDatePrice = jObjectHouse.getJSONArray("PriceS");
			Group<DatePrice> datePriceGroup = new Group<DatePrice>();
			for (int j = 0; j < jArrayDatePrice.length(); j++) {
				DatePrice datePrice = new DatePrice();
				JSONObject jObjectDatePrice = jArrayDatePrice.getJSONObject(j);
				if (jObjectDatePrice.has("Date")) {
					datePrice.setDate(jObjectDatePrice.getString("Date"));
				}
				if (jObjectDatePrice.has("Price")) {
					datePrice.setPrice(jObjectDatePrice.getString("Price"));
				}
				datePriceGroup.add(datePrice);
			}
			house.setDatePrice(datePriceGroup);
		}

		return house;
	}

}