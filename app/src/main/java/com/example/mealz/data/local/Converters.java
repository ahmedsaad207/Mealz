package com.example.mealz.data.local;

import androidx.room.TypeConverter;

import com.example.mealz.model.Ingredient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class Converters {

    private Gson gson = new Gson();

    @TypeConverter
    public String fromList(List<Ingredient> ingredients) {
        if (ingredients == null) {
            return null;
        }
        return gson.toJson(ingredients);
    }

    @TypeConverter
    public List<Ingredient> toList(String json) {

        if (json == null) {
            return null;
        }
        return gson.fromJson(json, new TypeToken<List<Ingredient>>() {
        }.getType());
    }
}
