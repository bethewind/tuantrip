package com.tudaidai.tuantrip.location;

import android.location.LocationManager;

public class NetworkLocationListener extends BestLocationListener {

	protected String getProviderName() {
		return LocationManager.NETWORK_PROVIDER;
	}
}
