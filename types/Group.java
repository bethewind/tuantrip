package com.tudaidai.tuantrip.types;

import java.util.ArrayList;

public class Group<T extends TuanTripType> extends ArrayList<T> implements
		TuanTripType {
	private static final long serialVersionUID = 1L;
	private String mType;

	public String getType() {
		return this.mType;
	}

	public void setType(String paramString) {
		this.mType = paramString;
	}
}