package com.tudaidai.tuantrip;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Observable;
import java.util.Observer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tudaidai.tuantrip.app.TuanTripApp;
import com.tudaidai.tuantrip.types.Cities;
import com.tudaidai.tuantrip.types.City;
import com.tudaidai.tuantrip.types.Group;
import com.tudaidai.tuantrip.types.Product;
import com.tudaidai.tuantrip.types.ProductResult;
import com.tudaidai.tuantrip.types.Shop;
import com.tudaidai.tuantrip.types.UserResult;
import com.tudaidai.tuantrip.types.Version;
import com.tudaidai.tuantrip.util.NotificationsUtil;
import com.tudaidai.tuantrip.util.RemoteResourceManager;
import com.tudaidai.tuantrip.util.TuanUtils;

public class TodayActivity extends Activity {

	private static final boolean DEBUG = TuanTripSettings.DEBUG;
	private static final String TAG = "TodayActivity";
	private static final int MENU_ABOUT = 3;
	private static final int MENU_ACCOUNT = 0;
	private static final int MENU_PREFERENCE = 1;
	private static final int MENU_REFRESH = 2;
	private static final int DIALOG_OPTION = 1;
	static final int REQUEST_PRODUCTLIST_CODE = 1;
	public static final int RESULT_PRODUCTLIST_CODE = 1;
	private ProgressDialog mProgressDialog;
	private ProgressDialog mDownloadDialog;
	private Group<City> mCities;
	private Handler mHandler = new Handler();
	private TextView mDays;
	private TextView mHours;
	private TextView mMinutes;
	private String mNewCity;
	private Product mProduct;
	private TextView mSeconds;
	private Handler mTimerHandler = new Handler();
	private Runnable mTimerRunnable = new MyRunnable();
	private RemoteResourceManagerObserver mResourcesObserver;
	private RemoteResourceManager mRrm;
	private Button cityButton;
	private Button refreshButton;
	private ImageView introPhoto;
	private TextView weSayText;
	private TextView detailtext;
	public static final int BUY = 0;
	public static final int BUYHOTEL = 1;
	public static final int BUYXIANLU = 2;
	public int pic1 = 0;
	public int pic2 = 0;
	public volatile int isFirstPic = 0;
	private static final String TUANPATH = "/tuantrip/";
	private String APKFILE = "";
	private Version version;
	int fileSize;
	int downLoadFileSize;
	String fileEx, fileNa, filename;
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {// 定义一个Handler，用于处理下载线程与UI间通讯
			if (!Thread.currentThread().isInterrupted()) {
				switch (msg.what) {
				case 0:
					mDownloadDialog.setMax(fileSize);
				case 1:
					mDownloadDialog.setProgress(downLoadFileSize);
					int result = downLoadFileSize * 100 / fileSize;

					break;
				case 2:
					Toast.makeText(TodayActivity.this, "文件下载完成", 1).show();
					break;

				case -1:
					String error = msg.getData().getString("error");
					Toast.makeText(TodayActivity.this, error, 1).show();
					break;
				}
			}
			super.handleMessage(msg);
		}
	};

	private void setChosenCity(int i) {
		if (i != -1) {
			String city = ((City) mCities.get(i)).getName();
			String cncity = ((City) mCities.get(i)).getCnName();
			TuanTrip tuanTrip = (TuanTrip) this.getApplication();
			tuanTrip.setCity(city);
			tuanTrip.setCnCity(cncity);
			tuanTrip.setProductList(null);
			Void[] voids = new Void[0];
			new BootTask(this).execute(voids);
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.today_activity);
		setProgressBarIndeterminateVisibility(false);

		TuanTrip tuanTrip = (TuanTrip) this.getApplication();
		mCities = tuanTrip.getCitys();
		mRrm = tuanTrip.getRemoteResourceManager();
		mResourcesObserver = new RemoteResourceManagerObserver();
		mRrm.addObserver(mResourcesObserver);

		Intent data = getIntent();
		if (data.getExtras() != null && data.getExtras().containsKey("pid")) {

			int pid = data.getIntExtra("pid", tuanTrip.getProduct().getPid());
			if (pid != tuanTrip.getProduct().getPid()) {
				String[] as = new String[1];
				as[0] = new Integer(pid).toString();
				new BootPidTask(this).execute(as);
			} else {
				ensureUi();
			}

		} else {
			Void[] voids = new Void[0];
			new BootTask(this).execute(voids);
		}

		// Void[] voids = new Void[0];
		// new VersionTask().execute(voids);

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

	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, MENU_REFRESH, 0, "今日团购")
				.setIcon(R.drawable.ic_menu_refresh);
		super.onCreateOptionsMenu(menu);
		menu.add(0, MENU_ACCOUNT, 0, "我的账户")
				.setIcon(R.drawable.ic_menu_account);
		menu.add(0, MENU_PREFERENCE, 0, "系统设置").setIcon(
				R.drawable.ic_menu_preference);
		// menu.add(0,MENU_ABOUT,0,R.string.about_label).setIcon(R.drawable.ic_menu_about);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem menuitem) {
		boolean flag = super.onOptionsItemSelected(menuitem);
		Intent intent = null;
		switch (menuitem.getItemId()) {
		case MENU_ACCOUNT:
			intent = new Intent(this, AccountActivity.class);
			startActivity(intent);
			break;
		case MENU_PREFERENCE:
			intent = new Intent(this, SetActivity.class);
			startActivity(intent);
			break;
		case MENU_REFRESH:
			Void[] voids = new Void[0];
			new BootTask(this).execute(voids);
			break;
		case MENU_ABOUT:

			break;
		default:
			break;
		}

		return flag;
	}

	protected Dialog onCreateDialog(int i) {
		switch (i) {
		case DIALOG_OPTION:
			CityPickerDialogNoEdit citydialog = new CityPickerDialogNoEdit(
					this, mCities);
			citydialog.setOnCancelListener(new OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					CityPickerDialogNoEdit citypickerdialog = (CityPickerDialogNoEdit) dialog;
					int i = citypickerdialog.getChosenCity();
					TodayActivity.this.setChosenCity(i);
					removeDialog(DIALOG_OPTION);

				}
			});
			return citydialog;
		default:
			return null;
		}

	}

	private void ensureUi() {
		if (DEBUG)
			Log.i(TAG, "ensureUi()------------------");

		TuanTrip tuanTrip = (TuanTrip) this.getApplication();
		mProduct = tuanTrip.getProduct();
		mDays = (TextView) this.findViewById(R.id.remainDays);
		mHours = (TextView) this.findViewById(R.id.remainHours);
		mMinutes = (TextView) this.findViewById(R.id.remainMinutes);
		mSeconds = (TextView) this.findViewById(R.id.remainSeconds);
		TextView price = (TextView) this.findViewById(R.id.priceView);
		price.setText("仅售￥" + new Double(mProduct.getPrice()).toString());
		ImageView buyImageView = (ImageView) this.findViewById(R.id.buy_click);
		if (mProduct.getState().equals(Product.NONE)) {
			buyImageView.setImageDrawable(getResources().getDrawable(
					R.drawable.button_buy));
			buyImageView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = null;
					// intent = new
					// Intent(TodayActivity.this,BuyHotelActivity.class);

					switch (mProduct.getTeamType()) {
					case BUY:
						intent = new Intent(TodayActivity.this,
								BuyActivity.class);
						break;
					case BUYHOTEL:
						intent = new Intent(TodayActivity.this,
								BuyHotelActivity.class);
						break;
					case BUYXIANLU:
						intent = new Intent(TodayActivity.this,
								BuyXianluActivity.class);
						break;
					default:
						break;
					}
					startActivity(intent);

				}
			});
		} else if (mProduct.getState().equals(Product.SOLDOUT)) {
			buyImageView.setImageDrawable(getResources().getDrawable(
					R.drawable.sellout));
		} else if (mProduct.getState().equals(Product.SUCCESS)
				|| mProduct.getState().equals(Product.FAILURE)
				|| mProduct.getState().equals(Product.END)) {
			buyImageView.setImageDrawable(getResources().getDrawable(
					R.drawable.sellend));
		}
		TextView priceText = (TextView) this.findViewById(R.id.priceText);
		priceText.setText("原价"
				+ new Double(mProduct.getPriceOriginal()).toString()
				+ "元 折扣"
				+ new Double(mProduct.getReBate()).toString()
				+ " 节省"
				+ new Double(mProduct.getPriceOriginal() - mProduct.getPrice())
						.toString() + "元");
		TextView introText = (TextView) this.findViewById(R.id.introText);
		introText.setText(mProduct.getTitle());
		introPhoto = (ImageView) this.findViewById(R.id.introPhoto);
		String introImg = mProduct.getImageUrl();
		if (introImg != null && !TextUtils.isEmpty(introImg)) {
			Uri uri = Uri.parse(introImg);
			if (!mRrm.exists(uri) && isFirstPic == 0
					&& TuanUtils.isPic(introImg)) {
				if (DEBUG)
					Log.d(TAG, "!mRrm.exists(uri)" + uri);
				introPhoto.setImageDrawable(getResources().getDrawable(
						R.drawable.nopicbig));
				mRrm.request(uri);
				pic1++;
			} else {
				if (DEBUG)
					Log.d(TAG, "mRrm exists " + uri);
				try {

					Bitmap bitmap = mRrm.getBitmap(uri);
					if (bitmap != null)
						introPhoto.setImageBitmap(bitmap);
					else
						introPhoto.setImageDrawable(getResources().getDrawable(
								R.drawable.nopicbig));

				} catch (Exception exception) {
				}
			}
		}

		TextView buyNum = (TextView) this.findViewById(R.id.buyNum);
		buyNum.setText(mProduct.getBought() + "人已下单");
		detailtext = (TextView) this.findViewById(R.id.detailText);
		detailtext.setText(TuanUtils.showHtml(
				tuanTrip.getShowPic() ? mProduct.getDescription() : mProduct
						.getDescription().replaceAll("<img[^>]*?>", ""), mRrm,
				this));
		detailtext.setMovementMethod(LinkMovementMethod.getInstance());
		TextView noticeText = (TextView) this.findViewById(R.id.noticeText);
		noticeText.setText(TuanUtils.showHtml(
				tuanTrip.getShowPic() ? mProduct.getTip() : mProduct.getTip()
						.replaceAll("<img[^>]*?>", ""), mRrm, this));
		TextView shopText = (TextView) this.findViewById(R.id.shopText);
		StringBuilder shops = new StringBuilder();
		for (Shop shop : mProduct.getShops()) {
			shops.append(shop.getSName() + "<br/>");
			shops.append(shop.getSel() + "<br/>");
			shops.append(shop.getSddr() + "<br/>");
		}
		shopText.setText(TuanUtils.showHtml(shops.toString(), mRrm, this));
		TextView customSayText = (TextView) this.findViewById(R.id.tipText);
		customSayText.setText(TuanUtils.showHtml(
				tuanTrip.getShowPic() ? mProduct.getCustomSay() : mProduct
						.getCustomSay().replaceAll("<img[^>]*?>", ""), mRrm,
				this));
		customSayText.setMovementMethod(LinkMovementMethod.getInstance());
		weSayText = (TextView) this.findViewById(R.id.tuanText);
		weSayText.setText(TuanUtils.showHtml(
				tuanTrip.getShowPic() ? mProduct.getWeSay() : mProduct
						.getWeSay().replaceAll("<img[^>]*?>", ""), mRrm, this));
		isFirstPic = 1;

		cityButton = (Button) findViewById(R.id.cityButton);
		cityButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mCities != null) {
					showDialog(DIALOG_OPTION);
				} else {
					Void[] as = new Void[0];
					new CitiesTask().execute(as);
				}

			}
		});
		refreshButton = (Button) findViewById(R.id.refreshButton);
		refreshButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// if(pic2>=pic1)
				TodayActivity.this.mRrm.deleteObserver(mResourcesObserver);

				Intent intent = new Intent(TodayActivity.this,
						ProductListActivity.class);
				startActivityForResult(intent, REQUEST_PRODUCTLIST_CODE);

			}
		});
	}

	public void onActivityResult(int requestcode, int resultCode, Intent data) {
		if (DEBUG)
			Log.e(TAG, "onActivityResult~~~");
		TodayActivity.this.mRrm.addObserver(mResourcesObserver);
		TuanTrip tuanTrip = (TuanTrip) getApplication();
		if (resultCode == RESULT_PRODUCTLIST_CODE && data != null
				&& data.getExtras() != null
				&& data.getExtras().containsKey("pid")) {

			int pid = data.getIntExtra("pid", tuanTrip.getProduct().getPid());

			if (pid != tuanTrip.getProduct().getPid()) {
				String[] as = new String[1];
				as[0] = new Integer(pid).toString();
				new BootPidTask(this).execute(as);
			}

		}
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
	protected void onResume() {
		super.onResume();
		if (DEBUG)
			Log.e(TAG, "start onResume~~~");
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (DEBUG)
			Log.e(TAG, "start onPause~~~");
	}

	@Override
	protected void onStop() {
		super.onStop();
		if (DEBUG)
			Log.e(TAG, "start onStop~~~");
	}

	public void onDestroy() {
		if (DEBUG)
			Log.d(TAG, "onDestroy()");
		mTimerHandler.removeCallbacks(mTimerRunnable);
		super.onDestroy();
	}

	private void updateTime() {
		int i = mProduct.getTimeLeft();
		if (i > 0) {
			int j = i - 1;
			mProduct.setTimeLeft(j);
			String days = String.valueOf(j / (3600 * 24));
			mDays.setText(days);
			int j1 = j % (3600 * 24);
			String s = String.valueOf(j1 / 3600);
			mHours.setText(s);
			int k = j1 % 3600;
			String s1 = String.valueOf(k / 60);
			mMinutes.setText(s1);
			String s2 = String.valueOf(k % 60);
			mSeconds.setText(s2);
		} else {
			mDays.setText("0");
			mHours.setText("0");
			mMinutes.setText("0");
			mSeconds.setText("0");
		}
	}

	private void startProgressBar(String title, String content) {
		mProgressDialog = ProgressDialog.show(this, title, content);
		mProgressDialog.show();
	}

	private void dismissProgressDialog() {
		this.mProgressDialog.dismiss();
	}

	class CitiesTask extends AsyncTask<Void, Void, Cities> {
		private Exception mReason;

		private CitiesTask() {
		}

		protected Cities doInBackground(Void... paramArrayOfVoid) {
			if (DEBUG)
				Log.d("CitiesTask", "doInBackground()");
			TuanTrip localTuanTrip = (TuanTrip) TodayActivity.this
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
			TodayActivity.this.dismissProgressDialog();
		}

		protected void onPostExecute(Cities paramCities) {
			if (paramCities == null) {
				NotificationsUtil.ToastReasonForFailure(TodayActivity.this,
						mReason);
			} else if (paramCities.mHttpResult.getStat() == TuanTripSettings.POSTSUCCESS) {
				TuanTrip localTuanTrip = (TuanTrip) TodayActivity.this
						.getApplication();
				localTuanTrip.setCities(paramCities.mCities);
				mCities = paramCities.mCities;
				showDialog(DIALOG_OPTION);
			} else if (paramCities.mHttpResult.getStat() == TuanTripSettings.POSTFAILED) {
				Toast.makeText(TodayActivity.this,
						paramCities.mHttpResult.getMessage(),
						Toast.LENGTH_SHORT).show();
			} else {
				NotificationsUtil.ToastReasonForFailure(TodayActivity.this,
						mReason);
			}
			TodayActivity.this.dismissProgressDialog();
		}

		protected void onPreExecute() {
			if (DEBUG)
				Log.d("TodayActivity", "onPreExecute()");
			startProgressBar("获取城市列表", "加载中...");
		}
	}

	class BootTask extends AsyncTask<Void, Void, ProductResult> {
		private static final boolean DEBUG = TuanTripSettings.DEBUG;
		private static final String TAG = "BootTask";
		private TodayActivity todayActivity;
		private Exception mReason;

		private BootTask(TodayActivity todayActivity) {
			this.todayActivity = todayActivity;
		}

		protected ProductResult doInBackground(Void... params) {
			if (DEBUG)
				Log.d("BootTask", "doInBackground()");
			TuanTrip localTuanTrip = (TuanTrip) todayActivity.getApplication();
			ProductResult localProductResult = null;
			try {
				TuanTripApp localTuanTripApp = localTuanTrip.getTuanTripApp();
				String city = localTuanTrip.getStoreCity();
				localProductResult = localTuanTripApp.getCurProduct(city, 0);

			} catch (Exception localException) {
				if (DEBUG)
					Log.d("BootTask", "Caught Exception while boot.",
							localException);
				this.mReason = localException;
				localProductResult = null;
			}

			return localProductResult;
		}

		protected void onCancelled() {
			super.onCancelled();
			todayActivity.dismissProgressDialog();
		}

		protected void onPostExecute(ProductResult paramProductResult) {
			pic1 = pic2 = isFirstPic = 0;
			todayActivity.dismissProgressDialog();
			TuanTrip tuanTrip = (TuanTrip) todayActivity.getApplication();
			if (paramProductResult == null) {
				NotificationsUtil.ToastReasonForFailure(TodayActivity.this,
						mReason);
			} else if (paramProductResult.mHttpResult.getStat() == TuanTripSettings.POSTSUCCESS) {
				tuanTrip.setProduct(paramProductResult.mProduct);
				ensureUi();
				cityButton.setText(tuanTrip.getStoreCnCity() + "(切换)");
				mTimerHandler.removeCallbacks(mTimerRunnable);
				mTimerHandler.postDelayed(mTimerRunnable, 1000L);

			} else if (paramProductResult.mHttpResult.getStat() == TuanTripSettings.POSTFAILED) {
				Toast.makeText(TodayActivity.this,
						paramProductResult.mHttpResult.getMessage(),
						Toast.LENGTH_SHORT).show();
			} else {
				NotificationsUtil.ToastReasonForFailure(TodayActivity.this,
						mReason);
			}

			// autoLogin();
		}

		protected void onPreExecute() {
			if (DEBUG)
				Log.d("BootTask", "onPreExecute()");
			todayActivity.startProgressBar("获取今日团购", "加载中...");
		}
	}

	class BootPidTask extends AsyncTask<String, Void, ProductResult> {
		private static final boolean DEBUG = TuanTripSettings.DEBUG;
		private static final String TAG = "BootPidTask";
		private TodayActivity todayActivity;
		private Exception mReason;

		private BootPidTask(TodayActivity todayActivity) {
			this.todayActivity = todayActivity;
		}

		protected ProductResult doInBackground(String... params) {
			if (DEBUG)
				Log.d(TAG, "doInBackground()");
			TuanTrip localTuanTrip = (TuanTrip) todayActivity.getApplication();
			ProductResult localProductResult = null;
			try {
				TuanTripApp localTuanTripApp = localTuanTrip.getTuanTripApp();
				localProductResult = localTuanTripApp.getCurProduct(params[0],
						1);

			} catch (Exception localException) {
				if (DEBUG)
					Log.d(TAG, "Caught Exception while boot.", localException);
				this.mReason = localException;
				localProductResult = null;
			}

			return localProductResult;
		}

		protected void onCancelled() {
			super.onCancelled();
			todayActivity.dismissProgressDialog();
		}

		protected void onPostExecute(ProductResult paramProductResult) {
			pic1 = pic2 = isFirstPic = 0;
			todayActivity.dismissProgressDialog();
			TuanTrip tuanTrip = (TuanTrip) todayActivity.getApplication();
			if (paramProductResult == null) {
				NotificationsUtil.ToastReasonForFailure(TodayActivity.this,
						mReason);
			} else if (paramProductResult.mHttpResult.getStat() == TuanTripSettings.POSTSUCCESS) {
				tuanTrip.setProduct(paramProductResult.mProduct);
				ensureUi();
				mTimerHandler.removeCallbacks(mTimerRunnable);
				mTimerHandler.postDelayed(mTimerRunnable, 1000L);

			} else if (paramProductResult.mHttpResult.getStat() == TuanTripSettings.POSTFAILED) {
				Toast.makeText(TodayActivity.this,
						paramProductResult.mHttpResult.getMessage(),
						Toast.LENGTH_SHORT).show();
			} else {
				NotificationsUtil.ToastReasonForFailure(TodayActivity.this,
						mReason);
			}

			// autoLogin();

		}

		protected void onPreExecute() {
			if (DEBUG)
				Log.d("BootTask", "onPreExecute()");
			todayActivity.startProgressBar("获取今日团购", "加载中...");
		}
	}

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
				TuanTripApp localTuanTripApp = ((TuanTrip) TodayActivity.this
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
				NotificationsUtil.ToastReasonForFailure(TodayActivity.this,
						mReason);
			} else if (paramUserResult.mHttpResult.getStat() == TuanTripSettings.POSTSUCCESS) {
				Toast.makeText(TodayActivity.this, "自动登录成功", Toast.LENGTH_SHORT)
						.show();
				TuanTrip tuantrip = (TuanTrip) getApplication();
				String pwd = tuantrip.getPwd();
				paramUserResult.mUser.setPwd(pwd);
				tuantrip.setUser(paramUserResult.mUser);
			} else if (paramUserResult.mHttpResult.getStat() == TuanTripSettings.POSTFAILED) {
				Toast.makeText(TodayActivity.this,
						paramUserResult.mHttpResult.getMessage(),
						Toast.LENGTH_SHORT).show();
			} else {
				NotificationsUtil.ToastReasonForFailure(TodayActivity.this,
						mReason);
			}
		}

		protected void onPreExecute() {
			if (DEBUG)
				Log.d("LoginTask", "onPreExecute()");
			// LoginActivity.this.startProgressBar();
		}
	}

	class VersionTask extends AsyncTask<Void, Void, Version> {
		private Exception mReason;

		private VersionTask() {
		}

		protected Version doInBackground(Void... paramArrayOfVoid) {
			if (DEBUG)
				Log.d("VersionTask", "doInBackground()");
			TuanTrip localTuanTrip = (TuanTrip) TodayActivity.this
					.getApplication();
			TuanTripApp localTuanTripApp = localTuanTrip.getTuanTripApp();
			Version version = null;
			try {
				version = localTuanTripApp.getVersion();

			} catch (Exception localException) {
				this.mReason = localException;
				version = null;
			}
			return version;
		}

		protected void onCancelled() {
			super.onCancelled();
			// TodayActivity.this.dismissProgressDialog();
		}

		protected void onPostExecute(Version version1) {
			setProgressBarIndeterminateVisibility(false);
			if (version1 == null) {
				NotificationsUtil.ToastReasonForFailure(TodayActivity.this,
						mReason);
			} else {
				version = version1;
				TuanTrip localTuanTrip = (TuanTrip) TodayActivity.this
						.getApplication();
				if (localTuanTrip.getVersion() == version.getCode()) {
					Void[] voids = new Void[0];
					new BootTask(TodayActivity.this).execute(voids);
					TuanTrip tuanTrip = (TuanTrip) TodayActivity.this
							.getApplication();
					mCities = tuanTrip.getCitys();
					mRrm = tuanTrip.getRemoteResourceManager();
					mResourcesObserver = new RemoteResourceManagerObserver();
					mRrm.addObserver(mResourcesObserver);
				} else {
					Dialog dialog = new AlertDialog.Builder(TodayActivity.this)
							.setTitle("温馨提示")
							.setMessage("发现软件新版本,马上更新吗？")
							.setPositiveButton("确定",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											mDownloadDialog = new ProgressDialog(
													TodayActivity.this);
											mDownloadDialog.setTitle("下载最新版本");
											mDownloadDialog
													.setMessage("正在下载,请稍候...");
											mDownloadDialog
													.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

											new Thread() {

												public void run() {
													try {
														String sDStateString = android.os.Environment
																.getExternalStorageState();
														if (sDStateString
																.equals(android.os.Environment.MEDIA_MOUNTED)) {
															File SDFile = android.os.Environment
																	.getExternalStorageDirectory();
															String sdcard = SDFile
																	.getAbsolutePath();
															File apk = new File(
																	sdcard
																			+ TUANPATH);
															apk.mkdir();
															APKFILE = sdcard
																	+ TUANPATH
																	+ version
																			.getName();
															apk = new File(
																	APKFILE);
															if (!apk.exists()) {
																apk.createNewFile();
															}
															downFile(version
																	.getUrl());

														}

													} catch (Exception e) {
														mDownloadDialog
																.cancel();
														NotificationsUtil
																.ToastReasonForFailure(
																		TodayActivity.this,
																		e);
													}
													mDownloadDialog.cancel();
												}
											}.start();

											mDownloadDialog.show();

										}

									})
							.setNegativeButton("取消",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int whichButton) {
											TodayActivity.this.finish();

										}
									}).create();
					dialog.show();
				}
			}
		}

		protected void onPreExecute() {
			if (DEBUG)
				Log.d("TodayActivity", "onPreExecute()");
			setProgressBarIndeterminateVisibility(true);
		}
	}

	private class MyRunnable implements Runnable {

		public void run() {
			updateTime();
			Handler handler = mTimerHandler;
			Runnable runnable = mTimerRunnable;
			boolean flag = handler.postDelayed(runnable, 1000L);
		}

	}

	private void downFile(String url) throws IOException {
		// 下载函数
		filename = version.getName();
		// 获取文件名
		URL myURL = new URL(url);
		URLConnection conn = myURL.openConnection();
		conn.connect();
		InputStream is = conn.getInputStream();
		this.fileSize = conn.getContentLength();// 根据响应获取文件大小
		if (this.fileSize <= 0)
			throw new RuntimeException("无法获知文件大小 ");
		if (is == null)
			throw new RuntimeException("stream is null");
		FileOutputStream fos = new FileOutputStream(APKFILE);
		// 把数据存入路径+文件名
		byte buf[] = new byte[1024];
		downLoadFileSize = 0;
		sendMsg(0);
		do {
			// 循环读取
			int numread = is.read(buf);
			if (numread == -1) {
				break;
			}
			fos.write(buf, 0, numread);
			downLoadFileSize += numread;

			sendMsg(1);// 更新进度条
		} while (true);
		sendMsg(2);// 通知下载完成
		install();
		try {
			is.close();
		} catch (Exception ex) {
			Log.e("tag", "error: " + ex.getMessage(), ex);
		}

	}

	private void sendMsg(int flag) {
		Message msg = new Message();
		msg.what = flag;
		handler.sendMessage(msg);
	}

	private void install() {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(new File(APKFILE)),
				"application/vnd.android.package-archive");
		startActivity(intent);
	}

	private synchronized void ensurePic() {
		pic2++;
		if (DEBUG)
			Log.d(TAG, "pic1 is " + pic1 + " pic2 is " + pic2);
		if (pic2 < pic1)
			return;
		if (pic2 > pic1) {
			pic2 = pic1;
			return;
		}

		TuanTrip tuanTrip = (TuanTrip) this.getApplication();
		if (introPhoto == null) {
			introPhoto = (ImageView) this.findViewById(R.id.introPhoto);
		}
		String introImg = mProduct.getImageUrl();
		if (introImg != null && !TextUtils.isEmpty(introImg)) {
			Uri uri = Uri.parse(introImg);
			if (!mRrm.exists(uri) && isFirstPic == 0
					&& TuanUtils.isPic(introImg)) {
				if (DEBUG)
					Log.d(TAG, "!mRrm.exists(uri)" + uri);
				introPhoto.setImageDrawable(getResources().getDrawable(
						R.drawable.nopicbig));
				mRrm.request(uri);
				pic1++;
			} else {
				if (DEBUG)
					Log.d(TAG, "mRrm exists " + uri);
				try {

					Bitmap bitmap = mRrm.getBitmap(uri);
					if (bitmap != null)
						introPhoto.setImageBitmap(bitmap);
					else
						introPhoto.setImageDrawable(getResources().getDrawable(
								R.drawable.nopicbig));
				} catch (Exception exception) {
				}
			}
		}

		//
		if (weSayText == null) {
			weSayText = (TextView) this.findViewById(R.id.tuanText);
		}
		weSayText.setText(TuanUtils.showHtml(
				tuanTrip.getShowPic() ? mProduct.getWeSay() : mProduct
						.getWeSay().replaceAll("<img[^>]*?>", ""), mRrm, this));
		//
		if (detailtext == null) {
			detailtext = (TextView) this.findViewById(R.id.detailText);
		}
		detailtext.setText(TuanUtils.showHtml(
				tuanTrip.getShowPic() ? mProduct.getDescription() : mProduct
						.getDescription().replaceAll("<img[^>]*?>", ""), mRrm,
				this));

	}

	class RemoteResourceManagerObserver implements Observer {
		private static final boolean DEBUG = TuanTripSettings.DEBUG;
		private static final String TAG = "RemoteResourceManagerObserver";

		public void update(Observable paramObservable, Object paramObject) {

			String str = "Fetcher got: " + paramObject;
			if (DEBUG)
				Log.d(TAG, "update()" + str);
			if (paramObject == null)
				return;
			mHandler.post(new Runnable() {

				@Override
				public void run() {
					try {
						ensurePic();
					} catch (Exception e) {
						// TODO: handle exception
					}

				}
			});
		}
	}
}