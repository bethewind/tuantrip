package com.tudaidai.tuantrip.types;

public class HotelNight implements TuanTripType {
	private String nid;
	private String fangxing;
	private String price;
	private String finalprice;

	public void setNid(String nid) {
		this.nid = nid;
	}

	public String getNid() {
		return nid;
	}

	public void setFangxing(String fangxing) {
		this.fangxing = fangxing;
	}

	public String getFangxing() {
		return fangxing;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getPrice() {
		return price;
	}

	public void setFinalprice(String finalprice) {
		this.finalprice = finalprice;
	}

	public String getFinalprice() {
		return finalprice;
	}

}
