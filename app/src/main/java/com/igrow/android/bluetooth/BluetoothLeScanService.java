package com.igrow.android.bluetooth;

import android.content.ComponentCallbacks2;

/**
 * Created by jsr on 6/02/18.
 */

public interface BluetoothLeScanService {
    String ACTION_SCAN_UPDATE =
            "com.igrow.android.ACTION_SCAN_UPDATE";
    String ERROR_NOT_INITIALIZED =
                    "com.igrow.android.ERROR_NOT_INITIALIZED";
    String EXTRA_UPDATE_PARCELABLE =
                            "com.igrow.android.EXTRA_UPDATE_PARCELABLE";
    String ACTION_ALARM_PERIOD =
                                    "com.igrow.android.ACTION_ALARM_PERIOD";

    void startScan();

    void stopScan();

    void close();

    boolean isInitialized();
}
