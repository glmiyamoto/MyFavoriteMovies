package com.glmiyamoto.myfavoritemovies;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;

import com.glmiyamoto.myfavoritemovies.views.AbstractActivity;
import com.glmiyamoto.myfavoritemovies.views.DialogFragmentType;
import com.glmiyamoto.myfavoritemovies.views.FragmentInteraction;
import com.glmiyamoto.myfavoritemovies.views.FragmentInteraction.OnFragmentInteractionListener;
import com.glmiyamoto.myfavoritemovies.views.FragmentType;

public class SearchActivity extends AbstractActivity implements OnFragmentInteractionListener {

    private Fragment mDetailFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
    }

    @Override
    public void onBackPressed() {
        if (mDetailFragment != null) {
            onFragmentInteraction(FragmentInteraction.CLOSE_ITEM_DETAIL, null);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onFragmentInteraction(@FragmentInteraction.FragmentInteractionType final int type,
                                      final Bundle bundle) {
        switch (type) {
            case FragmentInteraction.SHOW_PROGRESS:
                if (mProgressDialog == null) {
                    mProgressDialog = showDialogFragmentByType(DialogFragmentType.PROGRESS, null);
                }
                break;
            case FragmentInteraction.HIDE_PROGRESS:
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }
                break;
            case FragmentInteraction.SHOW_ITEM_DETAIL:
                if (mDetailFragment == null) {
                    mDetailFragment = addFragmentByType(FragmentType.MOVIE_FRAGMENT, bundle, R.id.fragment);
                }
                break;
            case FragmentInteraction.CLOSE_ITEM_DETAIL:
                if (mDetailFragment != null) {
                    removeFragment(mDetailFragment);
                    mDetailFragment = null;
                }
                break;
            default:
                break;
        }
    }
}
