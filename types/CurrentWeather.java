package com.tudaidai.tuantrip.types;

public class CurrentWeather implements TuanTripType {
	private String condition;
	private String temp_c;
	private String humidity;
	private String icon;
	private String wind_condition;

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public String getCondition() {
		return condition;
	}

	public void setTemp_c(String temp_c) {
		this.temp_c = temp_c;
	}

	public String getTemp_c() {
		return temp_c;
	}

	public void setHumidity(String humidity) {
		this.humidity = humidity;
	}

	public String getHumidity() {
		return humidity;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getIcon() {
		return icon;
	}

	public void setWind_condition(String wind_condition) {
		this.wind_condition = wind_condition;
	}

	public String getWind_condition() {
		return wind_condition;
	}

}
