package com.firmannf.moflickfavoritemovie.screen.main;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;

import com.firmannf.moflickfavoritemovie.R;
import com.firmannf.moflickfavoritemovie.screen.detail.MovieDetailActivity;
import com.firmannf.moflickfavoritemovie.shared.MovieCursorAdapter;
import com.firmannf.moflickfavoritemovie.util.AppConstant;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.firmannf.moflickfavoritemovie.data.source.local.DatabaseContract.CONTENT_URI;

public class MainActivity extends AppCompatActivity implements
        MovieCursorAdapter.MovieItemListener,
        LoaderManager.LoaderCallbacks<Cursor> {

    @BindView(R.id.main_toolbar)
    Toolbar toolbar;
    @BindView(R.id.main_progressbar)
    ProgressBar progressBarLoved;
    @BindView(R.id.main_recyclerview)
    RecyclerView recyclerViewLoved;

    private MovieCursorAdapter movieAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setupToolbar();
        setupRecyclerView();
        getSupportLoaderManager().initLoader(AppConstant.MOVIE_LOVED_LOADER_ID,
                savedInstanceState,
                this);
    }

    @Override
    public void onMovieClick(int id) {
        Intent intent = new Intent(this, MovieDetailActivity.class);
        intent.setData(Uri.parse(CONTENT_URI + "/" + id));
        intent.putExtra(AppConstant.EXTRAS_FROM_LOVED, AppConstant.EXTRAS_FROM_LOVED);
        startActivity(intent);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        if (progressBarLoved != null)
            progressBarLoved.setVisibility(View.VISIBLE);
        movieAdapter.replaceData(null);
        return new CursorLoader(this, CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        if (progressBarLoved != null)
            progressBarLoved.setVisibility(View.GONE);
        movieAdapter.replaceData(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        movieAdapter.replaceData(null);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
    }

    private void setupRecyclerView() {
        recyclerViewLoved.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerViewLoved.setHasFixedSize(true);
        movieAdapter = new MovieCursorAdapter(this, null, this);
        recyclerViewLoved.setAdapter(movieAdapter);
    }
}
