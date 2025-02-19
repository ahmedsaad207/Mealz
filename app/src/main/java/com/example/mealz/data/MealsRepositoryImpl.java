package com.example.mealz.data;

import com.example.mealz.data.backup.BackUpRemoteDataSource;
import com.example.mealz.data.backup.BackUpRemoteDataSourceImpl;
import com.example.mealz.data.file.MealFileDataSourceImpl;
import com.example.mealz.data.local.MealsLocalDataSource;
import com.example.mealz.data.preferences.UserLocalDataSource;
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

    private final MealsRemoteDataSource remoteSource;
    private final MealsLocalDataSource localSource;
    private final UserLocalDataSource userLocalDataSource;
    private final MealFileDataSourceImpl fileSource;
    private final BackUpRemoteDataSource backupDataSource;

    public MealsRepositoryImpl(MealsRemoteDataSource remoteSource, MealsLocalDataSource localSource, MealFileDataSourceImpl fileSource,
                               UserLocalDataSource userLocalDataSource, BackUpRemoteDataSource backupDataSource) {
        this.remoteSource = remoteSource;
        this.localSource = localSource;
        this.fileSource = fileSource;
        this.userLocalDataSource = userLocalDataSource;
        this.backupDataSource = backupDataSource;
    }

    public static MealsRepositoryImpl getInstance(
            MealsRemoteDataSource remoteSource,
            MealsLocalDataSource localSource,
            MealFileDataSourceImpl fileSource,
            UserLocalDataSource UserLocalDataSource,
            BackUpRemoteDataSource backupDataSource) {
        if (instance == null) {
            instance = new MealsRepositoryImpl(remoteSource, localSource, fileSource,UserLocalDataSource,backupDataSource);
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
        return localSource.deleteMeal(meal.getNetworkId(), meal.getUserId());
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

    @Override
    public io.reactivex.Observable<String> getUserId() {
        return userLocalDataSource.getUserId();
    }

    @Override
    public void setUserId(String userId) {
        userLocalDataSource.setUserId(userId);
    }

    @Override
    public io.reactivex.Observable<String> getUsername() {
        return userLocalDataSource.getUsername();
    }

    @Override
    public void setUsername(String username) {
        userLocalDataSource.setUsername(username);
    }

    @Override
    public void clearUserId() {
        userLocalDataSource.clearUserId();
    }

    @Override
    public void clearUsername() {
        userLocalDataSource.clearUsername();
    }

    @Override
    public void setRememberMe(boolean value) {
        userLocalDataSource.setRememberMe(value);
    }

    @Override
    public Completable insertAllMeal(List<Meal> meals) {
        return localSource.insertAllMeal(meals);
    }

    @Override
    public Completable deletePlanMeal(long networkId, String userId, long date) {
        return localSource.deletePlanMeal(networkId, userId, date);
    }

    @Override
    public void backUp(Meal meal) {
        backupDataSource.backUp(meal);
    }

    @Override
    public void removeMealFromFavorites(Meal meal, BackUpRemoteDataSourceImpl.OnMealRemovedListener onMealRemovedListener) {
        backupDataSource.removeMealFromFavorites(meal, onMealRemovedListener);
    }

    @Override
    public void retrieveBackupMeals(String userId, BackUpRemoteDataSourceImpl.OnDataReceivedListener listener) {
        backupDataSource.retrieveBackupMeals(userId, listener);
    }
}
