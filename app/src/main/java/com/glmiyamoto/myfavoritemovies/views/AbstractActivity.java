package com.glmiyamoto.myfavoritemovies.views;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.glmiyamoto.myfavoritemovies.views.dialogs.DialogFragmentType;
import com.glmiyamoto.myfavoritemovies.views.fragments.FragmentType;

/**
 * Created by glmiyamoto on 4/4/17.
 */

public abstract class AbstractActivity extends AppCompatActivity {

    protected DialogFragment mProgressDialog;

    public Fragment addFragmentByType(final FragmentType fragmentsType,
                                      final Bundle bundle, final int containerViewId) {
        Fragment fragment = null;
        try {
            fragment = fragmentsType.getFragmentClass().newInstance();
            if (bundle != null) {
                fragment.setArguments(bundle);
            }
            getSupportFragmentManager().beginTransaction()
                    .add(containerViewId, fragment, fragmentsType.getTag())
                    .commit();

        } catch (final InstantiationException error) {
            // Do nothing
        } catch (final IllegalAccessException error) {
            // Do nothing
        }

        return fragment;
    }

    public void removeFragment(final Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().remove(fragment)
                    .commit();
        }
    }

    public DialogFragment showDialogFragmentByType(
            final DialogFragmentType dialogType, final Bundle bundle) {
        DialogFragment dialogFragment = null;

        try {
            dialogFragment = dialogType.getFragmentClass().newInstance();
            if (bundle != null) {
                dialogFragment.setArguments(bundle);
            }
            dialogFragment.show(getSupportFragmentManager(), dialogType.getTag());

        } catch (final InstantiationException error) {
            // Do nothing
        } catch (final IllegalAccessException error) {
            // Do nothing
        }

        return dialogFragment;
    }
}
