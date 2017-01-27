package com.argusat.igrow.android;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothProfile;

import java.util.List;

/**
 * Created by jsr on 14/12/2016.
 */

public class BluetoothEnvironmentalSensing implements BluetoothProfile {


    @Override
    public List<BluetoothDevice> getConnectedDevices() {
        return null;
    }

    @Override
    public List<BluetoothDevice> getDevicesMatchingConnectionStates(int[] ints) {
        return null;
    }

    @Override
    public int getConnectionState(BluetoothDevice bluetoothDevice) {
        return 0;
    }
}
