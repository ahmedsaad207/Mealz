package com.example.mealz.presenter.home;

import com.example.mealz.model.Meal;
import com.example.mealz.model.MealzResponse;
import com.example.mealz.model.SearchItem;

import java.util.List;

import io.reactivex.rxjava3.core.Single;

public interface HomePresenter {
    void getCategories();

    void getAreas();

    void getRandomMeal();

    void getIngredients();

    void search(CharSequence s);

    void setList(List<SearchItem> searchList);
}