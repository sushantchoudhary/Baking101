package com.udacity.android.baking101.ui.main;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.udacity.android.baking101.database.AppDatabase;

public class RecipeDetailViewModelFactory extends   ViewModelProvider.NewInstanceFactory{

    private final AppDatabase mDB;
    private final int recipeId;

    public RecipeDetailViewModelFactory(AppDatabase mDB, int recipeId) {
        this.mDB = mDB;
        this.recipeId = recipeId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new RecipeDetailViewModel(mDB, recipeId);
    }
}
