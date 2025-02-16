package com.example.mealz.presenter.home;

import com.example.mealz.model.Category;
import com.example.mealz.model.Ingredient;
import com.example.mealz.model.Meal;
import com.example.mealz.model.NetworkMeal;
import com.example.mealz.model.SearchItem;

import java.util.ArrayList;
import java.util.List;

public interface HomeView {
    void displayCategories(ArrayList<Category> categories);
    void displayAreas(List<Meal> meals);
    void displayDailyInspiration(List<Meal> meals);

    void onIngredientsListReady(List<Ingredient> ingredients);

    void displaySearchItems(List<SearchItem> meals);
}
