package com.igrow.android.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.bluetooth.le.BluetoothLeScanner;
import android.content.Context;

import java.util.Set;
import java.util.UUID;

public class BluetoothAdapterProxy implements BluetoothAdapter {

    private android.bluetooth.BluetoothAdapter mBluetoothAdapter;

    public BluetoothAdapterProxy(android.bluetooth.BluetoothAdapter bluetoothAdapter) {

    }

    @Override
    public boolean cancelDiscovery() {
        return false;
    }

    @Override
    public void closeProfileProxy(int profile, BluetoothProfile proxy) {

    }

    @Override
    public boolean disable() {
        return false;
    }

    @Override
    public boolean enable() {
        return false;
    }

    @Override
    public String getAddress() {
        return null;
    }

    @Override
    public BluetoothLeAdvertiser getBluetoothLeAdvertiser() {
        return null;
    }

    @Override
    public BluetoothLeScanner getBluetoothLeScanner() {
        return null;
    }

    @Override
    public Set<BluetoothDevice> getBondedDevices() {
        return null;
    }

    @Override
    public int getLeMaximumAdvertisingDataLength() {
        return 0;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public int getProfileConnectionState(int profile) {
        return 0;
    }

    @Override
    public boolean getProfileProxy(Context context, BluetoothProfile.ServiceListener listener, int profile) {
        return false;
    }

    @Override
    public BluetoothDevice getRemoteDevice(byte[] address) {
        return null;
    }

    @Override
    public BluetoothDevice getRemoteDevice(String address) {
        return null;
    }

    @Override
    public int getScanMode() {
        return 0;
    }

    @Override
    public int getState() {
        return 0;
    }

    @Override
    public boolean isDiscovering() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    @Override
    public boolean isLe2MPhySupported() {
        return false;
    }

    @Override
    public boolean isLeCodedPhySupported() {
        return false;
    }

    @Override
    public boolean isLeExtendedAdvertisingSupported() {
        return false;
    }

    @Override
    public boolean isLePeriodicAdvertisingSupported() {
        return false;
    }

    @Override
    public boolean isMultipleAdvertisementSupported() {
        return false;
    }

    @Override
    public boolean isOffloadedFilteringSupported() {
        return false;
    }

    @Override
    public boolean isOffloadedScanBatchingSupported() {
        return false;
    }

    @Override
    public BluetoothServerSocket listenUsingInsecureRfcommWithServiceRecord(String name, UUID uuid) {
        return null;
    }

    @Override
    public BluetoothServerSocket listenUsingRfcommWithServiceRecord(String name, UUID uuid) {
        return null;
    }

    @Override
    public boolean setName(String name) {
        return false;
    }

    @Override
    public boolean startDiscovery() {
        return false;
    }

    @Override
    public boolean startLeScan(UUID[] serviceUuids, android.bluetooth.BluetoothAdapter.LeScanCallback callback) {
        return false;
    }

    @Override
    public boolean startLeScan(android.bluetooth.BluetoothAdapter.LeScanCallback callback) {
        return false;
    }

    @Override
    public void stopLeScan(android.bluetooth.BluetoothAdapter.LeScanCallback callback) {

    }
}
