package com.example.movieexplorer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.example.movieexplorer.databinding.ActivityDetailBinding;
import com.example.movieexplorer.databinding.ActivityMainBinding;
import com.example.movieexplorer.models.Movie;

import org.parceler.Parcels;

public class DetailActivity extends AppCompatActivity {
    public ActivityDetailBinding activityDetailBinding;
    Movie movie;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityDetailBinding = ActivityDetailBinding.inflate(getLayoutInflater());
        View view = activityDetailBinding.getRoot();
        setContentView(view);


        movie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));
        activityDetailBinding.tvDvTitle.setText(movie.getTitle());
        activityDetailBinding.tvDvOverview.setText(movie.getOverview());
        float voteAverage = movie.getVoteAverage().floatValue();
        activityDetailBinding.ratingBar.setRating(voteAverage/2.0f);
        Glide.with(this).load(movie.getBackdropPath()).into(activityDetailBinding.ivPosterBackDrop);
        Glide.with(this).load(movie.getPosterPath()).into(activityDetailBinding.ivPosterMainDrop);
    }
}