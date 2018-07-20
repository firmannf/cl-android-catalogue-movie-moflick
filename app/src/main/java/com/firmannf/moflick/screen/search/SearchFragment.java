package com.firmannf.moflick.screen.search;


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
import com.firmannf.moflick.data.source.remote.MovieSearchLoader;
import com.firmannf.moflick.screen.detail.MovieDetailActivity;
import com.firmannf.moflick.screen.main.MainActivity;
import com.firmannf.moflick.shared.MovieAdapter;
import com.firmannf.moflick.util.AppConstant;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class SearchFragment extends Fragment implements
        MovieAdapter.MovieItemListener,
        LoaderManager.LoaderCallbacks<List<MovieModel>> {

    @BindView(R.id.search_progressbar)
    ProgressBar progressBarSearch;
    @BindView(R.id.search_recyclerview)
    RecyclerView recyclerViewSearch;
    private Unbinder unbinder;

    private MovieAdapter movieAdapter;

    private String paramTitle;

    public SearchFragment() {
    }

    public static SearchFragment newInstance(String paramTitle) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putString(AppConstant.EXTRAS_TITLE, paramTitle);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            paramTitle = getArguments().getString(AppConstant.EXTRAS_TITLE);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        unbinder = ButterKnife.bind(this, view);
        setupRecyclerView();
        ((MainActivity) getActivity()).getBottomNavigationView().setVisibility(View.GONE);
        Bundle bundle = new Bundle();
        bundle.putString(AppConstant.EXTRAS_TITLE, paramTitle);
        getActivity()
                .getSupportLoaderManager()
                .restartLoader(AppConstant.MOVIE_SEARCH_LOADER_ID, bundle, this);
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
        if (progressBarSearch != null)
            progressBarSearch.setVisibility(View.VISIBLE);
        movieAdapter.replaceData(new ArrayList<MovieModel>());
        String title = "";
        if (args != null) {
            title = args.getString(AppConstant.EXTRAS_TITLE);
        }
        return new MovieSearchLoader(getActivity(), title);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<MovieModel>> loader, List<MovieModel> data) {
        if (progressBarSearch != null)
            progressBarSearch.setVisibility(View.GONE);

        movieAdapter.replaceData(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<MovieModel>> loader) {
        movieAdapter.replaceData(new ArrayList<MovieModel>());
    }

    private void setupRecyclerView() {
        recyclerViewSearch.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        recyclerViewSearch.setHasFixedSize(true);

        movieAdapter = new MovieAdapter(getActivity(), new ArrayList<MovieModel>(), this);
        recyclerViewSearch.setAdapter(movieAdapter);
    }
}
