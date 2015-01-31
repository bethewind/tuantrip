package com.tudaidai.tuantrip.types;

public class SHotelOrderInfo implements TuanTripType {
	private String date;
	private String fangxing;
	private String kuandai;
	private String city;
	private double price;

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getFangxing() {
		return fangxing;
	}

	public void setFangxing(String fangxing) {
		this.fangxing = fangxing;
	}

	public String getKuandai() {
		return kuandai;
	}

	public void setKuandai(String kuandai) {
		this.kuandai = kuandai;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}
}
