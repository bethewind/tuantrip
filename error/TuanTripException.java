package com.tudaidai.tuantrip.error;

public class TuanTripException extends Exception {
	private static final long serialVersionUID = 1L;
	private String mExtra;

	public TuanTripException(String message) {
		super(message);
	}

	public TuanTripException(String paramString1, String paramString2) {
		super(paramString1);
		this.mExtra = paramString2;
	}

	public String getExtra() {
		return this.mExtra;
	}
}
