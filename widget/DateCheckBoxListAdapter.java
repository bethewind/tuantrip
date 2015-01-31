package com.tudaidai.tuantrip.widget;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.tudaidai.tuantrip.R;
import com.tudaidai.tuantrip.TuanTripSettings;
import com.tudaidai.tuantrip.types.DatePrice;
import com.tudaidai.tuantrip.types.Group;

public class DateCheckBoxListAdapter extends BaseGroupAdapter<DatePrice> {
	private LayoutInflater mInflater;
	private Context context;
	private Group<DatePrice> userBuyDatePrice;
	static final boolean DEBUG = TuanTripSettings.DEBUG;

	public DateCheckBoxListAdapter(Context context,
			Group<DatePrice> userBuyDatePrice) {
		super(context);
		mInflater = LayoutInflater.from(context);
		this.context = context;
		this.userBuyDatePrice = userBuyDatePrice;
	}

	public Group<DatePrice> getDatePrice() {
		return this.userBuyDatePrice;
	}

	@Override
	public View getView(int i, View convertView, ViewGroup parent) {
		ViewHolder viewholder = null;
		DatePrice datePrice = null;
		convertView = null;

		convertView = mInflater.inflate(R.layout.date_checkbox_item, null);
		viewholder = new ViewHolder();
		TextView textview = (TextView) convertView
				.findViewById(R.id.datePriceText);
		viewholder.title = textview;
		CheckBox checkBox = (CheckBox) convertView
				.findViewById(R.id.dateCheckBox);
		viewholder.checkBox = checkBox;
		convertView.setTag(viewholder);

		datePrice = (DatePrice) getItem(i);
		String titleString = datePrice.getDate() + "  ￥" + datePrice.getPrice()
				+ "元";
		viewholder.title.setText(titleString);
		viewholder.checkBox.setTag(datePrice);
		viewholder.checkBox
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						CheckBox checkBox = (CheckBox) buttonView;
						DatePrice datePrice = (DatePrice) checkBox.getTag();
						if (userBuyDatePrice == null)
							userBuyDatePrice = new Group<DatePrice>();

						if (isChecked) {
							if (!userBuyDatePrice.contains(datePrice)) {
								userBuyDatePrice.add(datePrice);
							}
						} else {
							if (userBuyDatePrice.contains(datePrice))
								userBuyDatePrice.remove(datePrice);
						}

					}
				});

		if (userBuyDatePrice != null && userBuyDatePrice.contains(datePrice)) {
			viewholder.checkBox.setChecked(true);
			if (DEBUG) {
				Log.i("1111111111", datePrice.getDate());
			}
		}

		return convertView;
	}

	public void setGroup(Group group) {
		super.setGroup(group);
	}

	public class ViewHolder {

		public CheckBox checkBox;
		public TextView title;

		private ViewHolder() {
		}
	}
}
