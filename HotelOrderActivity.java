package com.tudaidai.tuantrip;

import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.tudaidai.tuantrip.app.TuanTripApp;
import com.tudaidai.tuantrip.types.BaseResult;
import com.tudaidai.tuantrip.types.HotelOrderResult;
import com.tudaidai.tuantrip.types.SHotelOrderInfo;
import com.tudaidai.tuantrip.types.User;
import com.tudaidai.tuantrip.util.NotificationsUtil;

public class HotelOrderActivity extends Activity {
	public static final String EXTRA_ARR = "com.tudaidai.tuantrip.HotelOrderActivity.extra_arr";
	static final boolean DEBUG = TuanTripSettings.DEBUG;
	static final String TAG = "HotelOrderActivity";
	protected double price = 0.0; // 单价

	protected int idType = 1; // 证件类型
	private int buyNum = 1;// 预订间数
	private int zuizao = 0;//
	private int zuiwan = 0;//

	private boolean isfirstsubmit = true;// 第一次提交

	protected TextView totalText;
	protected EditText buyNumView;
	protected EditText userNameEdit;
	protected EditText lianxirenEdit;
	protected EditText phoneEdit;
	protected EditText dianhuaEdit;
	protected EditText remarkView;
	protected EditText idCodeView;
	protected EditText emailView;

	protected List<String> allnum;
	protected static String[] arr = { "身份证", "驾驶证", "军官证", "工作证", "其他证件" };
	protected static String[] Value = { "1", "2", "3", "4", "5" };
	protected static String[] arr1 = { "0点", "1点", "2点", "3点", "4点" };
	protected static String[] Value1 = { "0", "1", "2", "3", "4" };
	protected static String[] arr2 = { "1", "2", "3", "4" };
	private ProgressDialog mProgressDialog;
	private SHotelOrderInfo mOrderInfo;
	private User mUser;
	String[] as;

	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);

		mUser = ((TuanTrip) getApplication()).getUser();
		if (mUser == null) {
			Toast.makeText(this, "请先登录", Toast.LENGTH_SHORT).show();
			Intent intent = new Intent(this, LoginActivity.class);
			intent.putExtra(LoginActivity.EXTRA_OPTION,
					LoginActivity.OPTION_DO_NOTHING);
			startActivity(intent);
			finish();
		} else {
			as = getIntent().getStringArrayExtra(EXTRA_ARR);
			new GetOrderTask().execute(as);
		}
	}

	private void onUpdateTaskComplete(HotelOrderResult orderInfoResult,
			Exception exception) {
		if (orderInfoResult == null) {
			NotificationsUtil.ToastReasonForFailure(this, exception);
		} else if (orderInfoResult.mHttpResult.getStat() == TuanTripSettings.POSTSUCCESS
				|| orderInfoResult.mHttpResult.getStat() == TuanTripSettings.ISBUY) {
			mOrderInfo = orderInfoResult.sHotelOrderInfo;
			setContentView(R.layout.hotel_order_activity);
			ensureUi();
		} else if (orderInfoResult.mHttpResult.getStat() == TuanTripSettings.POSTFAILED
				|| orderInfoResult.mHttpResult.getStat() == 510) {
			Toast.makeText(this, orderInfoResult.mHttpResult.getMessage(),
					Toast.LENGTH_SHORT).show();
			finish();
		} else if (orderInfoResult.mHttpResult.getStat() == TuanTripSettings.LOGINFAILED) {
			Toast.makeText(this, orderInfoResult.mHttpResult.getMessage(),
					Toast.LENGTH_SHORT).show();
			((TuanTrip) getApplication()).loginOut();
			Intent localIntent4 = new Intent(this, LoginActivity.class);
			localIntent4
					.putExtra(LoginActivity.EXTRA_OPTION, TodayActivity.BUY);
			startActivity(localIntent4);
			finish();
		} else {
			NotificationsUtil.ToastReasonForFailure(this, exception);
		}

		dismissProgressDialog();
	}

	private void onSubmitTaskComplete(BaseResult baseResult, Exception exception) {
		isfirstsubmit = true;

		if (baseResult == null) {
			NotificationsUtil.ToastReasonForFailure(this, exception);
		} else if (baseResult.mHttpResult.getStat() == TuanTripSettings.POSTSUCCESS) {
			Toast.makeText(this, baseResult.mHttpResult.getMessage(),
					Toast.LENGTH_SHORT).show();
			finish();
		} else if (baseResult.mHttpResult.getStat() == TuanTripSettings.POSTFAILED) {
			Toast.makeText(this, baseResult.mHttpResult.getMessage(),
					Toast.LENGTH_SHORT).show();
		} else if (baseResult.mHttpResult.getStat() == TuanTripSettings.ISBUY) {
			Toast.makeText(this, baseResult.mHttpResult.getMessage(),
					Toast.LENGTH_SHORT).show();
			finish();
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

	private void SetIdType() {
		Spinner sp1 = (Spinner) findViewById(R.id.idTypeSp);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, arr);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp1.setAdapter(adapter);
		sp1.setSelection(idType - 1);
		sp1.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				idType = Integer.parseInt(Value[arg2]);

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}

		});
	}

	private void SetBuyNumType() {
		Spinner sp1 = (Spinner) findViewById(R.id.buyNumSp);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, arr2);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp1.setAdapter(adapter);
		sp1.setSelection(buyNum - 1);
		sp1.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				buyNum = arg2 + 1;
				totalText.setText((price * buyNum) + "元");

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}

		});
	}

	private void SetZuiZaoType() {
		Spinner sp1 = (Spinner) findViewById(R.id.zuizaoSp);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, arr1);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp1.setAdapter(adapter);
		sp1.setSelection(zuizao);
		sp1.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				zuizao = Integer.parseInt(Value[arg2]);

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}

		});
	}

	private void SetZuiWanType() {
		Spinner sp1 = (Spinner) findViewById(R.id.zuiwanSp);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, arr1);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp1.setAdapter(adapter);
		sp1.setSelection(zuiwan);
		sp1.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				zuiwan = Integer.parseInt(Value[arg2]);

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}

		});
	}

	protected void ensureUi() {
		if (mOrderInfo == null)
			return;

		TextView limitView = (TextView) findViewById(R.id.limitText);
		limitView.setText(mOrderInfo.getDate());

		if (mUser != null && mUser.getIdType() != 0) {
			idType = mUser.getIdType();
		}
		idCodeView = (EditText) findViewById(R.id.idCodeText);
		if (mUser != null && mUser.getIdNumber() != null) {
			idCodeView.setText(mUser.getIdNumber());
		}
		emailView = (EditText) findViewById(R.id.emailText);
		if (mUser != null && mUser.getEmail() != null) {
			emailView.setText(mUser.getEmail());
		}

		TextView priceText = (TextView) findViewById(R.id.priceText);
		price = mOrderInfo.getPrice();
		priceText.setText(price + "元");
		TextView fangxingText = (TextView) findViewById(R.id.fangxingText);
		fangxingText.setText(mOrderInfo.getFangxing());
		TextView kuandaiText = (TextView) findViewById(R.id.kuandaiText);
		kuandaiText.setText(mOrderInfo.getKuandai());
		totalText = (TextView) findViewById(R.id.totalText);
		totalText.setText(price + "元");
		TextView cityText = (TextView) findViewById(R.id.cityText);
		cityText.setText(mOrderInfo.getCity());
		userNameEdit = (EditText) findViewById(R.id.userNameEdit);
		if (mUser != null && mUser.getRealName() != null) {
			userNameEdit.setText(mUser.getRealName());
		}
		lianxirenEdit = (EditText) findViewById(R.id.lianxirenEdit);
		if (mUser != null && mUser.getRealName() != null) {
			lianxirenEdit.setText(mUser.getRealName());
		}
		phoneEdit = (EditText) findViewById(R.id.phoneEdit);
		if (mUser != null && mUser.getPhone() != null) {
			phoneEdit.setText(mUser.getPhone());
		}
		dianhuaEdit = (EditText) findViewById(R.id.dianhuaEdit);
		remarkView = (EditText) findViewById(R.id.remarkEdit);

		Button backButton = (Button) findViewById(R.id.cancelButton);
		backButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();

			}
		});
		Button okButton = (Button) findViewById(R.id.okButton);
		okButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (userNameEdit.getText().toString().trim().equals("")) {
					Toast.makeText(HotelOrderActivity.this, "入住人不能为空",
							Toast.LENGTH_SHORT).show();
					return;
				} else if (phoneEdit.getText().toString().trim().equals("")) {
					Toast.makeText(HotelOrderActivity.this, "手机号不能为空",
							Toast.LENGTH_SHORT).show();
					return;
				} else if (lianxirenEdit.getText().toString().trim().equals("")) {
					Toast.makeText(HotelOrderActivity.this, "联系人不能为空",
							Toast.LENGTH_SHORT).show();
					return;
				} else if (idCodeView.getText().toString().trim().equals("")) {
					Toast.makeText(HotelOrderActivity.this, "证件号码不能为空",
							Toast.LENGTH_SHORT).show();
					return;
				}

				if (isfirstsubmit) {
					isfirstsubmit = false;
					Toast.makeText(HotelOrderActivity.this, "再点一次提交订单,请再确认信息",
							Toast.LENGTH_SHORT).show();
				} else {
					String[] as1 = new String[16];
					as1[0] = mOrderInfo.getCity();
					as1[1] = userNameEdit.getText().toString().trim();
					as1[2] = lianxirenEdit.getText().toString().trim();
					as1[3] = emailView.getText().toString().trim();
					as1[4] = new Integer(zuizao).toString();
					as1[5] = new Integer(zuiwan).toString();
					as1[6] = new Integer(idType).toString();
					as1[7] = idCodeView.getText().toString().trim();
					as1[8] = mOrderInfo.getFangxing();
					as1[9] = as[1];
					as1[10] = as[2];
					as1[11] = remarkView.getText().toString().trim();
					as1[12] = phoneEdit.getText().toString().trim();
					as1[13] = as[0];
					as1[14] = new Integer(buyNum).toString();
					as1[15] = dianhuaEdit.getText().toString().trim();
					new SubmitOrderTask().execute(as1);
				}

			}
		});
		SetIdType();
		SetBuyNumType();
		SetZuiZaoType();
		SetZuiWanType();
	}

	private void dismissProgressDialog() {
		mProgressDialog.dismiss();
	}

	private void startProgressBar(String title, String content) {
		mProgressDialog = ProgressDialog.show(this, title, content);
		mProgressDialog.setCancelable(true);
		mProgressDialog.show();
	}

	class GetOrderTask extends AsyncTask<String, Void, HotelOrderResult> {
		private static final boolean DEBUG = TuanTripSettings.DEBUG;
		private static final String TAG = "GetOrderTask";
		private Exception mReason;

		private GetOrderTask() {
		}

		protected HotelOrderResult doInBackground(String... paramArrayOfVoid) {
			if (DEBUG)
				Log.d("UpdateTask", "doInBackground()");
			TuanTrip tuanTrip = (TuanTrip) HotelOrderActivity.this
					.getApplication();
			HotelOrderResult orderInfoResult = null;
			try {
				TuanTripApp tuanTripApp = tuanTrip.getTuanTripApp();
				String pid = ((Integer) tuanTrip.getProduct().getPid())
						.toString();
				orderInfoResult = tuanTripApp
						.getSHotelOrderInfo(paramArrayOfVoid);

			} catch (Exception localException) {
				if (DEBUG)
					Log.d("UpdateTask", "Caught Exception while get order.",
							localException);
				this.mReason = localException;
				orderInfoResult = null;
			}

			return orderInfoResult;
		}

		protected void onCancelled() {
			super.onCancelled();
			HotelOrderActivity.this.dismissProgressDialog();
		}

		protected void onPostExecute(HotelOrderResult paramOrderInfoResult) {
			if (DEBUG)
				Log.d("UpdateTask", "onPostExecute(): ");
			HotelOrderActivity.this.onUpdateTaskComplete(paramOrderInfoResult,
					this.mReason);
		}

		protected void onPreExecute() {
			if (DEBUG)
				Log.d("UpdateTask", "onPreExecute()");
			startProgressBar("获取订单信息", "获取订单信息中...");
		}
	}

	class SubmitOrderTask extends AsyncTask<String, Void, BaseResult> {
		private static final boolean DEBUG = TuanTripSettings.DEBUG;
		private static final String TAG = "GetOrderTask";
		private Exception mReason;

		private SubmitOrderTask() {
		}

		protected BaseResult doInBackground(String... paramArrayOfVoid) {
			if (DEBUG)
				Log.d("UpdateTask", "doInBackground()");
			TuanTrip tuanTrip = (TuanTrip) HotelOrderActivity.this
					.getApplication();
			BaseResult orderInfoResult = null;
			try {
				TuanTripApp tuanTripApp = tuanTrip.getTuanTripApp();
				String pid = ((Integer) tuanTrip.getProduct().getPid())
						.toString();
				orderInfoResult = tuanTripApp
						.submitSHotelOrder(paramArrayOfVoid);

			} catch (Exception localException) {
				if (DEBUG)
					Log.d("UpdateTask", "Caught Exception while get order.",
							localException);
				this.mReason = localException;
				orderInfoResult = null;
			}

			return orderInfoResult;
		}

		protected void onCancelled() {
			super.onCancelled();
			HotelOrderActivity.this.dismissProgressDialog();
		}

		protected void onPostExecute(BaseResult paramOrderInfoResult) {
			if (DEBUG)
				Log.d("UpdateTask", "onPostExecute(): ");
			HotelOrderActivity.this.onSubmitTaskComplete(paramOrderInfoResult,
					this.mReason);
		}

		protected void onPreExecute() {
			if (DEBUG)
				Log.d("UpdateTask", "onPreExecute()");
			startProgressBar("提交订单", "提交订单中...");
		}
	}
}
