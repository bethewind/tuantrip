package com.tudaidai.tuantrip.types;

public class Shop implements TuanTripType {
	private String sName;
	private String sel;
	private String sddr;
	private String longitude;
	private String latitude;

	public String getSName() {
		return sName;
	}

	public void setSName(String sName) {
		this.sName = sName;
	}

	public String getSel() {
		return sel;
	}

	public void setSel(String sel) {
		this.sel = sel;
	}

	public String getSddr() {
		return sddr;
	}

	public void setSddr(String sddr) {
		this.sddr = sddr;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
}
