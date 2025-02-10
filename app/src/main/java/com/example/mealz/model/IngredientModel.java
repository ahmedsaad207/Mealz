package com.example.mealz.model;

import java.util.ArrayList;
import java.util.List;

public class IngredientModel {
    String url = "https://www.themealdb.com/images/ingredients/";

    List<Ingredient> ingredients;

    public IngredientModel(Meal meal) {
        String[] ingredientNames = {
                meal.getMealIngredient1(),
                meal.getMealIngredient2(),
                meal.getMealIngredient3(),
                meal.getMealIngredient4(),
                meal.getMealIngredient5(),
                meal.getMealIngredient6(),
                meal.getMealIngredient7(),
                meal.getMealIngredient8(),
                meal.getMealIngredient9(),
                meal.getMealIngredient10()
        };
        String[] ingredientMeasures = {
                meal.getMealMeasure1(),
                meal.getMealMeasure2(),
                meal.getMealMeasure3(),
                meal.getMealMeasure4(),
                meal.getMealMeasure5(),
                meal.getMealMeasure6(),
                meal.getMealMeasure7(),
                meal.getMealMeasure8(),
                meal.getMealMeasure9(),
                meal.getMealMeasure10()
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

    public List<Ingredient> getIngredients() {
        return ingredients;
    }
}
