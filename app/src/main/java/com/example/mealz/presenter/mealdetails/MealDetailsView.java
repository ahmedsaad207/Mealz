package com.example.mealz.presenter.mealdetails;

import com.example.mealz.model.Meal;
import com.example.mealz.model.NetworkMeal;

import java.util.List;

public interface MealDetailsView {
    void displayMeal(Meal meal);
    void onSuccess();

    void insertMeal();

    void changeImageResourceForFav();
}
