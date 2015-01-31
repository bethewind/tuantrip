package com.tudaidai.tuantrip;

import java.util.Observable;
import java.util.Observer;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tudaidai.tuantrip.app.LoadableListActivity;
import com.tudaidai.tuantrip.app.TuanTripApp;
import com.tudaidai.tuantrip.types.HotelListResult;
import com.tudaidai.tuantrip.types.HotelShort;
import com.tudaidai.tuantrip.types.ProductShort;
import com.tudaidai.tuantrip.util.NotificationsUtil;
import com.tudaidai.tuantrip.util.RemoteResourceManager;
import com.tudaidai.tuantrip.util.TuanUtils;
import com.tudaidai.tuantrip.widget.HotelListAdapter;

public class HotelListActivity extends LoadableListActivity {

	private HotelListAdapter mListAdapter;
	private HotelListResult orderResult;
	private ListView mListView;
	static final boolean DEBUG = TuanTripSettings.DEBUG;
	static final String TAG = "HotelListActivity";
	private volatile boolean isFinish = true;
	public static final String PAGE_NUM = "10";

	private int lastItem = 1;
	LinearLayout loadingLayout;
	TextView firstLine;
	TextView secondLine;
	ImageView photo;
	String[] as;
	Bitmap tuanbBitmap;
	Handler handler = new Handler();
	RemoteResourceManager localRemoteResourceManager;
	RemoteResourceManager memRemoteResourceManager;
	private LayoutParams mLayoutParams = new LinearLayout.LayoutParams(
			LinearLayout.LayoutParams.WRAP_CONTENT,
			LinearLayout.LayoutParams.WRAP_CONTENT);
	private LayoutParams FFlayoutParams = new LinearLayout.LayoutParams(
			LinearLayout.LayoutParams.FILL_PARENT,
			LinearLayout.LayoutParams.FILL_PARENT);
	private LinearLayout vLayout;
	private RemoteResourceManagerObserver mResourcesObserver;

	public void onPause() {
		mListAdapter.removeObserver();
		memRemoteResourceManager.deleteObserver(mResourcesObserver);
		super.onPause();
	}

	@Override
	public void onResume() {
		mListAdapter.addObserver();
		memRemoteResourceManager.addObserver(mResourcesObserver);
		super.onResume();
	}

	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		// setContentView(R.layout.hotel_list_activity);
		as = getIntent().getStringArrayExtra("as");
		localRemoteResourceManager = ((TuanTrip) getApplication())
				.getFileRemoteResourceManager();
		memRemoteResourceManager = ((TuanTrip) getApplication())
				.getRemoteResourceManager();
		mResourcesObserver = new RemoteResourceManagerObserver();
		memRemoteResourceManager.addObserver(mResourcesObserver);

		// mListView = (ListView)findViewById(R.id.hotellistView);
		mListView = getListView();

		mListAdapter = new HotelListAdapter(HotelListActivity.this,
				localRemoteResourceManager);
		mListAdapter.setLocation(null);
		addHeadView();
		addLoadView();
		mListView.setAdapter(mListAdapter);
		mListView.setBackgroundResource(R.color.scroll_bk);
		mListView.setCacheColorHint(Color.TRANSPARENT);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int i,
					long id) {
				if (mListView.getHeaderViewsCount() != 0) {
					i = i - 1;
				}

				int hid = ((HotelShort) mListAdapter.getItem(i)).getHid();
				Intent intent = new Intent(HotelListActivity.this,
						HotelDetailActivity.class);
				intent.putExtra(HotelDetailActivity.HOTEL_ID, hid);
				intent.putExtra(HotelDetailActivity.COME_DATE, as[1]);
				intent.putExtra(HotelDetailActivity.OUT_DATE, as[2]);
				startActivity(intent);
			}
		});
		new HotelListTask().execute(as);

	}

	private void ensureUi(HotelListResult paramOrders) {

		if (orderResult == null) {
			if (paramOrders.mGroup.size() != 0) {
				orderResult = paramOrders;
				if (orderResult != null
						&& orderResult.mGroup.size() == orderResult.getTotla()) {
					mListView.removeFooterView(loadingLayout);
				}

				if (orderResult.productShort.getPid() != 0) {
					firstLine.setText(orderResult.productShort.getShortTitle());
					secondLine.setText("现价￥"
							+ orderResult.productShort.getPrice() + ","
							+ orderResult.productShort.getBought() + "人购买");
					// photo.loadUrl(orderResult.productShort.getImageUrl());
					updateTuanPic();
				} else {
					mListView.removeHeaderView(vLayout);
				}

				mListAdapter.setGroup(orderResult.mGroup);

			} else {
				Toast.makeText(this, "no hotel", Toast.LENGTH_LONG).show();
			}
		} else {
			for (HotelShort hotelShort : paramOrders.mGroup) {
				orderResult.mGroup.add(hotelShort);
			}

			// mListView.removeFooterView(loadingLayout);

			mListAdapter.notifyDataSetChanged();

		}

	}

	private void addHeadView() {
		LayoutInflater mInflater = LayoutInflater.from(this);
		vLayout = (LinearLayout) mInflater.inflate(
				R.layout.search_hotel_tuanlist_item, null);
		firstLine = (TextView) vLayout.findViewById(R.id.firstLine);
		secondLine = (TextView) vLayout.findViewById(R.id.secondLine);
		photo = (ImageView) vLayout.findViewById(R.id.photo);
		firstLine.setText("1111");
		secondLine.setText("现价￥" + 20 + "," + 2 + "人购买");
		// photo.setImageDrawable(getResources().getDrawable(R.drawable.img_defalt));
		vLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ProductShort productShort = orderResult.productShort;
				Intent intent = new Intent(HotelListActivity.this,
						TodayActivity.class);
				intent.putExtra("pid", productShort.getPid());
				startActivity(intent);

			}
		});
		mListView.addHeaderView(vLayout);
	}

	private void addLoadView() {
		LinearLayout layout = new LinearLayout(this);
		layout.setFocusable(false);
		layout.setFocusableInTouchMode(false);
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
		textView.setFocusable(false);
		textView.setFocusableInTouchMode(false);
		layout.addView(textView, FFlayoutParams);
		layout.setGravity(Gravity.CENTER);

		// 设置ListView的页脚layout
		loadingLayout = new LinearLayout(this);
		loadingLayout.addView(layout, mLayoutParams);
		loadingLayout.setGravity(Gravity.CENTER);
		loadingLayout.setFocusable(false);
		loadingLayout.setFocusableInTouchMode(false);
		loadingLayout
				.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);

		mListView.addFooterView(loadingLayout);

		mListView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				if (!isFinish)
					return;
				if (orderResult == null)
					return;

				lastItem = firstVisibleItem + visibleItemCount - 1;
				if (orderResult != null
						&& orderResult.mGroup.size() == orderResult.getTotla()
						&& mListView.getFooterViewsCount() != 0) {
					mListView.removeFooterView(loadingLayout);
				}
				if (DEBUG)
					Log.d(TAG, "Scroll>>>first: " + firstVisibleItem
							+ ", visible: " + visibleItemCount + ", total: "
							+ totalItemCount);

				if (DEBUG)
					Log.d(TAG, "Scroll>>>lastItem:" + lastItem);
				if (orderResult.mGroup.size() != orderResult.getTotla()) {

					if (firstVisibleItem + visibleItemCount == totalItemCount) {

						new HotelListTask().execute(as);
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

	class HotelListTask extends AsyncTask<String, Void, HotelListResult> {
		private static final String TAG = "HotelListTask";
		private Exception mReason;

		public HotelListTask() {
		}

		protected HotelListResult doInBackground(String... paramArrayOfString) {
			if (DEBUG)
				Log.d(TAG, "doInBackground()");
			HotelListResult orderResult = null;
			try {
				TuanTripApp localTuanTripApp = ((TuanTrip) HotelListActivity.this
						.getApplication()).getTuanTripApp();
				if (mListView.getHeaderViewsCount() == 0) {
					if (DEBUG)
						if (DEBUG)
							Log.d(TAG, "没有团购信息");
					lastItem++;
				}
				orderResult = localTuanTripApp.getSearchHotel(
						paramArrayOfString, new Integer(lastItem).toString(),
						PAGE_NUM);

				if (DEBUG)
					Log.d(TAG, "doInBackground() over");

			} catch (Exception localException) {
				this.mReason = localException;
				orderResult = null;
			}

			return orderResult;
		}

		protected void onPostExecute(HotelListResult paramOrders) {
			if (DEBUG)
				Log.d(TAG, "onPostExecute()");
			if (paramOrders == null) {
				NotificationsUtil.ToastReasonForFailure(HotelListActivity.this,
						mReason);
			} else if (paramOrders.mHttpResult.getStat() == TuanTripSettings.POSTSUCCESS) {
				ensureUi(paramOrders);
			} else if (paramOrders.mHttpResult.getStat() == TuanTripSettings.POSTFAILED) {
				Toast.makeText(HotelListActivity.this,
						paramOrders.mHttpResult.getMessage(),
						Toast.LENGTH_SHORT).show();
			} else if (paramOrders.mHttpResult.getStat() == TuanTripSettings.LOGINFAILED) {
				// Toast.makeText(NearbyHotelsActivity.this,paramOrders.mHttpResult.getMessage(),Toast.LENGTH_SHORT).show();
				// ((TuanTrip)getApplication()).loginOut();
				// Intent localIntent4 = new Intent(NearbyHotelsActivity.this,
				// LoginActivity.class);
				// localIntent4.putExtra(LoginActivity.EXTRA_OPTION,
				// LoginActivity.OPTION_ADDADDR);
				// startActivity(localIntent4);
			} else {
				NotificationsUtil.ToastReasonForFailure(HotelListActivity.this,
						mReason);
			}
			isFinish = true;
			// dismissProgressDialog();
		}

		protected void onCancelled() {
			if (DEBUG)
				Log.d(TAG, "onCancelled()");
			super.onCancelled();
		}

		protected void onPreExecute() {
			if (DEBUG)
				Log.d(TAG, "onPreExecute()");
			// startProgressBar("查询酒店","查询中...");
			isFinish = false;
		}
	}

	//
	class RemoteResourceManagerObserver implements Observer {
		private RemoteResourceManagerObserver() {
		}

		public void update(Observable paramObservable, Object paramObject) {
			if (paramObject == null)
				return;
			handler.post(new Runnable() {

				@Override
				public void run() {
					updateTuanPic();

				}
			});
		}
	}

	//
	private void updateTuanPic() {
		if (orderResult == null || photo == null)
			return;

		String introImg = orderResult.productShort.getImageUrl();
		if (introImg != null && !TextUtils.isEmpty(introImg)
				&& TuanUtils.isPic(introImg)) {
			Uri uri = Uri.parse(introImg);
			if (!memRemoteResourceManager.exists(uri)) {
				TuanTrip tuanTrip = (TuanTrip) getApplicationContext();
				// photo.setImageDrawable(getResources().getDrawable(R.drawable.img_defalt));
				// if(tuanTrip.getShowPic())
				{
					if (DEBUG)
						Log.d(TAG, "!mRrm.exists(uri)" + uri);
					memRemoteResourceManager.request(uri);
				}
			} else {
				if (DEBUG)
					Log.d(TAG, "mRrm exists " + uri);
				try {

					Bitmap bitmap = memRemoteResourceManager.getBitmap(uri);
					photo.setImageBitmap(bitmap);
				} catch (Exception exception) {
				}
			}
		}
	}
}
