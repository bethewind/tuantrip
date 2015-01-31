package com.tudaidai.tuantrip;

import java.util.Observable;
import java.util.Observer;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.tudaidai.tuantrip.app.TuanTripApp;
import com.tudaidai.tuantrip.types.AirportDetailResult;
import com.tudaidai.tuantrip.types.ForecastWeather;
import com.tudaidai.tuantrip.types.WeatherResult;
import com.tudaidai.tuantrip.util.NotificationsUtil;
import com.tudaidai.tuantrip.util.RemoteResourceManager;
import com.tudaidai.tuantrip.util.TuanUtils;

public class AirportDetailActivity extends BaseLbsActivity {

	public static final String ID = "com.tudaidai.tuantrip.AirportDetailActivity.aid";
	static final boolean DEBUG = TuanTripSettings.DEBUG;
	static final String TAG = "AirportDetailActivity";
	private static final int MENU_REFRESH = 0;
	AirportDetailResult vDetailResult;
	double price = 0.0;
	int id;
	TextView tianqiText;
	RemoteResourceManager mRrm1;
	private RemoteResourceManagerObserver1 mResourcesObserve1;
	Handler handler = new Handler();
	WeatherResult weatherResult;
	View serviceView;
	View jiaotongView;
	View tianqiView;

	private void setWeather() {
		if (weatherResult == null) {
			return;
		}

		if (tianqiView != null) {
			try {
				StringBuffer sBuffer = new StringBuffer(
						"<div class=\"homeNavigation\"><img src=\""
								+ weatherResult.currentWeather.getIcon()
								+ "\" style=\"width:25px;height:25px\" align=\"absmiddle\"> "
								+ weatherResult.currentWeather.getCondition()
								+ "<br/>温度："
								+ weatherResult.currentWeather.getTemp_c()
								+ "<br/>"
								+ weatherResult.currentWeather.getHumidity()
								+ "<br/>"
								+ weatherResult.currentWeather
										.getWind_condition()
								+ "   <br/>  <br/>");
				sBuffer.append("<strong> 未来" + weatherResult.weathers.size()
						+ "天天气摘要</strong><br/><br/>");
				for (ForecastWeather fWeather : weatherResult.weathers) {
					sBuffer.append("" + fWeather.getDay_of_week()
							+ "&nbsp;&nbsp;最低温" + fWeather.getLow()
							+ " &nbsp;&nbsp;最高温" + fWeather.getHigh()
							+ " &nbsp;&nbsp;&nbsp;&nbsp;"
							+ fWeather.getCondition() + "<br/>");
				}
				sBuffer.append("</div>");
				tianqiText.setText(TuanUtils.showHtml1(sBuffer.toString(),
						mRrm1));
			} catch (Exception e) {
				tianqiText.setText("暂无相关天气信息");
			}
		}
	}

	private void ensureUi(AirportDetailResult paramOrders) {
		vDetailResult = paramOrders;
		super.ensure(vDetailResult.aDetail);

		Button jiaotongTab = (Button) findViewById(R.id.jiaotongTab);
		jiaotongTab.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {

					content.removeAllViews();
					if (jiaotongView == null) {
						jiaotongView = mInflater.inflate(R.layout.lbs_common,
								null);

						detailText = (TextView) jiaotongView
								.findViewById(R.id.detailText);

						detailText.setText(Html.fromHtml(vDetailResult.aDetail
								.getJiaotong()));
						detailText.setMovementMethod(LinkMovementMethod
								.getInstance());
						content.addView(jiaotongView);
					} else {
						content.addView(jiaotongView);
					}

				}

			}
		});
		jiaotongTab.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
			}
		});
		Button serviceTab = (Button) findViewById(R.id.serviceTab);
		serviceTab.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {

					content.removeAllViews();
					if (serviceView == null) {
						serviceView = mInflater.inflate(R.layout.lbs_common,
								null);

						detailText = (TextView) serviceView
								.findViewById(R.id.detailText);

						detailText.setText(Html.fromHtml(vDetailResult.aDetail
								.getService()));
						detailText.setMovementMethod(LinkMovementMethod
								.getInstance());
						content.addView(serviceView);
					} else {
						content.addView(serviceView);
					}

				}

			}
		});
		serviceTab.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
			}
		});
		Button tianqiTab = (Button) findViewById(R.id.tianqiTab);
		tianqiTab.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {

					content.removeAllViews();
					if (tianqiView == null) {
						tianqiView = mInflater.inflate(R.layout.lbs_common,
								null);

						tianqiText = (TextView) tianqiView
								.findViewById(R.id.detailText);
						tianqiText.setText("获取天气信息中...");
						content.addView(tianqiView);
					} else {
						content.addView(tianqiView);
					}

					setWeather();

				}

			}
		});
		tianqiTab.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
			}
		});
	}

	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.airport_detail_activity);

		id = getIntent().getIntExtra(ID, -1);

		String[] as = new String[1];
		as[0] = new Integer(id).toString();
		new AirportDetailTask().execute(as);

		mRrm1 = ((TuanTrip) getApplication()).getFileRemoteResourceManager();
		mResourcesObserve1 = new RemoteResourceManagerObserver1();
	}

	protected void onResume() {
		mRrm1.addObserver(mResourcesObserve1);
		super.onResume();
	}

	protected void onPause() {
		mRrm1.deleteObserver(mResourcesObserve1);
		super.onPause();
	}

	class AirportDetailTask extends
			AsyncTask<String, Void, AirportDetailResult> {
		private static final String TAG = "AirportDetailTask";
		private Exception mReason;

		public AirportDetailTask() {
		}

		protected AirportDetailResult doInBackground(
				String... paramArrayOfString) {
			if (DEBUG)
				Log.d(TAG, "doInBackground()");
			AirportDetailResult vDetailResult = null;
			try {
				TuanTripApp localTuanTripApp = ((TuanTrip) AirportDetailActivity.this
						.getApplication()).getTuanTripApp();
				vDetailResult = localTuanTripApp
						.getAirportDetailResult(paramArrayOfString);

				if (DEBUG)
					Log.d(TAG, "doInBackground() over");

			} catch (Exception localException) {
				this.mReason = localException;
				vDetailResult = null;
			}

			return vDetailResult;
		}

		protected void onPostExecute(AirportDetailResult paramOrders) {
			if (DEBUG)
				Log.d(TAG, "onPostExecute()");
			if (paramOrders != null
					&& paramOrders.mHttpResult.getStat() == TuanTripSettings.POSTSUCCESS) {

				ensureUi(paramOrders);

				String[] as = new String[2];
				as[0] = new Long(
						(long) (paramOrders.aDetail.getJingdu() * 1000000))
						.toString();
				as[1] = new Long(
						(long) (paramOrders.aDetail.getWeidu() * 1000000))
						.toString();
				new WeatherTask().execute(as);
			} else {
				if (paramOrders == null) {
					NotificationsUtil.ToastReasonForFailure(
							AirportDetailActivity.this, mReason);
				} else if (paramOrders.mHttpResult.getStat() == TuanTripSettings.POSTFAILED) {
					Toast.makeText(AirportDetailActivity.this,
							paramOrders.mHttpResult.getMessage(),
							Toast.LENGTH_SHORT).show();
				} else {
					NotificationsUtil.ToastReasonForFailure(
							AirportDetailActivity.this, mReason);
				}
			}

			dismissProgressDialog();
		}

		protected void onCancelled() {
			if (DEBUG)
				Log.d(TAG, "onCancelled()");
			super.onCancelled();
		}

		protected void onPreExecute() {
			if (DEBUG)
				Log.d(TAG, "onPreExecute()");
			startProgressBar("加载信息", "加载中...");
		}
	}

	class WeatherTask extends AsyncTask<String, Void, WeatherResult> {
		private static final String TAG = "WeatherTask";
		private Exception mReason;

		public WeatherTask() {
		}

		protected WeatherResult doInBackground(String... paramArrayOfString) {
			if (DEBUG)
				Log.d(TAG, "doInBackground()");
			WeatherResult vDetailResult = null;
			try {
				TuanTripApp localTuanTripApp = ((TuanTrip) AirportDetailActivity.this
						.getApplication()).getTuanTripApp();
				vDetailResult = localTuanTripApp
						.getWeatherResult(paramArrayOfString);

				if (DEBUG)
					Log.d(TAG, "doInBackground() over");

			} catch (Exception localException) {
				this.mReason = localException;
				vDetailResult = null;
			}

			return vDetailResult;
		}

		protected void onPostExecute(WeatherResult paramOrders) {
			if (DEBUG)
				Log.d(TAG, "onPostExecute()");
			if (paramOrders != null
					&& paramOrders.mHttpResult.getStat() == TuanTripSettings.POSTSUCCESS) {

				weatherResult = paramOrders;
				setWeather();
			} else {
				if (paramOrders == null) {
					NotificationsUtil.ToastReasonForFailure(
							AirportDetailActivity.this, mReason);
				} else if (paramOrders.mHttpResult.getStat() == TuanTripSettings.POSTFAILED) {
					Toast.makeText(AirportDetailActivity.this,
							paramOrders.mHttpResult.getMessage(),
							Toast.LENGTH_SHORT).show();
				} else {
					NotificationsUtil.ToastReasonForFailure(
							AirportDetailActivity.this, mReason);
				}
			}

			// dismissProgressDialog();
		}

		protected void onCancelled() {
			if (DEBUG)
				Log.d(TAG, "onCancelled()");
			super.onCancelled();
		}

		protected void onPreExecute() {
			if (DEBUG)
				Log.d(TAG, "onPreExecute()");
			// startProgressBar("加载信息", "加载中...");
		}
	}

	class RemoteResourceManagerObserver1 implements Observer {
		private RemoteResourceManagerObserver1() {
		}

		public void update(Observable paramObservable, Object paramObject) {
			if (paramObject == null)
				return;
			handler.post(new Runnable() {

				@Override
				public void run() {
					setWeather();
				}
			});
		}
	}

}
