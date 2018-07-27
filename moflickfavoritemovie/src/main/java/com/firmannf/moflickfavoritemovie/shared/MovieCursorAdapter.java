package com.firmannf.moflickfavoritemovie.shared;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firmannf.moflickfavoritemovie.R;
import com.firmannf.moflickfavoritemovie.data.MovieModel;
import com.firmannf.moflickfavoritemovie.util.AppConstant;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by codelabs on 16/07/18.
 */

public class MovieCursorAdapter extends RecyclerView.Adapter<MovieCursorAdapter.ViewHolder> {
    private Context context;
    private Cursor movies;
    private MovieCursorAdapter.MovieItemListener movieItemListener;

    public MovieCursorAdapter(Context context, Cursor movies, MovieItemListener movieItemListener) {
        this.context = context;
        this.movies = movies;
        this.movieItemListener = movieItemListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_movie, null);
        return new MovieCursorAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final MovieModel movie = getItem(position);
        Glide.with(context)
                .load(AppConstant.BASE_IMAGE_URL + movie.getPosterPath())
                .into(holder.imageViewPoster);
        holder.textViewTitle.setText(movie.getTitle());
        holder.textViewVoteAverage.setText(String.valueOf(movie.getVoteAverage()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                movieItemListener.onMovieClick(movie.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        if (movies == null)
            return 0;
        return movies.getCount();
    }

    private MovieModel getItem(int position) {
        if (!movies.moveToPosition(position)) {
            throw new IllegalStateException("Position invalid");
        }
        return new MovieModel(movies);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.movie_imageview_poster)
        ImageView imageViewPoster;
        @BindView(R.id.movie_textview_title)
        TextView textViewTitle;
        @BindView(R.id.movie_textview_voteaverage)
        TextView textViewVoteAverage;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void replaceData(Cursor movies) {
        this.movies = movies;
        notifyDataSetChanged();
    }

    public interface MovieItemListener {
        void onMovieClick(int id);
    }
}
