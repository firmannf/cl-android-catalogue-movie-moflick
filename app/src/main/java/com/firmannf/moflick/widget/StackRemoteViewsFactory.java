package com.firmannf.moflick.widget;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.firmannf.moflick.R;
import com.firmannf.moflick.data.MovieModel;
import com.firmannf.moflick.data.source.local.MovieHelper;
import com.firmannf.moflick.util.AppConstant;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import static com.firmannf.moflick.data.source.local.DatabaseContract.CONTENT_URI;

/**
 * Created by codelabs on 15/08/18.
 */

public class StackRemoteViewsFactory implements
        RemoteViewsService.RemoteViewsFactory {

    private ArrayList<MovieModel> movies = new ArrayList<>();
    private Context context;

    public StackRemoteViewsFactory(Context context) {
        this.context = context;
    }

    public void onCreate() {
    }

    @Override
    public void onDataSetChanged() {
        MovieHelper movieHelper = new MovieHelper(context);
        movieHelper.open();
        movies = movieHelper.getAllData();
        movieHelper.close();
    }

    @Override
    public void onDestroy() {
        movies.clear();
    }

    @Override
    public int getCount() {
        return movies.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        final RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.row_movie_widget);
        final MovieModel movie = movies.get(position);

        Bitmap bitmap = null;
        try {
            bitmap = Glide.with(context)
                    .asBitmap()
                    .load(AppConstant.BASE_BIG_IMAGE_URL + movie.getPosterPath())
                    .submit()
                    .get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        remoteViews.setImageViewBitmap(R.id.moviewidget_imageview_poster, bitmap);
        Log.d("TESTTT", movie.getPosterPath() + " ");

        Bundle extras = new Bundle();
        extras.putString(AppConstant.EXTRAS_TITLE, movie.getTitle());
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);

        remoteViews.setOnClickFillInIntent(R.id.moviewidget_imageview_poster, fillInIntent);
        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return movies.get(i).getId();
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
