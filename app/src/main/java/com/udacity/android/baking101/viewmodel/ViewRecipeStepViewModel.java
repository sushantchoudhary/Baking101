package com.udacity.android.baking101.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.util.Log;

import com.udacity.android.baking101.database.AppDatabase;
import com.udacity.android.baking101.model.Step;

import java.util.List;

public class ViewRecipeStepViewModel extends ViewModel {
    private static final String TAG = MainViewModel.class.getSimpleName();
    private LiveData<List<Step>> steps ;
    private LiveData<Step> currentStep;

    public ViewRecipeStepViewModel(@NonNull AppDatabase database, int stepId) {
        Log.d(TAG, "Fetching steps from database");
//        steps = database.stepDao().loadStepByRecipeId(recipeId);
        currentStep = database.stepDao().loadStepByStepId(stepId);
    }

//    public LiveData<List<Step>> getSteps() {
//        return steps;
//    }

    public LiveData<Step> getStep() {
        return currentStep;
    }
}
