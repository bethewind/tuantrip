package com.tudaidai.tuantrip.types;

import java.util.ArrayList;

public class HotelDetail extends BaseLbsDetail {

	private String hid;
	private String dangci;
	private String koubei;
	Group<HotelNight> hotelNights;
	ArrayList<String> images;

	public String getHid() {
		return hid;
	}

	public void setHid(String hid) {
		this.hid = hid;
	}

	public String getDangci() {
		return dangci;
	}

	public void setDangci(String dangci) {
		this.dangci = dangci;
	}

	public String getKoubei() {
		return koubei;
	}

	public void setKoubei(String koubei) {
		this.koubei = koubei;
	}

	public Group<HotelNight> getHotelNights() {
		return hotelNights;
	}

	public void setHotelNights(Group<HotelNight> hotelNights) {
		this.hotelNights = hotelNights;
	}

	public ArrayList<String> getImages() {
		return images;
	}

	public void setImages(ArrayList<String> images) {
		this.images = images;
	}
}
