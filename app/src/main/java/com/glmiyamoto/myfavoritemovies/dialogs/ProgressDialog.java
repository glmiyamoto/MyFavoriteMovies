package com.glmiyamoto.myfavoritemovies.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.glmiyamoto.myfavoritemovies.R;

/**
 * Created by glmiyamoto on 4/4/17.
 */

public class ProgressDialog extends DialogFragment {

    public ProgressDialog() {
        // Required empty public constructor
    }

    /**
     * Create a new instance of this fragment.
     *
     * @return A new instance of fragment SearchFragment.
     */
    public static ProgressDialog newInstance() {
        return new ProgressDialog();
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstState) {

        final View view = inflater.inflate(R.layout.dialog_progress, container);

        final Dialog dialog = getDialog();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setLayout(100, 100);
        dialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent);

        setCancelable(false);

        return view;
    }
}
