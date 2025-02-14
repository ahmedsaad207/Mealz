package com.example.mealz.presenter.mealslist;

import com.example.mealz.data.MealsRepositoryImpl;
import com.example.mealz.model.Meal;
import com.example.mealz.model.NetworkMeal;
import com.example.mealz.utils.MealMapper;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MealsListPresenterImpl implements MealsListPresenter{

    private final MealsRepositoryImpl repo;
    private final MealsListView view;

    public MealsListPresenterImpl(MealsRepositoryImpl repo, MealsListView view) {
        this.repo = repo;
        this.view = view;
    }

    @Override
    public void getMealsByCategory(String category) {
        repo.getMealsByCategory(category)
                .subscribeOn(Schedulers.io())
                .map(mealzResponse -> MealMapper.mapNetworkMealsToMeals(mealzResponse.meals))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull List<Meal> meals) {
                        view.displayMeals(meals);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                });
    }

    @Override
    public void getMealsByArea(String area) {
        repo.getMealsByArea(area)
                .subscribeOn(Schedulers.io())
                .map(mealzResponse -> MealMapper.mapNetworkMealsToMeals(mealzResponse.meals))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull List<Meal> meals) {
                        view.displayMeals(meals);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                });
    }
}
