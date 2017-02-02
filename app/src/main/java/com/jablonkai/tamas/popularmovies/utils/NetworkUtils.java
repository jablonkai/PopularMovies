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

    private static final String THEMOVIEDB_ORG_BASE_URL = "http://api.themoviedb.org/3/movie/";
    private static final String THEMOVIEDB_ORG_POSTER_BASE_URL = "http://image.tmdb.org/t/p/";

    private static final String API_PARAM = "api_key";
    private static final String SIZE_PATH = "w342";

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
