package com.jablonkai.tamas.popularmovies.task;

import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;

import com.jablonkai.tamas.popularmovies.adapter.MoviePosterAdapter;
import com.jablonkai.tamas.popularmovies.data.Movie;
import com.jablonkai.tamas.popularmovies.utils.NetworkUtils;
import com.jablonkai.tamas.popularmovies.utils.TheMovieDbJsonUtils;

import java.net.URL;

public class FetchMoviesTask extends AsyncTask<String, Void, Movie[]> {

    private ProgressBar mLoadingIndicator;
    private MoviePosterAdapter mMovieAdapter;
    private FetchMoviesInterface mFetchMoviesInterface;

    public interface FetchMoviesInterface{
        void showMoviePostersView();
        void showErrorMessage();
    }

    public FetchMoviesTask(FetchMoviesInterface fetchMoviesInterface, ProgressBar progressBar, MoviePosterAdapter moviePosterAdapter) {
        mLoadingIndicator = progressBar;
        mMovieAdapter = moviePosterAdapter;
        mFetchMoviesInterface = fetchMoviesInterface;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }

    @Override
    protected Movie[] doInBackground(String... params) {
        if (params.length == 0) {
            return null;
        }

        URL moviesRequestUrl = NetworkUtils.buildUrl(params[0]);

        try {
            String jsonMoviesResponse = NetworkUtils.getResponseFromHttpUrl(moviesRequestUrl);

            return TheMovieDbJsonUtils.getMoviesDbFromJson(jsonMoviesResponse);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(Movie[] moviesData) {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        if (moviesData != null) {
            mFetchMoviesInterface.showMoviePostersView();
            mMovieAdapter.setMoviesData(moviesData);
        } else {
            mFetchMoviesInterface.showErrorMessage();
        }
    }
}
