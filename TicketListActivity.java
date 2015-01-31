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
import com.tudaidai.tuantrip.types.Ticket;
import com.tudaidai.tuantrip.types.TicketResult;
import com.tudaidai.tuantrip.util.NotificationsUtil;
import com.tudaidai.tuantrip.widget.TicketListAdapter;

public class TicketListActivity extends LoadableListActivity {
	static final boolean DEBUG = TuanTripSettings.DEBUG;
	static final String TAG = "TicketListActivity";
	public static final int OPTION_ALL = 0;
	public static final int OPTION_USED = 2;
	public static final int OPTION_UNUSED = 1;
	public static final int REQUEST_CODE = 1;
	public static final String PAGE_NUM = "15";
	private volatile boolean isFinish = true;
	public static final String EXTRA_OPTION = "com.tudaidai.tuantrip.TicketListActivity.OPTION";
	private int option;
	private TicketListAdapter mListAdapter;
	private ListView mListView;
	private TicketResult ticketResult;
	private int lastItem = 0;
	LinearLayout loadingLayout;
	private LayoutParams mLayoutParams = new LinearLayout.LayoutParams(
			LinearLayout.LayoutParams.WRAP_CONTENT,
			LinearLayout.LayoutParams.WRAP_CONTENT);
	private LayoutParams FFlayoutParams = new LinearLayout.LayoutParams(
			LinearLayout.LayoutParams.FILL_PARENT,
			LinearLayout.LayoutParams.FILL_PARENT);

	private void ensureUi(TicketResult paramTickets) {

		if (ticketResult == null) {
			if (paramTickets.mGroup.size() != 0) {
				ticketResult = paramTickets;
				if (ticketResult != null
						&& ticketResult.mGroup.size() == ticketResult
								.getTotla()) {
					mListView.removeFooterView(loadingLayout);
				}

				mListAdapter.setGroup(ticketResult.mGroup);
			} else {
				setEmptyView();
			}
		} else {
			for (Ticket Ticket : paramTickets.mGroup) {
				ticketResult.mGroup.add(Ticket);
			}

			mListView.removeFooterView(loadingLayout);

			mListAdapter.notifyDataSetChanged();

		}

	}

	@Override
	public int getNoSearchResultsStringId() {
		return R.string.no_tickets;
	}

	protected void onCreate(Bundle bundle) {
		if (DEBUG)
			Log.d(TAG, "onCreate");
		super.onCreate(bundle);
		mListView = getListView();
		mListAdapter = new TicketListAdapter(this);
		addLoadView();
		mListView.setAdapter(mListAdapter);
		mListView.setBackgroundResource(R.color.scroll_bk);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int i,
					long id) {
				Ticket ticket = (Ticket) mListAdapter.getItem(i);
				Intent intent = new Intent(TicketListActivity.this,
						TicketDetailActivity.class);
				intent.putExtra(TicketDetailActivity.EXTRA_Ticket, ticket);
				startActivityForResult(intent, REQUEST_CODE);
			}
		});
		option = getIntent().getExtras().getInt(EXTRA_OPTION);
		String[] as = new String[1];
		as[0] = new Integer(option).toString();
		new TicketTask().execute(as);
	}

	private void addLoadView() {
		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.HORIZONTAL);
		ProgressBar progressBar = new ProgressBar(this);
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
				if (ticketResult == null)
					return;

				if (ticketResult != null
						&& ticketResult.mGroup.size() != ticketResult
								.getTotla()
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
				if (ticketResult.mGroup.size() != ticketResult.getTotla()) {

					if (firstVisibleItem + visibleItemCount == totalItemCount) {
						String[] as = new String[1];
						as[0] = new Integer(option).toString();
						new TicketTask().execute(as);
					}
				}

			}

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

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
		if (DEBUG)
			Log.e(TAG, "onRetainNonConfigurationInstance~~~");
		return new Integer(100);
	}

	class TicketTask extends AsyncTask<String, Void, TicketResult> {
		private static final String TAG = "TicketTask";
		private Exception mReason;

		public TicketTask() {
		}

		protected TicketResult doInBackground(String... paramArrayOfString) {
			if (DEBUG)
				Log.d(TAG, "doInBackground()");
			TicketResult TicketResult = null;
			try {
				TuanTripApp localTuanTripApp = ((TuanTrip) TicketListActivity.this
						.getApplication()).getTuanTripApp();
				TicketResult = localTuanTripApp.getTickets(new Integer(option)
						.toString(), new Integer(lastItem + 1).toString(),
						PAGE_NUM);

				if (DEBUG)
					Log.d(TAG, "doInBackground() over");

			} catch (Exception localException) {
				this.mReason = localException;
				TicketResult = null;
			}

			return TicketResult;
		}

		protected void onPostExecute(TicketResult paramTickets) {
			if (DEBUG)
				Log.d(TAG, "onPostExecute()");
			if (paramTickets == null) {
				NotificationsUtil.ToastReasonForFailure(
						TicketListActivity.this, mReason);
			} else if (paramTickets.mHttpResult.getStat() == TuanTripSettings.POSTSUCCESS) {
				ensureUi(paramTickets);
			} else if (paramTickets.mHttpResult.getStat() == TuanTripSettings.POSTFAILED) {
				Toast.makeText(TicketListActivity.this,
						paramTickets.mHttpResult.getMessage(),
						Toast.LENGTH_SHORT).show();
			} else if (paramTickets.mHttpResult.getStat() == TuanTripSettings.LOGINFAILED) {
				Toast.makeText(TicketListActivity.this,
						paramTickets.mHttpResult.getMessage(),
						Toast.LENGTH_SHORT).show();
				((TuanTrip) getApplication()).loginOut();
				Intent localIntent4 = new Intent(TicketListActivity.this,
						LoginActivity.class);
				localIntent4.putExtra(LoginActivity.EXTRA_OPTION,
						LoginActivity.OPTION_ADDADDR);
				startActivity(localIntent4);
			} else {
				NotificationsUtil.ToastReasonForFailure(
						TicketListActivity.this, mReason);
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
