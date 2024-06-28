package com.example.myapplication;

public class Post {
    private String title;
    private String date;
    private String name;
    private String description;
    private String location;
    private String category;
    private String imageUrl;

    public Post() {
        // Default constructor required for Firestore deserialization
    }

    // Constructor
    public Post(String name, String title, String description, String location, String category, String date, String imageUrl) {
        this.name = name;
        this.title = title;
        this.description = description;
        this.location = location;
        this.category = category;
        this.date = date;
        this.imageUrl = imageUrl;
    }

    // Getters and setters (optional)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
