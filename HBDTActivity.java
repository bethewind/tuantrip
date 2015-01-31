package com.tudaidai.tuantrip;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.tudaidai.tuantrip.app.TuanTripApp;
import com.tudaidai.tuantrip.types.HBSKResult;
import com.tudaidai.tuantrip.util.NotificationsUtil;

public class HBDTActivity extends Activity {
	static final boolean DEBUG = TuanTripSettings.DEBUG;
	public static final String EXTRA_OPTION = "com.tudaidai.tuantrip.HBDTActivity.OPTION";
	public static final String EXTRA_OPTION1 = "com.tudaidai.tuantrip.HBDTActivity.OPTION1";
	public static final int OPTION_ORDER = 1;
	public static final int OPTION_SET = 2;
	public static final int REQUEST_CODE = 1;
	public static final int RESPONSE_SUCCESS = 1;
	public static final int RESPONSE_FAILED = 2;
	static final String TAG = "HBDTActivity";

	private ProgressDialog mProgressDialog;
	TextView hangbandtText;

	private void startProgressBar(String title, String content) {
		mProgressDialog = ProgressDialog.show(this, title, content);
		mProgressDialog.show();
	}

	private void dismissProgressDialog() {
		this.mProgressDialog.dismiss();
	}

	protected void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		setContentView(R.layout.hangban_dongtai_activity);

		hangbandtText = (TextView) findViewById(R.id.hangbandtText);
		String[] as = getIntent().getStringArrayExtra(EXTRA_OPTION);

		if (as != null) {
			new HBDTTask().execute(as);
		} else {
			String[] as1 = getIntent().getStringArrayExtra(EXTRA_OPTION1);
			if (as1 != null) {
				new HBDTTask1().execute(as1);
			}
		}

	}

	class HBDTTask extends AsyncTask<String, Void, HBSKResult> {
		private static final boolean DEBUG = TuanTripSettings.DEBUG;
		private static final String TAG = "HBSKTask";
		private Exception mReason = null;

		private HBDTTask() {
		}

		protected HBSKResult doInBackground(String... paramArrayOfVoid) {
			if (DEBUG)
				Log.d(TAG, "doInBackground()");
			HBSKResult localAddrListResult1;
			try {
				TuanTrip tuanTrip = (TuanTrip) HBDTActivity.this
						.getApplication();
				TuanTripApp tuanTripApp = tuanTrip.getTuanTripApp();
				localAddrListResult1 = tuanTripApp
						.getHBDTResult(paramArrayOfVoid);

			} catch (Exception localException) {
				if (DEBUG)
					Log.d("AddrTask", "Caught Exception.", localException);
				this.mReason = localException;
				localAddrListResult1 = null;
			}
			return localAddrListResult1;
		}

		protected void onCancelled() {
			super.onCancelled();
			HBDTActivity.this.dismissProgressDialog();
		}

		protected void onPostExecute(HBSKResult hbskResult) {
			if (DEBUG)
				Log.d(TAG, "onPostExecute(): ");
			if (hbskResult == null) {
				NotificationsUtil.ToastReasonForFailure(HBDTActivity.this,
						mReason);
			} else if (hbskResult.mHttpResult.getStat() == TuanTripSettings.POSTSUCCESS) {
				hangbandtText.setText(Html.fromHtml(hbskResult.mResult));

			} else if (hbskResult.mHttpResult.getStat() == TuanTripSettings.POSTFAILED) {
				Toast.makeText(HBDTActivity.this,
						hbskResult.mHttpResult.getMessage(), Toast.LENGTH_SHORT)
						.show();
			} else {
				NotificationsUtil.ToastReasonForFailure(HBDTActivity.this,
						mReason);
			}

			dismissProgressDialog();
		}

		protected void onPreExecute() {
			if (DEBUG)
				Log.d(TAG, "onPreExecute()");
			startProgressBar("获取航班信息", "获取航班信息中...");
		}
	}

	class HBDTTask1 extends AsyncTask<String, Void, HBSKResult> {
		private static final boolean DEBUG = TuanTripSettings.DEBUG;
		private static final String TAG = "HBSKTask";
		private Exception mReason = null;

		private HBDTTask1() {
		}

		protected HBSKResult doInBackground(String... paramArrayOfVoid) {
			if (DEBUG)
				Log.d(TAG, "doInBackground()");
			HBSKResult localAddrListResult1;
			try {
				TuanTrip tuanTrip = (TuanTrip) HBDTActivity.this
						.getApplication();
				TuanTripApp tuanTripApp = tuanTrip.getTuanTripApp();
				localAddrListResult1 = tuanTripApp
						.getHBDTResult1(paramArrayOfVoid);

			} catch (Exception localException) {
				if (DEBUG)
					Log.d("AddrTask", "Caught Exception.", localException);
				this.mReason = localException;
				localAddrListResult1 = null;
			}
			return localAddrListResult1;
		}

		protected void onCancelled() {
			super.onCancelled();
			HBDTActivity.this.dismissProgressDialog();
		}

		protected void onPostExecute(HBSKResult hbskResult) {
			if (DEBUG)
				Log.d(TAG, "onPostExecute(): ");
			if (hbskResult == null) {
				NotificationsUtil.ToastReasonForFailure(HBDTActivity.this,
						mReason);
			} else if (hbskResult.mHttpResult.getStat() == TuanTripSettings.POSTSUCCESS) {
				hangbandtText.setText(Html.fromHtml(hbskResult.mResult));

			} else if (hbskResult.mHttpResult.getStat() == TuanTripSettings.POSTFAILED) {
				Toast.makeText(HBDTActivity.this,
						hbskResult.mHttpResult.getMessage(), Toast.LENGTH_SHORT)
						.show();
			} else {
				NotificationsUtil.ToastReasonForFailure(HBDTActivity.this,
						mReason);
			}

			dismissProgressDialog();
		}

		protected void onPreExecute() {
			if (DEBUG)
				Log.d(TAG, "onPreExecute()");
			startProgressBar("获取航班信息", "获取航班信息中...");
		}
	}
}
