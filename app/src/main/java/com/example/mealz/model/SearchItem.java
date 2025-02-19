package com.example.mealz.model;

public class SearchItem {

    private String name;
    private String imageUrl;
    private int type;

    private int resId;

    public SearchItem(String name, String imageUrl, int type) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.type = type;
    }

    public SearchItem(String name, int resId, int type) {
        this.name = name;
        this.type = type;
        this.resId = resId;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getType() {
        return type;
    }

    public int getResId() {
        return resId;
    }
}
