package com.jablonkai.tamas.popularmovies.task;

import android.os.AsyncTask;

import com.jablonkai.tamas.popularmovies.adapter.ReviewAdapter;
import com.jablonkai.tamas.popularmovies.data.Review;
import com.jablonkai.tamas.popularmovies.utils.NetworkUtils;
import com.jablonkai.tamas.popularmovies.utils.TheMovieDbJsonUtils;

import java.net.URL;

public class FetchReviewsTask extends AsyncTask<Long, Void, Review[]> {

    private FetchReviewsInterface mFetchReviewsInterface;
    private ReviewAdapter mReviewAdapter;

    public interface FetchReviewsInterface{
        void showReviews();
    }

    public FetchReviewsTask(FetchReviewsInterface fetchReviewsTask, ReviewAdapter reviewAdapter) {
        mFetchReviewsInterface = fetchReviewsTask;
        mReviewAdapter = reviewAdapter;
    }

    @Override
    protected Review[] doInBackground(Long... params) {
        if (params.length == 0) {
            return null;
        }

        URL trailersRequestUrl = NetworkUtils.buildReviewsUrl(params[0]);

        try {
            String jsonTrailersResponse = NetworkUtils.getResponseFromHttpUrl(trailersRequestUrl);

            return TheMovieDbJsonUtils.getReviewsDbFromJson(jsonTrailersResponse);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(Review[] reviews) {
        if (reviews != null && reviews.length > 0) {
            mReviewAdapter.setReviewsData(reviews);
            mFetchReviewsInterface.showReviews();
        } // else assume there is no reviews
    }
}
