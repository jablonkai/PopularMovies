package com.jablonkai.tamas.popularmovies.task;

import android.os.AsyncTask;

import com.jablonkai.tamas.popularmovies.adapter.TrailerAdapter;
import com.jablonkai.tamas.popularmovies.data.Trailer;
import com.jablonkai.tamas.popularmovies.utils.NetworkUtils;
import com.jablonkai.tamas.popularmovies.utils.TheMovieDbJsonUtils;

import java.net.URL;

public class FetchTrailersTask extends AsyncTask<Long, Void, Trailer[]> {

    private FetchTrailersInterface mFetchTrailersInterface;
    private TrailerAdapter mTrailerAdapter;

    public interface FetchTrailersInterface{
        void showTrailers();
    }

    public FetchTrailersTask(FetchTrailersInterface fetchTrailersInterface, TrailerAdapter trailerAdapter) {
        mFetchTrailersInterface = fetchTrailersInterface;
        mTrailerAdapter = trailerAdapter;
    }

    @Override
    protected Trailer[] doInBackground(Long... params) {
        if (params.length == 0) {
            return null;
        }

        URL trailersRequestUrl = NetworkUtils.buildTrailersUrl(params[0]);

        try {
            String jsonMoviesResponse = NetworkUtils.getResponseFromHttpUrl(trailersRequestUrl);

            return TheMovieDbJsonUtils.getTrailersDbFromJson(jsonMoviesResponse);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(Trailer[] trailersData) {
        if (trailersData != null && trailersData.length > 0) {
            mFetchTrailersInterface.showTrailers();
            mTrailerAdapter.setTrailersData(trailersData);
        } // else assume there is no trailers
    }
}
