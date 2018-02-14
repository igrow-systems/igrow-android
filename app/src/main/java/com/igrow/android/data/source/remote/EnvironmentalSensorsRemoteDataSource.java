/*
 * Copyright 2016, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.igrow.android.data.source.remote;

import android.os.Handler;
import android.support.annotation.NonNull;

import com.google.common.collect.Lists;
import com.igrow.android.data.EnvironmentalSensor;
import com.igrow.android.data.source.EnvironmentalSensorsDataSource;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Implementation of the data source that adds a latency simulating network.
 */
public class EnvironmentalSensorsRemoteDataSource implements EnvironmentalSensorsDataSource {

    private static EnvironmentalSensorsRemoteDataSource INSTANCE;

    private static final int SERVICE_LATENCY_IN_MILLIS = 2000;

    private final static Map<UUID, EnvironmentalSensor> ENVIRONMENTAL_SENSORS_SERVICE_DATA;

    static {
        ENVIRONMENTAL_SENSORS_SERVICE_DATA = new LinkedHashMap<>(2);
        addEnvironmentalSensor("0Build tower in Pisa", "Ground looks good, no foundation work required.");
        addEnvironmentalSensor("1Finish bridge in Tacoma", "Found awesome girders at half the cost!");
        addEnvironmentalSensor("2Finish bridge in Tacoma", "Found awesome girders at half the cost!");
        addEnvironmentalSensor("3Finish bridge in Tacoma", "Found awesome girders at half the cost!");
        addEnvironmentalSensor("4Finish bridge in Tacoma", "Found awesome girders at half the cost!");
        addEnvironmentalSensor("5Finish bridge in Tacoma", "Found awesome girders at half the cost!");
        addEnvironmentalSensor("6Finish bridge in Tacoma", "Found awesome girders at half the cost!");
        addEnvironmentalSensor("7Finish bridge in Tacoma", "Found awesome girders at half the cost!");
        addEnvironmentalSensor("8Finish bridge in Tacoma", "Found awesome girders at half the cost!");
        addEnvironmentalSensor("9Finish bridge in Tacoma", "Found awesome girders at half the cost!");
        addEnvironmentalSensor("10Finish bridge in Tacoma", "Found awesome girders at half the cost!");
        addEnvironmentalSensor("11Finish bridge in Tacoma", "Found awesome girders at half the cost!");
        addEnvironmentalSensor("12Finish bridge in Tacoma", "Found awesome girders at half the cost!");
        addEnvironmentalSensor("13Finish bridge in Tacoma", "Found awesome girders at half the cost!");
        addEnvironmentalSensor("14Finish bridge in Tacoma", "Found awesome girders at half the cost!");
        addEnvironmentalSensor("15Finish bridge in Tacoma", "Found awesome girders at half the cost!");
    }

    public static EnvironmentalSensorsRemoteDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new EnvironmentalSensorsRemoteDataSource();
        }
        return INSTANCE;
    }

    // Prevent direct instantiation.
    private EnvironmentalSensorsRemoteDataSource() {
    }

    private static void addEnvironmentalSensor(String address, String fullName) {
        EnvironmentalSensor newEnvironmentalSensor = new EnvironmentalSensor.EnvironmentalSensorBuilder()
                .setId(UUID.randomUUID())
                .setAddress(address)
                .setFullName(fullName)
                .build();
        ENVIRONMENTAL_SENSORS_SERVICE_DATA.put(newEnvironmentalSensor.getSensorId(), newEnvironmentalSensor);
    }

    /**
     * Note: {@link LoadEnvironmentalSensorsCallback#onDataNotAvailable()} is never fired. In a real remote data
     * source implementation, this would be fired if the server can't be contacted or the server
     * returns an error.
     */
    @Override
    public void getEnvironmentalSensors(final @NonNull LoadEnvironmentalSensorsCallback callback) {
        // Simulate network by delaying the execution.
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                callback.onEnvironmentalSensorsLoaded(Lists.newArrayList(ENVIRONMENTAL_SENSORS_SERVICE_DATA.values()));
            }
        }, SERVICE_LATENCY_IN_MILLIS);
    }

    /**
     * Note: {@link GetEnvironmentalSensorCallback#onDataNotAvailable()} is never fired. In a real remote data
     * source implementation, this would be fired if the server can't be contacted or the server
     * returns an error.
     */
    @Override
    public void getEnvironmentalSensor(@NonNull UUID sensorId, final @NonNull GetEnvironmentalSensorCallback callback) {
        final EnvironmentalSensor environmentalSensors = ENVIRONMENTAL_SENSORS_SERVICE_DATA.get(sensorId);

        // Simulate network by delaying the execution.
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                callback.onEnvironmentalSensorLoaded(environmentalSensors);
            }
        }, SERVICE_LATENCY_IN_MILLIS);
    }

    @Override
    public void saveEnvironmentalSensor(@NonNull EnvironmentalSensor environmentalSensors) {
        ENVIRONMENTAL_SENSORS_SERVICE_DATA.put(environmentalSensors.getSensorId(), environmentalSensors);
    }

    @Override
    public void refreshEnvironmentalSensors() {
        // Not required because the {@link EnvironmentalSensorsRepository} handles the logic of refreshing the
        // environmental sensors from all the available data sources.
    }

    @Override
    public void deleteAllEnvironmentalSensors() {
        ENVIRONMENTAL_SENSORS_SERVICE_DATA.clear();
    }

    @Override
    public void deleteEnvironmentalSensor(@NonNull UUID sensorId) {
        ENVIRONMENTAL_SENSORS_SERVICE_DATA.remove(sensorId);
    }
}
