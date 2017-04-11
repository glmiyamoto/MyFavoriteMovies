package com.glmiyamoto.myfavoritemovies.files;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;

import com.glmiyamoto.myfavoritemovies.AppConstants;
import com.glmiyamoto.myfavoritemovies.utils.ImageUtils;

/**
 * Created by glmiyamoto on 4/10/17.
 */

public class ImageFileExplorer extends AbstractFileExplorer {
    private static final String IMAGE_FILE_DIRECTORY = "images";

    public ImageFileExplorer(final Context context) {
        super(context, IMAGE_FILE_DIRECTORY);
    }

    private AsyncImageSaveTask mSaveTask;
    private AsyncImageRemoveTask mRemoveTask;

    public Bitmap readImageFile(final String fileName) {
        final byte[] bytes = readFile(fileName);
        if (bytes != null) {
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        }

        return null;
    }

    public boolean createImageFile(final String fileName, final Bitmap bitmap) {
        return createFile(fileName, ImageUtils.toBytes(bitmap));
    }

    public AsyncImageSaveTask getNewAsyncImageSaveTask(final String fileName,
                                                       final OnAsyncImageSaveListener listener) {
        if (mSaveTask != null && !mSaveTask.isCancelled()) {
            mSaveTask.cancel(true);
        }

        mSaveTask = new AsyncImageSaveTask(fileName, listener);
        return mSaveTask;
    }

    public class AsyncImageSaveTask extends AsyncTask<Bitmap, Integer, Boolean> {
        private final String mFileName;
        private final OnAsyncImageSaveListener mListener;

        private Bitmap mBitmap;

        public AsyncImageSaveTask(final String fileName, final OnAsyncImageSaveListener listener) {
            mFileName = fileName;
            mListener = listener;
        }

        @Override
        protected Boolean doInBackground(final Bitmap... params) {
            mBitmap = params[0];

            return createImageFile(mFileName, mBitmap);
        }

        @Override
        protected void onPostExecute(final Boolean result) {
            super.onPostExecute(result);

            mBitmap.recycle();

            if (mListener != null) {
                final Bundle args = new Bundle();
                args.putString(AppConstants.FILE_NAME_ARG, mFileName);
                mListener.onImageSaved(result, args);
            }
        }
    }

    public AsyncImageRemoveTask getNewAsyncImageRemoveTask(final String fileName,
                                                           final OnAsyncImageRemoveListener listener) {
        if (mRemoveTask != null && !mRemoveTask.isCancelled()) {
            mRemoveTask.cancel(true);
        }

        mRemoveTask = new AsyncImageRemoveTask(fileName, listener);
        return mRemoveTask;
    }

    public class AsyncImageRemoveTask extends AsyncTask<Void, Integer, Boolean> {
        private final String mFileName;
        private final OnAsyncImageRemoveListener mListener;

        public AsyncImageRemoveTask(final String fileName, final OnAsyncImageRemoveListener listener) {
            mFileName = fileName;
            mListener = listener;
        }

        @Override
        protected Boolean doInBackground(final Void... params) {
            return deleteFile(mFileName);
        }

        @Override
        protected void onPostExecute(final Boolean result) {
            super.onPostExecute(result);

            if (mListener != null) {
                final Bundle args = new Bundle();
                args.putString(AppConstants.FILE_NAME_ARG, mFileName);
                mListener.onImageRemoved(result, args);
            }
        }
    }

    public interface OnAsyncImageSaveListener {
        void onImageSaved(boolean success, Bundle args);
    }

    public interface OnAsyncImageRemoveListener {
        void onImageRemoved(boolean success, Bundle args);
    }
}
