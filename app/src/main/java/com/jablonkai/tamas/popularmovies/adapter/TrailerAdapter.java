package com.jablonkai.tamas.popularmovies.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerAdapterViewHolder> {

    private String[] mTrailersData;

    public class TrailerAdapterViewHolder extends RecyclerView.ViewHolder {

        public TrailerAdapterViewHolder(View itemView) {
            super(itemView);
        }
/*
        @BindView(R.id.iv_movie_poster)
        ImageView mPosterImageView;
        @BindView(R.id.tv_title)
        TextView mTitle;
        @BindDrawable(R.drawable.placeholder)
        Drawable mPlaceholderDrawable;
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
        }*/
    }

    @Override
    public TrailerAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(TrailerAdapterViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        if (null == mTrailersData)
            return 0;

        return mTrailersData.length;
    }

    public void setTrailersData(String[] trailersData) {
        mTrailersData = trailersData;
        notifyDataSetChanged();
    }
}
