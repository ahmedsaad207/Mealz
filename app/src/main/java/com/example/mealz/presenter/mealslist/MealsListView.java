package com.example.mealz.presenter.mealslist;

import com.example.mealz.model.Meal;
import com.example.mealz.model.NetworkMeal;

import java.util.ArrayList;
import java.util.List;

public interface MealsListView {
    void displayMeals(List<Meal> meals);
}
