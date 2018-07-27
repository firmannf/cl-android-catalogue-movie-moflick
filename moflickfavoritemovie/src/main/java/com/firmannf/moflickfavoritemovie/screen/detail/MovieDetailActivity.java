package com.firmannf.moflickfavoritemovie.screen.detail;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firmannf.moflickfavoritemovie.R;
import com.firmannf.moflickfavoritemovie.data.MovieModel;
import com.firmannf.moflickfavoritemovie.util.AppConstant;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.provider.BaseColumns._ID;
import static com.firmannf.moflickfavoritemovie.data.source.local.DatabaseContract.CONTENT_URI;
import static com.firmannf.moflickfavoritemovie.data.source.local.DatabaseContract.DictionaryColumns.COLUMN_IS_LOVED;
import static com.firmannf.moflickfavoritemovie.data.source.local.DatabaseContract.DictionaryColumns.COLUMN_OVERVIEW;
import static com.firmannf.moflickfavoritemovie.data.source.local.DatabaseContract.DictionaryColumns.COLUMN_POSTER_PATH;
import static com.firmannf.moflickfavoritemovie.data.source.local.DatabaseContract.DictionaryColumns.COLUMN_RELEASE_DATE;
import static com.firmannf.moflickfavoritemovie.data.source.local.DatabaseContract.DictionaryColumns.COLUMN_TITLE;
import static com.firmannf.moflickfavoritemovie.data.source.local.DatabaseContract.DictionaryColumns.COLUMN_VOTE_AVERAGE;

public class MovieDetailActivity extends AppCompatActivity {
    @BindView(R.id.moviedetail_toolbar)
    Toolbar toolbar;
    @BindView(R.id.moviedetail_imageview_poster)
    ImageView imageViewPoster;
    @BindView(R.id.moviedetail_floatingbutton_love)
    FloatingActionButton floatingActionButtonLove;
    @BindView(R.id.moviedetail_textview_title)
    TextView textViewTitle;
    @BindView(R.id.moviedetail_textview_year)
    TextView textViewYear;
    @BindView(R.id.moviedetail_textview_voteaverage)
    TextView textViewVoteAverage;
    @BindView(R.id.moviedetail_textview_overview)
    TextView textViewOverview;

    MovieModel movie = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);

        setupToolbar();

        Intent intent = getIntent();
        Uri uri = getIntent().getData();
        if (uri != null) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                if (cursor.moveToFirst()) movie = new MovieModel(cursor);
                cursor.close();
            }
        }

        getSupportActionBar().setTitle(movie.getTitle());
        Glide.with(this)
                .load(AppConstant.BASE_IMAGE_URL + movie.getPosterPath())
                .into(imageViewPoster);

        Cursor cursor = getContentResolver().query(Uri.parse(CONTENT_URI + "/" + movie.getId()), null, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                movie.setLoved(1);
                floatingActionButtonLove.setImageResource(R.drawable.ic_love_black_24dp);
            }
            cursor.close();
        } else {
            movie.setLoved(0);
            floatingActionButtonLove.setImageResource(R.drawable.ic_love_border_black_24dp);
        }

        floatingActionButtonLove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse(CONTENT_URI + "/" + movie.getId());
                if (movie.isLoved() == 0) {
                    floatingActionButtonLove.setImageResource(R.drawable.ic_love_black_24dp);
                    movie.setLoved(1);
                    ContentValues values = new ContentValues();
                    values.put(_ID, movie.getId());
                    values.put(COLUMN_TITLE, movie.getTitle());
                    values.put(COLUMN_VOTE_AVERAGE, movie.getVoteAverage());
                    values.put(COLUMN_POSTER_PATH, movie.getPosterPath());
                    values.put(COLUMN_OVERVIEW, movie.getOverview());
                    values.put(COLUMN_RELEASE_DATE, movie.getReleaseDate());
                    values.put(COLUMN_IS_LOVED, movie.isLoved());
                    getContentResolver().insert(CONTENT_URI, values);
                } else if (movie.isLoved() == 1) {
                    floatingActionButtonLove.setImageResource(R.drawable.ic_love_border_black_24dp);
                    movie.setLoved(0);
                    getContentResolver().delete(uri, null, null);
                }
            }
        });
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

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
