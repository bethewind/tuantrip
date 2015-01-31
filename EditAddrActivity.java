package com.tudaidai.tuantrip;

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

import com.tudaidai.tuantrip.app.TuanTripApp;
import com.tudaidai.tuantrip.types.AddAddressResult;
import com.tudaidai.tuantrip.types.AddrInfo;
import com.tudaidai.tuantrip.util.NotificationsUtil;

public class EditAddrActivity extends Activity {
	static final boolean DEBUG = TuanTripSettings.DEBUG;
	static final String TAG = "EditAddrActivity";
	public static final String EXTRA_ADDR = "com.tudaidai.tuantrip.EditAddrActivity.ADDR";
	public static final String EXTRA_OPTION = "com.tudaidai.tuantrip.EditAddrActivity.OPTION";
	public static final int OPTION_EDIT = 1;
	public static final int OPTION_ADD = 2;
	private int option;
	private AddrInfo addrInfo;
	private EditText provinceEdit;
	private EditText cityEdit;
	private EditText areaEdit;
	private EditText houseEdit;
	private EditText codeEdit;
	private EditText phoneEdit;
	private EditText telEdit;
	private EditText receiveEdit;
	private ProgressDialog mProgressDialog;

	private void startProgressBar(String title, String content) {
		mProgressDialog = ProgressDialog.show(this, title, content);
		mProgressDialog.show();
	}

	private void dismissProgressDialog() {
		this.mProgressDialog.dismiss();
	}

	private void onAddAddrTaskComplete(AddAddressResult addAddressResult,
			Exception exception) {
		if (addAddressResult == null) {
			NotificationsUtil.ToastReasonForFailure(this, exception);
		} else if (addAddressResult.mHttpResult.getStat() == TuanTripSettings.POSTSUCCESS) {
			Toast.makeText(this, addAddressResult.mHttpResult.getMessage(),
					Toast.LENGTH_SHORT).show();
			setResult(AddrActivity.RESPONSE_SUCCESS);
			finish();
		} else if (addAddressResult.mHttpResult.getStat() == TuanTripSettings.POSTFAILED) {
			Toast.makeText(this, addAddressResult.mHttpResult.getMessage(),
					Toast.LENGTH_SHORT).show();
		} else if (addAddressResult.mHttpResult.getStat() == TuanTripSettings.LOGINFAILED) {
			Toast.makeText(this, addAddressResult.mHttpResult.getMessage(),
					Toast.LENGTH_SHORT).show();
			((TuanTrip) getApplication()).loginOut();
			Intent localIntent4 = new Intent(this, LoginActivity.class);
			localIntent4.putExtra(LoginActivity.EXTRA_OPTION,
					LoginActivity.OPTION_ADDADDR);
			startActivity(localIntent4);
		} else {
			NotificationsUtil.ToastReasonForFailure(this, exception);
		}

		dismissProgressDialog();
	}

	private void ensureUi() {
		provinceEdit = (EditText) findViewById(R.id.provinceEdit);
		cityEdit = (EditText) findViewById(R.id.cityEdit);
		areaEdit = (EditText) findViewById(R.id.areaEdit);
		houseEdit = (EditText) findViewById(R.id.houseEdit);
		codeEdit = (EditText) findViewById(R.id.codeEdit);
		phoneEdit = (EditText) findViewById(R.id.phoneEdit);
		telEdit = (EditText) findViewById(R.id.telEdit);
		receiveEdit = (EditText) findViewById(R.id.receiveEdit);
		if (option == OPTION_EDIT) {
			addrInfo = getIntent().getExtras().getParcelable(EXTRA_ADDR);
			provinceEdit.setText(addrInfo.getProvince());
			cityEdit.setText(addrInfo.getCity());
			areaEdit.setText(addrInfo.getArea());
			houseEdit.setText(addrInfo.getHouse());
			codeEdit.setText(addrInfo.getZip());
			phoneEdit.setText(addrInfo.getPhone());
			telEdit.setText(addrInfo.getTel());
			receiveEdit.setText(addrInfo.getConsiName());
		}

		Button cancelButton = (Button) findViewById(R.id.cancelButton);
		cancelButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();

			}
		});
		Button okButton = (Button) findViewById(R.id.okButton);
		okButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (provinceEdit.getText().toString().trim().equals("")) {
					Toast.makeText(EditAddrActivity.this, "请填写省",
							Toast.LENGTH_SHORT).show();
					return;
				} else if (cityEdit.getText().toString().trim().equals("")) {
					Toast.makeText(EditAddrActivity.this, "请填写市",
							Toast.LENGTH_SHORT).show();
					return;
				} else if (areaEdit.getText().toString().trim().equals("")) {
					Toast.makeText(EditAddrActivity.this, "请填写区",
							Toast.LENGTH_SHORT).show();
					return;
				} else if (houseEdit.getText().toString().trim().equals("")) {
					Toast.makeText(EditAddrActivity.this, "请填写街道地址",
							Toast.LENGTH_SHORT).show();
					return;
				} else if (codeEdit.getText().toString().trim().equals("")) {
					Toast.makeText(EditAddrActivity.this, "请填写邮政编码",
							Toast.LENGTH_SHORT).show();
					return;
				} else if (phoneEdit.getText().toString().trim().equals("")) {
					Toast.makeText(EditAddrActivity.this, "请填写手机号",
							Toast.LENGTH_SHORT).show();
					return;
				} else if (receiveEdit.getText().toString().trim().equals("")) {
					Toast.makeText(EditAddrActivity.this, "请填写收件人",
							Toast.LENGTH_SHORT).show();
					return;
				}

				String as[] = new String[8];
				as[0] = provinceEdit.getText().toString().trim();
				as[1] = cityEdit.getText().toString().trim();
				as[2] = areaEdit.getText().toString().trim();
				as[3] = houseEdit.getText().toString().trim();
				as[4] = codeEdit.getText().toString().trim();
				as[5] = phoneEdit.getText().toString().trim();
				as[6] = telEdit.getText().toString().trim();
				as[7] = receiveEdit.getText().toString().trim();
				new AddAddrTask().execute(as);
			}
		});
	}

	protected void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		setContentView(R.layout.edit_addr_activity);
		if ((getIntent() == null) || (getIntent().getExtras() == null)
				|| (!getIntent().getExtras().containsKey(EXTRA_OPTION))) {
			finish();
			return;
		}
		option = getIntent().getExtras().getInt(EXTRA_OPTION);
		ensureUi();
	}

	class AddAddrTask extends AsyncTask<String, Void, AddAddressResult> {
		private static final boolean DEBUG = TuanTripSettings.DEBUG;
		private static final String TAG = "AddAddrTask";
		private Exception mReason = null;

		private AddAddrTask() {
		}

		protected AddAddressResult doInBackground(String... paramArray) {
			if (DEBUG)
				Log.d(TAG, "doInBackground()");
			AddAddressResult localAddrListResult1 = null;
			try {
				TuanTrip tuanTrip = (TuanTrip) EditAddrActivity.this
						.getApplication();
				TuanTripApp tuanTripApp = tuanTrip.getTuanTripApp();
				if (option == OPTION_ADD) {
					localAddrListResult1 = tuanTripApp.addAddress(paramArray);
				} else if (option == OPTION_EDIT) {
					localAddrListResult1 = tuanTripApp.editAddress(
							((Integer) addrInfo.getAid()).toString(),
							paramArray);
				}

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
			dismissProgressDialog();
		}

		protected void onPostExecute(AddAddressResult paramAddrListResult) {
			if (DEBUG)
				Log.d(TAG, "onPostExecute(): ");
			onAddAddrTaskComplete(paramAddrListResult, this.mReason);
		}

		protected void onPreExecute() {
			if (DEBUG)
				Log.d(TAG, "onPreExecute()");
			if (option == OPTION_ADD) {
				startProgressBar("添加地址", "添加地址中...");
			} else if (option == OPTION_EDIT) {
				startProgressBar("编辑地址", "编辑地址中...");
			}
		}
	}
}
