package com.igrow.android;

import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.support.annotation.NonNull;

import com.igrow.android.bluetooth.BluetoothAdapter;
import com.igrow.android.bluetooth.BluetoothLeScanProxy;
import com.igrow.android.bluetooth.BluetoothManager;
import com.igrow.android.bluetooth.FakeBluetoothAdapter;
import com.igrow.android.bluetooth.FakeBluetoothLeScanProxy;
import com.igrow.android.bluetooth.FakeBluetoothLeScanService;
import com.igrow.android.bluetooth.FakeBluetoothManager;
import com.igrow.android.data.FakeEnvironmentalSensorsRemoteDataSource;
import com.igrow.android.data.source.EnvironmentalSensorsDataSource;
import com.igrow.android.data.source.EnvironmentalSensorsRepository;
import com.igrow.android.data.source.local.EnvironmentalSensorsLocalDataSource;
import com.igrow.android.data.source.local.IGrowDatabase;
import com.igrow.android.util.AppExecutors;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Enables injection of mock implementations for
 * {@link EnvironmentalSensorsDataSource} at compile time. This is useful for testing, since it allows us to use
 * a fake instance of the class to isolate the dependencies and run a test hermetically.
 */
public class Injection {

    // TODO: The following allow implementations from a mocking
    // framework to be set by test code.  This could be
    // fixed long term by having DI not in this form of
    // static methods but a runtime implementation.
    private static BluetoothManager mBluetoothManagerMock = null;

    private static BluetoothAdapter mBluetoothAdapterMock = null;

    private static BluetoothLeScanProxy mBluetoothLeScanProxyMock = null;

    public static EnvironmentalSensorsRepository provideEnvironmentalSensorsRepository(@NonNull Context context) {
        checkNotNull(context);
        IGrowDatabase database = IGrowDatabase.getInstance(context);
        return EnvironmentalSensorsRepository.getInstance(FakeEnvironmentalSensorsRemoteDataSource.getInstance(),
                EnvironmentalSensorsLocalDataSource.getInstance(new AppExecutors(),
                        database.environmentalSensorsDao()));
    }

    public static void bindBluetoothLeScanService(@NonNull Context context, ServiceConnection connection, int bindFlags) {
        checkNotNull(context);

        // TODO the sequencing of calls from a client's perspective may not be as per the framework proper
        connection.onServiceConnected(new ComponentName("com.igrow.android", "FakeBluetoothLeScanService"),
                FakeBluetoothLeScanService.getInstance(context).getBinder());
    }

    public static void unbindBluetoothLeScanService(@NonNull Context context, ServiceConnection connection) {
        checkNotNull(context);

        // TODO the sequencing of calls from a client's perspective may not be as per the framework proper
        connection.onServiceDisconnected(new ComponentName("com.igrow.android", "FakeBluetoothLeScanService"));
    }

    public static void setBluetoothManagerMock(BluetoothManager bluetoothManagerMock) {
        Injection.mBluetoothManagerMock = bluetoothManagerMock;
    }

    public static void setBluetoothAdapterMock(BluetoothAdapter bluetoothAdapterMock) {
        Injection.mBluetoothAdapterMock = bluetoothAdapterMock;
    }

    public static void setBluetoothLeScanProxyMock(BluetoothLeScanProxy bluetoothLeScanProxyMock) {
        Injection.mBluetoothLeScanProxyMock = bluetoothLeScanProxyMock;
    }

    public static BluetoothManager provideBluetoothManager(@NonNull Context context) {
        if (mBluetoothManagerMock == null) {
            return new FakeBluetoothManager();
        } else {
            return mBluetoothManagerMock;
        }
    }

    public static BluetoothAdapter provideBluetoothAdapter(@NonNull Context context) {
        if (mBluetoothAdapterMock == null) {
            android.bluetooth.BluetoothManager bluetoothManager
                    = (android.bluetooth.BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
            return new FakeBluetoothAdapter();
        } else {
            return mBluetoothAdapterMock;
        }
    }

    public static BluetoothLeScanProxy provideBluetoothLeScanProxy(BluetoothAdapter bluetoothAdapter) {
        if (mBluetoothLeScanProxyMock == null) {
            return new FakeBluetoothLeScanProxy();
        } else {
            return mBluetoothLeScanProxyMock;
        }
    }
}