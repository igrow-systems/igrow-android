package com.igrow.android.data.source;

import android.support.annotation.NonNull;

import com.igrow.android.data.EnvironmentalSensor;

import java.util.List;

/**
 * Created by jsr on 2/01/18.
 */

public interface EnvironmentalSensorsDataSource {

    interface LoadEnvironmentalSensorsCallback {

        void onEnvironmentalSensorsLoaded(List<EnvironmentalSensor> environmentalSensors);

        void onDataNotAvailable();
    }

    interface GetEnvironmentalSensorCallback {

        void onEnvironmentalSensorLoaded(EnvironmentalSensor environmentalSensor);

        void onDataNotAvailable();
    }

    void getEnvironmentalSensors(@NonNull LoadEnvironmentalSensorsCallback callback);

    void getEnvironmentalSensor(@NonNull String environmentalSensorId, @NonNull GetEnvironmentalSensorCallback callback);

    void saveEnvironmentalSensor(@NonNull EnvironmentalSensor environmentalSensor);

    void completeEnvironmentalSensor(@NonNull EnvironmentalSensor environmentalSensor);

    void completeEnvironmentalSensor(@NonNull String environmentalSensorId);

    void activateEnvironmentalSensor(@NonNull EnvironmentalSensor environmentalSensor);

    void activateEnvironmentalSensor(@NonNull String environmentalSensorId);

    void clearCompletedEnvironmentalSensors();

    void refreshEnvironmentalSensors();

    void deleteAllEnvironmentalSensors();

    void deleteEnvironmentalSensor(@NonNull String environmentalSensorId);
}
