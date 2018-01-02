package com.igrow.android.bluetooth;

/**
 * Created by jsr on 14/04/2017.
 */

public abstract class BluetoothLeScanProxy {

    protected OnUpdateCallback mCallback;

    interface OnUpdateCallback {
        void onUpdate(EnvironmentalSensorBLEScanUpdate sensorScanUpdate);
    }

    abstract void startLeScan();

    abstract void stopLeScan();

    void setOnUpdateCallback(OnUpdateCallback callback) {
        mCallback = callback;
    }

}
