package com.tudaidai.tuantrip;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TabHost;
import android.widget.TextView;

public class HotelOrderActivity1 extends TabActivity {
	static final boolean DEBUG = TuanTripSettings.DEBUG;
	static final String TAG = "OrderActivity";

	private void ensureUi() {

		TabHost localTabHost = getTabHost();
		Intent intent1 = new Intent(this, HotelOrderListActivity.class);
		intent1.putExtra(HotelOrderListActivity.EXTRA_OPTION,
				HotelOrderListActivity.OPTION_ALL);
		View view1 = prepareTabView("全部");
		localTabHost.addTab(localTabHost.newTabSpec("all").setIndicator(view1)
				.setContent(intent1));
		// Intent intent2 = new Intent(this, HotelOrderListActivity.class);
		// intent2.putExtra(HotelOrderListActivity.EXTRA_OPTION,HotelOrderListActivity.OPTION_SUB);
		// View view2 = prepareTabView("未提交");
		// localTabHost.addTab(localTabHost.newTabSpec("unsub").setIndicator(view2)
		// .setContent(intent2));
		Intent intent3 = new Intent(this, HotelOrderListActivity.class);
		intent3.putExtra(HotelOrderListActivity.EXTRA_OPTION,
				HotelOrderListActivity.OPTION_CHULI);
		View view3 = prepareTabView("处理中");
		localTabHost.addTab(localTabHost.newTabSpec("chuli")
				.setIndicator(view3).setContent(intent3));
		// Intent intent4 = new Intent(this, HotelOrderListActivity.class);
		// intent4.putExtra(HotelOrderListActivity.EXTRA_OPTION,HotelOrderListActivity.OPTION_QUEREN);
		// View view4 = prepareTabView("商家确认");
		// localTabHost.addTab(localTabHost.newTabSpec("queren").setIndicator(view4)
		// .setContent(intent4));
		Intent intent5 = new Intent(this, HotelOrderListActivity.class);
		intent5.putExtra(HotelOrderListActivity.EXTRA_OPTION,
				HotelOrderListActivity.OPTION_PAID);
		View view5 = prepareTabView("已付款");
		localTabHost.addTab(localTabHost.newTabSpec("paid").setIndicator(view5)
				.setContent(intent5));
		Intent intent6 = new Intent(this, HotelOrderListActivity.class);
		intent6.putExtra(HotelOrderListActivity.EXTRA_OPTION,
				HotelOrderListActivity.OPTION_QUXIAO);
		View view6 = prepareTabView("取消");
		localTabHost.addTab(localTabHost.newTabSpec("quxiao")
				.setIndicator(view6).setContent(intent6));

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
