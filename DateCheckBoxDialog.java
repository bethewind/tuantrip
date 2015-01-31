package com.tudaidai.tuantrip;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.tudaidai.tuantrip.types.DatePrice;
import com.tudaidai.tuantrip.types.Group;
import com.tudaidai.tuantrip.widget.DateCheckBoxListAdapter;

public class DateCheckBoxDialog extends Dialog {
	private Group<DatePrice> datePrice;
	private Group<DatePrice> userBuyDatePrice;
	private DateCheckBoxListAdapter dateCheckBoxListAdapter;
	private ListView listview;

	public DateCheckBoxDialog(Context paramContext, Group<DatePrice> datePrice,
			Group<DatePrice> userBuyDatePrice) {
		super(paramContext);
		this.datePrice = datePrice;
		this.userBuyDatePrice = userBuyDatePrice;
	}

	public Group<DatePrice> getDatePrice() {
		return this.userBuyDatePrice;
	}

	public void ensureUI() {
		listview = (ListView) findViewById(R.id.dateListView);
		dateCheckBoxListAdapter = new DateCheckBoxListAdapter(
				this.getContext(), userBuyDatePrice);
		dateCheckBoxListAdapter.setGroup(datePrice);
		listview.setAdapter(dateCheckBoxListAdapter);
		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// DateCheckBoxListAdapter.ViewHolder viewHolder =
				// (DateCheckBoxListAdapter.ViewHolder)view.getTag();
				// viewHolder.checkBox.setChecked(!viewHolder.checkBox.isChecked());

			}
		});

		Button dateButton = (Button) findViewById(R.id.dateCheckBoxButton);
		dateButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				userBuyDatePrice = dateCheckBoxListAdapter.getDatePrice();

				// int count = listview.getCount();
				// for(int i = 0; i < count; i++)
				// {
				// LinearLayout layout = (LinearLayout)listview.getChildAt(i);
				// int c = layout.getChildCount();
				// for(int j = 0; j < c; j++)
				// {
				// View view = layout.getChildAt(j);
				// if(view instanceof CheckBox)
				// {
				// CheckBox checkBox = (CheckBox)view;
				// if(checkBox.isChecked())
				// {
				// userBuyDatePrice.add((DatePrice)checkBox.getTag());
				// }
				// }
				// }
				// }

				cancel();

			}
		});
	}

	public void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		setContentView(R.layout.date_checkbox_dialog);
		setTitle("选择日期");
		ensureUI();
	}
}
