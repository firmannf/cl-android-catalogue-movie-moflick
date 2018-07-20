package com.firmannf.moflick.screen.popular;


import android.content.Intent;
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
import com.firmannf.moflick.data.MovieModel;
import com.firmannf.moflick.data.source.remote.MoviePopularLoader;
import com.firmannf.moflick.data.source.remote.MovieUpcomingLoader;
import com.firmannf.moflick.screen.detail.MovieDetailActivity;
import com.firmannf.moflick.shared.MovieAdapter;
import com.firmannf.moflick.util.AppConstant;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class PopularFragment extends Fragment implements
        MovieAdapter.MovieItemListener,
        LoaderManager.LoaderCallbacks<List<MovieModel>> {

    @BindView(R.id.popular_progressbar)
    ProgressBar progressBarPopular;
    @BindView(R.id.popular_recyclerview)
    RecyclerView recyclerViewPopular;
    private Unbinder unbinder;

    private MovieAdapter movieAdapter;

    public PopularFragment() {
    }

    public static PopularFragment newInstance() {
        PopularFragment fragment = new PopularFragment();
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_popular, container, false);
        unbinder = ButterKnife.bind(this, view);
        setupRecyclerView();
        getActivity().getSupportLoaderManager().initLoader(AppConstant.MOVIE_POPULAR_LOADER_ID,
                savedInstanceState,
                this);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onMovieClick(MovieModel extras) {
        Intent intent = new Intent(getActivity(), MovieDetailActivity.class);
        intent.putExtra(AppConstant.EXTRAS_MOVIE, extras);
        startActivity(intent);
    }

    @NonNull
    @Override
    public Loader<List<MovieModel>> onCreateLoader(int id, @Nullable Bundle args) {
        if (progressBarPopular != null)
            progressBarPopular.setVisibility(View.VISIBLE);
        movieAdapter.replaceData(new ArrayList<MovieModel>());
        return new MoviePopularLoader(getActivity());
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<MovieModel>> loader, List<MovieModel> data) {
        if (progressBarPopular != null)
            progressBarPopular.setVisibility(View.GONE);

        movieAdapter.replaceData(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<MovieModel>> loader) {
        movieAdapter.replaceData(new ArrayList<MovieModel>());
    }

    private void setupRecyclerView() {
        recyclerViewPopular.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        recyclerViewPopular.setHasFixedSize(true);

        movieAdapter = new MovieAdapter(getActivity(), new ArrayList<MovieModel>(), this);
        recyclerViewPopular.setAdapter(movieAdapter);
    }
}
