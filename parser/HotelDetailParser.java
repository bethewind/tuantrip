package com.tudaidai.tuantrip.parser;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.tudaidai.tuantrip.TuanTripSettings;
import com.tudaidai.tuantrip.types.Comment;
import com.tudaidai.tuantrip.types.Group;
import com.tudaidai.tuantrip.types.HotelDetailResult;
import com.tudaidai.tuantrip.types.HotelNight;

public class HotelDetailParser extends AbstractParser<HotelDetailResult> {

	@Override
	public HotelDetailResult parse(JSONObject json) throws JSONException {

		HotelDetailResult hotelDetailResult = new HotelDetailResult();

		JSONObject httpResultObject = json.getJSONObject("httpResult");
		if (httpResultObject.has("status")) {
			hotelDetailResult.mHttpResult.setStat(Integer
					.parseInt(httpResultObject.getString("status")));
		}
		if (httpResultObject.has("info")) {
			hotelDetailResult.mHttpResult.setMessage(httpResultObject
					.getString("info"));
		}

		if (json.has("hotel")) {
			JSONObject hotelJsonObject = json.getJSONObject("hotel");
			if (hotelJsonObject.has("hid")) {
				hotelDetailResult.hotelDetail.setHid(hotelJsonObject
						.getString("hid"));
			}
			if (hotelJsonObject.has("title")) {
				hotelDetailResult.hotelDetail.setTitle(hotelJsonObject
						.getString("title"));
			}
			if (hotelJsonObject.has("city")) {
				hotelDetailResult.hotelDetail.setCity(hotelJsonObject
						.getString("city"));
			}
			if (hotelJsonObject.has("image")) {
				String image = hotelJsonObject.getString("image");
				if (!image.startsWith("http://")) {
					image = TuanTripSettings.SITE + image;
				}

				hotelDetailResult.hotelDetail.setImage(image);
			}
			if (hotelJsonObject.has("dangci")) {
				hotelDetailResult.hotelDetail.setDangci(hotelJsonObject
						.getString("dangci"));
			}
			if (hotelJsonObject.has("address")) {
				hotelDetailResult.hotelDetail.setAddress(hotelJsonObject
						.getString("address"));
			}
			if (hotelJsonObject.has("phone")) {
				hotelDetailResult.hotelDetail.setPhone(hotelJsonObject
						.getString("phone"));
			}
			if (hotelJsonObject.has("jingdu")) {
				try {
					hotelDetailResult.hotelDetail.setJingdu(Double
							.parseDouble(hotelJsonObject.getString("jingdu")));
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
			if (hotelJsonObject.has("weidu")) {
				try {
					hotelDetailResult.hotelDetail.setWeidu(Double
							.parseDouble(hotelJsonObject.getString("weidu")));
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
			if (hotelJsonObject.has("koubei")) {
				hotelDetailResult.hotelDetail.setKoubei(hotelJsonObject
						.getString("koubei"));
			}
			if (hotelJsonObject.has("detail")) {
				hotelDetailResult.hotelDetail.setDetail(hotelJsonObject
						.getString("detail"));
			}
			if (hotelJsonObject.has("nights")) {
				JSONArray nightsjArray = hotelJsonObject.getJSONArray("nights");
				Group<HotelNight> gHotelNights = new Group<HotelNight>();
				for (int i = 0; i < nightsjArray.length(); i++) {
					HotelNight hotelNight = new HotelNight();
					JSONObject hJsonObject = nightsjArray.getJSONObject(i);
					if (hJsonObject.has("nid")) {
						hotelNight.setNid(hJsonObject.getString("nid"));
					}
					if (hJsonObject.has("fangxing")) {
						hotelNight.setFangxing(hJsonObject
								.getString("fangxing"));
					}
					if (hJsonObject.has("price")) {
						hotelNight.setPrice(hJsonObject.getString("price"));
					}
					if (hJsonObject.has("finalprice")) {
						hotelNight.setFinalprice(hJsonObject
								.getString("finalprice"));
					}

					gHotelNights.add(hotelNight);
				}

				hotelDetailResult.hotelDetail.setHotelNights(gHotelNights);
			}
			//
			if (hotelJsonObject.has("images")) {
				JSONArray nightsjArray = hotelJsonObject.getJSONArray("images");
				ArrayList<String> images = new ArrayList<String>();
				for (int i = 0; i < nightsjArray.length(); i++) {
					String imageUrl = "";
					JSONObject hJsonObject = nightsjArray.getJSONObject(i);
					if (hJsonObject.has("imageurl")) {
						String image = hJsonObject.getString("imageurl");
						if (!image.startsWith("http://")) {
							image = TuanTripSettings.SITE + image;
						}

						imageUrl = image;
					}

					images.add(imageUrl);
				}

				hotelDetailResult.hotelDetail.setImages(images);
			}
			if (hotelJsonObject.has("total")) {
				hotelDetailResult.hotelDetail.setTotal(Integer
						.parseInt(hotelJsonObject.getString("total")));
			}
			if (hotelJsonObject.has("lbsid")) {
				hotelDetailResult.hotelDetail.setLbsid(hotelJsonObject
						.getString("lbsid"));
			}
			if (hotelJsonObject.has("comments")) {
				JSONArray commentsjArray = hotelJsonObject
						.getJSONArray("comments");
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

				hotelDetailResult.hotelDetail.setComments(gHotelNights);
			}

		}

		return hotelDetailResult;
	}

}