package com.tudaidai.tuantrip.parser;

import org.json.JSONException;
import org.json.JSONObject;

import com.tudaidai.tuantrip.types.UserResult;

public class UserParser extends AbstractParser<UserResult> {

	@Override
	public UserResult parse(JSONObject json) throws JSONException {

		UserResult userResult = new UserResult();

		JSONObject httpResultObject = json.getJSONObject("HttpResult");
		if (httpResultObject.has("Status")) {
			userResult.mHttpResult.setStat(Integer.parseInt(httpResultObject
					.getString("Status")));
		}
		if (httpResultObject.has("Info")) {
			userResult.mHttpResult.setMessage(httpResultObject
					.getString("Info"));
		}

		if (json.has("User")) {
			JSONObject user = json.getJSONObject("User");
			if (user.has("Uid")) {
				userResult.mUser
						.setUid(Integer.parseInt(user.getString("Uid")));
			}
			if (user.has("Phone")) {
				userResult.mUser.setPhone(user.getString("Phone"));
			}
			if (user.has("Uname")) {
				userResult.mUser.setUname(user.getString("Uname"));
			}
			if (user.has("Email")) {
				userResult.mUser.setEmail(user.getString("Email"));
			}
			if (user.has("Realname")) {
				userResult.mUser.setRealName(user.getString("Realname"));
			}
			if (user.has("IdType")) {
				userResult.mUser.setIdType(Integer.parseInt(user
						.getString("IdType")));
			}
			if (user.has("IdNumber")) {
				userResult.mUser.setIdNumber(user.getString("IdNumber"));
			}
		}

		return userResult;
	}

}