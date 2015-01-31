package com.tudaidai.tuantrip.types;

public class NearbyResult implements TuanTripType {
	public HttpResult mHttpResult = new HttpResult();
	public Group<Nearby> mGroup = new Group<Nearby>();

	private int mTotal;

	public int getTotla() {
		return mTotal;
	}

	public void setTotla(int total) {
		this.mTotal = total;
	}
}