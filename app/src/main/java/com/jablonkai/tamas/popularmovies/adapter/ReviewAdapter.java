package com.jablonkai.tamas.popularmovies.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
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
    }

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
