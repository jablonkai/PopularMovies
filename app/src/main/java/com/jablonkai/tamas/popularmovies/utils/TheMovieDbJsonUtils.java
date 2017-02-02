package com.jablonkai.tamas.popularmovies.utils;

import com.jablonkai.tamas.popularmovies.FetchMoviesTask;
import com.jablonkai.tamas.popularmovies.data.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class TheMovieDbJsonUtils {

    public static Movie[] getMoviesDbFromJson(FetchMoviesTask fetchMoviesTask, String jsonMoviesResponse) throws JSONException {
        Movie[] parsedMovieData = null;

        final String MDB_RESULTS = "results";
        final String MDB_ORIGINAL_TITLE = "original_title";
        final String MDB_POSTER_PATH = "poster_path";
        final String MDB_OVERVIEW = "overview";
        final String MDB_VOTE_AVERAGE = "vote_average";
        final String MDB_VOTE_COUNT = "vote_count";
        final String MDB_RELEASE_DATE = "release_date";

        JSONObject moviesJson = new JSONObject(jsonMoviesResponse);

        JSONArray moviesArray = moviesJson.getJSONArray(MDB_RESULTS);
        parsedMovieData = new Movie[moviesArray.length()];

        for (int i = 0; i < moviesArray.length(); i++) {
            JSONObject movie = moviesArray.getJSONObject(i);

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

            parsedMovieData[i] = new Movie(originalTitle, moviePoster, overview, vote, voteCount, longDate);
        }

        return parsedMovieData;
    }
}
