package com.igrow.android.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattServer;
import android.bluetooth.BluetoothGattServerCallback;
import android.content.Context;

import java.util.List;

public interface BluetoothManager {

    BluetoothAdapter	getAdapter();

    List<BluetoothDevice> getConnectedDevices(int profile);

    int	getConnectionState(BluetoothDevice device, int profile);

    List<BluetoothDevice>	getDevicesMatchingConnectionStates(int profile, int[] states);

    BluetoothGattServer openGattServer(Context context, BluetoothGattServerCallback callback);
}
