package com.igrow.android.sensors;

import android.app.Activity;
import android.app.Instrumentation;
import android.bluetooth.BluetoothAdapter;
import android.content.ComponentName;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.DataInteraction;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import com.igrow.android.Injection;
import com.igrow.android.R;
import com.igrow.android.ViewModelFactory;
import com.igrow.android.addeditsensor.EnvironmentalSensorAddEditActivity;
import com.igrow.android.addeditsensor.EnvironmentalSensorAddEditFragment;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withChild;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.AllOf.allOf;

/**
 * Created by jsr on 13/11/17.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class EnvironmentalSensorsScanActivityTest {

    private final static String TEST_ADDRESS = "c0:9e:19:a7:ce:9c";

    @Rule
    public IntentsTestRule<EnvironmentalSensorsScanActivity> intentsTestRule =
            new IntentsTestRule<>(EnvironmentalSensorsScanActivity.class);

    //@BeforeClass
    //public static void stub

    @Before
    public void resetState() {
        ViewModelFactory.destroyInstance();
        // not using the repository - the scanning state is transient
        //Injection.provideEnvironmentalSensorsRepository(InstrumentationRegistry.getTargetContext())
        //        .deleteAllEnvironmentalSensors();
    }

    @Before
    public void stubEnvironmentalSensorListScanActivityIntent() {

        Instrumentation.ActivityResult result = createEnvironmentalSensorScanActivityResultStub();

        intending(allOf(hasComponent(new ComponentName(getTargetContext(),
                        EnvironmentalSensorsScanActivity.class)),
                hasAction(Intent.ACTION_PICK))).respondWith(result);

    }

    @Before
    public void stubRequestEnableBluetoothIntent() {

        Instrumentation.ActivityResult result = createRequestEnableBluetoothResultStub();

        intending(hasComponent(new ComponentName(getTargetContext(),
                BluetoothAdapter.ACTION_REQUEST_ENABLE))).respondWith(result);

    }

    @Test
    public void collection_isUpdatedByScanService() throws Exception {



    }

    @Test
    public void recyclerView_matchesCollection() throws Exception {


    }

    @Test
    public void rowClick_startsEnvironmentalSensorAddEditActivity() throws Exception {

        onView(allOf(withId(R.id.sensor_item),
                withChild(withId(R.id.textview_device_address)))).perform(click());

        //intended(allOf(hasComponent(new ComponentName(getTargetContext(), EnvironmentalSensorAddEditActivity.class)),
        //        hasExtra(EnvironmentalSensorAddEditFragment.ARGUMENT_EDIT_SENSOR_ID, ));

    }

    private Instrumentation.ActivityResult createEnvironmentalSensorScanActivityResultStub() {

        Intent resultData = new Intent();

        resultData.putExtra(EnvironmentalSensorsScanActivity.EXTRA_DEVICE_ADDRESS, TEST_ADDRESS);
        Instrumentation.ActivityResult result =
                new Instrumentation.ActivityResult(Activity.RESULT_OK, resultData);
        return result;
    }

    private Instrumentation.ActivityResult createRequestEnableBluetoothResultStub() {

        // create a stub that indicates the user did not enable Bluetooth
        Intent resultData = new Intent();

        Instrumentation.ActivityResult result =
                new Instrumentation.ActivityResult(Activity.RESULT_CANCELED, resultData);
        return result;
    }

    /**
     * Uses {@link Espresso#onData(org.hamcrest.Matcher)} to get a reference to a specific row.
     * <p>
     * Note: A custom matcher can be used to match the content and have more readable code.
     * See the Custom Matcher Sample.
     * </p>
     *
     * @param str the content of the field
     * @return a {@link DataInteraction} referencing the row
     */
    private static DataInteraction onRow(String str) {
        //return onData(hasEntry(equalTo(LongListActivity.ROW_TEXT), is(str)));
        return null;
    }

}