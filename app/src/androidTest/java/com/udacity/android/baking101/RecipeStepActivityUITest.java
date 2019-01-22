package com.udacity.android.baking101;

import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.CoreMatchers.not;


public class RecipeStepActivityUITest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class, false, true) ;


    private IdlingRegistry idlingRegistry = IdlingRegistry.getInstance();
    private IdlingResource recipeIdlingResource;

    @Before
    public void setup() {
        recipeIdlingResource = mActivityTestRule.getActivity().getIdlingResource();

        idlingRegistry.register(recipeIdlingResource);

        onView(withId(R.id.recyclerview_recipe))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));
    }


    @Test
    public void ClickRecipeStepView_OpensRecipeStepActivity() {

        onView(withId(R.id.recyclerview_recipe_steps))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        onView(withId(R.id.recipe_step_fragment))
                .check(matches(isDisplayed()));

        onView(withId(R.id.previous_step))
                .check(matches(not(isEnabled())));
    }


    @After
    public void unregisterIdlingResource()
    {
        if(recipeIdlingResource != null) {
            idlingRegistry.unregister(recipeIdlingResource);
        }
    }
}
