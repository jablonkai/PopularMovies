package com.jablonkai.tamas.popularmovies.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jablonkai.tamas.popularmovies.R;
import com.jablonkai.tamas.popularmovies.adapter.MoviePosterAdapter;
import com.jablonkai.tamas.popularmovies.data.Movie;
import com.jablonkai.tamas.popularmovies.task.FetchMoviesTask;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
        implements MoviePosterAdapter.MoviePosterAdapterClickHandler, FetchMoviesTask.FetchMoviesInterface {

    private static String MDB_SORT_POPULAR = "popular";
    private static String MDB_SORT_TOP_RATED = "top_rated";

    @BindView(R.id.rv_movie_posters) RecyclerView mRecyclerView;
    @BindView(R.id.tv_error_message_display) TextView mErrorMessageDisplay;
    @BindView(R.id.pb_loading_indicator) ProgressBar mLoadingIndicator;
    @BindString(R.string.main_activity_title_popular) String mTitlePopular;
    @BindString(R.string.main_activity_rate) String mTitleVote;

    private MoviePosterAdapter mMovieAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setTitle(mTitlePopular);

        // TODO: check and correct
        Configuration config = getResources().getConfiguration();
        if (config.orientation == Configuration.ORIENTATION_PORTRAIT)
            mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        else
            mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        mMovieAdapter = new MoviePosterAdapter(this);
        mRecyclerView.setAdapter(mMovieAdapter);

        loadMoviePosters();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("RECYLCE_VIEW", mRecyclerView.getLayoutManager().onSaveInstanceState());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        Parcelable listState = savedInstanceState.getParcelable("RECYLCE_VIEW");
        mRecyclerView.getLayoutManager().onRestoreInstanceState(listState);
    }

    private void loadMoviePosters() {
        showMoviePostersView();
        // TODO: több mint egy oldal betöltése
        new FetchMoviesTask(this, mLoadingIndicator, mMovieAdapter).execute(MDB_SORT_POPULAR);
    }

    public void showMoviePostersView() {
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    public void showErrorMessage() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(Movie movie) {
        Class destinationClass = DetailActivity.class;
        Intent intentToStartDetailActivity = new Intent(this, destinationClass);
        intentToStartDetailActivity.putExtra("MOVIE", movie);
        startActivity(intentToStartDetailActivity);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO: újrarendezésnél első sorba ugorjon
        switch (item.getItemId()) {
            case R.id.action_sort_popular:
                showMoviePostersView();
                setTitle(mTitlePopular);
                new FetchMoviesTask(this, mLoadingIndicator, mMovieAdapter).execute(MDB_SORT_POPULAR);
                return true;

            case R.id.action_sort_rated:
                showMoviePostersView();
                setTitle(mTitleVote);
                new FetchMoviesTask(this, mLoadingIndicator, mMovieAdapter).execute(MDB_SORT_TOP_RATED);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
