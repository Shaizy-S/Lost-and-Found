package com.example.myapplication.ui.post;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.myapplication.R;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class PostDetailsActivity extends AppCompatActivity {
    private int defaultImageResource = R.drawable.baseline_image_24;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);

        // Get data from the intent
        String name = getIntent().getStringExtra("name");
        String title = getIntent().getStringExtra("title");
        String description = getIntent().getStringExtra("description");
        String category = getIntent().getStringExtra("category");
        String location = getIntent().getStringExtra("location");
        String date = getIntent().getStringExtra("date");
        String imageUrl = getIntent().getStringExtra("imageUrl"); // Updated to "imageUrl"

        TextView nameTextView = findViewById(R.id.nameTextView);
        TextView titleTextView = findViewById(R.id.titleTextView);
        TextView descriptionTextView = findViewById(R.id.descriptionTextView);
        TextView categoryTextView = findViewById(R.id.categoryTextView);
        TextView locationTextView = findViewById(R.id.locationTextView);
        TextView dateTextView = findViewById(R.id.dateTextView);
        ImageView imageView = findViewById(R.id.imageView);

        // Set text to TextViews, handling null values
        nameTextView.setText(TextUtils.isEmpty(name) ? "Unknown User" : name);
        titleTextView.setText(TextUtils.isEmpty(title) ? "No Title" : title);
        descriptionTextView.setText(TextUtils.isEmpty(description) ? "No Description" : description);
        categoryTextView.setText(TextUtils.isEmpty(category) ? "No Category" : category);
        locationTextView.setText(TextUtils.isEmpty(location) ? "No Location" : location);
        dateTextView.setText(TextUtils.isEmpty(date) ? "No Date" : date);

        if (!TextUtils.isEmpty(imageUrl)) {
            // Load image using AsyncTask to fetch image from URL
            new DownloadImageTask(imageView).execute(imageUrl);
        } else {
            // Set default drawable image
            imageView.setImageResource(defaultImageResource);
        }
    }

    private static class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        private ImageView imageView;

        public DownloadImageTask(ImageView imageView) {
            this.imageView = imageView;
        }

        protected Bitmap doInBackground(String... urls) {
            String imageUrl = urls[0];
            Bitmap bitmap = null;
            try {
                URL url = new URL(imageUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                bitmap = BitmapFactory.decodeStream(input);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
            }
        }
    }
}
