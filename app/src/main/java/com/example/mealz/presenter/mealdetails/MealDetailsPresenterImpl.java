package com.example.mealz.presenter.mealdetails;

import com.example.mealz.data.MealsRepositoryImpl;
import com.example.mealz.model.Ingredient;
import com.example.mealz.model.Meal;
import com.example.mealz.model.NetworkMeal;
import com.example.mealz.utils.Constants;
import com.example.mealz.utils.MealMapper;

import java.util.Calendar;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MealDetailsPresenterImpl implements MealDetailsPresenter {

    private MealsRepositoryImpl repo;
    private MealDetailsView view;

    public MealDetailsPresenterImpl(MealsRepositoryImpl repo, MealDetailsView view) {
        this.repo = repo;
        this.view = view;
    }

    @Override
    public void getMealById(long id) {
        repo.getMealById(id)
                .subscribeOn(Schedulers.io())
                .map(mealzResponse -> MealMapper.mapNetworkMealToMeal(mealzResponse.meals.get(0)))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull Meal meal) {
                        view.displayMeal(meal);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                });
    }

    @Override
    public void insertFavMeal(Meal meal) { // TODO Data Source for Shared Preference
        repo.isFavMealExist("ahmed", meal.getNetworkId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull Meal meal) {
                        downloadMealImage(meal.getUrlImage());
                        downloadIngredientImages(meal.getIngredients());
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        insertMeal(meal, Constants.TYPE_FAVORITE);
                    }
                });

    }

    @Override
    public void insertPlanMeal(Meal meal) {
        Calendar calendar = Calendar.getInstance();
        insertMeal(meal, calendar.getTime().getTime());
    }

    @Override
    public void insertMeal(Meal meal, long date) {
        meal.setDate(date);
        repo.insertMeal(meal)
                .subscribeOn(Schedulers.io())
                .subscribe();

        downloadMealImage(meal.getUrlImage());
//        downloadIngredientImages(new IngredientModel(meal).getIngredients());
        downloadIngredientImages(meal.getIngredients());
    }

    @Override
    public void downloadMealImage(String mealImageUrl) {
        repo.downloadMealImage(mealImageUrl);
    }

    @Override
    public void downloadIngredientImages(List<Ingredient> ingredients) {
        repo.downloadIngredientImages(ingredients);
    }

    private Meal convertToMealModel(NetworkMeal networkMeal, long date) {
        Meal meal = new Meal();
        meal.setNetworkId(networkMeal.getMealId());
        meal.setArea(networkMeal.getMealArea());
        meal.setName(networkMeal.getMealName());
        meal.setCategory(networkMeal.getMealCategory());
        meal.setInstructions(networkMeal.getMealInstructions());
        meal.setUrlImage(networkMeal.getMealImage());
        meal.setUserId("ahmed");
        meal.setDate(date);
//        meal.setIngredients(ingredients);

        return meal;
    }
}
