package com.repofetcher;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by ricar on 18/10/2016.
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class IntroScreenTest {

    @Rule
    public ActivityTestRule<MainActivity> mainActivityActivityTestRule =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void clickLoginScreen_OpensLoginScreenUi(){
        onView(withId(R.id.login_center_button)).perform(click());

        onView(withId(R.id.login_github_button)).check(matches(isDisplayed()));
    }
}
