package com.tudaidai.tuantrip;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
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
import com.tudaidai.tuantrip.types.Airport;
import com.tudaidai.tuantrip.types.AirportResult;
import com.tudaidai.tuantrip.types.Cities;
import com.tudaidai.tuantrip.types.City;
import com.tudaidai.tuantrip.types.Group;
import com.tudaidai.tuantrip.types.ProductShort;
import com.tudaidai.tuantrip.util.NotificationsUtil;
import com.tudaidai.tuantrip.util.RemoteResourceManager;
import com.tudaidai.tuantrip.widget.AirportResultListAdapter;

public class AirportListActivity extends LoadableListActivity {

	private Group<ProductShort> mProductList;
	private AirportResultListAdapter mListAdapter;
	private AirportResult orderResult;
	private ListView mListView;
	static final boolean DEBUG = TuanTripSettings.DEBUG;
	static final String TAG = "AirportListActivity";
	private volatile boolean isFirst = true;
	private static final int MENU_REMEN = 1;
	private static final int MENU_CITY = 2;
	static final int CITY_DIALOG = 3;
	private Group<City> mCities;

	TextView firstLine;
	TextView secondLine;
	ImageView photo;
	Bitmap tuanbBitmap;
	Handler handler = new Handler();
	RemoteResourceManager localRemoteResourceManager;
	String city = null;
	TextView cityText;

	public void setEmptyView() {
		mEmptyProgress.setVisibility(ViewGroup.GONE);
		mEmptyText.setText("没有机场信息");
	}

	public void onPause() {
		mListAdapter.removeObserver();

		super.onPause();
	}

	@Override
	public void onResume() {
		mListAdapter.addObserver();

		super.onResume();
	}

	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.airport_list_activity);
		cityText = (TextView) findViewById(R.id.cityText);
		cityText.setText("热门机场");
		localRemoteResourceManager = ((TuanTrip) getApplication())
				.getFileRemoteResourceManager();

		// mListView = (ListView)findViewById(R.id.hotellistView);
		mListView = getListView();
		registerForContextMenu(mListView);

		mListAdapter = new AirportResultListAdapter(AirportListActivity.this,
				localRemoteResourceManager);

		mListView.setAdapter(mListAdapter);
		mListView.setBackgroundResource(R.color.scroll_bk);
		mListView.setCacheColorHint(Color.TRANSPARENT);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int i,
					long id) {
				int id1 = ((Airport) mListAdapter.getItem(i)).getId();
				Intent intent = new Intent(AirportListActivity.this,
						AirportDetailActivity.class);
				intent.putExtra(AirportDetailActivity.ID, id1);
				startActivity(intent);
			}
		});
		Void[] as = new Void[0];
		new AirportListTask().execute(as);

	}

	private void ensureUi(AirportResult paramOrders) {

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
		menu.add(0, MENU_REMEN, 0, "热门机场").setIcon(R.drawable.ic_menu_refresh);
		menu.add(0, MENU_CITY, 0, "选择城市").setIcon(R.drawable.ic_menu_refresh);
		super.onCreateOptionsMenu(menu);

		return true;
	}

	public boolean onOptionsItemSelected(MenuItem menuitem) {
		boolean flag = super.onOptionsItemSelected(menuitem);
		Intent intent = null;
		switch (menuitem.getItemId()) {
		case MENU_REMEN:
			city = null;
			cityText.setText("热门机场");
			Void[] as = new Void[0];
			new AirportListTask().execute(as);
			break;
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

	class AirportListTask extends AsyncTask<Void, Void, AirportResult> {
		private static final String TAG = "AirportListTask";
		private Exception mReason;

		public AirportListTask() {
		}

		protected AirportResult doInBackground(Void... paramArrayOfString) {
			if (DEBUG)
				Log.d(TAG, "doInBackground()");
			AirportResult orderResult = null;
			try {
				TuanTrip tuanTrip = (TuanTrip) AirportListActivity.this
						.getApplication();
				orderResult = tuanTrip.getTuanTripApp().getAirportList(city);

				if (DEBUG)
					Log.d(TAG, "doInBackground() over");

			} catch (Exception localException) {
				this.mReason = localException;
				orderResult = null;
			}

			return orderResult;
		}

		protected void onPostExecute(AirportResult paramOrders) {
			if (DEBUG)
				Log.d(TAG, "onPostExecute()");
			if (paramOrders == null) {
				NotificationsUtil.ToastReasonForFailure(
						AirportListActivity.this, mReason);
				setEmptyView();
			} else if (paramOrders.mHttpResult.getStat() == TuanTripSettings.POSTSUCCESS) {
				ensureUi(paramOrders);
			} else if (paramOrders.mHttpResult.getStat() == TuanTripSettings.POSTFAILED) {
				Toast.makeText(AirportListActivity.this,
						paramOrders.mHttpResult.getMessage(),
						Toast.LENGTH_SHORT).show();
				setEmptyView();
			} else {
				NotificationsUtil.ToastReasonForFailure(
						AirportListActivity.this, mReason);
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
			TuanTrip localTuanTrip = (TuanTrip) AirportListActivity.this
					.getApplication();
			TuanTripApp localTuanTripApp = localTuanTrip.getTuanTripApp();
			Cities citys = null;
			try {
				citys = localTuanTripApp.getAirportCities();

			} catch (Exception localException) {
				this.mReason = localException;
				citys = null;
			}
			return citys;
		}

		protected void onCancelled() {
			super.onCancelled();
			AirportListActivity.this.dismissProgressDialog();
		}

		protected void onPostExecute(Cities paramCities) {
			if (paramCities == null) {
				NotificationsUtil.ToastReasonForFailure(
						AirportListActivity.this, mReason);
			} else if (paramCities.mHttpResult.getStat() == TuanTripSettings.POSTSUCCESS) {
				mCities = paramCities.mCities;
				if (mCities != null && mCities.size() != 0) {
					showDialog(CITY_DIALOG);
				}
			} else if (paramCities.mHttpResult.getStat() == TuanTripSettings.POSTFAILED) {
				Toast.makeText(AirportListActivity.this,
						paramCities.mHttpResult.getMessage(),
						Toast.LENGTH_SHORT).show();
			} else {
				NotificationsUtil.ToastReasonForFailure(
						AirportListActivity.this, mReason);
			}
			AirportListActivity.this.dismissProgressDialog();
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
						cityText.setText(city1);
						Void[] as = new Void[0];
						new AirportListTask().execute(as);
					}
					removeDialog(CITY_DIALOG);

				}
			});
			return citydialog;
		}
		return null;
	}
}
