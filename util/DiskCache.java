package com.tudaidai.tuantrip.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import android.graphics.Bitmap;

public interface DiskCache {

	public boolean exists(String key);

	public File getFile(String key);

	public InputStream getInputStream(String key) throws IOException;

	public void store(String key, InputStream is) throws Exception;

	public void invalidate(String key);

	public void cleanup();

	public void clear();

	public Bitmap getBitmap(String s);

}
