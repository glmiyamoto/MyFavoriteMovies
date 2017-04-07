package com.glmiyamoto.myfavoritemovies.utils;

import android.app.Activity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by glmiyamoto on 4/4/17.
 */

public final class DeviceUtils {
    private DeviceUtils() {
        // Avoid instance
    }

    public static void hideKeyboard(final Activity activity) {
        final InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);

        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();

        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }

        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
