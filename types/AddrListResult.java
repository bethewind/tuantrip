package com.tudaidai.tuantrip.types;

public class AddrListResult implements TuanTripType {
	public Group<AddrInfo> mGroup = new Group<AddrInfo>();
	public HttpResult mHttpResult = new HttpResult();
}
