package com.repofetcher;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.core.AllOf.allOf;

/**
 * Created by ricar on 18/10/2016.
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class LoginCenterTest {

    @Rule
    public ActivityTestRule<MainActivity> mainActivityTestRule =
            new ActivityTestRule<>(MainActivity.class);

    @Before
    public void init(){
        mainActivityTestRule.getActivity().goToLoginCenter();
    }

    @Test
    public void clickOnGitHub_OpensGitHubWebView(){
        onView(withId(R.id.login_github_button)).perform(click());

        onView(withId(R.id.web_view)).check(matches(allOf(isDisplayed(), isEnabled())));
    }

    @Test
    public void clickOnBitbucket_OpensGitHubWebView(){
        onView(withId(R.id.login_bitbucket_button)).perform(click());

        onView(withId(R.id.web_view)).check(matches(allOf(isDisplayed(), isEnabled())));
    }
}
