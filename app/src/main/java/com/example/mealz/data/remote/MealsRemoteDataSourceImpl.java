package com.example.mealz.data.remote;

import com.example.mealz.model.MealzResponse;

import io.reactivex.rxjava3.core.Single;

public class MealsRemoteDataSourceImpl implements MealsRemoteDataSource {

    private static MealsRemoteDataSourceImpl remoteSource = null;
    private final MealzApiService service;

    private MealsRemoteDataSourceImpl() {
        service = RetrofitClient.getInstance();
    }

    public static MealsRemoteDataSourceImpl getInstance() {
        if (remoteSource == null) {
            remoteSource = new MealsRemoteDataSourceImpl();
        }
        return remoteSource;
    }

    @Override
    public Single<MealzResponse> getCategories() {
        return service.getCategories();
    }

    @Override
    public Single<MealzResponse> getMealsByCategory(String category) {
        return service.getMealsByCategory(category);
    }

    @Override
    public Single<MealzResponse> getMealById(long id) {
        return service.getMealById(id);
    }

    @Override
    public Single<MealzResponse> getIngredients() {
        return service.getIngredients();
    }

    @Override
    public Single<MealzResponse> getMealsByArea(String area) {
        return service.getMealsByArea(area);
    }

    @Override
    public Single<MealzResponse> getAreas() {
        return service.getAreas();
    }

    @Override
    public Single<MealzResponse> getRandomMeal() {
        return service.getRandomMeal();
    }

    @Override
    public Single<MealzResponse> searchByIngredient(String ingredient) {
        return service.searchByIngredient(ingredient);
    }
}
