/*
 * Copyright (C) 2013 The Android Open Source Project
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

package com.igrow.android;

import java.util.HashMap;
import java.util.UUID;

/**
 * This class includes a small subset of standard GATT attributes for demonstration purposes.
 */
public class IGrowGattAttributes {

    private static HashMap<String, String> attributes = new HashMap<String, String>();

    public static String CLIENT_CHARACTERISTIC_CONFIG = "00002902-0000-1000-8000-00805f9b34fb";

    public static String APPEARANCE                   = "00002a01-0000-1000-8000-00805f9b34fb";

    // Environmental Sensing
    public static String TEMPERATURE                  = "00002a6e-0000-1000-8000-00805f9b34fb";
    public static String RAINFALL                     = "00002a78-0000-1000-8000-00805f9b34fb";
    public static String HUMIDITY                     = "00002a6f-0000-1000-8000-00805f9b34fb";
    public static String IRRADIANCE                   = "00002a77-0000-1000-8000-00805f9b34fb";
    public static String DESCRIPTOR_VALUE_CHANGED     = "00002a7d-0000-1000-8000-00805f9b34fb";

    public static UUID TEMPERATURE_UUID              = UUID.fromString(TEMPERATURE);
    public static UUID RAINFALL_UUID                 = UUID.fromString(RAINFALL);
    public static UUID HUMIDITY_UUID                 = UUID.fromString(HUMIDITY);
    public static UUID IRRADIANCE_UUID               = UUID.fromString(IRRADIANCE);
    public static UUID DESCRIPTOR_VALUE_CHANGED_UUID = UUID.fromString(DESCRIPTOR_VALUE_CHANGED);


    // Battery Service
    public static String BATTERY_LEVEL                = "00002a19-0000-1000-8000-00805f9b34fb";

    // Immediate Alert Service
    public static String ALERT_LEVEL                  = "00002a06-0000-1000-8000-00805f9b34fb";
    public static String ALERT_STATUS                 = "00002a3f-0000-1000-8000-00805f9b34fb";

    // Device Information Service
    public static String MANUFACTURER_NAME_STRING     = "00002a29-0000-1000-8000-00805f9b34fb";
    public static String MODEL_NUMBER_STRING          = "00002a24-0000-1000-8000-00805f9b34fb";

    static {
        // Services
        attributes.put(IGrowGattServices.GENERIC_ACCESS, "Generic Access Service");
        attributes.put(IGrowGattServices.GENERIC_ATTRIBUTE, "Generic Attribute Service");
        attributes.put(IGrowGattServices.DEVICE_INFORMATION, "Device Information Service");
        attributes.put(IGrowGattServices.IMMEDIATE_ALERT, "Immediate Alert Service");
        attributes.put(IGrowGattServices.BATTERY, "Battery Service");
        attributes.put(IGrowGattServices.ENVIRONMENTAL_SENSING, "Environmental Sensing Service");

        // Characteristics

        attributes.put(TEMPERATURE, "Temperature");
        attributes.put(RAINFALL, "Rainfall");
        attributes.put(HUMIDITY, "Humidity");
        attributes.put(IRRADIANCE, "Irradiance");
        attributes.put(DESCRIPTOR_VALUE_CHANGED, "Descriptor Value Changed");

        attributes.put(BATTERY_LEVEL, "Battery Level");

        attributes.put(ALERT_LEVEL, "Alert Level");
        attributes.put(ALERT_STATUS, "Alert Status");

        attributes.put(MANUFACTURER_NAME_STRING, "Manufacturer Name String");
        attributes.put(MODEL_NUMBER_STRING, "Model Number String");
    }

    public static String lookup(String uuid, String defaultName) {
        String name = attributes.get(uuid);
        return name == null ? defaultName : name;
    }
}
