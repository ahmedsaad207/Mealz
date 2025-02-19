package com.example.mealz.model;

public class Area {
    private String areaName;

    private int ImageResourceId;

    public Area(String areaName, int imageResourceId) {
        this.areaName = areaName;
        ImageResourceId = imageResourceId;
    }

    public String getAreaName() {
        return areaName;
    }

    public int getImageResourceId() {
        return ImageResourceId;
    }


}
