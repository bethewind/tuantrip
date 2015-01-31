package com.tudaidai.tuantrip;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.tudaidai.tuantrip.types.Ticket;

public class TicketDetailActivity extends Activity {
	static final boolean DEBUG = TuanTripSettings.DEBUG;
	public static final String EXTRA_Ticket = "com.tudaidai.tuantrip.TicketDetailActivity.Ticket";
	static final String TAG = "TicketDetailActivity";
	private Ticket ticket;

	private void ensureUi() {
		TextView codeText = (TextView) findViewById(R.id.codeText);
		codeText.setText(ticket.getCode());
		TextView titTextView = (TextView) findViewById(R.id.titleText);
		titTextView.setText(ticket.getTitle());
		TextView dateText = (TextView) findViewById(R.id.dateText);
		dateText.setText(ticket.getDate());
		TextView pwdText = (TextView) findViewById(R.id.pwdText);
		pwdText.setText(ticket.getPwd());
		TextView stateText = (TextView) findViewById(R.id.stateText);
		stateText.setText(ticket.getTicketStatus());
		TextView travelDateText = (TextView) findViewById(R.id.travelDateText);
		travelDateText.setText(ticket.getTravelDate());

	}

	protected void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		setContentView(R.layout.ticket_detail_activity);
		ticket = (Ticket) getIntent().getExtras().getParcelable(EXTRA_Ticket);
		ensureUi();
	}

}
