package com.igrow.android;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.util.Log;

/**
 * Created by jsr on 14/04/2017.
 */

public class BluetoothLeScanL18Proxy implements BluetoothLeScanProxy {

    private final static String TAG = BluetoothLeScanL18Proxy.class.getSimpleName();

    private BluetoothAdapter mBluetoothAdapter;
    // Device scan callback.
    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {
                @Override
                public void onLeScan(final BluetoothDevice device, int rssi,
                                     byte[] scanRecord) {

                    EnvironmentalSensor sensor = new EnvironmentalSensorL18();
                    // TODO: pass the device back out by broadcasting Intent
                    //mLeDeviceMap.put(device.getAddress(), sensor);
                    //mLeDevice.notifyDataSetChanged();
                    Log.i(TAG, String.format("RSSI: %d Found: %s", rssi, device.toString()));
                }
            };

    public BluetoothLeScanL18Proxy(BluetoothAdapter bluetoothAdapter) {
        mBluetoothAdapter = bluetoothAdapter;
    }


    @Override
    public void startLeScan() {
        mBluetoothAdapter.startLeScan(mLeScanCallback);
    }

    @Override
    public void stopLeScan() {
        mBluetoothAdapter.stopLeScan(mLeScanCallback);
    }
}
