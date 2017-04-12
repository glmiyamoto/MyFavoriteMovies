package com.glmiyamoto.myfavoritemovies.views.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.glmiyamoto.myfavoritemovies.AppConstants;
import com.glmiyamoto.myfavoritemovies.views.MainActivity;
import com.glmiyamoto.myfavoritemovies.R;
import com.glmiyamoto.myfavoritemovies.controllers.ImageController;
import com.glmiyamoto.myfavoritemovies.controllers.MovieController;
import com.glmiyamoto.myfavoritemovies.models.Movie;
import com.glmiyamoto.myfavoritemovies.views.fragments.FragmentInteraction.OnFragmentInteractionListener;

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
            final String titleFormat = getString(R.string.item_title_format);
            final String title = String.format(titleFormat, mMovie.getTitle(), mMovie.getYear());
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(title);

            MovieController.getInstance().requestMoviePoster(mMovie,
                    new MovieController.OnMoviePosterReceivedListener() {
                @Override
                public void onMoviePosterReceived(final Bitmap bitmap) {
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
        if (mMenuHolder.mMenuItemSearch != null) {
            mMenuHolder.mMenuItemSearch.setVisible(false);
        }

        if (mMovie == null) {
            mMenuHolder.mMenuItemSave.setVisible(false);
            mMenuHolder.mMenuItemDelete.setVisible(false);
        } else if (MovieController.getInstance().getMovieById(mMovie.getId()) != null) {
            mMenuHolder.mMenuItemSave.setVisible(false);
            mMenuHolder.mMenuItemDelete.setVisible(true);
        } else {
            mMenuHolder.mMenuItemSave.setVisible(true);
            mMenuHolder.mMenuItemDelete.setVisible(false);
        }

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        final int id = item.getItemId();
        switch (id) {
            case R.id.action_save:
                performSaveMovie();
                return true;
            case R.id.action_delete:
                performRemoveMovie();
                return true;
            case android.R.id.home:
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

        // Enable UP button
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onDetach() {
        super.onDetach();

        // Set app name to action bar
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.app_name);

        if (getActivity() instanceof MainActivity) {
            // Disable UP button
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }

        mListener = null;
    }

    public void performSaveMovie() {
        mListener.onFragmentInteraction(FragmentInteraction.SHOW_PROGRESS, null);

        if (MovieController.getInstance().insert(mMovie)) {
            final String fileName = mMovie.getId() + ".jpg";
            final Bitmap poster = ((BitmapDrawable)mViewHolder.mImgPoster.getDrawable()).getBitmap();

            // Create a poster copy to original poster not be recycled
            final Bitmap posterCopy = poster.copy(poster.getConfig(), poster.isMutable());

            // Save poster to memory
            ImageController.getInstance().saveImageFile(fileName,
                    posterCopy, new ImageController.OnImageSavedListener() {
                        @Override
                        public void onImageSaved(boolean success, Bundle args) {
                            final String textFormat = getContext().getResources().getString(R.string.message_save_format);
                            Toast.makeText(getContext(), String.format(textFormat, mMovie.getTitle()),
                                    Toast.LENGTH_SHORT).show();

                            mListener.onFragmentInteraction(FragmentInteraction.HIDE_PROGRESS, null);

                            mMenuHolder.mMenuItemSave.setVisible(false);
                            mMenuHolder.mMenuItemDelete.setVisible(true);
                        }
                    });
        }
    }

    public void performRemoveMovie() {
        if (MovieController.getInstance().delete(mMovie)) {
            final String fileName = mMovie.getId() + ".jpg";

            // Remove poster from memory
            ImageController.getInstance().removeImageFile(fileName,
                    new ImageController.OnImageRemovedListener() {
                        @Override
                        public void onImageRemoved(boolean success, Bundle args) {
                            final String textFormat = getContext().getResources().getString(R.string.message_delete_format);
                            Toast.makeText(getContext(), String.format(textFormat, mMovie.getTitle()),
                                    Toast.LENGTH_SHORT).show();

                            mListener.onFragmentInteraction(FragmentInteraction.HIDE_PROGRESS, null);

                            mMenuHolder.mMenuItemSave.setVisible(true);
                            mMenuHolder.mMenuItemDelete.setVisible(false);
                        }
                    });
        }
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
        public MenuItem mMenuItemSearch;
        public MenuItem mMenuItemSave;
        public MenuItem mMenuItemDelete;

        public  MenuHolder(final Menu menu) {
            mMenu = menu;
            mMenuItemSearch = menu.findItem(R.id.action_search);
            mMenuItemSave = menu.findItem(R.id.action_save);
            mMenuItemDelete = menu.findItem(R.id.action_delete);
        }
    }
}
