package com.tudaidai.tuantrip;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

import com.tudaidai.tuantrip.app.OnBootReceiver;
import com.tudaidai.tuantrip.app.TuanTripApp;
import com.tudaidai.tuantrip.location.ILocation;
import com.tudaidai.tuantrip.location.LocationUtil;
import com.tudaidai.tuantrip.location.TuanLocation;
import com.tudaidai.tuantrip.types.GridInfo;
import com.tudaidai.tuantrip.types.Nearby;
import com.tudaidai.tuantrip.types.NearbyResult;
import com.tudaidai.tuantrip.types.UserResult;
import com.tudaidai.tuantrip.util.NotificationsUtil;
import com.tudaidai.tuantrip.util.TuanUtils;
import com.tudaidai.tuantrip.widget.GridAdapter;

public class TuanTripMain extends Activity implements ILocation {
	private GridView gridview;
	private List<GridInfo> list;
	private GridAdapter adapter;
	private static final int TUANGOU = 0;
	private static final int JIUDIAN = 3;
	private static final int HANGBAN = 5;
	private static final int JICHANG = 4;
	private static final int JIAOCHE = 2;
	private static final int JINGDIAN = 6;
	private static final int ZHOUBIAN = 1;
	private static final int WODE = 7;
	private static final int SET = 8;
	LocationUtil lUtil;
	Location mLocation;
	private Handler mHandler = new Handler();
	protected ProgressDialog mProgressDialog;
	private boolean isGetLocation = false;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

		Intent intent = new Intent(this, OnBootReceiver.class);
		sendBroadcast(intent);

		setContentView(R.layout.gridlayout);
		setProgressBarIndeterminateVisibility(false);
		autoLogin();

		gridview = (GridView) findViewById(R.id.gridview);
		list = new ArrayList<GridInfo>();
		list.add(new GridInfo("旅游团购", getResources().getDrawable(
				R.drawable.icon7)));
		list.add(new GridInfo("我的位置", getResources().getDrawable(
				R.drawable.icon5)));
		list.add(new GridInfo("叫车服务", getResources().getDrawable(
				R.drawable.icon4)));
		list.add(new GridInfo("酒店预订", getResources().getDrawable(
				R.drawable.icon2)));
		list.add(new GridInfo("机场信息", getResources().getDrawable(
				R.drawable.icon1)));
		list.add(new GridInfo("航班查询", getResources().getDrawable(
				R.drawable.icon3)));
		list.add(new GridInfo("景点查询", getResources().getDrawable(
				R.drawable.icon9)));
		list.add(new GridInfo("我的团程", getResources().getDrawable(
				R.drawable.icon6)));
		list.add(new GridInfo("系统设置", getResources().getDrawable(
				R.drawable.icon10)));
		adapter = new GridAdapter(this);
		adapter.setList(list);
		gridview.setAdapter(adapter);
		gridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (position == TUANGOU) {
					Intent intent = new Intent(TuanTripMain.this,
							TodayActivity.class);
					startActivity(intent);
				} else if (position == JIUDIAN) {
					Intent intent = new Intent(TuanTripMain.this,
							HotelActivity.class);
					startActivity(intent);
				} else if (position == HANGBAN) {
					Intent intent = new Intent(TuanTripMain.this,
							FlightActivity.class);
					startActivity(intent);
				} else if (position == JICHANG) {
					Intent intent = new Intent(TuanTripMain.this,
							AirportListActivity.class);
					startActivity(intent);
				} else if (position == JIAOCHE) {
					Intent intent = new Intent(TuanTripMain.this,
							CarListActivity.class);
					startActivity(intent);

				} else if (position == JINGDIAN) {
					Intent intent = new Intent(TuanTripMain.this,
							NearbyActivity.class);
					intent.putExtra(NearbyActivity.EXTRA_OPTION,
							NearbyActivity.VIEWPOINT);
					startActivity(intent);
				} else if (position == ZHOUBIAN) {
					TuanTrip trip = (TuanTrip) TuanTripMain.this
							.getApplication();
					String tuanlocation = trip.getTuanLocation();
					if (tuanlocation != null) {
						isGetLocation = true;

						String city = tuanlocation.split("-")[0];
						double jingdu = Double.parseDouble(tuanlocation
								.split("-")[1]);
						double weidu = Double.parseDouble(tuanlocation
								.split("-")[2]);
						String address = tuanlocation.split("-")[3];

						TuanLocation tLocation = new TuanLocation(new Location(
								LocationManager.NETWORK_PROVIDER));
						tLocation.setCity(city);
						tLocation.setLongitude(jingdu);
						tLocation.setLatitude(weidu);
						tLocation.setStreet(address);

						mLocation = tLocation;
						getNearBys();
					} else {
						lUtil.startGetLocation();
					}

				} else if (position == WODE) {
					Intent intent = new Intent(TuanTripMain.this,
							AccountActivity.class);
					startActivity(intent);
				} else if (position == SET) {
					Intent intent = new Intent(TuanTripMain.this,
							SetActivity.class);
					startActivity(intent);
				}

			}
		});

		lUtil = new LocationUtil(this, this);
		lUtil.setIsGps(true);

	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		boolean flag;
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			new AlertDialog.Builder(TuanTripMain.this)
					.setTitle("离开团程CC")
					.setMessage("确定要离开团程CC吗？")
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									TuanTripMain.this.finish();
								}
							})
					.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {

								}
							}).show();

			flag = true;
		} else {
			flag = super.onKeyDown(keyCode, event);
		}
		return flag;
	}

	// public void onDestroy()
	// {
	// TuanTrip tuanTrip = (TuanTrip)this.getApplication();
	// tuanTrip.setUser(null);
	// tuanTrip.setProduct(null);
	// tuanTrip.setProductList(null);
	// tuanTrip.setCities(null);
	// tuanTrip.setOpenGps(true);
	//
	// super.onDestroy();
	// android.os.Process.killProcess(android.os.Process.myPid()); //获取PID
	// }
	class LoginTask extends AsyncTask<String, Void, UserResult> {
		private static final boolean DEBUG = TuanTripSettings.DEBUG;
		private static final String TAG = "LoginTask";
		private Exception mReason = null;

		private LoginTask() {
		}

		protected UserResult doInBackground(String... paramArrayOfString) {
			if (DEBUG)
				Log.d("LoginTask", "doInBackground()");
			UserResult localUserResult = null;
			try {
				TuanTripApp localTuanTripApp = ((TuanTrip) TuanTripMain.this
						.getApplication()).getTuanTripApp();
				String str1 = paramArrayOfString[0];
				String str2 = paramArrayOfString[1];
				String str3 = paramArrayOfString[2];
				localUserResult = localTuanTripApp.login(str1, str2, str3);
			} catch (Exception localException) {
				if (DEBUG)
					Log.d("LoginTask", "Caught Exception logging in.",
							localException);
				this.mReason = localException;
				localUserResult = null;
			}
			return localUserResult;
		}

		protected void onCancelled() {
			super.onCancelled();
		}

		protected void onPostExecute(UserResult paramUserResult) {

			if (paramUserResult == null) {
				NotificationsUtil.ToastReasonForFailure(TuanTripMain.this,
						mReason);
			} else if (paramUserResult.mHttpResult.getStat() == TuanTripSettings.POSTSUCCESS) {
				Toast.makeText(TuanTripMain.this, "自动登录成功", Toast.LENGTH_SHORT)
						.show();
				TuanTrip tuantrip = (TuanTrip) getApplication();
				String pwd = tuantrip.getPwd();
				paramUserResult.mUser.setPwd(pwd);
				tuantrip.setUser(paramUserResult.mUser);
			} else if (paramUserResult.mHttpResult.getStat() == TuanTripSettings.POSTFAILED) {
				Toast.makeText(TuanTripMain.this,
						paramUserResult.mHttpResult.getMessage(),
						Toast.LENGTH_SHORT).show();
			} else {
				NotificationsUtil.ToastReasonForFailure(TuanTripMain.this,
						mReason);
			}
		}

		protected void onPreExecute() {
			if (DEBUG)
				Log.d("LoginTask", "onPreExecute()");
			// LoginActivity.this.startProgressBar();
		}
	}

	private void autoLogin() {
		TuanTrip tuantrip = (TuanTrip) getApplication();
		if (tuantrip.getUser() != null)
			return;

		String userName = tuantrip.getUserName();
		String pwd = tuantrip.getPwd();
		if (userName.length() > 0 && pwd.length() > 0) {
			LoginTask logintask = new LoginTask();
			String as[] = new String[3];
			as[0] = userName;
			as[1] = pwd;
			as[2] = "1";
			logintask.execute(as);
		}
	}

	@Override
	public void onResume() {
		lUtil.requestLocationUpdates();

		super.onResume();
	}

	@Override
	public void onPause() {

		lUtil.removeLocationUpdates();

		super.onPause();
	}

	@Override
	public void locationTaskPost() {
		mLocation = lUtil.getmLocation();
		dismissProgressDialog();

		getAddressAndNearbys();

	}

	@Override
	public void locationTaskPre() {
		startProgressBar("定位", "定位中...");

	}

	@Override
	public void getNoLocation() {
		dismissProgressDialog();
		Toast.makeText(TuanTripMain.this, "无法定位", Toast.LENGTH_SHORT).show();

	}

	@Override
	public void updateLocation() {
	}

	protected void dismissProgressDialog() {
		if (mProgressDialog != null)
			mProgressDialog.dismiss();
	}

	protected void startProgressBar(String title, String content) {
		// if(mProgressDialog==null)
		mProgressDialog = ProgressDialog.show(this, title, content);

		mProgressDialog.setCancelable(true);
		mProgressDialog.show();
	}

	private void getAddressAndNearbys() {
		new Thread() {
			public void run() {

				Location loc = lUtil.getTuanLocationWithGao(mLocation);
				if (loc == null || mLocation == null) {
					mHandler.post(new Runnable() {

						@Override
						public void run() {
							getNoLocation();
						}
					});

					return;
				}

				mLocation = loc;
				mHandler.post(new Runnable() {

					@Override
					public void run() {

						getNearBys();

					}
				});

			}
		}.start();
	}

	class NearbyListTask extends AsyncTask<String, Void, NearbyResult> {
		private static final String TAG = "NearbyListTask";
		private Exception mReason;

		public NearbyListTask() {
		}

		protected NearbyResult doInBackground(String... paramArrayOfString) {

			NearbyResult orderResult = null;

			if (paramArrayOfString[4].equals("")) {
				this.mReason = new Exception("未定位到城市,请刷新");
				return orderResult;
			}

			try {
				TuanTripApp localTuanTripApp = ((TuanTrip) TuanTripMain.this
						.getApplication()).getTuanTripApp();
				orderResult = localTuanTripApp.getNearby(paramArrayOfString,
						"1", "1");

			} catch (Exception localException) {
				this.mReason = localException;
				orderResult = null;
			}

			return orderResult;
		}

		protected void onPostExecute(NearbyResult paramOrders) {

			if (paramOrders != null
					&& paramOrders.mHttpResult.getStat() == TuanTripSettings.POSTSUCCESS
					&& paramOrders.mGroup.size() != 0) {

				Intent intent = null;
				int fid = ((Nearby) paramOrders.mGroup.get(0)).getFid();
				int type = 0;
				try {
					type = Integer
							.parseInt(((Nearby) paramOrders.mGroup.get(0))
									.getType());
				} catch (Exception e) {
					return;
				}
				switch (type) {
				case NearbyActivity.AIRPORT:
					intent = new Intent(TuanTripMain.this,
							AirportDetailActivity.class);
					intent.putExtra(AirportDetailActivity.ID, fid);
					break;
				case NearbyActivity.HOTEL:
					intent = new Intent(TuanTripMain.this,
							HotelDetailActivity.class);
					intent.putExtra(HotelDetailActivity.HOTEL_ID, fid);
					String[] as = TuanUtils.getTommory();
					intent.putExtra(HotelDetailActivity.COME_DATE, as[0]);
					intent.putExtra(HotelDetailActivity.OUT_DATE, as[1]);
					break;
				case NearbyActivity.VIEWPOINT:
					intent = new Intent(TuanTripMain.this,
							ViewpointDetailActivity.class);
					intent.putExtra(ViewpointDetailActivity.VID, fid);

					break;

				default:
					break;
				}

				if (intent != null) {
					startActivity(intent);
				}

			} else {
				Intent intent = new Intent(TuanTripMain.this,
						NearbyActivity.class);
				startActivity(intent);
			}

			dismissProgressDialog();
			setProgressBarIndeterminateVisibility(false);
		}

		protected void onCancelled() {

			super.onCancelled();
		}

		protected void onPreExecute() {
			setProgressBarIndeterminateVisibility(true);
			// if(isGetLocation)
			{
				startProgressBar("获取数据", "获取数据中...");
			}
		}
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
}