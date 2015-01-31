package com.tudaidai.tuantrip.types;

public class HotelOrderListResult implements TuanTripType {
	public Group<HotelOrder> mGroup = new Group<HotelOrder>();
	public HttpResult mHttpResult = new HttpResult();
	private int mTotal;

	public int getTotla() {
		return mTotal;
	}

	public void setTotla(int total) {
		this.mTotal = total;
	}
}