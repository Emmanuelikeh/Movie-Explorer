package com.example.movieexplorer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.movieexplorer.adapters.CommentAdapter;
import com.example.movieexplorer.databinding.ActivityDetailBinding;
import com.example.movieexplorer.databinding.ActivityMainBinding;
import com.example.movieexplorer.models.Movie;
import com.example.movieexplorer.models.MovieComments;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;


public class DetailActivity extends YouTubeBaseActivity {
    public static final String YOUTUBE_API_KEY = "AIzaSyDG1VGGRTOwoEjYqE5AolHcj5nIPrVAgTM";
    public static final String TRAILERS_URL = "https://api.themoviedb.org/3/movie/%d/videos?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";
    public ActivityDetailBinding activityDetailBinding;
    Movie movie;
    protected List<MovieComments> movieCommentsList;
    protected CommentAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityDetailBinding = ActivityDetailBinding.inflate(getLayoutInflater());
        View view = activityDetailBinding.getRoot();
        setContentView(view);
        movie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));
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

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(String.format(TRAILERS_URL, movie.getMovieId()), new JsonHttpResponseHandler() {
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

        movieCommentsList = new ArrayList<>();
        adapter = new CommentAdapter(this, movieCommentsList);
        activityDetailBinding.rvComments.setAdapter(adapter);
        activityDetailBinding.rvComments.setLayoutManager(new LinearLayoutManager(this));
        queryComments();

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

    private void queryComments() {
        ParseQuery<MovieComments> query = ParseQuery.getQuery(MovieComments.class);
        query.include(MovieComments.KEY_USERS);
        query.whereEqualTo(MovieComments.KEY_MOVIEID, movie.getMovieId());
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