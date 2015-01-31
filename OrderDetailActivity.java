package com.tudaidai.tuantrip;

import com.tudaidai.tuantrip.app.TuanTripApp;
import com.tudaidai.tuantrip.types.BaseResult;
import com.tudaidai.tuantrip.types.Order;
import com.tudaidai.tuantrip.util.NotificationsUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class OrderDetailActivity extends Activity {
	static final boolean DEBUG = TuanTripSettings.DEBUG;
	public static final String EXTRA_ORDER = "com.tudaidai.tuantrip.OrderDetailActivity.ORDER";
	static final String TAG = "OrderDetailActivity";
	private Order order;
	private ProgressDialog mProgressDialog;

	private void dismissProgressDialog() {
		mProgressDialog.dismiss();
	}

	private void startProgressBar(String title, String content) {
		mProgressDialog = ProgressDialog.show(this, title, content);
		mProgressDialog.show();
	}

	private void ensureUi() {
		TextView travelDate = (TextView) findViewById(R.id.travelDateText);
		travelDate.setText(order.getTravelDate());
		TextView titleView = (TextView) findViewById(R.id.titleText);
		titleView.setText(order.getShortTitle());
		// TextView codeText = (TextView)findViewById(R.id.codeText);
		// codeText.setText(order.getCode());
		TextView cityText = (TextView) findViewById(R.id.cityText);
		cityText.setText(order.getCityName());
		TextView numText = (TextView) findViewById(R.id.numText);
		numText.setText(order.getQuantity());
		TextView priceText = (TextView) findViewById(R.id.priceText);
		priceText.setText(order.getPrice());
		TextView stateText = (TextView) findViewById(R.id.stateText);
		if (order.getOrderStatus().equals("unpay")) {
			stateText.setText("未付款");
		} else if (order.getOrderStatus().equals("pay")) {
			stateText.setText("已付款");
		} else if (order.getOrderStatus().equals("end")) {
			stateText.setText("过期");
		} else if (order.getOrderStatus().equals("refunded")) {
			stateText.setText("已退款");
		}
		TextView orderTimeText = (TextView) findViewById(R.id.orderTimeText);
		orderTimeText.setText(order.getOrderTime());
		if (order.getOrderStatus().equals("unpay")) {
			LinearLayout layout = (LinearLayout) findViewById(R.id.unpaidLayout);
			layout.setVisibility(View.VISIBLE);
			Button unpay = (Button) findViewById(R.id.payButton);
			unpay.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {

					Dialog dialog = new AlertDialog.Builder(
							OrderDetailActivity.this)
							.setTitle("提交订单")
							.setMessage("确认用余额进行支付吗？")
							.setPositiveButton("确定",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											String[] as = new String[1];
											as[0] = order.getOid();
											new SubmitOrderTask().execute(as);
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

				}
			});
		}

	}

	protected void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		setContentView(R.layout.order_detail_activity);
		order = (Order) getIntent().getExtras().getParcelable(EXTRA_ORDER);
		ensureUi();
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
				TuanTrip tuanTrip = (TuanTrip) OrderDetailActivity.this
						.getApplication();
				TuanTripApp tuanTripApp = tuanTrip.getTuanTripApp();
				baseResult = tuanTripApp.submitOrder(paramArray[0]);

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
}
