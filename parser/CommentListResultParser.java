package com.tudaidai.tuantrip.parser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.tudaidai.tuantrip.types.Comment;
import com.tudaidai.tuantrip.types.CommentListResult;
import com.tudaidai.tuantrip.types.Group;

public class CommentListResultParser extends AbstractParser<CommentListResult> {

	@Override
	public CommentListResult parse(JSONObject json) throws JSONException {

		CommentListResult mListResult = new CommentListResult();

		JSONObject httpResultObject = json.getJSONObject("httpresult");
		if (httpResultObject.has("status")) {
			mListResult.mHttpResult.setStat(Integer.parseInt(httpResultObject
					.getString("status")));
		} else if (httpResultObject.has("Status")) {
			mListResult.mHttpResult.setStat(Integer.parseInt(httpResultObject
					.getString("Status")));
		}
		if (httpResultObject.has("info")) {
			mListResult.mHttpResult.setMessage(httpResultObject
					.getString("info"));
		} else if (httpResultObject.has("Info")) {
			mListResult.mHttpResult.setMessage(httpResultObject
					.getString("Info"));
		}

		if (json.has("total")) {
			mListResult.setTotal(Integer.parseInt(json.getString("total")));
		}
		if (json.has("comments")) {
			JSONArray commentsjArray = json.getJSONArray("comments");
			Group<Comment> gHotelNights = new Group<Comment>();
			for (int i = 0; i < commentsjArray.length(); i++) {
				Comment comment = new Comment();
				JSONObject hJsonObject = commentsjArray.getJSONObject(i);
				if (hJsonObject.has("cid")) {
					comment.setCid(hJsonObject.getString("cid"));
				}
				if (hJsonObject.has("uid")) {
					comment.setUid(hJsonObject.getString("uid"));
				}
				if (hJsonObject.has("uname")) {
					comment.setUname(hJsonObject.getString("uname"));
				}
				if (hJsonObject.has("uImage")) {
					comment.setUImage(hJsonObject.getString("uImage"));
				}
				if (hJsonObject.has("content")) {
					comment.setContent(hJsonObject.getString("content"));
				}
				if (hJsonObject.has("image")) {
					comment.setImage(hJsonObject.getString("image"));
				}
				if (hJsonObject.has("ding")) {
					comment.setDing(hJsonObject.getString("ding"));
				}
				if (hJsonObject.has("cai")) {
					comment.setCai(hJsonObject.getString("cai"));
				}
				if (hJsonObject.has("comtime")) {
					comment.setComtime(hJsonObject.getString("comtime"));
				}

				gHotelNights.add(comment);
			}

			mListResult.setComments(gHotelNights);
		}

		return mListResult;
	}

}