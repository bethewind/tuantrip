package com.tudaidai.tuantrip;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tudaidai.tuantrip.app.SearchLoadableListActivity;
import com.tudaidai.tuantrip.app.TuanTripApp;
import com.tudaidai.tuantrip.location.ILocation;
import com.tudaidai.tuantrip.location.LocationUtil;
import com.tudaidai.tuantrip.location.TuanLocation;
import com.tudaidai.tuantrip.types.Cities;
import com.tudaidai.tuantrip.types.City;
import com.tudaidai.tuantrip.types.Group;
import com.tudaidai.tuantrip.types.Nearby;
import com.tudaidai.tuantrip.types.NearbyResult;
import com.tudaidai.tuantrip.types.Order;
import com.tudaidai.tuantrip.util.NotificationsUtil;
import com.tudaidai.tuantrip.util.RemoteResourceManager;
import com.tudaidai.tuantrip.util.TuanUtils;
import com.tudaidai.tuantrip.widget.NearbyResultListAdapter;

public class NearbyActivity extends SearchLoadableListActivity implements
		ILocation {
	static final boolean DEBUG = TuanTripSettings.DEBUG;
	static final String TAG = "NearbyActivity";

	public static final String PAGE_NUM = "8";
	private static final int MENU_REFRESH = 0;
	private static final int MENU_DISTANCE = 1;
	private static final int MENU_SELECT = 2;
	private static final int MENU_MAP = 3;
	public static final String EXTRA_OPTION = "com.tudaidai.tuantrip.NearbyActivity.OPTION";
	public static final String EXTRA_LOCATION = "com.tudaidai.tuantrip.NearbyActivity.EXTRA_LOCATION";
	private int option;
	private NearbyResultListAdapter mListAdapter;
	private ListView mListView;
	private Group<Order> orders;
	private NearbyResult orderResult;
	private int lastItem = 0;
	LinearLayout loadingLayout;
	Location mLocation;
	Location lastLocation;
	WifiManager mainWifi;
	LocationUtil lUtil;

	private static final int DIALOG_OPENGPS = 1;
	private volatile boolean isGpsEable = true;
	private volatile boolean isWifiEable = true;
	private String openGpsText = "未打开以下定位模块,是否现在打开?<br/>";

	private volatile boolean isFinish = true;
	private volatile boolean isFinish1 = true;
	private volatile boolean isFirstLoad = true;
	private volatile boolean isNearby = true;
	private volatile boolean isNearby1 = true;// 是否是搜索lbs信息周边
	private volatile boolean isScro = false;

	Button cityButton;
	EditText contentText;
	TextView locationText;
	Button searchButton;
	static final int CITY_DIALOG = 3;
	private Group<City> mCities;
	public static final int ALL = 0;
	public static final int AIRPORT = 1;
	public static final int HOTEL = 2;
	public static final int VIEWPOINT = 3;
	public static final int HUOCHE = 4;
	String city = null;
	String type = "0";
	int typeItem = 0;
	String typeStrings[] = { "全部", "机场", "酒店", "景点", "火车站" };
	String typeStrings1[] = { "0", "1", "2", "3", "4" };
	String banjing = "2000";
	int banjingItem = 2;
	String banjingStrings[] = { "500米", "1000米", "2000米", "5000米" };
	String banjingStrings1[] = { "500", "1000", "2000", "5000" };

	private LayoutParams mLayoutParams = new LinearLayout.LayoutParams(
			LinearLayout.LayoutParams.WRAP_CONTENT,
			LinearLayout.LayoutParams.WRAP_CONTENT);
	private LayoutParams FFlayoutParams = new LinearLayout.LayoutParams(
			LinearLayout.LayoutParams.FILL_PARENT,
			LinearLayout.LayoutParams.FILL_PARENT);

	private Handler mHandler = new Handler();

	private void ensureUi(NearbyResult paramOrders) {

		if (orderResult == null) {
			if (paramOrders.mGroup.size() != 0) {
				orderResult = paramOrders;
				if (orderResult != null
						&& orderResult.mGroup.size() == orderResult.getTotla()) {
					mListView.removeFooterView(loadingLayout);
				}

				mListAdapter.setGroup(orderResult.mGroup);
			} else {
				setEmptyView();
			}
		} else {
			for (Nearby nearby : paramOrders.mGroup) {
				orderResult.mGroup.add(nearby);
			}

			// mListView.removeFooterView(loadingLayout);

			mListAdapter.notifyDataSetChanged();

		}

	}

	public boolean onCreateOptionsMenu(Menu menu) {
		if (isNearby1) {
			menu.add(0, MENU_REFRESH, 0, "刷新").setIcon(
					R.drawable.ic_menu_refresh);
		} else {
			menu.add(0, MENU_REFRESH, 0, "搜索周边").setIcon(
					R.drawable.btn_icon_nears_normal);
		}
		menu.add(0, MENU_DISTANCE, 0, "距离").setIcon(R.drawable.icon_nav_foot);
		menu.add(0, MENU_SELECT, 0, "筛选").setIcon(R.drawable.icon_nav_back);
		menu.add(0, MENU_MAP, 0, "地图模式").setIcon(R.drawable.icon_viewmap);
		super.onCreateOptionsMenu(menu);

		return true;
	}

	public boolean onOptionsItemSelected(MenuItem menuitem) {
		boolean flag = super.onOptionsItemSelected(menuitem);
		Intent intent = null;
		switch (menuitem.getItemId()) {
		case MENU_REFRESH:
			mListAdapter = null;
			isNearby1 = true;
			isNearby = true;
			lUtil.setIsGps(true);
			isFinish = true;
			isScro = false;

			lUtil.requestLocationUpdates();
			lUtil.startGetLocation();

			break;
		case MENU_DISTANCE:

			new AlertDialog.Builder(NearbyActivity.this)
					.setTitle("选择距离")
					.setIcon(android.R.drawable.ic_dialog_info)
					.setSingleChoiceItems(banjingStrings, banjingItem,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									banjing = banjingStrings1[which];
									banjingItem = which;

									if (isNearby) {
										if (mLocation == null) {
											lUtil.requestLocationUpdates();
											lUtil.startGetLocation();
										} else {
											getNearBys();
										}
									}

									dialog.cancel();
								}
							}).setNegativeButton("取消", null).show();

			break;
		case MENU_SELECT:
			new AlertDialog.Builder(NearbyActivity.this)
					.setTitle("选择类型")
					.setIcon(android.R.drawable.ic_dialog_info)
					.setSingleChoiceItems(typeStrings, typeItem,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									type = typeStrings1[which];
									typeItem = which;

									if (isNearby) {
										if (mLocation == null) {
											lUtil.requestLocationUpdates();
											lUtil.startGetLocation();
										} else {
											getNearBys();
										}
									} else {
										getNearBys1();
									}

									dialog.cancel();
								}
							}).setNegativeButton("取消", null).show();
			break;
		case MENU_MAP:
			Intent intent2 = new Intent(NearbyActivity.this, Map.class);

			if (orderResult != null && orderResult.mGroup.size() != 0) {
				ArrayList<Nearby> arrayList = new ArrayList<Nearby>();
				for (Nearby nearby : orderResult.mGroup) {
					arrayList.add(nearby);
				}
				intent2.putParcelableArrayListExtra(Map.POIGROUPS, arrayList);
			}

			if (mLocation != null) {
				ArrayList<Location> arrayList1 = new ArrayList<Location>();
				arrayList1.add(mLocation);
				intent2.putParcelableArrayListExtra(Map.MYLOGROUPS, arrayList1);
			}

			startActivity(intent2);
			break;
		default:
			break;
		}

		return flag;
	}

	protected void onCreate(Bundle bundle) {
		if (DEBUG)
			Log.d(TAG, "onCreate");
		super.onCreate(bundle);
		locationText = (TextView) findViewById(R.id.locationText);

		if (getIntent().hasExtra(EXTRA_LOCATION)) {
			String[] as = getIntent().getStringArrayExtra(EXTRA_LOCATION);
			// 121.1,31.2,城市,地址
			isNearby1 = false;
			TuanLocation location1 = new TuanLocation(new Location(
					LocationManager.NETWORK_PROVIDER));
			location1.setStreet(as[3]);
			location1.setCity(as[2]);
			location1.setLatitude(Double.parseDouble(as[1]));
			location1.setLongitude(Double.parseDouble(as[0]));
			mLocation = location1;
			setLocationText();
		}
		if (getIntent().hasExtra(EXTRA_OPTION)) {
			type = String.valueOf(getIntent().getIntExtra(EXTRA_OPTION, 0));
			typeItem = Integer.parseInt(type);
		}
		mainWifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);

		cityButton = (Button) findViewById(R.id.cityButton);
		cityButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (mCities == null || mCities.size() == 0) {
					Void[] as = new Void[0];
					new CitiesTask().execute(as);
				} else {
					showDialog(CITY_DIALOG);
				}

			}
		});
		searchButton = (Button) findViewById(R.id.searchButton);
		contentText = (EditText) findViewById(R.id.contentText);
		searchButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (city == null) {
					Toast.makeText(NearbyActivity.this, "请选择城市或等待定位城市",
							Toast.LENGTH_SHORT).show();
					return;
				}

				if (isFinish) {
					getNearBys1();
					isNearby = false;
				} else {
					Toast.makeText(NearbyActivity.this, "请稍后",
							Toast.LENGTH_SHORT).show();
					return;
				}

			}
		});

		lUtil = new LocationUtil(this, this);
		lUtil.setIsGps(true);
		isFinish = true;

		if (isNearby1) {
			if (DEBUG)
				Toast.makeText(NearbyActivity.this, "locationTask",
						Toast.LENGTH_SHORT).show();

			TuanTrip trip = (TuanTrip) NearbyActivity.this.getApplication();
			String tuanlocation = trip.getTuanLocation();
			if (tuanlocation != null) {
				String city = tuanlocation.split("-")[0];
				double jingdu = Double.parseDouble(tuanlocation.split("-")[1]);
				double weidu = Double.parseDouble(tuanlocation.split("-")[2]);
				String address = tuanlocation.split("-")[3];

				TuanLocation tLocation = new TuanLocation(new Location(
						LocationManager.NETWORK_PROVIDER));
				tLocation.setCity(city);
				tLocation.setLongitude(jingdu);
				tLocation.setLatitude(weidu);
				tLocation.setStreet(address);

				lUtil.setIsGps(false);
				mLocation = tLocation;
				setLocationText();
				getNearBys();
			} else {
				lUtil.startGetLocation();
			}

		} else {
			getNearBys();
		}

	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case CITY_DIALOG:
			CityPickerDialog citydialog = new CityPickerDialog(this, mCities);
			citydialog.setOnCancelListener(new OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					CityPickerDialog citypickerdialog = (CityPickerDialog) dialog;
					String city1 = citypickerdialog.getChosenCityName();
					if (!city1.equals("-1")) {
						city = city1;
						cityButton.setText(city1 + "/切换");
						searchButton.performClick();
					}
					removeDialog(CITY_DIALOG);

				}
			});
			return citydialog;
		case DIALOG_OPENGPS:
			LayoutInflater factory = LayoutInflater.from(this);
			final View textEntryView = factory.inflate(R.layout.gps_dialog,
					null);
			TextView contentText = (TextView) textEntryView
					.findViewById(R.id.contentText);
			CheckBox nocBox = (CheckBox) textEntryView.findViewById(R.id.noCb);
			nocBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView,
						boolean isChecked) {
					TuanTrip tuanTrip = (TuanTrip) getApplication();
					if (isChecked)
						tuanTrip.setOpenGps(false);
					else
						tuanTrip.setOpenGps(true);

				}
			});
			contentText.setText(Html.fromHtml(openGpsText));
			return new AlertDialog.Builder(NearbyActivity.this)
					.setIcon(R.drawable.alert_dialog_icon)
					.setTitle("温馨提示")
					.setView(textEntryView)
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {

									if (!isGpsEable) {
										Intent myIntent = new Intent(
												Settings.ACTION_SECURITY_SETTINGS);
										startActivity(myIntent);
									}
									if (!isWifiEable) {
										new Thread() {
											public void run() {
												mainWifi.setWifiEnabled(true);
												mHandler.post(new Runnable() {

													@Override
													public void run() {
														Toast.makeText(
																NearbyActivity.this,
																"已打开wifi",
																Toast.LENGTH_SHORT)
																.show();

													}
												});
											}
										}.start();
									}
								}
							})
					.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {

									/* User clicked cancel so do some stuff */
								}
							}).create();
		}

		return null;
	}

	private void addLoadView() {
		LinearLayout layout = new LinearLayout(this);
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
		layout.addView(textView, FFlayoutParams);
		layout.setGravity(Gravity.CENTER);

		// 设置ListView的页脚layout
		loadingLayout = new LinearLayout(this);
		loadingLayout.addView(layout, mLayoutParams);
		loadingLayout.setGravity(Gravity.CENTER);

		mListView.addFooterView(loadingLayout);

	}

	@Override
	protected void onStart() {
		super.onStart();
		if (DEBUG)
			Log.e(TAG, "start onStart~~~");
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		if (DEBUG)
			Log.e(TAG, "start onRestart~~~");
	}

	@Override
	public void onDestroy() {
		if (DEBUG)
			Toast.makeText(this, "onDestroy", Toast.LENGTH_SHORT).show();
		super.onDestroy();
	}

	@Override
	public void onResume() {

		if (!mainWifi.isWifiEnabled()) {
			isWifiEable = false;
		} else {
			isWifiEable = true;
		}
		LocationManager alm = (LocationManager) this
				.getSystemService(Context.LOCATION_SERVICE);
		if (!alm.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
			isGpsEable = false;
		} else {
			isGpsEable = true;
		}

		if (DEBUG)
			Log.d(TAG, "onResume");

		if (isNearby1) {
			lUtil.requestLocationUpdates();
		}

		if (mListAdapter != null) {
			mListAdapter.addObserver();
		}
		super.onResume();
	}

	@Override
	public void onPause() {
		if (DEBUG)
			Toast.makeText(this, "onPause", Toast.LENGTH_SHORT).show();

		if (isNearby1) {
			lUtil.removeLocationUpdates();
		}

		if (mListAdapter != null)
			mListAdapter.removeObserver();

		super.onPause();
	}

	public Object onRetainNonConfigurationInstance() {
		Log.e(TAG, "onRetainNonConfigurationInstance~~~");
		return new Integer(100);
	}

	class NearbyListTask extends AsyncTask<String, Void, NearbyResult> {
		private static final String TAG = "NearbyListTask";
		private Exception mReason;

		public NearbyListTask() {
		}

		protected NearbyResult doInBackground(String... paramArrayOfString) {
			if (DEBUG)
				Log.d(TAG, "doInBackground()");
			NearbyResult orderResult = null;

			if (paramArrayOfString[4].equals("")) {
				this.mReason = new Exception("未定位到城市,请刷新");
				return orderResult;
			}

			try {
				TuanTripApp localTuanTripApp = ((TuanTrip) NearbyActivity.this
						.getApplication()).getTuanTripApp();
				orderResult = localTuanTripApp.getNearby(paramArrayOfString,
						new Integer(lastItem + 1).toString(), PAGE_NUM);

				if (DEBUG)
					Log.d(TAG, "doInBackground() over");

			} catch (Exception localException) {
				this.mReason = localException;
				orderResult = null;
			}

			return orderResult;
		}

		protected void onPostExecute(NearbyResult paramOrders) {
			if (DEBUG)
				Log.d(TAG, "onPostExecute()");
			if (paramOrders == null) {
				// Toast.makeText(NearbyActivity.this,"无相关信息",Toast.LENGTH_SHORT).show();
				setEmptyView();
				mListAdapter = null;
			} else if (paramOrders.mHttpResult.getStat() == TuanTripSettings.POSTSUCCESS) {
				ensureUi(paramOrders);
			} else if (paramOrders.mHttpResult.getStat() == TuanTripSettings.POSTFAILED) {
				// Toast.makeText(NearbyActivity.this,"无相关信息",Toast.LENGTH_SHORT).show();
				setEmptyView();
				mListAdapter = null;
			} else {
				// Toast.makeText(NearbyActivity.this,"无相关信息",Toast.LENGTH_SHORT).show();
				setEmptyView();
				mListAdapter = null;
			}
			if (!isFirstLoad) {
				dismissProgressDialog();
			}
			if (!isScro && isNearby1) {
				openGpsWifi();
			}

			isFinish = true;
			isFirstLoad = false;
			isScro = false;
		}

		protected void onCancelled() {
			if (DEBUG)
				Log.d(TAG, "onCancelled()");
			super.onCancelled();
		}

		protected void onPreExecute() {
			if (DEBUG)
				Log.d(TAG, "onPreExecute()");
			if (!isScro) {
				// if (isFirstLoad)
				{
					setLoadingView();
					mEmptyText.setText("获取周边信息");
				}
			}
			isFinish = false;

			if (DEBUG)
				Toast.makeText(NearbyActivity.this, "NearbyListTaskPreexecute",
						Toast.LENGTH_SHORT).show();
		}
	}

	class NearbyListTask1 extends AsyncTask<String, Void, NearbyResult> {
		private static final String TAG = "NearbyListTask1";
		private Exception mReason;

		public NearbyListTask1() {
		}

		protected NearbyResult doInBackground(String... paramArrayOfString) {
			if (DEBUG)
				Log.d(TAG, "doInBackground()");
			NearbyResult orderResult = null;

			try {
				TuanTripApp localTuanTripApp = ((TuanTrip) NearbyActivity.this
						.getApplication()).getTuanTripApp();
				orderResult = localTuanTripApp.getNearbySearchInfo(
						paramArrayOfString,
						new Integer(lastItem + 1).toString(), PAGE_NUM);

				if (DEBUG)
					Log.d(TAG, "doInBackground() over");

			} catch (Exception localException) {
				this.mReason = localException;
				orderResult = null;
			}

			return orderResult;
		}

		protected void onPostExecute(NearbyResult paramOrders) {
			if (DEBUG)
				Log.d(TAG, "onPostExecute()");
			if (paramOrders == null) {
				NotificationsUtil.ToastReasonForFailure(NearbyActivity.this,
						mReason);
				setEmptyView();
				mListAdapter = null;
			} else if (paramOrders.mHttpResult.getStat() == TuanTripSettings.POSTSUCCESS) {
				ensureUi(paramOrders);
			} else if (paramOrders.mHttpResult.getStat() == TuanTripSettings.POSTFAILED) {
				Toast.makeText(NearbyActivity.this,
						paramOrders.mHttpResult.getMessage(),
						Toast.LENGTH_SHORT).show();
				setEmptyView();
				mListAdapter = null;
			} else {
				NotificationsUtil.ToastReasonForFailure(NearbyActivity.this,
						mReason);
				setEmptyView();
				mListAdapter = null;
			}

			dismissProgressDialog();

			isFinish = true;
			isFirstLoad = false;
			isScro = false;
		}

		protected void onCancelled() {
			if (DEBUG)
				Log.d(TAG, "onCancelled()");
			super.onCancelled();
		}

		protected void onPreExecute() {
			if (DEBUG)
				Log.d(TAG, "onPreExecute()");

			setLoadingView();
			mEmptyText.setText("获取数据中");

			if (!isScro) {
				// startProgressBar("加载数据","加载数据中...");
			}

			isFinish = false;
		}
	}

	private void getNearBys() {
		if (DEBUG)
			Toast.makeText(NearbyActivity.this, "getNearBys",
					Toast.LENGTH_SHORT).show();

		initia();
		mListView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				if (!isFinish)
					return;
				if (orderResult == null)
					return;

				if (orderResult != null
						&& orderResult.mGroup.size() == orderResult.getTotla()
						&& mListView.getFooterViewsCount() != 0) {
					mListView.removeFooterView(loadingLayout);
				}
				if (DEBUG)
					Log.d(TAG, "Scroll>>>first: " + firstVisibleItem
							+ ", visible: " + visibleItemCount + ", total: "
							+ totalItemCount);
				lastItem = firstVisibleItem + visibleItemCount - 1;
				if (DEBUG)
					Log.d(TAG, "Scroll>>>lastItem:" + lastItem);
				if (orderResult.mGroup.size() != orderResult.getTotla()) {

					if (firstVisibleItem + visibleItemCount == totalItemCount) {
						isScro = true;
						String[] as = new String[5];
						as[0] = new Double(lastLocation.getLongitude())
								.toString();
						as[1] = new Double(lastLocation.getLatitude())
								.toString();
						as[2] = banjing;
						as[3] = type;
						as[4] = (lastLocation instanceof TuanLocation) ? ((TuanLocation) lastLocation)
								.getCity() : "";
						new NearbyListTask().execute(as);
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

		String[] as = new String[5];
		as[0] = new Double(lastLocation.getLongitude()).toString();
		as[1] = new Double(lastLocation.getLatitude()).toString();
		as[2] = banjing;
		as[3] = type;
		as[4] = (lastLocation instanceof TuanLocation) ? ((TuanLocation) lastLocation)
				.getCity() : "";
		if (isNearby1) {
			city = as[4];
			cityButton.setText(city + "/切换");
		}
		new NearbyListTask().execute(as);
	}

	private void getNearBys1() {

		initia();
		mListView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				if (!isFinish)
					return;
				if (orderResult == null)
					return;

				if (orderResult != null
						&& orderResult.mGroup.size() == orderResult.getTotla()
						&& mListView.getFooterViewsCount() != 0) {
					mListView.removeFooterView(loadingLayout);
				}
				if (DEBUG)
					Log.d(TAG, "Scroll>>>first: " + firstVisibleItem
							+ ", visible: " + visibleItemCount + ", total: "
							+ totalItemCount);
				lastItem = firstVisibleItem + visibleItemCount - 1;
				if (DEBUG)
					Log.d(TAG, "Scroll>>>lastItem:" + lastItem);
				if (orderResult.mGroup.size() != orderResult.getTotla()) {

					if (firstVisibleItem + visibleItemCount == totalItemCount) {
						isScro = true;
						String[] as = new String[3];
						as[0] = contentText.getText().toString();
						as[1] = type;
						as[2] = city;
						new NearbyListTask1().execute(as);
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

		String[] as = new String[3];
		as[0] = contentText.getText().toString();
		as[1] = type;
		as[2] = city;

		new NearbyListTask1().execute(as);
	}

	private void initia() {
		isFinish = false;

		mListView = getListView();
		lastItem = 0;
		RemoteResourceManager localRemoteResourceManager = ((TuanTrip) getApplication())
				.getFileRemoteResourceManager();
		mListAdapter = new NearbyResultListAdapter(NearbyActivity.this,
				localRemoteResourceManager);
		orderResult = null;
		lastLocation = mLocation;
		mListAdapter.setLocation(mLocation);
		if (mListView.getFooterViewsCount() == 0)
			addLoadView();
		mListView.setAdapter(mListAdapter);
		mListView.setBackgroundResource(R.color.scroll_bk);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int i,
					long id) {
				Intent intent = null;
				int fid = ((Nearby) mListAdapter.getItem(i)).getFid();
				int type = 0;
				try {
					type = Integer.parseInt(((Nearby) mListAdapter.getItem(i))
							.getType());
				} catch (Exception e) {
					return;
				}
				switch (type) {
				case AIRPORT:
					intent = new Intent(NearbyActivity.this,
							AirportDetailActivity.class);
					intent.putExtra(AirportDetailActivity.ID, fid);
					break;
				case HOTEL:
					intent = new Intent(NearbyActivity.this,
							HotelDetailActivity.class);
					intent.putExtra(HotelDetailActivity.HOTEL_ID, fid);
					String[] as = TuanUtils.getTommory();
					intent.putExtra(HotelDetailActivity.COME_DATE, as[0]);
					intent.putExtra(HotelDetailActivity.OUT_DATE, as[1]);
					break;
				case VIEWPOINT:
					intent = new Intent(NearbyActivity.this,
							ViewpointDetailActivity.class);
					intent.putExtra(ViewpointDetailActivity.VID, fid);

					break;

				default:
					break;
				}

				if (intent != null) {
					startActivity(intent);
				}
			}
		});
	}

	private void getAddressFromLocation() {
		new Thread() {
			public void run() {

				Location loc = lUtil.getTuanLocation(mLocation);
				if (loc == null || mLocation == null) {
					isFinish1 = true;
					return;
				}

				mLocation = loc;

				mHandler.post(new Runnable() {

					@Override
					public void run() {
						// Toast.makeText(NearbyActivity.this,((TuanLocation)mLocation).getStreet(),Toast.LENGTH_LONG).show();
						setLocationText();
						isFinish1 = true;
					}
				});

			}
		}.start();
	}

	private void getAddressAndNearbys() {
		new Thread() {
			public void run() {

				Location loc = lUtil.getTuanLocationWithGao(mLocation);
				if (loc == null || mLocation == null) {
					mHandler.post(new Runnable() {

						@Override
						public void run() {
							setEmptyView();
							mListAdapter = null;
							openGpsWifi();
							if (DEBUG)
								Toast.makeText(NearbyActivity.this,
										"getAddressAndNearbys-locNULLreturn",
										Toast.LENGTH_SHORT).show();
						}
					});

					isFinish1 = true;
					return;
				}

				mLocation = loc;
				mHandler.post(new Runnable() {

					@Override
					public void run() {

						if (isNearby) {
							getNearBys();
						}

					}
				});

				Location loc1 = lUtil.getTuanLocationWithGear(mLocation);
				if (loc1 != null) {
					mLocation = loc1;

				}
				mHandler.post(new Runnable() {

					@Override
					public void run() {
						setLocationText();

					}
				});

				isFinish1 = true;

			}
		}.start();
	}

	class CitiesTask extends AsyncTask<Void, Void, Cities> {
		private Exception mReason;

		private CitiesTask() {
		}

		protected Cities doInBackground(Void... paramArrayOfVoid) {
			if (DEBUG)
				Log.d("CitiesTask", "doInBackground()");
			TuanTrip localTuanTrip = (TuanTrip) NearbyActivity.this
					.getApplication();
			TuanTripApp localTuanTripApp = localTuanTrip.getTuanTripApp();
			Cities citys = null;
			try {
				citys = localTuanTripApp.getCities();

			} catch (Exception localException) {
				this.mReason = localException;
				citys = null;
			}
			return citys;
		}

		protected void onCancelled() {
			super.onCancelled();
			NearbyActivity.this.dismissProgressDialog();
		}

		protected void onPostExecute(Cities paramCities) {
			if (paramCities == null) {
				NotificationsUtil.ToastReasonForFailure(NearbyActivity.this,
						mReason);
			} else if (paramCities.mHttpResult.getStat() == TuanTripSettings.POSTSUCCESS) {
				mCities = paramCities.mCities;
				if (mCities != null && mCities.size() != 0) {
					showDialog(CITY_DIALOG);
				}
			} else if (paramCities.mHttpResult.getStat() == TuanTripSettings.POSTFAILED) {
				Toast.makeText(NearbyActivity.this,
						paramCities.mHttpResult.getMessage(),
						Toast.LENGTH_SHORT).show();
			} else {
				NotificationsUtil.ToastReasonForFailure(NearbyActivity.this,
						mReason);
			}
			NearbyActivity.this.dismissProgressDialog();
		}

		protected void onPreExecute() {
			if (DEBUG)
				Log.d("TodayActivity", "onPreExecute()");
			startProgressBar("获取城市列表", "加载中...");
		}
	}

	private void setLocationText() {
		if (mLocation instanceof TuanLocation) {
			String lString = ((TuanLocation) mLocation).getStreet().replaceAll(
					"中国", "")
					+ "附近";
			locationText.setText(lString);
		}
		if (mListAdapter != null) {
			mListAdapter.setLocation(mLocation);
			mListAdapter.notifyDataSetChanged();
		}
	}

	private void openGpsWifi() {
		TuanTrip tuanTrip = (TuanTrip) getApplication();
		if (!tuanTrip.getOpenGps())
			return;

		if (!isGpsEable)
			openGpsText += "(gps)<br/>可定为到街道级别<br/>";
		if (!isWifiEable)
			openGpsText += "(无线wifi)<br/>在室内gps信号不好的情况下,用wifi定位更准确";

		if (!isGpsEable || !isWifiEable) {
			showDialog(DIALOG_OPENGPS);
		}
	}

	@Override
	public void locationTaskPost() {
		mLocation = lUtil.getmLocation();

		if (mLocation instanceof TuanLocation)// getAddressFromLocation();
		{
			setLocationText();
			getNearBys();
		} else
			getAddressAndNearbys();

		if (!isFirstLoad) {
			dismissProgressDialog();
		}

	}

	@Override
	public void locationTaskPre() {
		if (isFirstLoad) {
			setLoadingView();
			mEmptyText.setText("定位中...");
		} else {
			startProgressBar("定位", "定位中...");
		}

	}

	@Override
	public void getNoLocation() {
		if (isFirstLoad) {
			setEmptyView();
		} else {
			setProgressBarIndeterminateVisibility(false);
		}
		Toast.makeText(NearbyActivity.this, "无法定位", Toast.LENGTH_SHORT).show();
		openGpsWifi();
		if (!isFirstLoad) {
			dismissProgressDialog();
		}

	}

	@Override
	public void updateLocation() {
		mHandler.post(new Runnable() {

			@Override
			public void run() {

				if (DEBUG)
					Log.d(TAG, "updateLocation()");

				if (!isFinish1 || mLocation == null || mListAdapter == null
						|| !isFinish)
					return;

				isFinish1 = false;
				mLocation = lUtil.getmLocation();

				if (lUtil.getIsGps()) {
					if (DEBUG)
						Toast.makeText(NearbyActivity.this, "位置isGps",
								Toast.LENGTH_SHORT).show();

					getAddressFromLocation();
				} else {
					if (DEBUG)
						Toast.makeText(NearbyActivity.this, "位置isnotGps",
								Toast.LENGTH_SHORT).show();

					lUtil.setIsGps(true);
					getAddressAndNearbys();
				}

			}
		});

	}

}
