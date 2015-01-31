package com.tudaidai.tuantrip.error;

public class LocationException extends TuanTripException {

	public LocationException() {
		super("Unable to determine your location.");
	}

	public LocationException(String message) {
		super(message);
	}

	private static final long serialVersionUID = 1L;

}
