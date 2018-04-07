package com.igrow.android.data.source;

/**
 * Created by jsr on 3/01/18.
 */

import com.google.common.collect.Lists;
import com.igrow.android.data.EnvironmentalSensor;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for the implementation of the in-memory repository with cache.
 */
public class EnvironmentalSensorsRepositoryTest {

    private final static UUID TEST_ID_1 = UUID.fromString("f24d1220-98ca-455c-a5ff-b39806ded040");

    private final static UUID TEST_ID_2 = UUID.fromString("d2fb5bd2-815d-4171-a516-a3e35443f269");

    private final static UUID TEST_ID_3 = UUID.fromString("ccf786b0-393c-4ae8-ac3a-4261cb79325b");

    private final static UUID TEST_ID_4 = UUID.fromString("caf3ba38-af9a-4a66-b424-0a09052c5d26");

    private final static String TEST_ADDRESS_1 = "c0:9e:19:a7:ce:9c";

    private final static String TEST_ADDRESS_2 = "c0:a7:9e:19:9c:ce";

    private final static String TEST_ADDRESS_3 = "6e:73:dd:57:73:b6";

    // We initialise the sensors to 3
    private final static EnvironmentalSensor sensor1 = new EnvironmentalSensor(
            TEST_ID_1,
            TEST_ADDRESS_1,
            "Bottom of the garden",
            25,
            1514936648636L,
            0,
            null,
            null);
    private final static EnvironmentalSensor sensor2 = new EnvironmentalSensor(
            TEST_ID_2,
            TEST_ADDRESS_2,
            "In the greenhouse",
            12,
            1514936648887L,
            0,
            null,
            null);
    private final static EnvironmentalSensor sensor3 = new EnvironmentalSensor(
            TEST_ID_3,
            TEST_ADDRESS_3,
            "Middle of polytunnel",
            37,
            1514936658621L,
            0,
            null,
            null);

    private static List<EnvironmentalSensor> ENVIRONMENTAL_SENSORS = Lists.newArrayList(sensor1, sensor2, sensor3);
    
    private EnvironmentalSensorsRepository mEnvironmentalSensorsRepository;

    @Mock
    private EnvironmentalSensorsDataSource mEnvironmentalSensorsRemoteDataSource;

    @Mock
    private EnvironmentalSensorsDataSource mEnvironmentalSensorsLocalDataSource;

    @Mock
    private EnvironmentalSensorsDataSource.GetEnvironmentalSensorCallback mGetEnvironmentalSensorCallback;

    @Mock
    private EnvironmentalSensorsDataSource.LoadEnvironmentalSensorsCallback mLoadEnvironmentalSensorsCallback;

    @Captor
    private ArgumentCaptor<EnvironmentalSensorsDataSource.LoadEnvironmentalSensorsCallback> mEnvironmentalSensorsCallbackCaptor;

    @Captor
    private ArgumentCaptor<EnvironmentalSensorsDataSource.GetEnvironmentalSensorCallback> mEnvironmentalSensorCallbackCaptor;

    @Before
    public void setupEnvironmentalSensorsRepository() {
        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
        // inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.initMocks(this);

        // Get a reference to the class under test
        mEnvironmentalSensorsRepository = EnvironmentalSensorsRepository.getInstance(
                mEnvironmentalSensorsRemoteDataSource, mEnvironmentalSensorsLocalDataSource);
    }

    @After
    public void destroyRepositoryInstance() {
        EnvironmentalSensorsRepository.destroyInstance();
    }

    @Test
    public void getEnvironmentalSensors_repositoryCachesAfterFirstApiCall() {
        // Given a setup Captor to capture callbacks
        // When two calls are issued to the environmental sensors repository
        twoEnvironmentalSensorsLoadCallsToRepository(mLoadEnvironmentalSensorsCallback);

        // Then environmental sensors were only requested once from Service API
        verify(mEnvironmentalSensorsRemoteDataSource).getEnvironmentalSensors(any(EnvironmentalSensorsDataSource.LoadEnvironmentalSensorsCallback.class));
    }

    @Test
    public void getEnvironmentalSensors_requestsAllEnvironmentalSensorsFromLocalDataSource() {
        // When environmental sensors are requested from the environmental sensors repository
        mEnvironmentalSensorsRepository.getEnvironmentalSensors(mLoadEnvironmentalSensorsCallback);

        // Then environmental sensors are loaded from the local data source
        verify(mEnvironmentalSensorsLocalDataSource).getEnvironmentalSensors(any(EnvironmentalSensorsDataSource.LoadEnvironmentalSensorsCallback.class));
    }

    @Test
    public void saveEnvironmentalSensor_savesEnvironmentalSensorToServiceAPI() {
        // Given a stub task with title and description
        EnvironmentalSensor newEnvironmentalSensor = new EnvironmentalSensor(
                TEST_ID_4,
                "96:af:0f:a0:2a:57",
                "Basement",
                9,
                1514936642213L,
                1234,
                null,
                null);

        // When a task is saved to the environmental sensors repository
        mEnvironmentalSensorsRepository.saveEnvironmentalSensor(newEnvironmentalSensor);

        // Then the service API and persistent repository are called and the cache is updated
        verify(mEnvironmentalSensorsRemoteDataSource).saveEnvironmentalSensor(newEnvironmentalSensor);
        verify(mEnvironmentalSensorsLocalDataSource).saveEnvironmentalSensor(newEnvironmentalSensor);
        assertThat(mEnvironmentalSensorsRepository.mCachedEnvironmentalSensors.size(), is(1));
    }

    @Test
    public void getEnvironmentalSensor_requestsSingleEnvironmentalSensorFromLocalDataSource() {
        // When a task is requested from the environmental sensors repository
        mEnvironmentalSensorsRepository.getEnvironmentalSensor(TEST_ID_1, mGetEnvironmentalSensorCallback);

        // Then the sensor is loaded from the database
        verify(mEnvironmentalSensorsLocalDataSource).getEnvironmentalSensor(eq(TEST_ID_1), any(
                EnvironmentalSensorsDataSource.GetEnvironmentalSensorCallback.class));
    }

    @Test
    public void getEnvironmentalSensorByAddress_requestsSingleEnvironmentalSensorFromLocalDataSource() {
        // When a task is requested from the environmental sensors repository
        mEnvironmentalSensorsRepository.getEnvironmentalSensor(TEST_ADDRESS_2, mGetEnvironmentalSensorCallback);

        // Then the sensor is loaded from the database
        verify(mEnvironmentalSensorsLocalDataSource).getEnvironmentalSensor(eq(TEST_ADDRESS_2), any(
                EnvironmentalSensorsDataSource.GetEnvironmentalSensorCallback.class));
    }

    @Test
    public void deleteAllEnvironmentalSensors_deleteEnvironmentalSensorsToServiceAPIUpdatesCache() {
        // Given 2 stub completed environmental sensors and 1 stub active environmental sensors in the repository

        mEnvironmentalSensorsRepository.saveEnvironmentalSensor(sensor1);

        mEnvironmentalSensorsRepository.saveEnvironmentalSensor(sensor2);

        mEnvironmentalSensorsRepository.saveEnvironmentalSensor(sensor3);

        // When all environmental sensors are deleted to the environmental sensors repository
        mEnvironmentalSensorsRepository.deleteAllEnvironmentalSensors();

        // Verify the data sources were called
        verify(mEnvironmentalSensorsRemoteDataSource).deleteAllEnvironmentalSensors();
        verify(mEnvironmentalSensorsLocalDataSource).deleteAllEnvironmentalSensors();

        assertThat(mEnvironmentalSensorsRepository.mCachedEnvironmentalSensors.size(), is(0));
    }

    @Test
    public void deleteEnvironmentalSensor_deleteEnvironmentalSensorToServiceAPIRemovedFromCache() {
        // Given a task in the repository

        mEnvironmentalSensorsRepository.saveEnvironmentalSensor(sensor3);
        assertThat(mEnvironmentalSensorsRepository.mCachedEnvironmentalSensors.containsKey(sensor3.getSensorId()), is(true));

        // When deleted
        mEnvironmentalSensorsRepository.deleteEnvironmentalSensor(sensor3.getSensorId());

        // Verify the data sources were called
        verify(mEnvironmentalSensorsRemoteDataSource).deleteEnvironmentalSensor(sensor3.getSensorId());
        verify(mEnvironmentalSensorsLocalDataSource).deleteEnvironmentalSensor(sensor3.getSensorId());

        // Verify it's removed from repository
        assertThat(mEnvironmentalSensorsRepository.mCachedEnvironmentalSensors.containsKey(sensor3.getSensorId()), is(false));
    }

    @Test
    public void getEnvironmentalSensorsWithDirtyCache_environmentalSensorsAreRetrievedFromRemote() {
        // When calling getEnvironmentalSensors in the repository with dirty cache
        mEnvironmentalSensorsRepository.refreshEnvironmentalSensors();
        mEnvironmentalSensorsRepository.getEnvironmentalSensors(mLoadEnvironmentalSensorsCallback);

        // And the remote data source has data available
        setEnvironmentalSensorsAvailable(mEnvironmentalSensorsRemoteDataSource, ENVIRONMENTAL_SENSORS);

        // Verify the environmental sensors from the remote data source are returned, not the local
        verify(mEnvironmentalSensorsLocalDataSource, never()).getEnvironmentalSensors(mLoadEnvironmentalSensorsCallback);
        verify(mLoadEnvironmentalSensorsCallback).onEnvironmentalSensorsLoaded(ENVIRONMENTAL_SENSORS);
    }

    @Test
    public void getEnvironmentalSensorsWithLocalDataSourceUnavailable_environmentalSensorAreRetrievedFromRemote() {
        // When calling getEnvironmentalSensors in the repository
        mEnvironmentalSensorsRepository.getEnvironmentalSensors(mLoadEnvironmentalSensorsCallback);

        // And the local data source has no data available
        setEnvironmentalSensorsNotAvailable(mEnvironmentalSensorsLocalDataSource);

        // And the remote data source has data available
        setEnvironmentalSensorsAvailable(mEnvironmentalSensorsRemoteDataSource, ENVIRONMENTAL_SENSORS);

        // Verify the environmental sensors from the local data source are returned
        verify(mLoadEnvironmentalSensorsCallback).onEnvironmentalSensorsLoaded(ENVIRONMENTAL_SENSORS);
    }

    @Test
    public void getEnvironmentalSensorsWithBothDataSourcesUnavailable_firesOnDataUnavailable() {
        // When calling getEnvironmentalSensors in the repository
        mEnvironmentalSensorsRepository.getEnvironmentalSensors(mLoadEnvironmentalSensorsCallback);

        // And the local data source has no data available
        setEnvironmentalSensorsNotAvailable(mEnvironmentalSensorsLocalDataSource);

        // And the remote data source has no data available
        setEnvironmentalSensorsNotAvailable(mEnvironmentalSensorsRemoteDataSource);

        // Verify no data is returned
        verify(mLoadEnvironmentalSensorsCallback).onDataNotAvailable();
    }

    @Test
    public void getEnvironmentalSensorWithBothDataSourcesUnavailable_firesOnDataUnavailable() {
        // Given a task id
        final UUID sensorId = UUID.randomUUID();  // small but finite chance we have this already...

        // When calling getEnvironmentalSensor in the repository
        mEnvironmentalSensorsRepository.getEnvironmentalSensor(sensorId, mGetEnvironmentalSensorCallback);

        // And the local data source has no data available
        setEnvironmentalSensorNotAvailable(mEnvironmentalSensorsLocalDataSource, sensorId);

        // And the remote data source has no data available
        setEnvironmentalSensorNotAvailable(mEnvironmentalSensorsRemoteDataSource, sensorId);

        // Verify no data is returned
        verify(mGetEnvironmentalSensorCallback).onDataNotAvailable();
    }

    @Test
    public void getEnvironmentalSensors_refreshesLocalDataSource() {
        // Mark cache as dirty to force a reload of data from remote data source.
        mEnvironmentalSensorsRepository.refreshEnvironmentalSensors();

        // When calling getEnvironmentalSensors in the repository
        mEnvironmentalSensorsRepository.getEnvironmentalSensors(mLoadEnvironmentalSensorsCallback);

        // Make the remote data source return data
        setEnvironmentalSensorsAvailable(mEnvironmentalSensorsRemoteDataSource, ENVIRONMENTAL_SENSORS);

        // Verify that the data fetched from the remote data source was saved in local.
        verify(mEnvironmentalSensorsLocalDataSource, times(ENVIRONMENTAL_SENSORS.size())).saveEnvironmentalSensor(any(EnvironmentalSensor.class));
    }

    /**
     * Convenience method that issues two calls to the environmental sensors repository
     */
    private void twoEnvironmentalSensorsLoadCallsToRepository(EnvironmentalSensorsDataSource.LoadEnvironmentalSensorsCallback callback) {
        // When environmental sensors are requested from repository
        mEnvironmentalSensorsRepository.getEnvironmentalSensors(callback); // First call to API

        // Use the Mockito Captor to capture the callback
        verify(mEnvironmentalSensorsLocalDataSource).getEnvironmentalSensors(mEnvironmentalSensorsCallbackCaptor.capture());

        // Local data source doesn't have data yet
        mEnvironmentalSensorsCallbackCaptor.getValue().onDataNotAvailable();


        // Verify the remote data source is queried
        verify(mEnvironmentalSensorsRemoteDataSource).getEnvironmentalSensors(mEnvironmentalSensorsCallbackCaptor.capture());

        // Trigger callback so environmental sensors are cached
        mEnvironmentalSensorsCallbackCaptor.getValue().onEnvironmentalSensorsLoaded(ENVIRONMENTAL_SENSORS);

        mEnvironmentalSensorsRepository.getEnvironmentalSensors(callback); // Second call to API
    }

    private void setEnvironmentalSensorsNotAvailable(EnvironmentalSensorsDataSource dataSource) {
        verify(dataSource).getEnvironmentalSensors(mEnvironmentalSensorsCallbackCaptor.capture());
        mEnvironmentalSensorsCallbackCaptor.getValue().onDataNotAvailable();
    }

    private void setEnvironmentalSensorsAvailable(EnvironmentalSensorsDataSource dataSource, List<EnvironmentalSensor> environmentalSensors) {
        verify(dataSource).getEnvironmentalSensors(mEnvironmentalSensorsCallbackCaptor.capture());
        mEnvironmentalSensorsCallbackCaptor.getValue().onEnvironmentalSensorsLoaded(environmentalSensors);
    }

    private void setEnvironmentalSensorNotAvailable(EnvironmentalSensorsDataSource dataSource, UUID sensorId) {
        verify(dataSource).getEnvironmentalSensor(eq(sensorId), mEnvironmentalSensorCallbackCaptor.capture());
        mEnvironmentalSensorCallbackCaptor.getValue().onDataNotAvailable();
    }

    private void setEnvironmentalSensorAvailable(EnvironmentalSensorsDataSource dataSource, EnvironmentalSensor sensor) {
        verify(dataSource).getEnvironmentalSensor(eq(sensor.getSensorId()), mEnvironmentalSensorCallbackCaptor.capture());
        mEnvironmentalSensorCallbackCaptor.getValue().onEnvironmentalSensorLoaded(sensor);
    }
}