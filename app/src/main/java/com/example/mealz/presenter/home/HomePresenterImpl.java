package com.example.mealz.presenter.home;

import android.util.Log;

import com.example.mealz.data.MealsRepositoryImpl;
import com.example.mealz.model.Category;
import com.example.mealz.model.Ingredient;
import com.example.mealz.model.Meal;
import com.example.mealz.model.MealzResponse;
import com.example.mealz.model.SearchItem;
import com.example.mealz.utils.MealMapper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.core.CompletableSource;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.core.Single;
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

    @Override
    public void getUsername() {
        repo.getUsername()
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.Observer<String>() {
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

    @Override
    public void insertAllMeal(List<Meal> meals) {
//        repo.isFavMealExist();

        repo.insertAllMeal(meals)
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

                    }
                });
    }

    public Completable checkAndInsertMeals(List<Meal> meals, String userId, long networkId) {
        List<Meal> mealsToInsert = new ArrayList<>();

//        return Single.fromCallable(() -> {
//                    meals.forEach(meal -> {
//                        repo.isFavMealExist(meal.getUserId(), meal.getNetworkId())
//                                .subscribeOn(Schedulers.io())
//                                .observeOn(Schedulers.computation())
//                                .subscribe(new SingleObserver<Meal>() {
//                                    @Override
//                                    public void onSubscribe(@NonNull Disposable d) {
//
//                                    }
//
//                                    @Override
//                                    public void onSuccess(@NonNull Meal existingMeal) {
//                                        if (existingMeal == null) {
//                                            mealsToInsert.add(meal);
//                                        }
//                                    }
//
//                                    @Override
//                                    public void onError(@NonNull Throwable e) {
//                                        Log.d("TAG", "onError: meal already inserted, network id= " + meal.getNetworkId());
//                                    }
//                                });
//
//                    };
//                    return mealsToInsert;
//                })
//                .flatMapCompletable(this::apply);

        return null;
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
                    public void onNext(String userId) {
                        // TODO
//                        view.displayUserName(username);
                        retrieveBackupMeals(userId);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void retrieveBackupMeals(String userId) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users");

        myRef.child(userId).child("favorites").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {
                List<Meal> meals = new ArrayList<>();
                for (DataSnapshot mealSnapShot : snapshot.getChildren()) {
                    meals.add(mealSnapShot.getValue(Meal.class));
                }

                insertAllMeal(meals);

                Log.d("TAG", "onDataChange: list from firebase size" + meals.size());
            }

            @Override
            public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {

            }
        });

    }

    private CompletableSource apply(List<Meal> nonExistingMeals) {
        if (!nonExistingMeals.isEmpty()) {
//            return insertAllMeal(nonExistingMeals);
        } else {
            return Completable.complete();
        }
    }
}
