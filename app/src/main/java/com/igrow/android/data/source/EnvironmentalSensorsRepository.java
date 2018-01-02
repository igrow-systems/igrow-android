package com.igrow.android.data.source;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.igrow.android.data.source.local.EnvironmentalSensorsDao;
import com.igrow.android.data.EnvironmentalSensor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

import javax.inject.Singleton;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by jsr on 2/01/18.
 */

@Singleton
public class EnvironmentalSensorsRepository {

    private volatile static EnvironmentalSensorsRepository INSTANCE = null;

    private final EnvironmentalSensorsDataSource mEnvironmentalSensorsRemoteDataSource;

    private final EnvironmentalSensorsDataSource mEnvironmentalSensorsLocalDataSource;

    /**
     * This variable has package local visibility so it can be accessed from tests.
     */
    Map<String, Task> mCachedEnvironmentalSensors;

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
    public void getEnvironmentalSensors(@NonNull final LoadEnvironmentalSensorsCallback callback) {
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
            mEnvironmentalSensorsLocalDataSource.getEnvironmentalSensors(new LoadEnvironmentalSensorsCallback() {
                @Override
                public void onEnvironmentalSensorsLoaded(List<Task> environmentalSensors) {
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
    public void saveTask(@NonNull Task task) {
        checkNotNull(task);
        mEnvironmentalSensorsRemoteDataSource.saveTask(task);
        mEnvironmentalSensorsLocalDataSource.saveTask(task);

        // Do in memory cache update to keep the app UI up to date
        if (mCachedEnvironmentalSensors == null) {
            mCachedEnvironmentalSensors = new LinkedHashMap<>();
        }
        mCachedEnvironmentalSensors.put(task.getId(), task);
    }

    @Override
    public void completeTask(@NonNull Task task) {
        checkNotNull(task);
        mEnvironmentalSensorsRemoteDataSource.completeTask(task);
        mEnvironmentalSensorsLocalDataSource.completeTask(task);

        Task completedTask = new Task(task.getTitle(), task.getDescription(), task.getId(), true);

        // Do in memory cache update to keep the app UI up to date
        if (mCachedEnvironmentalSensors == null) {
            mCachedEnvironmentalSensors = new LinkedHashMap<>();
        }
        mCachedEnvironmentalSensors.put(task.getId(), completedTask);
    }

    @Override
    public void completeTask(@NonNull String taskId) {
        checkNotNull(taskId);
        completeTask(getTaskWithId(taskId));
    }

    @Override
    public void activateTask(@NonNull Task task) {
        checkNotNull(task);
        mEnvironmentalSensorsRemoteDataSource.activateTask(task);
        mEnvironmentalSensorsLocalDataSource.activateTask(task);

        Task activeTask = new Task(task.getTitle(), task.getDescription(), task.getId());

        // Do in memory cache update to keep the app UI up to date
        if (mCachedEnvironmentalSensors == null) {
            mCachedEnvironmentalSensors = new LinkedHashMap<>();
        }
        mCachedEnvironmentalSensors.put(task.getId(), activeTask);
    }

    @Override
    public void activateTask(@NonNull String taskId) {
        checkNotNull(taskId);
        activateTask(getTaskWithId(taskId));
    }

    @Override
    public void clearCompletedEnvironmentalSensors() {
        mEnvironmentalSensorsRemoteDataSource.clearCompletedEnvironmentalSensors();
        mEnvironmentalSensorsLocalDataSource.clearCompletedEnvironmentalSensors();

        // Do in memory cache update to keep the app UI up to date
        if (mCachedEnvironmentalSensors == null) {
            mCachedEnvironmentalSensors = new LinkedHashMap<>();
        }
        Iterator<Map.Entry<String, Task>> it = mCachedEnvironmentalSensors.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Task> entry = it.next();
            if (entry.getValue().isCompleted()) {
                it.remove();
            }
        }
    }

    /**
     * Gets environmentalSensors from local data source (sqlite) unless the table is new or empty. In that case it
     * uses the network data source. This is done to simplify the sample.
     * <p>
     * Note: {@link GetTaskCallback#onDataNotAvailable()} is fired if both data sources fail to
     * get the data.
     */
    @Override
    public void getTask(@NonNull final String taskId, @NonNull final GetTaskCallback callback) {
        checkNotNull(taskId);
        checkNotNull(callback);

        Task cachedTask = getTaskWithId(taskId);

        // Respond immediately with cache if available
        if (cachedTask != null) {
            callback.onTaskLoaded(cachedTask);
            return;
        }

        EspressoIdlingResource.increment(); // App is busy until further notice

        // Load from server/persisted if needed.

        // Is the task in the local data source? If not, query the network.
        mEnvironmentalSensorsLocalDataSource.getTask(taskId, new GetTaskCallback() {
            @Override
            public void onTaskLoaded(Task task) {
                // Do in memory cache update to keep the app UI up to date
                if (mCachedEnvironmentalSensors == null) {
                    mCachedEnvironmentalSensors = new LinkedHashMap<>();
                }
                mCachedEnvironmentalSensors.put(task.getId(), task);

                EspressoIdlingResource.decrement(); // Set app as idle.

                callback.onTaskLoaded(task);
            }

            @Override
            public void onDataNotAvailable() {
                mEnvironmentalSensorsRemoteDataSource.getTask(taskId, new GetTaskCallback() {
                    @Override
                    public void onTaskLoaded(Task task) {
                        if (task == null) {
                            onDataNotAvailable();
                            return;
                        }
                        // Do in memory cache update to keep the app UI up to date
                        if (mCachedEnvironmentalSensors == null) {
                            mCachedEnvironmentalSensors = new LinkedHashMap<>();
                        }
                        mCachedEnvironmentalSensors.put(task.getId(), task);
                        EspressoIdlingResource.decrement(); // Set app as idle.

                        callback.onTaskLoaded(task);
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
    public void deleteTask(@NonNull String taskId) {
        mEnvironmentalSensorsRemoteDataSource.deleteTask(checkNotNull(taskId));
        mEnvironmentalSensorsLocalDataSource.deleteTask(checkNotNull(taskId));

        mCachedEnvironmentalSensors.remove(taskId);
    }

    private void getEnvironmentalSensorsFromRemoteDataSource(@NonNull final LoadEnvironmentalSensorsCallback callback) {
        mEnvironmentalSensorsRemoteDataSource.getEnvironmentalSensors(new LoadEnvironmentalSensorsCallback() {
            @Override
            public void onEnvironmentalSensorsLoaded(List<Task> environmentalSensors) {
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

    private void refreshCache(List<Task> environmentalSensors) {
        if (mCachedEnvironmentalSensors == null) {
            mCachedEnvironmentalSensors = new LinkedHashMap<>();
        }
        mCachedEnvironmentalSensors.clear();
        for (Task task : environmentalSensors) {
            mCachedEnvironmentalSensors.put(task.getId(), task);
        }
        mCacheIsDirty = false;
    }

    private void refreshLocalDataSource(List<Task> environmentalSensors) {
        mEnvironmentalSensorsLocalDataSource.deleteAllEnvironmentalSensors();
        for (Task task : environmentalSensors) {
            mEnvironmentalSensorsLocalDataSource.saveTask(task);
        }
    }

    @Nullable
    private Task getTaskWithId(@NonNull String id) {
        checkNotNull(id);
        if (mCachedEnvironmentalSensors == null || mCachedEnvironmentalSensors.isEmpty()) {
            return null;
        } else {
            return mCachedEnvironmentalSensors.get(id);
        }
    }

    //private Webservice webservice;

    private final EnvironmentalSensorsDao mEnvironmentalSensorsDao;

    private final Executor mExecutor;

    public EnvironmentalSensorsRepository(EnvironmentalSensorsDao environmentalSensorsDao,
                                          Executor executor) {

        this.mEnvironmentalSensorsDao = environmentalSensorsDao;
        this.mExecutor = executor;
    }

    // ...
    public LiveData<List<EnvironmentalSensor>> getEnvironmentalSensorList(String userId) {

        refreshEnvironmentalSensorList(userId);
        // return a LiveData directly from the database.
        final LiveData<EnvironmentalSensor> sensor = this.mEnvironmentalSensorsDao.load(userId);
        return new LiveData<List<EnvironmentalSensor>> () {{ Arrays.asList(sensor); }};
    }

    private void refreshEnvironmentalSensorList(final String userId) {
        this.mExecutor.execute(() -> {
            // running in a background thread
            // check if user was fetched recently
            boolean userExists = this.mEnvironmentalSensorsDao.(FRESH_TIMEOUT);
            if (!userExists) {
                // refresh the data
                //Response response = webservice.getUser(userId).execute();
                // TODO check for error etc.
                // Update the database.The LiveData will automatically refresh so
                // we don't need to do anything else here besides updating the database
                //userDao.save(response.body());
            }
        });

}
