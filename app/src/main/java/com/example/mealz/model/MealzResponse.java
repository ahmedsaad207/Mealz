package com.example.mealz.model;

import java.util.ArrayList;
import java.util.List;

public class MealzResponse {
    private ArrayList<Category> categories;
    private List<NetworkMeal> meals;

    public ArrayList<Category> getCategories() {
        return categories;
    }

    public List<NetworkMeal> getMeals() {
        return meals;
    }
}
