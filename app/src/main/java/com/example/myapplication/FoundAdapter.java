package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class FoundAdapter extends RecyclerView.Adapter<FoundAdapter.PostViewHolder> {
    private List<Found> founds;

    private OnPostClickListener clickListener;

    public FoundAdapter(List<Found> founds, OnPostClickListener clickListener) {
        this.founds = founds;

        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item1, parent, false);
        return new PostViewHolder(view, clickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Found post = founds.get(position);
        holder.nameTextView.setText(post.getName());
        holder.titleTextView.setText(post.getTitle());
        holder.descriptionTextView.setText(post.getDescription());
        holder.locationTextView.setText(post.getLocation());
        holder.dateTextView.setText(post.getDate());

        // Set category based on the post
        holder.categoryTextView.setText(post.getCategory());


    }

    @Override
    public int getItemCount() {
        return founds.size();
    }

    static class PostViewHolder extends RecyclerView.ViewHolder {
        TextView dateTextView;
        TextView nameTextView;
        TextView titleTextView;
        TextView descriptionTextView;
        TextView locationTextView;
        TextView categoryTextView;


        PostViewHolder(@NonNull View itemView, final OnPostClickListener clickListener) {
            super(itemView);
            nameTextView=itemView.findViewById(R.id.nameTextView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            locationTextView = itemView.findViewById(R.id.locationTextView);
            categoryTextView = itemView.findViewById(R.id.categoryTextView);
            dateTextView=itemView.findViewById(R.id.dateTextView);


            itemView.setOnClickListener(v -> {
                if (clickListener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        clickListener.onPostClick(position);
                    }
                }
            });
        }
    }



    // Define an interface for item click handling
    public interface OnPostClickListener {
        void onPostClick(int position);
    }

    public void setFilteredFound(List<Found> filteredFound) {
        this.founds = filteredFound;
        notifyDataSetChanged();
    }
}
