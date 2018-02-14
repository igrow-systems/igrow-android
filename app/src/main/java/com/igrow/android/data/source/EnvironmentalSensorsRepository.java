package com.igrow.android.data.source;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.igrow.android.data.EnvironmentalSensor;
import com.igrow.android.util.EspressoIdlingResource;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.inject.Singleton;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by jsr on 2/01/18.
 */

@Singleton
public class EnvironmentalSensorsRepository implements EnvironmentalSensorsDataSource {

    private volatile static EnvironmentalSensorsRepository INSTANCE = null;

    private final EnvironmentalSensorsDataSource mEnvironmentalSensorsRemoteDataSource;

    private final EnvironmentalSensorsDataSource mEnvironmentalSensorsLocalDataSource;

    /**
     * This variable has package local visibility so it can be accessed from tests.
     */
    Map<UUID, EnvironmentalSensor> mCachedEnvironmentalSensors;

    /**
     * Marks the cache as invalid, to force an update the next time data is requested. This variable
     * has package local visibility so it can be accessed from tests.
     */
    private boolean mCacheIsDirty = false;

    // Prevent direct instantiation.
    private EnvironmentalSensorsRepository(@NonNull EnvironmentalSensorsDataSource environmentalSensorsRemoteDataSource,
                                           @NonNull EnvironmentalSensorsDataSource environmentalSensorsLocalDataSource) {
        mEnvironmentalSensorsRemoteDataSource = checkNotNull(environmentalSensorsRemoteDataSource);
        mEnvironmentalSensorsLocalDataSource = checkNotNull(environmentalSensorsLocalDataSource);
    }

    /**
     * Returns the single instance of this class, creating it if necessary.
     *
     * @param environmentalSensorsRemoteDataSource the backend data source
     * @param environmentalSensorsLocalDataSource  the device storage data source
     * @return the {@link EnvironmentalSensorsRepository} instance
     */
    public static EnvironmentalSensorsRepository getInstance(EnvironmentalSensorsDataSource environmentalSensorsRemoteDataSource,
                                              EnvironmentalSensorsDataSource environmentalSensorsLocalDataSource) {
        if (INSTANCE == null) {
            synchronized (EnvironmentalSensorsRepository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new EnvironmentalSensorsRepository(environmentalSensorsRemoteDataSource, environmentalSensorsLocalDataSource);
                }
            }
        }
        return INSTANCE;
    }

    /**
     * Used to force {@link #getInstance(EnvironmentalSensorsDataSource, EnvironmentalSensorsDataSource)} to create a new instance
     * next time it's called.
     */
    public static void destroyInstance() {
        INSTANCE = null;
    }

    /**
     * Gets environmentalSensors from cache, local data source (SQLite) or remote data source, whichever is
     * available first.
     * <p>
     * Note: {@link LoadEnvironmentalSensorsCallback#onDataNotAvailable()} is fired if all data sources fail to
     * get the data.
     */
    @Override
    public void getEnvironmentalSensors(@NonNull final EnvironmentalSensorsDataSource.LoadEnvironmentalSensorsCallback callback) {
        checkNotNull(callback);

        // Respond immediately with cache if available and not dirty
        if (mCachedEnvironmentalSensors != null && !mCacheIsDirty) {
            callback.onEnvironmentalSensorsLoaded(new ArrayList<>(mCachedEnvironmentalSensors.values()));
            return;
        }

        EspressoIdlingResource.increment(); // App is busy until further notice

        if (mCacheIsDirty) {
            // If the cache is dirty we need to fetch new data from the network.
            getEnvironmentalSensorsFromRemoteDataSource(callback);
        } else {
            // Query the local storage if available. If not, query the network.
            mEnvironmentalSensorsLocalDataSource.getEnvironmentalSensors(new EnvironmentalSensorsDataSource.LoadEnvironmentalSensorsCallback() {
                @Override
                public void onEnvironmentalSensorsLoaded(List<EnvironmentalSensor> environmentalSensors) {
                    refreshCache(environmentalSensors);

                    EspressoIdlingResource.decrement(); // Set app as idle.
                    callback.onEnvironmentalSensorsLoaded(new ArrayList<>(mCachedEnvironmentalSensors.values()));
                }

                @Override
                public void onDataNotAvailable() {
                    getEnvironmentalSensorsFromRemoteDataSource(callback);
                }
            });
        }
    }

    @Override
    public void saveEnvironmentalSensor(@NonNull EnvironmentalSensor environmentalSensor) {
        checkNotNull(environmentalSensor);
        mEnvironmentalSensorsRemoteDataSource.saveEnvironmentalSensor(environmentalSensor);
        mEnvironmentalSensorsLocalDataSource.saveEnvironmentalSensor(environmentalSensor);

        // Do in memory cache update to keep the app UI up to date
        if (mCachedEnvironmentalSensors == null) {
            mCachedEnvironmentalSensors = new LinkedHashMap<>();
        }
        mCachedEnvironmentalSensors.put(environmentalSensor.getSensorId(), environmentalSensor);
    }

    /**
     * Gets environmentalSensors from local data source (sqlite) unless the table is new or empty. In that case it
     * uses the network data source. This is done to simplify the sample.
     * <p>
     * Note: {@link GetEnvironmentalSensorCallback#onDataNotAvailable()} is fired if both data sources fail to
     * get the data.
     */
    @Override
    public void getEnvironmentalSensor(@NonNull final UUID sensorId, @NonNull final GetEnvironmentalSensorCallback callback) {
        checkNotNull(sensorId);
        checkNotNull(callback);

        EnvironmentalSensor cachedEnvironmentalSensor = getEnvironmentalSensorWithId(sensorId);

        // Respond immediately with cache if available
        if (cachedEnvironmentalSensor != null) {
            callback.onEnvironmentalSensorLoaded(cachedEnvironmentalSensor);
            return;
        }

        EspressoIdlingResource.increment(); // App is busy until further notice

        // Load from server/persisted if needed.

        // Is the environmentalSensor in the local data source? If not, query the network.
        mEnvironmentalSensorsLocalDataSource.getEnvironmentalSensor(sensorId, new GetEnvironmentalSensorCallback() {
            @Override
            public void onEnvironmentalSensorLoaded(EnvironmentalSensor environmentalSensor) {
                // Do in memory cache update to keep the app UI up to date
                if (mCachedEnvironmentalSensors == null) {
                    mCachedEnvironmentalSensors = new LinkedHashMap<>();
                }
                mCachedEnvironmentalSensors.put(environmentalSensor.getSensorId(), environmentalSensor);

                EspressoIdlingResource.decrement(); // Set app as idle.

                callback.onEnvironmentalSensorLoaded(environmentalSensor);
            }

            @Override
            public void onDataNotAvailable() {
                mEnvironmentalSensorsRemoteDataSource.getEnvironmentalSensor(sensorId, new GetEnvironmentalSensorCallback() {
                    @Override
                    public void onEnvironmentalSensorLoaded(EnvironmentalSensor environmentalSensor) {
                        if (environmentalSensor == null) {
                            onDataNotAvailable();
                            return;
                        }
                        // Do in memory cache update to keep the app UI up to date
                        if (mCachedEnvironmentalSensors == null) {
                            mCachedEnvironmentalSensors = new LinkedHashMap<>();
                        }
                        mCachedEnvironmentalSensors.put(environmentalSensor.getSensorId(), environmentalSensor);
                        EspressoIdlingResource.decrement(); // Set app as idle.

                        callback.onEnvironmentalSensorLoaded(environmentalSensor);
                    }

                    @Override
                    public void onDataNotAvailable() {
                        EspressoIdlingResource.decrement(); // Set app as idle.

                        callback.onDataNotAvailable();
                    }
                });
            }
        });
    }

    @Override
    public void refreshEnvironmentalSensors() {
        mCacheIsDirty = true;
    }

    @Override
    public void deleteAllEnvironmentalSensors() {
        mEnvironmentalSensorsRemoteDataSource.deleteAllEnvironmentalSensors();
        mEnvironmentalSensorsLocalDataSource.deleteAllEnvironmentalSensors();

        if (mCachedEnvironmentalSensors == null) {
            mCachedEnvironmentalSensors = new LinkedHashMap<>();
        }
        mCachedEnvironmentalSensors.clear();
    }

    @Override
    public void deleteEnvironmentalSensor(@NonNull UUID sensorId) {
        mEnvironmentalSensorsRemoteDataSource.deleteEnvironmentalSensor(checkNotNull(sensorId));
        mEnvironmentalSensorsLocalDataSource.deleteEnvironmentalSensor(checkNotNull(sensorId));

        mCachedEnvironmentalSensors.remove(sensorId);
    }

    private void getEnvironmentalSensorsFromRemoteDataSource(@NonNull final LoadEnvironmentalSensorsCallback callback) {
        mEnvironmentalSensorsRemoteDataSource.getEnvironmentalSensors(new LoadEnvironmentalSensorsCallback() {
            @Override
            public void onEnvironmentalSensorsLoaded(List<EnvironmentalSensor> environmentalSensors) {
                refreshCache(environmentalSensors);
                refreshLocalDataSource(environmentalSensors);

                EspressoIdlingResource.decrement(); // Set app as idle.
                callback.onEnvironmentalSensorsLoaded(new ArrayList<>(mCachedEnvironmentalSensors.values()));
            }

            @Override
            public void onDataNotAvailable() {

                EspressoIdlingResource.decrement(); // Set app as idle.
                callback.onDataNotAvailable();
            }
        });
    }

    private void refreshCache(List<EnvironmentalSensor> environmentalSensors) {
        if (mCachedEnvironmentalSensors == null) {
            mCachedEnvironmentalSensors = new LinkedHashMap<>();
        }
        mCachedEnvironmentalSensors.clear();
        for (EnvironmentalSensor environmentalSensor : environmentalSensors) {
            mCachedEnvironmentalSensors.put(environmentalSensor.getSensorId(), environmentalSensor);
        }
        mCacheIsDirty = false;
    }

    private void refreshLocalDataSource(List<EnvironmentalSensor> environmentalSensors) {
        mEnvironmentalSensorsLocalDataSource.deleteAllEnvironmentalSensors();
        for (EnvironmentalSensor environmentalSensor : environmentalSensors) {
            mEnvironmentalSensorsLocalDataSource.saveEnvironmentalSensor(environmentalSensor);
        }
    }

    @Nullable
    private EnvironmentalSensor getEnvironmentalSensorWithId(@NonNull UUID id) {
        checkNotNull(id);
        if (mCachedEnvironmentalSensors == null || mCachedEnvironmentalSensors.isEmpty()) {
            return null;
        } else {
            return mCachedEnvironmentalSensors.get(id);
        }
    }

}
