package com.tudaidai.tuantrip;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tudaidai.tuantrip.app.LoadableListActivity;
import com.tudaidai.tuantrip.app.TuanTripApp;
import com.tudaidai.tuantrip.types.HotelOrder;
import com.tudaidai.tuantrip.types.HotelOrderListResult;
import com.tudaidai.tuantrip.util.NotificationsUtil;
import com.tudaidai.tuantrip.widget.HotelOrderListAdapter;

public class HotelOrderListActivity extends LoadableListActivity {
	static final boolean DEBUG = TuanTripSettings.DEBUG;
	static final String TAG = "HotelOrderListActivity";
	public static final int OPTION_ALL = 0;
	public static final int OPTION_SUB = 1;
	public static final int OPTION_CHULI = 2;
	public static final int OPTION_QUEREN = 3;
	public static final int OPTION_PAID = 4;
	public static final int OPTION_QUXIAO = 5;

	public static final int REQUEST_CODE = 1;
	public static final String PAGE_NUM = "15";
	public static final String EXTRA_OPTION = "com.tudaidai.tuantrip.HotelOrderListActivity.OPTION";
	private int option;
	private HotelOrderListAdapter mListAdapter;
	private ListView mListView;
	private HotelOrderListResult orderResult;
	private int lastItem = 0;
	LinearLayout loadingLayout;
	private volatile boolean isFinish = true;
	private LayoutParams mLayoutParams = new LinearLayout.LayoutParams(
			LinearLayout.LayoutParams.WRAP_CONTENT,
			LinearLayout.LayoutParams.WRAP_CONTENT);
	private LayoutParams FFlayoutParams = new LinearLayout.LayoutParams(
			LinearLayout.LayoutParams.FILL_PARENT,
			LinearLayout.LayoutParams.FILL_PARENT);

	private void ensureUi(HotelOrderListResult paramOrders) {

		if (orderResult == null) {
			if (paramOrders.mGroup.size() != 0) {
				orderResult = paramOrders;
				if (orderResult != null
						&& orderResult.mGroup.size() == orderResult.getTotla()) {
					mListView.removeFooterView(loadingLayout);
				}

				mListAdapter.setGroup(orderResult.mGroup);
			} else {
				setEmptyView();
			}
		} else {
			for (HotelOrder order : paramOrders.mGroup) {
				orderResult.mGroup.add(order);
			}

			mListView.removeFooterView(loadingLayout);

			mListAdapter.notifyDataSetChanged();

		}

	}

	protected void onCreate(Bundle bundle) {
		if (DEBUG)
			Log.d(TAG, "onCreate");
		super.onCreate(bundle);
		mListView = getListView();
		mListAdapter = new HotelOrderListAdapter(this);
		addLoadView();
		mListView.setAdapter(mListAdapter);
		mListView.setBackgroundResource(R.color.scroll_bk);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int i,
					long id) {
				HotelOrder order = (HotelOrder) mListAdapter.getItem(i);
				Intent intent = new Intent(HotelOrderListActivity.this,
						HotelOrderDetailActivity.class);
				intent.putExtra(HotelOrderDetailActivity.EXTRA_ORDER, order);
				startActivityForResult(intent, REQUEST_CODE);
			}
		});
		option = getIntent().getExtras().getInt(EXTRA_OPTION);
		String[] as = new String[1];
		as[0] = new Integer(option).toString();
		new OrderTask().execute(as);
	}

	private void addLoadView() {
		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.HORIZONTAL);
		ProgressBar progressBar = new ProgressBar(this);
		// progressBar.setIndeterminateDrawable(getResources().getDrawable(R.drawable.progress));
		progressBar.setPadding(0, 0, 15, 0);
		layout.addView(progressBar, mLayoutParams);
		TextView textView = new TextView(this);
		textView.setText("加载中...");
		textView.setGravity(Gravity.CENTER_VERTICAL);
		layout.addView(textView, FFlayoutParams);
		layout.setGravity(Gravity.CENTER);

		// 设置ListView的页脚layout
		loadingLayout = new LinearLayout(this);
		loadingLayout.addView(layout, mLayoutParams);
		loadingLayout.setGravity(Gravity.CENTER);

		mListView.addFooterView(loadingLayout);

		mListView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				if (!isFinish)
					return;
				if (orderResult == null)
					return;

				if (orderResult != null
						&& orderResult.mGroup.size() != orderResult.getTotla()
						&& mListView.getFooterViewsCount() == 0) {
					mListView.addFooterView(loadingLayout);

				}
				if (DEBUG)
					Log.d(TAG, "Scroll>>>first: " + firstVisibleItem
							+ ", visible: " + visibleItemCount + ", total: "
							+ totalItemCount);
				lastItem = firstVisibleItem + visibleItemCount - 1;
				if (DEBUG)
					Log.d(TAG, "Scroll>>>lastItem:" + lastItem);
				if (orderResult.mGroup.size() != orderResult.getTotla()) {

					if (firstVisibleItem + visibleItemCount == totalItemCount) {
						String[] as = new String[1];
						as[0] = new Integer(option).toString();
						new OrderTask().execute(as);
					}
				}

			}

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

				// if (lastItem == orderResult.mGroup.size() && scrollState ==
				// OnScrollListener.SCROLL_STATE_IDLE)
				// {
				// if(DEBUG)
				// Log.d(TAG,"ScrollStateChanged>>>state:"+scrollState+"lastItem:"
				// + lastItem);
				// }

			}
		});
	}

	class OrderTask extends AsyncTask<String, Void, HotelOrderListResult> {
		private static final String TAG = "OrderTask";
		private Exception mReason;

		public OrderTask() {
		}

		protected HotelOrderListResult doInBackground(
				String... paramArrayOfString) {
			if (DEBUG)
				Log.d(TAG, "doInBackground()");
			HotelOrderListResult orderResult = null;
			try {
				TuanTripApp localTuanTripApp = ((TuanTrip) HotelOrderListActivity.this
						.getApplication()).getTuanTripApp();
				orderResult = localTuanTripApp.getHotelOrders(
						paramArrayOfString[0],
						new Integer(lastItem + 1).toString(), PAGE_NUM);

				if (DEBUG)
					Log.d(TAG, "doInBackground() over");

			} catch (Exception localException) {
				this.mReason = localException;
				orderResult = null;
			}

			return orderResult;
		}

		protected void onPostExecute(HotelOrderListResult paramOrders) {
			if (DEBUG)
				Log.d(TAG, "onPostExecute()");
			if (paramOrders == null) {
				// NotificationsUtil.ToastReasonForFailure(HotelOrderListActivity.this,mReason);
				setEmptyView();
			} else if (paramOrders.mHttpResult.getStat() == TuanTripSettings.POSTSUCCESS) {
				ensureUi(paramOrders);
			} else if (paramOrders.mHttpResult.getStat() == TuanTripSettings.POSTFAILED) {
				// Toast.makeText(HotelOrderListActivity.this,paramOrders.mHttpResult.getMessage(),Toast.LENGTH_SHORT).show();
				setEmptyView();
			} else if (paramOrders.mHttpResult.getStat() == TuanTripSettings.LOGINFAILED) {
				Toast.makeText(HotelOrderListActivity.this,
						paramOrders.mHttpResult.getMessage(),
						Toast.LENGTH_SHORT).show();
				((TuanTrip) getApplication()).loginOut();
				Intent localIntent4 = new Intent(HotelOrderListActivity.this,
						LoginActivity.class);
				localIntent4.putExtra(LoginActivity.EXTRA_OPTION,
						LoginActivity.OPTION_ADDADDR);
				startActivity(localIntent4);
			} else {
				NotificationsUtil.ToastReasonForFailure(
						HotelOrderListActivity.this, mReason);
			}
			isFinish = true;
		}

		protected void onCancelled() {
			if (DEBUG)
				Log.d(TAG, "onCancelled()");
			super.onCancelled();
		}

		protected void onPreExecute() {
			if (DEBUG)
				Log.d(TAG, "onPreExecute()");
			setLoadingView();
			isFinish = false;
		}
	}
}
