package com.udacity.android.baking101;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.udacity.android.baking101.ui.main.MainFragment;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.CoreMatchers.allOf;
import static org.junit.Assert.assertEquals;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    public static final Intent MAIN_ACTIVITY_INTENT =   new Intent(InstrumentationRegistry.getTargetContext(), MainActivity.class);

    public IntentsTestRule<MainActivity> mActivityTestRule = new IntentsTestRule<>(MainActivity.class);

    @Before
    public void setup() {
        mActivityTestRule.launchActivity(MAIN_ACTIVITY_INTENT);
    }

    @Test @Ignore
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.udacity.android.baking101", appContext.getPackageName());
    }

    @Test
    public void ClickRecipeView_OpensRecipeDetailActivity() {
        MainFragment mainFragment = new MainFragment();

        mActivityTestRule.getActivity().getSupportFragmentManager().beginTransaction()
                .add(R.id.container, mainFragment).commit();

        onView(allOf(isDisplayed()), ViewMatchers.withId(R.id.recyclerview_recipe))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));

        intended(hasComponent(RecipeDetailActivity.class.getSimpleName()));



    }
}
