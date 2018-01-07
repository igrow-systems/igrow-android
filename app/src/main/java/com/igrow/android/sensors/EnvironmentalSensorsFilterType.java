package com.igrow.android.sensors;

/**
 * Created by jsr on 7/01/18.
 */

public enum EnvironmentalSensorsFilterType {
        /**
         * Do not filter tasks.
         */
        ALL_SENSORS,

        /**
         * Filters only the active (not completed yet) tasks.
         */
        ACTIVE_SENSORS,

        /**
         * Filters only the completed tasks.
         */
        COMPLETED_SENSORS
}
