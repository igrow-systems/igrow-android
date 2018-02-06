package com.igrow.android.bluetooth;

/**
 * Created by jsr on 6/02/18.
 */

import android.os.Binder;

/**
 * Class used for the client Binder.  Because we know this service always
 * runs in the same process as its clients, we don't need to deal with IPC.
 */
public class LocalBinder extends Binder {

    BluetoothLeScanService mBluetoothLeScanService;

    LocalBinder(BluetoothLeScanService bluetoothLeScanService) {
        mBluetoothLeScanService = bluetoothLeScanService;
    }

    public BluetoothLeScanService getService() {
        // Return this instance of LocalService so clients can call public methods
        return mBluetoothLeScanService;
    }
}
