package com.example.mealz.presenter.favorite;

import android.util.Log;

import com.example.mealz.data.MealsRepository;
import com.example.mealz.data.MealsRepositoryImpl;
import com.example.mealz.data.backup.BackUpRemoteDataSourceImpl;
import com.example.mealz.model.Ingredient;
import com.example.mealz.model.Meal;
import com.example.mealz.utils.Constants;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class FavoritePresenterImpl implements FavoritePresenter, BackUpRemoteDataSourceImpl.OnMealRemovedListener {

    MealsRepository repo;
    FavoriteView view;

    public FavoritePresenterImpl(MealsRepositoryImpl repo, FavoriteView view) {
        this.repo = repo;
        this.view = view;
        getUserId();
    }

    @Override
    public void getFavoriteMeals() {
        repo.getUserId()
                .flatMap(userId -> {
                    repo.getFavoriteMeals(userId)
                            .subscribeOn(Schedulers.io())
                            .map(meals -> {
                                for (Meal meal : meals) {
                                    meal.setUrlImage(getIngredientFilePath(meal.getUrlImage(), Constants.FOLDER_MEALS));
                                    for (Ingredient ingredient : meal.getIngredients()) {
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
                                    Log.i("TAG", "from get fav: " + meals.size());
                                    view.displayFavoriteMeals(meals);
                                }

                                @Override
                                public void onError(@NonNull Throwable e) {

                                }

                                @Override
                                public void onComplete() {

                                }
                            });
                    return null;
                })
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .subscribe(new io.reactivex.Observer<Object>() {
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

    @Override
    public void getUserId() {
        repo.getUserId()
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.Observer<>() {
                    @Override
                    public void onSubscribe(io.reactivex.disposables.Disposable d) {

                    }

                    @Override
                    public void onNext(String username) {
                        view.onUserId(username);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void deleteFromFirebase(Meal meal) {
        repo.removeMealFromFavorites(meal, this);
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference reference = database.getReference("users");
//        reference
//                .child(meal.getUserId())
//                .child(meal.getDate() == Constants.TYPE_FAVORITE ? "favorites" : "plan")
//                .child(meal.getDate() == Constants.TYPE_FAVORITE ? String.valueOf(meal.getNetworkId()) : String.valueOf(meal.getDate()))
//                .removeValue()
//                .addOnSuccessListener(command ->deleteMeal(meal))
//                .addOnFailureListener(command -> Log.d("TAG", "meal failed to delete from firebase"));
    }

    @Override
    public void onMealRemoved(Meal meal) {
        deleteMeal(meal);
    }
}
