package com.glmiyamoto.myfavoritemovies.webservices;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.glmiyamoto.myfavoritemovies.VolleyApplication;
import com.glmiyamoto.myfavoritemovies.models.Movie;
import com.glmiyamoto.myfavoritemovies.models.OmdbItemObject;
import com.glmiyamoto.myfavoritemovies.models.OmdbObject;

public final class OmdbApiProvider {
	private static final String SERVICE_URL = "http://www.omdbapi.com";
	private static final String URL_SEARCH_BY_NAME = SERVICE_URL + "/?s=";
	private static final String URL_GET_BY_ID = SERVICE_URL + "/?plot=full&i=";

	private static OmdbApiProvider mInstance;

	private final VolleyApplication mServiceCtrl;

	private OmdbApiProvider() {
		mServiceCtrl = VolleyApplication.getInstance();
	}

	public static OmdbApiProvider getInstance() {
		synchronized (OmdbApiProvider.class) {
			if (mInstance == null) {
				mInstance = new OmdbApiProvider();
			}

			return mInstance;
		}
	}

	public void searchMoviesByName(final String name, final Listener<OmdbObject> listener,
								   final ErrorListener errorListener) {
		//final Type type = new TypeToken<List<Movie>>() {}.getType();
		final GsonRequest<OmdbObject> req = new GsonRequest<>(
				URL_SEARCH_BY_NAME + name, OmdbObject.class, null, listener, errorListener);

		// Add the request to the RequestQueue.
		mServiceCtrl.addToRequestQueue(req);
	}

	public void getMovieById(final String id, final Listener<OmdbItemObject> listener,
							 final ErrorListener errorListener) {
		final GsonRequest<OmdbItemObject> req = new GsonRequest<>(
				URL_GET_BY_ID + id, OmdbItemObject.class, null, listener, errorListener);

		// Add the request to the RequestQueue.
		mServiceCtrl.addToRequestQueue(req);
	}
}