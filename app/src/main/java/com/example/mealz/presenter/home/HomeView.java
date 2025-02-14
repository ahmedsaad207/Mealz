package com.example.mealz.presenter.home;

import com.example.mealz.model.Category;
import com.example.mealz.model.Meal;
import com.example.mealz.model.NetworkMeal;

import java.util.ArrayList;
import java.util.List;

public interface HomeView {
    void displayCategories(ArrayList<Category> categories);
    void displayAreas(List<Meal> meals);
    void displayDailyInspiration(List<Meal> meals);
}
