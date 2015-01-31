package com.tudaidai.tuantrip;

import com.tudaidai.tuantrip.app.TuanTripApp;
import com.tudaidai.tuantrip.types.UserResult;
import com.tudaidai.tuantrip.util.NotificationsUtil;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity {
	static final boolean DEBUG = TuanTripSettings.DEBUG;
	public static final String EXTRA_OPTION = "com.tudaidai.tuantrip.LoginActivity.OPTION";
	public static final int OPTION_ADDADDR = 6;
	public static final int OPTION_ACCOUNT = 4;
	public static final int OPTION_COIN = 5;
	public static final int OPTION_ORDER = 8;
	public static final int OPTION_SET = 3;
	public static final int OPTION_SUBMIT_ORDER = 7;
	public static final int OPTION_DO_NOTHING = 9;
	static final String TAG = "LoginActivity";
	private CheckBox mAutoCb;
	private int mAutoLogin = 0;
	private int mOption = 0;
	private ProgressDialog mProgressDialog;
	private EditText mPwdEdit;
	private EditText mUserEdit;

	private void dismissProgressDialog() {
		mProgressDialog.dismiss();
	}

	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.login_activity);
		ensureUi();
		mOption = getIntent().getExtras().getInt(EXTRA_OPTION);
		TuanTrip tuantrip = (TuanTrip) getApplication();
		mUserEdit.setText(tuantrip.getLoginUser());
		String userName = tuantrip.getUserName();
		String pwd = tuantrip.getPwd();
		if (userName.length() > 0 && pwd.length() > 0) {
			mAutoLogin = 1;
			mAutoCb.setChecked(true);
			mPwdEdit.setText(pwd);
			LoginTask logintask = new LoginTask();
			String as[] = new String[3];
			as[0] = userName;
			as[1] = pwd;
			as[2] = "1";
			logintask.execute(as);
		}
		Button loginButton = (Button) findViewById(R.id.loginButton);
		loginButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mUserEdit.getText().toString().trim().equals("")) {
					Toast.makeText(LoginActivity.this, "请输入用户名",
							Toast.LENGTH_SHORT).show();
					return;
				} else if (mPwdEdit.getText().toString().trim().equals("")) {
					Toast.makeText(LoginActivity.this, "请输入密码",
							Toast.LENGTH_SHORT).show();
					return;
				}

				LoginTask logintask = new LoginTask();
				String as[] = new String[3];
				as[0] = mUserEdit.getText().toString();
				as[1] = mPwdEdit.getText().toString();
				as[2] = "1";
				logintask.execute(as);

			}
		});
		TextView registerText = (TextView) findViewById(R.id.registerText);
		registerText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_VIEW, Uri
						.parse("http://passport.tuantrip.com/reg.php"));
				startActivity(intent);

			}
		});
	}

	private void startProgressBar() {
		if (mProgressDialog == null) {
			mProgressDialog = ProgressDialog.show(this, "登录", "登录中...");
		}
		mProgressDialog.show();
	}

	private void ensureUi() {
		mUserEdit = (EditText) findViewById(R.id.userEdit);
		mPwdEdit = (EditText) findViewById(R.id.pwdEdit);
		mAutoCb = (CheckBox) findViewById(R.id.autoCb);
		mAutoCb.setChecked(true);

	}

	private void onTaskComplete(UserResult paramUserResult, Exception exception) {
		TuanTrip tuanTrip = (TuanTrip) getApplication();
		if (paramUserResult == null) {
			NotificationsUtil.ToastReasonForFailure(this, exception);
		} else if (paramUserResult.mHttpResult.getStat() == TuanTripSettings.POSTSUCCESS) {
			Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
			tuanTrip.storeLoginUser(paramUserResult.mUser.getEmail());
			paramUserResult.mUser.setPwd(mPwdEdit.getText().toString());
			if (mAutoCb.isChecked()) {
				tuanTrip.setUser(paramUserResult.mUser);
			} else {
				tuanTrip.setUserOnly(paramUserResult.mUser);
			}

			if (mOption == TodayActivity.BUY) {
				Intent intent = new Intent(this, BuyActivity.class);
				startActivity(intent);
			} else if (mOption == TodayActivity.BUYHOTEL) {
				Intent intent = new Intent(this, BuyHotelActivity.class);
				startActivity(intent);
			} else if (mOption == TodayActivity.BUYXIANLU) {
				Intent intent = new Intent(this, BuyXianluActivity.class);
				startActivity(intent);
			} else if (mOption == OPTION_SET) {
				Intent intent = new Intent(this, SetActivity.class);
				startActivity(intent);
			} else if (mOption == OPTION_ADDADDR
					|| mOption == OPTION_SUBMIT_ORDER
					|| mOption == OPTION_DO_NOTHING) {

			} else if (mOption == OPTION_ACCOUNT) {
				Intent intent = new Intent(this, AccountActivity.class);
				startActivity(intent);
			}

			finish();
		} else if (paramUserResult.mHttpResult.getStat() == TuanTripSettings.POSTFAILED) {
			Toast.makeText(this, paramUserResult.mHttpResult.getMessage(),
					Toast.LENGTH_SHORT).show();
		} else {
			NotificationsUtil.ToastReasonForFailure(this, exception);
		}

		dismissProgressDialog();

	}

	class LoginTask extends AsyncTask<String, Void, UserResult> {
		private static final boolean DEBUG = TuanTripSettings.DEBUG;
		private static final String TAG = "LoginTask";
		private Exception mReason = null;

		private LoginTask() {
		}

		protected UserResult doInBackground(String... paramArrayOfString) {
			if (DEBUG)
				Log.d("LoginTask", "doInBackground()");
			UserResult localUserResult = null;
			try {
				TuanTripApp localTuanTripApp = ((TuanTrip) LoginActivity.this
						.getApplication()).getTuanTripApp();
				String str1 = paramArrayOfString[0];
				String str2 = paramArrayOfString[1];
				String str3 = paramArrayOfString[2];
				localUserResult = localTuanTripApp.login(str1, str2, str3);
			} catch (Exception localException) {
				if (DEBUG)
					Log.d("LoginTask", "Caught Exception logging in.",
							localException);
				this.mReason = localException;
				localUserResult = null;
			}
			return localUserResult;
		}

		protected void onCancelled() {
			super.onCancelled();
			LoginActivity.this.dismissProgressDialog();
		}

		protected void onPostExecute(UserResult paramUserResult) {
			LoginActivity localLoginActivity = LoginActivity.this;
			Exception localException = this.mReason;
			localLoginActivity.onTaskComplete(paramUserResult, localException);
		}

		protected void onPreExecute() {
			if (DEBUG)
				Log.d("LoginTask", "onPreExecute()");
			LoginActivity.this.startProgressBar();
		}
	}
}
