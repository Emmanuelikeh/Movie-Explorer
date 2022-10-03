package com.example.movieexplorer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


import com.example.movieexplorer.databinding.ActivityMainBinding;

import com.google.android.material.navigation.NavigationBarView;
import com.parse.ParseUser;


public class MainActivity extends AppCompatActivity {
    public ActivityMainBinding activityMainBinding;

    final FragmentManager fragmentManager = getSupportFragmentManager();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());

        activityMainBinding.bottomNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment;
                switch (item.getItemId()) {
                    case R.id.action_movie:
                        fragment = new NowPlayingFragment();
                        break;
                    case R.id.action_favourite:
                        fragment = new FavouriteFragment();
                        break;
                    default:
                        fragment = new NowPlayingFragment();
                        break;
                }
                fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.logout){
            Toast.makeText(this, "Logout successful", Toast.LENGTH_SHORT).show();
            onLogout();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void onLogout() {
        ParseUser.logOut();
        Intent intent   = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}