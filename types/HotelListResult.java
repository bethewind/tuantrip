package com.tudaidai.tuantrip.types;

public class HotelListResult implements TuanTripType {
	public HttpResult mHttpResult = new HttpResult();
	public Group<HotelShort> mGroup = new Group<HotelShort>();
	public ProductShort productShort = new ProductShort();

	private int mTotal;

	public int getTotla() {
		return mTotal;
	}

	public void setTotla(int total) {
		this.mTotal = total;
	}
}