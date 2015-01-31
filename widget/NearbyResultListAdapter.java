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

import com.tudaidai.tuantrip.NearbyActivity;
import com.tudaidai.tuantrip.R;
import com.tudaidai.tuantrip.TuanTripSettings;
import com.tudaidai.tuantrip.types.Nearby;
import com.tudaidai.tuantrip.util.LocationUtil;
import com.tudaidai.tuantrip.util.RemoteResourceManager;
import com.tudaidai.tuantrip.util.TuanUtils;

public class NearbyResultListAdapter extends BaseGroupAdapter<Nearby> {

	private Handler mHandler = new Handler();
	private LayoutInflater mInflater;
	private RemoteResourceManagerObserver mResourcesObserver;
	private RemoteResourceManager mRrm;
	static final boolean DEBUG = TuanTripSettings.DEBUG;
	static final String TAG = "NearbyResultListAdapter";
	private Context context;
	private Location location;

	public NearbyResultListAdapter(Context context,
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
		Nearby nearby = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.nearby_list_item, null);
			viewholder = new ViewHolder();
			viewholder.name = (TextView) convertView.findViewById(R.id.name);
			viewholder.addr = (TextView) convertView.findViewById(R.id.addr);
			viewholder.photo = (ImageView) convertView.findViewById(R.id.photo);
			viewholder.distance = (TextView) convertView
					.findViewById(R.id.distance);

			convertView.setTag(viewholder);
		} else {
			viewholder = (ViewHolder) convertView.getTag();
		}
		nearby = (Nearby) getItem(i);
		viewholder.name.setText(nearby.getName());
		viewholder.addr.setText("地址:" + nearby.getAddress());

		if (location != null) {
			double distance = LocationUtil.distanceByLnglat(nearby.getJingdu(),
					nearby.getWeidu(), location.getLongitude(),
					location.getLatitude());

			String priceDis;
			if (distance < 10000)
				priceDis = "距离:" + distance + "米";
			else
				priceDis = "距离:大于10000米";
			int type = Integer.parseInt(nearby.getType());
			switch (type) {
			case NearbyActivity.AIRPORT:
				priceDis += "(机场)";
				break;
			case NearbyActivity.HOTEL:
				priceDis += "(酒店)";
				break;
			case NearbyActivity.VIEWPOINT:
				priceDis += "(景点)";
				break;
			case NearbyActivity.HUOCHE:
				priceDis += "(火车站)";
				break;

			default:
				break;
			}
			viewholder.distance.setText(priceDis);

		}

		String introImg = nearby.getImageUrl();
		if (introImg != null && !TextUtils.isEmpty(introImg)
				&& TuanUtils.isPic(introImg)) {
			Uri uri = Uri.parse(introImg);
			if (!mRrm.exists(uri)) {
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
		TextView name;
		ImageView photo;
		TextView addr;
		TextView distance;
	}

	public void setLocation(Location location) {
		this.location = location;
	}
}
