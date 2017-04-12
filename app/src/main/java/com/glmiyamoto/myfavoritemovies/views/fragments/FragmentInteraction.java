package com.glmiyamoto.myfavoritemovies.views.fragments;

import android.os.Bundle;
import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by glmiyamoto on 4/4/17.
 */

public abstract class FragmentInteraction {
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({
            SHOW_PROGRESS,
            HIDE_PROGRESS,
            SHOW_ITEM_DETAIL,
            CLOSE_ITEM_DETAIL
    })
    public @interface FragmentInteractionType {
    }

    // Declare the constants
    public static final int SHOW_PROGRESS = 0;
    public static final int HIDE_PROGRESS = 1;
    public static final int SHOW_ITEM_DETAIL = 2;
    public static final int CLOSE_ITEM_DETAIL = 3;

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(@FragmentInteraction.FragmentInteractionType int type, Bundle bundle);
    }
}
