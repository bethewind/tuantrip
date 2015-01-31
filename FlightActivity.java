package com.tudaidai.tuantrip;

import com.tudaidai.tuantrip.types.User;

import android.app.TabActivity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.TabHost;

public class FlightActivity extends TabActivity {
	static final boolean DEBUG = TuanTripSettings.DEBUG;
	static final String TAG = "HotelActivity";
	private User mUser;

	private void ensureUi() {
		TabHost localTabHost = getTabHost();
		localTabHost.addTab(localTabHost
				.newTabSpec("ticket")
				.setIndicator("航班时刻",
						getResources().getDrawable(R.drawable.tab_q_icon_true))
				.setContent(new Intent(this, SearchHBSKActivity.class)));
		localTabHost.addTab(localTabHost
				.newTabSpec("tuanorder")
				.setIndicator("航班动态",
						getResources().getDrawable(R.drawable.tab_f_icon_true))
				.setContent(new Intent(this, SearchHBDTActivity.class)));
	}

	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);

		ensureUi();

	}
}
