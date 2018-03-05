package com.igrow.android.sensordetail;

import android.content.Intent;
import android.content.res.Resources;
import android.support.annotation.Nullable;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.igrow.android.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.UUID;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class EnvironmentalSensorDetailActivityTest {

    @Rule
    public IntentsTestRule<EnvironmentalSensorDetailActivity> intentsTestRule =
            new IntentsTestRule<>(EnvironmentalSensorDetailActivity.class, false, false);


    @Test
    public void launchActivity_activityLaunched() {
        // Launch activity to add a new task
        launchNewSensorActivity(null);

        // Verify that the activity is still displayed (a correct task would close it).
        onView(withId(R.id.sensor_detail_frag)).check(matches(isDisplayed()));
    }

    /**
     * @param sensorId is null if used to add a new task, otherwise it edits the task.
     */
    private void launchNewSensorActivity(@Nullable UUID sensorId) {
        Intent intent = new Intent(InstrumentationRegistry.getInstrumentation()
                .getTargetContext(), EnvironmentalSensorDetailActivity.class);

        intent.putExtra(EnvironmentalSensorDetailFragment.ARGUMENT_SENSOR_ID, sensorId);
        intentsTestRule.launchActivity(intent);
    }

    /**
     * Matches the toolbar title with a specific string resource.
     *
     * @param resourceId the ID of the string resource to match
     */
    public static Matcher<View> withToolbarTitle(final int resourceId) {
        return new BoundedMatcher<View, Toolbar>(Toolbar.class) {

            @Override
            public void describeTo(Description description) {
                description.appendText("with toolbar title from resource id: ");
                description.appendValue(resourceId);
            }

            @Override
            protected boolean matchesSafely(Toolbar toolbar) {
                CharSequence expectedText = "";
                try {
                    expectedText = toolbar.getResources().getString(resourceId);
                } catch (Resources.NotFoundException ignored) {
                    /* view could be from a context unaware of the resource id. */
                }
                CharSequence actualText = toolbar.getTitle();
                return expectedText.equals(actualText);
            }
        };
    }
}