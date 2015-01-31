package com.tudaidai.tuantrip.types;

public class Xianlu implements TuanTripType {
	private String typeId;
	private String travelDate;
	private double price;

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	public String getTypeId() {
		return typeId;
	}

	public void setTravelDate(String travelDate) {
		this.travelDate = travelDate;
	}

	public String getTravelDate() {
		return travelDate;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public double getPrice() {
		return price;
	}
}
