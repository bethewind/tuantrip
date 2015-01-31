package com.tudaidai.tuantrip;

import com.tudaidai.tuantrip.app.TuanTripApp;
import com.tudaidai.tuantrip.types.BaseResult;
import com.tudaidai.tuantrip.types.User;
import com.tudaidai.tuantrip.util.NotificationsUtil;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class PrivateActivity extends Activity {
	static final boolean DEBUG = true;
	static final String TAG = "PrivateActivity";
	private EditText mEmailEdit;
	private EditText mNewPwdEdit;
	private EditText mOldPwdEdit;
	private EditText mPhoneEdit;
	private ProgressDialog mProgressDialog;
	private EditText mRePwdEdit;
	private User mUser;
	private EditText mUserEdit;

	private void dismissProgressDialog() {
		mProgressDialog.dismiss();
	}

	private void startProgressBar(String title, String content) {
		mProgressDialog = ProgressDialog.show(this, title, content);
		mProgressDialog.show();
	}

	private void onUpdateTaskComplete(BaseResult baseResult, Exception exception) {

		if (baseResult == null) {
			NotificationsUtil.ToastReasonForFailure(this, exception);
		} else if (baseResult.mHttpResult.getStat() == TuanTripSettings.POSTSUCCESS) {
			Toast.makeText(this, baseResult.mHttpResult.getMessage(),
					Toast.LENGTH_SHORT).show();
			((TuanTrip) getApplication()).loginOut();
			Intent localIntent4 = new Intent(this, LoginActivity.class);
			localIntent4.putExtra(LoginActivity.EXTRA_OPTION,
					LoginActivity.OPTION_SET);
			startActivity(localIntent4);
			finish();
		} else if (baseResult.mHttpResult.getStat() == TuanTripSettings.POSTFAILED) {
			Toast.makeText(this, baseResult.mHttpResult.getMessage(),
					Toast.LENGTH_SHORT).show();
		} else if (baseResult.mHttpResult.getStat() == TuanTripSettings.LOGINFAILED) {
			Toast.makeText(this, baseResult.mHttpResult.getMessage(),
					Toast.LENGTH_SHORT).show();
			((TuanTrip) getApplication()).loginOut();
			Intent localIntent4 = new Intent(this, LoginActivity.class);
			localIntent4.putExtra(LoginActivity.EXTRA_OPTION,
					LoginActivity.OPTION_SUBMIT_ORDER);
			startActivity(localIntent4);
		} else {
			NotificationsUtil.ToastReasonForFailure(this, exception);
		}

		dismissProgressDialog();

	}

	private void ensureUi() {
		mEmailEdit = (EditText) findViewById(R.id.emailEdit);
		mEmailEdit.setText(mUser.getEmail());
		mUserEdit = (EditText) findViewById(R.id.userEdit);
		mUserEdit.setText(mUser.getUname());
		mPhoneEdit = (EditText) findViewById(R.id.phoneEdit);
		mPhoneEdit.setText(mUser.getPhone());
		mOldPwdEdit = (EditText) findViewById(R.id.oldPwdEdit);
		mNewPwdEdit = (EditText) findViewById(R.id.newPwdEdit);
		mRePwdEdit = (EditText) findViewById(R.id.rePwdEdit);

		Button okButton = (Button) findViewById(R.id.okButton);
		okButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!mNewPwdEdit.getText().toString().trim()
						.equals(mRePwdEdit.getText().toString().trim())) {
					Toast.makeText(PrivateActivity.this, "两次密码不一致",
							Toast.LENGTH_SHORT).show();
					return;
				}

				String[] as = new String[4];
				as[0] = mPhoneEdit.getText().toString();
				as[1] = mOldPwdEdit.getText().toString();
				as[2] = mNewPwdEdit.getText().toString();
				as[3] = mRePwdEdit.getText().toString();
				new UpdateTask().execute(as);

			}
		});
	}

	protected void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		setContentView(R.layout.private_activity);
		this.mUser = ((TuanTrip) getApplication()).getUser();
		ensureUi();
	}

	class UpdateTask extends AsyncTask<String, Void, BaseResult> {
		private static final boolean DEBUG = TuanTripSettings.DEBUG;
		private static final String TAG = "UpdateTask";
		private Exception mReason = null;

		private UpdateTask() {
		}

		protected BaseResult doInBackground(String... paramArray) {
			if (DEBUG)
				Log.d(TAG, "doInBackground()");
			BaseResult baseResult = null;
			try {
				TuanTrip tuanTrip = (TuanTrip) PrivateActivity.this
						.getApplication();
				TuanTripApp tuanTripApp = tuanTrip.getTuanTripApp();
				baseResult = tuanTripApp.updateUser(paramArray);

			} catch (Exception localException) {
				if (DEBUG)
					Log.d(TAG, "Caught Exception.", localException);
				this.mReason = localException;
				baseResult = null;
			}
			return baseResult;
		}

		protected void onCancelled() {
			super.onCancelled();
			dismissProgressDialog();
		}

		protected void onPostExecute(BaseResult baseResult) {
			if (DEBUG)
				Log.d(TAG, "onPostExecute(): ");
			onUpdateTaskComplete(baseResult, this.mReason);
		}

		protected void onPreExecute() {
			if (DEBUG)
				Log.d(TAG, "onPreExecute()");
			startProgressBar("提交资料", "提交中...");
		}
	}
}
