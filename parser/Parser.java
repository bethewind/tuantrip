package com.tudaidai.tuantrip.parser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.tudaidai.tuantrip.error.TuanTripException;
import com.tudaidai.tuantrip.types.Group;
import com.tudaidai.tuantrip.types.TuanTripType;

public interface Parser<T extends TuanTripType> {

	public abstract T parse(JSONObject json) throws JSONException;

	public Group parse(JSONArray array) throws JSONException;

	public abstract T parse(String html) throws TuanTripException;

}
