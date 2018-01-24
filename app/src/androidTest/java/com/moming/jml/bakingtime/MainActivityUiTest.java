package com.moming.jml.bakingtime;

/**
 * Created by jml on 2018/1/24.
 */

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.scrollToPosition;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class MainActivityUiTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule =
            new ActivityTestRule<>(MainActivity.class);
    private static final Intent MY_ACTIVITY_INTENT = new Intent(InstrumentationRegistry.getTargetContext(), MainActivity.class);

    @Before
    public void setup(){
        mActivityTestRule.launchActivity(MY_ACTIVITY_INTENT);
    }

    @Test
    public void RecipeCardShow_checkItsText(){
        onView(withRecyclerView(R.id.rv_recipe_card).atPosition(0))
                .check(matches(hasDescendant(withText("Nutella Pie"))));
        onView(withId(R.id.rv_recipe_card)).perform(scrollToPosition(1));
        onView(withRecyclerView(R.id.rv_recipe_card).atPosition(1)).perform(click());


    }



    static RecyclerViewMatcher withRecyclerView(final int recyclerViewId) {
        return new RecyclerViewMatcher(recyclerViewId);
    }



}

