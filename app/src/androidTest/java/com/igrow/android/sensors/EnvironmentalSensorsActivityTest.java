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
import com.igrow.android.data.EnvironmentalSensor;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.UUID;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.AllOf.allOf;

/**
 * Created by jsr on 13/11/17.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class EnvironmentalSensorsActivityTest {

    private final static String TEST_ADDRESS = "c0:9e:19:a7:ce:9c";

    private final static UUID TEST_ID_2 = UUID.fromString("d2fb5bd2-815d-4171-a516-a3e35443f269");

    private final static String TEST_ADDRESS_2 = "c0:a7:9e:19:9c:ce";

    private final static EnvironmentalSensor sensor2 = new EnvironmentalSensor(
            TEST_ID_2,
            TEST_ADDRESS_2,
            "In the greenhouse",
            12,
            1514936648887L,
            0,
            null,
            null);

    @Rule
    public IntentsTestRule<EnvironmentalSensorsActivity> intentsTestRule =
            new IntentsTestRule<>(EnvironmentalSensorsActivity.class);

    //@BeforeClass
    //public static void stub

    @Before
    public void resetState() {
        ViewModelFactory.destroyInstance();
        Injection.provideEnvironmentalSensorsRepository(InstrumentationRegistry.getTargetContext())
                .deleteAllEnvironmentalSensors();
        // Modify the repository before the activity starts
        // as there is some delay when propagating the UI change
        // which causes the related view to not be displayed
        // when perform() ing the click()
        addSensorToMockRepository();
    }

    @Before
    public void stubEnvironmentalSensorListScanActivityIntent() {

        Instrumentation.ActivityResult result = createEnvironmentalSensorListScanActivityResultStub();

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
    public void addNewSensor_addedToCollection() throws Exception {

        onView(ViewMatchers.withId(R.id.fab_add_sensor)).perform(click());

        intended(allOf(hasComponent(new ComponentName(getTargetContext(),
                        EnvironmentalSensorsScanActivity.class)),
                hasAction(Intent.ACTION_PICK)));

    }

//    @Test
//    public void recyclerView_matchesCollection() throws Exception {

//        RecyclerViewInteraction.
//                <EnvironmentalSensor>onRecyclerView(withId(R.id.recyclerview_environmentalsensor))
//                .withItems(items)
//                .check(new RecyclerViewInteraction.ItemViewAssertion<EnvironmentalSensor>() {
//                    @Override
//                    public void check(EnvironmentalSensor item, View view, NoMatchingViewException e) {
//                        matches(hasDescendant(withText(item.getAddress())))
//                                .check(view, e);
//                    }
//                });
//    }

    @Test
    public void rowClick_displaysDetailFragment() throws Exception {

        onView(allOf(withId(R.id.textview_device_address),
                withText(TEST_ADDRESS_2))).perform(click());

        onView(withId(R.id.sensor_detail_frag)).check(matches(isDisplayed()));

    }

    private Instrumentation.ActivityResult createEnvironmentalSensorListScanActivityResultStub() {

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

    private void addSensorToMockRepository() {
        Injection.provideEnvironmentalSensorsRepository(InstrumentationRegistry.getTargetContext())
                .saveEnvironmentalSensor(sensor2);
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