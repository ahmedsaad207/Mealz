package com.example.mealz.data.local;

import android.content.Context;

import com.example.mealz.model.Meal;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

public class MealsLocalDataSourceImpl implements MealsLocalDataSource {

    private static MealsLocalDataSourceImpl localSource = null;
    private final MealDao dao;

    private MealsLocalDataSourceImpl(Context context) {
        dao = MealsDatabase.getInstance(context).getMealDao();
    }

    public static MealsLocalDataSourceImpl getInstance(Context context) {
        if (localSource == null) {
            localSource = new MealsLocalDataSourceImpl(context);
        }

        return localSource;
    }

    @Override
    public Completable insertMeal(Meal meal) {
        return dao.insertMeal(meal);
    }

    @Override
    public Completable deleteMeal(long networkId, String userId) {
        return dao.deleteMeal(networkId, userId);
    }

    @Override
    public Observable<List<Meal>> getFavoriteMeals(String userId) {
        return dao.getFavoriteMeals(userId);
    }

    @Override
    public Observable<List<Meal>> getPlannedMeals(String userId) {
        return dao.getPlannedMeals(userId);
    }

    @Override
    public Single<Meal> isFavMealExist(String userId, long networkMealId) {
        return dao.isFavMealExist(userId, networkMealId);
    }

    @Override
    public Completable insertAllMeal(List<Meal> meals) {
        return dao.insertAllMeal(meals);
    }

    @Override
    public Completable deletePlanMeal(long networkId, String userId, long date) {
        return dao.deletePlanMeal(networkId, userId, date);
    }
}
