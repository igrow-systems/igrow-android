package com.igrow.android;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jsr on 16/04/2017.
 */

public class EnvironmentalSensorCollection {

    /**
     * An array of sample (dummy) items.
     */
    public static List<EnvironmentalSensor> ITEMS = new ArrayList<EnvironmentalSensor>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static Map<String, EnvironmentalSensor> ITEM_MAP = new HashMap<String, EnvironmentalSensor>();

    public static void addItem(EnvironmentalSensor item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.getBluetoothDevice().getAddress(), item);
    }

    public static void removeItem(EnvironmentalSensor item) {
        ITEMS.remove(item);
        ITEM_MAP.remove(item.getBluetoothDevice().getAddress());
    }

}
