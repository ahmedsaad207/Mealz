package com.example.mealz.presenter.favorite;

import com.example.mealz.data.MealsRepository;
import com.example.mealz.model.Ingredient;
import com.example.mealz.model.Meal;

public interface FavoritePresenter {
    void getFavoriteMeals(String userId);

    String getIngredientFilePath(String imageUrl, String folder);
    void deleteMeal(Meal meal);
}
