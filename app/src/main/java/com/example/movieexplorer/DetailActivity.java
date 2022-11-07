package com.example.movieexplorer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.movieexplorer.adapters.CommentAdapter;
import com.example.movieexplorer.databinding.ActivityDetailBinding;
import com.example.movieexplorer.models.Favorites;
import com.example.movieexplorer.models.Movie;
import com.example.movieexplorer.models.MovieComments;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.Headers;

public class DetailActivity extends YouTubeBaseActivity {
    public static final String YOUTUBE_API_KEY = "AIzaSyDG1VGGRTOwoEjYqE5AolHcj5nIPrVAgTM";
    public static final String TRAILERS_URL = "https://api.themoviedb.org/3/movie/%d/videos?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";
    public ActivityDetailBinding activityDetailBinding;
    Movie movie;
    Favorites favorites;
    protected List<MovieComments> movieCommentsList;
    protected CommentAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityDetailBinding = ActivityDetailBinding.inflate(getLayoutInflater());
        View view = activityDetailBinding.getRoot();
        setContentView(view);
        movie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));
        favorites = (Favorites) getIntent().getExtras().get("favorite");

        movieCommentsList = new ArrayList<>();
        adapter = new CommentAdapter(this, movieCommentsList);
        activityDetailBinding.rvComments.setAdapter(adapter);
        activityDetailBinding.rvComments.setLayoutManager(new LinearLayoutManager(this));
        activityDetailBinding.imgbtnComments.setTag("opened");


        activityDetailBinding.imgbtnComments.setOnClickListener(v -> {
            String resource = (String) activityDetailBinding.imgbtnComments.getTag();
            if(Objects.equals(resource, "opened")){
                activityDetailBinding.rvComments.setVisibility(View.GONE);
                activityDetailBinding.etCommentbox.setVisibility(View.GONE);
                activityDetailBinding.btnAddComment.setVisibility(View.GONE);
                activityDetailBinding.imgbtnComments.setTag("closed");
                activityDetailBinding.imgbtnComments.setImageResource(R.drawable.ic_baseline_arrow_drop_up_24);
            }
            else{
                activityDetailBinding.rvComments.setVisibility(View.VISIBLE);
                activityDetailBinding.etCommentbox.setVisibility(View.VISIBLE);
                activityDetailBinding.btnAddComment.setVisibility(View.VISIBLE);
                activityDetailBinding.imgbtnComments.setTag("opened");
                activityDetailBinding.imgbtnComments.setImageResource(R.drawable.ic_baseline_arrow_drop_down_24);
            }

        });

        if(movie != null){
            displayFavouriteButton(movie);
            activityDetailBinding.tvDvTitle.setText(movie.getTitle());
            activityDetailBinding.tvDvOverview.setText(movie.getOverview());
            activityDetailBinding.btnWatchTrailer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    activityDetailBinding.ytPlayer.setVisibility(View.VISIBLE);
                }
            });
            float voteAverage = movie.getVoteAverage().floatValue();
            activityDetailBinding.ratingBar.setRating(voteAverage/2.0f);
            Glide.with(this).load(movie.getBackdropPath()).into(activityDetailBinding.ivPosterBackDrop);
            Glide.with(this).load(movie.getPosterPath()).into(activityDetailBinding.ivPosterMainDrop);

            activityDetailBinding.btnAddFavourite.setOnClickListener(v -> {
                Favorites favorites = new Favorites();
                favorites.setPoster(movie.getPosterPath());
                favorites.setID(String.valueOf(movie.getMovieId()));
                favorites.setUser(ParseUser.getCurrentUser());
                favorites.setDescription(movie.getOverview());
                favorites.setTitle(movie.getTitle());
                favorites.setBackdrop(movie.getBackdropPath());
                favorites.setVoteAverage(movie.getVoteAverage());
                favorites.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e == null){
                            Toast.makeText(DetailActivity.this, "Added to Favourites", Toast.LENGTH_SHORT).show();
                            activityDetailBinding.btnAddFavourite.setVisibility(View.GONE);
                        }
                    }
                });
            });
            youtubeRequest(movie.getMovieId());
            queryComments(movie.getMovieId());
        }

        if(favorites != null){
            activityDetailBinding.tvDvTitle.setText(favorites.getTitle());
            activityDetailBinding.tvDvOverview.setText(favorites.getDescription());
            activityDetailBinding.btnWatchTrailer.setOnClickListener(v -> {
                activityDetailBinding.ytPlayer.setVisibility(View.VISIBLE);
            });
            float voteAverage = favorites.getVoteAverage().floatValue();
            activityDetailBinding.ratingBar.setRating(voteAverage/2.0f);
            Glide.with(this).load(favorites.getBackdrop()).into(activityDetailBinding.ivPosterBackDrop);
            Glide.with(this).load(favorites.getPoster()).into(activityDetailBinding.ivPosterMainDrop);
            youtubeRequest(Integer.parseInt(favorites.getId()));
            queryComments(Integer.parseInt(favorites.getId()));
        }

        activityDetailBinding.btnAddComment.setOnClickListener( v ->{
            MovieComments movieComments = new MovieComments();
            movieComments.setComment(activityDetailBinding.etCommentbox.getText().toString());
            movieComments.setMovieId(movie.getMovieId());
            movieComments.setUser(ParseUser.getCurrentUser());
            movieComments.saveInBackground();
            movieCommentsList.add(movieComments);
            adapter.notifyDataSetChanged();
            activityDetailBinding.etCommentbox.setText("");
        });
    }

    private void youtubeRequest(int id) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(String.format(TRAILERS_URL,id), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                try {
                    JSONArray results = json.jsonObject.getJSONArray("results");
                    if (results.length() == 0){
                        Log.i("warning", "no trailer found");
                        return;
                    }
                    JSONObject movieTrailerJson = results.getJSONObject(results.length()-2);
                    String youtubekey = movieTrailerJson.getString("key");
                    initializeYouTube(youtubekey);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.e("Faliure", "this shit failed to get the request");
            }
        });
    }

    private void displayFavouriteButton(Movie movie) {
        ParseQuery<Favorites> query = new ParseQuery<>(Favorites.class);
        query.whereEqualTo("Movie_Id",String.valueOf(movie.getMovieId()));
        query.findInBackground(new FindCallback<Favorites>() {
            @Override
            public void done(List<Favorites> objects, ParseException e) {
                if (e == null){
                    if (objects.size() == 0){
                        activityDetailBinding.btnAddFavourite.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

    }

    private void queryComments(int movieId) {
        ParseQuery<MovieComments> query = ParseQuery.getQuery(MovieComments.class);
        query.include(MovieComments.KEY_USERS);
        query.whereEqualTo(MovieComments.KEY_MOVIEID, movieId);
        query.findInBackground(new FindCallback<MovieComments>() {
            @Override
            public void done(List<MovieComments> objects, ParseException e) {
                if(e != null){
                    return;
                }
                movieCommentsList.addAll(objects);
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void initializeYouTube(String youtubekey) {
        activityDetailBinding.ytPlayer.initialize(YOUTUBE_API_KEY, new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                youTubePlayer.cueVideo(youtubekey);
            }
            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        });
    }
}