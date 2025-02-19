package com.example.mealz.presenter.mealplan;

import com.example.mealz.model.Meal;

import java.util.List;

public interface MealPlanView {
    void displayFirstDayInCurrentWeek(List<Meal> meals);

    void displayUserName(String username);
}
