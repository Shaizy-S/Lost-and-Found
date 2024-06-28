package com.example.myapplication.ui.found;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.myapplication.R;

public class ClaimItemDialogFragment extends DialogFragment {

    private EditText nameEditText, emailEditText, descriptionEditText, locationEditText, colorEditText, sendToEditText;
    private String posterEmail; // Added member variable to hold the poster's email

    // Method to create a new instance of the dialog fragment with poster's email
    public static ClaimItemDialogFragment newInstance(String posterEmail) {
        ClaimItemDialogFragment fragment = new ClaimItemDialogFragment();
        Bundle args = new Bundle();
        args.putString("posterEmail", posterEmail);
        fragment.setArguments(args);
        return fragment;
    }

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_claim_item, container, false);

        // Retrieve the poster's email from arguments
        posterEmail = getArguments().getString("posterEmail");

        nameEditText = view.findViewById(R.id.name);
        emailEditText = view.findViewById(R.id.email);
        descriptionEditText = view.findViewById(R.id.description);
        locationEditText = view.findViewById(R.id.location);
        colorEditText = view.findViewById(R.id.color);
        sendToEditText = view.findViewById(R.id.sendTo);

        Button sendButton = view.findViewById(R.id.button_send);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendClaim();
            }
        });

        return view;
    }

    private void sendClaim() {
        String name = nameEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String description = descriptionEditText.getText().toString();
        String location = locationEditText.getText().toString();
        String color = colorEditText.getText().toString();
        String sendTo = sendToEditText.getText().toString();

        // Email validation pattern
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (!name.isEmpty() && !email.isEmpty() && !description.isEmpty() && !sendTo.isEmpty()) {
            // Validate email format
            if (!sendTo.matches(emailPattern)) {
                sendToEditText.setError("Invalid email format");
                return;
            }

            if (!email.matches(emailPattern)) {
                emailEditText.setError("Invalid email format");
                return;
            }
            // Validate email format


            // Validate description length
            if (description.split("\\s+").length < 10) {
                descriptionEditText.setError("Description should be at least 10 words");
                return;
            }

            // Compose the email message
            String intro = String.format("Hello,\n\nI hope this message finds you well. My name is %s, and I'm reaching out to you regarding a lost item.", name);
            String details = String.format("\n\nThe item I've lost has the following details:\n- Description: %s\n- Color: %s\n- Location Last Seen: %s", description, color, location);
            String conclusion = "\n\nI kindly request you to consider these details. If you believe you may have come across the lost item matching this description, please let me know so we can discuss through chat further and hopefully reunite me with my object.\n\nRegards,\n" + name;

            String subject = "Lost Item Claim Request";
            String message = intro + details + conclusion;

            // Create an email intent
            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{sendTo});
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
            emailIntent.putExtra(Intent.EXTRA_TEXT, message);
            emailIntent.setType("message/rfc822");

            // Start the email activity
            try {
                startActivity(Intent.createChooser(emailIntent, "Send Email"));
                dismiss(); // Close the dialog fragment
            } catch (ActivityNotFoundException e) {
                Log.e("ClaimItemDialog", "No activity found to handle email intent", e);
                Toast.makeText(getActivity(), "No email app found", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getActivity(), "All fields are required.", Toast.LENGTH_SHORT).show();
        }
    }
}