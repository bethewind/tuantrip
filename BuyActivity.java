package com.tudaidai.tuantrip;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.tudaidai.tuantrip.app.TuanTripApp;
import com.tudaidai.tuantrip.types.AddrInfo;
import com.tudaidai.tuantrip.types.AddrListResult;
import com.tudaidai.tuantrip.types.BaseResult;
import com.tudaidai.tuantrip.types.Group;
import com.tudaidai.tuantrip.types.OrderInfo;
import com.tudaidai.tuantrip.types.OrderInfoResult;
import com.tudaidai.tuantrip.types.User;
import com.tudaidai.tuantrip.util.NotificationsUtil;
import com.tudaidai.tuantrip.util.TuanUtils;

public class BuyActivity extends Activity implements OnDateSetListener {
	private User mUser;
	static final boolean DEBUG = TuanTripSettings.DEBUG;
	static final String ADDRPARCEL = "com.tudaidai.tuantrip.ADDR_PARCEL";
	static final int RESULT_CODE_ACTIVITY_ADDR = 1;
	static final int RESULT_CODE_ACTIVITY_BUY = 2;
	static final String TAG = "BuyActivity";
	private ProgressDialog mProgressDialog;
	private OrderInfo mOrderInfo = null;
	private int pid = -1; // 产品id
	private int quantity = 1; // 购买的数量
	private int boughtLimit = 0; // 限制购买多少单
	private int freightNum = 0; // 多少个以上免运费
	private boolean isBuy = false; // 是否已经购买此商品
	private boolean haveAddress = false; // 是否填地址信息
	private boolean isDate = false; // 是否填日期信息
	private double balance = 0.0; // 余额
	private double price = 0.0; // 单价
	private double totalPrice = 0.0; // 总价
	private double freightFee = 0.0; // 运费
	private int aid = 0; // 地址id
	private int idType = 1; // 证件类型
	private TextView totalView;
	private TextView deliverPriceView;
	private TextView finalPriceView;
	private EditText buyNumView;
	private EditText userName;
	private EditText phoneEdit;
	private EditText remarkView;
	private EditText idCodeView;
	private TextView dateView;
	static final int DATE_DIALOG_ID = 2;
	static final int DATE_SELECT_DIALOG_ID = 1;
	private int mYear;
	private int mMonth;
	private int mDay;
	private TextView addrClick;
	private CheckBox daiding;
	Spinner sp1;
	ArrayAdapter<String> adapter;
	List<String> allnum;
	private static String[] arr = { "身份证", "驾驶证", "军官证", "工作证", "其他证件" };
	private static String[] Value = { "1", "2", "3", "4", "5" };
	private ArrayList<AddrInfo> mAddrList = null;
	private Handler handler = new Handler();

	private void setChosenDate(String date) {
		if (!date.equals("")) {
			dateView.setText(date);
		}
	}

	// 计算运输费
	private void countDeliver() {
		if (mOrderInfo.getIsFreight() == 1) {
			freightFee = mOrderInfo.getFreightFee();
		}
		if (freightNum != 0 && quantity >= freightNum)
			freightFee = 0.0;
	}

	// 设置证件类型
	private void SetIdType() {
		sp1 = (Spinner) findViewById(R.id.idTypeSp);
		adapter = new ArrayAdapter<String>(this,
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

	private void ensureUi() {
		if (mOrderInfo == null)
			return;
		TuanTrip tuanTrip = (TuanTrip) getApplication();
		if (mOrderInfo.getHaveAddress() == 1) {
			haveAddress = true;
		}
		if (mOrderInfo.getIsTravelDate() == DATE_SELECT_DIALOG_ID
				|| mOrderInfo.getIsTravelDate() == DATE_DIALOG_ID) {
			isDate = true;
		}
		if (mUser != null && mUser.getIdType() != 0) {
			idType = mUser.getIdType();
		}
		if (isBuy) {
			quantity = mOrderInfo.getQuantity();
			idType = mOrderInfo.getIdType();
		}

		pid = tuanTrip.getProduct().getPid();
		TextView titleView = (TextView) findViewById(R.id.titleText);
		titleView.setText(mOrderInfo.getShortTitle());
		TextView limitView = (TextView) findViewById(R.id.limitText);
		boughtLimit = tuanTrip.getProduct().getBoughtLimit();
		freightNum = mOrderInfo.getFreightNum();
		if (boughtLimit == 0) {
			if (haveAddress && freightNum != 0) {
				limitView.setText(freightNum + "个及以上免运费");
			} else {
				limitView.setVisibility(View.INVISIBLE);
				limitView.getLayoutParams().height = 0;
			}
		} else {
			if (haveAddress && freightNum != 0) {
				limitView.setText("每人限购" + boughtLimit + "个," + freightNum
						+ "个及以上免运费");
			} else {
				limitView.setText("每人限购" + boughtLimit + "个");
			}
		}

		TextView priceView = (TextView) findViewById(R.id.priceText);
		price = tuanTrip.getProduct().getPrice();
		priceView.setText(tuanTrip.getProduct().getPrice() + "元");
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
						quantity = 1;
						buyNumView.setText("1");
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
					if (haveAddress) {
						if (freightNum != 0 && quantity >= freightNum)
							freightFee = 0.0;
						else if (quantity == 0) {
							freightFee = 0.0;
						} else
							freightFee = mOrderInfo.getFreightFee();

						deliverPriceView.setText(freightFee + "元");
					}
					finalPriceView.setText(totalPrice + freightFee + "元");
				}

			}
		});
		totalView = (TextView) findViewById(R.id.totalText);
		totalPrice = (isBuy ? mOrderInfo.getQuantity()
				* tuanTrip.getProduct().getPrice() : tuanTrip.getProduct()
				.getPrice());
		totalView.setText(totalPrice + "元");
		TextView coinremain = (TextView) findViewById(R.id.coinRemainText);
		balance = mOrderInfo.getBalance();
		coinremain.setText(balance + "元");
		LinearLayout deliverLayout = (LinearLayout) findViewById(R.id.deliverLayout);
		LinearLayout addrLayout = (LinearLayout) findViewById(R.id.addrLayout);
		if (haveAddress) {

			deliverLayout.setVisibility(View.VISIBLE);
			addrLayout.setVisibility(View.VISIBLE);
			countDeliver();
			deliverPriceView = (TextView) findViewById(R.id.deliverPriceText);
			deliverPriceView.setText(freightFee + "元");
			aid = mOrderInfo.getAId();
			addrClick = (TextView) findViewById(R.id.addrClick);
			if (mOrderInfo.getAName() != null) {
				addrClick.setText(mOrderInfo.getAName());
			}
			addrClick.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Void[] voids = new Void[0];
					new AddrTask().execute(voids);

				}
			});
		} else {

			deliverLayout.getLayoutParams().height = 0;
			addrLayout.getLayoutParams().height = 0;
		}

		SetIdType();
		idCodeView = (EditText) findViewById(R.id.idCodeText);
		if (mUser != null && mUser.getIdNumber() != null) {
			idCodeView.setText(mUser.getIdNumber());
		}
		if (isBuy) {
			idCodeView.setText(mOrderInfo.getIdNumber());
		}
		LinearLayout dateLayout = (LinearLayout) findViewById(R.id.dateLayout);
		if (isDate) {
			final Calendar calendar = Calendar.getInstance();
			mYear = calendar.get(Calendar.YEAR);
			mMonth = calendar.get(Calendar.MONTH);
			mDay = calendar.get(Calendar.DAY_OF_MONTH);

			dateLayout.setVisibility(View.VISIBLE);
			dateView = (TextView) findViewById(R.id.dateText);
			dateView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					daiding.setChecked(false);
					if (mOrderInfo.getIsTravelDate() == DATE_SELECT_DIALOG_ID) {
						showDialog(DATE_SELECT_DIALOG_ID);
					} else if (mOrderInfo.getIsTravelDate() == DATE_DIALOG_ID) {
						showDialog(DATE_DIALOG_ID);
					}

				}
			});

			daiding = (CheckBox) findViewById(R.id.ddCb);
			daiding.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView,
						boolean isChecked) {
					if (isChecked) {
						dateView.setText("无日期");
					}

				}
			});
			if (isBuy) {
				if (mOrderInfo.getDateBind().equals("1")) {
					daiding.setChecked(true);
				} else {
					String[] dates = mOrderInfo.getDateBind().split("-");
					try {
						mYear = Integer.parseInt(dates[0]);
						mMonth = Integer.parseInt(dates[1]) - 1;
						mDay = Integer.parseInt(dates[2]);
					} catch (Exception e) {
						// TODO: handle exception
					}
					dateView.setText(mOrderInfo.getDateBind());
					daiding.setChecked(false);
				}
			}
		} else {
			dateLayout.getLayoutParams().height = 0;
		}
		finalPriceView = (TextView) findViewById(R.id.finalPriceText);
		finalPriceView.setText(totalPrice + freightFee + "元");
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

		Button backButton = (Button) findViewById(R.id.cancelButton);
		backButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				BuyActivity.this.finish();

			}
		});
		Button submitButton = (Button) findViewById(R.id.okButton);
		submitButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (haveAddress && aid == 0) {
					Toast.makeText(BuyActivity.this, "请添加收货地址",
							Toast.LENGTH_SHORT).show();
					return;
				} else if (phoneEdit.getText().toString().trim().equals("")) {
					Toast.makeText(BuyActivity.this, "手机号不能为空",
							Toast.LENGTH_SHORT).show();
					return;
				} else if (!TuanUtils.checkPhone(phoneEdit.getText().toString()
						.trim())) {
					Toast.makeText(BuyActivity.this, "手机号格式不正确",
							Toast.LENGTH_SHORT).show();
					return;
				} else if (userName.getText().toString().trim().equals("")) {
					Toast.makeText(BuyActivity.this, "购买人姓名不能为空",
							Toast.LENGTH_SHORT).show();
					return;
				} else if (idCodeView.getText().toString().trim().equals("")) {
					Toast.makeText(BuyActivity.this, "证件号码不能为空",
							Toast.LENGTH_SHORT).show();
					return;
				} else if (isDate
						&& dateView.getText().toString().trim().equals("无日期")
						&& !daiding.isChecked()) {
					Toast.makeText(BuyActivity.this, "日期不能为空",
							Toast.LENGTH_SHORT).show();
					return;
				} else if (buyNumView.getText().toString().trim().equals("")) {
					Toast.makeText(BuyActivity.this, "请填写正确的购买数量",
							Toast.LENGTH_SHORT).show();
					return;
				}

				if (balance >= (totalPrice + freightFee)) {

					Dialog dialog = new AlertDialog.Builder(BuyActivity.this)
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
		String[] as = new String[11];
		as[0] = ((Integer) pid).toString();
		as[1] = ((Integer) aid).toString();
		as[2] = phoneEdit.getText().toString().trim();
		as[3] = remarkView.getText().toString().trim();
		as[4] = userName.getText().toString().trim();
		as[5] = ((Integer) idType).toString();
		as[6] = idCodeView.getText().toString().trim();
		if (isDate) {
			as[7] = dateView.getText().toString().trim();
			as[8] = daiding.isChecked() ? "1" : "0";
		} else {
			as[7] = "无日期";
			as[8] = "0";
		}
		as[9] = ((Integer) quantity).toString();
		as[10] = ((Integer) boughtLimit).toString();
		new SubmitOrderTask().execute(as);
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_DIALOG_ID:
			return new DatePickerDialog(this, this, mYear, mMonth, mDay);
		case DATE_SELECT_DIALOG_ID:
			DateSelectDialog datedialog = new DateSelectDialog(this, mOrderInfo
					.getTravelDate().split("\\|"));
			datedialog.setOnCancelListener(new OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					DateSelectDialog dateSelectDialog = (DateSelectDialog) dialog;
					String date = dateSelectDialog.getChosenDate();
					BuyActivity.this.setChosenDate(date);
					removeDialog(DATE_SELECT_DIALOG_ID);

				}
			});
			return datedialog;
		}
		return null;
	}

	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		mUser = ((TuanTrip) getApplication()).getUser();
		if (mUser == null) {
			Toast.makeText(this, "请先登录", Toast.LENGTH_SHORT).show();
			Intent intent = new Intent(this, LoginActivity.class);
			intent.putExtra(LoginActivity.EXTRA_OPTION, TodayActivity.BUY);
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

	private void onUpdateTaskComplete(OrderInfoResult orderInfoResult,
			Exception exception) {
		if (orderInfoResult == null) {
			NotificationsUtil.ToastReasonForFailure(this, exception);
		} else if (orderInfoResult.mHttpResult.getStat() == TuanTripSettings.POSTSUCCESS
				|| orderInfoResult.mHttpResult.getStat() == TuanTripSettings.ISBUY) {
			mOrderInfo = orderInfoResult.mOrderInfo;
			if (orderInfoResult.mHttpResult.getStat() == TuanTripSettings.ISBUY) {
				isBuy = true;
			}
			setContentView(R.layout.buy_activity);
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

	private void onAddrTaskComplete(AddrListResult addrlistresult,
			Exception exception) {
		if (addrlistresult == null) {
			NotificationsUtil.ToastReasonForFailure(this, exception);
		} else if (addrlistresult.mHttpResult.getStat() == TuanTripSettings.POSTSUCCESS) {
			mAddrList = new Group<AddrInfo>();
			Iterator localIterator = addrlistresult.mGroup.iterator();
			while (localIterator.hasNext()) {
				mAddrList.add((AddrInfo) localIterator.next());
			}
			Intent localIntent1 = new Intent(this, AddrActivity.class);
			localIntent1.putExtra(AddrActivity.EXTRA_OPTION,
					AddrActivity.OPTION_ORDER);
			localIntent1.putExtra(AddrActivity.EXTRA_ADDR, mAddrList);
			startActivityForResult(localIntent1, RESULT_CODE_ACTIVITY_ADDR);
		} else if (addrlistresult.mHttpResult.getStat() == TuanTripSettings.POSTFAILED) {
			Toast.makeText(this, addrlistresult.mHttpResult.getMessage(),
					Toast.LENGTH_SHORT).show();
		} else if (addrlistresult.mHttpResult.getStat() == TuanTripSettings.LOGINFAILED) {
			Toast.makeText(this, addrlistresult.mHttpResult.getMessage(),
					Toast.LENGTH_SHORT).show();
			((TuanTrip) getApplication()).loginOut();
			Intent localIntent4 = new Intent(this, LoginActivity.class);
			localIntent4.putExtra(LoginActivity.EXTRA_OPTION,
					LoginActivity.OPTION_ORDER);
			startActivity(localIntent4);
			finish();
		} else {
			NotificationsUtil.ToastReasonForFailure(this, exception);
		}

		dismissProgressDialog();
	}

	public void onActivityResult(int requestcode, int resultCode, Intent data) {
		if (requestcode == RESULT_CODE_ACTIVITY_ADDR && data != null
				&& data.getExtras() != null
				&& data.getExtras().containsKey(ADDRPARCEL)) {
			AddrInfo addrinfo = (AddrInfo) data.getParcelableExtra(ADDRPARCEL);
			aid = addrinfo.getAid();
			addrClick = (TextView) findViewById(R.id.addrClick);
			addrClick.setText(addrinfo.getProvince() + addrinfo.getCity()
					+ addrinfo.getArea() + addrinfo.getHouse());
		}
	}

	private void dismissProgressDialog() {
		mProgressDialog.dismiss();
	}

	private void startProgressBar(String title, String content) {
		mProgressDialog = ProgressDialog.show(this, title, content);
		mProgressDialog.show();
	}

	class UpdateTask extends AsyncTask<Void, Void, OrderInfoResult> {
		private static final boolean DEBUG = TuanTripSettings.DEBUG;
		private static final String TAG = "UpdateTask";
		private Exception mReason;

		private UpdateTask() {
		}

		protected OrderInfoResult doInBackground(Void... paramArrayOfVoid) {
			if (DEBUG)
				Log.d("UpdateTask", "doInBackground()");
			TuanTrip tuanTrip = (TuanTrip) BuyActivity.this.getApplication();
			OrderInfoResult orderInfoResult = null;
			try {
				TuanTripApp tuanTripApp = tuanTrip.getTuanTripApp();
				String pid = ((Integer) tuanTrip.getProduct().getPid())
						.toString();
				orderInfoResult = tuanTripApp.getOrderInfo(pid);

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
			BuyActivity.this.dismissProgressDialog();
		}

		protected void onPostExecute(OrderInfoResult paramOrderInfoResult) {
			if (DEBUG)
				Log.d("UpdateTask", "onPostExecute(): ");
			BuyActivity.this.onUpdateTaskComplete(paramOrderInfoResult,
					this.mReason);
		}

		protected void onPreExecute() {
			if (DEBUG)
				Log.d("UpdateTask", "onPreExecute()");
			startProgressBar("获取订单信息", "获取订单信息中...");
		}
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
				TuanTrip tuanTrip = (TuanTrip) BuyActivity.this
						.getApplication();
				TuanTripApp tuanTripApp = tuanTrip.getTuanTripApp();
				localAddrListResult1 = tuanTripApp.getAddress();

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
			BuyActivity.this.dismissProgressDialog();
		}

		protected void onPostExecute(AddrListResult paramAddrListResult) {
			if (DEBUG)
				Log.d(TAG, "onPostExecute(): ");
			BuyActivity.this.onAddrTaskComplete(paramAddrListResult,
					this.mReason);
		}

		protected void onPreExecute() {
			if (DEBUG)
				Log.d(TAG, "onPreExecute()");
			startProgressBar("获取地址", "获取地址中...");
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
				TuanTrip tuanTrip = (TuanTrip) BuyActivity.this
						.getApplication();
				TuanTripApp tuanTripApp = tuanTrip.getTuanTripApp();
				baseResult = tuanTripApp.submitOrder(paramArray);

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
	public void onDateSet(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {
		try {
			String month11 = new Integer(monthOfYear + 1).toString();
			if ((monthOfYear + 1) < 10)
				month11 = ("0" + month11);
			String day11 = new Integer(dayOfMonth).toString();
			if (dayOfMonth < 10)
				day11 = ("0" + day11);
			final String[] dates = mOrderInfo.getTravelDate().split("\\|");
			long setDate = new SimpleDateFormat("yyyy-MM-dd").parse(
					year + "-" + month11 + "-" + day11).getTime();
			try {
				long start = new SimpleDateFormat("yyyy-MM-dd").parse(dates[0])
						.getTime();
				if (setDate < start) {
					Toast.makeText(this,
							"日期应该在" + dates[0] + "和" + dates[1] + "之间",
							Toast.LENGTH_LONG).show();
					return;
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
			try {
				long end = new SimpleDateFormat("yyyy-MM-dd").parse(dates[1])
						.getTime();
				if (setDate > end) {
					Toast.makeText(this,
							"日期应该在" + dates[0] + "和" + dates[1] + "之间",
							Toast.LENGTH_LONG).show();
					return;
				}
			} catch (Exception e) {
				// TODO: handle exception
			}

		} catch (Exception e) {
			// TODO: handle exception
		}

		mYear = year;
		mMonth = monthOfYear;
		mDay = dayOfMonth;

		String month11 = new Integer(mMonth + 1).toString();
		if ((mMonth + 1) < 10)
			month11 = ("0" + month11);
		String day11 = new Integer(mDay).toString();
		if (mDay < 10)
			day11 = ("0" + day11);
		dateView.setText(mYear + "-" + month11 + "-" + day11);
	}

	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {
		switch (id) {
		case DATE_DIALOG_ID:
			((DatePickerDialog) dialog).updateDate(mYear, mMonth, mDay);
			break;
		}
	}
}
