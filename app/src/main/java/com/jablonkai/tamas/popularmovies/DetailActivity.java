package com.jablonkai.tamas.popularmovies;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jablonkai.tamas.popularmovies.data.Movie;
import com.jablonkai.tamas.popularmovies.data.MovieDetail;
import com.jablonkai.tamas.popularmovies.task.FetchMovieDetailsTask;
import com.jablonkai.tamas.popularmovies.task.FetchTrailersTask;
import com.jablonkai.tamas.popularmovies.utils.NetworkUtils;

import com.squareup.picasso.Picasso;

import java.net.URL;
import java.text.DateFormat;
import java.util.Date;

import butterknife.BindDrawable;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity
        implements FetchTrailersTask.FetchTrailersInterface, FetchMovieDetailsTask.FetchMovieDetailsInterface {

    //@BindView(R.id.iv_cover_image) ImageView mCoverImageView;
    @BindView(R.id.tv_title) TextView mTitleTextView;
    @BindView(R.id.iv_movie_poster) ImageView mPosterImageView;
    @BindView(R.id.tv_release_date) TextView mReleaseDateTextView;
    @BindView(R.id.tv_average_vote) TextView mVoteTextView;
    @BindView(R.id.tv_vote_count) TextView mVoteCountTextView;
    @BindView(R.id.tv_overview) TextView mOverviewTextView;
    @BindView(R.id.tv_trailers) TextView mTrailersTextView;
    @BindView(R.id.rv_trailers) RecyclerView mTrailersRecyclerView;
    @BindString(R.string.detail_activity_title) String mTitle;
    @BindDrawable(R.drawable.placeholder) Drawable mPlaceholderDrawable;
    @BindDrawable(R.drawable.error) Drawable mErrorDrawable;

    MovieDetail mMovieDetail = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);
        setTitle(mTitle);

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
            new FetchMovieDetailsTask(this).execute(movieId);
            new FetchTrailersTask(this).execute(movieId);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
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
/*            final URL backdropUrl = NetworkUtils.buildBackdropPath(mMovie.getBackdropPath());
            Picasso.with(getParent())
                    .load(String.valueOf(backdropUrl))
                    .placeholder(mPlaceholderDrawable)
                    .error(mErrorDrawable)
                    .into(mCoverImageView);*/
        mMovieDetail = movieDetail;

//        mTitleTextView.setText(mMovie.getOriginalTitle());
        mReleaseDateTextView.setText(localDateFromLong(mMovieDetail.getReleaseDate()));
        mVoteTextView.setText(mMovieDetail.getVoteAverage() + "/10");
        mVoteCountTextView.setText(Long.toString(mMovieDetail.getVoteCount()));
        mOverviewTextView.setText(mMovieDetail.getOverview());
    }

    @Override
    public void showTrailers() {
        mTrailersTextView.setVisibility(View.VISIBLE);
        mTrailersRecyclerView.setVisibility(View.VISIBLE);
    }
}
