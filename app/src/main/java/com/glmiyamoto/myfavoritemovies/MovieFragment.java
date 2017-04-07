package com.glmiyamoto.myfavoritemovies;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.glmiyamoto.myfavoritemovies.controllers.ImageController;
import com.glmiyamoto.myfavoritemovies.controllers.MovieController;
import com.glmiyamoto.myfavoritemovies.models.Movie;
import com.glmiyamoto.myfavoritemovies.views.FragmentInteraction;
import com.glmiyamoto.myfavoritemovies.views.FragmentInteraction.OnFragmentInteractionListener;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentInteraction.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MovieFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MovieFragment extends Fragment {

    private Movie mMovie;

    private ViewHolder mViewHolder;
    private MenuHolder mMenuHolder;

    private OnFragmentInteractionListener mListener;

    public MovieFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment.
     *
     * @return A new instance of fragment MovieFragment.
     */
    public static MovieFragment newInstance() {
        return new MovieFragment();
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

        // Get fragment arguments
        final Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey(AppConstants.ITEM_KEY)) {
            mMovie = bundle.getParcelable(AppConstants.ITEM_KEY);
        }

        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_movie, container, false);

        mViewHolder = new ViewHolder(view);

        return view;
    }

    @Override
    public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (mMovie != null) {
            ImageController.getInstance().requestImageByUrl(mMovie.getPoster(),
                    new ImageController.OnReceivedImageListener() {
                @Override
                public void onReceivedImage(final Bitmap bitmap) {
                    if (bitmap != null) {
                        mViewHolder.mImgPoster.setImageBitmap(bitmap);
                    }
                }
            });

            mViewHolder.mTxtWriter.setText(mMovie.getWriter());
            mViewHolder.mTxtDirector.setText(mMovie.getDirector());
            mViewHolder.mTxtActors.setText(mMovie.getActors());
            mViewHolder.mTxtLanguage.setText(mMovie.getLanguage());
            mViewHolder.mTxtCountry.setText(mMovie.getCountry());
            mViewHolder.mTxtAwards.setText(mMovie.getAwards());
            mViewHolder.mTxtPlot.setText(mMovie.getPlot());
        }
    }

    @Override
    public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater) {
        inflater.inflate(R.menu.menu_movie, menu);

        mMenuHolder = new MenuHolder(menu);
//        mMenuHolder.mMenuItemSave.setVisible(false);
//        mMenuHolder.mMenuItemDelete.setVisible(false);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        final int id = item.getItemId();
        switch (id) {
            case R.id.action_save:
                MovieController.getInstance().insert(mMovie);
                mListener.onFragmentInteraction(FragmentInteraction.CLOSE_ITEM_DETAIL, null);
                return true;
            case R.id.action_delete:
                MovieController.getInstance().delete(mMovie);
                mListener.onFragmentInteraction(FragmentInteraction.CLOSE_ITEM_DETAIL, null);
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
    }

    @Override
    public void onDetach() {
        super.onDetach();

        mListener = null;
    }

    public class ViewHolder {
        public final View mView;
        public final ImageView mImgPoster;
        public final TextView mTxtWriter;
        public final TextView mTxtDirector;
        public final TextView mTxtActors;
        public final TextView mTxtLanguage;
        public final TextView mTxtCountry;
        public final TextView mTxtGenre;
        public final TextView mTxtAwards;
        public final TextView mTxtPlot;

        public ViewHolder(final View view) {
            mView = view;
            mImgPoster = (ImageView) view.findViewById(R.id.image_view_poster);
            mTxtWriter = (TextView) view.findViewById(R.id.text_view_writer);
            mTxtDirector = (TextView) view.findViewById(R.id.text_view_director);
            mTxtActors = (TextView) view.findViewById(R.id.text_view_actors);
            mTxtLanguage = (TextView) view.findViewById(R.id.text_view_language);
            mTxtCountry = (TextView) view.findViewById(R.id.text_view_country);
            mTxtGenre = (TextView) view.findViewById(R.id.text_view_genre);
            mTxtAwards = (TextView) view.findViewById(R.id.text_view_awards);
            mTxtPlot = (TextView) view.findViewById(R.id.text_view_plot);
        }
    }

    public class MenuHolder {
        public Menu mMenu;
        public MenuItem mMenuItemSave;
        public MenuItem mMenuItemDelete;

        public  MenuHolder(final Menu menu) {
            mMenu = menu;
            mMenuItemSave = menu.getItem(0);
            mMenuItemDelete = menu.getItem(1);
        }
    }
}
