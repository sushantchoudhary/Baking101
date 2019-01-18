package com.udacity.android.baking101;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.udacity.android.baking101.model.Recipe;
import com.udacity.android.baking101.ui.main.RecipeDetailFragment;

public class RecipeDetailActivity extends AppCompatActivity {

    private Recipe recipe;
    private static final String FRAGMENT_TAG = "RecipeDetailFragment";
    private static  final String TAG = RecipeDetailActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_detail_activity);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

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
            getSupportFragmentManager().popBackStack();

        }
    }
}
