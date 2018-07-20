package com.firmannf.moflick.data.source.remote;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

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

/**
 * Created by codelabs on 17/07/18.
 */

public class MovieSearchLoader extends AsyncTaskLoader<List<MovieModel>> {
    private List<MovieModel> movies;
    private boolean hasResult = false;
    private String title;

    public MovieSearchLoader(Context context, String title) {
        super(context);

        onContentChanged();
        this.title = title;
    }

    @Override
    protected void onStartLoading() {
        if (takeContentChanged())
            forceLoad();
        else if (hasResult)
            deliverResult(movies);
    }

    @Override
    public void deliverResult(@Nullable List<MovieModel> movies) {
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
    public List<MovieModel> loadInBackground() {
        final List<MovieModel> movies = new ArrayList<>();

        SyncHttpClient client = new SyncHttpClient();
        if (title != null && !title.equals("")) {
            String url = AppConstant.MOVIE_SEARCH_URL
                    + "?api_key=" + BuildConfig.THE_MOVIE_DB_API_KEY_DEBUG
                    + "&query=" + title;

            client.get(url, new AsyncHttpResponseHandler() {
                @Override
                public void onStart() {
                    super.onStart();
                    setUseSynchronousMode(true);
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    String jsonResult = new String(responseBody);
                    MovieResultModel moviesJson = new Gson().fromJson(jsonResult, MovieResultModel.class);
                    movies.addAll(moviesJson.getResults());
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    error.printStackTrace();
                }
            });
        }

        return movies;
    }
}
