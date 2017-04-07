package com.glmiyamoto.myfavoritemovies;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.glmiyamoto.myfavoritemovies.controllers.MovieController;
import com.glmiyamoto.myfavoritemovies.models.Movie;
import com.glmiyamoto.myfavoritemovies.views.FragmentInteraction;
import com.glmiyamoto.myfavoritemovies.views.FragmentInteraction.OnFragmentInteractionListener;

import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainFragment extends Fragment {

    private ViewHolder mViewHolder;
    private MovieListAdapter mAdapter;

    private OnFragmentInteractionListener mListener;

    public MainFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_main, container, false);

        mViewHolder = new ViewHolder(view);

        mViewHolder.mRcyMovies.setLayoutManager(new LinearLayoutManager(getContext()));
        final List<Movie> movies = MovieController.getInstance().getMovies();
        mAdapter = new MovieListAdapter(getContext(), movies,
                new MovieListAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(final AdapterView<?> parent, final View view,
                                            final int position, final String id) {
                        showMovieDetail(id);
                    }
                });
        mViewHolder.mRcyMovies.setAdapter(mAdapter);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final int id = item.getItemId();
        if (id == R.id.action_search) {
            final Intent intent = new Intent(getContext(), SearchActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivityForResult(intent, 0);

            return true;
        }

        return super.onOptionsItemSelected(item);
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

    private void showMovieDetail(final String id) {
        mListener.onFragmentInteraction(FragmentInteraction.SHOW_PROGRESS, null);

        final Movie movie = MovieController.getInstance().getMovieById(id);
        if (movie != null) {
            final Bundle args = new Bundle();
            args.putParcelable(AppConstants.ITEM_KEY, movie);
            mListener.onFragmentInteraction(FragmentInteraction.SHOW_ITEM_DETAIL, args);
        }
    }

    public class ViewHolder {
        public final RecyclerView mRcyMovies;

        public ViewHolder(final View view) {
            mRcyMovies = (RecyclerView) view.findViewById(R.id.recycler_view_movies);
        }
    }
}
