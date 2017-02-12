package com.jablonkai.tamas.popularmovies.utils;

import android.net.Uri;

import com.jablonkai.tamas.popularmovies.BuildConfig;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {

    private static final String THEMOVIEDB_ORG_BASE_URL = "https://api.themoviedb.org/3/movie/";
    private static final String THEMOVIEDB_ORG_POSTER_BASE_URL = "https://image.tmdb.org/t/p/";

    private static final String YOUTUBE_POSTER_BASE_URL = "https://img.youtube.com/vi/";
    private static final String YOUTUBE_VIDEO_BASE_URL = "https://www.youtube.com/watch";

    private static final String PATH_VIDEOS = "videos";
    private static final String PATH_REVIEWS = "reviews";
    private static final String API_PARAM = "api_key";
    private static final String SIZE_PATH = "w342";

    private static final String YOUTUBE_IMAGE = "0.jpg";
    private static final String VIDEO_PARAM = "v";

    public static URL buildUrl(String sort) {
        Uri builtUri = Uri.parse(THEMOVIEDB_ORG_BASE_URL).buildUpon()
                .appendPath(sort)
                .appendQueryParameter(API_PARAM, BuildConfig.MOVIEDB_ORG_API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static URL buildMovieDetailUrl(long movieId) {
        Uri builtUri = Uri.parse(THEMOVIEDB_ORG_BASE_URL).buildUpon()
                .appendPath(Long.toString(movieId))
                .appendQueryParameter(API_PARAM, BuildConfig.MOVIEDB_ORG_API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static URL buildTrailersUrl(long movieId) {
        Uri builtUri = Uri.parse(THEMOVIEDB_ORG_BASE_URL).buildUpon()
                .appendPath(Long.toString(movieId))
                .appendPath(PATH_VIDEOS)
                .appendQueryParameter(API_PARAM, BuildConfig.MOVIEDB_ORG_API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }


    public static URL buildReviewsUrl(long movieId) {
        Uri builtUri = Uri.parse(THEMOVIEDB_ORG_BASE_URL).buildUpon()
                .appendPath(Long.toString(movieId))
                .appendPath(PATH_REVIEWS)
                .appendQueryParameter(API_PARAM, BuildConfig.MOVIEDB_ORG_API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static URL buildPosterUrl(String path) {
        Uri builtUri = Uri.parse(THEMOVIEDB_ORG_POSTER_BASE_URL).buildUpon()
                .appendPath(SIZE_PATH)
                .appendPath(path)
                .appendQueryParameter(API_PARAM, BuildConfig.MOVIEDB_ORG_API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return  url;
    }

    public static URL buildYouTubePosterUrl(String key) {
        Uri builtUri = Uri.parse(YOUTUBE_POSTER_BASE_URL).buildUpon()
                .appendPath(key)
                .appendPath(YOUTUBE_IMAGE)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return  url;
    }

    public static Uri buildYouTubeUrl(String key) {
        Uri builtUri = Uri.parse(YOUTUBE_VIDEO_BASE_URL).buildUpon()
                .appendQueryParameter(VIDEO_PARAM, key)
                .build();

        return builtUri;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
