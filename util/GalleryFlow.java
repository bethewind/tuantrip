package com.tudaidai.tuantrip.util;

import android.content.Context;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Transformation;
import android.widget.Gallery;
import android.widget.ImageView;

public class GalleryFlow extends Gallery {

	/* 图形的照相机ImageViews用于转换矩阵的 */

	private Camera mCamera = new Camera();

	/** 这个的最大角ImageView将旋转 */

	private int mMaxRotationAngle = 60;

	/** 这个研究中心的最大放大效果 */

	private int mMaxZoom = -120;

	/* Coverflow的中心 */

	private int mCoveflowCenter;

	public GalleryFlow(Context context) {

		super(context);

		this.setStaticTransformationsEnabled(true);

	}

	public GalleryFlow(Context context, AttributeSet attrs) {

		super(context, attrs);

		this.setStaticTransformationsEnabled(true);

	}

	public GalleryFlow(Context context, AttributeSet attrs, int defStyle) {

		super(context, attrs, defStyle);

		this.setStaticTransformationsEnabled(true);

	}

	/**
	 * 
	 * 旋转角度得到最大的形象
	 */

	public int getMaxRotationAngle() {

		return mMaxRotationAngle;

	}

	/**
	 * 
	 * 设置它的最大旋转角度的每一个图像
	 */

	public void setMaxRotationAngle(int maxRotationAngle) {

		mMaxRotationAngle = maxRotationAngle;

	}

	public int getMaxZoom() {

		return mMaxZoom;

	}

	public void setMaxZoom(int maxZoom) {

		mMaxZoom = maxZoom;

	}

	private int getCenterOfCoverflow() {

		// Log.e("CoverFlow Width+Height", getWidth() + "*" + getHeight());

		return (getWidth() - getPaddingLeft() - getPaddingRight()) / 2

		+ getPaddingLeft();

	}

	private static int getCenterOfView(View view) {

		/*
		 * Log.e("ChildView Width+Height", view.getWidth() + "*"
		 * 
		 * + view.getHeight());
		 */

		return view.getLeft() + view.getWidth() / 2;

	}

	protected boolean getChildStaticTransformation(View child, Transformation t) {

		final int childCenter = getCenterOfView(child);

		final int childWidth = child.getWidth();

		int rotationAngle = 0;

		t.clear();

		t.setTransformationType(Transformation.TYPE_MATRIX);

		if (childCenter == mCoveflowCenter) {

			transformImageBitmap((ImageView) child, t, 0);

		} else {

			rotationAngle = (int) (((float) (mCoveflowCenter - childCenter) / childWidth) * mMaxRotationAngle);

			if (Math.abs(rotationAngle) > mMaxRotationAngle) {

				rotationAngle = (rotationAngle < 0) ? -mMaxRotationAngle

				: mMaxRotationAngle;

			}

			transformImageBitmap((ImageView) child, t, rotationAngle);

		}

		return true;

	}

	/**
	 * 
	 * 这就是所谓的在大小的布局时,这一观点已经发生了改变。如果
	 * 
	 * 
	 * 你只是添加到视图层次,有人叫你旧的观念
	 * 
	 * 值为0.
	 * 
	 * 
	 * 
	 * @param w
	 * 
	 *            Current width of this view.
	 * 
	 * @param h
	 * 
	 *            Current height of this view.
	 * 
	 * @param oldw
	 * 
	 *            Old width of this view.
	 * 
	 * @param oldh
	 * 
	 *            Old height of this view.
	 */

	protected void onSizeChanged(int w, int h, int oldw, int oldh) {

		mCoveflowCenter = getCenterOfCoverflow();

		super.onSizeChanged(w, h, oldw, oldh);

	}

	/**
	 * 
	 * Transform the Image Bitmap by the Angle passed
	 * 
	 * 
	 * 
	 * @param imageView
	 * 
	 *            ImageView the ImageView whose bitmap we want to rotate
	 * 
	 * @param t
	 * 
	 *            transformation
	 * 
	 * @param rotationAngle
	 * 
	 *            以旋转角度的位图
	 */

	private void transformImageBitmap(ImageView child, Transformation t,

	int rotationAngle) {

		mCamera.save();

		final Matrix imageMatrix = t.getMatrix();

		final int imageHeight = child.getLayoutParams().height;

		final int imageWidth = child.getLayoutParams().width;

		final int rotation = Math.abs(rotationAngle);

		// 在Z轴上正向移动camera的视角，实际效果为放大图片。

		// 如果在Y轴上移动，则图片上下移动；X轴上对应图片左右移动。

		mCamera.translate(0.0f, 0.0f, 100.0f);

		// 如视图的角度更少,放大

		if (rotation < mMaxRotationAngle) {

			float zoomAmount = (float) (mMaxZoom + (rotation * 1.5));

			mCamera.translate(0.0f, 0.0f, zoomAmount);

		}

		// 在Y轴上旋转，对应图片竖向向里翻转。

		// 如果在X轴上旋转，则对应图片横向向里翻转。

		mCamera.rotateY(rotationAngle);

		mCamera.getMatrix(imageMatrix);

		// Preconcats matrix相当于右乘矩阵，Postconcats matrix相当于左乘矩阵。

		imageMatrix.preTranslate(-(imageWidth / 2), -(imageHeight / 2));

		imageMatrix.postTranslate((imageWidth / 2), (imageHeight / 2));

		mCamera.restore();

	}

}
