package com.firmannf.moflick.screen.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;

import com.firmannf.moflick.R;
import com.firmannf.moflick.data.MovieModel;
import com.firmannf.moflick.screen.detail.MovieDetailActivity;
import com.firmannf.moflick.util.AppConstant;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements
        MovieAdapter.MovieItemListener,
        LoaderManager.LoaderCallbacks<List<MovieModel>> {

    @BindView(R.id.main_toolbar)
    Toolbar toolbar;
    @BindView(R.id.main_progressbar_movies)
    ProgressBar progressBarMovies;
    @BindView(R.id.main_recyclerview_movies)
    RecyclerView recyclerViewMovies;

    private MovieAdapter movieAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setupToolbar();
        setupRecyclerView();
        getSupportLoaderManager().initLoader(AppConstant.MOVIE_LOADER_ID, savedInstanceState, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        final SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Bundle bundle = new Bundle();
                bundle.putString(AppConstant.EXTRAS_TITLE, searchView.getQuery().toString());
                getSupportLoaderManager().restartLoader(AppConstant.MOVIE_LOADER_ID, bundle, MainActivity.this);

                // Hide Keyboard
                InputMethodManager inputMethodManager =
                        (InputMethodManager) getSystemService(
                                Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(
                        getCurrentFocus().getWindowToken(), 0);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return true;
    }

    @Override
    public void onMovieClick(String extras) {
        Intent intent = new Intent(this, MovieDetailActivity.class);
        intent.putExtra(AppConstant.EXTRAS_MOVIE_JSON, extras);
        startActivity(intent);
    }

    @NonNull
    @Override
    public Loader<List<MovieModel>> onCreateLoader(int id, @Nullable Bundle args) {
        progressBarMovies.setVisibility(View.VISIBLE);
        movieAdapter.replaceData(new ArrayList<MovieModel>());
        String title = "";
        if (args != null) {
            title = args.getString(AppConstant.EXTRAS_TITLE);
        }

        if (title != null && title.equals("")) {
            return new MoviePopularLoader(this);
        } else {
            return new MovieSearchLoader(this, title);
        }
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<MovieModel>> loader, List<MovieModel> data) {
        movieAdapter.replaceData(data);
        progressBarMovies.setVisibility(View.GONE);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<MovieModel>> loader) {
        movieAdapter.replaceData(new ArrayList<MovieModel>());
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.app_name));
    }

    private void setupRecyclerView() {
        recyclerViewMovies.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerViewMovies.setHasFixedSize(true);

        movieAdapter = new MovieAdapter(this, new ArrayList<MovieModel>(), this);
        recyclerViewMovies.setAdapter(movieAdapter);
    }
}
