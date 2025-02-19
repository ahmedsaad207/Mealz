package com.example.mealz.data.local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.mealz.model.Meal;

@Database(entities = {Meal.class}, version = 2, exportSchema = false)
@TypeConverters(value = Converters.class)
public abstract class MealsDatabase extends RoomDatabase {
    private volatile static MealsDatabase instance;

    public static MealsDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (RoomDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(
                                    context.getApplicationContext(), MealsDatabase.class, "meals.db"
                            )
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }

        }

        return instance;
    }

    public abstract MealDao getMealDao();
}
