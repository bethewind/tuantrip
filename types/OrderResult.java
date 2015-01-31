package com.tudaidai.tuantrip.types;

public class OrderResult implements TuanTripType {
	public Group<Order> mGroup = new Group<Order>();
	public HttpResult mHttpResult = new HttpResult();
	private int mTotal;

	public int getTotla() {
		return mTotal;
	}

	public void setTotla(int total) {
		this.mTotal = total;
	}
}