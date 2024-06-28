package com.example.myapplication.ui.found;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

public class ClaimMessageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_claim_message);

        // Retrieve data passed from the intent
        String name = getIntent().getStringExtra("name");
        String email = getIntent().getStringExtra("email");
        String description = getIntent().getStringExtra("description");
        String location = getIntent().getStringExtra("location");
        String color = getIntent().getStringExtra("color");

        // Find TextViews in the layout
        TextView nameTextView = findViewById(R.id.nameTextView);
        TextView emailTextView = findViewById(R.id.emailTextView);
        TextView descriptionTextView = findViewById(R.id.descriptionTextView);
        TextView locationTextView = findViewById(R.id.locationTextView);
        TextView colorTextView = findViewById(R.id.colorTextView);

        // Set text to TextViews
        nameTextView.setText(name);
        emailTextView.setText(email);
        descriptionTextView.setText(description);
        locationTextView.setText(location);
        colorTextView.setText(color);
    }
}
