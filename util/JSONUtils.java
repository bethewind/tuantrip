package com.tudaidai.tuantrip.util;

import java.io.IOException;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.tudaidai.tuantrip.TuanTripSettings;
import com.tudaidai.tuantrip.error.TuanTripException;
import com.tudaidai.tuantrip.error.TuanTripParseException;
import com.tudaidai.tuantrip.parser.Parser;
import com.tudaidai.tuantrip.types.TuanTripType;

public class JSONUtils {

	private static final boolean DEBUG = TuanTripSettings.DEBUG;
	private static final Logger LOG = Logger.getLogger(JSONUtils.class
			.getCanonicalName());

	/**
	 * Takes a parser, a json string, and returns a foursquare type.
	 */
	public static TuanTripType consume(Parser<? extends TuanTripType> parser,
			String content) throws TuanTripParseException, TuanTripException,
			IOException {

		if (DEBUG) {
			LOG.log(Level.FINE, "http response: " + content);
		}

		try {
			// The v1 API returns the response raw with no wrapper. Depending on
			// the
			// type of API call, the content might be a JSONObject or a
			// JSONArray.
			// Since JSONArray does not derive from JSONObject, we need to check
			// for
			// either of these cases to parse correctly.
			JSONObject json = new JSONObject(content);
			Iterator<String> it = (Iterator<String>) json.keys();
			if (it.hasNext()) {
				String key = (String) it.next();
				if (key.equals("error")) {
					throw new TuanTripException(json.getString(key));
				} else {
					Object obj = json.get(key);
					if (obj instanceof JSONArray) {
						return parser.parse((JSONArray) obj);
					} else {
						return parser.parse((JSONObject) obj);
					}
				}
			} else {
				throw new TuanTripException(
						"Error parsing JSON response, object had no single child key.");
			}

		} catch (JSONException ex) {
			throw new TuanTripException("Error parsing JSON response: "
					+ ex.getMessage() + content);
		}
	}
}