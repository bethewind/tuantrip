package com.tudaidai.tuantrip;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.tudaidai.tuantrip.app.TuanTripApp;
import com.tudaidai.tuantrip.types.Group;
import com.tudaidai.tuantrip.types.ProductListResult;
import com.tudaidai.tuantrip.types.ProductShort;
import com.tudaidai.tuantrip.util.NotificationsUtil;
import com.tudaidai.tuantrip.util.RemoteResourceManager;
import com.tudaidai.tuantrip.widget.ProductListAdapter;

public class ProductListActivity extends Activity {
	private Group<ProductShort> mProductList;
	private ProgressDialog mProgressDialog;
	private ProductListAdapter mListAdapter;
	static final boolean DEBUG = TuanTripSettings.DEBUG;
	static final String TAG = "ProductListActivity";

	public void onDestroy() {
		if (DEBUG)
			Log.d(TAG, "onDestroy()");
		mListAdapter.removeObserver();
		super.onDestroy();
	}

	private void ensureUi() {
		if (mProductList == null || mProductList.size() == 0)
			return;

		RemoteResourceManager localRemoteResourceManager = ((TuanTrip) getApplication())
				.getRemoteResourceManager();
		this.mListAdapter = new ProductListAdapter(this,
				localRemoteResourceManager);
		ListView listView = (ListView) findViewById(R.id.listView);
		listView.setAdapter(mListAdapter);
		listView.setCacheColorHint(0);
		listView.setBackgroundResource(R.color.white);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				ProductShort productShort = (ProductShort) mListAdapter
						.getItem(position);
				Intent intent = new Intent();
				intent.putExtra("pid", productShort.getPid());
				setResult(TodayActivity.RESULT_PRODUCTLIST_CODE, intent);
				finish();

			}
		});
		mListAdapter.setGroup(mProductList);

		Button reflushButton = (Button) findViewById(R.id.refreshButton);
		reflushButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Void[] as = new Void[0];
				new UpdateTask().execute(as);

			}
		});
		Button returnButton = (Button) findViewById(R.id.returnButton);
		returnButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();

			}
		});
	}

	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.product_list_activity);
		TuanTrip tuanTrip = (TuanTrip) getApplication();
		mProductList = tuanTrip.getProductList();
		if (mProductList == null) {
			Void[] as = new Void[0];
			new UpdateTask().execute(as);
		} else {
			ensureUi();
		}
	}

	private void dismissProgressDialog() {
		mProgressDialog.dismiss();
	}

	private void startProgressBar(String title, String content) {
		mProgressDialog = ProgressDialog.show(this, title, content);
		mProgressDialog.show();
	}

	class UpdateTask extends AsyncTask<Void, Void, ProductListResult> {
		private static final boolean DEBUG = TuanTripSettings.DEBUG;
		private static final String TAG = "UpdateTask";
		private Exception mReason;

		private UpdateTask() {
		}

		protected ProductListResult doInBackground(Void... paramArrayOfVoid) {
			if (DEBUG)
				Log.d("UpdateTask", "doInBackground()");
			TuanTrip tuanTrip = (TuanTrip) ProductListActivity.this
					.getApplication();
			ProductListResult productListResult = null;
			try {
				TuanTripApp tuanTripApp = tuanTrip.getTuanTripApp();
				String city = tuanTrip.getStoreCity();
				productListResult = tuanTripApp.getProductList(city);

			} catch (Exception localException) {
				if (DEBUG)
					Log.d("UpdateTask", "Caught Exception while get order.",
							localException);
				this.mReason = localException;
				productListResult = null;
			}

			return productListResult;
		}

		protected void onCancelled() {
			super.onCancelled();
			dismissProgressDialog();
		}

		protected void onPostExecute(ProductListResult productListResult) {
			if (DEBUG)
				Log.d("UpdateTask", "onPostExecute(): ");
			TuanTrip tuanTrip = (TuanTrip) ProductListActivity.this
					.getApplication();
			if (productListResult == null) {
				NotificationsUtil.ToastReasonForFailure(
						ProductListActivity.this, mReason);
			} else if (productListResult.mHttpResult.getStat() == TuanTripSettings.POSTSUCCESS) {
				mProductList = productListResult.mProductList;
				tuanTrip.setProductList(mProductList);
				ensureUi();
			} else if (productListResult.mHttpResult.getStat() == TuanTripSettings.POSTFAILED) {
				Toast.makeText(ProductListActivity.this,
						productListResult.mHttpResult.getMessage(),
						Toast.LENGTH_SHORT).show();
				finish();
			} else {
				NotificationsUtil.ToastReasonForFailure(
						ProductListActivity.this, mReason);
			}

			dismissProgressDialog();
		}

		protected void onPreExecute() {
			if (DEBUG)
				Log.d("UpdateTask", "onPreExecute()");
			startProgressBar("获取今日所有团购", "加载中...");
		}
	}
}
