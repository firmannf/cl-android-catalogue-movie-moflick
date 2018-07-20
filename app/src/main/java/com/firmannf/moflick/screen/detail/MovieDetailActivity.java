package com.firmannf.moflick.screen.detail;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firmannf.moflick.R;
import com.firmannf.moflick.data.MovieModel;
import com.firmannf.moflick.util.AppConstant;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetailActivity extends AppCompatActivity {

    @BindView(R.id.moviedetail_toolbar)
    Toolbar toolbar;
    @BindView(R.id.moviedetail_imageview_poster)
    ImageView imageViewPoster;
    @BindView(R.id.moviedetail_textview_title)
    TextView textViewTitle;
    @BindView(R.id.moviedetail_textview_year)
    TextView textViewYear;
    @BindView(R.id.moviedetail_textview_voteaverage)
    TextView textViewVoteAverage;
    @BindView(R.id.moviedetail_textview_overview)
    TextView textViewOverview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);

        setupToolbar();

        Intent intent = getIntent();
        MovieModel movie = intent.getParcelableExtra(AppConstant.EXTRAS_MOVIE);

        getSupportActionBar().setTitle(movie.getTitle());
        Glide.with(this)
                .load(AppConstant.BASE_IMAGE_URL + movie.getPosterPath())
                .into(imageViewPoster);
        textViewTitle.setText(movie.getTitle());
        textViewYear.setText(movie.getReleaseDate().split("-")[0]);
        textViewVoteAverage.setText(String.valueOf(movie.getVoteAverage()));
        textViewOverview.setText(String.valueOf(movie.getOverview()));
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    private void setupToolbar(){
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
