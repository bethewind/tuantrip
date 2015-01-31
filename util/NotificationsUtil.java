package com.tudaidai.tuantrip.util;

import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.tudaidai.tuantrip.TuanTripSettings;
import com.tudaidai.tuantrip.error.TuanTripException;

public class NotificationsUtil {
	private static final String TAG = "NotificationsUtil";
	private static final boolean DEBUG = TuanTripSettings.DEBUG;

	public static void ToastReasonForFailure(Context context, Exception e) {
		if (DEBUG)
			Log.d(TAG, "Toasting for exception: ", e);

		if (e == null) {
			Toast.makeText(context, "请求异常，请重试!", Toast.LENGTH_SHORT).show();
		} else if (e instanceof SocketTimeoutException) {
			Toast.makeText(context, "请求超时", Toast.LENGTH_SHORT).show();

		} else if (e instanceof SocketException) {
			Toast.makeText(context, "服务器没响应", Toast.LENGTH_SHORT).show();

		} else if (e instanceof IOException) {
			Toast.makeText(context, "网络不可用", Toast.LENGTH_SHORT).show();

		} else if (e instanceof TuanTripException) {
			String message;
			int toastLength = Toast.LENGTH_SHORT;
			if (e.getMessage() == null) {
				message = "无效请求";
			} else {
				message = e.getMessage();
				toastLength = Toast.LENGTH_LONG;
			}
			Toast.makeText(context, message, toastLength).show();

		} else {
			Toast.makeText(context, "请求异常，请重试!", Toast.LENGTH_SHORT).show();
		}
	}
}
