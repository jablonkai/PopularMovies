package com.jablonkai.tamas.popularmovies.task;

import android.os.AsyncTask;

import com.jablonkai.tamas.popularmovies.data.MovieDetail;
import com.jablonkai.tamas.popularmovies.utils.NetworkUtils;
import com.jablonkai.tamas.popularmovies.utils.TheMovieDbJsonUtils;

import java.net.URL;

public class FetchMovieDetailsTask extends AsyncTask<Long, Void, MovieDetail> {

    private FetchMovieDetailsInterface mFetchMovieDetailsInterface;

    public interface FetchMovieDetailsInterface{
        void showMovieDetail(MovieDetail movieDetail);
    }

    public FetchMovieDetailsTask(FetchMovieDetailsTask.FetchMovieDetailsInterface fetchMovieDetailsInterface) {
        mFetchMovieDetailsInterface = fetchMovieDetailsInterface;
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
        if (movieDetail != null) {
            mFetchMovieDetailsInterface.showMovieDetail(movieDetail);
        }
    }
}
