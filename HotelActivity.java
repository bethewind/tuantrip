package com.tudaidai.tuantrip;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

public class HotelActivity extends TabActivity {
	static final boolean DEBUG = TuanTripSettings.DEBUG;
	static final String TAG = "HotelActivity";

	private void ensureUi() {
		TabHost localTabHost = getTabHost();
		localTabHost.addTab(localTabHost
				.newTabSpec("searchhotel")
				.setIndicator("酒店预订",
						getResources().getDrawable(R.drawable.ticket_press))
				.setContent(new Intent(this, SearchHotelActivity.class)));
		Intent intent = new Intent(this, NearbyActivity.class);
		intent.putExtra(NearbyActivity.EXTRA_OPTION, NearbyActivity.HOTEL);
		localTabHost.addTab(localTabHost
				.newTabSpec("nearbyhotel")
				.setIndicator("周边酒店",
						getResources().getDrawable(R.drawable.order))
				.setContent(intent));
		localTabHost.addTab(localTabHost
				.newTabSpec("historyhotel")
				.setIndicator("历史酒店",
						getResources().getDrawable(R.drawable.lishihangxian))
				.setContent(new Intent(this, HistoryHotelsActivity.class)));
	}

	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		ensureUi();

	}
}
