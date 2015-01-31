package com.tudaidai.tuantrip;

import java.util.ArrayList;
import java.util.Iterator;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.tudaidai.tuantrip.app.TuanTripApp;
import com.tudaidai.tuantrip.types.AddrInfo;
import com.tudaidai.tuantrip.types.AddrListResult;
import com.tudaidai.tuantrip.types.LogOutResult;
import com.tudaidai.tuantrip.types.User;
import com.tudaidai.tuantrip.util.NotificationsUtil;

public class SetActivity extends Activity {
	static final boolean DEBUG = TuanTripSettings.DEBUG;
	private static final int RESULT_CODE_ACTIVITY_EDIT_ADDR = 1;
	static final String TAG = "SetActivity";
	private ArrayList<AddrInfo> mAddrList = null;
	private ProgressDialog mProgressDialog;
	private User mUser;
	private RadioGroup picRadioGroup;
	private RadioButton showPicButton;
	private RadioButton noShowPicButton;

	private void onAddrTaskComplete(AddrListResult addrlistresult,
			Exception exception) {
		if (addrlistresult == null) {
			NotificationsUtil.ToastReasonForFailure(this, exception);
		} else if (addrlistresult.mHttpResult.getStat() == TuanTripSettings.POSTSUCCESS) {
			mAddrList = new ArrayList<AddrInfo>();
			Iterator localIterator = addrlistresult.mGroup.iterator();
			while (localIterator.hasNext()) {
				mAddrList.add((AddrInfo) localIterator.next());
			}
			Intent localIntent1 = new Intent(this, AddrActivity.class);
			localIntent1.putExtra(AddrActivity.EXTRA_OPTION,
					AddrActivity.OPTION_SET);
			localIntent1.putExtra(AddrActivity.EXTRA_ADDR, mAddrList);
			startActivity(localIntent1);
		} else if (addrlistresult.mHttpResult.getStat() == TuanTripSettings.POSTFAILED) {
			Toast.makeText(this, addrlistresult.mHttpResult.getMessage(),
					Toast.LENGTH_SHORT).show();
		} else if (addrlistresult.mHttpResult.getStat() == TuanTripSettings.LOGINFAILED) {
			Toast.makeText(this, addrlistresult.mHttpResult.getMessage(),
					Toast.LENGTH_SHORT).show();
			((TuanTrip) getApplication()).loginOut();
			Intent localIntent4 = new Intent(this, LoginActivity.class);
			localIntent4.putExtra(LoginActivity.EXTRA_OPTION,
					LoginActivity.OPTION_SET);
			startActivity(localIntent4);
			finish();
		} else {
			NotificationsUtil.ToastReasonForFailure(this, exception);
		}

		dismissProgressDialog();
	}

	private void startProgressBar(String title, String content) {
		mProgressDialog = ProgressDialog.show(this, title, content);
		mProgressDialog.show();
	}

	private void dismissProgressDialog() {
		this.mProgressDialog.dismiss();
	}

	private void ensureUi() {
		TuanTrip tuanTrip = (TuanTrip) getApplication();
		showPicButton = (RadioButton) findViewById(R.id.showPicButton);
		noShowPicButton = (RadioButton) findViewById(R.id.noShowPicButton);
		if (tuanTrip.getShowPic()) {
			showPicButton.setChecked(true);
		} else {
			noShowPicButton.setChecked(true);
		}
		TextView addrClick = (TextView) findViewById(R.id.addrClick);
		addrClick.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Void[] voids = new Void[0];
				new AddrTask().execute(voids);

			}
		});
		TextView privateClick = (TextView) findViewById(R.id.privateClick);
		if (mUser != null) {
			privateClick.setText(mUser.getUname());
		}
		// privateClick.setOnClickListener(new OnClickListener(){
		// @Override
		// public void onClick(View v)
		// {
		// Intent intent = new Intent(SetActivity.this,PrivateActivity.class);
		// startActivity(intent);
		//
		// }});

		picRadioGroup = (RadioGroup) findViewById(R.id.picRadioGroup);
		picRadioGroup
				.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						if (checkedId == showPicButton.getId()) {
							setShowPic(true);
						}
						if (checkedId == noShowPicButton.getId()) {
							setShowPic(false);
						}

					}
				});
		TextView exitButton = (TextView) findViewById(R.id.exitButton);
		exitButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Void[] voids = new Void[0];
				new LogOutTask().execute(voids);

			}
		});
	}

	protected void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		try {
			User localUser = ((TuanTrip) getApplication()).getUser();
			this.mUser = localUser;
			if (this.mUser == null) {
				Intent localIntent1 = new Intent(this, LoginActivity.class);
				localIntent1.putExtra(LoginActivity.EXTRA_OPTION,
						LoginActivity.OPTION_SET);
				startActivity(localIntent1);
				finish();

			}

		} catch (Exception localException) {
		}
		setContentView(R.layout.set_activity);
		ensureUi();

	}

	private void setShowPic(boolean isShowPic) {
		TuanTrip tuanTrip = (TuanTrip) getApplication();
		tuanTrip.setShowPic(isShowPic);
	}

	class AddrTask extends AsyncTask<Void, Void, AddrListResult> {
		private static final boolean DEBUG = TuanTripSettings.DEBUG;
		private static final String TAG = "AddrTask";
		private Exception mReason = null;

		private AddrTask() {
		}

		protected AddrListResult doInBackground(Void... paramArrayOfVoid) {
			if (DEBUG)
				Log.d(TAG, "doInBackground()");
			AddrListResult localAddrListResult1;
			try {
				TuanTrip tuanTrip = (TuanTrip) SetActivity.this
						.getApplication();
				TuanTripApp tuanTripApp = tuanTrip.getTuanTripApp();
				localAddrListResult1 = tuanTripApp.getAddress();

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
			SetActivity.this.dismissProgressDialog();
		}

		protected void onPostExecute(AddrListResult paramAddrListResult) {
			if (DEBUG)
				Log.d(TAG, "onPostExecute(): ");
			SetActivity.this.onAddrTaskComplete(paramAddrListResult,
					this.mReason);
		}

		protected void onPreExecute() {
			if (DEBUG)
				Log.d(TAG, "onPreExecute()");
			startProgressBar("获取地址", "获取地址中...");
		}
	}

	class LogOutTask extends AsyncTask<Void, Void, LogOutResult> {
		private static final boolean DEBUG = TuanTripSettings.DEBUG;
		private static final String TAG = "LogOutTask";
		private Exception mReason = null;

		private LogOutTask() {
		}

		protected LogOutResult doInBackground(Void... paramArrayOfVoid) {
			if (DEBUG)
				Log.d(TAG, "doInBackground()");
			LogOutResult localAddrListResult1;
			try {
				TuanTrip tuanTrip = (TuanTrip) SetActivity.this
						.getApplication();
				TuanTripApp tuanTripApp = tuanTrip.getTuanTripApp();
				localAddrListResult1 = tuanTripApp.logOut();

			} catch (Exception localException) {
				if (DEBUG)
					Log.d(TAG, "Caught Exception.", localException);
				this.mReason = localException;
				localAddrListResult1 = null;
			}
			return localAddrListResult1;
		}

		protected void onCancelled() {
			super.onCancelled();
			SetActivity.this.dismissProgressDialog();
		}

		protected void onPostExecute(LogOutResult logOutResult) {
			if (DEBUG)
				Log.d(TAG, "onPostExecute(): ");
			if (logOutResult == null) {
				NotificationsUtil.ToastReasonForFailure(SetActivity.this,
						mReason);
			} else if (logOutResult.mHttpResult.getStat() == TuanTripSettings.POSTSUCCESS) {
				Toast.makeText(SetActivity.this,
						logOutResult.mHttpResult.getMessage(),
						Toast.LENGTH_SHORT).show();
				((TuanTrip) getApplication()).loginOut();
				Intent localIntent4 = new Intent(SetActivity.this,
						LoginActivity.class);
				localIntent4.putExtra(LoginActivity.EXTRA_OPTION,
						LoginActivity.OPTION_SET);
				startActivity(localIntent4);
				finish();
			} else if (logOutResult.mHttpResult.getStat() == TuanTripSettings.POSTFAILED) {
				Toast.makeText(SetActivity.this,
						logOutResult.mHttpResult.getMessage(),
						Toast.LENGTH_SHORT).show();
			} else {
				NotificationsUtil.ToastReasonForFailure(SetActivity.this,
						mReason);
			}

			dismissProgressDialog();
		}

		protected void onPreExecute() {
			if (DEBUG)
				Log.d(TAG, "onPreExecute()");
			startProgressBar("注销登录", "注销中...");
		}
	}
}
