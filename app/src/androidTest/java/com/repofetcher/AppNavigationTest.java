package com.repofetcher;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.KeyEvent;
import android.widget.EditText;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.pressKey;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by ricar on 18/10/2016.
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class AppNavigationTest {

    @Rule
    public ActivityTestRule<MainActivity> mainActivityTestRule =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void clickSearch_OpenRepositoriesUi(){
        onView(withId(R.id.action_search)).perform(click());

        onView(withId(android.support.design.R.id.search_src_text)).perform(typeText("RicardoMiguel"), pressKey(KeyEvent.KEYCODE_ENTER));

        onView(withId(R.id.tabLayout)).check(matches(isDisplayed()));

    }
}
