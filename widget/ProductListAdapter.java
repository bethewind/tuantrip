package com.tudaidai.tuantrip.widget;

import java.util.Observable;
import java.util.Observer;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tudaidai.tuantrip.R;
import com.tudaidai.tuantrip.TuanTrip;
import com.tudaidai.tuantrip.TuanTripSettings;
import com.tudaidai.tuantrip.types.ProductShort;
import com.tudaidai.tuantrip.util.RemoteResourceManager;
import com.tudaidai.tuantrip.util.TuanUtils;

public class ProductListAdapter extends BaseGroupAdapter<ProductShort> {

	private Handler mHandler = new Handler();
	private LayoutInflater mInflater;
	private RemoteResourceManagerObserver mResourcesObserver;
	private RemoteResourceManager mRrm;
	static final boolean DEBUG = TuanTripSettings.DEBUG;
	static final String TAG = "HotelListAdapter";
	private Context context;

	public ProductListAdapter(Context context,
			RemoteResourceManager paramRemoteResourceManager) {
		super(context);
		mInflater = LayoutInflater.from(context);
		this.context = context;
		this.mRrm = paramRemoteResourceManager;
		mResourcesObserver = new RemoteResourceManagerObserver();
		mRrm.addObserver(mResourcesObserver);
	}

	public void removeObserver() {
		mRrm.deleteObserver(mResourcesObserver);
	}

	@Override
	public View getView(int i, View convertView, ViewGroup parent) {
		ViewHolder viewholder = null;
		ProductShort productShort = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.product_list_item, null);
			viewholder = new ViewHolder();
			viewholder.firstLine = (TextView) convertView
					.findViewById(R.id.firstLine);
			viewholder.secondLine = (TextView) convertView
					.findViewById(R.id.secondLine);
			viewholder.photo = (ImageView) convertView.findViewById(R.id.photo);

			convertView.setTag(viewholder);
		} else {
			viewholder = (ViewHolder) convertView.getTag();
		}
		productShort = (ProductShort) getItem(i);
		viewholder.firstLine.setText(productShort.getShortTitle());
		viewholder.secondLine.setText("现价￥" + productShort.getPrice() + ","
				+ productShort.getBought() + "人购买");
		String introImg = productShort.getImageUrl();
		if (introImg != null && !TextUtils.isEmpty(introImg)
				&& TuanUtils.isPic(introImg)) {
			Uri uri = Uri.parse(introImg);
			if (!mRrm.exists(uri)) {
				TuanTrip tuanTrip = (TuanTrip) context.getApplicationContext();
				viewholder.photo.setImageDrawable(context.getResources()
						.getDrawable(R.drawable.img_defalt));
				// if(tuanTrip.getShowPic())
				{
					if (DEBUG)
						Log.d(TAG, "!mRrm.exists(uri)" + uri);
					mRrm.request(uri);
				}
			} else {
				if (DEBUG)
					Log.d(TAG, "mRrm exists " + uri);
				try {

					Bitmap bitmap = mRrm.getBitmap(uri);
					viewholder.photo.setImageBitmap(bitmap);
				} catch (Exception exception) {
				}
			}
		}
		return convertView;
	}

	class RemoteResourceManagerObserver implements Observer {
		private RemoteResourceManagerObserver() {
		}

		public void update(Observable paramObservable, Object paramObject) {
			if (paramObject == null)
				return;
			mHandler.post(new Runnable() {

				@Override
				public void run() {
					notifyDataSetChanged();

				}
			});
		}
	}

	class ViewHolder {
		TextView firstLine;
		ImageView photo;
		TextView secondLine;
	}
}
