package com.udacity.android.baking101.database;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.udacity.android.baking101.model.Ingredient;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

class IngredientTypeConverter {
    static Gson gson = new Gson();

    @TypeConverter
    public static List<Ingredient> stringToIngredientList(String data) {
        if (data == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<Ingredient>>() {
        }.getType();
        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String ingredientListToString(List<Ingredient> ingredientList) {
        return gson.toJson(ingredientList);
    }
}
