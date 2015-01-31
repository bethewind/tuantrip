package com.tudaidai.tuantrip;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TabHost;
import android.widget.TextView;

public class TicketActivity extends TabActivity {
	static final boolean DEBUG = TuanTripSettings.DEBUG;
	static final String TAG = "TicketActivity";

	private void ensureUi() {

		TabHost localTabHost = getTabHost();

		Intent intent1 = new Intent(this, TicketListActivity.class);
		intent1.putExtra(TicketListActivity.EXTRA_OPTION,
				TicketListActivity.OPTION_ALL);
		View view1 = prepareTabView("全部");
		localTabHost.addTab(localTabHost.newTabSpec("all").setIndicator(view1)
				.setContent(intent1));
		Intent intent2 = new Intent(this, TicketListActivity.class);
		intent2.putExtra(TicketListActivity.EXTRA_OPTION,
				TicketListActivity.OPTION_UNUSED);
		View view2 = prepareTabView("未使用");
		localTabHost.addTab(localTabHost.newTabSpec("unused")
				.setIndicator(view2).setContent(intent2));
		Intent intent3 = new Intent(this, TicketListActivity.class);
		intent3.putExtra(TicketListActivity.EXTRA_OPTION,
				TicketListActivity.OPTION_USED);
		View view3 = prepareTabView("已使用");
		localTabHost.addTab(localTabHost.newTabSpec("used").setIndicator(view3)
				.setContent(intent3));

	}

	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.ticket_view);
		ensureUi();
	}

	private View prepareTabView(String paramString) {
		View localView = LayoutInflater.from(this).inflate(R.layout.ticket_tab,
				null);
		((TextView) localView.findViewById(R.id.ticketTabTextView))
				.setText(paramString);
		return localView;
	}
}
