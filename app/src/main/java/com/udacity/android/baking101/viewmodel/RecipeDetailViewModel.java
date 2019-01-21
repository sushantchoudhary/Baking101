package com.udacity.android.baking101.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.util.Log;

import com.udacity.android.baking101.database.AppDatabase;
import com.udacity.android.baking101.model.Recipe;


public class RecipeDetailViewModel extends ViewModel {
    private static final String TAG = MainViewModel.class.getSimpleName();
    private LiveData<Recipe> recipe ;

    public RecipeDetailViewModel(@NonNull AppDatabase database, int recipeId) {

        Log.d(TAG, "Fetching recipe from database");
        recipe = database.recipeDao().loadRecipeById(recipeId);
    }

    public LiveData<Recipe> getRecipe() {
        return recipe;
    }
}
