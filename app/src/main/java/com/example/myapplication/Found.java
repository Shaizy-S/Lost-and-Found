package com.example.myapplication;

public class Found {
    private String title;
    private String date;
    private String name;
    private String description;
    private String location;
    private String category;

    public Found() {
        // Default constructor required for Firestore deserialization
    }

    // Constructor
    public Found(String name, String title, String description, String location, String category, String date, String imageUrl) {
        this.name = name;
        this.title = title;
        this.description = description;
        this.location = location;
        this.category = category;
        this.date = date;
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
}


