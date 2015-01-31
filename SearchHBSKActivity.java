package com.tudaidai.tuantrip;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.tudaidai.tuantrip.app.TuanTripApp;
import com.tudaidai.tuantrip.types.Cities;
import com.tudaidai.tuantrip.types.City;
import com.tudaidai.tuantrip.types.Group;
import com.tudaidai.tuantrip.util.NotificationsUtil;

public class SearchHBSKActivity extends Activity {
	static final boolean DEBUG = TuanTripSettings.DEBUG;

	static final String TAG = "SearchHBSKActivity";
	TextView startCityEdit;
	TextView endCityEdit;
	static final int STARTCITY_DIALOG = 0;
	static final int ENDCITY_DIALOG = 1;
	private Group<City> mCities;
	private int witchcity = 0;// 0是出发城市，1到达城市
	protected ProgressDialog mProgressDialog;

	protected void dismissProgressDialog() {
		if (mProgressDialog != null)
			mProgressDialog.dismiss();
	}

	protected void startProgressBar(String title, String content) {
		// if(mProgressDialog==null)
		mProgressDialog = ProgressDialog.show(this, title, content);

		mProgressDialog.setCancelable(true);
		mProgressDialog.show();
	}

	protected void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		setContentView(R.layout.search_hbsk_activity);

		startCityEdit = (TextView) findViewById(R.id.startCityEdit);
		startCityEdit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				witchcity = 0;
				if (mCities == null || mCities.size() == 0) {
					Void[] as1 = new Void[0];
					new CitiesTask().execute(as1);
				} else {
					showDialog(STARTCITY_DIALOG);
				}

			}
		});

		endCityEdit = (TextView) findViewById(R.id.endCityEdit);
		endCityEdit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				witchcity = 1;
				if (mCities == null || mCities.size() == 0) {
					Void[] as1 = new Void[0];
					new CitiesTask().execute(as1);
				} else {
					showDialog(ENDCITY_DIALOG);
				}

			}
		});
		Button okButton = (Button) findViewById(R.id.okButton);
		okButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (startCityEdit.getText().toString().trim().equals("")) {
					Toast.makeText(SearchHBSKActivity.this, "出发地不能为空",
							Toast.LENGTH_SHORT).show();
					return;
				} else if (endCityEdit.getText().toString().trim().equals("")) {
					Toast.makeText(SearchHBSKActivity.this, "目的地不能为空",
							Toast.LENGTH_SHORT).show();
					return;
				}

				String[] as = new String[2];
				as[0] = startCityEdit.getText().toString().trim();
				as[1] = endCityEdit.getText().toString().trim();
				Intent intent = new Intent(SearchHBSKActivity.this,
						HBSKActivity.class);
				intent.putExtra(HBSKActivity.EXTRA_OPTION, as);
				startActivity(intent);

			}
		});

		TextView t1 = (TextView) findViewById(R.id.t1);
		t1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getCity(v);

			}
		});
		TextView t2 = (TextView) findViewById(R.id.t2);
		t2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getCity(v);

			}
		});
		TextView t3 = (TextView) findViewById(R.id.t3);
		t3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getCity(v);

			}
		});
		TextView t4 = (TextView) findViewById(R.id.t4);
		t4.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getCity(v);

			}
		});

	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case STARTCITY_DIALOG:
			CityPickerDialog citydialog = new CityPickerDialog(this, mCities);
			citydialog.setOnCancelListener(new OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					CityPickerDialog citypickerdialog = (CityPickerDialog) dialog;
					String city1 = citypickerdialog.getChosenCityName();
					if (!city1.equals("-1")) {
						startCityEdit.setText(city1);
					}
					removeDialog(STARTCITY_DIALOG);

				}
			});
			return citydialog;
		case ENDCITY_DIALOG:
			CityPickerDialog citydialog1 = new CityPickerDialog(this, mCities);
			citydialog1.setOnCancelListener(new OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					CityPickerDialog citypickerdialog = (CityPickerDialog) dialog;
					String city1 = citypickerdialog.getChosenCityName();
					if (!city1.equals("-1")) {
						endCityEdit.setText(city1);
					}
					removeDialog(ENDCITY_DIALOG);

				}
			});
			return citydialog1;
		}
		return null;
	}

	class CitiesTask extends AsyncTask<Void, Void, Cities> {
		private Exception mReason;

		private CitiesTask() {
		}

		protected Cities doInBackground(Void... paramArrayOfVoid) {
			if (DEBUG)
				Log.d("CitiesTask", "doInBackground()");
			TuanTrip localTuanTrip = (TuanTrip) SearchHBSKActivity.this
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
			SearchHBSKActivity.this.dismissProgressDialog();
		}

		protected void onPostExecute(Cities paramCities) {
			if (paramCities == null) {
				NotificationsUtil.ToastReasonForFailure(
						SearchHBSKActivity.this, mReason);
			} else if (paramCities.mHttpResult.getStat() == TuanTripSettings.POSTSUCCESS) {
				mCities = paramCities.mCities;
				if (mCities != null && mCities.size() != 0) {
					if (witchcity == 0) {
						showDialog(STARTCITY_DIALOG);
					} else {
						showDialog(ENDCITY_DIALOG);
					}
				}
			} else if (paramCities.mHttpResult.getStat() == TuanTripSettings.POSTFAILED) {
				Toast.makeText(SearchHBSKActivity.this,
						paramCities.mHttpResult.getMessage(),
						Toast.LENGTH_SHORT).show();
			} else {
				NotificationsUtil.ToastReasonForFailure(
						SearchHBSKActivity.this, mReason);
			}
			SearchHBSKActivity.this.dismissProgressDialog();
		}

		protected void onPreExecute() {
			if (DEBUG)
				Log.d("TodayActivity", "onPreExecute()");
			startProgressBar("获取城市列表", "加载中...");
		}
	}

	private void getCity(View v) {
		String city = ((TextView) v).getText().toString();
		startCityEdit.setText(city.split("-")[0]);
		endCityEdit.setText(city.split("-")[1]);
	}
}
