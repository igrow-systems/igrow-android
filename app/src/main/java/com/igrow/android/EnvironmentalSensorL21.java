package com.igrow.android;

import android.bluetooth.le.ScanResult;

/**
 * Created by jsr on 15/04/2017.
 */

public class EnvironmentalSensorL21 {

    private ScanResult mScanResult;

    public ScanResult getScanResult() {
        return mScanResult;
    }

    public void setScanRecord(ScanResult scanResult) {
        mScanResult = scanResult;
    }



}
