package com.example.mealz.data.local;

import com.example.mealz.model.Meal;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

public interface MealsLocalDataSource {
    Completable insertMeal(Meal meal);

    Completable deleteMeal(Meal meal);

    Observable<List<Meal>> getFavoriteMeals(String userId);

    Observable<List<Meal>> getPlannedMeals(String userId);

    Single<Meal> isFavMealExist(String userId, long networkMealId);
}
