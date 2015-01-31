package com.tudaidai.tuantrip.sqllite;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.tudaidai.tuantrip.types.HotelShort;

public class HistoryHotelProvider extends ContentProvider {

	private static final String TAG = "HistoryHotelProvider";

	private static final String DATABASE_NAME = "hotels.db";
	private static final int DATABASE_VERSION = 1;
	private static final String HOTELS_TABLE_NAME = "hotels";

	private static final int HOTELS = 1;
	private static final int HOTELS_ID = 2;

	private static final UriMatcher sUriMatcher;

	/**
	 * This class helps open, create, and upgrade the database file.
	 */
	private static class DatabaseHelper extends SQLiteOpenHelper {

		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL("CREATE TABLE " + HOTELS_TABLE_NAME + " ("
					+ HotelShort.HID + " INTEGER PRIMARY KEY,"
					+ HotelShort.SHORTTITLE + " TEXT," + HotelShort.IMAGEURL
					+ " TEXT," + HotelShort.PRICE + " TEXT,"
					+ HotelShort.ADDRESS + " TEXT" + ");");
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
					+ newVersion + ", which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS " + HOTELS_TABLE_NAME + "");
			onCreate(db);
		}
	}

	private DatabaseHelper mOpenHelper;

	@Override
	public boolean onCreate() {
		mOpenHelper = new DatabaseHelper(getContext());
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {

		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setTables(HOTELS_TABLE_NAME);

		switch (sUriMatcher.match(uri)) {
		case HOTELS:

			break;
		case HOTELS_ID:
			qb.appendWhere(HotelShort.HID + "=" + uri.getPathSegments().get(1));
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		// Get the database and run the query
		SQLiteDatabase db = mOpenHelper.getReadableDatabase();
		Cursor c = qb.query(db, projection, selection, selectionArgs, null,
				null, null);

		// Tell the cursor what uri to watch, so it knows when its source data
		// changes
		c.setNotificationUri(getContext().getContentResolver(), uri);
		return c;

	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		if (sUriMatcher.match(uri) != HOTELS) {
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		long rowId = db.insert(HOTELS_TABLE_NAME, HotelShort.ADDRESS, values);
		if (rowId > 0) {
			Uri noteUri = ContentUris.withAppendedId(HotelShort.CONTENT_URI,
					rowId);
			getContext().getContentResolver().notifyChange(noteUri, null);
			return noteUri;
		}

		throw new SQLException("Failed to insert row into " + uri);
	}

	@Override
	public int delete(Uri uri, String where, String[] whereArgs) {

		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		int count;
		switch (sUriMatcher.match(uri)) {
		case HOTELS:
			count = db.delete(HOTELS_TABLE_NAME, where, whereArgs);
			break;

		case HOTELS_ID:
			String noteId = uri.getPathSegments().get(1);
			count = db.delete(HOTELS_TABLE_NAME,
					HotelShort.HID
							+ "="
							+ noteId
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;

		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		getContext().getContentResolver().notifyChange(uri, null);
		return count;

	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

	static {
		sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		sUriMatcher.addURI(HotelShort.AUTHORITY, "hotels", HOTELS);
		sUriMatcher.addURI(HotelShort.AUTHORITY, "hotels/#", HOTELS_ID);
	}

}
