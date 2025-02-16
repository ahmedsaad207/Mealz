package com.example.mealz.utils;

import android.content.Context;

import com.example.mealz.model.Area;
import com.example.mealz.model.Ingredient;
import com.example.mealz.model.IngredientModel;
import com.example.mealz.model.Meal;
import com.example.mealz.model.NetworkMeal;

import java.util.ArrayList;
import java.util.List;

public class MealMapper {
    public static Meal mapNetworkMealToMeal(NetworkMeal networkMeal) {
        Meal meal = new Meal();
        meal.setNetworkId(networkMeal.getMealId());
        meal.setArea(networkMeal.getMealArea());
        meal.setName(networkMeal.getMealName());
        meal.setCategory(networkMeal.getMealCategory());
        meal.setYoutubeUrl(networkMeal.getMealYoutube());
        meal.setInstructions(networkMeal.getMealInstructions());
        meal.setUrlImage(networkMeal.getMealImage());
        meal.setUserId("ahmed");
        meal.setDate(Constants.TYPE_DEFAULT);
        meal.setIngredients(IngredientModel.getIngredients(networkMeal));
        return meal;
    }

    public static List<Meal> mapNetworkMealsToMeals(List<NetworkMeal> networkMeals) {
        List<Meal> meals = new ArrayList<>();
        for (NetworkMeal networkMeal : networkMeals) {
            meals.add(mapNetworkMealToMeal(networkMeal));
        }
        return meals;
    }

    public static List<Area> mapMealsToAreas(List<Meal> meals, Context context) {
        ArrayList<Area> areas = new ArrayList<>();

        for (Meal meal : meals) {
            int imageResource = Utils.getDrawableResourceForCountry(meal.getArea(), context);
            if (imageResource != 0) {
                areas.add(new Area(meal.getArea(), imageResource));
            }
        }

        return areas;
    }

    public static List<Ingredient> mapNetworkMealToIngredients(List<NetworkMeal> networkMeals) {

        List<Ingredient> ingredients = new ArrayList<>();
        for (NetworkMeal meal : networkMeals) {
            String name = meal.getStrIngredient();
            if (name != null && !name.isEmpty()) {
                ingredients.add(new Ingredient(name, "", "https://www.themealdb.com/images/ingredients/" + name + "-Small.png"));
            }
        }
        return ingredients;
    }
}
