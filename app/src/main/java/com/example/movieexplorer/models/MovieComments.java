package com.example.movieexplorer.models;


import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

@ParseClassName("MovieComments")
public class MovieComments extends ParseObject {
    public static final String KEY_MOVIEID = "movieId";
    public static final String KEY_COMMENTS = "Comments";
    public static final String KEY_USERS = "Users";

    public void setUser(ParseUser user){
        put(KEY_USERS,user);
    }
    public void setComment(String comment){
        put(KEY_COMMENTS,comment);

    }
    public void setMovieId(int movieId){
        put(KEY_MOVIEID,movieId);
    }

    public String getComments(){
        return getString(KEY_COMMENTS);
    }
    public int getMovieId(){
        return (int) getNumber(KEY_MOVIEID);
    }
    public String getUser(){
        return Objects.requireNonNull(getParseUser(KEY_USERS)).getUsername();
    }
    public String getCreatedAtString(){
        DateFormat parser = new SimpleDateFormat("yyyy-MM-dd-H-m", Locale.getDefault());
        Date createAt = getCreatedAt();
        String strDate = null;
        if (createAt != null) {
            strDate = parser.format(createAt);
            return strDate;
        }
        return "";

    }


}
