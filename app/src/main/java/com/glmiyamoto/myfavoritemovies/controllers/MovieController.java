package com.glmiyamoto.myfavoritemovies.controllers;

import android.content.Context;

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
}
