package com.example.myapplication.ui.found;

import android.os.Bundle;
import android.text.TextUtils;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;


public class FoundDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details1);

        // Get data from the intent
        String name = getIntent().getStringExtra("name");
        String title = getIntent().getStringExtra("title");
        String description = getIntent().getStringExtra("description");
        String category = getIntent().getStringExtra("category");
        String location = getIntent().getStringExtra("location");
        String date = getIntent().getStringExtra("date");

        TextView nameTextView = findViewById(R.id.nameTextView);
        TextView titleTextView = findViewById(R.id.titleTextView);
        TextView descriptionTextView = findViewById(R.id.descriptionTextView);
        TextView categoryTextView = findViewById(R.id.categoryTextView);
        TextView locationTextView = findViewById(R.id.locationTextView);
        TextView dateTextView = findViewById(R.id.dateTextView);


        // Set text to TextViews, handling null values
        nameTextView.setText(TextUtils.isEmpty(name) ? "Unknown User" : name);
        titleTextView.setText(TextUtils.isEmpty(title) ? "No Title" : title);
        descriptionTextView.setText(TextUtils.isEmpty(description) ? "No Description" : description);
        categoryTextView.setText(TextUtils.isEmpty(category) ? "No Category" : category);
        locationTextView.setText(TextUtils.isEmpty(location) ? "No Location" : location);
        dateTextView.setText(TextUtils.isEmpty(date) ? "No Date" : date);

        Button claimButton = findViewById(R.id.button_claim);
        claimButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show the claim form dialog or fragment
                String posterEmail = getIntent().getStringExtra("name"); // Assuming "name" contains the poster's email
                showClaimForm(posterEmail);
            }
        });

    }
    private void showClaimForm(String posterEmail) {
        // Create and show the claim form dialog or fragment
        ClaimItemDialogFragment claimDialog = ClaimItemDialogFragment.newInstance(posterEmail);
        claimDialog.show(getSupportFragmentManager(), "claim_dialog");
    }


}
