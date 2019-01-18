package com.udacity.android.baking101.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.util.Log;

import com.udacity.android.baking101.model.Ingredient;
import com.udacity.android.baking101.model.Recipe;
import com.udacity.android.baking101.model.Step;

@Database(entities = {Recipe.class, Ingredient.class,
        Step.class}, version = 1, exportSchema = false)
@TypeConverters({IngredientTypeConverter.class, StepTypeConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    private static final String LOG_TAG = AppDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "recipedb";
    private static AppDatabase sInstance;

    public static AppDatabase getsInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                Log.d(LOG_TAG, "Creating new recipe database");
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        AppDatabase.class, DATABASE_NAME).allowMainThreadQueries().build();
            }

        }
        Log.d(LOG_TAG, "Fetching the database instance");
        return sInstance;
    }

    public abstract RecipeDao recipeDao();
    public abstract StepDao stepDao();
    public abstract IngredientDao ingredientDao();


}
