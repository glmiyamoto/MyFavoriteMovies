package com.glmiyamoto.myfavoritemovies.views.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.glmiyamoto.myfavoritemovies.AppConstants;
import com.glmiyamoto.myfavoritemovies.R;
import com.glmiyamoto.myfavoritemovies.models.Movie;
import com.glmiyamoto.myfavoritemovies.models.OmdbObject;
import com.glmiyamoto.myfavoritemovies.models.OmdbItemObject;
import com.glmiyamoto.myfavoritemovies.utils.DeviceUtils;
import com.glmiyamoto.myfavoritemovies.utils.NetworkUtils;
import com.glmiyamoto.myfavoritemovies.views.adapters.MovieListAdapter;
import com.glmiyamoto.myfavoritemovies.views.fragments.FragmentInteraction.OnFragmentInteractionListener;
import com.glmiyamoto.myfavoritemovies.webservices.OmdbApiProvider;
import com.glmiyamoto.myfavoritemovies.views.adapters.MovieListAdapter.OnItemClickListener;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentInteraction.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {

    private ViewHolder mViewHolder;
    private MovieListAdapter mAdapter;

    private OnFragmentInteractionListener mListener;

    public SearchFragment() {
        // Required empty public constructor
    }

    /**
     * Create a new instance of this fragment.
     *
     * @return A new instance of fragment SearchFragment.
     */
    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {
        // Enable Option Menu
        setHasOptionsMenu(true);

        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_search, container, false);

        mViewHolder = new ViewHolder(view);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mViewHolder.mRcySearch.setLayoutManager(layoutManager);
        mViewHolder.mRcySearch.addItemDecoration(new DividerItemDecoration(getContext(),
                layoutManager.getOrientation()));
        mAdapter = new MovieListAdapter(getContext(), new ArrayList<Movie>(),
                new OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, final View view,
                                    final int position, final String id) {
                showMovieDetail(id);
            }
        });
        mViewHolder.mRcySearch.setAdapter(mAdapter);

        mViewHolder.mEtxSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(final TextView textView, final int actionId, final KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    final String text = mViewHolder.mEtxSearch.getText().toString();
                    searchMovies(text);
                    return true;
                }

                return false;
            }
        });

        mViewHolder.mIbtSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                final String text = mViewHolder.mEtxSearch.getText().toString();
                searchMovies(text);

                DeviceUtils.hideKeyboard(getActivity());
            }
        });

        return view;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        final int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                getActivity().onBackPressed();
                return true;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAttach(final Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }

        // Enable UP button
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onDetach() {
        super.onDetach();

        // Disable UP button
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        mListener = null;
    }

    private void searchMovies(final String text) {
        if (!NetworkUtils.hasNetwork(getContext())) {
            showMessageDialog(R.string.message_network_is_require);
            return;
        }

        mAdapter.removeAll();

        if (!text.isEmpty()) {
            // Show progress dialog
            mListener.onFragmentInteraction(FragmentInteraction.SHOW_PROGRESS, null);

            OmdbApiProvider.getInstance().searchMoviesByName(text, new Response.Listener<OmdbObject>() {
                @Override
                public void onResponse(final OmdbObject obj) {
                    if (obj != null && obj.isResponse()) {
                        mAdapter.addAll(obj.getItems());
                        mAdapter.loadPoster(0);
                    }

                    mListener.onFragmentInteraction(FragmentInteraction.HIDE_PROGRESS, null);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(final VolleyError error) {
                    mListener.onFragmentInteraction(FragmentInteraction.HIDE_PROGRESS, null);
                }
            });
        }
    }

    private void showMovieDetail(final String id) {
        mListener.onFragmentInteraction(FragmentInteraction.SHOW_PROGRESS, null);

        OmdbApiProvider.getInstance().getMovieById(id, new Response.Listener<OmdbItemObject>() {
            @Override
            public void onResponse(final OmdbItemObject item) {
                if (item != null && item.isResponse()) {
                    final Bundle args = new Bundle();
                    args.putParcelable(AppConstants.ITEM_KEY, item);
                    mListener.onFragmentInteraction(FragmentInteraction.SHOW_ITEM_DETAIL, args);
                }

                mListener.onFragmentInteraction(FragmentInteraction.HIDE_PROGRESS, null);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mListener.onFragmentInteraction(FragmentInteraction.HIDE_PROGRESS, null);
            }
        });
    }

    /**
     * Shows message dialog
     */
    public void showMessageDialog(final int messageId) {
        new AlertDialog.Builder(getContext())
                .setMessage(messageId)
                .setCancelable(true)
                .setPositiveButton(android.R.string.ok, null)
                .show();
    }

    public class ViewHolder {
        public final EditText mEtxSearch;
        public final ImageButton mIbtSearch;
        public final RecyclerView mRcySearch;

        public ViewHolder(final View view) {
            mEtxSearch = (EditText) view.findViewById(R.id.edit_text_search);
            mIbtSearch = (ImageButton) view.findViewById(R.id.image_button_search);
            mRcySearch = (RecyclerView) view.findViewById(R.id.recycler_view_search);
        }
    }
}
