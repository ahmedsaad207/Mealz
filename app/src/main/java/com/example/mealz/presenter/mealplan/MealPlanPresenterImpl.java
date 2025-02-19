package com.example.mealz.presenter.mealplan;

import android.util.Log;

import com.example.mealz.data.MealsRepository;
import com.example.mealz.data.MealsRepositoryImpl;
import com.example.mealz.data.backup.BackUpRemoteDataSourceImpl;
import com.example.mealz.model.Meal;
import com.example.mealz.presenter.favorite.FavoriteView;
import com.example.mealz.utils.Constants;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MealPlanPresenterImpl implements MealPlanPresenter, BackUpRemoteDataSourceImpl.OnMealRemovedListener {
    MealsRepository repo;
    MealPlanView view;

    public MealPlanPresenterImpl(MealsRepositoryImpl repo, MealPlanView view) {
        this.repo = repo;
        this.view = view;
    }

    @Override
    public void getPlannedMeals() {
        repo.getUserId()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .concatMap(userId -> {
                    Log.i("TAG", "getPlannedMeals: userId:"+userId);
                     repo.getPlannedMeals(userId)
                             .subscribeOn(io.reactivex.rxjava3.schedulers.Schedulers.io())
                             .observeOn(io.reactivex.rxjava3.android.schedulers.AndroidSchedulers.mainThread())
                             .subscribe(new Observer<>() {
                                 @Override
                                 public void onSubscribe(@NonNull Disposable d) {

                                 }

                                 @Override
                                 public void onNext(@NonNull List<Meal> meals) {
                                     Log.i("TAG", "onNext: " + meals.size());
                                     view.displayFirstDayInCurrentWeek(meals);
                                 }

                                 @Override
                                 public void onError(@NonNull Throwable e) {

                                 }

                                 @Override
                                 public void onComplete() {
                                     Log.i("TAG", "onComplete: ");
                                 }
                             });
                return null;
                })
                .subscribe(new io.reactivex.Observer<>() {
                    @Override
                    public void onSubscribe(io.reactivex.disposables.Disposable d) {

                    }

                    @Override
                    public void onNext(Object o) {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });


        /*
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
        */
    }

    @Override
    public void getUsername() {
        repo.getUsername()
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.Observer<>() {
                    @Override
                    public void onSubscribe(io.reactivex.disposables.Disposable d) {

                    }

                    @Override
                    public void onNext(String username) {
                        view.displayUserName(username);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    public void deleteFromFirebase(Meal meal) {
        repo.removeMealFromFavorites(meal, this);

//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference reference = database.getReference("users");
//        reference
//                .child(meal.getUserId())
//                .child(meal.getDate() == Constants.TYPE_FAVORITE ? "favorites" : "plan")
//                .child(meal.getDate() == Constants.TYPE_FAVORITE ? String.valueOf(meal.getNetworkId()) : String.valueOf(meal.getDate()))
//                .removeValue()
//                .addOnSuccessListener(command ->deletePlanMeal(meal.getNetworkId(), meal.getUserId(), meal.getDate()))
//                .addOnFailureListener(command -> Log.d("TAG", "meal failed to delete from firebase"));
    }
    @Override
    public void deletePlanMeal(long networkId, String userId, long date) {
        repo.deletePlanMeal(networkId, userId, date)
                .subscribeOn(io.reactivex.rxjava3.schedulers.Schedulers.io())
                .observeOn(io.reactivex.rxjava3.android.schedulers.AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                });
    }

    @Override
    public void onMealRemoved(Meal meal) {
        deletePlanMeal(meal.getNetworkId(), meal.getUserId(), meal.getDate());
    }
}
