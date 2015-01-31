package com.tudaidai.tuantrip.types;

import android.net.Uri;

public class HotelShort implements TuanTripType {
	public static final String AUTHORITY = "com.tudaidai.tuantrip.types.HotelShort";
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
			+ "/hotels");

	public static final String HID = "hid";
	public static final String SHORTTITLE = "shortTitle";
	public static final String IMAGEURL = "imageUrl";
	public static final String PRICE = "price";
	public static final String ADDRESS = "address";
	public static final String JINGDU = "jingdu";
	public static final String WEIDU = "weidu";

	private int hid;
	private String shortTitle;
	private String imageUrl;
	private String price;
	private String address;
	private double jingdu = 0;
	private double weidu = 0;

	public int getHid() {
		return hid;
	}

	public void setHid(int hid) {
		this.hid = hid;
	}

	public String getShortTitle() {
		return shortTitle;
	}

	public void setShortTitle(String shortTitle) {
		this.shortTitle = shortTitle;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public double getJingdu() {
		return jingdu;
	}

	public void setJingdu(double jingdu) {
		this.jingdu = jingdu;
	}

	public double getWeidu() {
		return weidu;
	}

	public void setWeidu(double weidu) {
		this.weidu = weidu;
	}
}
