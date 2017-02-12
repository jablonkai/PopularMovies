package com.jablonkai.tamas.popularmovies.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jablonkai.tamas.popularmovies.R;
import com.jablonkai.tamas.popularmovies.data.Review;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewAdapterViewHolder> {

    private Review[] mReviewsData;

    public class ReviewAdapterViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_reviewer) TextView mReviewerTextView;
        @BindView(R.id.tv_review) TextView mReviewTextView;

        public ReviewAdapterViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

/*        @BindView(R.id.iv_trailer_poster) ImageView mPosterImageView;
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
            mClickHandler.onClick(mTrailersData[adapterPosition]);
        }
  */  }


    @Override
    public ReviewAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.review_item;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, viewGroup, false);
        return new ReviewAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewAdapterViewHolder holder, int position) {
        holder.mReviewerTextView.setText(mReviewsData[position].getAuthor());
        holder.mReviewTextView.setText(mReviewsData[position].getContent());
    }

    @Override
    public int getItemCount() {
        if (null == mReviewsData)
            return 0;

        return mReviewsData.length;
    }

    public Review getItemAt(int position) {
        return mReviewsData[position];
    }

    public void setReviewsData(Review[] reviewsData) {
        mReviewsData = reviewsData;
        notifyDataSetChanged();
    }
}


/*public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerAdapterViewHolder> {

    private Trailer[] mTrailersData;
    private final TrailerAdapterClickHandler mClickHandler;

    public interface TrailerAdapterClickHandler {
        void onClick(Trailer trailer);
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
            mClickHandler.onClick(mTrailersData[adapterPosition]);
        }
    }

    public TrailerAdapter(TrailerAdapterClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }


}*/