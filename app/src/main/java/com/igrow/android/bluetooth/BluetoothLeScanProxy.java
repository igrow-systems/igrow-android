package com.igrow.android.bluetooth;

import android.os.Build;

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

    public static BluetoothLeScanProxy create(android.bluetooth.BluetoothAdapter bluetoothAdapter) {

        BluetoothLeScanProxy bluetoothLeScanProxy = null;

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            bluetoothLeScanProxy = new BluetoothLeScanL18Proxy(bluetoothAdapter);
        } else {
            bluetoothLeScanProxy = new BluetoothLeScanL21Proxy(bluetoothAdapter);
        }
        return bluetoothLeScanProxy;
    }
}
