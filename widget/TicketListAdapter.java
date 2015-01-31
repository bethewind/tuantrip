package com.tudaidai.tuantrip.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tudaidai.tuantrip.R;
import com.tudaidai.tuantrip.types.Group;
import com.tudaidai.tuantrip.types.Ticket;

public class TicketListAdapter extends BaseGroupAdapter<Ticket> {
	private LayoutInflater mInflater;
	private Context context;

	public TicketListAdapter(Context context) {
		super(context);
		mInflater = LayoutInflater.from(context);
		this.context = context;
		// TODO Auto-generated constructor stub
	}

	@Override
	public View getView(int i, View convertView, ViewGroup parent) {
		ViewHolder viewholder = null;
		Ticket ticket = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.order_list_item, null);
			viewholder = new ViewHolder();
			TextView textview = (TextView) convertView
					.findViewById(R.id.titleText);
			// textview.setHeight(27);
			// textview.setGravity(Gravity.CENTER);
			viewholder.title = textview;
			convertView.setTag(viewholder);

		} else {
			viewholder = (ViewHolder) convertView.getTag();
		}
		ticket = (Ticket) getItem(i);
		String titleString = ticket.getTitle();
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
