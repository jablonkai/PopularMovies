package com.jablonkai.tamas.popularmovies;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.jablonkai.tamas.popularmovies.data.Movie;
import com.jablonkai.tamas.popularmovies.utils.NetworkUtils;

import com.squareup.picasso.Picasso;

import java.net.URL;
import java.text.DateFormat;
import java.util.Date;

import butterknife.BindDrawable;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {

    @BindView(R.id.tv_title) TextView mTitleTextView;
    @BindView(R.id.iv_movie_poster) ImageView mPosterImageView;
    @BindView(R.id.tv_release_date) TextView mReleaseDateTextView;
    @BindView(R.id.tv_average_vote) TextView mVoteTextView;
    @BindView(R.id.tv_vote_count) TextView mVoteCountTextView;
    @BindView(R.id.tv_overview) TextView mOverviewTextView;
    @BindString(R.string.detail_activity_title) String mTitle;
    @BindDrawable(R.drawable.placeholder) Drawable mPlaceholderDrawable;
    @BindDrawable(R.drawable.error) Drawable mErrorDrawable;

    Movie mMovie = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);
        setTitle(mTitle);

        Intent intentThatStartedThisActivity = getIntent();

        if (intentThatStartedThisActivity != null) {
            mMovie = intentThatStartedThisActivity.getParcelableExtra("MOVIE");

            mTitleTextView.setText(mMovie.getOriginalTitle());
            mReleaseDateTextView.setText(localDateFromLong(mMovie.getReleaseDate()));
            mVoteTextView.setText(mMovie.getVoteAverage() + "/10");
            mVoteCountTextView.setText(Long.toString(mMovie.getVoteCount()));
            mOverviewTextView.setText(mMovie.getOverview());

            final URL url = NetworkUtils.buildPosterUrl(mMovie.getPosterPath());
            Picasso.with(this).load(String.valueOf(url)).into(mPosterImageView);

            Picasso.with(getParent())
                    .load(String.valueOf(url))
                    .placeholder(mPlaceholderDrawable)
                    .error(mErrorDrawable)
                    .into(mPosterImageView);
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

    public static String localDateFromLong(long longDate) {
        Date date = new Date(longDate);

        if (DateUtils.isToday(longDate))
            return DateFormat.getTimeInstance(DateFormat.SHORT).format(date);
        else
            return DateFormat.getDateInstance(DateFormat.SHORT).format(date);
    }
}
