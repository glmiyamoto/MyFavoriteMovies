package com.glmiyamoto.myfavoritemovies.utils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;

/**
 * Created by glmiyamoto on 4/4/17.
 */

public final class ImageUtils {

	private ImageUtils() {
		// Avoid instance
	}

	/**
	 * Decode the sample bitmap
	 *
	 * @param src
	 * @param dstWidth
	 * @param dstHeight
	 * @return
	 */
	public static Bitmap decodeSampledBitmap(final Bitmap src,
											 final int dstWidth, final int dstHeight) {
		final ByteArrayOutputStream stream = new ByteArrayOutputStream();
		src.compress(Bitmap.CompressFormat.PNG, 100, stream);
		return decodeSampledBitmap(stream.toByteArray(), dstWidth, dstHeight);
	}

	/**
	 * Decode the sample bitmap
	 *
	 * @param src
	 * @param dstWidth
	 * @param dstHeight
	 * @return
	 */
	@TargetApi(Build.VERSION_CODES.GINGERBREAD_MR1)
	public static Bitmap decodeSampledBitmap(final byte[] src,
											 final int dstWidth, final int dstHeight) {
		final BitmapFactory.Options realOptions = new BitmapFactory.Options();
		realOptions.inJustDecodeBounds = true;
		BitmapFactory.decodeByteArray(src, 0, src.length, realOptions);

		final BitmapFactory.Options scalatedOptions = new BitmapFactory.Options();
		scalatedOptions.inSampleSize = calculateInSampleSize(
				realOptions.outWidth, realOptions.outHeight, dstWidth,
				dstHeight);
		scalatedOptions.inScaled = true;
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD_MR1) {
			scalatedOptions.inPreferQualityOverSpeed = false;
		}
		scalatedOptions.inPurgeable = true;
		scalatedOptions.inTempStorage = new byte[16 * 1024];
		scalatedOptions.inDither = true;
		scalatedOptions.inPreferredConfig = Config.RGB_565; // Bitmap.Config.ARGB_8888;

		return BitmapFactory.decodeByteArray(src, 0, src.length,
				scalatedOptions);
	}

	/**
	 * Calculate the sample size
	 *
	 * @param srcWidth
	 * @param srcHeight
	 * @param dstWidth
	 * @param dstHeight
	 * @return
	 */
	private static int calculateInSampleSize(final int srcWidth,
											 final int srcHeight, final int dstWidth, final int dstHeight) {
		int scale = 1;
		while (srcWidth / scale / 2 >= dstWidth
				&& srcHeight / scale / 2 >= dstHeight) {
			scale *= 2;
		}
		return scale;
	}

	public static byte[] toBytes(final Bitmap bitmap) {
		final ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, arrayOutputStream);
		return arrayOutputStream.toByteArray();
	}

	public static Bitmap drawableToBitmap(final Drawable drawable) {
		if (drawable instanceof BitmapDrawable) {
			return ((BitmapDrawable) drawable).getBitmap();
		}

		final Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
				drawable.getIntrinsicHeight(), Config.ARGB_8888);
		final Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
		drawable.draw(canvas);

		return bitmap;
	}
}