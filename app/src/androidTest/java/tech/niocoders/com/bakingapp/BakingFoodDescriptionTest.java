package tech.niocoders.com.bakingapp;


import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
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
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class BakingFoodDescriptionTest {
    //get the target context
    public Context mContext = InstrumentationRegistry.getTargetContext();
    //test rule for out description activity views for given item
    @Rule
    public ActivityTestRule<FoodDescription> foodDescriptionActivityTestRule =  new ActivityTestRule<>(FoodDescription.class);
    //lets test the Ingredient object


    @Test
    public void TestUIDisplayElementsWithExplicitIntentContent()
    {
        Intent intent = new Intent();
        intent.putExtra(BakingActivity.FOOD_ID,1);
        intent.putExtra(BakingActivity.FOOD_AUTHOR,mContext.getResources()
        .getString(R.string.food_author));
        intent.putExtra(BakingActivity.FOOD_NAME,mContext.
        getResources().getString(R.string.food_name));

        Preference.saveFoodIdAndKey(mContext,mContext.getResources().getString(R.string.current_food_index)
        ,mContext.getResources().getString(R.string.food_name));


        foodDescriptionActivityTestRule.launchActivity(intent);

        //check the author textView
        onView(withId(R.id.Author)).check((matches(withText(
                mContext.getResources().getString(R.string.food_author)
        ))));
        //check the Recipe title name text
        onView(withId(R.id.foodName)).check((matches(withText(
                mContext.getResources().getString(R.string.current_page_title_name)
        ))));

        //lets check the current page index for the recycler view text
        onView(withId(R.id.TutorialStep)).check((matches(withText(
                mContext.getResources().getString(R.string.current_page_index)
        ))));


    }


    @Test
    public void CheckPageClickForRecipeIntro()
    {
        onView(ViewMatchers.withId(R.id.tutorial_pages))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1,
                        click()));
        //lets check the page
        onView(withId(R.id.TutorialStep)).check((matches(withText(
                mContext.getResources().getString(R.string.current_description_index)
        ))));

        //check the current short description
        onView(withId(R.id.short_description)).check((matches(withText(
                mContext.getResources().getString(R.string.starting_prep)
        ))));

        //check the full Description
        onView(withId(R.id.description)).check((matches(withText(
                mContext.getResources().getString(R.string.description_prep)
        ))));

    }

}
