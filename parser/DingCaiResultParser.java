package com.tudaidai.tuantrip.parser;

import org.json.JSONException;
import org.json.JSONObject;

import com.tudaidai.tuantrip.types.DingCaiResult;

public class DingCaiResultParser extends AbstractParser<DingCaiResult> {

	@Override
	public DingCaiResult parse(JSONObject json) throws JSONException {

		DingCaiResult mListResult = new DingCaiResult();

		JSONObject httpResultObject = json.getJSONObject("httpresult");
		if (httpResultObject.has("Status")) {
			mListResult.mHttpResult.setStat(Integer.parseInt(httpResultObject
					.getString("Status")));
		} else if (httpResultObject.has("status")) {
			mListResult.mHttpResult.setStat(Integer.parseInt(httpResultObject
					.getString("status")));
		}
		if (httpResultObject.has("Info")) {
			mListResult.mHttpResult.setMessage(httpResultObject
					.getString("Info"));
		} else if (httpResultObject.has("info")) {
			mListResult.mHttpResult.setMessage(httpResultObject
					.getString("info"));
		}

		if (json.has("comment")) {
			JSONObject cObject = json.getJSONObject("comment");
			if (cObject.has("ding")) {
				mListResult.comment.setDing(cObject.getString("ding"));
			}
			if (cObject.has("cai")) {
				mListResult.comment.setCai(cObject.getString("cai"));
			}
		}

		return mListResult;
	}

}