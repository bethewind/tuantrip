package com.tudaidai.tuantrip.widget;

import java.util.Observable;
import java.util.Observer;

import android.content.Context;
import android.location.Location;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tudaidai.tuantrip.R;
import com.tudaidai.tuantrip.TuanTripSettings;
import com.tudaidai.tuantrip.types.Airport;
import com.tudaidai.tuantrip.util.RemoteResourceManager;

public class AirportResultListAdapter extends BaseGroupAdapter<Airport> {

	private Handler mHandler = new Handler();
	private LayoutInflater mInflater;
	private RemoteResourceManagerObserver mResourcesObserver;
	private RemoteResourceManager mRrm;
	static final boolean DEBUG = TuanTripSettings.DEBUG;
	static final String TAG = "NearbyResultListAdapter";
	private Context context;
	private Location location;

	public AirportResultListAdapter(Context context,
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
		Airport airport = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.airport_list_item, null);
			viewholder = new ViewHolder();
			viewholder.name = (TextView) convertView.findViewById(R.id.name);
			viewholder.addr = (TextView) convertView.findViewById(R.id.addr);
			// viewholder.photo =
			// (ImageView)convertView.findViewById(R.id.photo);

			convertView.setTag(viewholder);
		} else {
			viewholder = (ViewHolder) convertView.getTag();
		}
		airport = (Airport) getItem(i);
		viewholder.name.setText(airport.getName());
		viewholder.addr.setText(airport.getAddress());

		// String introImg = airport.getImageUrl();
		// if (introImg != null && !TextUtils.isEmpty(introImg)&&
		// TuanUtils.isPic(introImg))
		// {
		// Uri uri = Uri.parse(introImg);
		// if (!mRrm.exists(uri))
		// {
		// TuanTrip tuanTrip = (TuanTrip)context.getApplicationContext();
		// viewholder.photo.setImageDrawable(context.getResources().getDrawable(R.drawable.img_defalt));
		// //if(tuanTrip.getShowPic())
		// {
		// if(DEBUG) Log.d(TAG, "!mRrm.exists(uri)"+uri);
		// mRrm.request(uri);
		// }
		// } else
		// {
		// if(DEBUG) Log.d(TAG, "mRrm exists "+uri);
		// try
		// {
		//
		// Bitmap bitmap = mRrm.getBitmap(uri);
		// viewholder.photo.setImageBitmap(bitmap);
		// }
		// catch (Exception exception) { }
		// }
		// }

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
	}

	public void setLocation(Location location) {
		this.location = location;
	}
}
