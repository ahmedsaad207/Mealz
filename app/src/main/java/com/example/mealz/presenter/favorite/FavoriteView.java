package com.example.mealz.presenter.favorite;

import com.example.mealz.model.Meal;

import java.util.List;

public interface FavoriteView {
    void displayFavoriteMeals(List<Meal> meals);
}
