package com.jablonkai.tamas.popularmovies;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jablonkai.tamas.popularmovies.data.Movie;
import com.jablonkai.tamas.popularmovies.utils.NetworkUtils;

import com.squareup.picasso.Picasso;

import java.net.URL;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MoviePosterAdapter extends RecyclerView.Adapter<MoviePosterAdapter.MoviePosterAdapterViewHolder> {

    private Movie[] mMoviesData;
    private final MoviePosterAdapterClickHandler mClickHandler;

    public interface MoviePosterAdapterClickHandler {
        void onClick(Movie movie);
    }

    public class MoviePosterAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.iv_movie_poster) ImageView mPosterImageView;
        @BindView(R.id.tv_title) TextView mTitle;
        @BindDrawable(R.drawable.placeholder) Drawable mPlaceholderDrawable;
        @BindDrawable(R.drawable.error) Drawable mErrorDrawable;

        public MoviePosterAdapterViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mClickHandler.onClick(mMoviesData[adapterPosition]);
        }
    }

    MoviePosterAdapter(MoviePosterAdapterClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    @Override
    public MoviePosterAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.list_item;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, viewGroup, false);
        return new MoviePosterAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MoviePosterAdapterViewHolder holder, int position) {
        URL url = NetworkUtils.buildPosterUrl(mMoviesData[position].getPosterPath());

        Picasso.with(holder.itemView.getContext())
                .load(String.valueOf(url))
                .placeholder(holder.mPlaceholderDrawable)
                .error(holder.mErrorDrawable)
                .into(holder.mPosterImageView);

        holder.mTitle.setText(mMoviesData[position].getOriginalTitle());
    }

    @Override
    public int getItemCount() {
        if (null == mMoviesData)
            return 0;

        return mMoviesData.length;
    }

    public void setMoviesData(Movie[] moviesData) {
        this.mMoviesData = moviesData;
        notifyDataSetChanged();
    }
}
