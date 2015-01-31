package com.tudaidai.tuantrip.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tudaidai.tuantrip.R;
import com.tudaidai.tuantrip.types.Group;
import com.tudaidai.tuantrip.types.HotelOrder;

public class HotelOrderListAdapter extends BaseGroupAdapter<HotelOrder> {
	private LayoutInflater mInflater;
	private Context context;

	public HotelOrderListAdapter(Context context) {
		super(context);
		mInflater = LayoutInflater.from(context);
		this.context = context;
		// TODO Auto-generated constructor stub
	}

	@Override
	public View getView(int i, View convertView, ViewGroup parent) {
		ViewHolder viewholder = null;
		HotelOrder order = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.order_list_item, null);
			viewholder = new ViewHolder();
			TextView textview = (TextView) convertView
					.findViewById(R.id.titleText);
			viewholder.title = textview;
			convertView.setTag(viewholder);
		} else {
			viewholder = (ViewHolder) convertView.getTag();
		}
		order = (HotelOrder) getItem(i);
		String titleString = order.getName() + "(" + order.getCometime() + ")";
		// viewholder.title.setText(titleString.length() < 20 ? titleString :
		// titleString.substring(0,20)+"...");
		viewholder.title.setText(titleString);
		return convertView;
	}

	public void setGroup(Group group) {
		super.setGroup(group);
	}

	class ViewHolder {

		TextView title;

		private ViewHolder() {
		}

	}
}
