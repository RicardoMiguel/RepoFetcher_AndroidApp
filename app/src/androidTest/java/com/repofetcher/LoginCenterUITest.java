package com.repofetcher;

import android.support.annotation.StringRes;
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
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.AllOf.allOf;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class LoginCenterUITest {

    @Rule
    public ActivityTestRule<MainActivity> mainActivityTestRule =
            new ActivityTestRule<>(MainActivity.class);

    @Before
    public void init(){
        mainActivityTestRule.getActivity().goToLoginCenter();
    }

    @Test
    public void showNoSessionsView(){
        onView(withText(R.string.to_add_label));
    }

    @Test
    public void clickOnGitHub_OpensGitHubWebView() throws InterruptedException {
        clickOnRepository_OpensWebView(R.string.github);
        // Commented due to "script-src assets-cdn.github.com"

//        onWebView(withId(R.id.web_view)).forceJavascriptEnabled()
//                .withElement(findElement(Locator.NAME, "login")).perform(webKeys(""))
//                .withElement(findElement(Locator.NAME, "password")).perform(webKeys(""));
//                .withElement(findElement(Locator.NAME, "//*[@id=\"login\"]/form/div[3]")).perform(webClick());

//        onView(withText(R.string.github)).check(matches(allOf(isDisplayed(), not(isEnabled()))));
        //.perform(webKeys("RicardoMiguel"));
    }

    @Test
    public void clickOnBitbucket_OpensBitbucketWebView() throws InterruptedException {
        clickOnRepository_OpensWebView(R.string.bitbucket);
        //Commented due to java.lang.RuntimeException: java.util.concurrent.TimeoutException: Timeout waiting for task.
        //The webview takes too long to load.

//        onWebView(withId(R.id.web_view)).forceJavascriptEnabled();
//        onWebView(withId(R.id.web_view)).withElement(findElement(Locator.ID, "js-email-field")).withTimeout(25, TimeUnit.SECONDS).perform(webKeys(""))
//                .withElement(findElement(Locator.ID, "js-password-field")).perform(webKeys(""))
//                .withElement(findElement(Locator.XPATH, "//*[@id=\"aid-login-form\"]/div[2]/input")).perform(webClick());
//        Thread.sleep(2000);
//        onView(withText(R.string.bitbucket)).check(matches(allOf(isDisplayed(), not(isEnabled()))));
    }

    private void clickOnRepository_OpensWebView(@StringRes int resource){
        onView(withId(R.id.floating_button)).perform(click());

        onView(withText(resource)).perform(click());

        onView(withId(R.id.web_view)).check(matches(allOf(isDisplayed(), isEnabled())));
    }
}
