package com.tudaidai.tuantrip.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;

import com.tudaidai.tuantrip.TodayActivity;
import com.tudaidai.tuantrip.TuanTrip;
import com.tudaidai.tuantrip.TuanTripSettings;

public class TuanUtils {
	private static final boolean DEBUG = TuanTripSettings.DEBUG;
	private static final String TAG = "TuanUtils";

	public static Spanned showHtml(String s, final RemoteResourceManager mRrm,
			final Activity activity) {

		return Html.fromHtml(s, new ImageGetter() {

			@Override
			public Drawable getDrawable(String source) {
				TuanTrip tuanTrip = (TuanTrip) activity.getApplication();
				TodayActivity todayActivity = (TodayActivity) activity;
				Drawable drawable = null;
				if (source != null && !TextUtils.isEmpty(source)) {
					Uri uri = Uri.parse(source);
					if (!mRrm.exists(uri) && todayActivity.isFirstPic == 0
							&& TuanUtils.isPic(source)) {
						if (tuanTrip.getShowPic()) {
							if (DEBUG)
								Log.d(TAG, "!mRrm.exists(uri)" + uri);
							mRrm.request(uri);
							todayActivity.pic1++;
							// drawable =
							// activity.getResources().getDrawable(R.drawable.default_pic);
						}

					} else {

						if (DEBUG)
							Log.d(TAG, "mRrm exists " + uri);
						try {

							Bitmap bitmap = mRrm.getBitmap(uri);
							if (bitmap == null)
								throw new Exception("bitmap is null of " + uri);

							int width = bitmap.getWidth();
							int height = bitmap.getHeight();
							int newWidth = 291;
							int newHeight = 220; // 220
							if (height < newHeight)
								newHeight = height;
							float scaleWidth = ((float) newWidth) / width;
							float scaleHeight = ((float) newHeight) / height;

							Matrix matrix = new Matrix();
							matrix.postScale(scaleWidth, scaleHeight);

							// create the new Bitmap object
							Bitmap resizedBitmap = Bitmap.createBitmap(bitmap,
									0, 0, width, height, matrix, true);

							BitmapDrawable drawable1 = new BitmapDrawable(
									resizedBitmap);
							int i = drawable1.getIntrinsicWidth();
							int j = drawable1.getIntrinsicHeight();
							drawable1.setBounds(0, 0, i, j);
							drawable = drawable1;
						} catch (Exception e) {
							if (DEBUG)
								Log.d(TAG, "Exception" + e);
							drawable = null;
						} catch (OutOfMemoryError e) {
							if (DEBUG)
								Log.e(TAG, uri + " is too large22 with " + e);
							drawable = null;
						}

					}
				}
				return drawable;
			}
		}, null);
	}

	public static Spanned showHtml1(String s, final RemoteResourceManager mRrm) {

		return Html.fromHtml(s, new ImageGetter() {

			@Override
			public Drawable getDrawable(String source) {

				Drawable drawable = null;
				if (source != null && !TextUtils.isEmpty(source)) {
					Uri uri = Uri.parse(source);
					if (!mRrm.exists(uri) && TuanUtils.isPic(source)) {

						if (DEBUG)
							Log.d(TAG, "!mRrm.exists(uri)" + uri);
						mRrm.request(uri);
						// drawable =
						// activity.getResources().getDrawable(R.drawable.default_pic);

					} else {

						if (DEBUG)
							Log.d(TAG, "mRrm exists " + uri);
						try {

							Bitmap bitmap = mRrm.getBitmap(uri);
							if (bitmap == null)
								throw new Exception("bitmap is null of " + uri);

							BitmapDrawable drawable1 = new BitmapDrawable(
									bitmap);
							int i = drawable1.getIntrinsicWidth();
							int j = drawable1.getIntrinsicHeight();
							drawable1.setBounds(0, 0, i, j);
							drawable = drawable1;
						} catch (Exception e) {
							if (DEBUG)
								Log.d(TAG, "Exception" + e);
							drawable = null;
						} catch (OutOfMemoryError e) {
							if (DEBUG)
								Log.e(TAG, uri + " is too large22 with " + e);
							drawable = null;
						}

					}
				}
				return drawable;
			}
		}, null);
	}

	public static boolean checkPhone(String phone) {
		Pattern pattern = Pattern.compile("^1\\d{10}$");
		Matcher matcher = pattern.matcher(phone);

		if (matcher.matches()) {
			return true;
		}
		return false;
	}

	public static boolean isPic(String picUrl) {
		if (!picUrl.startsWith("http://"))
			return false;

		int i = picUrl.lastIndexOf('.');
		String surfix = picUrl.substring(i + 1, picUrl.length());
		if (!"jpg".equalsIgnoreCase(surfix) && !"png".equalsIgnoreCase(surfix)
				&& !"gif".equalsIgnoreCase(surfix)) {
			return false;
		}

		return true;
	}

	public static Bitmap getBitmapFromUrl(String urlString) {
		Bitmap bitmap = null;
		try {
			URL url = new URL(urlString);

			URLConnection uc = url.openConnection();

			InputStream inputstream = (InputStream) uc.getInputStream();

			try {
				bitmap = BitmapFactory.decodeStream(inputstream);
			} catch (OutOfMemoryError e) {
				if (DEBUG)
					Log.e(TAG, urlString + " is too large22 with " + e);
				bitmap = null;
			}

		} catch (IOException e) {
			if (DEBUG)
				Log.e(TAG, urlString + " with " + e);
		}
		return bitmap;
	}

	public static String[] getToday() {
		String[] as = new String[2];
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		int MILLIS_IN_DAY = 1000 * 60 * 60 * 24;
		Date startDate = new Date();
		Date endDate = new Date(new Date().getTime() + MILLIS_IN_DAY * 2);
		as[0] = format.format(startDate);
		as[1] = format.format(endDate);

		return as;
	}

	public static String[] getTommory() {
		String[] as = new String[2];
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		int MILLIS_IN_DAY = 1000 * 60 * 60 * 24;
		Date startDate = new Date(new Date().getTime() + MILLIS_IN_DAY * 1);
		Date endDate = new Date(new Date().getTime() + MILLIS_IN_DAY * 3);
		as[0] = format.format(startDate);
		as[1] = format.format(endDate);

		return as;
	}

	public static String[] getHoutain() {
		String[] as = new String[2];
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		int MILLIS_IN_DAY = 1000 * 60 * 60 * 24;
		Date startDate = new Date(new Date().getTime() + MILLIS_IN_DAY * 2);
		Date endDate = new Date(new Date().getTime() + MILLIS_IN_DAY * 4);
		as[0] = format.format(startDate);
		as[1] = format.format(endDate);

		return as;
	}

}
