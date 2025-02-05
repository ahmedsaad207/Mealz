package com.example.mealz.data.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MealzRetrofit {
    public static Retrofit retrofit;

    static {
        retrofit = new Retrofit.Builder().baseUrl("https://www.themealdb.com/api/json/v1/1/").addConverterFactory(GsonConverterFactory.create()).build();
    }

    public static MealzApiService getService() {
        return retrofit.create(MealzApiService.class);
    }

}
