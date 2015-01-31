package com.tudaidai.tuantrip.types;

import android.graphics.drawable.Drawable;

public class GridInfo {

	private String name;
	private Drawable image;

	public GridInfo(String name, Drawable image) {
		super();
		this.name = name;
		this.image = image;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Drawable getImage() {
		return image;
	}

	public void setImage(Drawable image) {
		this.image = image;
	}

}
