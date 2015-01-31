package com.tudaidai.tuantrip.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.tudaidai.tuantrip.util.GalleryFlow;

public class ImageAdapter extends BaseAdapter {

	int mGalleryItemBackground;

	private Context mContext;

	private int[] mImageIds;

	private ImageView[] mImages;

	public ImageAdapter(Context c, int[] ImageIds) {

		mContext = c;

		mImageIds = ImageIds;

		mImages = new ImageView[mImageIds.length];

	}

	public boolean createReflectedImages() {

		// 我们想要的差距之间的省思与原始图像

		final int reflectionGap = 4;

		int index = 0;

		for (int imageId : mImageIds) {

			Bitmap originalImage = BitmapFactory.decodeResource(mContext

			.getResources(), imageId);

			int width = originalImage.getWidth();

			int height = originalImage.getHeight();

			// 但这并不会将规模翻在Y轴上

			Matrix matrix = new Matrix();

			matrix.preScale(1, -1);

			// 创建一个位图与翻转矩阵,适用于这。

			// 我们只需要底部一半的形象

			Bitmap reflectionImage = Bitmap.createBitmap(originalImage, 0,

			height / 2, width, height / 2, matrix, false);

			// 创建一个新的位图以及相同宽度但更高的格格不入

			// 反射

			Bitmap bitmapWithReflection = Bitmap.createBitmap(width,

			(height + height / 2), Config.ARGB_8888);

			Canvas canvas = new Canvas(bitmapWithReflection);

			// 画出原始图像
			canvas.drawBitmap(originalImage, 0, 0, null);

			// 画在缝

			Paint deafaultPaint = new Paint();

			canvas.drawRect(0, height, width, height + reflectionGap,

			deafaultPaint);

			canvas.drawBitmap(reflectionImage, 0, height + reflectionGap, null);

			Paint paint = new Paint();

			LinearGradient shader = new LinearGradient(0, originalImage

			.getHeight(), 0, bitmapWithReflection.getHeight()

			+ reflectionGap, 0x70ffffff, 0x00ffffff, TileMode.CLAMP);

			paint.setShader(shader);

			paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));

			// 画一个长方形使用油漆与我们的线性梯度

			canvas.drawRect(0, height, width, bitmapWithReflection.getHeight()

			+ reflectionGap, paint);

			// 解决图片的锯齿现象

			BitmapDrawable bd = new BitmapDrawable(bitmapWithReflection);

			bd.setAntiAlias(true);

			ImageView imageView = new ImageView(mContext);

			// imageView.setImageBitmap(bitmapWithReflection);

			imageView.setImageDrawable(bd);

			imageView.setLayoutParams(new GalleryFlow.LayoutParams(160, 240));

			// imageView.setScaleType(ScaleType.MATRIX);

			mImages[index++] = imageView;

		}

		return true;

	}

	public int getCount() {

		return mImageIds.length;

	}

	public Object getItem(int position) {

		return position;

	}

	public long getItemId(int position) {

		return position;

	}

	public View getView(int position, View convertView, ViewGroup parent) {

		return mImages[position];

	}

	public float getScale(boolean focused, int offset) {

		/* Formula: 1 / (2 ^ offset) */

		return Math.max(0, 1.0f / (float) Math.pow(2, Math.abs(offset)));

	}

}
