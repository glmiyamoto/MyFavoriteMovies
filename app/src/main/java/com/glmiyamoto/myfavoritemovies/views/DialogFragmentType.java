package com.glmiyamoto.myfavoritemovies.views;

import android.support.v4.app.DialogFragment;

import com.glmiyamoto.myfavoritemovies.AppConstants;
import com.glmiyamoto.myfavoritemovies.dialogs.ProgressDialog;

/**
 * Created by glmiyamoto on 4/4/17.
 */

public enum DialogFragmentType {
    PROGRESS(AppConstants.PROGRESS_DIALOG_TAG, ProgressDialog.class);

    private String mTag;
    private Class<? extends DialogFragment> mDialogFragClass;

    private DialogFragmentType(final String tag,
                                final Class<? extends DialogFragment> dialogFragClass) {
        this.mTag = tag;
        this.mDialogFragClass = dialogFragClass;
    }

    public String getTag() {
        return mTag;
    }

    public Class<? extends DialogFragment> getFragmentClass() {
        return mDialogFragClass;
    }

    public static DialogFragmentType getTypeByTag(final String tag) {
        for (final DialogFragmentType fragmentType : DialogFragmentType.values()) {
            if (fragmentType.getTag().equalsIgnoreCase(tag)) {
                return fragmentType;
            }
        }

        return null;
    }
}
