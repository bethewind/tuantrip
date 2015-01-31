package com.tudaidai.tuantrip;

public class TuanTripSettings {

	public static final boolean USE_DEBUG_SERVER = false;

	public static final boolean DEBUG = true;
	public static final boolean LOCATION_DEBUG = false;
	public static final String SITE = "http://upload.tudaidai.com/";
	public static final boolean USE_DUMPCATCHER = true;
	public static final boolean DUMPCATCHER_TEST = false;
	public static final int POSTSUCCESS = 200;
	public static final int POSTFAILED = 502;
	public static final int LOGINFAILED = 520;
	public static final int ISBUY = 302;
	public static final long CATEGORY_ICON_EXPIRATION = 60L * 60L * 24L * 7L
			* 1000L * 2L; // two weeks.

}
