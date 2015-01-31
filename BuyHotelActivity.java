package com.tudaidai.tuantrip;

import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
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
import com.tudaidai.tuantrip.types.DatePrice;
import com.tudaidai.tuantrip.types.Group;
import com.tudaidai.tuantrip.types.HotelOrderInfo;
import com.tudaidai.tuantrip.types.HotelOrderInfoResult;
import com.tudaidai.tuantrip.types.House;
import com.tudaidai.tuantrip.types.User;
import com.tudaidai.tuantrip.util.NotificationsUtil;
import com.tudaidai.tuantrip.util.TuanUtils;

public class BuyHotelActivity extends BaseBuyActivity {
	private User mUser;
	static final boolean DEBUG = TuanTripSettings.DEBUG;
	static final String ADDRPARCEL = "com.tudaidai.tuantrip.ADDR_PARCEL";
	static final int RESULT_CODE_ACTIVITY_ADDR = 1;
	static final int RESULT_CODE_ACTIVITY_BUY = 2;
	static final int DATE_DIALOG_ID = 1;
	static final String TAG = "BuyActivity";
	private ProgressDialog mProgressDialog;
	private HotelOrderInfo mOrderInfo = null;
	private Group<House> houseGroup;
	private House userBuyHouse = null;
	private int selectType = 0; // 选择的项
	protected String hotelType; // 房间类型
	private Group<DatePrice> datePrice; // 此房间类型对应的日期价格
	protected static String[] hotelTypeValueArr;
	protected static String[] hotelTypeArr;
	protected static int[] hotelTypeSelectId;
	private Spinner idHouseTypeSp;
	private TextView dateView;

	private void setChosenDate(String date) {

	}

	// 设置房型
	private void SetHotelType() {
		idHouseTypeSp = (Spinner) findViewById(R.id.idHouseType);
		ArrayAdapter adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, hotelTypeArr);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		idHouseTypeSp.setAdapter(adapter);
		idHouseTypeSp.setSelection(selectType);
		idHouseTypeSp.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				selectType = arg2;
				hotelType = hotelTypeValueArr[arg2];
				datePrice = houseGroup.get(arg2).getDatePrice();

				if (userBuyHouse == null) {
					userBuyHouse = new House();
					userBuyHouse.setTypeId(hotelType);
				}

				if (userBuyHouse != null && userBuyHouse.getTypeId() != null
						&& !userBuyHouse.getTypeId().equals(hotelType)) {
					userBuyHouse = new House();
					userBuyHouse.setTypeId(hotelType);
					price = 0.0;
					totalPrice = price;
					totalView.setText(totalPrice + "元");
					priceView.setText(price + "元");
					finalPriceView.setText(totalPrice + "元");
					dateView.setText("请选择日期");
				}
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
		houseGroup = mOrderInfo.getHouseGroup();
		if (isBuy)
			userBuyHouse = mOrderInfo.getUserBuyHouse();

		hotelTypeSelectId = new int[houseGroup.size()];
		hotelTypeArr = new String[houseGroup.size()];
		hotelTypeValueArr = new String[houseGroup.size()];
		for (int i = 0; i < houseGroup.size(); i++) {
			House house = houseGroup.get(i);
			hotelTypeSelectId[i] = i;
			hotelTypeArr[i] = house.getTypeName();
			hotelTypeValueArr[i] = house.getTypeId();

			if (isBuy) {
				if (house.getTypeId().endsWith(userBuyHouse.getTypeId())) {
					selectType = i;
				}
			}
		}
		if (isBuy) {
			hotelType = userBuyHouse.getTypeId();
			datePrice = houseGroup.get(selectType).getDatePrice();

		} else {
			selectType = 0;
			hotelType = houseGroup.get(0).getTypeId();
			datePrice = houseGroup.get(0).getDatePrice();
		}

		SetHotelType();

		dateView = (TextView) findViewById(R.id.dateText);
		if (isBuy && userBuyHouse != null)
			dateView.setText(userBuyHouse.getDatePrice().get(0).getDate());
		dateView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showDialog(DATE_DIALOG_ID);

			}
		});

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
		if (isBuy) {
			for (DatePrice datePrice : userBuyHouse.getDatePrice()) {
				try {
					price += Double.parseDouble(datePrice.getPrice());
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		}
		totalPrice = (isBuy ? mOrderInfo.getQuantity() * price : price);
		totalView.setText(totalPrice + "元");
		priceView = (TextView) findViewById(R.id.priceText);
		priceView.setText(price + "元");
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
					Toast.makeText(BuyHotelActivity.this, "手机号不能为空",
							Toast.LENGTH_SHORT).show();
					return;
				} else if (!TuanUtils.checkPhone(phoneEdit.getText().toString()
						.trim())) {
					Toast.makeText(BuyHotelActivity.this, "手机号格式不正确",
							Toast.LENGTH_SHORT).show();
					return;
				} else if (userName.getText().toString().trim().equals("")) {
					Toast.makeText(BuyHotelActivity.this, "购买人姓名不能为空",
							Toast.LENGTH_SHORT).show();
					return;
				} else if (idCodeView.getText().toString().trim().equals("")) {
					Toast.makeText(BuyHotelActivity.this, "证件号码不能为空",
							Toast.LENGTH_SHORT).show();
					return;
				} else if (buyNumView.getText().toString().trim().equals("")) {
					Toast.makeText(BuyHotelActivity.this, "请填写正确的购买数量",
							Toast.LENGTH_SHORT).show();
					return;
				} else if (userBuyHouse == null
						|| userBuyHouse.getDatePrice() == null
						|| userBuyHouse.getDatePrice().size() == 0) {
					Toast.makeText(BuyHotelActivity.this, "请选择日期",
							Toast.LENGTH_SHORT).show();
					return;
				}

				if (balance >= (totalPrice)) {

					Dialog dialog = new AlertDialog.Builder(
							BuyHotelActivity.this)
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
		JSONObject dateJson = new JSONObject();
		for (DatePrice datePrice : userBuyHouse.getDatePrice()) {
			try {
				dateJson.put(datePrice.getDate(), datePrice.getPrice());
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		String[] as = new String[9];
		as[0] = ((Integer) pid).toString();
		as[1] = dateJson.toString();
		as[2] = phoneEdit.getText().toString().trim();
		as[3] = remarkView.getText().toString().trim();
		as[4] = userName.getText().toString().trim();
		as[5] = ((Integer) idType).toString();
		as[6] = idCodeView.getText().toString().trim();
		as[7] = ((Integer) quantity).toString();
		as[8] = hotelType;
		new SubmitOrderTask().execute(as);
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_DIALOG_ID:
			DateCheckBoxDialog datedialog = new DateCheckBoxDialog(this,
					datePrice, userBuyHouse == null ? null
							: userBuyHouse.getDatePrice());
			datedialog.setOnCancelListener(new OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					DateCheckBoxDialog dateSelectDialog = (DateCheckBoxDialog) dialog;
					userBuyHouse = new House();
					userBuyHouse.setTypeId(hotelType);
					userBuyHouse.setDatePrice(dateSelectDialog.getDatePrice());

					price = 0.0;
					if (userBuyHouse.getDatePrice() != null
							&& userBuyHouse.getDatePrice().size() != 0) {
						for (DatePrice datePrice : userBuyHouse.getDatePrice()) {
							try {
								price += Double.parseDouble(datePrice
										.getPrice());
							} catch (Exception e) {
								// TODO: handle exception
							}
						}
						dateView.setText(userBuyHouse.getDatePrice().get(0)
								.getDate());
					} else {
						dateView.setText("请选择日期");
					}
					totalPrice = quantity * price;
					totalView.setText(totalPrice + "元");
					priceView.setText(price + "元");
					finalPriceView.setText(totalPrice + "元");

					removeDialog(DATE_DIALOG_ID);

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
			intent.putExtra(LoginActivity.EXTRA_OPTION, TodayActivity.BUYHOTEL);
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

	private void onUpdateTaskComplete(HotelOrderInfoResult orderInfoResult,
			Exception exception) {
		if (orderInfoResult == null) {
			NotificationsUtil.ToastReasonForFailure(this, exception);
		} else if (orderInfoResult.mHttpResult.getStat() == TuanTripSettings.POSTSUCCESS
				|| orderInfoResult.mHttpResult.getStat() == TuanTripSettings.ISBUY) {
			mOrderInfo = orderInfoResult.mOrderInfo;
			if (orderInfoResult.mHttpResult.getStat() == TuanTripSettings.ISBUY) {
				isBuy = true;
			}
			setContentView(R.layout.buy_hotel_activity);
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
					TodayActivity.BUYHOTEL);
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

	class UpdateTask extends AsyncTask<Void, Void, HotelOrderInfoResult> {
		private static final boolean DEBUG = TuanTripSettings.DEBUG;
		private static final String TAG = "UpdateTask";
		private Exception mReason;

		private UpdateTask() {
		}

		protected HotelOrderInfoResult doInBackground(Void... paramArrayOfVoid) {
			if (DEBUG)
				Log.d("UpdateTask", "doInBackground()");
			TuanTrip tuanTrip = (TuanTrip) BuyHotelActivity.this
					.getApplication();
			HotelOrderInfoResult orderInfoResult = null;
			try {
				TuanTripApp tuanTripApp = tuanTrip.getTuanTripApp();
				String pid = ((Integer) tuanTrip.getProduct().getPid())
						.toString();
				orderInfoResult = tuanTripApp.getHotelOrderInfo(pid);

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
			BuyHotelActivity.this.dismissProgressDialog();
		}

		protected void onPostExecute(HotelOrderInfoResult paramOrderInfoResult) {
			if (DEBUG)
				Log.d("UpdateTask", "onPostExecute(): ");
			BuyHotelActivity.this.onUpdateTaskComplete(paramOrderInfoResult,
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
				TuanTrip tuanTrip = (TuanTrip) BuyHotelActivity.this
						.getApplication();
				TuanTripApp tuanTripApp = tuanTrip.getTuanTripApp();
				baseResult = tuanTripApp.submitHotelOrder(paramArray);

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
