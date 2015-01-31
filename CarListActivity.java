package com.tudaidai.tuantrip;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.tudaidai.tuantrip.app.LoadableListActivity;
import com.tudaidai.tuantrip.app.TuanTripApp;
import com.tudaidai.tuantrip.location.CellIDInfoManager;
import com.tudaidai.tuantrip.location.CellIDInfoManager.CellIDInfo;
import com.tudaidai.tuantrip.location.LocationUtil;
import com.tudaidai.tuantrip.location.TuanLocation;
import com.tudaidai.tuantrip.location.WifiInfoManager;
import com.tudaidai.tuantrip.location.WifiInfoManager.WifiInfo;
import com.tudaidai.tuantrip.types.Car;
import com.tudaidai.tuantrip.types.CarResult;
import com.tudaidai.tuantrip.types.Cities;
import com.tudaidai.tuantrip.types.City;
import com.tudaidai.tuantrip.types.Group;
import com.tudaidai.tuantrip.util.NotificationsUtil;
import com.tudaidai.tuantrip.util.RemoteResourceManager;
import com.tudaidai.tuantrip.widget.CarListAdapter;

public class CarListActivity extends LoadableListActivity {

	private CarListAdapter mListAdapter;
	private CarResult orderResult;
	private ListView mListView;
	static final boolean DEBUG = TuanTripSettings.DEBUG;
	static final String TAG = "CarListActivity";
	private volatile boolean isFirst = true;
	private static final int MENU_REMEN = 1;
	private static final int MENU_CITY = 2;
	static final int CITY_DIALOG = 3;
	private Group<City> mCities;
	static final long mSleepTimeInMs = 2000;

	TextView firstLine;
	TextView secondLine;
	ImageView photo;
	Bitmap tuanbBitmap;
	Handler handler = new Handler();
	RemoteResourceManager localRemoteResourceManager;
	String city = null;
	WifiReceiver receiverWifi = new WifiReceiver();
	List<ScanResult> wifiList;
	ArrayList<WifiInfo> wifis;
	ArrayList<CellIDInfo> cellIDs;
	WifiManager mainWifi;
	TextView cityText;

	public void setEmptyView() {
		mEmptyProgress.setVisibility(ViewGroup.GONE);
		mEmptyText.setText("没有出租车公司信息");
	}

	public void onPause() {
		mListAdapter.removeObserver();
		unregisterReceiver(receiverWifi);

		super.onPause();
	}

	@Override
	public void onResume() {
		mListAdapter.addObserver();
		registerReceiver(receiverWifi, new IntentFilter(
				WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
		super.onResume();
	}

	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		// setContentView(R.layout.hotel_list_activity);
		localRemoteResourceManager = ((TuanTrip) getApplication())
				.getFileRemoteResourceManager();
		setContentView(R.layout.airport_list_activity);
		cityText = (TextView) findViewById(R.id.cityText);

		mainWifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		mainWifi.startScan();

		// mListView = (ListView)findViewById(R.id.hotellistView);
		mListView = getListView();
		registerForContextMenu(mListView);

		cellIDs = new CellIDInfoManager().getCellIDInfo(CarListActivity.this);

		mListAdapter = new CarListAdapter(CarListActivity.this,
				localRemoteResourceManager);

		mListView.setAdapter(mListAdapter);
		mListView.setBackgroundResource(R.color.scroll_bk);
		mListView.setCacheColorHint(Color.TRANSPARENT);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int i,
					long id) {
				String phone = ((Car) mListAdapter.getItem(i)).getPhone();
				Intent myIntentDial = new Intent(Intent.ACTION_DIAL, Uri
						.parse("tel://" + phone));// 直接拨打电话Intent.ACTION_CALL

				startActivity(myIntentDial);
			}
		});
		// Void[] as = new Void[0];
		new CarListTask().execute();

	}

	private void ensureUi(CarResult paramOrders) {
		cityText.setText(city);

		if (paramOrders.mGroup.size() != 0) {
			orderResult = paramOrders;

			mListAdapter.setGroup(orderResult.mGroup);

		} else {
			mListAdapter.setGroup(paramOrders.mGroup);
			setEmptyView();
			// Toast.makeText(this, "no hotel", Toast.LENGTH_LONG).show();
		}

	}

	public boolean onCreateOptionsMenu(Menu menu) {
		// menu.add(0,MENU_REMEN,0,"热门机场").setIcon(R.drawable.ic_menu_refresh);
		menu.add(0, MENU_CITY, 0, "选择城市").setIcon(R.drawable.ic_menu_search);
		super.onCreateOptionsMenu(menu);

		return true;
	}

	public boolean onOptionsItemSelected(MenuItem menuitem) {
		boolean flag = super.onOptionsItemSelected(menuitem);
		Intent intent = null;
		switch (menuitem.getItemId()) {
		case MENU_CITY:
			if (mCities == null || mCities.size() == 0) {
				Void[] as1 = new Void[0];
				new CitiesTask().execute(as1);
			} else {
				showDialog(CITY_DIALOG);
			}

			break;
		default:
			break;
		}

		return flag;
	}

	class CarListTask extends AsyncTask<Void, Void, CarResult> {
		private static final String TAG = "CarListTask";
		private Exception mReason;

		public CarListTask() {
		}

		protected CarResult doInBackground(Void... paramArrayOfString) {
			if (DEBUG)
				Log.d(TAG, "doInBackground()");
			CarResult orderResult = null;

			try {

				if (city == null) {
					TuanTrip trip = (TuanTrip) CarListActivity.this
							.getApplication();
					String tuanlocation = trip.getTuanLocation();
					if (tuanlocation != null) {
						city = tuanlocation.split("-")[0];
					}
				}
				if (city == null) {
					if (mSleepTimeInMs > 0L) {
						Thread.sleep(mSleepTimeInMs);
					}

					TuanLocation location = LocationUtil.callGear(wifis,
							cellIDs);
					if (location != null) {
						city = location.getCity();
					}
				}
				if (city == null) {
					city = "上海";
				}

				TuanTrip tuanTrip = (TuanTrip) CarListActivity.this
						.getApplication();
				orderResult = tuanTrip.getTuanTripApp().getCarList(city);

				if (DEBUG)
					Log.d(TAG, "doInBackground() over");

			} catch (Exception localException) {
				this.mReason = localException;
				orderResult = null;
			}

			return orderResult;
		}

		protected void onPostExecute(CarResult paramOrders) {
			if (DEBUG)
				Log.d(TAG, "onPostExecute()");
			if (paramOrders == null) {
				NotificationsUtil.ToastReasonForFailure(CarListActivity.this,
						mReason);
				setEmptyView();
			} else if (paramOrders.mHttpResult.getStat() == TuanTripSettings.POSTSUCCESS) {
				ensureUi(paramOrders);
			} else if (paramOrders.mHttpResult.getStat() == TuanTripSettings.POSTFAILED) {
				Toast.makeText(CarListActivity.this,
						paramOrders.mHttpResult.getMessage(),
						Toast.LENGTH_SHORT).show();
				setEmptyView();
			} else {
				NotificationsUtil.ToastReasonForFailure(CarListActivity.this,
						mReason);
				setEmptyView();
			}

			if (!isFirst)
				dismissProgressDialog();

			isFirst = false;
		}

		protected void onCancelled() {
			if (DEBUG)
				Log.d(TAG, "onCancelled()");
			super.onCancelled();
		}

		protected void onPreExecute() {
			if (DEBUG)
				Log.d(TAG, "onPreExecute()");

			if (!isFirst)
				startProgressBar("加载信息", "加载中...");
		}
	}

	class CitiesTask extends AsyncTask<Void, Void, Cities> {
		private Exception mReason;

		private CitiesTask() {
		}

		protected Cities doInBackground(Void... paramArrayOfVoid) {
			if (DEBUG)
				Log.d("CitiesTask", "doInBackground()");
			TuanTrip localTuanTrip = (TuanTrip) CarListActivity.this
					.getApplication();
			TuanTripApp localTuanTripApp = localTuanTrip.getTuanTripApp();
			Cities citys = null;
			try {
				citys = localTuanTripApp.getCarCities();

			} catch (Exception localException) {
				this.mReason = localException;
				citys = null;
			}
			return citys;
		}

		protected void onCancelled() {
			super.onCancelled();
			CarListActivity.this.dismissProgressDialog();
		}

		protected void onPostExecute(Cities paramCities) {
			if (paramCities == null) {
				NotificationsUtil.ToastReasonForFailure(CarListActivity.this,
						mReason);
			} else if (paramCities.mHttpResult.getStat() == TuanTripSettings.POSTSUCCESS) {
				mCities = paramCities.mCities;
				if (mCities != null && mCities.size() != 0) {
					showDialog(CITY_DIALOG);
				}
			} else if (paramCities.mHttpResult.getStat() == TuanTripSettings.POSTFAILED) {
				Toast.makeText(CarListActivity.this,
						paramCities.mHttpResult.getMessage(),
						Toast.LENGTH_SHORT).show();
			} else {
				NotificationsUtil.ToastReasonForFailure(CarListActivity.this,
						mReason);
			}
			CarListActivity.this.dismissProgressDialog();
		}

		protected void onPreExecute() {
			if (DEBUG)
				Log.d("TodayActivity", "onPreExecute()");
			startProgressBar("获取城市列表", "加载中...");
		}
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case CITY_DIALOG:
			CityPickerDialog citydialog = new CityPickerDialog(this, mCities);
			citydialog.setOnCancelListener(new OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					CityPickerDialog citypickerdialog = (CityPickerDialog) dialog;
					String city1 = citypickerdialog.getChosenCityName();
					if (!city1.equals("-1")) {
						city = city1;
						Void[] as = new Void[0];
						new CarListTask().execute(as);
					}
					removeDialog(CITY_DIALOG);

				}
			});
			return citydialog;
		}
		return null;
	}

	class WifiReceiver extends BroadcastReceiver {
		public void onReceive(Context c, Intent intent) {
			wifiList = mainWifi.getScanResults();
			wifis = new WifiInfoManager().getWifiInfo(mainWifi, wifiList);
		}
	}
}
