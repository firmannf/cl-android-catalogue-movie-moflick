package com.firmannf.moflick.shared;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firmannf.moflick.R;
import com.firmannf.moflick.data.MovieModel;
import com.firmannf.moflick.util.AppConstant;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by codelabs on 16/07/18.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    private Context context;
    private List<MovieModel> movies;
    private MovieAdapter.MovieItemListener movieItemListener;

    public MovieAdapter(Context context, List<MovieModel> movies, MovieItemListener movieItemListener) {
        this.context = context;
        this.movies = movies;
        this.movieItemListener = movieItemListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_movie, null);
        return new MovieAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MovieModel movie = movies.get(position);
        Glide.with(context)
                .load(AppConstant.BASE_IMAGE_URL + movie.getPosterPath())
                .into(holder.imageViewPoster);
        holder.textViewTitle.setText(movie.getTitle());
        holder.textViewVoteAverage.setText(String.valueOf(movie.getVoteAverage()));
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.movie_imageview_poster)
        ImageView imageViewPoster;
        @BindView(R.id.movie_textview_title)
        TextView textViewTitle;
        @BindView(R.id.movie_textview_voteaverage)
        TextView textViewVoteAverage;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            movieItemListener.onMovieClick(movies.get(getAdapterPosition()));
        }
    }

    public void replaceData(List<MovieModel> movies) {
        this.movies = movies;
        notifyDataSetChanged();
    }

    public interface MovieItemListener {
        void onMovieClick(MovieModel movieModel);
    }
}
