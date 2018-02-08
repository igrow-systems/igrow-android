package com.igrow.android.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattServer;
import android.bluetooth.BluetoothGattServerCallback;
import android.content.Context;

import java.util.List;

public class FakeBluetoothManager implements BluetoothManager {
    @Override
    public BluetoothAdapter getAdapter() {
        return null;
    }

    @Override
    public List<BluetoothDevice> getConnectedDevices(int profile) {
        return null;
    }

    @Override
    public int getConnectionState(BluetoothDevice device, int profile) {
        return 0;
    }

    @Override
    public List<BluetoothDevice> getDevicesMatchingConnectionStates(int profile, int[] states) {
        return null;
    }

    @Override
    public BluetoothGattServer openGattServer(Context context, BluetoothGattServerCallback callback) {
        return null;
    }
}
