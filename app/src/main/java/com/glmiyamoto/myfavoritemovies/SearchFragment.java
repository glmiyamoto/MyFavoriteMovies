package com.glmiyamoto.myfavoritemovies;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.glmiyamoto.myfavoritemovies.models.Movie;
import com.glmiyamoto.myfavoritemovies.models.OmdbObject;
import com.glmiyamoto.myfavoritemovies.models.OmdbItemObject;
import com.glmiyamoto.myfavoritemovies.utils.DeviceUtils;
import com.glmiyamoto.myfavoritemovies.views.FragmentInteraction;
import com.glmiyamoto.myfavoritemovies.views.FragmentInteraction.OnFragmentInteractionListener;
import com.glmiyamoto.myfavoritemovies.webservices.OmdbApiProvider;
import com.glmiyamoto.myfavoritemovies.MovieListAdapter.OnItemClickListener;

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_search, container, false);

        mViewHolder = new ViewHolder(view);

        mViewHolder.mRcySearch.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new MovieListAdapter(getContext(), new ArrayList<Movie>(),
                new OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, final View view,
                                    final int position, final String id) {
                showMovieDetail(id);
            }
        });
        mViewHolder.mRcySearch.setAdapter(mAdapter);

        mViewHolder.mIbtSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                // Show progress dialog
                mListener.onFragmentInteraction(FragmentInteraction.SHOW_PROGRESS, null);

                // Request
                final String text = mViewHolder.mEtxSearch.getText().toString();
                searchMovies(text);

                DeviceUtils.hideKeyboard(getActivity());
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void searchMovies(final String text) {
        if (!text.isEmpty()) {
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
