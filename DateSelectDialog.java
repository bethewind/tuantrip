package com.tudaidai.tuantrip;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class DateSelectDialog extends Dialog {
	private String mChosenDate = "";
	private String[] mDates;

	public DateSelectDialog(Context paramContext, String[] mDates) {
		super(paramContext);
		this.mDates = mDates;
	}

	public String getChosenDate() {
		return this.mChosenDate;
	}

	public void ensureUI() {
		ListView listview = (ListView) findViewById(R.id.timeListView);
		ArrayAdapter<String> simpleadapter = new ArrayAdapter<String>(
				this.getContext(), R.layout.time_picker_list_item, R.id.text,
				mDates);
		listview.setAdapter(simpleadapter);
		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				mChosenDate = (String) parent.getItemAtPosition(position);
				cancel();

			}
		});
	}

	public void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		setContentView(R.layout.time_picker_dialog);
		setTitle("选择日期");
		ensureUI();
	}
}
