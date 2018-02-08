package com.igrow.android.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattServer;
import android.bluetooth.BluetoothGattServerCallback;
import android.content.Context;

import java.util.List;

public class BluetoothManagerProxy implements BluetoothManager {

    private android.bluetooth.BluetoothManager mBluetoothManager;

    public BluetoothManagerProxy(android.bluetooth.BluetoothManager bluetoothManager) {
        mBluetoothManager = bluetoothManager;
    }

    @Override
    public BluetoothAdapter getAdapter() {
        return new BluetoothAdapterProxy(mBluetoothManager.getAdapter());
    }

    @Override
    public List<BluetoothDevice> getConnectedDevices(int profile) {
        return mBluetoothManager.getConnectedDevices(profile);
    }

    @Override
    public int getConnectionState(BluetoothDevice device, int profile) {
        return mBluetoothManager.getConnectionState(device, profile);
    }

    @Override
    public List<BluetoothDevice> getDevicesMatchingConnectionStates(int profile, int[] states) {
        return mBluetoothManager.getDevicesMatchingConnectionStates(profile, states);
    }

    @Override
    public BluetoothGattServer openGattServer(Context context, BluetoothGattServerCallback callback) {
        return mBluetoothManager.openGattServer(context, callback);
    }
}
