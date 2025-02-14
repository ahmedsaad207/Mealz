package com.example.mealz.presenter.home;

import com.example.mealz.data.MealsRepositoryImpl;
import com.example.mealz.model.Category;
import com.example.mealz.model.Meal;
import com.example.mealz.model.NetworkMeal;
import com.example.mealz.utils.MealMapper;

import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.FlowableSubscriber;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class HomePresenterImpl implements HomePresenter {

    private MealsRepositoryImpl repo;
    private HomeView view;

    public HomePresenterImpl(MealsRepositoryImpl repo, HomeView view) {
        this.repo = repo;
        this.view = view;
    }

    @Override
    public void getCategories() {
        repo.getCategories()
                .subscribeOn(Schedulers.io())
                .map(mealzResponse -> mealzResponse.categories)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull ArrayList<Category> categories) {
                        view.displayCategories(categories);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                });
    }

    @Override
    public void getAreas() {
        repo.getAreas()
                .subscribeOn(Schedulers.io())
                .map(mealzResponse -> MealMapper.mapNetworkMealsToMeals(mealzResponse.meals))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<Meal>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull List<Meal> meals) {
                        view.displayAreas(meals);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                });
    }

    @Override
    public void getRandomMeal() {
        repo.getRandomMeal()
                .subscribeOn(Schedulers.io())
                .map(mealzResponse -> MealMapper.mapNetworkMealToMeal(mealzResponse.meals.get(0)))
                .repeat(10)
                .distinctUntilChanged()
                .collect(Collectors.toList())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<Meal>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull List<Meal> meals) {
                        view.displayDailyInspiration(meals);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                });
    }
}
