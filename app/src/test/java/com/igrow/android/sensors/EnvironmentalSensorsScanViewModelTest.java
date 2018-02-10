package com.igrow.android.sensors;

import android.app.Application;
import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.lifecycle.Observer;
import android.content.res.Resources;

import com.google.common.collect.Lists;
import com.igrow.android.R;
import com.igrow.android.TestUtils;
import com.igrow.android.addeditsensor.EnvironmentalSensorAddEditActivity;
import com.igrow.android.bluetooth.EnvironmentalSensorBLEScanUpdate;
import com.igrow.android.data.EnvironmentalSensor;
import com.igrow.android.data.source.EnvironmentalSensorsDataSource;
import com.igrow.android.data.source.EnvironmentalSensorsRepository;
import com.igrow.android.sensordetail.EnvironmentalSensorDetailActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by jsr on 3/01/18.
 */

/**
 * Unit tests for the implementation of {@link EnvironmentalSensorsViewModel}
 */
// TODO: temporarily silence the unnecessary stubbing exception throwing
@RunWith(MockitoJUnitRunner.Silent.class)
public class EnvironmentalSensorsScanViewModelTest {

    // Executes each task synchronously using Architecture Components.
    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    private static List<EnvironmentalSensor> ENVIRONMENTAL_SENSORS;

    @Mock
    private EnvironmentalSensorsRepository mEnvironmentalSensorsRepository;

    @Mock
    private Application mContext;

    @Captor
    private ArgumentCaptor<EnvironmentalSensorsDataSource.LoadEnvironmentalSensorsCallback> mLoadEnvironmentalSensorsCallbackCaptor;

    private EnvironmentalSensorsScanViewModel mEnvironmentalSensorsScanViewModel;

    private final static String TEST_ADDRESS_1 = "c0:9e:19:a7:ce:9c";

    private final static String TEST_ADDRESS_2 = "c0:a7:9e:19:9c:ce";

    private final static String TEST_ADDRESS_3 = "6e:73:dd:57:73:b6";

    private final static String TEST_ADDRESS_4 = "5a:ef:43:69:13:e0";

    @Before
    public void setupEnvironmentalSensorsViewModel() {
        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
        // inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.initMocks(this);

        setupContext();

        // Get a reference to the class under test
        mEnvironmentalSensorsScanViewModel = new EnvironmentalSensorsScanViewModel(mContext, mEnvironmentalSensorsRepository);

        // We initialise the sensors to 3
        EnvironmentalSensor sensor1 = new EnvironmentalSensor(TEST_ADDRESS_1,
                "Bottom of the garden",
                25, 1514936648636L,
                null,
                null);
        EnvironmentalSensor sensor2 = new EnvironmentalSensor(TEST_ADDRESS_2,
                "In the greenhouse",
                12,
                1514936648887L,
                null,
                null);
        EnvironmentalSensor sensor3 = new EnvironmentalSensor(TEST_ADDRESS_3,
                "Middle of polytunnel",
                37,
                1514936658621L,
                null,
                null);

        ENVIRONMENTAL_SENSORS = Lists.newArrayList(sensor1, sensor2, sensor3);

        mEnvironmentalSensorsScanViewModel.getSnackbarMessage().removeObservers(TestUtils.TEST_OBSERVER);

    }

    private void setupContext() {
        when(mContext.getApplicationContext()).thenReturn(mContext);
        when(mContext.getString(R.string.successfully_saved_sensor_message))
                .thenReturn("EDIT_RESULT_OK");
        when(mContext.getString(R.string.successfully_added_sensor_message))
                .thenReturn("ADD_EDIT_RESULT_OK");
        when(mContext.getString(R.string.successfully_deleted_sensor_message))
                .thenReturn("DELETE_RESULT_OK");

        when(mContext.getResources()).thenReturn(mock(Resources.class));
    }

    @Test
    public void loadAllEnvironmentalSensorsFromRepository_dataLoaded() {
        // Given an initialized EnvironmentalSensorsViewModel with initialized tasks
        // When loading of EnvironmentalSensors is requested
        //mEnvironmentalSensorsScanViewModel.setFiltering(EnvironmentalSensorsFilterType.ALL_TASKS);
        mEnvironmentalSensorsScanViewModel.loadEnvironmentalSensors(true);

        // Callback is captured and invoked with stubbed tasks
        verify(mEnvironmentalSensorsRepository).getEnvironmentalSensors(mLoadEnvironmentalSensorsCallbackCaptor.capture());


        // Then progress indicator is shown
        assertTrue(mEnvironmentalSensorsScanViewModel.dataLoading.get());
        mLoadEnvironmentalSensorsCallbackCaptor.getValue().onEnvironmentalSensorsLoaded(ENVIRONMENTAL_SENSORS);

        // Then progress indicator is hidden
        assertFalse(mEnvironmentalSensorsScanViewModel.dataLoading.get());

        // And data loaded
        assertFalse(mEnvironmentalSensorsScanViewModel.sensors.isEmpty());
        assertTrue(mEnvironmentalSensorsScanViewModel.sensors.size() == 3);
    }

    @Test
    public void loadActiveEnvironmentalSensorsFromRepositoryAndLoadIntoView() {
        // Given an initialized EnvironmentalSensorsViewModel with initialized tasks
        // When loading of EnvironmentalSensors is requested
        mEnvironmentalSensorsScanViewModel.setFiltering(EnvironmentalSensorsFilterType.ACTIVE_SENSORS);
        mEnvironmentalSensorsScanViewModel.loadEnvironmentalSensors(true);

        // Callback is captured and invoked with stubbed tasks
        verify(mEnvironmentalSensorsRepository).getEnvironmentalSensors(mLoadEnvironmentalSensorsCallbackCaptor.capture());
        mLoadEnvironmentalSensorsCallbackCaptor.getValue().onEnvironmentalSensorsLoaded(ENVIRONMENTAL_SENSORS);

        // Then progress indicator is hidden
        assertFalse(mEnvironmentalSensorsScanViewModel.dataLoading.get());

        // And data loaded
        assertFalse(mEnvironmentalSensorsScanViewModel.sensors.isEmpty());
        // TODO:  We don't currently have a notion of an active sensor, so assume all are active
        assertTrue(mEnvironmentalSensorsScanViewModel.sensors.size() == 3);
    }

  /*  @Test
    public void clickOnFab_ShowsAddEnvironmentalSensorUi() {

        Observer<Void> observer = mock(Observer.class);

        mEnvironmentalSensorsScanViewModel.getNewEnvironmentalSensorEvent().observe(TestUtils.TEST_OBSERVER, observer);

        // When adding a new task
        mEnvironmentalSensorsScanViewModel.addNewEnvironmentalSensor();

        // Then the event is triggered
        verify(observer).onChanged(null);
    }*/

    @Test
    public void handleEnvironmentalSensorUpdate_AddsNewEnvironmentalSensor() {

        // Add some sensors
        mEnvironmentalSensorsScanViewModel.sensors.add(ENVIRONMENTAL_SENSORS.get(0));
        mEnvironmentalSensorsScanViewModel.sensors.add(ENVIRONMENTAL_SENSORS.get(1));

        // Stub result from BLE update to a previously unused address
        EnvironmentalSensorBLEScanUpdate update = mock(EnvironmentalSensorBLEScanUpdate.class);
        when(update.getAddress()).thenReturn(TEST_ADDRESS_3);
        when(update.getRSSI()).thenReturn(11);

        Observer<Void> newSensorObserver = mock(Observer.class);
        Observer<Void> updateSensorObserver = mock(Observer.class);

        mEnvironmentalSensorsScanViewModel.getNewEnvironmentalSensorEvent().observe(TestUtils.TEST_OBSERVER, newSensorObserver);
        mEnvironmentalSensorsScanViewModel.getUpdateEnvironmentalSensorEvent().observe(TestUtils.TEST_OBSERVER, updateSensorObserver);

        mEnvironmentalSensorsScanViewModel.handleEnvironmentalSensorUpdate(update);

        // assert that the correct event was raised
        verify(newSensorObserver).onChanged(null);
        verify(updateSensorObserver, never()).onChanged(null);

        // assert that sensors now contains the correct sensor ids
        assertTrue(mEnvironmentalSensorsScanViewModel.sensors.size() == 3);
    }

    @Test
    public void handleEnvironmentalSensorUpdate_UpdatesExistingEnvironmentalSensor() {

        // Add some sensors
        mEnvironmentalSensorsScanViewModel.sensors.add(ENVIRONMENTAL_SENSORS.get(0));
        mEnvironmentalSensorsScanViewModel.sensors.add(ENVIRONMENTAL_SENSORS.get(1));

        // Stub result from BLE update to a previously used address
        EnvironmentalSensorBLEScanUpdate update = mock(EnvironmentalSensorBLEScanUpdate.class);
        when(update.getAddress()).thenReturn(TEST_ADDRESS_2);
        when(update.getRSSI()).thenReturn(11);

        Observer<Void> newSensorObserver = mock(Observer.class);
        Observer<Void> updateSensorObserver = mock(Observer.class);

        mEnvironmentalSensorsScanViewModel.getNewEnvironmentalSensorEvent().observe(TestUtils.TEST_OBSERVER, newSensorObserver);
        mEnvironmentalSensorsScanViewModel.getUpdateEnvironmentalSensorEvent().observe(TestUtils.TEST_OBSERVER, updateSensorObserver);

        mEnvironmentalSensorsScanViewModel.handleEnvironmentalSensorUpdate(update);

        // assert that the correct event was raised
        verify(updateSensorObserver).onChanged(null);
        verify(newSensorObserver, never()).onChanged(null);
        // assert that sensors now contains the correct sensor ids
        assertTrue(mEnvironmentalSensorsScanViewModel.sensors.size() == 2);
    }

    @Test
    public void handleActivityResult_editOK() {
        // When EnvironmentalSensorDetailActivity sends a EDIT_RESULT_OK
        Observer<Integer> observer = mock(Observer.class);

        mEnvironmentalSensorsScanViewModel.getSnackbarMessage().observe(TestUtils.TEST_OBSERVER, observer);

        mEnvironmentalSensorsScanViewModel.handleActivityResult(
                EnvironmentalSensorAddEditActivity.REQUEST_CODE, EnvironmentalSensorDetailActivity.EDIT_RESULT_OK);

        // Then the snackbar shows the correct message
        verify(observer).onChanged(R.string.successfully_saved_sensor_message);
    }

    @Test
    public void handleActivityResult_addEditOK() {
        // When EnvironmentalSensorDetailActivity sends a EDIT_RESULT_OK
        Observer<Integer> observer = mock(Observer.class);

        mEnvironmentalSensorsScanViewModel.getSnackbarMessage().observe(TestUtils.TEST_OBSERVER, observer);

        // When EnvironmentalSensorAddEditActivity sends a ADD_EDIT_RESULT_OK
        mEnvironmentalSensorsScanViewModel.handleActivityResult(
                EnvironmentalSensorAddEditActivity.REQUEST_CODE, EnvironmentalSensorAddEditActivity.ADD_EDIT_RESULT_OK);

        // Then the snackbar shows the correct message
        verify(observer).onChanged(R.string.successfully_added_sensor_message);
    }

    @Test
    public void handleActivityResult_deleteOk() {
        // When EnvironmentalSensorDetailActivity sends a EDIT_RESULT_OK
        Observer<Integer> observer = mock(Observer.class);

        mEnvironmentalSensorsScanViewModel.getSnackbarMessage().observe(TestUtils.TEST_OBSERVER, observer);

        // When AddEditEnvironmentalSensorActivity sends a ADD_EDIT_RESULT_OK
        mEnvironmentalSensorsScanViewModel.handleActivityResult(
                EnvironmentalSensorAddEditActivity.REQUEST_CODE, EnvironmentalSensorDetailActivity.DELETE_RESULT_OK);

        // Then the snackbar shows the correct message
        verify(observer).onChanged(R.string.successfully_deleted_sensor_message);
    }

    @Test
    public void getEnvironmentalSensorsAddViewVisible() {
        // When the filter type is ALL_TASKS
        mEnvironmentalSensorsScanViewModel.setFiltering(EnvironmentalSensorsFilterType.ALL_SENSORS);

        // Then the "Add task" action is visible
        assertThat(mEnvironmentalSensorsScanViewModel.sensorsAddViewVisible.get(), is(true));
    }
}
