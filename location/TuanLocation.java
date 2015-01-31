package com.tudaidai.tuantrip.location;

import android.location.Location;

public class TuanLocation extends Location {

	private String city;
	private String street = "";

	public TuanLocation(Location l) {
		super(l);
		// TODO Auto-generated constructor stub
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCity() {
		return city;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getStreet() {
		return street;
	}

}
