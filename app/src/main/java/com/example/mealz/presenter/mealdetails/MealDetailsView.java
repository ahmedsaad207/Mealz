package com.example.mealz.presenter.mealdetails;

import com.example.mealz.model.Meal;

public interface MealDetailsView {
    void displayMeal(Meal meal);

    void changeImageResourceForFav();

    void onUserId(String userId);

    void onDeleteComplete();

    void onDeleteError(String error);

    void onInsertFavCompleted();

    void onInsertError(String error);

    void onFetchMealFailed(String error);

    void onInsertPlanCompleted();
}
