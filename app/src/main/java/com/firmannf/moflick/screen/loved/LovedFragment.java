package com.firmannf.moflick.screen.loved;


import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.firmannf.moflick.R;
import com.firmannf.moflick.data.source.local.MovieLovedLoader;
import com.firmannf.moflick.screen.detail.MovieDetailActivity;
import com.firmannf.moflick.shared.MovieCursorAdapter;
import com.firmannf.moflick.util.AppConstant;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.firmannf.moflick.data.source.local.DatabaseContract.CONTENT_URI;

public class LovedFragment extends Fragment implements
        MovieCursorAdapter.MovieItemListener,
        LoaderManager.LoaderCallbacks<Cursor> {

    @BindView(R.id.loved_progressbar)
    ProgressBar progressBarLoved;
    @BindView(R.id.loved_recyclerview)
    RecyclerView recyclerViewLoved;
    private Unbinder unbinder;

    private MovieCursorAdapter movieAdapter;

    public LovedFragment() {
    }

    public static LovedFragment newInstance() {
        LovedFragment fragment = new LovedFragment();
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_loved, container, false);
        unbinder = ButterKnife.bind(this, view);
        setupRecyclerView();
        getActivity().getSupportLoaderManager().initLoader(AppConstant.MOVIE_LOVED_LOADER_ID,
                savedInstanceState,
                this);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        getActivity().getSupportLoaderManager().restartLoader(AppConstant.MOVIE_LOVED_LOADER_ID,
                null,
                this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onMovieClick(int id) {
        Intent intent = new Intent(getActivity(), MovieDetailActivity.class);
        intent.setData(Uri.parse(CONTENT_URI + "/" + id));
        startActivity(intent);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        if (progressBarLoved != null)
            progressBarLoved.setVisibility(View.VISIBLE);
        movieAdapter.replaceData(null);
        return new MovieLovedLoader(getActivity());
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

    private void setupRecyclerView() {
        recyclerViewLoved.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        recyclerViewLoved.setHasFixedSize(true);
        movieAdapter = new MovieCursorAdapter(getActivity(), null, this);
        recyclerViewLoved.setAdapter(movieAdapter);
    }
}
