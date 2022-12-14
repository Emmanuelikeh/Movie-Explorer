package com.example.movieexplorer.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel
public class Movie {
    String posterPath;
    String title;
    String overview;
    int movieId;
    String backdropPath;
    Double voteAverage;

    public Movie(JSONObject jsonObject) throws JSONException {
        backdropPath = jsonObject.getString("backdrop_path");
        posterPath = jsonObject.getString("poster_path");
        title = jsonObject.getString("title");
        movieId = jsonObject.getInt("id");
        overview = jsonObject.getString("overview");
        voteAverage = jsonObject.getDouble("vote_average");

    }
    // no-arg, empty constructor required for Parceler
    public Movie() {}

    public static List<Movie> fromJsonArray(JSONArray movieJsonArray) throws JSONException {
        List<Movie> movies = new ArrayList<>();
        for( int i =0; i < movieJsonArray.length(); i++){
            movies.add( new Movie(movieJsonArray.getJSONObject(i)));
        }
        return movies;
    }

    public String getPosterPath() {
        return String.format("https://image.tmdb.org/t/p/w342/%s",posterPath);
    }

    public String getBackdropPath(){
        return String.format("https://image.tmdb.org/t/p/w342/%s",backdropPath);
    }

    public int getMovieId(){
        return movieId;
    }

    public String getTitle() {
        return title;
    }
    public String getOverview() {
        return overview;
    }
    public Double getVoteAverage(){ return voteAverage;}
}
