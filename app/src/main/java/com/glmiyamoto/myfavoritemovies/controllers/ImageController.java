package com.glmiyamoto.myfavoritemovies.controllers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;

import com.glmiyamoto.myfavoritemovies.files.ImageFileExplorer;
import com.glmiyamoto.myfavoritemovies.files.ImageFileExplorer.AsyncImageSaveTask;
import com.glmiyamoto.myfavoritemovies.files.ImageFileExplorer.AsyncImageRemoveTask;
import com.glmiyamoto.myfavoritemovies.files.ImageFileExplorer.OnAsyncImageSaveListener;
import com.glmiyamoto.myfavoritemovies.files.ImageFileExplorer.OnAsyncImageRemoveListener;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by glmiyamoto on 4/5/17.
 */

public class ImageController {
    private static final ImageController mInstance = new ImageController();

    public static ImageController getInstance() {
        return mInstance;
    }

    private ImageFileExplorer mImageFileExplorer;

    private ImageController() {
    }

    public void init(final Context context) {
        mImageFileExplorer = new ImageFileExplorer(context);
    }

    public boolean hasFile(final String fileName) {
        return mImageFileExplorer.hasFile(fileName);
    }

    public Bitmap readImageFile(final String fileName) {
        return mImageFileExplorer.readImageFile(fileName);
    }

    public void saveImageFile(final String fileName, final Bitmap bitmap,
                              final OnImageSavedListener listener) {
        final AsyncImageSaveTask saveTask = mImageFileExplorer.getNewAsyncImageSaveTask(fileName,
                new OnAsyncImageSaveListener() {
            @Override
            public void onImageSaved(final boolean success, final Bundle args) {
                if (listener != null) {
                    listener.onImageSaved(success, args);
                }
            }
        });
        saveTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, bitmap);
    }

    public void removeImageFile(final String fileName, final OnImageRemovedListener listener) {
        final AsyncImageRemoveTask removeTask = mImageFileExplorer.getNewAsyncImageRemoveTask(fileName,
                new OnAsyncImageRemoveListener() {
                    @Override
                    public void onImageRemoved(final boolean success, final Bundle args) {
                        if (listener != null) {
                            listener.onImageRemoved(success, args);
                        }
                    }
                });
        removeTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null);
    }

    /**
     * Request bitmap (image) by URL
     *
     * @param url URL path
     * @return bitmap (image)
     */
    public Bitmap requestBitmapByUrl(final String url) {
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
    public Bitmap requestBitmapByUrl(final URL url) {
        try {
            final InputStream is = new BufferedInputStream(url.openStream());
            return BitmapFactory.decodeStream(is);
        } catch (Exception e) {
        }

        return null;
    }

    public interface OnImageSavedListener {
        void onImageSaved(boolean success, Bundle args);
    }

    public interface OnImageRemovedListener {
        void onImageRemoved(boolean success, Bundle args);
    }
}
