package com.igrow.android.addeditsensor;

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
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class EnvironmentalSensorAddEditActivityTest {

    @Rule
    public IntentsTestRule<EnvironmentalSensorAddEditActivity> intentsTestRule =
            new IntentsTestRule<>(EnvironmentalSensorAddEditActivity.class, false, false);


    @Test
    public void emptySensorName_isNotSaved() {
        // Launch activity to add a new task
        launchNewSensorActivity(null);

        // Add invalid title and description combination
        //onView(withId(R.id.add_edit_sensor_address)).perform(clearText());
        onView(withId(R.id.add_edit_sensor_fullname)).perform(clearText());
        // Try to save the task
        onView(withId(R.id.fab_edit_sensor_done)).perform(click());

        // Verify that the activity is still displayed (a correct task would close it).
        onView(withId(R.id.add_edit_sensor_fullname)).check(matches(isDisplayed()));
    }

//    @Test
//    public void toolbarTitle_newTask_persistsRotation() {
//        // Launch activity to add a new task
//        launchNewTaskActivity(null);
//
//        // Check that the toolbar shows the correct title
//        onView(withId(toolbar)).check(matches(withToolbarTitle(R.string.add_task)));
//
//        // Rotate activity
//        TestUtils.rotateOrientation(mActivityTestRule.getActivity());
//
//        // Check that the toolbar title is persisted
//        onView(withId(toolbar)).check(matches(withToolbarTitle(R.string.add_task)));
//    }
//
//    @Test
//    public void toolbarTitle_editTask_persistsRotation() {
//        // Put a task in the repository and start the activity to edit it
//        TasksRepository.destroyInstance();
//        FakeTasksRemoteDataSource.getInstance().addTasks(new Task("Title1", "", TASK_ID, false));
//        launchNewTaskActivity(TASK_ID);
//
//        // Check that the toolbar shows the correct title
//        onView(withId(toolbar)).check(matches(withToolbarTitle(R.string.edit_task)));
//
//        // Rotate activity
//        TestUtils.rotateOrientation(mActivityTestRule.getActivity());
//
//        // check that the toolbar title is persisted
//        onView(withId(toolbar)).check(matches(withToolbarTitle(R.string.edit_task)));
//    }

    /**
     * @param sensorId is null if used to add a new task, otherwise it edits the task.
     */
    private void launchNewSensorActivity(@Nullable UUID sensorId) {
        Intent intent = new Intent(InstrumentationRegistry.getInstrumentation()
                .getTargetContext(), EnvironmentalSensorAddEditActivity.class);

        intent.putExtra(EnvironmentalSensorAddEditFragment.ARGUMENT_EDIT_SENSOR_ID, sensorId);
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