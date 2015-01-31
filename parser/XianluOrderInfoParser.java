package com.tudaidai.tuantrip.parser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.tudaidai.tuantrip.types.Group;
import com.tudaidai.tuantrip.types.Xianlu;
import com.tudaidai.tuantrip.types.XianluOrderInfoResult;

public class XianluOrderInfoParser extends
		AbstractParser<XianluOrderInfoResult> {

	@Override
	public XianluOrderInfoResult parse(JSONObject json) throws JSONException {

		XianluOrderInfoResult aListResult = new XianluOrderInfoResult();

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
			if (jObject.has("XianluS")) {
				JSONArray jArrayXianluS = jObject.getJSONArray("XianluS");
				Group<Xianlu> xianluGroup = new Group<Xianlu>();
				for (int i = 0; i < jArrayXianluS.length(); i++) {
					JSONObject jObjectXianlu = jArrayXianluS.getJSONObject(i);
					xianluGroup.add(getXianlu(jObjectXianlu));
				}
				aListResult.mOrderInfo.setXianluGroup(xianluGroup);
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

				if (user.has("Xianlu")) {
					JSONObject jObjectXianlu = user.getJSONObject("Xianlu");
					aListResult.mOrderInfo
							.setUserBuyXianlu(getXianlu(jObjectXianlu));
				}
			}

		}

		return aListResult;
	}

	private Xianlu getXianlu(JSONObject jObjectXianlu) throws JSONException {
		Xianlu xianlu = new Xianlu();
		if (jObjectXianlu.has("Type_id")) {
			xianlu.setTypeId(jObjectXianlu.getString("Type_id"));
		}
		if (jObjectXianlu.has("Travel_date")) {
			xianlu.setTravelDate(jObjectXianlu.getString("Travel_date"));
		}
		if (jObjectXianlu.has("Price")) {
			xianlu.setPrice(Double.parseDouble(jObjectXianlu.getString("Price")));
		}

		return xianlu;
	}

}