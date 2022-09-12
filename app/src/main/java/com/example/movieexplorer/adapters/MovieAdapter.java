package com.example.movieexplorer.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.L;
import com.bumptech.glide.Glide;
import com.example.movieexplorer.databinding.ItemMovieBinding;
import com.example.movieexplorer.models.Movie;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder>{
    private Context context;
    private List<Movie> movies;

    public MovieAdapter(Context context , List<Movie> movies){
        this.context = context;
        this.movies = movies;
    }


    @NonNull
    @Override
    public MovieAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemMovieBinding itemMovieBinding = ItemMovieBinding.inflate(layoutInflater,parent,false);
        return  new ViewHolder(itemMovieBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Movie movie = movies.get(position);
        holder.bind(movie);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }


    public class ViewHolder extends  RecyclerView.ViewHolder{
        ItemMovieBinding itemMovieBinding;

        public ViewHolder(@NonNull ItemMovieBinding itemMovieBinding) {
            super(itemMovieBinding.getRoot());
            this.itemMovieBinding = itemMovieBinding;
        }

        public void bind(Movie movie){
            itemMovieBinding.tvTitle.setText(movie.getTitle());
            itemMovieBinding.tvOverview.setText(movie.getOverview());
            String imageUrl = movie.getPosterPath();
            Glide.with(context).load(imageUrl).into(itemMovieBinding.ivPoster);

        }
    }
}
