package com.tudaidai.tuantrip.types;

public class DatePrice implements TuanTripType {
	private String date;
	private String price;

	public void setDate(String date) {
		this.date = date;
	}

	public String getDate() {
		return date;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getPrice() {
		return price;
	}

	public boolean equals(Object o) {
		if (o instanceof DatePrice) {
			return ((DatePrice) o).getDate().equals(this.date);
		}
		return false;
	}

	@Override
	public String toString() {
		return date.toString();
	}
}
