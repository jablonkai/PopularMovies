package com.jablonkai.tamas.popularmovies.utils;

import com.jablonkai.tamas.popularmovies.data.Movie;
import com.jablonkai.tamas.popularmovies.data.MovieDetail;
import com.jablonkai.tamas.popularmovies.data.Review;
import com.jablonkai.tamas.popularmovies.data.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class TheMovieDbJsonUtils {

    public static Movie[] getMoviesDbFromJson(String jsonMoviesResponse) throws JSONException {
        final String MDB_RESULTS = "results";
        final String MDB_ID = "id";
        final String MDB_ORIGINAL_TITLE = "original_title";
        final String MDB_POSTER_PATH = "poster_path";

        JSONObject moviesJson = new JSONObject(jsonMoviesResponse);

        JSONArray moviesArray = moviesJson.getJSONArray(MDB_RESULTS);
        Movie[] parsedMovieData = new Movie[moviesArray.length()];

        for (int i = 0; i < moviesArray.length(); i++) {
            JSONObject movie = moviesArray.getJSONObject(i);

            long id = movie.getLong(MDB_ID);
            String originalTitle = movie.getString(MDB_ORIGINAL_TITLE);
            String moviePoster = movie.getString(MDB_POSTER_PATH).replace("/", "");

            parsedMovieData[i] = new Movie(id, originalTitle, moviePoster);
        }

        return parsedMovieData;
    }

    public static MovieDetail getMovieDetailFromJson(String jsonMovieDetailResponse) throws JSONException {
        final String MDB_ID = "id";
        final String MDB_ORIGINAL_TITLE = "original_title";
        final String MDB_POSTER_PATH = "poster_path";
        final String MDB_OVERVIEW = "overview";
        final String MDB_VOTE_AVERAGE = "vote_average";
        final String MDB_VOTE_COUNT = "vote_count";
        final String MDB_RELEASE_DATE = "release_date";

        JSONObject movie = new JSONObject(jsonMovieDetailResponse);

        long id = movie.getLong(MDB_ID);
        String originalTitle = movie.getString(MDB_ORIGINAL_TITLE);
        String moviePoster = movie.getString(MDB_POSTER_PATH).replace("/", "");
        String overview = movie.getString(MDB_OVERVIEW);
        double vote = movie.getDouble(MDB_VOTE_AVERAGE);
        long voteCount = movie.getLong(MDB_VOTE_COUNT);
        String dateString = movie.getString(MDB_RELEASE_DATE);

        String[] dateArray = dateString.split("-");
        int year = Integer.parseInt(dateArray[0]);
        int month = Integer.parseInt(dateArray[1]);
        int date = Integer.parseInt(dateArray[2]);

        Calendar cal = Calendar.getInstance();
        cal.set(year, month, date);
        long longDate = cal.getTimeInMillis();

        return new MovieDetail(id, originalTitle, moviePoster, overview, vote, voteCount, longDate);
    }

    public static Trailer[] getTrailersDbFromJson(String jsonTrailersResponse) throws JSONException {
        final String MDB_RESULTS = "results";
        final String MDB_NAME = "name";
        final String MDB_KEY = "key";

        JSONObject trailersJson = new JSONObject(jsonTrailersResponse);

        JSONArray trailersArray = trailersJson.getJSONArray(MDB_RESULTS);
        Trailer[] parsedTrailerData = new Trailer[trailersArray.length()];

        for (int i = 0; i < trailersArray.length(); i++) {
            JSONObject trailer = trailersArray.getJSONObject(i);

            String name = trailer.getString(MDB_NAME);
            String key = trailer.getString(MDB_KEY);

            parsedTrailerData[i] = new Trailer(name, key);
        }

        return parsedTrailerData;
    }

    public static Review[] getReviewsDbFromJson(String jsonReviewResponse) throws JSONException {
        final String MDB_RESULTS = "results";
        final String MDB_AUTHOR = "author";
        final String MDB_CONTENT = "content";

        JSONObject reviewsJson = new JSONObject(jsonReviewResponse);

        JSONArray reviewsArray = reviewsJson.getJSONArray(MDB_RESULTS);
        Review[] parsedReviewData = new Review[reviewsArray.length()];

        for (int i = 0; i < reviewsArray.length(); i++) {
            JSONObject review = reviewsArray.getJSONObject(i);

            String author = review.getString(MDB_AUTHOR);
            String content = review.getString(MDB_CONTENT);

            parsedReviewData[i] = new Review(author, content);
        }

        return parsedReviewData;
    }
}
