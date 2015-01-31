package com.tudaidai.tuantrip.parser;

import org.json.JSONException;
import org.json.JSONObject;

import com.tudaidai.tuantrip.types.Version;

public class VersionParser extends AbstractParser<Version> {

	@Override
	public Version parse(JSONObject json) throws JSONException {

		Version version = new Version();

		JSONObject versionObject = json.getJSONObject("Version");
		if (versionObject.has("Code")) {
			version.setCode(Integer.parseInt(versionObject.getString("Code")));
		}
		if (versionObject.has("Name")) {
			version.setName(versionObject.getString("Name"));
		}
		if (versionObject.has("Url")) {
			version.setUrl(versionObject.getString("Url"));
		}

		return version;
	}

}