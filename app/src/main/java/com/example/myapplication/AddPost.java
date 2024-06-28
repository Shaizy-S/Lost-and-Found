package com.example.myapplication;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import android.app.DatePickerDialog;
import android.widget.DatePicker;
import android.widget.EditText;

public class AddPost extends Fragment {

    private static final int REQUEST_IMAGE_PICK = 101;

    private EditText editTextTitle, editTextDescription, editTextLocation, editTextName, editTextDate;
    private ImageView imageView;
    private Button buttonChooseImage, buttonPost;
    private RadioGroup categoryRadioGroup;
    private RadioButton lostRadioButton, foundRadioButton;

    private FirebaseFirestore db;
    private StorageReference storageRef;

    private ActivityResultLauncher<Intent> imagePickerLauncher;
    private Bitmap imageBitmap = null; // Store the selected image as a bitmap

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_post, container, false);

        db = FirebaseFirestore.getInstance();
        storageRef = FirebaseStorage.getInstance().getReference();

        editTextTitle = view.findViewById(R.id.editTextTitle);
        editTextDescription = view.findViewById(R.id.editTextDescription);
        editTextLocation = view.findViewById(R.id.editTextLocation);
        editTextName = view.findViewById(R.id.editTextName);
        editTextDate = view.findViewById(R.id.editTextDate);

        imageView = view.findViewById(R.id.imageView);
        buttonChooseImage = view.findViewById(R.id.buttonChooseImage);
        buttonPost = view.findViewById(R.id.buttonPost);
        categoryRadioGroup = view.findViewById(R.id.categoryRadioGroup);
        lostRadioButton = view.findViewById(R.id.lostRadioButton);
        foundRadioButton = view.findViewById(R.id.foundRadioButton);

        // Set up the date picker dialog
        editTextDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    requireContext(),
                    (view1, selectedYear, selectedMonth, selectedDay) -> {
                        String formattedDate = String.format(Locale.getDefault(), "%02d/%02d/%d", selectedDay, selectedMonth + 1, selectedYear);
                        editTextDate.setText(formattedDate);
                    },
                    year, month, day
            );



            datePickerDialog.show();
        });

        buttonChooseImage.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_IMAGE_PICK);
            } else {
                openGallery();
            }
        });

        buttonPost.setOnClickListener(v -> {
            String title = editTextTitle.getText().toString();
            String description = editTextDescription.getText().toString();
            String location = editTextLocation.getText().toString();
            String name = editTextName.getText().toString();
            String date = editTextDate.getText().toString();

            if (name.isEmpty() || title.isEmpty() || description.isEmpty() || location.isEmpty() || date.isEmpty()) {
                Toast.makeText(requireContext(), "Username, title, description, location, or date cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!isValidEmail(name)) {
                Toast.makeText(requireContext(), "Please enter a valid email address as the username", Toast.LENGTH_SHORT).show();
                return;
            }

            // Determine selected category
            String category;
            if (lostRadioButton.isChecked()) {
                category = "Lost";
            } else if (foundRadioButton.isChecked()) {
                category = "Found";
            } else {
                Toast.makeText(requireContext(), "Please select a category", Toast.LENGTH_SHORT).show();
                return;
            }

            Map<String, Object> post = new HashMap<>();
            post.put("name", name);
            post.put("title", title);
            post.put("description", description);
            post.put("location", location);
            post.put("category", category);
            post.put("date", date);

            uploadImageAndSaveToFirestore(post);
        });

        imagePickerLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                Uri imageUri = result.getData().getData();
                try {
                    imageBitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), imageUri);
                    imageView.setImageBitmap(imageBitmap);
                } catch (Exception e) {
                    Toast.makeText(getContext(), "Failed to load image: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

        return view;
    }

    private boolean isValidEmail(String name) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        return name.matches(emailPattern);
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        imagePickerLauncher.launch(intent);
    }

    private void uploadImageAndSaveToFirestore(Map<String, Object> post) {
        if (imageBitmap == null) {
            Toast.makeText(requireContext(), "No image selected, adding post without image.", Toast.LENGTH_SHORT).show();
            addPostToFirestore(post);
            return;
        }

        String imageName = UUID.randomUUID().toString() + ".jpg";
        StorageReference imageRef = storageRef.child("images/" + imageName);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = imageRef.putBytes(data);
        uploadTask.addOnSuccessListener(taskSnapshot -> {
            imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                String imageUrl = uri.toString();
                post.put("imageUrl", imageUrl);
                addPostToFirestore(post);
            }).addOnFailureListener(e -> {
                Toast.makeText(requireContext(), "Failed to get image URL, adding post without image.", Toast.LENGTH_SHORT).show();
                addPostToFirestore(post);
            });
        }).addOnFailureListener(e -> {
            Toast.makeText(requireContext(), "image: " , Toast.LENGTH_SHORT).show();
            addPostToFirestore(post); // Optionally add post without the image
        });
    }

    private void addPostToFirestore(Map<String, Object> post) {
        post.put("timestamp", FieldValue.serverTimestamp());

        db.collection("item").add(post).addOnSuccessListener(documentReference -> {
            Toast.makeText(requireContext(), "Post added successfully", Toast.LENGTH_SHORT).show();
            editTextName.setText("");
            editTextTitle.setText("");
            editTextDescription.setText("");
            editTextLocation.setText("");
            editTextDate.setText("");
            imageView.setImageDrawable(null);
            imageBitmap = null; // Clear the bitmap after upload
        }).addOnFailureListener(e -> {
            Toast.makeText(requireContext(), "post", Toast.LENGTH_SHORT).show();
        });
    }
}