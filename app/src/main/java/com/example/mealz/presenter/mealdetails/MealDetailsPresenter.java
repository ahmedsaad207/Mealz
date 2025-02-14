package com.example.mealz.presenter.mealdetails;

import com.example.mealz.model.Ingredient;
import com.example.mealz.model.Meal;
import com.example.mealz.model.NetworkMeal;

import java.util.List;

public interface MealDetailsPresenter {
    void getMealById(long id);
    void insertFavMeal(Meal meal);
    void insertPlanMeal(Meal meal);
    void insertMeal(Meal meal, long date);

    void downloadMealImage(String mealImageUrl);

    void downloadIngredientImages(List<Ingredient> ingredients);
}
