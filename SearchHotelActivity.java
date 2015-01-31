package com.tudaidai.tuantrip;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.tudaidai.tuantrip.app.TuanTripApp;
import com.tudaidai.tuantrip.location.CellIDInfoManager;
import com.tudaidai.tuantrip.location.CellIDInfoManager.CellIDInfo;
import com.tudaidai.tuantrip.location.LocationUtil;
import com.tudaidai.tuantrip.location.TuanLocation;
import com.tudaidai.tuantrip.location.WifiInfoManager;
import com.tudaidai.tuantrip.location.WifiInfoManager.WifiInfo;
import com.tudaidai.tuantrip.types.Cities;
import com.tudaidai.tuantrip.types.City;
import com.tudaidai.tuantrip.types.Group;
import com.tudaidai.tuantrip.types.HotelListResult;
import com.tudaidai.tuantrip.types.HotelShort;
import com.tudaidai.tuantrip.types.ProductShort;
import com.tudaidai.tuantrip.util.NotificationsUtil;
import com.tudaidai.tuantrip.util.RemoteResourceManager;
import com.tudaidai.tuantrip.util.TuanUtils;
import com.tudaidai.tuantrip.widget.DateSetLisner;
import com.tudaidai.tuantrip.widget.HotelListAdapter;

public class SearchHotelActivity extends Activity {

	private Group<ProductShort> mProductList;
	private ProgressDialog mProgressDialog;
	private HotelListAdapter mListAdapter;
	private HotelListResult orderResult;
	private ListView mListView;
	static final boolean DEBUG = TuanTripSettings.DEBUG;
	static final String TAG = "SearchHotelActivity";
	private volatile boolean isFinish = true;
	public static final String PAGE_NUM = "15";
	EditText cityEdit;
	EditText comEdit;
	EditText outEdit;
	EditText keyWordEdit;
	private int lastItem = 0;
	LinearLayout loadingLayout;
	TextView firstLine;
	TextView secondLine;
	ImageView photo;
	LinearLayout searchLinear;
	int searchLinearLength;
	ListView hotellistView;
	Bitmap tuanbBitmap;
	Handler handler = new Handler();
	private int mYear;
	private int mMonth;
	private int mDay;
	static final int COM_DATE = 1;
	static final int OUT_DATE = 2;
	static final int CITY_DIALOG = 3;
	private Group<City> mCities;
	RemoteResourceManager localRemoteResourceManager;
	private LayoutParams mLayoutParams = new LinearLayout.LayoutParams(
			LinearLayout.LayoutParams.WRAP_CONTENT,
			LinearLayout.LayoutParams.WRAP_CONTENT);
	private LayoutParams FFlayoutParams = new LinearLayout.LayoutParams(
			LinearLayout.LayoutParams.FILL_PARENT,
			LinearLayout.LayoutParams.FILL_PARENT);

	static final long mSleepTimeInMs = 2000;
	WifiReceiver receiverWifi = new WifiReceiver();
	List<ScanResult> wifiList;
	ArrayList<WifiInfo> wifis;
	ArrayList<CellIDInfo> cellIDs;
	WifiManager mainWifi;
	String city = null;

	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.search_hotel_activity);

		cityEdit = (EditText) findViewById(R.id.cityEdit);
		cityEdit.setFocusable(false);
		cityEdit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mCities == null || mCities.size() == 0) {
					Void[] as = new Void[0];
					new CitiesTask().execute(as);
				} else {
					showDialog(CITY_DIALOG);
				}
			}
		});
		comEdit = (EditText) findViewById(R.id.comEdit);
		comEdit.setFocusable(false);
		comEdit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showDialog(COM_DATE);

			}
		});
		outEdit = (EditText) findViewById(R.id.outEdit);
		outEdit.setFocusable(false);
		outEdit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showDialog(OUT_DATE);

			}
		});
		keyWordEdit = (EditText) findViewById(R.id.keyWordEdit);

		final Calendar calendar = Calendar.getInstance();
		mYear = calendar.get(Calendar.YEAR);
		mMonth = calendar.get(Calendar.MONTH);
		mDay = calendar.get(Calendar.DAY_OF_MONTH);

		Button button = (Button) findViewById(R.id.okButton);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (cityEdit.getText().toString().trim().equals("")) {
					Toast.makeText(SearchHotelActivity.this, "请选择城市",
							Toast.LENGTH_SHORT).show();
					return;
				} else if (comEdit.getText().toString().trim().equals("")) {
					Toast.makeText(SearchHotelActivity.this, "请选择入住日期",
							Toast.LENGTH_SHORT).show();
					return;
				} else if (outEdit.getText().toString().trim().equals("")) {
					Toast.makeText(SearchHotelActivity.this, "请选择离店日期",
							Toast.LENGTH_SHORT).show();
					return;
				}

				String[] as = new String[4];
				as[0] = cityEdit.getText().toString();
				as[1] = comEdit.getText().toString();

				as[2] = outEdit.getText().toString();
				as[3] = keyWordEdit.getText().toString();

				Intent intent = new Intent(SearchHotelActivity.this,
						HotelListActivity.class);
				intent.putExtra("as", as);
				startActivity(intent);

			}
		});
		Button todayButton = (Button) findViewById(R.id.todayButton);
		todayButton.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {

					String[] as = TuanUtils.getToday();
					comEdit.setText(as[0]);
					outEdit.setText(as[1]);

				}

			}
		});
		todayButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
			}
		});
		Button tommoryButton = (Button) findViewById(R.id.tommoryButton);
		tommoryButton.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {

					String[] as = TuanUtils.getTommory();
					comEdit.setText(as[0]);
					outEdit.setText(as[1]);

				}

			}
		});
		tommoryButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
			}
		});
		Button houtianButton = (Button) findViewById(R.id.houtianButton);
		houtianButton.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {

					String[] as = TuanUtils.getHoutain();
					comEdit.setText(as[0]);
					outEdit.setText(as[1]);

				}

			}
		});
		houtianButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
			}
		});
		tommoryButton.requestFocus();
		String[] as = TuanUtils.getToday();
		comEdit.setText(as[0]);
		outEdit.setText(as[1]);

		registerReceiver(receiverWifi, new IntentFilter(
				WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
		mainWifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		mainWifi.startScan();
		cellIDs = new CellIDInfoManager()
				.getCellIDInfo(SearchHotelActivity.this);

		if (city == null) {
			TuanTrip trip = (TuanTrip) getApplication();
			String tuanlocation = trip.getTuanLocation();
			if (tuanlocation != null) {
				city = tuanlocation.split("-")[0];
				if (cityEdit != null
						&& cityEdit.getText().toString().trim().equals("")
						&& city != null) {
					cityEdit.setText(city);
				}
			}
		}
		if (city == null) {
			new Thread() {
				public void run() {
					try {
						if (mSleepTimeInMs > 0L) {
							Thread.sleep(mSleepTimeInMs);
						}

						if (city == null) {
							TuanLocation location = LocationUtil.callGear(
									wifis, cellIDs);
							city = location.getCity();
						}
						handler.post(new Runnable() {

							@Override
							public void run() {
								if (cityEdit != null
										&& cityEdit.getText().toString().trim()
												.equals("") && city != null) {
									cityEdit.setText(city);
								}

							}
						});
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
			}.start();
		}

	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case COM_DATE:
			return new DatePickerDialog(this, new DateSetLisner(comEdit),
					mYear, mMonth, mDay);
		case OUT_DATE:
			return new DatePickerDialog(this, new DateSetLisner(outEdit),
					mYear, mMonth, mDay);
		case CITY_DIALOG:
			CityPickerDialog citydialog = new CityPickerDialog(this, mCities);
			citydialog.setOnCancelListener(new OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					CityPickerDialog citypickerdialog = (CityPickerDialog) dialog;
					String city = citypickerdialog.getChosenCityName();
					if (!city.equals("-1")) {
						cityEdit.setText(city);
					}
					removeDialog(CITY_DIALOG);

				}
			});
			return citydialog;
		}
		return null;
	}

	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {
		switch (id) {
		case COM_DATE:
			((DatePickerDialog) dialog).updateDate(mYear, mMonth, mDay);
			break;
		case OUT_DATE:
			((DatePickerDialog) dialog).updateDate(mYear, mMonth, mDay);
			break;
		}
	}

	private void ensureUi(HotelListResult paramOrders) {

		if (orderResult == null) {
			if (paramOrders.mGroup.size() != 0) {
				orderResult = paramOrders;
				if (orderResult != null
						&& orderResult.mGroup.size() == orderResult.getTotla()) {
					mListView.removeFooterView(loadingLayout);
				}
				firstLine.setText(orderResult.productShort.getShortTitle());
				secondLine.setText("现价￥" + orderResult.productShort.getPrice()
						+ "," + orderResult.productShort.getBought() + "人购买");
				// photo.loadUrl(orderResult.productShort.getImageUrl());
				new Thread() {
					public void run() {
						tuanbBitmap = TuanUtils
								.getBitmapFromUrl(orderResult.productShort
										.getImageUrl());
						handler.post(new Runnable() {

							@Override
							public void run() {
								photo.setImageBitmap(tuanbBitmap);

							}
						});
					}
				}.start();

				mListAdapter.setGroup(orderResult.mGroup);

				// searchLinear.getLayoutParams().height = 100;
				searchLinear.setVisibility(View.INVISIBLE);
				hotellistView.getLayoutParams().height = LayoutParams.WRAP_CONTENT;
				hotellistView.setVisibility(View.VISIBLE);

			} else {
				Toast.makeText(this, "no hotel", Toast.LENGTH_LONG).show();
			}
		} else {
			for (HotelShort hotelShort : paramOrders.mGroup) {
				orderResult.mGroup.add(hotelShort);
			}

			mListView.removeFooterView(loadingLayout);

			mListAdapter.notifyDataSetChanged();

		}

	}

	private void dismissProgressDialog() {
		mProgressDialog.dismiss();
	}

	private void startProgressBar(String title, String content) {
		mProgressDialog = ProgressDialog.show(this, title, content);
		mProgressDialog.show();
	}

	class HotelListTask extends AsyncTask<String, Void, HotelListResult> {
		private static final String TAG = "HotelListTask";
		private Exception mReason;

		public HotelListTask() {
		}

		protected HotelListResult doInBackground(String... paramArrayOfString) {
			if (DEBUG)
				Log.d(TAG, "doInBackground()");
			HotelListResult orderResult = null;
			try {
				TuanTripApp localTuanTripApp = ((TuanTrip) SearchHotelActivity.this
						.getApplication()).getTuanTripApp();
				orderResult = localTuanTripApp.getSearchHotel(
						paramArrayOfString,
						new Integer(lastItem + 1).toString(), PAGE_NUM);

				if (DEBUG)
					Log.d(TAG, "doInBackground() over");

			} catch (Exception localException) {
				this.mReason = localException;
				orderResult = null;
			}

			return orderResult;
		}

		protected void onPostExecute(HotelListResult paramOrders) {
			if (DEBUG)
				Log.d(TAG, "onPostExecute()");
			if (paramOrders == null) {
				NotificationsUtil.ToastReasonForFailure(
						SearchHotelActivity.this, mReason);
			} else if (paramOrders.mHttpResult.getStat() == TuanTripSettings.POSTSUCCESS) {
				ensureUi(paramOrders);
			} else if (paramOrders.mHttpResult.getStat() == TuanTripSettings.POSTFAILED) {
				Toast.makeText(SearchHotelActivity.this,
						paramOrders.mHttpResult.getMessage(),
						Toast.LENGTH_SHORT).show();
			} else if (paramOrders.mHttpResult.getStat() == TuanTripSettings.LOGINFAILED) {
				// Toast.makeText(NearbyHotelsActivity.this,paramOrders.mHttpResult.getMessage(),Toast.LENGTH_SHORT).show();
				// ((TuanTrip)getApplication()).loginOut();
				// Intent localIntent4 = new Intent(NearbyHotelsActivity.this,
				// LoginActivity.class);
				// localIntent4.putExtra(LoginActivity.EXTRA_OPTION,
				// LoginActivity.OPTION_ADDADDR);
				// startActivity(localIntent4);
			} else {
				NotificationsUtil.ToastReasonForFailure(
						SearchHotelActivity.this, mReason);
			}
			isFinish = true;
			dismissProgressDialog();
		}

		protected void onCancelled() {
			if (DEBUG)
				Log.d(TAG, "onCancelled()");
			super.onCancelled();
		}

		protected void onPreExecute() {
			if (DEBUG)
				Log.d(TAG, "onPreExecute()");
			startProgressBar("查询酒店", "查询中...");
			isFinish = false;
		}
	}

	class CitiesTask extends AsyncTask<Void, Void, Cities> {
		private Exception mReason;

		private CitiesTask() {
		}

		protected Cities doInBackground(Void... paramArrayOfVoid) {
			if (DEBUG)
				Log.d("CitiesTask", "doInBackground()");
			TuanTrip localTuanTrip = (TuanTrip) SearchHotelActivity.this
					.getApplication();
			TuanTripApp localTuanTripApp = localTuanTrip.getTuanTripApp();
			Cities citys = null;
			try {
				citys = localTuanTripApp.getHotelCities();

			} catch (Exception localException) {
				this.mReason = localException;
				citys = null;
			}
			return citys;
		}

		protected void onCancelled() {
			super.onCancelled();
			SearchHotelActivity.this.dismissProgressDialog();
		}

		protected void onPostExecute(Cities paramCities) {
			if (paramCities == null) {
				NotificationsUtil.ToastReasonForFailure(
						SearchHotelActivity.this, mReason);
			} else if (paramCities.mHttpResult.getStat() == TuanTripSettings.POSTSUCCESS) {
				mCities = paramCities.mCities;
				if (mCities != null && mCities.size() != 0) {
					showDialog(CITY_DIALOG);
				}
			} else if (paramCities.mHttpResult.getStat() == TuanTripSettings.POSTFAILED) {
				Toast.makeText(SearchHotelActivity.this,
						paramCities.mHttpResult.getMessage(),
						Toast.LENGTH_SHORT).show();
			} else {
				NotificationsUtil.ToastReasonForFailure(
						SearchHotelActivity.this, mReason);
			}
			SearchHotelActivity.this.dismissProgressDialog();
		}

		protected void onPreExecute() {
			if (DEBUG)
				Log.d("TodayActivity", "onPreExecute()");
			startProgressBar("获取城市列表", "加载中...");
		}
	}

	class WifiReceiver extends BroadcastReceiver {
		public void onReceive(Context c, Intent intent) {
			wifiList = mainWifi.getScanResults();
			wifis = new WifiInfoManager().getWifiInfo(mainWifi, wifiList);
		}
	}
}
