package com.example.mealz.presenter.mealplan;

import com.example.mealz.data.MealsRepository;
import com.example.mealz.data.MealsRepositoryImpl;
import com.example.mealz.model.Meal;
import com.example.mealz.presenter.favorite.FavoriteView;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MealPlanPresenterImpl implements MealPlanPresenter{
    MealsRepository repo;
    MealPlanView view;

    public MealPlanPresenterImpl(MealsRepositoryImpl repo, MealPlanView view) {
        this.repo = repo;
        this.view = view;
    }

    @Override
    public void getPlannedMeals(String userId) {
        repo.getPlannedMeals(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull List<Meal> meals) {
                        view.displayFirstDayInCurrentWeek(meals);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
