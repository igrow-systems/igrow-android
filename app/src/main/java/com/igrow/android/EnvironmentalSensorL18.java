package com.igrow.android;

import android.bluetooth.BluetoothDevice;

/**
 * Created by jsr on 15/04/2017.
 */

public class EnvironmentalSensorL18 extends EnvironmentalSensor {

    @Override
    public BluetoothDevice getBluetoothDevice() {
        return null;
    }

    @Override
    public int getRSSI() {
        return 0;
    }

    @Override
    public int getTimestamp() {
        return 0;
    }
}
