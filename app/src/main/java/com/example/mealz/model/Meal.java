package com.example.mealz.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;

import java.io.Serializable;
import java.util.List;


@Entity(tableName = "meals_table", primaryKeys = {"userId", "networkId", "date"})
public class Meal implements Serializable {

    @NonNull
    private long date;
    @NonNull
    private String userId;
    @NonNull
    private long networkId;
    private String name;
    private String urlImage;
    private String category;
    private String instructions;
    private String area;

    private String youtubeUrl;

    private List<Ingredient> ingredients;

    public Meal() {
    }

    public Meal(@NonNull long date, @NonNull String userId, @NonNull long networkId, String name, String urlImage, String category, String instructions, String area, String youtubeUrl, List<Ingredient> ingredients) {
        this.date = date;
        this.userId = userId;
        this.networkId = networkId;
        this.name = name;
        this.urlImage = urlImage;
        this.category = category;
        this.instructions = instructions;
        this.area = area;
        this.youtubeUrl = youtubeUrl;
        this.ingredients = ingredients;
    }

    public Meal(String name, String imageUrl, int resId) {
        setName(name);
        setUrlImage(imageUrl);
        setDate(resId);
    }


    @NonNull
    public long getDate() {
        return date;
    }

    public void setDate(@NonNull long date) {
        this.date = date;
    }

    public @NonNull String getUserId() {
        return userId;
    }

    public void setUserId(@NonNull String userId) {
        this.userId = userId;
    }

    @NonNull
    public long getNetworkId() {
        return networkId;
    }

    public void setNetworkId(@NonNull long networkId) {
        this.networkId = networkId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getYoutubeUrl() {
        return youtubeUrl;
    }

    public void setYoutubeUrl(String youtubeUrl) {
        this.youtubeUrl = youtubeUrl;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }
}
