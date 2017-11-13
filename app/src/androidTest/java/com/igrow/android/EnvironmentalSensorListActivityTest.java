package com.igrow.android;

import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

/**
 * Created by jsr on 13/11/17.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class EnvironmentalSensorListActivityTest {
    @Test
    public void addNewSensorToSensorCollection() throws Exception {

        onView(withId(R.id.activity_environmentalsensor_list)).perform(click());


    }

}