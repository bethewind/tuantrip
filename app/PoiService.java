package com.tudaidai.tuantrip.app;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.tudaidai.tuantrip.AirportDetailActivity;
import com.tudaidai.tuantrip.HotelDetailActivity;
import com.tudaidai.tuantrip.NearbyActivity;
import com.tudaidai.tuantrip.R;
import com.tudaidai.tuantrip.TuanTrip;
import com.tudaidai.tuantrip.TuanTripSettings;
import com.tudaidai.tuantrip.ViewpointDetailActivity;
import com.tudaidai.tuantrip.location.ILocation;
import com.tudaidai.tuantrip.location.LocationUtil;
import com.tudaidai.tuantrip.location.TuanLocation;
import com.tudaidai.tuantrip.types.CommentListResult;
import com.tudaidai.tuantrip.types.Nearby;
import com.tudaidai.tuantrip.types.NearbyResult;
import com.tudaidai.tuantrip.util.TuanUtils;

public class PoiService extends Service implements ILocation {

	static final boolean DEBUG = TuanTripSettings.DEBUG;
	static final String TAG = "PoiService";
	NotificationManager mNM;
	LocationUtil lUtil;
	volatile Location mLocation;
	Nearby nearby;
	private Handler mHandler = new Handler();
	static final int mSleepTimeInMs = 30 * 1000;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		if (DEBUG)
			Log.e(TAG, "onCreate()");

		mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

		lUtil = new LocationUtil(this, this);
		lUtil.setIsGps(true);
		lUtil.requestLocationUpdates();
		lUtil.startGetLocation();
	}

	@Override
	public void onDestroy() {
		if (DEBUG)
			Log.e(TAG, "onDestroy()");

		lUtil.removeLocationUpdates();
	}

	@Override
	public void locationTaskPost() {
		mLocation = lUtil.getmLocation();

		getAddressAndNearbys();

	}

	@Override
	public void locationTaskPre() {
		// TODO Auto-generated method stub

	}

	@Override
	public void getNoLocation() {
		PoiService.this.stopSelf();

	}

	@Override
	public void updateLocation() {
		mLocation = lUtil.getmLocation();

	}

	private void getAddressAndNearbys() {
		new Thread() {
			public void run() {

				if (mSleepTimeInMs > 0L) {
					try {
						Thread.sleep(mSleepTimeInMs);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				Location loc = lUtil.getTuanLocation(mLocation);
				if (loc == null || mLocation == null) {
					getNoLocation();
					return;
				}

				mLocation = loc;

				TuanLocation tuanLocation = (TuanLocation) mLocation;
				TuanTrip trip = (TuanTrip) PoiService.this.getApplication();
				String city = tuanLocation.getCity();
				String jingdu = String.valueOf(tuanLocation.getLongitude());
				String weidu = String.valueOf(tuanLocation.getLatitude());
				String address = tuanLocation.getStreet();
				trip.setTuanLocation(city + "-" + jingdu + "-" + weidu + "-"
						+ address);

				mHandler.post(new Runnable() {

					@Override
					public void run() {

						getNearBys();

					}
				});

			}
		}.start();
	}

	private void getNearBys() {
		String[] as = new String[5];
		as[0] = new Double(mLocation.getLongitude()).toString();
		as[1] = new Double(mLocation.getLatitude()).toString();
		as[2] = "500";
		as[3] = "0";
		as[4] = (mLocation instanceof TuanLocation) ? ((TuanLocation) mLocation)
				.getCity() : "";
		new NearbyListTask().execute(as);
	}

	class NearbyListTask extends AsyncTask<String, Void, Integer> {
		private static final String TAG = "NearbyListTask";
		private Exception mReason;

		public NearbyListTask() {
		}

		protected Integer doInBackground(String... paramArrayOfString) {

			Integer rInteger = 0;

			if (paramArrayOfString[4].equals("")) {
				this.mReason = new Exception("未定位到城市,请刷新");
				return 0;
			}

			try {
				TuanTripApp localTuanTripApp = ((TuanTrip) PoiService.this
						.getApplication()).getTuanTripApp();
				NearbyResult orderResult = localTuanTripApp.getNearby(
						paramArrayOfString, "1", "1");

				if (orderResult != null && orderResult.mGroup.size() != 0) {
					nearby = orderResult.mGroup.get(0);
					int lid = nearby.getLid();
					String[] as = new String[3];
					as[0] = String.valueOf(lid);
					as[1] = "1";
					as[2] = "1";
					CommentListResult cResult = localTuanTripApp
							.getCommentList(as);
					if (cResult != null) {
						rInteger = cResult.getTotal();
					}
				} else {
					nearby = null;
				}

			} catch (Exception localException) {
				this.mReason = localException;
				rInteger = 0;
			}

			return rInteger;
		}

		protected void onPostExecute(Integer rInteger) {

			if (nearby == null)
				return;

			if (rInteger != 0) {
				String poi = ((TuanTrip) PoiService.this.getApplication())
						.getPoi();
				int lid = Integer.parseInt(poi.split("-")[0]);
				int total = Integer.parseInt(poi.split("-")[1]);
				if (lid != 0) {
					if ((nearby.getLid() == lid) && rInteger > total) {
						showNotification();
					}

				}

			}

			String poi1 = String.valueOf(nearby.getLid()) + "-"
					+ String.valueOf(rInteger);
			((TuanTrip) PoiService.this.getApplication()).setPoi(poi1);

			PoiService.this.stopSelf();
		}

		protected void onCancelled() {

			super.onCancelled();
		}

		protected void onPreExecute() {
		}

	}

	private void showNotification() {

		String content = "当前附近:" + nearby.getName() + "有最新消息";

		Notification notification = new Notification(R.drawable.icon, content,
				System.currentTimeMillis());

		PendingIntent contentIntent;
		Intent intent = null;
		int fid = nearby.getFid();
		int type = 0;
		try {
			type = Integer.parseInt(nearby.getType());
		} catch (Exception e) {
			return;
		}
		switch (type) {
		case NearbyActivity.AIRPORT:
			intent = new Intent(PoiService.this, AirportDetailActivity.class);
			intent.putExtra(AirportDetailActivity.ID, fid);
			break;
		case NearbyActivity.HOTEL:
			intent = new Intent(PoiService.this, HotelDetailActivity.class);
			intent.putExtra(HotelDetailActivity.HOTEL_ID, fid);
			String[] as = TuanUtils.getTommory();
			intent.putExtra(HotelDetailActivity.COME_DATE, as[0]);
			intent.putExtra(HotelDetailActivity.OUT_DATE, as[1]);
			break;
		case NearbyActivity.VIEWPOINT:
			intent = new Intent(PoiService.this, ViewpointDetailActivity.class);
			intent.putExtra(ViewpointDetailActivity.VID, fid);

			break;

		default:
			break;
		}
		if (intent != null) {
			mNM.cancel(R.drawable.icon);
			contentIntent = PendingIntent.getActivity(this, 0, intent, 0);

			notification.setLatestEventInfo(this, content, content,
					contentIntent);
			mNM.notify(R.drawable.icon, notification);
		}

	}

}
