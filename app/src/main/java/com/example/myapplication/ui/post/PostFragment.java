package com.example.myapplication.ui.post;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Post;
import com.example.myapplication.PostsAdapter;
import com.example.myapplication.R;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class PostFragment extends Fragment implements PostsAdapter.OnPostClickListener {
    private RecyclerView postsRecyclerView;
    private PostsAdapter postsAdapter;
    private List<Post> posts;
    private SearchView searchView;

    private int defaultImageResource = R.drawable.baseline_image_24;

    public PostFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post, container, false);

        postsRecyclerView = view.findViewById(R.id.postsRecyclerView);
        searchView = view.findViewById(R.id.searchView);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return true;
            }
        });

        postsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        posts = new ArrayList<>();

        postsAdapter = new PostsAdapter(posts, defaultImageResource, this);
        postsRecyclerView.setAdapter(postsAdapter);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("item").whereEqualTo("category", "Lost").orderBy("timestamp", Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w("PostFragment", "Listen failed.", e);
                            return;
                        }

                        if (queryDocumentSnapshots != null) {
                            posts.clear();
                            for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                                Post post = doc.toObject(Post.class);
                                posts.add(post);
                            }
                            postsAdapter.notifyDataSetChanged();
                        } else {
                            Log.d("PostFragment", "Current data: null");
                        }
                    }
                });
        return view;
    }

    @Override
    public void onPostClick(int position) {
        // Handle item click here
        // For example, open a new activity with details of the clicked post
        Post clickedPost = posts.get(position);
        // You can pass data to a new activity using Intent
        Intent intent = new Intent(getContext(), PostDetailsActivity.class);
        intent.putExtra("name", clickedPost.getName());
        intent.putExtra("title", clickedPost.getTitle());
        intent.putExtra("description", clickedPost.getDescription());
        intent.putExtra("category", clickedPost.getCategory());
        intent.putExtra("location", clickedPost.getLocation());
        intent.putExtra("date", clickedPost.getDate());
        intent.putExtra("imageUrl", clickedPost.getImageUrl()); // Updated to "imageUrl"

        // Add more data as needed
        startActivity(intent);
    }

    private void filterList(String text) {
        List<Post> filteredPosts = new ArrayList<>();
        for (Post post : posts) {
            if (post.getTitle().toLowerCase().contains(text.toLowerCase())) {
                filteredPosts.add(post);
            }
        }

        if (filteredPosts.isEmpty())
            Toast.makeText(getContext(), getString(R.string.no_data_found), Toast.LENGTH_SHORT).show();
        else
            postsAdapter.setFilteredPosts(filteredPosts);
    }
}
