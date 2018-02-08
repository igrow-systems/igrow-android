package com.igrow.android.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.bluetooth.le.BluetoothLeScanner;
import android.content.Context;

import java.util.Set;
import java.util.UUID;

public interface BluetoothAdapter {

    boolean	cancelDiscovery();

    static boolean	checkBluetoothAddress(String address) {};

    void	closeProfileProxy(int profile, BluetoothProfile proxy);

    boolean	disable();

    boolean	enable();

    String	getAddress();

    BluetoothLeAdvertiser getBluetoothLeAdvertiser();

    BluetoothLeScanner getBluetoothLeScanner();

    Set<BluetoothDevice> getBondedDevices();

    static BluetoothAdapter	getDefaultAdapter() {};

    int	getLeMaximumAdvertisingDataLength();

    String	getName();

    int	getProfileConnectionState(int profile);

    boolean	getProfileProxy(Context context, BluetoothProfile.ServiceListener listener, int profile);

    BluetoothDevice	getRemoteDevice(byte[] address);

    BluetoothDevice	getRemoteDevice(String address);

    int	getScanMode();

    int	getState();

    boolean	isDiscovering();

    boolean	isEnabled();

    boolean	isLe2MPhySupported();

    boolean	isLeCodedPhySupported();

    boolean	isLeExtendedAdvertisingSupported();

    boolean	isLePeriodicAdvertisingSupported();

    boolean	isMultipleAdvertisementSupported();

    boolean	isOffloadedFilteringSupported();

    boolean	isOffloadedScanBatchingSupported();

    BluetoothServerSocket listenUsingInsecureRfcommWithServiceRecord(String name, UUID uuid);

    BluetoothServerSocket	listenUsingRfcommWithServiceRecord(String name, UUID uuid);

    boolean	setName(String name);

    boolean	startDiscovery();

    boolean	startLeScan(UUID[] serviceUuids, android.bluetooth.BluetoothAdapter.LeScanCallback callback);

    boolean	startLeScan(android.bluetooth.BluetoothAdapter.LeScanCallback callback);

    void	stopLeScan(android.bluetooth.BluetoothAdapter.LeScanCallback callback);
}
