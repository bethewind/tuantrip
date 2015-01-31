package com.tudaidai.tuantrip;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.tudaidai.tuantrip.app.TuanTripApp;
import com.tudaidai.tuantrip.types.BaseResult;
import com.tudaidai.tuantrip.types.Group;
import com.tudaidai.tuantrip.types.User;
import com.tudaidai.tuantrip.types.Xianlu;
import com.tudaidai.tuantrip.types.XianluOrderInfo;
import com.tudaidai.tuantrip.types.XianluOrderInfoResult;
import com.tudaidai.tuantrip.util.NotificationsUtil;
import com.tudaidai.tuantrip.util.TuanUtils;

public class BuyXianluActivity extends BaseBuyActivity {
	private User mUser;
	static final boolean DEBUG = TuanTripSettings.DEBUG;
	static final String ADDRPARCEL = "com.tudaidai.tuantrip.ADDR_PARCEL";
	static final int RESULT_CODE_ACTIVITY_ADDR = 1;
	static final int RESULT_CODE_ACTIVITY_BUY = 2;
	static final String TAG = "BuyXianluActivity";
	private ProgressDialog mProgressDialog;
	private XianluOrderInfo mOrderInfo = null;
	private Group<Xianlu> xianluGroup;
	private Xianlu userBuyXianlu;
	private int selectType = -1; // 选择的项
	protected String xianluType = ""; // 线路类型
	protected String xianluDate = "未选择日期"; // 此线路对应的日期
	protected double xianluPrice = 0.0; // 此线路对应的价格
	protected static String[] xianluTypeValueArr;
	protected static String[] xianluTypeArr;
	protected static int[] xianluTypeSelectId;
	private Spinner idXianluTypeSp;

	private void setChosenDate(String date) {

	}

	// 设置房型
	private void SetXianluType() {
		idXianluTypeSp = (Spinner) findViewById(R.id.XianluTypeSp);
		ArrayAdapter adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, xianluTypeArr);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		idXianluTypeSp.setAdapter(adapter);
		idXianluTypeSp.setSelection(selectType);
		idXianluTypeSp.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				selectType = arg2;
				if (arg2 != xianluGroup.size()) {
					xianluType = xianluTypeValueArr[arg2];
					xianluDate = xianluGroup.get(arg2).getTravelDate();
					xianluPrice = xianluGroup.get(arg2).getPrice();
				} else {
					xianluType = xianluTypeValueArr[arg2];
					xianluDate = xianluTypeArr[arg2];
					xianluPrice = 0.0;
				}
				priceView.setText(xianluPrice + "元");
				totalPrice = quantity * xianluPrice;
				totalView.setText(totalPrice + "元");

				finalPriceView.setText(totalPrice + "元");
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

		if (mUser != null && mUser.getIdType() != 0) {
			idType = mUser.getIdType();
		}
		if (isBuy) {
			quantity = mOrderInfo.getQuantity();
			idType = mOrderInfo.getIdType();
		}
		super.ensureUi();
		TuanTrip tuanTrip = (TuanTrip) getApplication();
		xianluGroup = mOrderInfo.getXianluGroup();
		if (isBuy)
			userBuyXianlu = mOrderInfo.getUserBuyXianlu();

		xianluTypeSelectId = new int[xianluGroup.size() + 1];
		xianluTypeArr = new String[xianluGroup.size() + 1];
		xianluTypeValueArr = new String[xianluGroup.size() + 1];
		for (int i = 0; i < xianluGroup.size(); i++) {
			Xianlu xianlu = xianluGroup.get(i);
			xianluTypeSelectId[i] = i;
			xianluTypeArr[i] = xianlu.getTravelDate() + " ￥"
					+ xianlu.getPrice() + "元";
			xianluTypeValueArr[i] = xianlu.getTypeId();

			if (isBuy) {
				if (xianlu.getTypeId().equals(userBuyXianlu.getTypeId())) {
					selectType = i;
				}
			}
		}
		xianluTypeSelectId[xianluGroup.size()] = xianluGroup.size();
		xianluTypeArr[xianluGroup.size()] = "未选择日期";
		xianluTypeValueArr[xianluGroup.size()] = "";
		if (isBuy) {
			xianluType = userBuyXianlu.getTypeId();
			xianluDate = userBuyXianlu.getTravelDate();
			xianluPrice = userBuyXianlu.getPrice();
		} else {
			selectType = xianluGroup.size();
		}

		SetXianluType();

		TextView titleView = (TextView) findViewById(R.id.titleText);
		titleView.setText(mOrderInfo.getShortTitle());
		buyNumView = (EditText) findViewById(R.id.buyNumEdit);
		buyNumView.setText(isBuy ? mOrderInfo.getQuantity() + "" : "1");
		buyNumView.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

				try {
					quantity = Integer.parseInt(buyNumView.getText().toString()
							.trim());
					if (quantity == 0) {
						quantity = 1;
						buyNumView.setText("1");
					}

				} catch (Exception e) {
					if (DEBUG)
						Log.d(TAG, "onTextChanged" + e);

					if (!buyNumView.getText().toString().trim().equals("")) {
						buyNumView.setText("1");
						quantity = 1;
					} else {
						quantity = 0;
					}
				} finally {
					if (boughtLimit != 0 && quantity > boughtLimit) {
						quantity = boughtLimit;
						buyNumView.setText(quantity + "");
					}
					totalPrice = quantity * price;
					totalView.setText(totalPrice + "元");
					finalPriceView.setText(totalPrice + "元");
				}

			}
		});
		totalView = (TextView) findViewById(R.id.totalText);
		totalPrice = (isBuy ? mOrderInfo.getQuantity() * xianluPrice
				: xianluPrice);
		totalView.setText(totalPrice + "元");
		priceView = (TextView) findViewById(R.id.priceText);
		priceView.setText(xianluPrice + "元");
		finalPriceView = (TextView) findViewById(R.id.finalPriceText);
		finalPriceView.setText(totalPrice + "元");
		TextView coinremain = (TextView) findViewById(R.id.coinRemainText);
		balance = mOrderInfo.getBalance();
		coinremain.setText(balance + "元");
		LinearLayout deliverLayout = (LinearLayout) findViewById(R.id.deliverLayout);
		LinearLayout addrLayout = (LinearLayout) findViewById(R.id.addrLayout);

		idCodeView = (EditText) findViewById(R.id.idCodeText);
		if (mUser != null && mUser.getIdNumber() != null) {
			idCodeView.setText(mUser.getIdNumber());
		}
		if (isBuy) {
			idCodeView.setText(mOrderInfo.getIdNumber());
		}
		LinearLayout dateLayout = (LinearLayout) findViewById(R.id.dateLayout);

		remarkView = (EditText) findViewById(R.id.remarkEdit);
		if (isBuy)
			remarkView.setText(mOrderInfo.getRemaik());
		userName = (EditText) findViewById(R.id.userNameEdit);
		if (mUser != null && mUser.getRealName() != null) {
			userName.setText(mUser.getRealName());
		}
		if (isBuy)
			userName.setText(mOrderInfo.getUname());
		phoneEdit = (EditText) findViewById(R.id.phoneEdit);
		if (mUser != null && mUser.getPhone() != null) {
			phoneEdit.setText(mUser.getPhone());
		}
		if (isBuy)
			phoneEdit.setText(mOrderInfo.getPhone());

		Button submitButton = (Button) findViewById(R.id.okButton);
		submitButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (phoneEdit.getText().toString().trim().equals("")) {
					Toast.makeText(BuyXianluActivity.this, "手机号不能为空",
							Toast.LENGTH_SHORT).show();
					return;
				} else if (!TuanUtils.checkPhone(phoneEdit.getText().toString()
						.trim())) {
					Toast.makeText(BuyXianluActivity.this, "手机号格式不正确",
							Toast.LENGTH_SHORT).show();
					return;
				} else if (userName.getText().toString().trim().equals("")) {
					Toast.makeText(BuyXianluActivity.this, "购买人姓名不能为空",
							Toast.LENGTH_SHORT).show();
					return;
				} else if (idCodeView.getText().toString().trim().equals("")) {
					Toast.makeText(BuyXianluActivity.this, "证件号码不能为空",
							Toast.LENGTH_SHORT).show();
					return;
				} else if (buyNumView.getText().toString().trim().equals("")) {
					Toast.makeText(BuyXianluActivity.this, "请填写正确的购买数量",
							Toast.LENGTH_SHORT).show();
					return;
				} else if (selectType == xianluGroup.size()) {
					Toast.makeText(BuyXianluActivity.this, "请选择日期",
							Toast.LENGTH_SHORT).show();
					return;
				}

				if (balance >= (totalPrice)) {

					Dialog dialog = new AlertDialog.Builder(
							BuyXianluActivity.this)
							.setTitle("提交订单")
							.setMessage("确认用余额进行支付吗？")
							.setPositiveButton("确定",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											submitOrder();
										}

									})
							.setNegativeButton("取消",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int whichButton) {

										}
									}).create();
					dialog.show();

				} else {
					submitOrder();
				}

			}
		});

	}

	private void submitOrder() {
		String[] as = new String[8];
		as[0] = ((Integer) pid).toString();
		as[1] = xianluType;
		as[2] = phoneEdit.getText().toString().trim();
		as[3] = remarkView.getText().toString().trim();
		as[4] = userName.getText().toString().trim();
		as[5] = ((Integer) idType).toString();
		as[6] = idCodeView.getText().toString().trim();
		as[7] = ((Integer) quantity).toString();
		new SubmitOrderTask().execute(as);
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		// switch (id) {
		// case DATE_DIALOG_ID:
		// return new DatePickerDialog(this,this,mYear, mMonth, mDay);
		// case DATE_SELECT_DIALOG_ID:
		// DateSelectDialog datedialog = new
		// DateSelectDialog(this,mOrderInfo.getTravelDate().split("\\|"));
		// datedialog.setOnCancelListener(new OnCancelListener(){
		// @Override
		// public void onCancel(DialogInterface dialog)
		// {
		// DateSelectDialog dateSelectDialog = (DateSelectDialog)dialog;
		// String date = dateSelectDialog.getChosenDate();
		// BuyHotelActivity.this.setChosenDate(date);
		// removeDialog(DATE_SELECT_DIALOG_ID);
		//
		// }});
		// return datedialog;
		// }
		return null;
	}

	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		mUser = ((TuanTrip) getApplication()).getUser();
		if (mUser == null) {
			Toast.makeText(this, "请先登录", Toast.LENGTH_SHORT).show();
			Intent intent = new Intent(this, LoginActivity.class);
			intent.putExtra(LoginActivity.EXTRA_OPTION, TodayActivity.BUYXIANLU);
			startActivity(intent);
			finish();
		} else {
			Void[] as = new Void[0];
			new UpdateTask().execute(as);
		}
	}

	private void onSubmitTaskComplete(BaseResult baseResult, Exception exception) {

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

	private void onUpdateTaskComplete(XianluOrderInfoResult orderInfoResult,
			Exception exception) {
		if (orderInfoResult == null) {
			NotificationsUtil.ToastReasonForFailure(this, exception);
		} else if (orderInfoResult.mHttpResult.getStat() == TuanTripSettings.POSTSUCCESS
				|| orderInfoResult.mHttpResult.getStat() == TuanTripSettings.ISBUY) {
			mOrderInfo = orderInfoResult.mOrderInfo;
			if (orderInfoResult.mHttpResult.getStat() == TuanTripSettings.ISBUY) {
				isBuy = true;
			}
			setContentView(R.layout.buy_xianlu_activity);
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
			localIntent4.putExtra(LoginActivity.EXTRA_OPTION,
					TodayActivity.BUYXIANLU);
			startActivity(localIntent4);
			finish();
		} else {
			NotificationsUtil.ToastReasonForFailure(this, exception);
		}

		dismissProgressDialog();
	}

	public void onActivityResult(int requestcode, int resultCode, Intent data) {

	}

	private void dismissProgressDialog() {
		mProgressDialog.dismiss();
	}

	private void startProgressBar(String title, String content) {
		mProgressDialog = ProgressDialog.show(this, title, content);
		mProgressDialog.show();
	}

	class UpdateTask extends AsyncTask<Void, Void, XianluOrderInfoResult> {
		private static final boolean DEBUG = TuanTripSettings.DEBUG;
		private static final String TAG = "UpdateTask";
		private Exception mReason;

		private UpdateTask() {
		}

		protected XianluOrderInfoResult doInBackground(Void... paramArrayOfVoid) {
			if (DEBUG)
				Log.d("UpdateTask", "doInBackground()");
			TuanTrip tuanTrip = (TuanTrip) BuyXianluActivity.this
					.getApplication();
			XianluOrderInfoResult orderInfoResult = null;
			try {
				TuanTripApp tuanTripApp = tuanTrip.getTuanTripApp();
				String pid = ((Integer) tuanTrip.getProduct().getPid())
						.toString();
				orderInfoResult = tuanTripApp.getXianluOrderInfo(pid);

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
			BuyXianluActivity.this.dismissProgressDialog();
		}

		protected void onPostExecute(XianluOrderInfoResult paramOrderInfoResult) {
			if (DEBUG)
				Log.d("UpdateTask", "onPostExecute(): ");
			BuyXianluActivity.this.onUpdateTaskComplete(paramOrderInfoResult,
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
		private static final String TAG = "SubmitOrderTask";
		private Exception mReason = null;

		private SubmitOrderTask() {
		}

		protected BaseResult doInBackground(String... paramArray) {
			if (DEBUG)
				Log.d(TAG, "doInBackground()");
			BaseResult baseResult = null;
			try {
				TuanTrip tuanTrip = (TuanTrip) BuyXianluActivity.this
						.getApplication();
				TuanTripApp tuanTripApp = tuanTrip.getTuanTripApp();
				baseResult = tuanTripApp.submitXianluOrder(paramArray);

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
			onSubmitTaskComplete(baseResult, this.mReason);
		}

		protected void onPreExecute() {
			if (DEBUG)
				Log.d(TAG, "onPreExecute()");
			startProgressBar("提交订单", "提交订单中...");
		}
	}

	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {
	}
}
