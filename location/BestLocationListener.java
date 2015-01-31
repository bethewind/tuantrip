package com.tudaidai.tuantrip.location;

import java.util.Observable;
import java.util.Timer;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.tudaidai.tuantrip.TuanTripSettings;

public class BestLocationListener extends Observable implements
		LocationListener {
	private static final String TAG = "BestLocationListener";
	private static final boolean DEBUG = TuanTripSettings.LOCATION_DEBUG;
	public static final long LOCATION_DELTA = 1000 * 60 * 5;
	public static final int TIME_DELTA = 60;
	public static final int TIME_UPLIMIT = 500;
	LocationManager locationManager;
	Timer timer = null;
	private volatile int timerNum = TIME_DELTA;

	private Location mLastLocation;

	public BestLocationListener() {
		super();
	}

	protected String getProviderName() {
		return LocationManager.GPS_PROVIDER;
	}

	@Override
	public void onLocationChanged(Location location) {
		if (DEBUG)
			Log.e(TAG, "onLocationChanged: " + location);
		if (getProviderName().equals(LocationManager.GPS_PROVIDER)) {
			if (timer == null) {
				timer = new Timer();
				timer.schedule(new MyTask(), 0, 1000);
			}

			if (timerNum >= TIME_DELTA) {
				timerNum = 0;
				updateLocation(location);
			}
		}
	}

	@Override
	public void onProviderDisabled(String provider) {
		// do nothing.
		if (DEBUG)
			Log.d(TAG, "onProviderDisabled: ");
	}

	@Override
	public void onProviderEnabled(String provider) {
		// do nothing.
		if (DEBUG)
			Log.d(TAG, "onProviderEnabled: ");
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		if (DEBUG)
			Log.d(TAG, "onStatusChanged: ");
	}

	synchronized public void onBestLocationChanged(Location location) {
		if (DEBUG)
			Log.d(TAG, "onBestLocationChanged: " + location);
		mLastLocation = location;
		setChanged();
		notifyObservers(location);
	}

	synchronized public Location getLastKnownLocation() {
		mLastLocation = locationManager.getLastKnownLocation(getProviderName());
		return mLastLocation;
	}

	synchronized public void clearLastKnownLocation() {
		mLastLocation = null;
	}

	public void updateLocation(Location location) {
		onBestLocationChanged(location);
		return;

	}

	public boolean isProviderEnabled() {
		if (locationManager == null)
			return true;

		return locationManager.isProviderEnabled(getProviderName());
	}

	public void register(LocationManager locationManager1, boolean gps) {
		if (DEBUG)
			Log.d(TAG, "Registering this location listener: " + this.toString());
		locationManager = locationManager1;
		if (locationManager.isProviderEnabled(getProviderName())) {
			try {
				locationManager.requestLocationUpdates(getProviderName(), 0, 0,
						this);
			} catch (Exception e) {
				if (DEBUG)
					Log.i(TAG, e.toString());
			}
			// updateLocation(locationManager.getLastKnownLocation(getProviderName()));
		}
		if (getProviderName().equals(LocationManager.GPS_PROVIDER)) {
			if (timer != null)
				timer.cancel();

			timer = new Timer();
			timer.schedule(new MyTask(), 0, 1000);
		}

	}

	public void unregister(LocationManager locationManager) {
		if (DEBUG)
			Log.d(TAG,
					"Unregistering this location listener: " + this.toString());
		locationManager.removeUpdates(this);

		if (getProviderName().equals(LocationManager.GPS_PROVIDER)) {
			if (timer != null)
				timer.cancel();
		}
	}

	class MyTask extends java.util.TimerTask {

		@Override
		public void run() {
			if (DEBUG)
				Log.d(TAG, "timerNum++------------");

			timerNum++;

			if (timerNum > TIME_UPLIMIT) {
				if (DEBUG)
					Log.d(TAG, "timerNum > 500--------");
				timerNum = TIME_DELTA;
				timer.cancel();
				timer = null;
			}
		}
	}

}
