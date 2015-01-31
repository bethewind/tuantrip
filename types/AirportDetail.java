package com.tudaidai.tuantrip.types;


public class AirportDetail extends BaseLbsDetail {

	private String id;
	private String service;
	private String jiaotong;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getJiaotong() {
		return jiaotong;
	}

	public void setJiaotong(String jiaotong) {
		this.jiaotong = jiaotong;
	}
}
