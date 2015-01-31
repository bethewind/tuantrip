package com.tudaidai.tuantrip;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import com.tudaidai.tuantrip.types.HotelOrder;

public class HotelOrderDetailActivity extends Activity {
	static final boolean DEBUG = TuanTripSettings.DEBUG;
	public static final String EXTRA_ORDER = "com.tudaidai.tuantrip.HotelOrderDetailActivity.ORDER";
	static final String TAG = "HotelOrderDetailActivity";
	private HotelOrder order;

	private void ensureUi() {
		TextView detailText = (TextView)findViewById(R.id.detailText);
		detailText.setText(Html.fromHtml(order.getDetail()));
	}

	protected void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		setContentView(R.layout.hotel_order_detail_activity);
		order = (HotelOrder) getIntent().getExtras().getParcelable(EXTRA_ORDER);
		ensureUi();
	}
}
