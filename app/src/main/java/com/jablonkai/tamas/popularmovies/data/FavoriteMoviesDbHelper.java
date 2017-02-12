package com.jablonkai.tamas.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.jablonkai.tamas.popularmovies.data.FavoriteMoviesContract.FavoriteMovieEntry;

public class FavoriteMoviesDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "favorite_movies.db";
    private static final int DATABASE_VERSION = 1;

    public FavoriteMoviesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_FAVORITE_MOVIES_TABLE =
            "CREATE TABLE " + FavoriteMovieEntry.TABLE_NAME + " (" +
                    FavoriteMovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    FavoriteMovieEntry.THEMOVIEDB_ID + " INTEGER, " +
                    FavoriteMovieEntry.ORIGINAL_TITLE + " TEXT, " +
                    FavoriteMovieEntry.POSTER_PATH + " TEXT, " +
                    FavoriteMovieEntry.OVERVIEW + " TEXT, " +
                    FavoriteMovieEntry.VOTE_AVERAGE + " REAL, " +
                    FavoriteMovieEntry.VOTE_COUNT + " INTEGER, " +
                    FavoriteMovieEntry.RELEASE_DATE + " INTEGER);";

        sqLiteDatabase.execSQL(SQL_CREATE_FAVORITE_MOVIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FavoriteMovieEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
