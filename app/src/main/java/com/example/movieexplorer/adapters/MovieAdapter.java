package com.example.movieexplorer.adapters;

import android.content.Context;
import android.content.res.Configuration;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.L;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.movieexplorer.R;
import com.example.movieexplorer.databinding.ItemMovieBinding;
import com.example.movieexplorer.models.Movie;

import java.util.List;
import java.util.Objects;

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
            if(Objects.equals(movie.getOverview(), "")){
                itemMovieBinding.tvOverview.setText(R.string.no_overview);
            }
            else{
                itemMovieBinding.tvOverview.setText(movie.getOverview());
            }

            int radius = 25; // corner radius, higher value = more rounded
//            int margin = 10; // crop margin, set to 0 for corners with no crop

            String imageUrl;
            if(context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                imageUrl = movie.getBackdropPath();
                Glide.with(context)
                        .load(imageUrl)
//                         .centerCrop()
//                    .override(Target.SIZE_ORIGINAL* 2, Target.SIZE_ORIGINAL*2)
                        .transform(new RoundedCorners(radius))
                        .into(itemMovieBinding.ivPoster);
            }
            else{
                imageUrl = movie.getPosterPath();
                Glide.with(context)
                        .load(imageUrl)
                        .fitCenter()
//                    .override(Target.SIZE_ORIGINAL* 2, Target.SIZE_ORIGINAL*2)
                        .transform(new RoundedCorners(radius))
                        .into(itemMovieBinding.ivPoster);
            }

        }
    }
}
