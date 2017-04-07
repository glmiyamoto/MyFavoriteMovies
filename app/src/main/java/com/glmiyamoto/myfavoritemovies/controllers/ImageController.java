package com.glmiyamoto.myfavoritemovies.controllers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.glmiyamoto.myfavoritemovies.utils.ImageUtils;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by glmiyamoto on 4/5/17.
 */

public class ImageController {
    private static final ImageController mInstance = new ImageController();

    public static ImageController getInstance() {
        return mInstance;
    }

    private ImageController() {
    }

    public void requestImageByUrl(final String url, final OnReceivedImageListener listener) {
        final RequestImageTask task = new RequestImageTask(listener);
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new String[] { url });
    }

    /**
     * Request bitmap (image) by URL
     *
     * @param url URL path
     * @return bitmap (image)
     */
    private Bitmap requestBitmapByUrl(final String url) {
        try {
            return requestBitmapByUrl(new URL(url));
        } catch (Exception e) {
        }

        return null;
    }

    /**
     * Request bitmap (image) by URL
     *
     * @param url URL path
     * @return bitmap (image)
     */
    private Bitmap requestBitmapByUrl(final URL url) {
        try {
            final InputStream is = new BufferedInputStream(url.openStream());
            return BitmapFactory.decodeStream(is);
        } catch (Exception e) {
        }

        return null;
    }

    public class RequestImageTask extends AsyncTask<String, Void, Bitmap> {

        private final OnReceivedImageListener mListener;

        public RequestImageTask(final OnReceivedImageListener listener) {
            mListener = listener;
        }

        @Override
        protected Bitmap doInBackground(final String... urls) {
            if (urls != null && urls.length > 0) {
                final String url = urls[0];
                return requestBitmapByUrl(url);
            }

            return null;
        }

        @Override
        protected void onPostExecute(final Bitmap bitmap) {
            super.onPostExecute(bitmap);

            if (mListener != null) {
                mListener.onReceivedImage(bitmap);
            }
        }
    }

    public interface OnReceivedImageListener {
        void onReceivedImage(Bitmap bitmap);
    }
}
