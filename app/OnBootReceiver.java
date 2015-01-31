package com.tudaidai.tuantrip.app;

import com.tudaidai.tuantrip.TuanTripSettings;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

public class OnBootReceiver extends BroadcastReceiver {
	public static final String TAG = "OnBootReceiver";
	static final boolean DEBUG = TuanTripSettings.DEBUG;
	public static final int DELATE = 10 * 60 * 1000;

	@Override
	public void onReceive(Context context, Intent intent) {

		if (DEBUG)
			Log.e(TAG, "监听到了监听到了监听到了监听到了---------------");

		// Intent startIntent = new Intent();
		// startIntent.setClass(context, PoiService.class);
		// context.startService(startIntent);

		long firstTime = SystemClock.elapsedRealtime();
		PendingIntent mAlarmSender = PendingIntent.getService(context, 0,
				new Intent(context, PoiService.class), 0);
		AlarmManager am = (AlarmManager) context
				.getSystemService(android.content.Context.ALARM_SERVICE);
		am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstTime,
				DELATE, mAlarmSender);
	}
}
