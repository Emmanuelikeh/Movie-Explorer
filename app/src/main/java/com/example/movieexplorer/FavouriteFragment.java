package com.example.movieexplorer;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.movieexplorer.adapters.FavouriteMovieAdapter;
import com.example.movieexplorer.databinding.FragmentFavouriteBinding;
import com.example.movieexplorer.models.Favorites;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


public class FavouriteFragment extends Fragment {
    FragmentFavouriteBinding fragmentFavouriteBinding;
    List<Favorites> favoriteMovies;

    public FavouriteFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_favourite, container, false);
        fragmentFavouriteBinding = FragmentFavouriteBinding.inflate(inflater,container,false);
        return fragmentFavouriteBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        favoriteMovies = new ArrayList<>();
        FavouriteMovieAdapter favouriteMovieAdapter = new FavouriteMovieAdapter(getContext(), favoriteMovies);
        fragmentFavouriteBinding.rvFavoriteMovies.setAdapter(favouriteMovieAdapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeItem(favouriteMovieAdapter));
        itemTouchHelper.attachToRecyclerView(fragmentFavouriteBinding.rvFavoriteMovies);
        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(getContext());
        fragmentFavouriteBinding.rvFavoriteMovies.setLayoutManager(linearLayoutManager);


        ParseQuery<Favorites> query  = ParseQuery.getQuery(Favorites.class);
        query.whereEqualTo(Favorites.KEY_USER, ParseUser.getCurrentUser());
        query.findInBackground(new FindCallback<Favorites>() {
            @Override
            public void done(List<Favorites> objects, ParseException e) {
                if(e != null){
                  Log.i("Failure", e.getMessage().toString());
                }
                else{
                    favoriteMovies.addAll(objects);
                    favouriteMovieAdapter.notifyDataSetChanged();
                }
            }
        });

    }
}