package com.example.myapplication.ui.found;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.FoundAdapter;
import com.example.myapplication.Found;

import com.example.myapplication.R;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
public class FoundFragment extends Fragment implements FoundAdapter.OnPostClickListener {
    private RecyclerView foundRecyclerView;
    private FoundAdapter foundAdapter;
    private List<Found> founds;
    private SearchView searchView;

    public FoundFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post3, container, false);

        foundRecyclerView = view.findViewById(R.id.postsRecyclerView);
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

        foundRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        founds = new ArrayList<>();
        foundAdapter = new FoundAdapter(founds, this); // Initialize the adapter
        foundRecyclerView.setAdapter(foundAdapter); // Set the adapter to RecyclerView

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("item").whereEqualTo("category", "Found").orderBy("timestamp", Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w("PostFragment", "Listen failed.", e);
                            return;
                        }

                        if (queryDocumentSnapshots != null) {
                            founds.clear();
                            for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                                Found post = doc.toObject(Found.class);
                                founds.add(post);
                            }
                            foundAdapter.notifyDataSetChanged();
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
        Found clickedPost = founds.get(position);
        // You can pass data to a new activity using Intent
        Intent intent = new Intent(getContext(), FoundDetailsActivity.class);
        intent.putExtra("name", clickedPost.getName());
        intent.putExtra("title", clickedPost.getTitle());
        intent.putExtra("description", clickedPost.getDescription());
        intent.putExtra("category", clickedPost.getCategory());
        intent.putExtra("location", clickedPost.getLocation());
        intent.putExtra("date", clickedPost.getDate());

        // Add more data as needed
        startActivity(intent);
    }

    private void filterList(String text) {
        List<Found> filteredFound = new ArrayList<>();
        for (Found post : founds) {
            if (post.getTitle().toLowerCase().contains(text.toLowerCase())) {
                filteredFound.add(post);
            }
        }

        if (filteredFound.isEmpty())
            Toast.makeText(getContext(), getString(R.string.no_data_found), Toast.LENGTH_SHORT).show();
        else
            foundAdapter.setFilteredFound(filteredFound);
    }
}
