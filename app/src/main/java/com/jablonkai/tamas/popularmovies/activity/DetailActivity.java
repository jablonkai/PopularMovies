package com.jablonkai.tamas.popularmovies.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jablonkai.tamas.popularmovies.R;
import com.jablonkai.tamas.popularmovies.adapter.ReviewAdapter;
import com.jablonkai.tamas.popularmovies.adapter.TrailerAdapter;
import com.jablonkai.tamas.popularmovies.data.FavoriteMoviesContract;
import com.jablonkai.tamas.popularmovies.data.Movie;
import com.jablonkai.tamas.popularmovies.data.MovieDetail;
import com.jablonkai.tamas.popularmovies.data.Trailer;
import com.jablonkai.tamas.popularmovies.task.FetchMovieDetailsTask;
import com.jablonkai.tamas.popularmovies.task.FetchReviewsTask;
import com.jablonkai.tamas.popularmovies.task.FetchTrailersTask;
import com.jablonkai.tamas.popularmovies.utils.NetworkUtils;

import com.squareup.picasso.Picasso;

import java.net.URL;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindDrawable;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity
        implements FetchTrailersTask.FetchTrailersInterface, FetchMovieDetailsTask.FetchMovieDetailsInterface,
        TrailerAdapter.TrailerAdapterClickHandler, FetchReviewsTask.FetchReviewsInterface,
        View.OnClickListener {

    @BindView(R.id.tv_title) TextView mTitleTextView;
    @BindView(R.id.iv_movie_poster) ImageView mPosterImageView;
    @BindView(R.id.tv_release_date) TextView mReleaseDateTextView;
    @BindView(R.id.tv_average_vote) TextView mVoteTextView;
    @BindView(R.id.tv_vote_count) TextView mVoteCountTextView;
    @BindView(R.id.tv_overview) TextView mOverviewTextView;
    @BindView(R.id.tv_error_message_detail) TextView mErrorMessageTextView;
    @BindView(R.id.tv_trailers) TextView mTrailersTextView;
    @BindView(R.id.rv_trailers) RecyclerView mTrailersRecyclerView;
    @BindView(R.id.pb_loading_indicator) ProgressBar mLoadingIndicator;
    @BindView(R.id.ll_detail) LinearLayout mDetailLinearLayout;
    @BindView(R.id.tv_reviews) TextView mReviewTextView;
    @BindView(R.id.reviews_more) Button mMoreReviewsButton;
    @BindViews({R.id.tv_reviewer1, R.id.tv_reviewer2, R.id.tv_reviewer3}) List<TextView> mReviewersTextViewList;
    @BindViews({R.id.tv_review1, R.id.tv_review2, R.id.tv_review3}) List<TextView> mReviewsTextViewList;
    @BindString(R.string.detail_activity_title) String mTitle;
    @BindDrawable(R.drawable.placeholder) Drawable mPlaceholderDrawable;
    @BindDrawable(R.drawable.error) Drawable mErrorDrawable;

    private MovieDetail mMovieDetail = null;
    private TrailerAdapter mTrailerAdapter;
    private static ReviewAdapter mReviewAdapter;
    private boolean isFavorite = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);
        setTitle(mTitle);

        mTrailersTextView.setVisibility(View.GONE);
        mTrailersRecyclerView.setVisibility(View.GONE);

        mTrailersRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        mTrailerAdapter = new TrailerAdapter(this);
        mTrailersRecyclerView.setAdapter(mTrailerAdapter);

        mReviewAdapter = new ReviewAdapter();

        Intent intentThatStartedThisActivity = getIntent();

        if (intentThatStartedThisActivity != null) {
            Movie mMovie = intentThatStartedThisActivity.getParcelableExtra("MOVIE");

            mTitleTextView.setText(mMovie.getOriginalTitle());

            final URL url = NetworkUtils.buildPosterUrl(mMovie.getPosterPath());
            Picasso.with(getParent())
                    .load(String.valueOf(url))
                    .placeholder(mPlaceholderDrawable)
                    .error(mErrorDrawable)
                    .into(mPosterImageView);

            long movieId = mMovie.getId();
            new FetchMovieDetailsTask(this, mLoadingIndicator).execute(movieId);
            new FetchTrailersTask(this, mTrailerAdapter).execute(movieId);
            new FetchReviewsTask(this, mReviewAdapter).execute(movieId);

            Cursor cursor = getContentResolver().query(FavoriteMoviesContract.FavoriteMovieEntry.CONTENT_URI,
                    null, FavoriteMoviesContract.FavoriteMovieEntry.THEMOVIEDB_ID + " = " + movieId, null, null);

            if (cursor.getCount() == 1)
                isFavorite = true;
        }

        mMoreReviewsButton.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.detail, menu);

        if (isFavorite)
            menu.findItem(R.id.action_favorite).setIcon(R.drawable.ic_star_white_24dp);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_favorite:
                if (isFavorite) {
                    getContentResolver().delete(FavoriteMoviesContract.FavoriteMovieEntry.CONTENT_URI,
                            FavoriteMoviesContract.FavoriteMovieEntry.THEMOVIEDB_ID + " = " + mMovieDetail.getId(), null);

                    item.setIcon(R.drawable.ic_star_border_white_24dp);
                } else {
                    ContentValues contentValues = new ContentValues();

                    contentValues.put(FavoriteMoviesContract.FavoriteMovieEntry.THEMOVIEDB_ID, mMovieDetail.getId());
                    contentValues.put(FavoriteMoviesContract.FavoriteMovieEntry.ORIGINAL_TITLE, mMovieDetail.getOriginalTitle());
                    contentValues.put(FavoriteMoviesContract.FavoriteMovieEntry.POSTER_PATH, mMovieDetail.getPosterPath());
                    contentValues.put(FavoriteMoviesContract.FavoriteMovieEntry.OVERVIEW, mMovieDetail.getOverview());
                    contentValues.put(FavoriteMoviesContract.FavoriteMovieEntry.VOTE_AVERAGE, mMovieDetail.getVoteAverage());
                    contentValues.put(FavoriteMoviesContract.FavoriteMovieEntry.VOTE_COUNT, mMovieDetail.getVoteCount());
                    contentValues.put(FavoriteMoviesContract.FavoriteMovieEntry.RELEASE_DATE, mMovieDetail.getReleaseDate());

                    getContentResolver().insert(FavoriteMoviesContract.FavoriteMovieEntry.CONTENT_URI, contentValues);
                    item.setIcon(R.drawable.ic_star_white_24dp);
                }

                isFavorite = !isFavorite;
                return true;

            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private static String localDateFromLong(long longDate) {
        Date date = new Date(longDate);
        return DateFormat.getDateInstance(DateFormat.SHORT).format(date);
    }

    @Override
    public void showMovieDetail(MovieDetail movieDetail) {
        mMovieDetail = movieDetail;

        mLoadingIndicator.setVisibility(View.GONE);
        mDetailLinearLayout.setVisibility(View.VISIBLE);

        mReleaseDateTextView.setText(localDateFromLong(mMovieDetail.getReleaseDate()));
        mVoteTextView.setText(mMovieDetail.getVoteAverage() + "/10");
        mVoteCountTextView.setText(Long.toString(mMovieDetail.getVoteCount()));
        mOverviewTextView.setText(mMovieDetail.getOverview());
    }

    @Override
    public void showErrorMessage() {
        mDetailLinearLayout.setVisibility(View.GONE);
        mErrorMessageTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showTrailers() {
        mTrailersTextView.setVisibility(View.VISIBLE);
        mTrailersRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onTrailerClick(Trailer trailer) {
        startActivity(new Intent(Intent.ACTION_VIEW, NetworkUtils.buildYouTubeUrl(trailer.getKey())));
    }

    @Override
    public void showReviews() {
        int maxI = Math.min(3, mReviewAdapter.getItemCount());

        if (maxI > 0) {
            mReviewTextView.setVisibility(View.VISIBLE);
            mMoreReviewsButton.setVisibility(View.VISIBLE);

            for (int i = 0; i < maxI; i++) {
                mReviewersTextViewList.get(i).setVisibility(View.VISIBLE);
                mReviewsTextViewList.get(i).setVisibility(View.VISIBLE);
                mReviewersTextViewList.get(i).setText(mReviewAdapter.getItemAt(i).getAuthor());
                mReviewsTextViewList.get(i).setText(mReviewAdapter.getItemAt(i).getContent());
            }
        }
    }

    @Override
    public void onClick(View v) {
        Class destinationClass = ReviewActivity.class;
        Intent intentToStartDetailActivity = new Intent(this, destinationClass);
        startActivity(intentToStartDetailActivity);
    }

    public static ReviewAdapter getReviewAdapter() {
        return mReviewAdapter;
    }
}
