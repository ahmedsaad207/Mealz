package com.example.mealz.data.file;

import com.example.mealz.model.Ingredient;
import com.example.mealz.model.Meal;

public interface MealFileDataSource {
    String getIngredientFilePath(String imageUrl, String folder);
}
