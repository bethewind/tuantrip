package com.tudaidai.tuantrip;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.tudaidai.tuantrip.app.TuanTripApp;
import com.tudaidai.tuantrip.types.AddrInfo;
import com.tudaidai.tuantrip.types.Group;
import com.tudaidai.tuantrip.types.HBSKResult;
import com.tudaidai.tuantrip.util.NotificationsUtil;
import com.tudaidai.tuantrip.widget.AddrListAdapter;

public class HBSKActivity extends Activity {
	static final boolean DEBUG = TuanTripSettings.DEBUG;
	public static final String EXTRA_OPTION = "com.tudaidai.tuantrip.HBSKActivity.OPTION";
	public static final int OPTION_ORDER = 1;
	public static final int OPTION_SET = 2;
	public static final int REQUEST_CODE = 1;
	public static final int RESPONSE_SUCCESS = 1;
	public static final int RESPONSE_FAILED = 2;
	static final String TAG = "HBSKActivity";
	private AddrListAdapter mListAdapter;
	private ArrayList<AddrInfo> mAddr;
	private Group<AddrInfo> mgAddr = new Group<AddrInfo>();
	private int mOption;
	private ProgressDialog mProgressDialog;
	TextView hangbanshikeText;

	private void startProgressBar(String title, String content) {
		mProgressDialog = ProgressDialog.show(this, title, content);
		mProgressDialog.show();
	}

	private void dismissProgressDialog() {
		this.mProgressDialog.dismiss();
	}

	protected void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		setContentView(R.layout.hangban_shike_activity);

		hangbanshikeText = (TextView) findViewById(R.id.hangbanshikeText);
		String[] as = getIntent().getStringArrayExtra(EXTRA_OPTION);
		TextView hanbantitle = (TextView) findViewById(R.id.hangbanTitle);
		hanbantitle.setText("" + as[0] + "-" + as[1] + "最新航班时刻表：");
		new HBSKTask().execute(as);

	}

	class HBSKTask extends AsyncTask<String, Void, HBSKResult> {
		private static final boolean DEBUG = TuanTripSettings.DEBUG;
		private static final String TAG = "HBSKTask";
		private Exception mReason = null;

		private HBSKTask() {
		}

		protected HBSKResult doInBackground(String... paramArrayOfVoid) {
			if (DEBUG)
				Log.d(TAG, "doInBackground()");
			HBSKResult localAddrListResult1;
			try {
				TuanTrip tuanTrip = (TuanTrip) HBSKActivity.this
						.getApplication();
				TuanTripApp tuanTripApp = tuanTrip.getTuanTripApp();
				localAddrListResult1 = tuanTripApp
						.getHBSKResult(paramArrayOfVoid);

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
			HBSKActivity.this.dismissProgressDialog();
		}

		protected void onPostExecute(HBSKResult hbskResult) {
			if (DEBUG)
				Log.d(TAG, "onPostExecute(): ");
			if (hbskResult == null) {
				NotificationsUtil.ToastReasonForFailure(HBSKActivity.this,
						mReason);
			} else if (hbskResult.mHttpResult.getStat() == TuanTripSettings.POSTSUCCESS) {
				hangbanshikeText.setText(Html.fromHtml(hbskResult.mResult));

			} else if (hbskResult.mHttpResult.getStat() == TuanTripSettings.POSTFAILED) {
				Toast.makeText(HBSKActivity.this,
						hbskResult.mHttpResult.getMessage(), Toast.LENGTH_SHORT)
						.show();
			} else {
				NotificationsUtil.ToastReasonForFailure(HBSKActivity.this,
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
