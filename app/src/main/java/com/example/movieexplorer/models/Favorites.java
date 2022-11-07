package com.example.movieexplorer.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Favorites")
public class Favorites extends ParseObject {
    public static final String KEY_USER = "User_Favorite";
    public static final String KEY_TITLE = "Movie_Title";
    public static final String KEY_POSTER = "Movie_Poster";
    public static final String KEY_BACKDROP = "Movie_Backdrop";
    public static final String KEY_DESCRIPTION = "Movie_Description";
    public static final String KEY_ID = "Movie_Id";
    public static final String KEY_VoteAverage = "Vote_Average";

    public void setUser(ParseUser user){
        put(KEY_USER,user);
    }

    public void setTitle(String title){
        put(KEY_TITLE, title);
    }

    public void setPoster(String poster){
        put(KEY_POSTER, poster);
    }

    public void setBackdrop(String backdrop){put(KEY_BACKDROP, backdrop);}

    public void setDescription(String description){
        put(KEY_DESCRIPTION, description);
    }

    public void setID(String id){put(KEY_ID, id);}

    public void  setVoteAverage(Double voteAverage){put(KEY_VoteAverage, voteAverage);}

    public String getTitle(){
        return getString(KEY_TITLE);
    }
    public String getPoster(){
        return getString(KEY_POSTER);
    }
    public String getBackdrop(){ return  getString(KEY_BACKDROP);}
    public  String getId(){ return  getString(KEY_ID);}
    public String getDescription(){
        return getString(KEY_DESCRIPTION);
    }
    public Double getVoteAverage(){ return getDouble(KEY_VoteAverage);}

}
