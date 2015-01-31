package com.tudaidai.tuantrip.widget;

import android.content.Context;
import android.widget.BaseAdapter;

import com.tudaidai.tuantrip.types.Group;
import com.tudaidai.tuantrip.types.TuanTripType;

abstract class BaseGroupAdapter<T extends TuanTripType> extends BaseAdapter {

	Group<T> group = null;

	public BaseGroupAdapter(Context context) {
	}

	@Override
	public int getCount() {
		return (group == null) ? 0 : group.size();
	}

	@Override
	public Object getItem(int position) {
		return group.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public boolean isEmpty() {
		return (group == null) ? true : group.isEmpty();
	}

	public void setGroup(Group<T> g) {
		group = g;
		notifyDataSetInvalidated();
	}
}
