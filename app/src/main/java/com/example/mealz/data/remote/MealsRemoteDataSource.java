package com.example.mealz.data.remote;

import com.example.mealz.model.MealzResponse;

import io.reactivex.rxjava3.core.Single;

public interface MealsRemoteDataSource {
    Single<MealzResponse> getCategories();

    Single<MealzResponse> getMealsByCategory(String category);

    Single<MealzResponse> getMealById(long id);

    Single<MealzResponse> getIngredients();

    Single<MealzResponse> getMealsByArea(String area);

    Single<MealzResponse> getAreas();

    Single<MealzResponse> getRandomMeal();

    Single<MealzResponse> searchByIngredient(String ingredient);
}
