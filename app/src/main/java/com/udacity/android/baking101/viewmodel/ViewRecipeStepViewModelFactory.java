package com.udacity.android.baking101.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.udacity.android.baking101.database.AppDatabase;

public class ViewRecipeStepViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private final AppDatabase mDB;
    private final int stepId;

    public ViewRecipeStepViewModelFactory(AppDatabase mDB , int stepId) {
        this.mDB = mDB;
        this.stepId = stepId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new ViewRecipeStepViewModel(mDB, stepId);
    }
}
