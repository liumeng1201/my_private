package com.lm.clientapp.utils;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

//图片操作工具类
public class ImageUtils {

	// zoom 放缩
	public static Bitmap zoomBitmap(Bitmap bitmap, int w, int h) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		Matrix matrix = new Matrix();

		float scaleWidth = w / (float) width;
		float scaleHeight = h / (float) height;
		matrix.postScale(scaleWidth, scaleHeight);

		Bitmap bitmap2 = Bitmap.createBitmap(bitmap, 0, 0, width, height,
				matrix, true);
		return bitmap2;
	}

	// Round Corner Bitmap 获得圆角图片
	public static Bitmap getRoundCornerBitmap(Bitmap bitmap, float roundPX /* 圆角的半径 */) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();

		Bitmap bitmap2 = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap2);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, width, height);
		final RectF rectF = new RectF(rect);

		paint.setColor(color);
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		canvas.drawRoundRect(rectF, roundPX, roundPX, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return bitmap2;
	}

	// Reflect Bitmap 获得带倒影的图片
	public static Bitmap createReflectedBitmap(Bitmap bitmap) {
		// 倒影和图片本身的间距
		final int reflectedGap = 4;
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();

		Matrix matrix = new Matrix();
		matrix.preScale(1, -1);

		Bitmap reflectedImage = Bitmap.createBitmap(bitmap, 0, height / 2,
				width, height / 2, matrix, false);
		Bitmap reflectedBitmap = Bitmap.createBitmap(width,
				(height + height / 2), Config.ARGB_8888);

		Canvas canvas = new Canvas(reflectedBitmap);
		canvas.drawBitmap(bitmap, 0, 0, null);
		Paint defaultPaint = new Paint();
		canvas.drawRect(0, height, width, height + reflectedGap, defaultPaint);
		canvas.drawBitmap(reflectedImage, 0, height + reflectedGap, null);

		Paint paint = new Paint();
		LinearGradient shader = new LinearGradient(0, bitmap.getHeight(), 0,
				reflectedBitmap.getHeight() + reflectedGap, 0x70ffffff,
				0x00ffffff, TileMode.CLAMP);
		paint.setShader(shader);
		paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
		canvas.drawRect(0, height, width, reflectedBitmap.getHeight()
				+ reflectedGap, paint);

		return reflectedBitmap;
	}

	// drawable转bitmap
	public static Bitmap drawable2Bitmap(Drawable drawable) {
		int width = drawable.getIntrinsicHeight();
		int height = drawable.getIntrinsicHeight();

		Bitmap bitmap = Bitmap.createBitmap(width, height, drawable
				.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
				: Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, width, height);
		drawable.draw(canvas);

		return bitmap;
	}

	// bitmap转drawable
	public static Drawable bitmapToDrawable(Bitmap bitmap) {
		// BitmapDrawable 是 Drawable的派生类
		return new BitmapDrawable(bitmap);
	}
}
