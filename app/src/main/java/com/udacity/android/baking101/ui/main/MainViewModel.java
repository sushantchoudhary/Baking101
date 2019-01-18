package com.udacity.android.baking101.ui.main;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.udacity.android.baking101.database.AppDatabase;
import com.udacity.android.baking101.model.Recipe;

import java.util.List;


public class MainViewModel extends AndroidViewModel {
    private static final String TAG = MainViewModel.class.getSimpleName();
    private LiveData<List<Recipe>> recipes ;

    public MainViewModel(@NonNull Application application) {
        super(application);

        Log.d(TAG, "Fetching recipe from database");
       AppDatabase database = AppDatabase.getsInstance(this.getApplication());
       recipes = database.recipeDao().loadAllRecipe();
    }

    public LiveData<List<Recipe>> getRecipes() {
        return recipes;
    }
}
