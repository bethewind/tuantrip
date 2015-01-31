package com.tudaidai.tuantrip;

import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.location.Location;
import android.location.LocationManager;
import android.preference.PreferenceManager;
import android.util.Log;

import com.tudaidai.tuantrip.app.TuanTripApp;
import com.tudaidai.tuantrip.app.TuanTripHttpApi;
import com.tudaidai.tuantrip.location.BestLocationListener;
import com.tudaidai.tuantrip.location.NetworkLocationListener;
import com.tudaidai.tuantrip.types.City;
import com.tudaidai.tuantrip.types.Group;
import com.tudaidai.tuantrip.types.Product;
import com.tudaidai.tuantrip.types.ProductShort;
import com.tudaidai.tuantrip.types.User;
import com.tudaidai.tuantrip.util.BaseDiskCache;
import com.tudaidai.tuantrip.util.JavaLoggingHandler;
import com.tudaidai.tuantrip.util.MemDiskCache;
import com.tudaidai.tuantrip.util.NullDiskCache;
import com.tudaidai.tuantrip.util.RemoteResourceManager;

public class TuanTrip extends Application {
	private static final String TAG = "TuanTrip";
	private static final boolean DEBUG = TuanTripSettings.DEBUG;
	private BestLocationListener mBestLocationListener = new BestLocationListener();
	private BestLocationListener networkLocationListener = new NetworkLocationListener();

	static {
		Logger.getLogger("com.tudaidai.tuantrip").addHandler(
				new JavaLoggingHandler());
		Logger.getLogger("com.tudaidai.tuantrip").setLevel(Level.ALL);
	}

	public static final String PACKAGE_NAME = "com.tudaidai.tuantrip";

	public static final String INTENT_ACTION_LOGGED_OUT = "com.tudaidai.tuantrip.intent.action.LOGGED_OUT";
	public static final String INTENT_ACTION_LOGGED_IN = "com.tudaidai.tuantrip.intent.action.LOGGED_IN";
	public static final String EXTRA_LOGIN_USER = "com.aibang.abtuan.EXTRA_LOGIN_USER";

	private int mVersion;

	private SharedPreferences mPrefs;
	private RemoteResourceManager mRemoteResourceManager;
	private RemoteResourceManager mFileRemoteResourceManager;
	private TuanTripApp tuanTripApp;
	private boolean mIsFirstRun;
	private String city;
	private Product product = new Product();
	private User mUser;
	private Group<City> mCities;
	private Group<ProductShort> mProductList;

	@Override
	public void onCreate() {
		Log.i(TAG, "Using Debug Server:\t" + TuanTripSettings.USE_DEBUG_SERVER);
		Log.i(TAG, "Using Dumpcatcher:\t" + TuanTripSettings.USE_DUMPCATCHER);
		Log.i(TAG, "Using Debug Log:\t" + DEBUG);
		mVersion = getVersionString(this);
		mPrefs = PreferenceManager.getDefaultSharedPreferences(this);

		new MediaCardStateBroadcastReceiver().register();
		loadResourceManagers();

		TuanTripHttpApi tripHttpApi = new TuanTripHttpApi("mi.tuantrip.com",
				PACKAGE_NAME + ":" + mVersion);
		tuanTripApp = new TuanTripApp(tripHttpApi);

		super.onCreate();
	}

	public int getVersion() {

		return mVersion;
	}

	private static int getVersionString(Context context) {
		// Get a version string for the app.
		try {
			PackageManager pm = context.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(PACKAGE_NAME, 0);
			return pi.versionCode;
		} catch (NameNotFoundException e) {
			if (DEBUG)
				Log.d(TAG, "Could not retrieve package info", e);
			throw new RuntimeException(e);
		}
	}

	private void loadResourceManagers() {
		try {
			if (DEBUG)
				Log.d(TAG,
						"Attempting to load RemoteResourceManager(new MemDiskCache())");
			mRemoteResourceManager = new RemoteResourceManager(
					new MemDiskCache());

		} catch (Exception e) {
			if (DEBUG)
				Log.d(TAG,
						"Falling back to NullDiskCache for RemoteResourceManager(new MemDiskCache())");
			mRemoteResourceManager = new RemoteResourceManager(
					new NullDiskCache());

		}
		try {
			if (DEBUG)
				Log.d(TAG, "Attempting to load RemoteResourceManager(cache)");
			mFileRemoteResourceManager = new RemoteResourceManager(
					new BaseDiskCache("tuantrip", "cache"));
		} catch (Exception e) {
			if (DEBUG)
				Log.d(TAG,
						"Falling back to NullDiskCache for RemoteResourceManager(cache)");
			mFileRemoteResourceManager = new RemoteResourceManager(
					new MemDiskCache());
		}
	}

	public TuanTripApp getTuanTripApp() {
		return tuanTripApp;
	}

	public Group<City> getCitys() {
		return mCities;
	}

	public String getStoreCity() {
		city = this.mPrefs.getString("city", "sh");
		return city;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public RemoteResourceManager getRemoteResourceManager() {
		return this.mRemoteResourceManager;
	}

	public RemoteResourceManager getFileRemoteResourceManager() {
		return this.mFileRemoteResourceManager;
	}

	public String getPwd() {
		return this.mPrefs.getString("pwd", "");
	}

	public User getUser() {
		return this.mUser;
	}

	public void setUser(User user) {
		this.mUser = user;
		if (user != null) {
			SharedPreferences.Editor localEditor1 = this.mPrefs.edit();
			localEditor1.putInt("uid", user.getUid());
			localEditor1.putString("uname", user.getEmail());
			localEditor1.putString("pwd", user.getPwd());
			localEditor1.commit();
		}
	}

	public void setUserOnly(User user) {
		this.mUser = user;
	}

	public String getUserId() {
		return this.mPrefs.getString("uid", "");
	}

	public String getUserName() {
		return this.mPrefs.getString("uname", "");
	}

	public void setProductList(Group<ProductShort> mProductList) {
		this.mProductList = mProductList;
	}

	public Group<ProductShort> getProductList() {
		return this.mProductList;
	}

	public void loginOut() {
		this.mUser = null;
		SharedPreferences.Editor localEditor1 = this.mPrefs.edit();
		localEditor1.putString("uname", "");
		localEditor1.putString("pwd", "");
		localEditor1.commit();
	}

	public void storeLoginUser(String paramString) {
		SharedPreferences.Editor localEditor1 = this.mPrefs.edit();
		localEditor1.putString(EXTRA_LOGIN_USER, paramString);
		localEditor1.commit();
	}

	public String getLoginUser() {
		return this.mPrefs.getString(EXTRA_LOGIN_USER, "");
	}

	public void setCities(Group<City> paramGroup) {
		this.mCities = paramGroup;
	}

	public void setCity(String paramString) {
		this.city = paramString;
		SharedPreferences.Editor localEditor1 = this.mPrefs.edit();
		localEditor1.putString("city", paramString);
		localEditor1.commit();
	}

	public void setShowPic(boolean isShowPic) {
		SharedPreferences.Editor localEditor1 = this.mPrefs.edit();
		localEditor1.putBoolean("showPic", isShowPic);
		localEditor1.commit();
	}

	public boolean getShowPic() {
		return this.mPrefs.getBoolean("showPic", true);
	}

	public void setOpenGps(boolean isopen) {
		SharedPreferences.Editor localEditor1 = this.mPrefs.edit();
		localEditor1.putBoolean("openGps", isopen);
		localEditor1.commit();
	}

	public boolean getOpenGps() {
		return this.mPrefs.getBoolean("openGps", true);
	}

	public void setCnCity(String paramString) {
		SharedPreferences.Editor localEditor1 = this.mPrefs.edit();
		localEditor1.putString("cncity", paramString);
		localEditor1.commit();
	}

	public String getStoreCnCity() {
		return this.mPrefs.getString("cncity", "上海");
	}

	//
	public void setPoi(String paramString) {
		SharedPreferences.Editor localEditor1 = this.mPrefs.edit();
		localEditor1.putString("Poi", paramString);
		localEditor1.commit();
	}

	public String getPoi() {
		return this.mPrefs.getString("Poi", "0-0");
	}

	public void setTuanLocation(String paramString) {
		SharedPreferences.Editor localEditor1 = this.mPrefs.edit();
		localEditor1.putString("location", paramString);
		localEditor1.commit();
	}

	public String getTuanLocation() {
		return this.mPrefs.getString("location", null);
	}

	public BestLocationListener requestLocationUpdates(Observer observer) {
		mBestLocationListener.addObserver(observer);
		mBestLocationListener.register(
				(LocationManager) getSystemService(LOCATION_SERVICE), true);
		return mBestLocationListener;
	}

	public void removeLocationUpdates() {
		mBestLocationListener
				.unregister((LocationManager) getSystemService(LOCATION_SERVICE));
	}

	public boolean isProviderEnabled() {
		return mBestLocationListener.isProviderEnabled();
	}

	public void removeLocationUpdates(Observer observer) {
		mBestLocationListener.deleteObserver(observer);
		this.removeLocationUpdates();
	}

	public Location getLastKnownLocation() {
		return mBestLocationListener.getLastKnownLocation();
	}

	// /////////////////network////////
	public BestLocationListener requestLocationUpdates1() {
		networkLocationListener.register(
				(LocationManager) getSystemService(LOCATION_SERVICE), true);
		return networkLocationListener;
	}

	public void removeLocationUpdates1() {
		networkLocationListener
				.unregister((LocationManager) getSystemService(LOCATION_SERVICE));
	}

	public boolean isProviderEnabled1() {
		return networkLocationListener.isProviderEnabled();
	}

	public Location getLastKnownLocation1() {
		return networkLocationListener.getLastKnownLocation();
	}

	// /////////////////

	private class MediaCardStateBroadcastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (DEBUG)
				Log.d(TAG, "Media state changed, reloading resource managers:"
						+ intent.getAction());
			if (Intent.ACTION_MEDIA_UNMOUNTED.equals(intent.getAction())) {
				getFileRemoteResourceManager().shutdown();
				mFileRemoteResourceManager = new RemoteResourceManager(
						new MemDiskCache());
			} else if (Intent.ACTION_MEDIA_MOUNTED.equals(intent.getAction())) {
				getFileRemoteResourceManager().shutdown();
				mFileRemoteResourceManager = new RemoteResourceManager(
						new BaseDiskCache("tuantrip", "cache"));
			}
		}

		public void register() {
			// Register our media card broadcast receiver so we can
			// enable/disable the cache as
			// appropriate.
			IntentFilter intentFilter = new IntentFilter();
			intentFilter.addAction(Intent.ACTION_MEDIA_UNMOUNTED);
			intentFilter.addAction(Intent.ACTION_MEDIA_MOUNTED);
			// intentFilter.addAction(Intent.ACTION_MEDIA_REMOVED);
			// intentFilter.addAction(Intent.ACTION_MEDIA_SHARED);
			// intentFilter.addAction(Intent.ACTION_MEDIA_BAD_REMOVAL);
			// intentFilter.addAction(Intent.ACTION_MEDIA_UNMOUNTABLE);
			// intentFilter.addAction(Intent.ACTION_MEDIA_NOFS);
			// intentFilter.addAction(Intent.ACTION_MEDIA_SCANNER_STARTED);
			// intentFilter.addAction(Intent.ACTION_MEDIA_SCANNER_FINISHED);
			intentFilter.addDataScheme("file");
			registerReceiver(this, intentFilter);
		}
	}
}
