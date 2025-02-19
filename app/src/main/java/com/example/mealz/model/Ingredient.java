package com.example.mealz.model;

public class Ingredient {

    private String name;
    private String measure;
    private String imageUrl;

    public Ingredient() {}

    public Ingredient(String name, String measure, String imageUrl) {
        this.name = name;
        this.measure = measure;
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public String getMeasure() {
        return measure;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String path) {
        this.imageUrl = path;
    }

}
