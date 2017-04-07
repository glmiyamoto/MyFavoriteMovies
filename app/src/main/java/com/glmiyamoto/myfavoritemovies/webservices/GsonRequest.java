package com.glmiyamoto.myfavoritemovies.webservices;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.Map;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class GsonRequest<T> extends Request<T> {
	private final Type type;
	private final Map<String, String> params;
	private final Listener<T> listener;

	/**
	 * Make a GET request and return a parsed object from JSON.
	 *
	 * @param url
	 *            URL of the request to make
	 * @param type
	 *            Relevant class type object, for Gson's reflection
	 * @param params
	 *            Map of request headers
	 */
	public GsonRequest(final String url, final Type type, final Map<String, String> params,
					   final Listener<T> listener, final ErrorListener errorListener) {
		super(Method.GET, url, errorListener);
		this.type = type;
		this.params = params;
		this.listener = listener;
	}

	public GsonRequest(final int method, final String url, final Type type, final Map<String, String> params,
					   final Listener<T> listener, final ErrorListener errorListener) {
		super(method, url, errorListener);
		this.type = type;
		this.params = params;
		this.listener = listener;
	}

	@Override
	public Map<String, String> getParams() throws AuthFailureError {
		return params == null ? super.getParams() : params;
	}

	@Override
	protected void deliverResponse(final T response) {
		listener.onResponse(response);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Response<T> parseNetworkResponse(final NetworkResponse response) {
		try {
			final String json = new String(response.data,
					HttpHeaderParser.parseCharset(response.headers));
			return (Response<T>) Response.success(new Gson().fromJson(json, type),
					HttpHeaderParser.parseCacheHeaders(response));
		} catch (UnsupportedEncodingException e) {
			return Response.error(new ParseError(e));
		} catch (JsonSyntaxException e) {
			return Response.error(new ParseError(e));
		}
	}
}