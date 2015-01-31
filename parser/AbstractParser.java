package com.tudaidai.tuantrip.parser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.tudaidai.tuantrip.error.TuanTripException;
import com.tudaidai.tuantrip.types.Group;
import com.tudaidai.tuantrip.types.TuanTripType;

public abstract class AbstractParser<T extends TuanTripType> implements
		Parser<T> {

	/**
	 * All derived parsers must implement parsing a JSONObject instance of
	 * themselves.
	 */
	public T parse(JSONObject json) throws JSONException {
		throw new JSONException("Unexpected JSON parse type encountered.");
	}

	/**
	 * Only the GroupParser needs to implement this.
	 */
	public Group parse(JSONArray array) throws JSONException {
		throw new JSONException("Unexpected JSONArray parse type encountered.");
	}

	public T parse(String html) throws TuanTripException {
		throw new TuanTripException("Unexpected String parse type encountered.");
	}
}
