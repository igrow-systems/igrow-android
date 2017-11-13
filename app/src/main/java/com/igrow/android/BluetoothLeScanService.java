package com.igrow.android;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by jsr on 14/12/2016.
 */

public class BluetoothLeScanService extends Service implements BluetoothLeScanProxy.OnUpdateCallback {

    private final static String TAG = BluetoothLeScanService.class.getSimpleName();

    public final static String ACTION_SCAN_UPDATE =
            "com.igrow.android.ACTION_SCAN_UPDATE";

    public final static String ACTION_ERROR_NOT_INITIALIZED =
            "com.igrow.android.ACTION_ERROR_NOT_INITIALIZED";

    public final static String EXTRA_UPDATE_PARCELABLE =
            "com.igrow.android.EXTRA_UPDATE_PARCELABLE";

    public final static String ACTION_ALARM_PERIOD =
            "com.igrow.android.ACTION_ALARM_PERIOD";

    private int REQUEST_ALARM_SCAN_PERIOD = 1;

    private int REQUEST_ALARM_SCAN_INTERVAL = 2;

    private BluetoothManager mBluetoothManager;

    private BluetoothAdapter mBluetoothAdapter;

    private BluetoothLeScanProxy mBluetoothLeScanProxy;

    private BluetoothGatt mBluetoothGatt;

    // Stops scanning after 5 seconds.
    private static final long SCAN_PERIOD = 3000;

    // Scans every 60 seconds.
    private static final long SCAN_INTERVAL = 10000;

    private boolean mIsScanning = false;

    private boolean mIsInitialized = false;

    private AlarmManager mAlarmManager;

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "Received Broadcast Intent: "
                    + intent.toString());

            switch (intent.getAction()) {
                case ACTION_ALARM_PERIOD:
                    stopScan();
                    break;
            }

        }
    };

    private void broadcastUpdate(final String action) {
        final Intent intent = new Intent(action);
        sendBroadcast(intent);
    }

    private void broadcastUpdate(final EnvironmentalSensorBLEScanUpdate sensorScanUpdate) {
        final Intent intent = new Intent(ACTION_SCAN_UPDATE);
        intent.putExtra(EXTRA_UPDATE_PARCELABLE, sensorScanUpdate);
        sendBroadcast(intent);
        Log.d(TAG, "Broadcast Intent: " + ACTION_SCAN_UPDATE);
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
                for (byte byteChar : data)
                    stringBuilder.append(String.format("%02X ", byteChar));
                intent.putExtra(BluetoothLeService.EXTRA_DATA, new String(data)
                        + "\n" + stringBuilder.toString());
            }
        }
        sendBroadcast(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_ALARM_PERIOD);
        registerReceiver(mBroadcastReceiver, filter);

        mIsInitialized = initialize();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (mIsInitialized) {
            startScan();
        } else {
            broadcastUpdate(ACTION_ERROR_NOT_INITIALIZED);
            stopSelf();
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        unregisterReceiver(mBroadcastReceiver);
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
        } else {
            if (!mBluetoothAdapter.isEnabled()) {
                return false;
            }
        }

        mAlarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if (mAlarmManager == null) {
            Log.e(TAG, "Unable to obtain a AlarmManager.");
            return false;
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP && mBluetoothLeScanProxy == null) {
            mBluetoothLeScanProxy = new BluetoothLeScanL18Proxy(mBluetoothAdapter);
        } else if (mBluetoothLeScanProxy == null) {
            mBluetoothLeScanProxy = new BluetoothLeScanL21Proxy(mBluetoothAdapter);
        }
        mBluetoothLeScanProxy.setOnUpdateCallback(this);

        return true;
    }

    public void startScan() {

        if (mIsScanning) {
            Log.d(TAG, "Call to startScan() when already started.");
        } else {
            Intent intervalAlarmIntent = new Intent(BluetoothLeScanService.this,
                    BluetoothLeScanService.class);

            PendingIntent intervalPendingIntent = PendingIntent.getService(BluetoothLeScanService.this,
                    REQUEST_ALARM_SCAN_INTERVAL, intervalAlarmIntent, 0);

            mAlarmManager.set(AlarmManager.RTC_WAKEUP,
                    System.currentTimeMillis() + SCAN_INTERVAL, intervalPendingIntent);

            Intent periodAlarmIntent = new Intent();
            periodAlarmIntent.setAction(ACTION_ALARM_PERIOD);

            PendingIntent periodPendingIntent = PendingIntent.getBroadcast(this,
                    REQUEST_ALARM_SCAN_PERIOD, periodAlarmIntent, 0);

            mAlarmManager.set(AlarmManager.RTC_WAKEUP,
                    System.currentTimeMillis() + SCAN_PERIOD, periodPendingIntent);

            mBluetoothLeScanProxy.startLeScan();
            Log.d(TAG, "startLeScan()");
            mIsScanning = true;
        }
    }

    public void stopScan() {

        if (mIsScanning) {
            mBluetoothLeScanProxy.stopLeScan();
            Log.d(TAG, "stopLeScan()");
            mIsScanning = false;
        } else {
            Log.d(TAG, "Call to stopScan() when already stopped.");
        }
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

    @Override
    public void onUpdate(EnvironmentalSensorBLEScanUpdate sensorScanUpdate) {
        broadcastUpdate(sensorScanUpdate);
    }
}
