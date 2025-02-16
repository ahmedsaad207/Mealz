package com.example.mealz.data;

import com.example.mealz.data.file.MealFileDataSourceImpl;
import com.example.mealz.data.local.MealsLocalDataSource;
import com.example.mealz.data.remote.MealsRemoteDataSource;
import com.example.mealz.model.Ingredient;
import com.example.mealz.model.Meal;
import com.example.mealz.model.MealzResponse;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

public class MealsRepositoryImpl implements MealsRepository {

    private static MealsRepositoryImpl instance;

    private MealsRemoteDataSource remoteSource;
    private MealsLocalDataSource localSource;
    private MealFileDataSourceImpl fileSource;

    public MealsRepositoryImpl(MealsRemoteDataSource remoteSource, MealsLocalDataSource localSource, MealFileDataSourceImpl fileSource) {
        this.remoteSource = remoteSource;
        this.localSource = localSource;
        this.fileSource = fileSource;
    }

    public static MealsRepositoryImpl getInstance(
            MealsRemoteDataSource remoteSource,
            MealsLocalDataSource localSource,
            MealFileDataSourceImpl fileSource) {
        if (instance == null) {
            instance = new MealsRepositoryImpl(remoteSource, localSource,fileSource);
        }
        return instance;
    }

    @Override
    public Single<MealzResponse> getCategories() {
        return remoteSource.getCategories();
    }

    @Override
    public Single<MealzResponse> getMealsByCategory(String category) {
        return remoteSource.getMealsByCategory(category);
    }

    @Override
    public Single<MealzResponse> getMealById(long id) {
        return remoteSource.getMealById(id);
    }

    @Override
    public Single<MealzResponse> getIngredients() {
        return remoteSource.getIngredients();
    }

    @Override
    public Single<MealzResponse> getMealsByArea(String area) {
        return remoteSource.getMealsByArea(area);
    }

    @Override
    public Single<MealzResponse> getAreas() {
        return remoteSource.getAreas();
    }

    @Override
    public Single<MealzResponse> getRandomMeal() {
        return remoteSource.getRandomMeal();
    }

    @Override
    public Single<MealzResponse> searchByIngredient(String ingredient) {
        return remoteSource.searchByIngredient(ingredient);
    }

    @Override
    public Completable insertMeal(Meal meal) {
        return localSource.insertMeal(meal);
    }

    @Override
    public Completable deleteMeal(Meal meal) {
        return localSource.deleteMeal(meal);
    }

    @Override
    public Observable<List<Meal>> getFavoriteMeals(String userId) {
        return localSource.getFavoriteMeals(userId);
    }

    @Override
    public Observable<List<Meal>> getPlannedMeals(String userId) {
        return localSource.getPlannedMeals(userId);
    }

    @Override
    public Single<Meal> isFavMealExist(String userId, long networkMealId) {
        return localSource.isFavMealExist(userId, networkMealId);
    }

    @Override
    public void downloadMealImage(Meal mealImageUrl) {
        fileSource.downloadMealImage(mealImageUrl);
    }

    @Override
    public Completable downloadIngredientImages(List<Ingredient> ingredients) {
        return fileSource.downloadIngredientImages(ingredients);
    }

    @Override
    public String getIngredientFilePath(String imageUrl, String folder) {
        return fileSource.getIngredientFilePath(imageUrl, folder);
    }
}
