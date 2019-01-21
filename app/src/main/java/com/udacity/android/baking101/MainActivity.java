package com.udacity.android.baking101;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;

import com.udacity.android.baking101.ui.main.MainFragment;

public class MainActivity extends AppCompatActivity {

    @Nullable
    private RecipeIdlingResource recipeIdlingResource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, MainFragment.newInstance())
                    .commitNow();
        }

    }

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (recipeIdlingResource == null) {
            recipeIdlingResource = new RecipeIdlingResource();
        }

        return  recipeIdlingResource;
    }


}
