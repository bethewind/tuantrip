package com.tudaidai.tuantrip;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tudaidai.tuantrip.types.City;
import com.tudaidai.tuantrip.types.Group;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

public class CityPickerDialogNoEdit extends Dialog {
	private int mChosenCity = -1;
	private String mChosenCityName = "-1";
	private Group<City> mCities;

	public CityPickerDialogNoEdit(Context paramContext, Group<City> paramGroup) {
		super(paramContext);
		this.mCities = paramGroup;
	}

	public int getChosenCity() {
		return this.mChosenCity;
	}

	public String getChosenCityName() {
		return this.mChosenCityName;
	}

	public void ensureUI() {
		ListView listview = (ListView) findViewById(R.id.timeListView);
		SimpleAdapter simpleadapter = new SimpleAdapter(this.getContext(),
				getData(), R.layout.time_picker_list_item,
				new String[] { "text" }, new int[] { R.id.text });
		listview.setAdapter(simpleadapter);
		listview.setCacheColorHint(0);
		listview.setBackgroundResource(R.color.white);
		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				mChosenCity = position;
				mChosenCityName = mCities.get(position).getCnName();
				cancel();

			}
		});
	}

	public void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		setContentView(R.layout.time_picker_dialog);
		setTitle("城市列表");
		ensureUI();
	}

	private List<Map<String, Object>> getData() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		for (int i = 0; i < mCities.size(); i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("text", mCities.get(i).getCnName());
			list.add(map);
		}
		return list;

	}
}
