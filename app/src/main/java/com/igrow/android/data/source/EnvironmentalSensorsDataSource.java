package com.igrow.android.data.source;

import android.support.annotation.NonNull;

import com.igrow.android.data.EnvironmentalSensor;

import java.util.List;
import java.util.UUID;

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

    void getEnvironmentalSensor(@NonNull UUID sensorId, @NonNull GetEnvironmentalSensorCallback callback);

    void getEnvironmentalSensor(@NonNull String address, @NonNull GetEnvironmentalSensorCallback callback);

    void saveEnvironmentalSensor(@NonNull EnvironmentalSensor environmentalSensor);

    void refreshEnvironmentalSensors();

    void deleteAllEnvironmentalSensors();

    void deleteEnvironmentalSensor(@NonNull UUID environmentalSensorId);
}
