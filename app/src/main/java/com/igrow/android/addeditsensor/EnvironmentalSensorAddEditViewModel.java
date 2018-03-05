package com.igrow.android.addeditsensor;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.databinding.ObservableBoolean;
import android.support.annotation.Nullable;

import com.igrow.android.R;
import com.igrow.android.SingleLiveEvent;
import com.igrow.android.SnackbarMessage;
import com.igrow.android.data.EnvironmentalSensor;
import com.igrow.android.data.source.EnvironmentalSensorsDataSource;
import com.igrow.android.data.source.EnvironmentalSensorsRepository;

import java.util.UUID;

import javax.inject.Inject;

/**
 * Created by jsr on 2/01/18.
 */

public class EnvironmentalSensorAddEditViewModel extends AndroidViewModel implements EnvironmentalSensorsDataSource.GetEnvironmentalSensorCallback {

    public final ObservableBoolean dataLoading = new ObservableBoolean(false);

    public final MutableLiveData<String> fullname = new MutableLiveData<>();

    public final MutableLiveData<String> address = new MutableLiveData<>();

    private final ObservableBoolean mIsDataLoadingError = new ObservableBoolean(false);

    private final SnackbarMessage mSnackbarText = new SnackbarMessage();

    private final SingleLiveEvent<Void> mSensorUpdatedEvent = new SingleLiveEvent<>();

    private final EnvironmentalSensorsRepository mEnvironmentalSensorsRepository;

    @Nullable
    private UUID mSensorId;

    private boolean mIsNewSensor;

    private boolean mIsDataLoaded = false;

    private final Context mContext;


    @Inject
    public EnvironmentalSensorAddEditViewModel(Application context, EnvironmentalSensorsRepository environmentalSensorsRepository) {
        super(context);
        this.mContext = context.getApplicationContext();
        this.mEnvironmentalSensorsRepository = environmentalSensorsRepository;
    }

    public void start(UUID sensorId) {

        if (dataLoading.get()) {
            // Already loading, ignore.
            return;
        }
        mSensorId = sensorId;
        if (sensorId == null) {
            // No need to populate, it's a new sensor
            mIsNewSensor = true;
            return;
        }
        if (mIsDataLoaded) {
            // No need to populate, already have data.
            return;
        }
        mIsNewSensor = false;
        dataLoading.set(true);

        mEnvironmentalSensorsRepository.getEnvironmentalSensor(sensorId, this);
    }


    @Override
    public void onEnvironmentalSensorLoaded(EnvironmentalSensor environmentalSensor) {

        fullname.setValue(environmentalSensor.getFullName());
        address.setValue(environmentalSensor.getAddress());
        //description.set(task.getDescription());
        //mTaskCompleted = task.isCompleted();
        dataLoading.set(false);
        mIsDataLoaded = true;

        // Note that there's no need to notify that the values changed because we're using
        // LiveData.
    }

    @Override
    public void onDataNotAvailable() {
        dataLoading.set(false);
    }

    // Called when clicking on fab.
    void saveSensor() {
        EnvironmentalSensor sensor = new EnvironmentalSensor.EnvironmentalSensorBuilder()
                .setAddress(address.getValue())
                .setFullName(fullname.getValue())
                .build();

        if (sensor.isEmpty()) {
            mSnackbarText.setValue(R.string.empty_sensor_message);
            return;
        }
        if (isNewSensor() || mSensorId == null) {
            createSensor(sensor);
        } else {
            sensor = new EnvironmentalSensor.EnvironmentalSensorBuilder()
                    .setId(mSensorId)
                    .setAddress(address.getValue())
                    .setFullName(fullname.getValue())
                    .build();
            updateSensor(sensor);
        }
    }

    SnackbarMessage getSnackbarMessage() {
        return mSnackbarText;
    }

    SingleLiveEvent<Void> getSensorUpdatedEvent() {
        return mSensorUpdatedEvent;
    }

    private void showSnackbarMessage(Integer message) {
        mSnackbarText.setValue(message);
    }


    private boolean isNewSensor() {
        return mIsNewSensor;
    }

    private void createSensor(EnvironmentalSensor newSensor) {
        mEnvironmentalSensorsRepository.saveEnvironmentalSensor(newSensor);
        mSensorUpdatedEvent.call();
    }

    private void updateSensor(EnvironmentalSensor sensor) {
        if (isNewSensor()) {
            throw new RuntimeException("updateTask() was called but task is new.");
        }
        mEnvironmentalSensorsRepository.saveEnvironmentalSensor(sensor);
        mSensorUpdatedEvent.call();
    }
}
