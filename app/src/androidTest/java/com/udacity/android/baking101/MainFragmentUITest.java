package com.udacity.android.baking101;

import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.udacity.android.baking101.ui.main.MainFragment;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.CoreMatchers.anyOf;

/**
 * TODO Plan to use androidx fragment test scenario APIs
 **/
@RunWith(AndroidJUnit4.class)
public class MainFragmentUITest {

    @Rule
    public ActivityTestRule<SingleFragmentActivity> mActivityTestRule = new ActivityTestRule<>(SingleFragmentActivity.class, false, true);

    @Before
    public void setup() {}

    @Test @Ignore
    public void ViewMainActivity_VerifyFragmentLoad() {

        MainFragment fragment = MainFragment.newInstance();

        mActivityTestRule.getActivity().runOnUiThread(() -> {
            mActivityTestRule.getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, fragment )
                    .commit();
        });

        onView(anyOf(withId(R.id.pb_loading_indicator))).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.INVISIBLE)));
    }

}
