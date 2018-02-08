package com.igrow.android.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by jsr on 14/04/2017.
 */

public class BluetoothLeScanL18Proxy extends BluetoothLeScanProxy {

    private final static String TAG = BluetoothLeScanL18Proxy.class.getSimpleName();

    private BluetoothAdapter mBluetoothAdapter;
    // Device scan callback.
    private android.bluetooth.BluetoothAdapter.LeScanCallback mLeScanCallback =
            new android.bluetooth.BluetoothAdapter.LeScanCallback() {
                @Override
                public void onLeScan(final BluetoothDevice device, int rssi,
                                     byte[] scanRecord) {

                    Log.d(TAG, String.format("RSSI: %d Found: %s Scan Record: %s",
                            rssi, device.toString(), scanRecord.toString()));

                    EnvironmentalSensorBLEScanUpdate sensorScanUpdate = new EnvironmentalSensorBLEScanUpdate(device.getAddress(), rssi);
                    if (mCallback != null) {
                        mCallback.onUpdate(sensorScanUpdate);
                    }
                }
            };

    public BluetoothLeScanL18Proxy(BluetoothAdapter bluetoothAdapter) {
        mBluetoothAdapter = bluetoothAdapter;
    }


    @Override
    public void startLeScan() {

        //UUID[] serviceUuids = new UUID[] { ENVIRONMENTAL_SENSING_UUID };
        //mBluetoothAdapter.startLeScan(serviceUuids, mLeScanCallback);
        mBluetoothAdapter.startLeScan(mLeScanCallback);
    }

    @Override
    public void stopLeScan() {

        mBluetoothAdapter.stopLeScan(mLeScanCallback);
    }

    private List<UUID> parseUuids(byte[] advertisedData) {
        List<UUID> uuids = new ArrayList<UUID>();

        ByteBuffer buffer = ByteBuffer.wrap(advertisedData).order(ByteOrder.LITTLE_ENDIAN);
        while (buffer.remaining() > 2) {
            byte length = buffer.get();
            if (length == 0) break;

            byte type = buffer.get();
            switch (type) {
                case 0x02: // Partial list of 16-bit UUIDs
                case 0x03: // Complete list of 16-bit UUIDs
                    while (length >= 2) {
                        uuids.add(UUID.fromString(String.format(
                                "%08x-0000-1000-8000-00805f9b34fb", buffer.getShort())));
                        length -= 2;
                    }
                    break;

                case 0x06: // Partial list of 128-bit UUIDs
                case 0x07: // Complete list of 128-bit UUIDs
                    while (length >= 16) {
                        long lsb = buffer.getLong();
                        long msb = buffer.getLong();
                        uuids.add(new UUID(msb, lsb));
                        length -= 16;
                    }
                    break;

                default:
                    buffer.position(buffer.position() + length - 1);
                    break;
            }
        }
        return uuids;
    }

}
