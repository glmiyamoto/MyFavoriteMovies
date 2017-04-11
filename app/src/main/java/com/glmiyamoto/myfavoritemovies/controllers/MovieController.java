package com.glmiyamoto.myfavoritemovies.controllers;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.glmiyamoto.myfavoritemovies.datas.DbMovie;
import com.glmiyamoto.myfavoritemovies.models.Movie;

import java.util.List;

/**
 * Created by glmiyamoto on 4/6/17.
 */

public class MovieController {
    private static final MovieController mInstance = new MovieController();

    public static MovieController getInstance() {
        return mInstance;
    }

    private DbMovie mDbMovie;

    private MovieController() {
    }

    public void init(final Context context) {
        if (mDbMovie == null) {
            mDbMovie = new DbMovie(context);
        }
    }

    public boolean insert(final Movie movie) {
        // Check if movie is already registered
        if (mDbMovie.getMovieById(movie.getId()) == null) {
            // Insert movie
            return mDbMovie.insert(movie) > 0;
        }

        return false;
    }

    public boolean delete(final Movie movie) {
        return mDbMovie.delete(movie.getId()) > 0;
    }

    public List<Movie> getMovies() {
        return mDbMovie.getMovies();
    }

    public Movie getMovieById(final String id) {
        return mDbMovie.getMovieById(id);
    }

    public void requestMoviePoster(final Movie movie, final OnMoviePosterReceivedListener listener) {
        final RequestMoviePosterTask task = new RequestMoviePosterTask(listener);
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Movie[] { movie });
    }

    public class RequestMoviePosterTask extends AsyncTask<Movie, Void, Bitmap> {

        private final OnMoviePosterReceivedListener mListener;

        public RequestMoviePosterTask(final OnMoviePosterReceivedListener listener) {
            mListener = listener;
        }

        @Override
        protected Bitmap doInBackground(final Movie... movies) {
            if (movies != null && movies.length > 0) {
                final Movie movie = movies[0];
                final String fileName = movie.getId() + ".jpg";
                if (ImageController.getInstance().hasFile(fileName)) {
                    return ImageController.getInstance().readImageFile(fileName);
                }

                return ImageController.getInstance().requestBitmapByUrl(movie.getPoster());
            }

            return null;
        }

        @Override
        protected void onPostExecute(final Bitmap bitmap) {
            super.onPostExecute(bitmap);

            if (mListener != null) {
                mListener.onMoviePosterReceived(bitmap);
            }
        }
    }

    public interface OnMoviePosterReceivedListener {
        void onMoviePosterReceived(Bitmap bitmap);
    }
}
