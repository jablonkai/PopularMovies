package com.jablonkai.tamas.popularmovies.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
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
import com.jablonkai.tamas.popularmovies.data.FavoriteMoviesContract;
import com.jablonkai.tamas.popularmovies.data.Movie;
import com.jablonkai.tamas.popularmovies.task.FetchMoviesTask;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
        implements MoviePosterAdapter.MoviePosterAdapterClickHandler, FetchMoviesTask.FetchMoviesInterface {

    private static String MDB_SORT_POPULAR = "popular";
    private static String MDB_SORT_TOP_RATED = "top_rated";

    private static int SORT_BY_POPULAR = 1;
    private static int SORT_BY_VOTES = 2;
    private static int SORT_BY_FAVORITES = 3;

    @BindView(R.id.rv_movie_posters) RecyclerView mRecyclerView;
    @BindView(R.id.tv_error_message_display) TextView mErrorMessageDisplay;
    @BindView(R.id.pb_loading_indicator) ProgressBar mLoadingIndicator;
    @BindString(R.string.main_activity_title_popular) String mTitlePopular;
    @BindString(R.string.main_activity_rate) String mTitleVote;
    @BindString(R.string.main_activity_favorites) String mTitleFavorites;

    private MoviePosterAdapter mMovieAdapter;
    private int mSortBy = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setTitle(mTitlePopular);

        Configuration config = getResources().getConfiguration();
        if (config.orientation == Configuration.ORIENTATION_PORTRAIT)
            mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        else
            mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        mMovieAdapter = new MoviePosterAdapter(this);
        mRecyclerView.setAdapter(mMovieAdapter);

        if (savedInstanceState != null) {
            Parcelable listState = savedInstanceState.getParcelable("RECYLCE_VIEW");
            mRecyclerView.getLayoutManager().onRestoreInstanceState(listState);

            mSortBy = savedInstanceState.getInt("SORT_BY");
            if (mSortBy == SORT_BY_POPULAR) {
                sortPopular();
            } else if (mSortBy == SORT_BY_VOTES) {
                sortVotes();
            } else if (mSortBy == SORT_BY_FAVORITES) {
                sortFavorites();
            }
        } else {
            sortPopular();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("RECYLCE_VIEW", mRecyclerView.getLayoutManager().onSaveInstanceState());
        outState.putInt("SORT_BY", mSortBy);
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
        switch (item.getItemId()) {
            case R.id.action_sort_popular:
                sortPopular();
                return true;

            case R.id.action_sort_rated:
                sortVotes();
                return true;

            case R.id.action_sort_favorites:
                sortFavorites();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void sortPopular() {
        showMoviePostersView();
        setTitle(mTitlePopular);
        new FetchMoviesTask(this, mLoadingIndicator, mMovieAdapter).execute(MDB_SORT_POPULAR);
        mRecyclerView.scrollToPosition(0);
        mSortBy = SORT_BY_POPULAR;
    }

    private void sortVotes() {
        showMoviePostersView();
        setTitle(mTitleVote);
        new FetchMoviesTask(this, mLoadingIndicator, mMovieAdapter).execute(MDB_SORT_TOP_RATED);
        mRecyclerView.scrollToPosition(0);
        mSortBy = SORT_BY_VOTES;
    }

    private void sortFavorites() {
        showMoviePostersView();
        setTitle(mTitleFavorites);

        String sortOrder = FavoriteMoviesContract.FavoriteMovieEntry.ORIGINAL_TITLE + " COLLATE LOCALIZED ASC";
        Cursor cursor = getContentResolver().query(FavoriteMoviesContract.FavoriteMovieEntry.CONTENT_URI,
                null, null, null, sortOrder);

        try {
            Movie[] movies = new Movie[cursor.getCount()];

            while (cursor.moveToNext()) {
                movies[cursor.getPosition()] = new Movie(cursor.getLong(cursor.getColumnIndex(FavoriteMoviesContract.FavoriteMovieEntry.THEMOVIEDB_ID)),
                        cursor.getString(cursor.getColumnIndex(FavoriteMoviesContract.FavoriteMovieEntry.ORIGINAL_TITLE)),
                        cursor.getString(cursor.getColumnIndex(FavoriteMoviesContract.FavoriteMovieEntry.POSTER_PATH)));
            }

            mMovieAdapter.setMoviesData(movies);
        } catch (Exception e) {
            mMovieAdapter.setMoviesData(null);
        } finally {
            cursor.close();
        }

        mSortBy = SORT_BY_FAVORITES;
        mRecyclerView.scrollToPosition(0);
    }
}
