package com.example.mealz.data;

import com.example.mealz.data.backup.BackUpRemoteDataSourceImpl;
import com.example.mealz.model.Ingredient;
import com.example.mealz.model.Meal;
import com.example.mealz.model.MealzResponse;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

public interface MealsRepository {
    Single<MealzResponse> getCategories();

    Single<MealzResponse> getMealsByCategory(String category);

    Single<MealzResponse> getMealById(long id);

    Single<MealzResponse> getIngredients();

    Single<MealzResponse> getMealsByArea(String area);

    Single<MealzResponse> getAreas();

    Single<MealzResponse> getRandomMeal();

    Single<MealzResponse> searchByIngredient(String ingredient);

    Completable insertMeal(Meal meal);

    Completable deleteMeal(Meal meal);

    Observable<List<Meal>> getFavoriteMeals(String userId);

    Observable<List<Meal>> getPlannedMeals(String userId);

    Single<Meal> isFavMealExist(String userId, long networkMealId);

    void downloadMealImage(Meal mealImageUrl);

    Completable downloadIngredientImages(List<Ingredient> ingredients);

    String getIngredientFilePath(String imageUrl, String folder);

    io.reactivex.Observable<String> getUserId();

    void setUserId(String userId);

    io.reactivex.Observable<String> getUsername();

    void setUsername(String username);

    void clearUserId();

    void clearUsername();

    void setRememberMe(boolean value);

    Completable insertAllMeal(List<Meal> meals);

    Completable deletePlanMeal(long networkId, String userId, long date);

    void backUp(Meal meal);

    void removeMealFromFavorites(Meal meal, BackUpRemoteDataSourceImpl.OnMealRemovedListener onMealRemovedListener);

    void retrieveBackupMeals(String userId, BackUpRemoteDataSourceImpl.OnDataReceivedListener listener);
}
