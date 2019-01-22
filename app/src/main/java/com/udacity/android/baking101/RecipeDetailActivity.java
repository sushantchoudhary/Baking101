package com.udacity.android.baking101;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.udacity.android.baking101.model.Recipe;
import com.udacity.android.baking101.model.Step;
import com.udacity.android.baking101.ui.main.RecipeDetailFragment;
import com.udacity.android.baking101.ui.main.ViewRecipeStepFragment;
import com.udacity.android.baking101.util.RecipeIdlingResource;

public class RecipeDetailActivity extends AppCompatActivity implements RecipeDetailFragment.OnRecipeClickListener{

    @Nullable
    private RecipeIdlingResource recipeIdlingResource;
    private Recipe recipe;
    private boolean mTwoPane;
    private static final String FRAGMENT_TAG = "RecipeDetailFragment";
    private static  final String TAG = RecipeDetailActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_detail_activity);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        if(findViewById(R.id.recipe_detail_tablet_layout) != null) {
            mTwoPane = true;
            if(savedInstanceState  == null) {
                Intent intentFromHome = getIntent();
                if (intentFromHome != null) {
                    if (intentFromHome.hasExtra(Intent.EXTRA_TEXT)) {
                        recipe = intentFromHome.getParcelableExtra(Intent.EXTRA_TEXT);
                    }
                }
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.recipe_detail_container, RecipeDetailFragment.newInstance(recipe), FRAGMENT_TAG )
                        .addToBackStack(FRAGMENT_TAG)
                        .commit();

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.view_recipe_step_container, ViewRecipeStepFragment.newInstance(recipe.getSteps().get(0)), FRAGMENT_TAG)
                        .addToBackStack(FRAGMENT_TAG)
                        .commit();

            }
        } else  {
            mTwoPane = false;
            if (savedInstanceState == null) {
                Intent intentFromHome = getIntent();
                if (intentFromHome != null) {
                    if (intentFromHome.hasExtra(Intent.EXTRA_TEXT)) {
                        recipe = intentFromHome.getParcelableExtra(Intent.EXTRA_TEXT);
                    }
                }
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.recipe_detail_container, RecipeDetailFragment.newInstance(recipe), FRAGMENT_TAG )
                        .addToBackStack(FRAGMENT_TAG)
                        .commit();

            } else {

                Fragment mContent  = getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.recipe_detail_container, mContent)
                        .commit();
            }
        }


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("recipe", recipe);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        recipe = savedInstanceState.getParcelable("recipe");
        Log.d(TAG, "Restoring recipe from bundle during orientation change");
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            super.onBackPressed();
        } else if (getSupportFragmentManager().getBackStackEntryCount() == 1 ){
            finish();
        } else {
            if (mTwoPane) {
                    finish();
            }
            getSupportFragmentManager().popBackStack();
        }
    }

    @Override
    public void onStepSelected(Step step) {
        Toast.makeText(this, "Step selected = " + step.getDescription(), Toast.LENGTH_LONG).show();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.view_recipe_step_container, ViewRecipeStepFragment.newInstance(step), FRAGMENT_TAG)
                .addToBackStack(FRAGMENT_TAG)
                .commit();

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
