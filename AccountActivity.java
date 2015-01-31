package com.tudaidai.tuantrip;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

import com.tudaidai.tuantrip.types.User;

/**
 * @author Administrator
 *
 */
public class AccountActivity extends TabActivity {
	static final boolean DEBUG = TuanTripSettings.DEBUG;
	static final String TAG = "HotelActivity";
	private User mUser;

	private void ensureUi() {
		TabHost localTabHost = getTabHost();
		localTabHost.addTab(localTabHost
				.newTabSpec("ticket")
				.setIndicator("我的团购券",
						getResources().getDrawable(R.drawable.ticket))
				.setContent(new Intent(this, TicketActivity.class)));
		localTabHost.addTab(localTabHost
				.newTabSpec("tuanorder")
				.setIndicator("团购订单",
						getResources().getDrawable(R.drawable.order))
				.setContent(new Intent(this, OrderActivity.class)));
//		 localTabHost.addTab(localTabHost.newTabSpec("hotelorder").setIndicator("酒店订单",getResources().getDrawable(R.drawable.account))
//		 .setContent(new Intent(this, HotelOrderActivity1.class)));
	}
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		User localUser = ((TuanTrip) getApplication()).getUser();
		this.mUser = localUser;
		if (this.mUser == null) {
			Intent localIntent1 = new Intent(this, LoginActivity.class);
			localIntent1.putExtra(LoginActivity.EXTRA_OPTION,
					LoginActivity.OPTION_ACCOUNT);
			startActivity(localIntent1);
			finish();

		} else {
			ensureUi();
		}

	}
}
