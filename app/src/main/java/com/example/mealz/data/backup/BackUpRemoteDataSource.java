package com.example.mealz.data.backup;

import com.example.mealz.model.Meal;

public interface BackUpRemoteDataSource {
    void removeMealFromFavorites(Meal meal, BackUpRemoteDataSourceImpl.OnMealRemovedListener onMealRemovedListener);

    void retrieveBackupMeals(String userId, BackUpRemoteDataSourceImpl.OnDataReceivedListener listener);

    void backUp(Meal meal);

}
