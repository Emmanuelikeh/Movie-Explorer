package com.example.movieexplorer.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Favorites")
public class Favorites extends ParseObject {
    public static final String KEY_USER = "User_Favorite";
    public static final String KEY_TITLE = "Movie_Title";
    public static final String KEY_POSTER = "Movie_Poster";
    public static final String KEY_DESCRIPTION = "Movie_Description";

    public void setUser(ParseUser user){
        put(KEY_USER,user);
    }

    public void setTitle(String title){
        put(KEY_TITLE, title);
    }

    public void setPoster(String poster){
        put(KEY_POSTER, poster);
    }

    public void setDescription(String description){
        put(KEY_DESCRIPTION, description);
    }

    public String getTitle(){
        return getString(KEY_TITLE);
    }
    public String getPoster(){
        return getString(KEY_POSTER);
    }

    public String getDescription(){
        return getString(KEY_DESCRIPTION);
    }

}
