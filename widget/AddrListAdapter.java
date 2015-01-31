package com.tudaidai.tuantrip.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.tudaidai.tuantrip.AddrActivity;
import com.tudaidai.tuantrip.EditAddrActivity;
import com.tudaidai.tuantrip.R;
import com.tudaidai.tuantrip.types.AddrInfo;
import com.tudaidai.tuantrip.types.Group;

public class AddrListAdapter extends BaseGroupAdapter<AddrInfo> {
	private LayoutInflater mInflater;
	private Context context;

	public AddrListAdapter(Context context) {
		super(context);
		mInflater = LayoutInflater.from(context);
		this.context = context;
		// TODO Auto-generated constructor stub
	}

	@Override
	public View getView(int i, View convertView, ViewGroup parent) {
		ViewHolder viewholder = null;
		AddrInfo addrinfo = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.addr_list_item, null);
			viewholder = new ViewHolder();
			TextView textview = (TextView) convertView
					.findViewById(R.id.addrText);
			viewholder.title = textview;
			Button button = (Button) convertView.findViewById(R.id.editButton);
			viewholder.editBtn = button;
			convertView.setTag(viewholder);
		} else {
			viewholder = (ViewHolder) convertView.getTag();
		}
		addrinfo = (AddrInfo) getItem(i);
		String titleString = addrinfo.getProvince() + addrinfo.getCity()
				+ addrinfo.getArea() + addrinfo.getHouse();
		viewholder.title.setText(titleString);
		viewholder.editBtn.setTag((Integer) i);
		viewholder.editBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				Integer i = (Integer) view.getTag();
				Intent intent = new Intent(context, EditAddrActivity.class);
				AddrInfo addrinfo = (AddrInfo) getItem(i);
				intent.putExtra(EditAddrActivity.EXTRA_ADDR, addrinfo);
				intent.putExtra(EditAddrActivity.EXTRA_OPTION,
						EditAddrActivity.OPTION_EDIT);
				((Activity) context).startActivityForResult(intent,
						AddrActivity.REQUEST_CODE);

			}
		});

		return convertView;
	}

	public void setGroup(Group group) {
		super.setGroup(group);
	}

	class ViewHolder {

		Button editBtn;
		TextView title;

		private ViewHolder() {
		}
	}
}
