package com.example.mealz.model;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import androidx.core.content.res.ResourcesCompat;

import java.util.ArrayList;
import java.util.List;

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
