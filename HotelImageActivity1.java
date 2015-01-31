package com.tudaidai.tuantrip;


import java.lang.reflect.Field;
import java.util.ArrayList;

import com.tudaidai.tuantrip.util.RemoteResourceManager;
import com.tudaidai.tuantrip.widget.ImageAdapter1;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.AdapterView.OnItemClickListener;

public class HotelImageActivity1 extends Activity {
	private Gallery mGallery;
	ImageAdapter1 imageAdapter;
	 public static final String IMAGES = "images";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hotel_image_activity1);
        
        String[] images = getIntent().getStringArrayExtra(IMAGES);
        ArrayList<String> iArrayList = new ArrayList<String>(images.length);
        for (String img : images) {
        	iArrayList.add(img);
		}
        mGallery = (Gallery)findViewById(R.id.gallery);
        RemoteResourceManager localRemoteResourceManager = ((TuanTrip)getApplication()).getRemoteResourceManager();
        imageAdapter = new ImageAdapter1(this, localRemoteResourceManager);
        imageAdapter.setGroup(iArrayList);
        mGallery.setAdapter(imageAdapter);
        mGallery.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView parent, View v, int position, long id) {
            	String title = (String)imageAdapter.getItem(position);
            	try {
            		title = title.substring(title.lastIndexOf("/")+1,title.lastIndexOf("."));
				} catch (Exception e) {
					// TODO: handle exception
				}
            	
            	HotelImageActivity1.this.setTitle(title);
            }
        });
    }
}