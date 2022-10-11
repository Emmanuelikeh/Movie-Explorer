package com.example.movieexplorer.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.movieexplorer.databinding.ItemMovieBinding;
import com.example.movieexplorer.models.Favorites;
import com.parse.DeleteCallback;
import com.parse.ParseException;

import java.util.List;

public class FavouriteMovieAdapter extends RecyclerView.Adapter<FavouriteMovieAdapter.ViewHolder>{
    public Context context;
    private List<Favorites> favoriteMovies;

    public FavouriteMovieAdapter(Context context, List<Favorites> favoriteMovies) {
        this.context = context;
        this.favoriteMovies = favoriteMovies;
    }

    @NonNull
    @Override
    public FavouriteMovieAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemMovieBinding itemMovieBinding = ItemMovieBinding.inflate(layoutInflater,parent,false);
        return new ViewHolder(itemMovieBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull FavouriteMovieAdapter.ViewHolder holder, int position) {
        Favorites favorites = favoriteMovies.get(position);
        holder.bind(favorites);
    }

    @Override
    public int getItemCount() {
        return favoriteMovies.size();
    }

    public void deleteItem(int position) {
        Favorites favorites = favoriteMovies.get(position);
        favorites.deleteInBackground(new DeleteCallback() {
            @Override
            public void done(ParseException e) {
                if(e != null){
                    Toast.makeText(context, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(context, "Removed from list", Toast.LENGTH_SHORT).show();
                }
            }
        });
        this.favoriteMovies.remove(position);
        notifyItemRemoved(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemMovieBinding itemMovieBinding;
        public ViewHolder(@NonNull ItemMovieBinding itemMovieBinding) {
            super(itemMovieBinding.getRoot());
            this.itemMovieBinding = itemMovieBinding;
        }

        public void bind(Favorites favorites) {
            int radius = 25;
            itemMovieBinding.tvOverview.setText(favorites.getDescription());
            itemMovieBinding.tvTitle.setText(favorites.getTitle());
            Glide.with(context).load(favorites.getPoster()).transform(new RoundedCorners(radius)).into(itemMovieBinding.ivPoster);
        }

    }
}
