package com.tudaidai.tuantrip.types;

public class HttpResult implements TuanTripType {
	private String mMessage = "未知异常,请重试";
	private int mStat = 502;

	public String getMessage() {
		return this.mMessage;
	}

	public int getStat() {
		return this.mStat;
	}

	public void setMessage(String paramString) {
		this.mMessage = paramString;
	}

	public void setStat(int paramInt) {
		this.mStat = paramInt;
	}
}