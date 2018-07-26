package com.firmannf.moflick.data.source.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.firmannf.moflick.data.MovieModel;

import java.util.ArrayList;

import static android.provider.BaseColumns._ID;
import static com.firmannf.moflick.data.source.local.DatabaseContract.DictionaryColumns.COLUMN_OVERVIEW;
import static com.firmannf.moflick.data.source.local.DatabaseContract.DictionaryColumns.COLUMN_POSTER_PATH;
import static com.firmannf.moflick.data.source.local.DatabaseContract.DictionaryColumns.COLUMN_RELEASE_DATE;
import static com.firmannf.moflick.data.source.local.DatabaseContract.DictionaryColumns.COLUMN_TITLE;
import static com.firmannf.moflick.data.source.local.DatabaseContract.DictionaryColumns.COLUMN_VOTE_AVERAGE;
import static com.firmannf.moflick.data.source.local.DatabaseContract.TABLE_FAVORITE_MOVIE;

/**
 * Created by codelabs on 23/07/18.
 */

public class MovieHelper {

    private Context context;
    private DatabaseHelper databaseHelper;
    private SQLiteDatabase sqLiteDatabase;

    public MovieHelper(Context context) {
        this.context = context;
    }

    public MovieHelper open() throws SQLException {
        databaseHelper = new DatabaseHelper(context);
        sqLiteDatabase = databaseHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        databaseHelper.close();
    }

    public ArrayList<MovieModel> getAllData() {
        Cursor cursor = sqLiteDatabase.query(TABLE_FAVORITE_MOVIE,
                null,
                null,
                null,
                null,
                null,
                _ID + " DESC",
                null);
        cursor.moveToFirst();

        ArrayList<MovieModel> movies = new ArrayList<>();
        MovieModel movie;
        if (cursor.getCount() > 0) {
            do {
                movie = new MovieModel();
                movie.setId(cursor.getInt(cursor.getColumnIndexOrThrow(_ID)));
                movie.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)));
                movie.setVoteAverage(cursor.getFloat(cursor.getColumnIndexOrThrow(COLUMN_VOTE_AVERAGE)));
                movie.setPosterPath(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_POSTER_PATH)));
                movie.setOverview(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_OVERVIEW)));
                movie.setReleaseDate(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RELEASE_DATE)));
                movies.add(movie);
                cursor.moveToNext();
            } while (!cursor.isAfterLast());
        }
        cursor.close();

        return movies;
    }

    public long insert(MovieModel movie) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, movie.getId());
        values.put(COLUMN_VOTE_AVERAGE, movie.getVoteAverage());
        values.put(COLUMN_POSTER_PATH, movie.getPosterPath());
        values.put(COLUMN_OVERVIEW, movie.getOverview());
        values.put(COLUMN_RELEASE_DATE, movie.getReleaseDate());
        return sqLiteDatabase.insert(TABLE_FAVORITE_MOVIE, null, values);
    }

    public int update(MovieModel movie) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, movie.getId());
        values.put(COLUMN_VOTE_AVERAGE, movie.getVoteAverage());
        values.put(COLUMN_POSTER_PATH, movie.getPosterPath());
        values.put(COLUMN_OVERVIEW, movie.getOverview());
        values.put(COLUMN_RELEASE_DATE, movie.getReleaseDate());
        return sqLiteDatabase.update(TABLE_FAVORITE_MOVIE,
                values,
                _ID + "= '" + movie.getId() + "'",
                null);
    }

    public int delete(int id) {
        return sqLiteDatabase.delete(TABLE_FAVORITE_MOVIE,
                _ID + " = '" + id + "'",
                null);
    }

    public Cursor queryByIdProvider(String id) {
        return sqLiteDatabase.query(TABLE_FAVORITE_MOVIE, null
                , _ID + " = ?"
                , new String[]{id}
                , null
                , null
                , null
                , null);
    }

    public Cursor queryProvider() {
        return sqLiteDatabase.query(TABLE_FAVORITE_MOVIE
                , null
                , null
                , null
                , null
                , null
                , _ID + " DESC");
    }

    public long insertProvider(ContentValues values) {
        return sqLiteDatabase.insert(TABLE_FAVORITE_MOVIE, null, values);
    }

    public int updateProvider(String id, ContentValues values) {
        return sqLiteDatabase.update(TABLE_FAVORITE_MOVIE, values, _ID + " = ?", new String[]{id});
    }

    public int deleteProvider(String id) {
        return sqLiteDatabase.delete(TABLE_FAVORITE_MOVIE, _ID + " = ?", new String[]{id});
    }
}
