package com.igrow.android;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by jsr on 14/12/2016.
 */

public class BluetoothLeScanService extends Service {

    private final static String TAG = BluetoothLeService.class.getSimpleName();

    private BluetoothManager mBluetoothManager;

    private BluetoothAdapter mBluetoothAdapter;

    private BluetoothLeScanProxy mBluetoothLeScanProxy;

    private BluetoothGatt mBluetoothGatt;

    // Stops scanning after 10 seconds.
    private static final long SCAN_PERIOD = 20000;

    // Scans every 5 minutes.
    private static final long SCAN_INTERVAL = 30000;

    private boolean mScanning = false;

    private AlarmManager mAlarmManager;


    private void broadcastUpdate(final String action) {
        final Intent intent = new Intent(action);
        sendBroadcast(intent);
    }

    private void broadcastUpdate(final String action,
                                 final BluetoothGattCharacteristic characteristic) {
        final Intent intent = new Intent(action);

        // This is special handling for the Environmental Sensing profile.  Data parsing is
        // carried out as per profile specifications:
        // http://developer.bluetooth.org/gatt/characteristics/Pages/CharacteristicViewer.aspx?u=org.bluetooth.characteristic.temperature.xml
        if (IGrowGattAttributes.TEMPERATURE.equals(characteristic.getUuid())) {

            int format = BluetoothGattCharacteristic.FORMAT_SINT16;
            Log.d(TAG, "Temperature format SINT16.");

            final int temperature = characteristic.getIntValue(format, 1);
            Log.d(TAG, String.format("Received temperature: %d", temperature));
            intent.putExtra(BluetoothLeService.EXTRA_DATA, String.valueOf(temperature));
        } else {
            // For all other profiles, writes the data formatted in HEX.
            final byte[] data = characteristic.getValue();
            if (data != null && data.length > 0) {
                final StringBuilder stringBuilder = new StringBuilder(data.length);
                for(byte byteChar : data)
                    stringBuilder.append(String.format("%02X ", byteChar));
                intent.putExtra(BluetoothLeService.EXTRA_DATA, new String(data) + "\n" + stringBuilder.toString());
            }
        }
        sendBroadcast(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        initialize();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        start();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }

    /**
     * Initializes a reference to the local Bluetooth adapter.
     *
     * @return Return true if the initialization is successful.
     */
    public boolean initialize() {
        // For API level 18 and above, get a reference to BluetoothAdapter through
        // BluetoothManager.
        if (mBluetoothManager == null) {
            mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
            if (mBluetoothManager == null) {
                Log.e(TAG, "Unable to initialize BluetoothManager.");
                return false;
            }
        }

        mBluetoothAdapter = mBluetoothManager.getAdapter();
        if (mBluetoothAdapter == null) {
            Log.e(TAG, "Unable to obtain a BluetoothAdapter.");
            return false;
        }

        mAlarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if (mAlarmManager == null) {
            Log.e(TAG, "Unable to obtain a AlarmManager.");
            return false;
        }

        return true;
    }

    public void start() {
        // Stops scanning after a pre-defined scan period.

        Intent alarmIntent = new Intent(BluetoothLeScanService.this,
                BluetoothLeScanService.class);
        PendingIntent pendingIntent = PendingIntent.getService(BluetoothLeScanService.this,
                1, alarmIntent, 0);
        mAlarmManager.set(AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis() + SCAN_INTERVAL, pendingIntent);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP && mBluetoothLeScanProxy == null) {
            mBluetoothLeScanProxy = new BluetoothLeScanL18Proxy(mBluetoothAdapter);
        } else if (mBluetoothLeScanProxy == null) {
            mBluetoothLeScanProxy = new BluetoothLeScanL21Proxy(mBluetoothAdapter);
        }

        mScanning = true;
        Log.d(TAG, "startLeScan()");
        mBluetoothLeScanProxy.startLeScan();
    }

    public void stop() {
        mScanning = false;
        Log.d(TAG, "stopLeScan()");
        mBluetoothLeScanProxy.stopLeScan();
    }

    /**
     * After using a given BLE device, the app must call this method to ensure resources are
     * released properly.
     */
    public void close() {
        if (mBluetoothGatt == null) {
            return;
        }
        mBluetoothGatt.close();
        mBluetoothGatt = null;
    }
}
