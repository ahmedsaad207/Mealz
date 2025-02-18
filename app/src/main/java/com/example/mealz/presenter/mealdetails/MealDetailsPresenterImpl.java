package com.example.mealz.presenter.mealdetails;

import android.util.Log;

import com.example.mealz.data.MealsRepositoryImpl;
import com.example.mealz.model.Ingredient;
import com.example.mealz.model.Meal;
import com.example.mealz.utils.Constants;
import com.example.mealz.utils.MealMapper;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MealDetailsPresenterImpl implements MealDetailsPresenter {

    private final MealsRepositoryImpl repo;
    private final MealDetailsView view;

    public MealDetailsPresenterImpl(MealsRepositoryImpl repo, MealDetailsView view) {
        this.repo = repo;
        this.view = view;
    }

    @Override
    public void getMealById(long id) {
        repo.getMealById(id)
                .subscribeOn(Schedulers.io())
                .map(mealzResponse -> MealMapper.mapNetworkMealToMeal(mealzResponse.getMeals().get(0)))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull Meal meal) {
                        if (meal != null) {
                            view.displayMeal(meal);
                        } else {
                            view.onFetchMealFailed("Problem with fetching data details");
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        view.onFetchMealFailed(e.getMessage());
                    }
                });
    }

    @Override
    public void insertFavMeal(Meal meal) {
        repo.isFavMealExist(meal.getUserId(), meal.getNetworkId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull Meal meal) {
                        downloadMealImage(meal);
                        downloadIngredientImages(meal.getIngredients());
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        meal.setDate(Constants.TYPE_FAVORITE);
                        insertMeal(meal);
                    }
                });

        /*
        repo.getUserId()
                .flatMap(userId -> {
                    meal.setUserId(userId);
                    repo.isFavMealExist(userId, meal.getNetworkId())
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new SingleObserver<>() {
                                @Override
                                public void onSubscribe(@NonNull Disposable d) {

                                }

                                @Override
                                public void onSuccess(@NonNull Meal meal) {
                                    downloadMealImage(meal);
                                    downloadIngredientImages(meal.getIngredients());
                                }

                                @Override
                                public void onError(@NonNull Throwable e) {
                                    insertMeal(meal);
                                }
                            });
                    return null;
                })
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
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
        */
    }

    @Override
    public void insertPlanMeal(Meal meal) {
        insertMeal(meal);
    }

    @Override
    public void insertMeal(Meal meal) {

//        meal.setDate(Constants.TYPE_FAVORITE);
        repo.insertMeal(meal)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        backUp(meal);
                        downloadMealImage(meal);
                        downloadIngredientImages(meal.getIngredients());

                        if (meal.getDate() == Constants.TYPE_FAVORITE) {
                            view.onInsertFavCompleted();
                        } else {
                            view.onInsertPlanCompleted();
                        }

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        view.onInsertError(e.getMessage());
                    }
                });

        /*
        repo.getUserId()
                .flatMap(userId -> {
                    meal.setUserId(userId);
                    repo.insertMeal(meal)
                            .subscribeOn(Schedulers.io())
                            .subscribe(new CompletableObserver() {
                                @Override
                                public void onSubscribe(@NonNull Disposable d) {

                                }

                                @Override
                                public void onComplete() {
                                    downloadMealImage(meal);
                                    downloadIngredientImages(meal.getIngredients());
                                    view.onSuccess();
                                }

                                @Override
                                public void onError(@NonNull Throwable e) {

                                }
                            });
                    return null;
                })
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
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
        */
    }

    public void backUp(Meal meal) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("users");
        reference
                .child(meal.getUserId())
                .child(meal.getDate() == Constants.TYPE_FAVORITE ? "favorites" : "plan")
                .child(meal.getDate() == Constants.TYPE_FAVORITE ? String.valueOf(meal.getNetworkId()) : String.valueOf(meal.getDate()))
                .setValue(meal).addOnSuccessListener(command -> {
                    Log.d("TAG", "added to firebase");
                });
    }

    @Override
    public void downloadMealImage(Meal meal) {
        repo.downloadMealImage(meal);
    }

    @Override
    public void downloadIngredientImages(List<Ingredient> ingredients) {
        repo.downloadIngredientImages(ingredients)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        Log.d("TAG", "on download images Completed: ");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                });
    }

    @Override
    public void isFavMealExist(long networkId) {
        repo.getUserId()
                .flatMap(userId -> {
                    repo.isFavMealExist(userId, networkId)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new SingleObserver<Meal>() {
                                @Override
                                public void onSubscribe(@NonNull Disposable d) {

                                }

                                @Override
                                public void onSuccess(@NonNull Meal meal) {
                                    view.changeImageResourceForFav();
                                }

                                @Override
                                public void onError(@NonNull Throwable e) {

                                }
                            });
                    return null;
                })
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
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
                        downloadMealImage(meal);
                        downloadIngredientImages(meal.getIngredients());
                        deleteFromFirebase(meal);
                        view.onDeleteComplete();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        view.onDeleteError(e.getMessage());
                    }
                });
        /*
        repo.getUserId()
                .flatMap(userId -> {
                    meal.setUserId(userId);
                    repo.deleteMeal(meal)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new CompletableObserver() {
                                @Override
                                public void onSubscribe(@NonNull Disposable d) {

                                }

                                @Override
                                public void onComplete() {
                                    downloadMealImage(meal);
                                    downloadIngredientImages(meal.getIngredients());
//                                    view.onSuccess();
                                    deleteFromFirebase(meal);
                                    view.onDeleteComplete();
                                }

                                @Override
                                public void onError(@NonNull Throwable e) {
                                    view.onDeleteError();
                                }
                            });
                    return null;
                })
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
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
        */
    }

    private void deleteFromFirebase(Meal meal) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("users");
        reference
                .child(meal.getUserId())
                .child(meal.getDate() == Constants.TYPE_FAVORITE ? "favorites" : "plans")
                .child(meal.getDate() == Constants.TYPE_FAVORITE ? String.valueOf(meal.getNetworkId()) : String.valueOf(meal.getDate()))
                .removeValue()
                .addOnSuccessListener(command -> Log.d("TAG", "meal deleted from firebase"))
                .addOnFailureListener(command -> Log.d("TAG", "meal failed to delete from firebase"));
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
//                        view.onDeleteError("Problem with fetching data credential");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


}
