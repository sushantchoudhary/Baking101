package com.udacity.android.baking101;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;

import com.udacity.android.baking101.model.Step;
import com.udacity.android.baking101.ui.main.ViewRecipeStepFragment;

public class ViewRecipeStepActivity extends AppCompatActivity {

    private static final String FRAGMENT_TAG = "ViewRecipeStepFragment";
    private Step step;
    private int stepCount;
    private Button previousStep;
    private Button nextStep;

    private static final  String TAG = ViewRecipeStepActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_recipe_step_activity);
        if (savedInstanceState == null) {

            Intent intentFromHome = getIntent();
            if (intentFromHome != null) {
                if (intentFromHome.hasExtra(Intent.EXTRA_TEXT)) {
                    step = intentFromHome.getParcelableExtra(Intent.EXTRA_TEXT);
                }

            }
            setupUI(step);

        } else {
            step = savedInstanceState.getParcelable("step");
            setupUI(step);
        }

    }

    private void setupUI(Step step) {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.view_recipe_step_container, ViewRecipeStepFragment.newInstance(step), FRAGMENT_TAG)
                .addToBackStack(FRAGMENT_TAG)
                .commit();

        this.setTitle(step.getShortDescription());

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


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("step", step);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        step = savedInstanceState.getParcelable("step");
        Log.d(TAG, "Restoring step from bundle during orientation change");
    }
}
