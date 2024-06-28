package com.example.myapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.PostViewHolder> {
    private List<Post> posts;
    private int defaultImageResource = R.drawable.baseline_image_24;
    private OnPostClickListener clickListener;

    public PostsAdapter(List<Post> posts, int defaultImageResource, OnPostClickListener clickListener) {
        this.posts = posts;
        this.defaultImageResource = defaultImageResource;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item, parent, false);
        return new PostViewHolder(view, clickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.nameTextView.setText(post.getName());
        holder.titleTextView.setText(post.getTitle());
        holder.descriptionTextView.setText(post.getDescription());
        holder.locationTextView.setText(post.getLocation());
        holder.dateTextView.setText(post.getDate());

        // Set category based on the post
        holder.categoryTextView.setText(post.getCategory());

        if (!TextUtils.isEmpty(post.getImageUrl())) {
            // Load image from URL
            new DownloadImageTask(holder.imageView).execute(post.getImageUrl());
        } else {
            // Set default drawable image
            holder.imageView.setImageResource(defaultImageResource);
        }
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    static class PostViewHolder extends RecyclerView.ViewHolder {
        TextView dateTextView;
        TextView nameTextView;
        TextView titleTextView;
        TextView descriptionTextView;
        TextView locationTextView;
        TextView categoryTextView;
        ImageView imageView;

        PostViewHolder(@NonNull View itemView, final OnPostClickListener clickListener) {
            super(itemView);
            nameTextView=itemView.findViewById(R.id.nameTextView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            locationTextView = itemView.findViewById(R.id.locationTextView);
            categoryTextView = itemView.findViewById(R.id.categoryTextView);
            dateTextView=itemView.findViewById(R.id.dateTextView);
            imageView = itemView.findViewById(R.id.imageView);

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

    private static class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        private final ImageView imageView;

        public DownloadImageTask(ImageView imageView) {
            this.imageView = imageView;
        }

        protected Bitmap doInBackground(String... urls) {
            String url = urls[0];
            try {
                URL imageUrl = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) imageUrl.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream inputStream = connection.getInputStream();
                return BitmapFactory.decodeStream(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
            }
        }
    }

    // Define an interface for item click handling
    public interface OnPostClickListener {
        void onPostClick(int position);
    }

    public void setFilteredPosts(List<Post> filteredPosts) {
        this.posts = filteredPosts;
        notifyDataSetChanged();
    }
}
