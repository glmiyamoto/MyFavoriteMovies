package com.glmiyamoto.myfavoritemovies.views.fragments;

import android.support.v4.app.Fragment;

import com.glmiyamoto.myfavoritemovies.AppConstants;

/**
 * Created by glmiyamoto on 4/4/17.
 */

public enum FragmentType {
    MOVIE_FRAGMENT(AppConstants.MOVIE_FRAGMENT_TAG, MovieFragment.class);

    private String mTag;
    private Class<? extends Fragment> mFragmentClass;

    private FragmentType(final String tag,
                         final Class<? extends Fragment> fragmentClass) {
        this.mTag = tag;
        this.mFragmentClass = fragmentClass;
    }

    public String getTag() {
        return mTag;
    }

    public Class<? extends Fragment> getFragmentClass() {
        return mFragmentClass;
    }

    public static FragmentType getTypeByTag(final String tag) {
        for (final FragmentType fragmentType : FragmentType.values()) {
            if (fragmentType.getTag().equalsIgnoreCase(tag)) {
                return fragmentType;
            }
        }

        return null;
    }
}
