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

    void refreshEnvironmentalSensors();

    void deleteAllEnvironmentalSensors();

    void deleteEnvironmentalSensor(@NonNull String environmentalSensorId);
}
