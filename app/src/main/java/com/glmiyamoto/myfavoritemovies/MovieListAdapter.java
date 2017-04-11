package com.glmiyamoto.myfavoritemovies;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.glmiyamoto.myfavoritemovies.controllers.ImageController;
import com.glmiyamoto.myfavoritemovies.controllers.MovieController;
import com.glmiyamoto.myfavoritemovies.models.Movie;

import java.util.HashMap;
import java.util.List;

/**
 * Created by glmiyamoto on 4/4/17.
 */

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.ViewHolder> {
    private final List<Movie> mItems;

    private Context mContext;

    public final HashMap<String, Bitmap> mPosters = new HashMap<>();
    private int mLoadingPos;

    private int mSelectedItemPosition = -1;
    private OnItemClickListener mListener;

    public MovieListAdapter(final Context context, final List<Movie> items,
                            final OnItemClickListener listener) {
        mContext = context;
        mItems = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final View view = LayoutInflater.from(mContext)
                .inflate(R.layout.view_search_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Movie item = mItems.get(position);

        holder.mItem = item;

        // Set the Project selection
        if (mSelectedItemPosition == position) {
            holder.mView.setBackgroundResource(R.color.colorAccent);
        } else {
            holder.mView.setBackgroundResource(android.R.color.transparent);
        }

        // Set values in UI
        final String titleFormat = mContext.getResources().getString(R.string.item_title_format);
        holder.mTxtTitle.setText(String.format(titleFormat,
                new Object[]{ item.getTitle(), item.getYear() }));

        // Set the cover image
        if (mPosters.containsKey(item.getId()) && mPosters.get(item.getId()) != null) {
            holder.mImgPoster.setImageBitmap(mPosters.get(item.getId()));
        } else {
            holder.mImgPoster.setImageResource(R.mipmap.ic_launcher);
        }

        holder.mView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(final View view) {
                if (mListener != null) {
                    mListener.onItemClick(null, view, position, item.getId());
                }
            }

        });
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public Movie getItem(final int position) {
        return mItems.get(position);
    }

    public void addAll(final List<? extends Movie> items) {
        if (items != null && items.size() > 0) {
            final int position = mItems.size() == 0 ? 0 : mItems.size();

            mItems.addAll(items);

            notifyDataSetChanged();
        }
    }

    public void resume() {
        loadPoster(mLoadingPos);
    }

    public void pause() {

    }

    public final void loadPoster(final int position) {
        if (position < getItemCount()) {
            final Movie item = mItems.get(position);
            MovieController.getInstance().requestMoviePoster(item,
                    new MovieController.OnMoviePosterReceivedListener() {
                        @Override
                        public void onMoviePosterReceived(Bitmap bitmap) {
                if (bitmap != null) {
                    mPosters.put(item.getId(), bitmap);
                    notifyItemChanged(position);
                }

                loadPoster(++mLoadingPos);
                }
            });
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView mImgPoster;
        public final TextView mTxtTitle;
        public Movie mItem;

        public ViewHolder(final View view) {
            super(view);

            mView = view;
            mImgPoster = (ImageView) view.findViewById(R.id.image_view_poster);
            mTxtTitle = (TextView) view.findViewById(R.id.text_view_title);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(AdapterView<?> parent, View view, int position, String id);
    }
}
