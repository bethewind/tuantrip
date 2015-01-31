package com.tudaidai.tuantrip.parser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.tudaidai.tuantrip.TuanTripSettings;
import com.tudaidai.tuantrip.types.BaseLbsDetail;
import com.tudaidai.tuantrip.types.Comment;
import com.tudaidai.tuantrip.types.Group;

public class BaselbsParser {

	public static void parserComment(JSONObject hotelJsonObject,
			BaseLbsDetail bDetail) throws JSONException {
		if (hotelJsonObject.has("title")) {
			bDetail.setTitle(hotelJsonObject.getString("title"));
		}
		if (hotelJsonObject.has("city")) {
			bDetail.setCity(hotelJsonObject.getString("city"));
		}
		if (hotelJsonObject.has("image")) {
			String image = hotelJsonObject.getString("image");
			if (!image.startsWith("http://")) {
				image = TuanTripSettings.SITE + image;
			}

			bDetail.setImage(image);
		}
		if (hotelJsonObject.has("address")) {
			bDetail.setAddress(hotelJsonObject.getString("address"));
		}
		if (hotelJsonObject.has("phone")) {
			bDetail.setPhone(hotelJsonObject.getString("phone"));
		}
		if (hotelJsonObject.has("jingdu")) {
			bDetail.setJingdu(Double.parseDouble(hotelJsonObject
					.getString("jingdu")));
		}
		if (hotelJsonObject.has("weidu")) {
			bDetail.setWeidu(Double.parseDouble(hotelJsonObject
					.getString("weidu")));
		}
		if (hotelJsonObject.has("detail")) {
			bDetail.setDetail(hotelJsonObject.getString("detail"));
		}
		if (hotelJsonObject.has("total")) {
			bDetail.setTotal(Integer.parseInt(hotelJsonObject
					.getString("total")));
		}
		if (hotelJsonObject.has("lbsid")) {
			bDetail.setLbsid(hotelJsonObject.getString("lbsid"));
		}
		if (hotelJsonObject.has("comments")) {
			JSONArray commentsjArray = hotelJsonObject.getJSONArray("comments");
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

			bDetail.setComments(gHotelNights);
		}
	}
}
