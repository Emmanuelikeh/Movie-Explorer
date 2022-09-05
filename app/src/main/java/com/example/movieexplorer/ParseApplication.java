package com.example.movieexplorer;

import android.app.Application;

import com.parse.Parse;

public class ParseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("wsn2wDKDyNaGgzvyOalHQbzGO3UJMdA9ZpwfoRwH")
                .clientKey("F61TT6S77mZwJmQYm8pedJAvB9htBHw7Ev1uiwx0")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}
