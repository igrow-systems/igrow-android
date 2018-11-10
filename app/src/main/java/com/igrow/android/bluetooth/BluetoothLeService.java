package com.igrow.android.bluetooth;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;

import java.util.List;

interface BluetoothLeService {

    String ERROR_NOT_INITIALIZED =
            "com.igrow.android.ERROR_NOT_INITIALIZED";
    String ACTION_GATT_CONNECTED =
            "com.example.bluetooth.le.ACTION_GATT_CONNECTED";
    String ACTION_GATT_DISCONNECTED =
            "com.example.bluetooth.le.ACTION_GATT_DISCONNECTED";
    String ACTION_GATT_SERVICES_DISCOVERED =
            "com.example.bluetooth.le.ACTION_GATT_SERVICES_DISCOVERED";
    String ACTION_DATA_AVAILABLE =
            "com.example.bluetooth.le.ACTION_DATA_AVAILABLE";
    String EXTRA_DATA =
            "com.example.bluetooth.le.EXTRA_DATA";

    boolean initialize();

    boolean connect(String address);

    void disconnect();

    void close();

    void readCharacteristic(BluetoothGattCharacteristic characteristic);

    void setCharacteristicNotification(BluetoothGattCharacteristic characteristic,
                                       boolean enabled);

    List<BluetoothGattService> getSupportedGattServices();
}
