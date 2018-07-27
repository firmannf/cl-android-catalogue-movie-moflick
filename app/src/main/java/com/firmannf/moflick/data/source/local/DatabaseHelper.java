package com.firmannf.moflick.data.source.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static android.provider.BaseColumns._ID;
import static com.firmannf.moflick.data.source.local.DatabaseContract.DictionaryColumns.COLUMN_IS_LOVED;
import static com.firmannf.moflick.data.source.local.DatabaseContract.DictionaryColumns.COLUMN_OVERVIEW;
import static com.firmannf.moflick.data.source.local.DatabaseContract.DictionaryColumns.COLUMN_POSTER_PATH;
import static com.firmannf.moflick.data.source.local.DatabaseContract.DictionaryColumns.COLUMN_RELEASE_DATE;
import static com.firmannf.moflick.data.source.local.DatabaseContract.DictionaryColumns.COLUMN_TITLE;
import static com.firmannf.moflick.data.source.local.DatabaseContract.DictionaryColumns.COLUMN_VOTE_AVERAGE;
import static com.firmannf.moflick.data.source.local.DatabaseContract.TABLE_FAVORITE_MOVIE;

/**
 * Created by codelabs on 23/07/18.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "dictionary.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String REAL_TYPE = " REAL";
    private static final String NOT_NULL = " NOT NULL";
    private static final String COMMA_SEP = ",";

    private static final String SQL_CREATE_TABLE_FAVORITE_MOVIE =
            "CREATE TABLE " + TABLE_FAVORITE_MOVIE + " (" +
                    _ID + INTEGER_TYPE + " PRIMARY KEY" + COMMA_SEP +
                    COLUMN_TITLE + TEXT_TYPE + NOT_NULL + COMMA_SEP +
                    COLUMN_VOTE_AVERAGE + REAL_TYPE + NOT_NULL + COMMA_SEP +
                    COLUMN_POSTER_PATH + TEXT_TYPE + NOT_NULL + COMMA_SEP +
                    COLUMN_OVERVIEW + TEXT_TYPE + NOT_NULL + COMMA_SEP +
                    COLUMN_RELEASE_DATE + TEXT_TYPE + NOT_NULL + COMMA_SEP +
                    COLUMN_IS_LOVED + INTEGER_TYPE + NOT_NULL +
                    " )";
    private static final String SQL_DROP_TABLE_FAVORITE_MOVIE =
            "DROP TABLE IF EXISTS " + TABLE_FAVORITE_MOVIE;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE_FAVORITE_MOVIE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(SQL_DROP_TABLE_FAVORITE_MOVIE);
        onCreate(sqLiteDatabase);
    }
}
