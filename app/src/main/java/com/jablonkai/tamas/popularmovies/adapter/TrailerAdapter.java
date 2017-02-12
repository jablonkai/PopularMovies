package com.jablonkai.tamas.popularmovies.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jablonkai.tamas.popularmovies.R;
import com.jablonkai.tamas.popularmovies.data.Trailer;
import com.jablonkai.tamas.popularmovies.utils.NetworkUtils;

import com.squareup.picasso.Picasso;

import java.net.URL;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerAdapterViewHolder> {

    private Trailer[] mTrailersData;
    private final TrailerAdapterClickHandler mClickHandler;

    public interface TrailerAdapterClickHandler {
        void onTrailerClick(Trailer trailer);
    }

    public class TrailerAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {

        @BindView(R.id.iv_trailer_poster) ImageView mPosterImageView;
        @BindView(R.id.tv_trailer) TextView mTitle;
        @BindDrawable(R.drawable.placeholder) Drawable mPlaceholderDrawable;
        @BindDrawable(R.drawable.error) Drawable mErrorDrawable;

        public TrailerAdapterViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mClickHandler.onTrailerClick(mTrailersData[adapterPosition]);
        }
    }

    public TrailerAdapter(TrailerAdapterClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    @Override
    public TrailerAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.trailer_item;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, viewGroup, false);
        return new TrailerAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrailerAdapterViewHolder holder, int position) {
        URL url = NetworkUtils.buildYouTubePosterUrl(mTrailersData[position].getKey());

        // TODO: add play icon to video posters
        Picasso.with(holder.itemView.getContext())
                .load(String.valueOf(url))
                .placeholder(holder.mPlaceholderDrawable)
                .error(holder.mErrorDrawable)
                .into(holder.mPosterImageView);

        holder.mTitle.setText(mTrailersData[position].getName());
    }

    @Override
    public int getItemCount() {
        if (null == mTrailersData)
            return 0;

        return mTrailersData.length;
    }

    public void setTrailersData(Trailer[] trailersData) {
        mTrailersData = trailersData;
        notifyDataSetChanged();
    }
}
