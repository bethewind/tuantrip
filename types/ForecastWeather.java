package com.tudaidai.tuantrip.types;

public class ForecastWeather implements TuanTripType {
	private String day_of_week;
	private String low;
	private String high;
	private String icon;
	private String condition;

	public void setDay_of_week(String day_of_week) {
		this.day_of_week = day_of_week;
	}

	public String getDay_of_week() {
		return day_of_week;
	}

	public void setLow(String low) {
		this.low = low;
	}

	public String getLow() {
		return low;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getIcon() {
		return icon;
	}

	public void setHigh(String high) {
		this.high = high;
	}

	public String getHigh() {
		return high;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public String getCondition() {
		return condition;
	}

}
