package com.glmiyamoto.myfavoritemovies.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by glmiyamoto on 4/12/17.
 */

public final class NetworkUtils {

    private NetworkUtils() {
        // Avoid instance
    }

    public static boolean hasNetwork(final Context context) {
        final ConnectivityManager manager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        final NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        if ((networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected())) {
            return true;
        }

        return false;
    }
}
