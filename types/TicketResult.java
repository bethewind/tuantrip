package com.tudaidai.tuantrip.types;

public class TicketResult implements TuanTripType {
	public Group<Ticket> mGroup = new Group<Ticket>();
	public HttpResult mHttpResult = new HttpResult();
	private int mTotal;

	public int getTotla() {
		return mTotal;
	}

	public void setTotla(int total) {
		this.mTotal = total;
	}
}