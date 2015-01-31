package com.tudaidai.tuantrip.widget;

import java.util.Observable;
import java.util.Observer;

import android.content.Context;
import android.graphics.Bitmap;
import android.location.Location;
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
import com.tudaidai.tuantrip.types.HotelShort;
import com.tudaidai.tuantrip.util.LocationUtil;
import com.tudaidai.tuantrip.util.RemoteResourceManager;
import com.tudaidai.tuantrip.util.TuanUtils;

public class HotelListAdapter extends BaseGroupAdapter<HotelShort> {

	private Handler mHandler = new Handler();
	private LayoutInflater mInflater;
	private RemoteResourceManagerObserver mResourcesObserver;
	private RemoteResourceManager mRrm;
	static final boolean DEBUG = TuanTripSettings.DEBUG;
	static final String TAG = "HotelListAdapter";
	private Context context;
	private Location location;

	public HotelListAdapter(Context context,
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

	public void addObserver() {
		mRrm.addObserver(mResourcesObserver);
	}

	@Override
	public View getView(int i, View convertView, ViewGroup parent) {
		ViewHolder viewholder = null;
		HotelShort hotelShort = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.search_hotel_list_item,
					null);
			viewholder = new ViewHolder();
			viewholder.hotelName = (TextView) convertView
					.findViewById(R.id.hotelName);
			viewholder.addr = (TextView) convertView.findViewById(R.id.addr);
			viewholder.photo = (ImageView) convertView.findViewById(R.id.photo);
			viewholder.price = (TextView) convertView.findViewById(R.id.price);

			convertView.setTag(viewholder);
		} else {
			viewholder = (ViewHolder) convertView.getTag();
		}
		hotelShort = (HotelShort) getItem(i);
		viewholder.hotelName.setText(hotelShort.getShortTitle());
		viewholder.addr.setText("地址:" + hotelShort.getAddress());
		String priceDis;
		if (location == null)
			priceDis = hotelShort.getPrice() + "元";
		else
			priceDis = hotelShort.getPrice()
					+ "元  距离"
					+ LocationUtil.distanceByLnglat(hotelShort.getJingdu(),
							hotelShort.getWeidu(), location.getLongitude(),
							location.getLatitude()) + "米";
		viewholder.price.setText(priceDis);
		String introImg = hotelShort.getImageUrl();
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
		TextView hotelName;
		ImageView photo;
		TextView addr;
		TextView price;
	}

	public void setLocation(Location location) {
		this.location = location;
	}
}
