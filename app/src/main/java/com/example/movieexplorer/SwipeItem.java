package com.example.movieexplorer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movieexplorer.adapters.FavouriteMovieAdapter;

public class SwipeItem extends ItemTouchHelper.SimpleCallback {

    private FavouriteMovieAdapter favouriteMovieAdapter;
    private Drawable icon;
    private final ColorDrawable background;

    public SwipeItem(FavouriteMovieAdapter favouriteMovieAdapter){
        super(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.favouriteMovieAdapter = favouriteMovieAdapter;
        icon = ContextCompat.getDrawable(favouriteMovieAdapter.context.getApplicationContext(), R.drawable.ic_baseline_delete_24);
        background = new ColorDrawable(Color.RED);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        int position = viewHolder.getAdapterPosition();
        this.favouriteMovieAdapter.deleteItem(position);
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        View itemView = viewHolder.itemView;
        int iconMargin = (itemView.getHeight() - icon.getIntrinsicHeight())/2;

        if(dX>0){
            background.setBounds(itemView.getLeft(),itemView.getTop(), (int)dX,itemView.getBottom());
            icon.setBounds(itemView.getLeft() + iconMargin, itemView.getTop() + iconMargin, itemView.getLeft() + iconMargin + icon.getIntrinsicWidth(), itemView.getBottom() - iconMargin);
        }
        else{
            background.setBounds(itemView.getRight() + (int)dX,itemView.getTop(),itemView.getRight(),itemView.getBottom());
            icon.setBounds(itemView.getRight() - iconMargin - icon.getIntrinsicWidth(), itemView.getTop() + iconMargin, itemView.getRight() - iconMargin, itemView.getBottom() - iconMargin);
        }
        background.draw(c);
        c.save();

        if(dX>0){
            c.clipRect(itemView.getLeft(),itemView.getTop(), (int)dX,itemView.getBottom());
        }
        else {
            c.clipRect(itemView.getRight() + (int)dX,itemView.getTop(),itemView.getRight(),itemView.getBottom());
        }

        icon.draw(c);
        c.restore();
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

    }
}
