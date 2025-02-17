package com.example.mealz.presenter.favorite;

import com.example.mealz.data.MealsRepository;
import com.example.mealz.data.MealsRepositoryImpl;
import com.example.mealz.model.Ingredient;
import com.example.mealz.model.Meal;
import com.example.mealz.utils.Constants;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class FavoritePresenterImpl implements FavoritePresenter {

    MealsRepository repo;
    FavoriteView view;

    public FavoritePresenterImpl(MealsRepositoryImpl repo, FavoriteView view) {
        this.repo = repo;
        this.view = view;
    }

    @Override
    public void getFavoriteMeals(String userId) {
        repo.getFavoriteMeals(userId)
                .subscribeOn(Schedulers.io())
                .map(meals -> {
                    for (Meal meal : meals) {
                        meal.setUrlImage(getIngredientFilePath(meal.getUrlImage(), Constants.FOLDER_MEALS));
                        for (Ingredient ingredient: meal.getIngredients()) {
                            ingredient.setImageUrl(getIngredientFilePath(ingredient.getImageUrl(), Constants.FOLDER_INGREDIENTS));
                        }
                    }
                    return meals;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull List<Meal> meals) {
                        view.displayFavoriteMeals(meals);
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
    public String getIngredientFilePath(String imageUrl, String folder) {
        return repo.getIngredientFilePath(imageUrl, folder);
    }

    @Override
    public void deleteMeal(Meal meal) {
        repo.deleteMeal(meal)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        view.showError(e.getMessage());
                    }
                });
    }
}
