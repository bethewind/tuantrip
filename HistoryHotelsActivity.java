package com.tudaidai.tuantrip;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.tudaidai.tuantrip.app.LoadableListActivity;
import com.tudaidai.tuantrip.types.Group;
import com.tudaidai.tuantrip.types.HotelListResult;
import com.tudaidai.tuantrip.types.HotelShort;
import com.tudaidai.tuantrip.types.ProductShort;
import com.tudaidai.tuantrip.util.NotificationsUtil;
import com.tudaidai.tuantrip.util.RemoteResourceManager;
import com.tudaidai.tuantrip.widget.HotelListAdapter;

public class HistoryHotelsActivity extends LoadableListActivity {

	private Group<ProductShort> mProductList;
	private HotelListAdapter mListAdapter;
	private HotelListResult orderResult;
	private ListView mListView;
	static final boolean DEBUG = TuanTripSettings.DEBUG;
	static final String TAG = "HistoryHotelsActivity";
	private volatile boolean isFirst = true;
	public static final String PAGE_NUM = "8";
	private static final int SEARCH_OPTION = 1;
	private static final int MENU_ITEM_DELETE = 2;
	private static final int MENU_CLEAR = 3;

	private int lastItem = 1;
	LinearLayout loadingLayout;
	TextView firstLine;
	TextView secondLine;
	ImageView photo;
	Bitmap tuanbBitmap;
	Handler handler = new Handler();
	RemoteResourceManager localRemoteResourceManager;
	public static final String[] PROJECTION = new String[] { HotelShort.HID, // 0
			HotelShort.SHORTTITLE, // 1
			HotelShort.IMAGEURL, HotelShort.PRICE, HotelShort.ADDRESS };

	public void setEmptyView() {
		mEmptyProgress.setVisibility(ViewGroup.GONE);
		mEmptyText.setText("没有历史酒店");
	}

	public void onPause() {
		mListAdapter.removeObserver();

		super.onPause();
	}

	@Override
	public void onResume() {
		mListAdapter.addObserver();

		super.onResume();
	}

	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);

		localRemoteResourceManager = ((TuanTrip) getApplication())
				.getFileRemoteResourceManager();

		// mListView = (ListView)findViewById(R.id.hotellistView);
		mListView = getListView();
		registerForContextMenu(mListView);

		mListAdapter = new HotelListAdapter(HistoryHotelsActivity.this,
				localRemoteResourceManager);

		mListView.setAdapter(mListAdapter);
		mListView.setBackgroundResource(R.color.scroll_bk);
		mListView.setCacheColorHint(Color.TRANSPARENT);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int i,
					long id) {
				int hid = ((HotelShort) mListAdapter.getItem(i)).getHid();
				Intent intent = new Intent(HistoryHotelsActivity.this,
						HotelDetailActivity.class);
				intent.putExtra(HotelDetailActivity.HOTEL_ID, hid);
				intent.putExtra(HotelDetailActivity.COME_DATE, "");
				intent.putExtra(HotelDetailActivity.OUT_DATE, "");
				startActivity(intent);
			}
		});
		Void[] as = new Void[0];
		new HotelListTask().execute(as);

	}

	private void ensureUi(HotelListResult paramOrders) {

		if (paramOrders.mGroup.size() != 0) {
			orderResult = paramOrders;

			mListAdapter.setGroup(orderResult.mGroup);

		} else {
			mListAdapter.setGroup(paramOrders.mGroup);
			setEmptyView();
			// Toast.makeText(this, "no hotel", Toast.LENGTH_LONG).show();
		}

	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View view,
			ContextMenuInfo menuInfo) {
		AdapterView.AdapterContextMenuInfo info;
		try {
			info = (AdapterView.AdapterContextMenuInfo) menuInfo;
		} catch (ClassCastException e) {
			Log.e(TAG, "bad menuInfo", e);
			return;
		}

		Object object = mListAdapter.getItem(info.position);
		if (object == null) {
			// For some reason the requested item isn't available, do nothing
			return;
		}

		// Setup the menu header
		menu.setHeaderTitle(((HotelShort) mListAdapter.getItem(info.position))
				.getShortTitle());

		// Add a menu item to delete the note
		menu.add(0, MENU_ITEM_DELETE, 0, "删除");
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterView.AdapterContextMenuInfo info;
		try {
			info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
		} catch (ClassCastException e) {
			Log.e(TAG, "bad menuInfo", e);
			return false;
		}

		switch (item.getItemId()) {
		case MENU_ITEM_DELETE: {
			int hid = ((HotelShort) mListAdapter.getItem(info.position))
					.getHid();
			// Delete the note that the context menu is for
			Uri noteUri = ContentUris.withAppendedId(HotelShort.CONTENT_URI,
					hid);
			int count = getContentResolver().delete(noteUri, null, null);
			if (count == 1) {
				Toast.makeText(this, "删除成功", Toast.LENGTH_SHORT);
				Void[] as = new Void[0];
				new HotelListTask().execute(as);
			} else {
				Toast.makeText(this, "删除失败,请重试", Toast.LENGTH_SHORT);
			}
			return true;
		}
		}
		return false;
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, MENU_CLEAR, 0, "清空").setIcon(R.drawable.icon_del_rst);
		super.onCreateOptionsMenu(menu);

		return true;
	}

	public boolean onOptionsItemSelected(MenuItem menuitem) {
		boolean flag = super.onOptionsItemSelected(menuitem);
		Intent intent = null;
		switch (menuitem.getItemId()) {
		case MENU_CLEAR:
			int count = getContentResolver().delete(HotelShort.CONTENT_URI,
					null, null);
			if (count != 0) {
				Toast.makeText(this, "删除成功", Toast.LENGTH_SHORT);
				Void[] as = new Void[0];
				new HotelListTask().execute(as);
			} else {
				Toast.makeText(this, "删除失败,请重试", Toast.LENGTH_SHORT);
			}
			break;
		default:
			break;
		}

		return flag;
	}

	class HotelListTask extends AsyncTask<Void, Void, HotelListResult> {
		private static final String TAG = "HotelListTask";
		private Exception mReason;

		public HotelListTask() {
		}

		protected HotelListResult doInBackground(Void... paramArrayOfString) {
			if (DEBUG)
				Log.d(TAG, "doInBackground()");
			HotelListResult orderResult = null;
			try {
				orderResult = getHistoryHotels();

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
				NotificationsUtil.ToastReasonForFailure(
						HistoryHotelsActivity.this, mReason);
			} else if (paramOrders.mHttpResult.getStat() == TuanTripSettings.POSTSUCCESS) {
				ensureUi(paramOrders);
			} else if (paramOrders.mHttpResult.getStat() == TuanTripSettings.POSTFAILED) {
				Toast.makeText(HistoryHotelsActivity.this,
						paramOrders.mHttpResult.getMessage(),
						Toast.LENGTH_SHORT).show();
			} else {
				NotificationsUtil.ToastReasonForFailure(
						HistoryHotelsActivity.this, mReason);
			}

			if (!isFirst)
				dismissProgressDialog();

			isFirst = false;
		}

		protected void onCancelled() {
			if (DEBUG)
				Log.d(TAG, "onCancelled()");
			super.onCancelled();
		}

		protected void onPreExecute() {
			if (DEBUG)
				Log.d(TAG, "onPreExecute()");

			if (!isFirst)
				startProgressBar("加载信息", "加载中...");
		}
	}

	public HotelListResult getHistoryHotels() {
		HotelListResult hListResult = new HotelListResult();
		Cursor cur = managedQuery(HotelShort.CONTENT_URI, PROJECTION, null,
				null, null);
		hListResult.mHttpResult.setStat(TuanTripSettings.POSTSUCCESS);
		if (cur != null && cur.moveToFirst()) {
			String id = null;
			String userName = null;
			do {
				HotelShort productShort = new HotelShort();
				productShort.setHid(Integer.parseInt(cur.getString(cur
						.getColumnIndex(HotelShort.HID))));
				productShort.setShortTitle(cur.getString(cur
						.getColumnIndex(HotelShort.SHORTTITLE)));
				productShort.setPrice(cur.getString(cur
						.getColumnIndex(HotelShort.PRICE)));
				productShort.setImageUrl(cur.getString(cur
						.getColumnIndex(HotelShort.IMAGEURL)));
				productShort.setAddress(cur.getString(cur
						.getColumnIndex(HotelShort.ADDRESS)));

				hListResult.mGroup.add(productShort);
			} while (cur.moveToNext());
		}

		return hListResult;
	}

}
