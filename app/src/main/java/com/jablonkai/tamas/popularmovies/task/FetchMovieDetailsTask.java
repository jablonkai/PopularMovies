package com.jablonkai.tamas.popularmovies.task;

import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;

import com.jablonkai.tamas.popularmovies.data.MovieDetail;
import com.jablonkai.tamas.popularmovies.utils.NetworkUtils;
import com.jablonkai.tamas.popularmovies.utils.TheMovieDbJsonUtils;

import java.net.URL;

public class FetchMovieDetailsTask extends AsyncTask<Long, Void, MovieDetail> {

    private FetchMovieDetailsInterface mFetchMovieDetailsInterface;
    private ProgressBar mLoadingIndicator;

    public interface FetchMovieDetailsInterface{
        void showMovieDetail(MovieDetail movieDetail);
        void showErrorMessage();
    }

    public FetchMovieDetailsTask(FetchMovieDetailsTask.FetchMovieDetailsInterface fetchMovieDetailsInterface, ProgressBar progressBar) {
        mFetchMovieDetailsInterface = fetchMovieDetailsInterface;
        mLoadingIndicator = progressBar;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }

    @Override
    protected MovieDetail doInBackground(Long... params) {
        if (params.length == 0) {
            return null;
        }

        URL movieDetailRequestUrl = NetworkUtils.buildMovieDetailUrl(params[0]);

        try {
            String jsonMoviesResponse = NetworkUtils.getResponseFromHttpUrl(movieDetailRequestUrl);

            return TheMovieDbJsonUtils.getMovieDetailFromJson(jsonMoviesResponse);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(MovieDetail movieDetail) {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        if (movieDetail != null) {
            mFetchMovieDetailsInterface.showMovieDetail(movieDetail);
        } else {
            mFetchMovieDetailsInterface.showErrorMessage();
        }
    }
}
