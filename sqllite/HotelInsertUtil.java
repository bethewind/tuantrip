package com.tudaidai.tuantrip.sqllite;

import android.content.ContentValues;
import android.content.Context;

import com.tudaidai.tuantrip.types.HotelShort;

public class HotelInsertUtil {
	private Context context;

	public HotelInsertUtil(Context context) {
		this.context = context;
	}

	public void insertHotel(ContentValues cValues) {
		context.getContentResolver().insert(HotelShort.CONTENT_URI, cValues);
	}

}
