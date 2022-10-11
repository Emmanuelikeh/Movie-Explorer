package com.example.movieexplorer;

import android.app.Application;

import com.example.movieexplorer.models.Favorites;
import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(Favorites.class);
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("wsn2wDKDyNaGgzvyOalHQbzGO3UJMdA9ZpwfoRwH")
                .clientKey("F61TT6S77mZwJmQYm8pedJAvB9htBHw7Ev1uiwx0")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}
