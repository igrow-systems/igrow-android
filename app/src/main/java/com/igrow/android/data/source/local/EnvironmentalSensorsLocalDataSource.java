package com.igrow.android.data.source.local;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.igrow.android.data.EnvironmentalSensor;
import com.igrow.android.data.source.EnvironmentalSensorsDataSource;
import com.igrow.android.util.AppExecutors;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by jsr on 2/01/18.
 */

public class EnvironmentalSensorsLocalDataSource implements EnvironmentalSensorsDataSource {

    private static volatile EnvironmentalSensorsLocalDataSource INSTANCE;

    private EnvironmentalSensorsDao mEnvironmentalSensorsDao;

    private AppExecutors mAppExecutors;

    // Prevent direct instantiation.
    private EnvironmentalSensorsLocalDataSource(@NonNull AppExecutors appExecutors,
                                                @NonNull EnvironmentalSensorsDao EnvironmentalSensorsDao) {
        mAppExecutors = appExecutors;
        mEnvironmentalSensorsDao = EnvironmentalSensorsDao;
    }

    public static EnvironmentalSensorsLocalDataSource getInstance(@NonNull AppExecutors appExecutors,
                                                                  @NonNull EnvironmentalSensorsDao EnvironmentalSensorsDao) {
        if (INSTANCE == null) {
            synchronized (EnvironmentalSensorsLocalDataSource.class) {
                if (INSTANCE == null) {
                    INSTANCE = new EnvironmentalSensorsLocalDataSource(appExecutors, EnvironmentalSensorsDao);
                }
            }
        }
        return INSTANCE;
    }

    /**
     * Note: {@link LoadEnvironmentalSensorsCallback#onDataNotAvailable()} is fired if the database doesn't exist
     * or the table is empty.
     */
    @Override
    public void getEnvironmentalSensors(@NonNull final LoadEnvironmentalSensorsCallback callback) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final List<EnvironmentalSensor> EnvironmentalSensors = mEnvironmentalSensorsDao.getEnvironmentalSensors();
                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (EnvironmentalSensors.isEmpty()) {
                            // This will be called if the table is new or just empty.
                            callback.onDataNotAvailable();
                        } else {
                            callback.onEnvironmentalSensorsLoaded(environmentalSensors);
                        }
                    }
                });
            }
        };

        mAppExecutors.diskIO().execute(runnable);
    }

    /**
     * Note: {@link GetEnvironmentalSensorCallback#onDataNotAvailable()} is fired if the {@link EnvironmentalSensor} isn't
     * found.
     */
    @Override
    public void getEnvironmentalSensor(@NonNull final String environmentalSensorId, @NonNull final GetEnvironmentalSensorCallback callback) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final EnvironmentalSensor environmentalSensor = mEnvironmentalSensorsDao.getEnvironmentalSensorById(environmentalSensorId);

                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (environmentalSensor != null) {
                            callback.onEnvironmentalSensorLoaded(environmentalSensor);
                        } else {
                            callback.onDataNotAvailable();
                        }
                    }
                });
            }
        };

        mAppExecutors.diskIO().execute(runnable);
    }

    @Override
    public void saveEnvironmentalSensor(@NonNull final EnvironmentalSensor environmentalSensor) {
        checkNotNull(environmentalSensor);
        Runnable saveRunnable = new Runnable() {
            @Override
            public void run() {
                mEnvironmentalSensorsDao.insertEnvironmentalSensor(environmentalSensor);
            }
        };
        mAppExecutors.diskIO().execute(saveRunnable);
    }

    @Override
    public void completeEnvironmentalSensor(@NonNull final EnvironmentalSensor environmentalSensor) {
        Runnable completeRunnable = new Runnable() {
            @Override
            public void run() {
                mEnvironmentalSensorsDao.updateCompleted(environmentalSensor.getId(), true);
            }
        };

        mAppExecutors.diskIO().execute(completeRunnable);
    }

    @Override
    public void completeEnvironmentalSensor(@NonNull String environmentalSensorId) {
        // Not required for the local data source because the {@link EnvironmentalSensorsRepository} handles
        // converting from a {@code environmentalSensorId} to a {@link environmentalSensor} using its cached data.
    }

    @Override
    public void activateEnvironmentalSensor(@NonNull final EnvironmentalSensor environmentalSensor) {
        Runnable activateRunnable = new Runnable() {
            @Override
            public void run() {
                mEnvironmentalSensorsDao.updateCompleted(environmentalSensor.getId(), false);
            }
        };
        mAppExecutors.diskIO().execute(activateRunnable);
    }

    @Override
    public void activateEnvironmentalSensor(@NonNull String environmentalSensorId) {
        // Not required for the local data source because the {@link EnvironmentalSensorsRepository} handles
        // converting from a {@code environmentalSensorId} to a {@link environmentalSensor} using its cached data.
    }

    @Override
    public void clearCompletedEnvironmentalSensors() {
        Runnable clearEnvironmentalSensorsRunnable = new Runnable() {
            @Override
            public void run() {
                mEnvironmentalSensorsDao.deleteCompletedEnvironmentalSensors();

            }
        };

        mAppExecutors.diskIO().execute(clearEnvironmentalSensorsRunnable);
    }

    @Override
    public void refreshEnvironmentalSensors() {
        // Not required because the {@link EnvironmentalSensorsRepository} handles the logic of refreshing the
        // EnvironmentalSensors from all the available data sources.
    }

    @Override
    public void deleteAllEnvironmentalSensors() {
        Runnable deleteRunnable = new Runnable() {
            @Override
            public void run() {
                mEnvironmentalSensorsDao.deleteEnvironmentalSensors();
            }
        };

        mAppExecutors.diskIO().execute(deleteRunnable);
    }

    @Override
    public void deleteEnvironmentalSensor(@NonNull final String environmentalSensorId) {
        Runnable deleteRunnable = new Runnable() {
            @Override
            public void run() {
                mEnvironmentalSensorsDao.deleteEnvironmentalSensorById(environmentalSensorId);
            }
        };

        mAppExecutors.diskIO().execute(deleteRunnable);
    }

    @VisibleForTesting
    static void clearInstance() {
        INSTANCE = null;
    }
}
