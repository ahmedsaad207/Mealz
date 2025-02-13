package com.example.mealz.model;

public class Ingredient {

    private final String name;
    private final String measure;
    private final String imageUrl;

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


}
