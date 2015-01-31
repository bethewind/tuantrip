package com.tudaidai.tuantrip;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.tudaidai.tuantrip.app.TuanTripApp;
import com.tudaidai.tuantrip.types.ViewpointDetailResult;
import com.tudaidai.tuantrip.util.NotificationsUtil;

public class ViewpointDetailActivity extends BaseLbsActivity {

	public static final String VID = "com.tudaidai.tuantrip.ViewpointDetailActivity.vid";
	static final boolean DEBUG = TuanTripSettings.DEBUG;
	static final String TAG = "ViewpointDetailActivity";
	private static final int MENU_REFRESH = 0;
	ViewpointDetailResult vDetailResult;
	double price = 0.0;
	int vid;

	View menpiaoView;
	View jiaotongView;

	private void ensureUi(ViewpointDetailResult paramOrders) {
		vDetailResult = paramOrders;

		super.ensure(vDetailResult.viewpointDetail);
		Button menpiaoTab = (Button) findViewById(R.id.menpiaoTab);
		menpiaoTab.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {

					content.removeAllViews();
					if (menpiaoView == null) {
						menpiaoView = mInflater.inflate(R.layout.lbs_common,
								null);

						detailText = (TextView) menpiaoView
								.findViewById(R.id.detailText);

						detailText.setText(Html
								.fromHtml(vDetailResult.viewpointDetail
										.getMenpiao()));
						detailText.setMovementMethod(LinkMovementMethod
								.getInstance());
						content.addView(menpiaoView);
					} else {
						content.addView(menpiaoView);
					}

				}

			}
		});
		menpiaoTab.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
			}
		});
		Button jiaotongTab = (Button) findViewById(R.id.jiaotongTab);
		jiaotongTab.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {

					content.removeAllViews();
					if (jiaotongView == null) {
						jiaotongView = mInflater.inflate(R.layout.lbs_common,
								null);

						detailText = (TextView) jiaotongView
								.findViewById(R.id.detailText);

						detailText.setText(Html
								.fromHtml(vDetailResult.viewpointDetail
										.getJiaotong()));
						detailText.setMovementMethod(LinkMovementMethod
								.getInstance());
						content.addView(jiaotongView);
					} else {
						content.addView(jiaotongView);
					}

				}

			}
		});
		jiaotongTab.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
			}
		});
	}

	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.viewpoint_detail_activity);

		vid = getIntent().getIntExtra(VID, -1);

		String[] as = new String[1];
		as[0] = new Integer(vid).toString();
		new ViewPointDetailTask().execute(as);

	}

	class ViewPointDetailTask extends
			AsyncTask<String, Void, ViewpointDetailResult> {
		private static final String TAG = "ViewPointDetailTask";
		private Exception mReason;

		public ViewPointDetailTask() {
		}

		protected ViewpointDetailResult doInBackground(
				String... paramArrayOfString) {
			if (DEBUG)
				Log.d(TAG, "doInBackground()");
			ViewpointDetailResult vDetailResult = null;
			try {
				TuanTripApp localTuanTripApp = ((TuanTrip) ViewpointDetailActivity.this
						.getApplication()).getTuanTripApp();
				vDetailResult = localTuanTripApp
						.getViewPointDetailResult(paramArrayOfString);

				if (DEBUG)
					Log.d(TAG, "doInBackground() over");

			} catch (Exception localException) {
				this.mReason = localException;
				vDetailResult = null;
			}

			return vDetailResult;
		}

		protected void onPostExecute(ViewpointDetailResult paramOrders) {
			if (DEBUG)
				Log.d(TAG, "onPostExecute()");
			if (paramOrders != null
					&& paramOrders.mHttpResult.getStat() == TuanTripSettings.POSTSUCCESS) {

				ensureUi(paramOrders);
			} else {
				if (paramOrders == null) {
					NotificationsUtil.ToastReasonForFailure(
							ViewpointDetailActivity.this, mReason);
				} else if (paramOrders.mHttpResult.getStat() == TuanTripSettings.POSTFAILED) {
					Toast.makeText(ViewpointDetailActivity.this,
							paramOrders.mHttpResult.getMessage(),
							Toast.LENGTH_SHORT).show();
				} else {
					NotificationsUtil.ToastReasonForFailure(
							ViewpointDetailActivity.this, mReason);
				}
			}

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
			startProgressBar("加载信息", "加载中...");
		}
	}

}
