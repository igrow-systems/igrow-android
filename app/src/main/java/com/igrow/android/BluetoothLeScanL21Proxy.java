package com.igrow.android;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.os.Build;

import java.util.ArrayList;
import java.util.List;

import static android.bluetooth.le.ScanSettings.SCAN_MODE_LOW_LATENCY;

/**
 * Created by jsr on 14/04/2017.
 */

public class BluetoothLeScanL21Proxy implements BluetoothLeScanProxy {

    private BluetoothLeScanner mBluetoothLeScanner;

    // Device scan callback
    private ScanCallback mLeScanCallback;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public BluetoothLeScanL21Proxy(BluetoothAdapter bluetoothAdapter) {

        mBluetoothLeScanner = bluetoothAdapter.getBluetoothLeScanner();

        mLeScanCallback = new ScanCallback() {

            @Override
            public void onScanResult(int callbackType, ScanResult result) {
                super.onScanResult(callbackType, result);
            }

            @Override
            public void onBatchScanResults(List<ScanResult> results) {
                super.onBatchScanResults(results);
            }

            @Override
            public void onScanFailed(int errorCode) {
                super.onScanFailed(errorCode);
            }
        };
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void startLeScan() {
        List<ScanFilter> filters = new ArrayList<ScanFilter>();
        ScanFilter filter = new ScanFilter.Builder().build();
        filters.add(filter);

        ScanSettings settings = new ScanSettings.Builder().setScanMode(SCAN_MODE_LOW_LATENCY)
                .build();

        mBluetoothLeScanner.startScan(filters, settings, mLeScanCallback);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void stopLeScan() {
        mBluetoothLeScanner.stopScan(mLeScanCallback);
    }
}
