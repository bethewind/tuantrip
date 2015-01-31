package com.tudaidai.tuantrip;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tudaidai.tuantrip.app.TuanTripApp;
import com.tudaidai.tuantrip.sqllite.HotelInsertUtil;
import com.tudaidai.tuantrip.types.HotelDetailResult;
import com.tudaidai.tuantrip.types.HotelNight;
import com.tudaidai.tuantrip.types.HotelShort;
import com.tudaidai.tuantrip.util.NotificationsUtil;
import com.tudaidai.tuantrip.widget.DateSetLisner;

public class HotelDetailActivity extends BaseLbsActivity {

	public static final String HOTEL_ID = "hotel_id";
	public static final String COME_DATE = "come_date";
	public static final String OUT_DATE = "out_date";
	static final boolean DEBUG = TuanTripSettings.DEBUG;
	static final String TAG = "HotelDetailActivity";
	private static final int MENU_REFRESH = 0;
	HotelDetailResult hotelDetailResult;
	double price = 0.0;
	String comDate = "";
	String outDate = "";
	static final int COM_DATE = 1;
	static final int OU_DATE = 2;
	EditText comText;
	EditText outText;
	private int mYear;
	private int mMonth;
	private int mDay;
	int hid;

	String[] images;
	View fangxingView;
	Button fangxingText;

	private void ensureUi(HotelDetailResult paramOrders) {
		hotelDetailResult = paramOrders;

		if (hotelDetailResult.hotelDetail.getImages() != null) {
			images = new String[hotelDetailResult.hotelDetail.getImages()
					.size()];
			for (int i = 0; i < hotelDetailResult.hotelDetail.getImages()
					.size(); i++) {
				images[i] = hotelDetailResult.hotelDetail.getImages().get(i);
			}
		}

		Button hotelImgButton = (Button) findViewById(R.id.hotelImgButtion);
		if (images == null) {
			hotelImgButton.setVisibility(View.GONE);
			hotelImgButton.setText("暂无图片");
		}

		hotelImgButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (images != null) {
					Intent intent = new Intent(HotelDetailActivity.this,
							HotelImageActivity1.class);
					intent.putExtra(HotelImageActivity1.IMAGES, images);
					startActivity(intent);
				}

			}
		});

		super.ensure(paramOrders.hotelDetail);
		fangxingText = (Button) findViewById(R.id.fangxingText);
		fangxingText.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {

					content.removeAllViews();
					if (hotelDetailResult.hotelDetail.getHotelNights() != null) {

						if (fangxingView == null) {
							fangxingView = mInflater.inflate(
									R.layout.hotel_fangxing, null);
							comText = (EditText) fangxingView
									.findViewById(R.id.comText);
							comText.setFocusable(false);
							comText.setText(comDate);
							comText.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									showDialog(COM_DATE);

								}
							});
							outText = (EditText) fangxingView
									.findViewById(R.id.outText);
							outText.setFocusable(false);
							outText.setText(outDate);
							outText.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									showDialog(OU_DATE);

								}
							});

							final Calendar calendar = Calendar.getInstance();
							mYear = calendar.get(Calendar.YEAR);
							mMonth = calendar.get(Calendar.MONTH);
							mDay = calendar.get(Calendar.DAY_OF_MONTH);
							Button btnUpdateDate = (Button) fangxingView
									.findViewById(R.id.btnUpdateDate);
							btnUpdateDate
									.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View v) {
											String[] as = new String[3];
											as[0] = new Integer(hid).toString();
											as[1] = comText.getText()
													.toString();
											as[2] = outText.getText()
													.toString();
											fangxingView = null;
											new HotelDetailTask().execute(as);

										}
									});
							TableLayout tableLayout = (TableLayout) fangxingView
									.findViewById(R.id.baojiaTable);
							tableLayout.removeAllViews();

							View viewhead = mInflater.inflate(
									R.layout.hotel_nights_table_head, null);
							tableLayout.addView(viewhead);

							for (HotelNight hotelNight : hotelDetailResult.hotelDetail
									.getHotelNights()) {
								View view = mInflater.inflate(
										R.layout.hotel_nights_table_row, null);
								TextView fangxing = (TextView) view
										.findViewById(R.id.fangxing);
								fangxing.setText(hotelNight.getFangxing()
										.length() <= 5 ? hotelNight
										.getFangxing() : hotelNight
										.getFangxing().subSequence(0, 4) + "..");
								TextView priceText = (TextView) view
										.findViewById(R.id.price);
								priceText.setText(hotelNight.getPrice() + "元");
								double price11 = Double.parseDouble(hotelNight
										.getPrice());
								if (price11 < price) {
									price = price11;
								}
								TextView finalprice = (TextView) view
										.findViewById(R.id.finalprice);
								finalprice.setText(hotelNight.getFinalprice()
										+ "元");
								Button yuding = (Button) view
										.findViewById(R.id.yudingButton);
								yuding.setTag(hotelNight.getNid());
								yuding.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										String inputStr = hotelDetailResult.hotelDetail
												.getPhone();
										Intent myIntentDial = new Intent(
												Intent.ACTION_DIAL, Uri
														.parse("tel://"
																+ inputStr));// 直接拨打电话Intent.ACTION_CALL

										startActivity(myIntentDial);
										try {
											insertHotel();
										} catch (Exception e) {
											Log.i(TAG, e.toString());
										}

										// ////////////
										// final String nid =
										// (String)v.getTag();
										//
										//
										// new
										// AlertDialog.Builder(HotelDetailActivity.this).setTitle("选择预订方式").setIcon(
										// android.R.drawable.ic_dialog_info).setItems(
										// new String[] { "电话预订", "订单预订" },
										// new DialogInterface.OnClickListener()
										// {
										// public void onClick(DialogInterface
										// dialog, int which) {
										// switch (which) {
										// case 0:
										// String inputStr =
										// hotelDetailResult.hotelDetail.getPhone();
										// Intent myIntentDial = new
										// Intent(Intent.ACTION_DIAL,
										// Uri.parse("tel://" +
										// inputStr));//直接拨打电话Intent.ACTION_CALL
										//
										// startActivity(myIntentDial);
										// try {
										// insertHotel();
										// } catch (Exception e) {
										// Log.i(TAG, e.toString());
										// }
										// break;
										// case 1:
										// Intent intent = new
										// Intent(HotelDetailActivity.this,HotelOrderActivity.class);
										// String[] as = new String[3];
										// as[0] = nid;
										// as[1] = comDate;
										// as[2] = outDate;
										// intent.putExtra(HotelOrderActivity.EXTRA_ARR,
										// as);
										// startActivity(intent);
										// default:
										// break;
										// }
										// }
										// }).setNegativeButton("取消",
										// null).show();
										//
										//
										//
									}
								});

								tableLayout.addView(view);
							}
							content.addView(fangxingView);

						} else {
							content.addView(fangxingView);
						}

					} else {
						TextView textView = new TextView(
								HotelDetailActivity.this);
						textView.setText("酒店暂无房型可预订");
						content.addView(textView);
					}

				}

			}
		});
		fangxingText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
			}
		});

	}

	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.hotel_detail_activity);

		hid = getIntent().getIntExtra(HOTEL_ID, -1);
		comDate = getIntent().getStringExtra(COME_DATE);
		outDate = getIntent().getStringExtra(OUT_DATE);

		String[] as = new String[3];
		as[0] = new Integer(hid).toString();
		as[1] = comDate;
		as[2] = outDate;
		new HotelDetailTask().execute(as);

	}

	class HotelDetailTask extends AsyncTask<String, Void, HotelDetailResult> {
		private static final String TAG = "HotelDetailTask";
		private Exception mReason;

		public HotelDetailTask() {
		}

		protected HotelDetailResult doInBackground(String... paramArrayOfString) {
			if (DEBUG)
				Log.d(TAG, "doInBackground()");
			HotelDetailResult hDetailResult = null;
			try {
				TuanTripApp localTuanTripApp = ((TuanTrip) HotelDetailActivity.this
						.getApplication()).getTuanTripApp();
				hDetailResult = localTuanTripApp
						.getHotelDetailResult(paramArrayOfString);

				if (DEBUG)
					Log.d(TAG, "doInBackground() over");

			} catch (Exception localException) {
				this.mReason = localException;
				hDetailResult = null;
			}

			return hDetailResult;
		}

		protected void onPostExecute(HotelDetailResult paramOrders) {
			if (DEBUG)
				Log.d(TAG, "onPostExecute()");
			if (paramOrders != null
					&& paramOrders.mHttpResult.getStat() == TuanTripSettings.POSTSUCCESS) {
				if (comText != null) {
					comDate = comText.getText().toString();
					outDate = outText.getText().toString();
				}
				ensureUi(paramOrders);
			} else {
				if (comText != null) {
					comText.setText(comDate);
					outText.setText(outDate);
				}

				if (paramOrders == null) {
					NotificationsUtil.ToastReasonForFailure(
							HotelDetailActivity.this, mReason);
				} else if (paramOrders.mHttpResult.getStat() == TuanTripSettings.POSTFAILED) {
					Toast.makeText(HotelDetailActivity.this,
							paramOrders.mHttpResult.getMessage(),
							Toast.LENGTH_SHORT).show();
				} else if (paramOrders.mHttpResult.getStat() == TuanTripSettings.LOGINFAILED) {
					// Toast.makeText(NearbyHotelsActivity.this,paramOrders.mHttpResult.getMessage(),Toast.LENGTH_SHORT).show();
					// ((TuanTrip)getApplication()).loginOut();
					// Intent localIntent4 = new
					// Intent(NearbyHotelsActivity.this, LoginActivity.class);
					// localIntent4.putExtra(LoginActivity.EXTRA_OPTION,
					// LoginActivity.OPTION_ADDADDR);
					// startActivity(localIntent4);
				} else {
					NotificationsUtil.ToastReasonForFailure(
							HotelDetailActivity.this, mReason);
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
			startProgressBar("加载酒店", "加载中...");
		}
	}

	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case COM_DATE:
			return new DatePickerDialog(this, new DateSetLisner(comText),
					mYear, mMonth, mDay);
		case OU_DATE:
			return new DatePickerDialog(this, new DateSetLisner(outText),
					mYear, mMonth, mDay);

		}
		return null;
	}

	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {
		switch (id) {
		case COM_DATE:
			((DatePickerDialog) dialog).updateDate(mYear, mMonth, mDay);
			break;
		case OU_DATE:
			((DatePickerDialog) dialog).updateDate(mYear, mMonth, mDay);
			break;
		}
	}

	// insert history hotel
	public void insertHotel() {
		ContentValues cValues = new ContentValues();
		cValues.put(HotelShort.HID, hotelDetailResult.hotelDetail.getHid());
		cValues.put(HotelShort.SHORTTITLE,
				hotelDetailResult.hotelDetail.getTitle());
		cValues.put(HotelShort.IMAGEURL,
				hotelDetailResult.hotelDetail.getImage());
		cValues.put(HotelShort.ADDRESS,
				hotelDetailResult.hotelDetail.getAddress());
		cValues.put(HotelShort.PRICE, new Double(price).toString());

		new HotelInsertUtil(this).insertHotel(cValues);
	}

}
