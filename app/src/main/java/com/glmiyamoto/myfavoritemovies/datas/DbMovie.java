package com.glmiyamoto.myfavoritemovies.datas;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.glmiyamoto.myfavoritemovies.models.Movie;

public class DbMovie {
	private static final int ZERO = 0;

	// TABLE
	private static final String TABLE_NAME = "movie";

    // FIELDS
    private static final String FIELD_ID = "_id";
    private static final String FIELD_TITLE = "title";
    private static final String FIELD_YEAR = "year";
    private static final String FIELD_POSTER = "poster";
    private static final String FIELD_GENRE = "genre";
    private static final String FIELD_DIRECTOR = "director";
    private static final String FIELD_WRITER = "writer";
    private static final String FIELD_ACTORS = "actors";
    private static final String FIELD_PLOT = "plot";
    private static final String FIELD_LANGUAGE = "language";
    private static final String FIELD_COUNTRY = "country";
    private static final String FIELD_AWARDS = "awards";

    // COLUMNS
    private static final String[] COLUMNS = new String[] { FIELD_ID, FIELD_TITLE, FIELD_YEAR,
            FIELD_POSTER, FIELD_GENRE, FIELD_DIRECTOR, FIELD_WRITER, FIELD_ACTORS, FIELD_PLOT,
            FIELD_LANGUAGE, FIELD_COUNTRY, FIELD_AWARDS
    };

    // SQL
    public static final String CREATE_TABLE_SQL = // Create table SQL
            "CREATE TABLE " + TABLE_NAME + " (" + // Table name
                    FIELD_ID + " TEXT NOT NULL PRIMARY KEY, " +
                    FIELD_TITLE + " TEXT NOT NULL, " +
                    FIELD_YEAR + " TEXT, " +
                    FIELD_POSTER + " TEXT, " +
                    FIELD_GENRE + " TEXT, " +
                    FIELD_DIRECTOR + " TEXT, " +
                    FIELD_WRITER + " TEXT, " +
                    FIELD_ACTORS + " TEXT, " +
                    FIELD_PLOT + " TEXT, " +
                    FIELD_LANGUAGE + " TEXT, " +
                    FIELD_COUNTRY + " TEXT, " +
                    FIELD_AWARDS + " TEXT);";

	private transient final SQLiteDatabase mDatabase;

	public DbMovie(final Context context) {
		final DatabaseAccess databaseAccess = new DatabaseAccess(context);
		mDatabase = databaseAccess.getDatabase();
	}

    /**
     * Insert a movie object
     *
     * @param movie
     * @return
     */
	public long insert(final Movie movie) {
		final ContentValues values = new ContentValues();
		putValues(values, movie);

		try {
			return mDatabase.insert(TABLE_NAME, "", values);
		} catch (SQLiteException e) {
			return ZERO;
		}
	}

    /**
     * Delete a movie object
     *
     * @param id
     * @return
     */
	public int delete(final String id) {
		final String where = FIELD_ID + "=?";
		final String _id = String.valueOf(id);
		final String[] whereArgs = new String[]{_id};
		return mDatabase.delete(TABLE_NAME, where, whereArgs);
	}

    /**
     * Get all registered movie objects
     *
     * @return
     */
	public List<Movie> getMovies() {
        final Cursor cursor = mDatabase.query(true, TABLE_NAME, COLUMNS,
                null, null, null, null, FIELD_TITLE, null);
		final List<Movie> movieList = new ArrayList<>();

		if (cursor != null) {
			if (cursor.moveToFirst()) {
				do {
					final Movie movie = new Movie();
					fillByCursor(movie, cursor);
					movieList.add(movie);
				} while (cursor.moveToNext());
			}

			cursor.close();
		}
		return movieList;
	}

    /**
     * Get movie object by id
     *
     * @param id Movie id
     * @return
     */
	public Movie getMovieById(final String id) {
		Movie movieAux = null;
		final Cursor cursor = mDatabase.query(true, TABLE_NAME, COLUMNS,
                FIELD_ID + "=?", new String[] { id }, null, null, null, null);

		if (cursor.getCount() > ZERO) {
			cursor.moveToFirst();
			movieAux = new Movie();
			fillByCursor(movieAux, cursor);
		}
		cursor.close();
		return movieAux;
	}

	public void putValues(final ContentValues values, final Movie movie) {
		values.put(FIELD_ID, movie.getId());
		values.put(FIELD_TITLE, movie.getTitle());
        values.put(FIELD_YEAR, movie.getYear());
        values.put(FIELD_POSTER, movie.getPoster());
        values.put(FIELD_GENRE, movie.getGenre());
        values.put(FIELD_DIRECTOR, movie.getDirector());
        values.put(FIELD_WRITER, movie.getWriter());
        values.put(FIELD_ACTORS, movie.getActors());
        values.put(FIELD_PLOT, movie.getPlot());
        values.put(FIELD_LANGUAGE, movie.getLanguage());
        values.put(FIELD_COUNTRY, movie.getCountry());
        values.put(FIELD_AWARDS, movie.getAwards());
	}

	public void fillByCursor(final Movie movie, final Cursor cursor) {
		movie.setId(cursor.getString(cursor.getColumnIndex(FIELD_ID)));
		movie.setTitle(cursor.getString(cursor.getColumnIndex(FIELD_TITLE)));
        movie.setYear(cursor.getString(cursor.getColumnIndex(FIELD_YEAR)));
        movie.setPoster(cursor.getString(cursor.getColumnIndex(FIELD_POSTER)));
        movie.setGenre(cursor.getString(cursor.getColumnIndex(FIELD_GENRE)));
        movie.setDirector(cursor.getString(cursor.getColumnIndex(FIELD_DIRECTOR)));
        movie.setWriter(cursor.getString(cursor.getColumnIndex(FIELD_WRITER)));
        movie.setActors(cursor.getString(cursor.getColumnIndex(FIELD_ACTORS)));
        movie.setPlot(cursor.getString(cursor.getColumnIndex(FIELD_PLOT)));
        movie.setLanguage(cursor.getString(cursor.getColumnIndex(FIELD_LANGUAGE)));
        movie.setCountry(cursor.getString(cursor.getColumnIndex(FIELD_COUNTRY)));
        movie.setAwards(cursor.getString(cursor.getColumnIndex(FIELD_AWARDS)));
	}
}
