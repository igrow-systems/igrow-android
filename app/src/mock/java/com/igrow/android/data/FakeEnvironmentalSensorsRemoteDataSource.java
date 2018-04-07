package com.igrow.android.data;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.google.common.collect.Lists;
import com.igrow.android.data.source.EnvironmentalSensorsDataSource;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by jsr on 3/01/18.
 */

public class FakeEnvironmentalSensorsRemoteDataSource implements EnvironmentalSensorsDataSource {

        private static FakeEnvironmentalSensorsRemoteDataSource INSTANCE;

        private static final Map<UUID, EnvironmentalSensor> SENSORS_SERVICE_DATA = new LinkedHashMap<>();

        private static final Map<String, EnvironmentalSensor> SENSORS_SERVICE_DATA_BY_ADDRESS = new LinkedHashMap<>();

        // Prevent direct instantiation.
        private FakeEnvironmentalSensorsRemoteDataSource() {}

        public static FakeEnvironmentalSensorsRemoteDataSource getInstance() {
            if (INSTANCE == null) {
                INSTANCE = new FakeEnvironmentalSensorsRemoteDataSource();
            }
            return INSTANCE;
        }

        @Override
        public void getEnvironmentalSensors(@NonNull LoadEnvironmentalSensorsCallback callback) {
            callback.onEnvironmentalSensorsLoaded(Lists.newArrayList(SENSORS_SERVICE_DATA.values()));
        }

        @Override
        public void getEnvironmentalSensor(@NonNull UUID sensorId, @NonNull GetEnvironmentalSensorCallback callback) {
            EnvironmentalSensor environmentalSensor = SENSORS_SERVICE_DATA.get(sensorId);
            callback.onEnvironmentalSensorLoaded(environmentalSensor);
        }

        @Override
        public void getEnvironmentalSensor(@NonNull String address, @NonNull GetEnvironmentalSensorCallback callback) {
            EnvironmentalSensor environmentalSensor = SENSORS_SERVICE_DATA.get(address);
            callback.onEnvironmentalSensorLoaded(environmentalSensor);
        }

        @Override
        public void saveEnvironmentalSensor(@NonNull EnvironmentalSensor environmentalSensor) {
            SENSORS_SERVICE_DATA.put(environmentalSensor.getSensorId(), environmentalSensor);
            SENSORS_SERVICE_DATA_BY_ADDRESS.put(environmentalSensor.getAddress(), environmentalSensor);
        }

        public void refreshEnvironmentalSensors() {
            // Not required because the {@link EnvironmentalSensorsRepository} handles the logic of refreshing the
            // environmental sensors from all the available data sources.
        }

        @Override
        public void deleteEnvironmentalSensor(@NonNull UUID sensorId) {
            SENSORS_SERVICE_DATA_BY_ADDRESS.remove(sensorId);
            SENSORS_SERVICE_DATA.remove(sensorId);
        }

        @Override
        public void deleteAllEnvironmentalSensors() {

            SENSORS_SERVICE_DATA_BY_ADDRESS.clear();
            SENSORS_SERVICE_DATA.clear();
        }

        @VisibleForTesting
        public void addEnvironmentalSensors(EnvironmentalSensor... environmentalSensors) {
            if (environmentalSensors != null) {
                for (EnvironmentalSensor environmentalSensor : environmentalSensors) {
                    SENSORS_SERVICE_DATA.put(environmentalSensor.getSensorId(), environmentalSensor);
                    SENSORS_SERVICE_DATA_BY_ADDRESS.put(environmentalSensor.getAddress(), environmentalSensor);
                }
            }
        }
}
