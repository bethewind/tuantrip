package com.tudaidai.tuantrip.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.tudaidai.tuantrip.TuanTripSettings;

public class MemDiskCache implements DiskCache {

	private static final boolean DEBUG = TuanTripSettings.DEBUG;
	private static final int MAX_FILE_NUM = 40;
	private static final String TAG = "MemDiskCache";
	private ConcurrentHashMap<String, Bitmap> mBitmapMap = new ConcurrentHashMap<String, Bitmap>();
	private List<String> mList = new LinkedList<String>();

	public MemDiskCache() {
	}

	public void cleanup() {
		mBitmapMap.clear();
		mList.clear();
	}

	public void clear() {
		mBitmapMap.clear();
		mList.clear();
	}

	public boolean exists(String s) {
		boolean flag;
		synchronized (mBitmapMap) {
			flag = mBitmapMap.containsKey(s);
		}
		return flag;
	}

	public Bitmap getBitmap(String s) {
		Bitmap bitmap;
		synchronized (mBitmapMap) {
			bitmap = (Bitmap) mBitmapMap.get(s);
			if (bitmap != null && bitmap.isRecycled() == true)
				bitmap = null;
		}
		return bitmap;
	}

	public File getFile(String s) {
		return null;
	}

	public InputStream getInputStream(String s) throws IOException {
		return null;
	}

	public void invalidate(String s) {
	}

	public void store(String s, InputStream inputstream) throws Exception {
		if (DEBUG)
			Log.d(TAG, "------------------------------mBitmapMap size is "
					+ mBitmapMap.size() + "--------------------------");
		String s1 = (new StringBuilder("store: ")).append(s).toString();
		if (DEBUG)
			Log.d(TAG, s1);

		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 2;
		Bitmap bitmap = null;
		try {
			bitmap = BitmapFactory.decodeStream(inputstream);
		} catch (OutOfMemoryError e) {
			if (DEBUG)
				Log.e(TAG, s + " is too large22 with " + e);
			bitmap = null;
		}

		if (bitmap == null) {
			if (DEBUG)
				Log.d("MemDiskCache", "bitmap is NULL!!");
			throw new Exception("bitmap is NULL!!");
		}

		synchronized (mBitmapMap) {
			if (mBitmapMap.size() < MAX_FILE_NUM) {
				mBitmapMap.put(s, bitmap);
				mList.add(s);
			} else if (!mList.isEmpty()) {
				if (DEBUG)
					Log.d("MemDiskCache",
							"mBitmapMap is full--------------------------");
				String s2 = (String) mList.remove(0);
				if (mBitmapMap.remove(s2) != null) {
					// if(bitmap11.isRecycled()==false) //如果没有回收
					// bitmap11.recycle();

					mBitmapMap.put(s, bitmap);
					mList.add(s);
				}
			}
		}

		if (DEBUG)
			Log.d(TAG, "store complete: " + s);

	}
}
