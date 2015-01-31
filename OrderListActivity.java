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
import com.tudaidai.tuantrip.types.Group;
import com.tudaidai.tuantrip.types.Order;
import com.tudaidai.tuantrip.types.OrderResult;
import com.tudaidai.tuantrip.util.NotificationsUtil;
import com.tudaidai.tuantrip.widget.OrderListAdapter;

public class OrderListActivity extends LoadableListActivity {
	static final boolean DEBUG = TuanTripSettings.DEBUG;
	static final String TAG = "NearbyHotelsActivity";
	public static final int OPTION_ALL = 0;
	public static final int OPTION_PAID = 2;
	public static final int OPTION_UNPAID = 1;
	public static final int REQUEST_CODE = 1;
	public static final String PAGE_NUM = "15";
	public static final String EXTRA_OPTION = "com.tudaidai.tuantrip.OrderListActivity.OPTION";
	private int option;
	private OrderListAdapter mListAdapter;
	private ListView mListView;
	private Group<Order> orders;
	private OrderResult orderResult;
	private int lastItem = 0;
	LinearLayout loadingLayout;
	private volatile boolean isFinish = true;
	private LayoutParams mLayoutParams = new LinearLayout.LayoutParams(
			LinearLayout.LayoutParams.WRAP_CONTENT,
			LinearLayout.LayoutParams.WRAP_CONTENT);
	private LayoutParams FFlayoutParams = new LinearLayout.LayoutParams(
			LinearLayout.LayoutParams.FILL_PARENT,
			LinearLayout.LayoutParams.FILL_PARENT);

	private void ensureUi(OrderResult paramOrders) {

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
			for (Order order : paramOrders.mGroup) {
				orderResult.mGroup.add(order);
			}

			if (orderResult != null
					&& orderResult.mGroup.size() == orderResult.getTotla()
					&& mListView.getFooterViewsCount() != 0) {
				mListView.removeFooterView(loadingLayout);
			}

			mListAdapter.notifyDataSetChanged();

		}

	}

	@Override
	public int getNoSearchResultsStringId() {
		return R.string.no_orders;
	}

	protected void onCreate(Bundle bundle) {
		if (DEBUG)
			Log.d(TAG, "onCreate");
		super.onCreate(bundle);
		mListView = getListView();
		mListAdapter = new OrderListAdapter(this);
		addLoadView();
		mListView.setAdapter(mListAdapter);
		mListView.setBackgroundResource(R.color.scroll_bk);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int i,
					long id) {
				Order order = (Order) mListAdapter.getItem(i);
				Intent intent = new Intent(OrderListActivity.this,
						OrderDetailActivity.class);
				intent.putExtra(OrderDetailActivity.EXTRA_ORDER, order);
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
		progressBar.setFocusable(false);
		progressBar.setFocusableInTouchMode(false);
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

				// if(orderResult!=null&&orderResult.mGroup.size()!=orderResult.getTotla()&&mListView.getFooterViewsCount()==0)
				// {
				// mListView.addFooterView(loadingLayout);
				//
				// }
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

	@Override
	protected void onStart() {
		super.onStart();
		if (DEBUG)
			Log.e(TAG, "start onStart~~~");
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		if (DEBUG)
			Log.e(TAG, "start onRestart~~~");
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (DEBUG)
			Log.e(TAG, "start onResume~~~");
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (DEBUG)
			Log.e(TAG, "start onPause~~~");
	}

	@Override
	protected void onStop() {
		super.onStop();
		if (DEBUG)
			Log.e(TAG, "start onStop~~~");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (DEBUG)
			Log.e(TAG, "start onDestroy~~~");
	}

	public Object onRetainNonConfigurationInstance() {
		Log.e(TAG, "onRetainNonConfigurationInstance~~~");
		return new Integer(100);
	}

	class OrderTask extends AsyncTask<String, Void, OrderResult> {
		private static final String TAG = "OrderTask";
		private Exception mReason;

		public OrderTask() {
		}

		protected OrderResult doInBackground(String... paramArrayOfString) {
			if (DEBUG)
				Log.d(TAG, "doInBackground()");
			OrderResult orderResult = null;
			try {
				TuanTripApp localTuanTripApp = ((TuanTrip) OrderListActivity.this
						.getApplication()).getTuanTripApp();
				orderResult = localTuanTripApp.getOrders(paramArrayOfString[0],
						new Integer(lastItem + 1).toString(), PAGE_NUM);

				if (DEBUG)
					Log.d(TAG, "doInBackground() over");

			} catch (Exception localException) {
				this.mReason = localException;
				orderResult = null;
			}

			return orderResult;
		}

		protected void onPostExecute(OrderResult paramOrders) {
			if (DEBUG)
				Log.d(TAG, "onPostExecute()");
			if (paramOrders == null) {
				NotificationsUtil.ToastReasonForFailure(OrderListActivity.this,
						mReason);
			} else if (paramOrders.mHttpResult.getStat() == TuanTripSettings.POSTSUCCESS) {
				ensureUi(paramOrders);
			} else if (paramOrders.mHttpResult.getStat() == TuanTripSettings.POSTFAILED) {
				Toast.makeText(OrderListActivity.this,
						paramOrders.mHttpResult.getMessage(),
						Toast.LENGTH_SHORT).show();
			} else if (paramOrders.mHttpResult.getStat() == TuanTripSettings.LOGINFAILED) {
				Toast.makeText(OrderListActivity.this,
						paramOrders.mHttpResult.getMessage(),
						Toast.LENGTH_SHORT).show();
				((TuanTrip) getApplication()).loginOut();
				Intent localIntent4 = new Intent(OrderListActivity.this,
						LoginActivity.class);
				localIntent4.putExtra(LoginActivity.EXTRA_OPTION,
						LoginActivity.OPTION_ADDADDR);
				startActivity(localIntent4);
			} else {
				NotificationsUtil.ToastReasonForFailure(OrderListActivity.this,
						mReason);
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
