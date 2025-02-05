package com.example.mealz.model;

import com.google.gson.annotations.SerializedName;

public class Category {
    public long idCategory;
    @SerializedName("strCategory")
    public String categoryName;
    @SerializedName("strCategoryDescription")
    public String categoryDescription;
    @SerializedName("strCategoryThumb")
    public String categoryImage;

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryDescription() {
        return categoryDescription;
    }

    public void setCategoryDescription(String categoryDescription) {
        this.categoryDescription = categoryDescription;
    }

    public String getCategoryImage() {
        return categoryImage;
    }

    public void setCategoryImage(String categoryImage) {
        this.categoryImage = categoryImage;
    }

    public long getIdCategory() {
        return idCategory;
    }
}
