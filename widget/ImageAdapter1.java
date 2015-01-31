package com.tudaidai.tuantrip.widget;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

import com.tudaidai.tuantrip.R;
import com.tudaidai.tuantrip.TuanTrip;
import com.tudaidai.tuantrip.TuanTripSettings;
import com.tudaidai.tuantrip.util.RemoteResourceManager;

public class ImageAdapter1 extends BaseAdapter {

	private Handler mHandler = new Handler();
	private LayoutInflater mInflater;
	private RemoteResourceManagerObserver mResourcesObserver;
	private RemoteResourceManager mRrm;
	static final boolean DEBUG = TuanTripSettings.DEBUG;
	static final String TAG = "HotelListAdapter";
	private Context context;

	public ImageAdapter1(Context context,
			RemoteResourceManager paramRemoteResourceManager) {
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
		String imageUrl = null;
		if (convertView == null) {
			viewholder = new ViewHolder();
			convertView = new ImageView(context);
			viewholder.photo = (ImageView) convertView;
			convertView.setTag(viewholder);
		} else {
			viewholder = (ViewHolder) convertView.getTag();
		}
		imageUrl = (String) getItem(i);
		viewholder.imageName = imageUrl;

		String introImg = imageUrl;
		if (introImg != null && !TextUtils.isEmpty(introImg)) {
			Uri uri = Uri.parse(introImg);
			if (!mRrm.exists(uri)) {
				TuanTrip tuanTrip = (TuanTrip) context.getApplicationContext();
				Bitmap bitmap = BitmapFactory.decodeResource(
						context.getResources(), R.drawable.default_pic);
				viewholder.photo.setScaleType(ImageView.ScaleType.FIT_XY);
				viewholder.photo.setLayoutParams(new Gallery.LayoutParams(
						bitmap.getWidth(), bitmap.getHeight()));
				viewholder.photo.setImageBitmap(bitmap);
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
					viewholder.photo.setScaleType(ImageView.ScaleType.FIT_XY);
					viewholder.photo.setLayoutParams(new Gallery.LayoutParams(
							bitmap.getWidth(), bitmap.getHeight()));
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
		String imageName;
		ImageView photo;
	}

	ArrayList<String> group = null;

	@Override
	public int getCount() {
		return (group == null) ? 0 : group.size();
	}

	@Override
	public Object getItem(int position) {
		return group.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public boolean isEmpty() {
		return (group == null) ? true : group.isEmpty();
	}

	public void setGroup(ArrayList<String> g) {
		group = g;
		notifyDataSetInvalidated();
	}
}
