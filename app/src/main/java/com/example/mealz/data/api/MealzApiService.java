package com.example.mealz.data.api;

import com.example.mealz.model.MealzResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MealzApiService {

    @GET("categories.php")
    Call<MealzResponse> getCategories();

    @GET("filter.php")
    Call<MealzResponse> getMealzByCategory(@Query("c") String category);

    @GET("lookup.php")
    Call<MealzResponse> getMealById(@Query("i") long id);

    @GET("list.php?i=list")
    Call<MealzResponse> getIngredients();

    @GET("random.php")
    Call<MealzResponse> getRandomMeal();

    @GET("filter.php")
    Call<MealzResponse> searchByIngredient(@Query("i") String ingredient);

    @GET("filter.php")
    Call<MealzResponse> searchByCategory(@Query("c") String category);

    @GET("filter.php")
    Call<MealzResponse> searchByArea(@Query("a") String area);
}
