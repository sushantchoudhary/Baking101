package com.udacity.android.baking101;

import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.content.Intent.EXTRA_TEXT;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtraWithKey;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

public class MainActivityIntentTest {

    @Rule
    public IntentsTestRule<MainActivity> activityTestRule = new IntentsTestRule<>(MainActivity.class);

    private IdlingResource recipeIdlingResource;
    private IdlingRegistry idlingRegistry =  IdlingRegistry.getInstance();


    @Before
    public void setup() {
        recipeIdlingResource = activityTestRule.getActivity().getIdlingResource();
        idlingRegistry.register(recipeIdlingResource);
        }

    @Test
    public void ClickRecyclerViewItem_createRecipeDetailsIntent() {

        onView(withId(R.id.recyclerview_recipe))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));
        intended(hasExtraWithKey(EXTRA_TEXT));
        intended( hasComponent(RecipeDetailActivity.class.getName()));
    }


    @After
    public void unregisterIdlingResource() {
        if(recipeIdlingResource != null) {
            idlingRegistry.unregister(recipeIdlingResource);
        }
    }

}
