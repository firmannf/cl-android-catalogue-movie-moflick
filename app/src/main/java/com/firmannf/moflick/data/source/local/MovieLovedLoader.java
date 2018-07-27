package com.firmannf.moflick.data.source.local;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;

import com.firmannf.moflick.BuildConfig;
import com.firmannf.moflick.data.MovieModel;
import com.firmannf.moflick.data.MovieResultModel;
import com.firmannf.moflick.util.AppConstant;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

import static com.firmannf.moflick.data.source.local.DatabaseContract.CONTENT_URI;

/**
 * Created by codelabs on 17/07/18.
 */

public class MovieLovedLoader extends AsyncTaskLoader<Cursor> {
    private Cursor movies;
    private boolean hasResult = false;

    public MovieLovedLoader(Context context) {
        super(context);

        onContentChanged();
    }

    @Override
    protected void onStartLoading() {
        if (takeContentChanged())
            forceLoad();
        else if (hasResult)
            deliverResult(movies);
    }

    @Override
    public void deliverResult(@Nullable Cursor movies) {
        this.movies = movies;
        hasResult = true;
        super.deliverResult(movies);
    }

    @Override
    protected void onReset() {
        super.onReset();
        onStopLoading();
        if (hasResult) {
            movies = null;
            hasResult = false;
        }
    }

    @Nullable
    @Override
    public Cursor loadInBackground() {
        return getContext().getContentResolver().query(CONTENT_URI, null, null, null, null);
    }
}
