package tech.niocoders.com.bakingapp;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
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

@RunWith(AndroidJUnit4.class)
public class BakingActivityFoodTest {
    public static String FOOD_NAME =  "Nutella Pie";

    @Rule
    public ActivityTestRule<BakingActivity> mActivityTestRule = new ActivityTestRule<>(BakingActivity.class);

    @Test
    public void checkMainBakingLayout() {
        onView(withId(R.id.mainLayout)).check(matches(isDisplayed()));
    }


    @Test
    public void clickGridViewItem_OpensFoodDetailActivity() {

        // Uses {@link Espresso#onData(org.hamcrest.Matcher)} to get a reference to a specific
        // gridview item and clicks it.
        onView(ViewMatchers.withId(R.id.food_recyclerview))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1,
                        click()));

        onView(withId(R.id.food_description)).check(matches(isDisplayed()));


    }

}
