package com.example.mealz.model;

import java.util.ArrayList;
import java.util.List;

public class IngredientModel {
    static List<Ingredient> ingredients;
    String url = "https://www.themealdb.com/images/ingredients/";

    public IngredientModel(NetworkMeal networkMeal) {
        String[] ingredientNames = {
                networkMeal.getMealIngredient1(),
                networkMeal.getMealIngredient2(),
                networkMeal.getMealIngredient3(),
                networkMeal.getMealIngredient4(),
                networkMeal.getMealIngredient5(),
                networkMeal.getMealIngredient6(),
                networkMeal.getMealIngredient7(),
                networkMeal.getMealIngredient8(),
                networkMeal.getMealIngredient9(),
                networkMeal.getMealIngredient10()
        };
        String[] ingredientMeasures = {
                networkMeal.getMealMeasure1(),
                networkMeal.getMealMeasure2(),
                networkMeal.getMealMeasure3(),
                networkMeal.getMealMeasure4(),
                networkMeal.getMealMeasure5(),
                networkMeal.getMealMeasure6(),
                networkMeal.getMealMeasure7(),
                networkMeal.getMealMeasure8(),
                networkMeal.getMealMeasure9(),
                networkMeal.getMealMeasure10()
        };
        ingredients = new ArrayList<>();

        for (int i = 0; i < ingredientNames.length; i++) {
            String name = ingredientNames[i];
            String measure = ingredientMeasures[i];

            if (name != null && !name.trim().isEmpty()) {
                ingredients.add(new Ingredient(name, measure, url + name + "-Small.png"));
            }
        }

    }

    public static List<Ingredient> getIngredients(NetworkMeal networkMeal) {
        new IngredientModel(networkMeal);
        return ingredients;
    }
}
