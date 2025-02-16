package com.example.mealz.data.remote;

import com.example.mealz.model.MealzResponse;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MealzApiService {

    @GET("categories.php")
    Single<MealzResponse> getCategories();

    @GET("filter.php")
    Single<MealzResponse> getMealsByCategory(@Query("c") String category);

    @GET("lookup.php")
    Single<MealzResponse> getMealById(@Query("i") long id);

    @GET("list.php?i=list")
    Single<MealzResponse> getIngredients();

    @GET("filter.php")
    Single<MealzResponse> getMealsByArea(@Query("a") String area);

    @GET("list.php?a=list")
    Single<MealzResponse> getAreas();

    @GET("random.php")
    Single<MealzResponse> getRandomMeal();

    @GET("filter.php")
    Single<MealzResponse> searchByIngredient(@Query("i") String ingredient);
}
