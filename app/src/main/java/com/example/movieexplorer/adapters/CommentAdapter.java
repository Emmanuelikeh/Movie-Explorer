package com.example.movieexplorer.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movieexplorer.databinding.ItemCommentBinding;
import com.example.movieexplorer.models.MovieComments;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    private Context context;
    private List<MovieComments> movieComments;

    public CommentAdapter(Context context, List<MovieComments> movieComments){
        this.context = context;
        this.movieComments = movieComments;
    }

    @NonNull
    @Override
    public CommentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemCommentBinding itemCommentBinding = ItemCommentBinding.inflate(layoutInflater, parent, false);
        return new ViewHolder(itemCommentBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.ViewHolder holder, int position) {
          MovieComments comments = movieComments.get(position);
          holder.bind(comments);
    }

    @Override
    public int getItemCount() {
        return movieComments.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ItemCommentBinding itemCommentBinding;
        public ViewHolder(@NonNull ItemCommentBinding itemCommentBinding) {
            super(itemCommentBinding.getRoot());
            this.itemCommentBinding = itemCommentBinding;
        }
        public void bind(MovieComments comments) {
            itemCommentBinding.tvComments.setText(comments.getComments());
            itemCommentBinding.tvUserName.setText(comments.getUser());
            itemCommentBinding.tvCreatedAt.setText(comments.getCreatedAtString());

        }
    }
}
