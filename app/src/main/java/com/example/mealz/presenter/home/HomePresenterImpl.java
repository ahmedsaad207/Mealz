package com.example.mealz.presenter.home;

import android.util.Log;

import com.example.mealz.data.MealsRepositoryImpl;
import com.example.mealz.model.Category;
import com.example.mealz.model.Ingredient;
import com.example.mealz.model.Meal;
import com.example.mealz.model.MealzResponse;
import com.example.mealz.model.SearchItem;
import com.example.mealz.utils.MealMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class HomePresenterImpl implements HomePresenter {

    private final MealsRepositoryImpl repo;
    private final HomeView view;

    private List<SearchItem> searchList, filteredList;


    public HomePresenterImpl(MealsRepositoryImpl repo, HomeView view) {
        this.repo = repo;
        this.view = view;
        filteredList = new ArrayList<>();
    }

    @Override
    public void getCategories() {
        repo.getCategories()
                .subscribeOn(Schedulers.io())
                .map(MealzResponse::getCategories)
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
                .map(mealzResponse -> MealMapper.mapNetworkMealsToMeals(mealzResponse.getMeals()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<>() {
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
                .map(mealzResponse -> MealMapper.mapNetworkMealToMeal(mealzResponse.getMeals().get(0)))
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

    @Override
    public void getIngredients() {
        repo.getIngredients()
                .subscribeOn(Schedulers.io())
                .map(mealzResponse -> MealMapper.mapNetworkMealToIngredients(mealzResponse.getMeals()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<Ingredient>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull List<Ingredient> ingredients) {
                        view.onIngredientsListReady(ingredients);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                });

    }

    @Override
    public void search(CharSequence s) {

        Observable.create(emitter -> {
                    if (!s.toString().trim().isEmpty()) {
                        filteredList = searchList.stream().filter(meal -> meal.getName().contains(s.toString().toLowerCase())).collect(Collectors.toList());
                        emitter.onNext(filteredList);
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull Object meals) {
                        //noinspection unchecked
                        view.displaySearchItems((List<SearchItem>) meals);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void setList(List<SearchItem> searchList) {
        this.searchList = searchList;
    }


}
