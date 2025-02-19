package com.example.mealz.view;

import com.example.mealz.model.Meal;

public interface OnMealItemClickListener {
    void navigateToMealDetails(Meal meal);

    void navigateToMealsList(String name, int type);

    void removeMealFromFavorites(Meal meal);
}
