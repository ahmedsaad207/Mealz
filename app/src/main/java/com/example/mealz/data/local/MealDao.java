package com.example.mealz.data.local;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.mealz.model.Meal;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

@Dao()
public interface MealDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    Completable insertMeal(Meal meal);

    @Delete
    Completable deleteMeal(Meal meal);

    @Query("SELECT * FROM meals_table WHERE date == 0 AND userId == :userId")
    Observable<List<Meal>> getFavoriteMealsByUserId(String userId);

    @Query("SELECT * FROM meals_table WHERE date > 0 AND userId == :userId")
    Observable<List<Meal>> getPlannedMeals(String userId);

    @Query("SELECT * FROM meals_table WHERE date == 0 AND userId == :userId AND networkId == :networkId")
    Single<Meal> isFavMealExist(String userId, long networkId);
}
