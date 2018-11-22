package com.igrow.android;

import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.support.annotation.NonNull;


import com.igrow.android.bluetooth.BluetoothAdapter;
import com.igrow.android.bluetooth.BluetoothLeScanProxy;
import com.igrow.android.bluetooth.BluetoothLeScanService;
import com.igrow.android.bluetooth.BluetoothLeScanServiceImpl;
import com.igrow.android.bluetooth.BluetoothManager;
import com.igrow.android.bluetooth.BluetoothManagerProxy;
import com.igrow.android.data.source.EnvironmentalSensorsRepository;
import com.igrow.android.data.source.local.EnvironmentalSensorsLocalDataSource;
import com.igrow.android.data.source.local.IGrowDatabase;
import com.igrow.android.data.source.remote.EnvironmentalSensorsRemoteDataSource;
import com.igrow.android.util.AppExecutors;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Enables injection of production implementations for
 * {@link EnvironmentalSensorsDataSource} at compile time.
 */
public class Injection {

    public static EnvironmentalSensorsRepository provideEnvironmentalSensorsRepository(@NonNull Context context) {
        checkNotNull(context);
        IGrowDatabase database = IGrowDatabase.getInstance(context);
        return EnvironmentalSensorsRepository.getInstance(EnvironmentalSensorsRemoteDataSource.getInstance(),
                EnvironmentalSensorsLocalDataSource.getInstance(new AppExecutors(),
                        database.environmentalSensorsDao()));
    }


    public static boolean bindBluetoothLeScanService(@NonNull Context context, ServiceConnection connection, int bindFlags) {
        checkNotNull(context);

        Intent intent = new Intent(context, BluetoothLeScanServiceImpl.class);
        return context.bindService(intent, connection, bindFlags);
    }

    public static void unbindBluetoothLeScanService(@NonNull Context context, ServiceConnection connection) {
        checkNotNull(context);

        context.unbindService(connection);
    }

    public static BluetoothManager provideBluetoothManager(@NonNull Context context) {
        android.bluetooth.BluetoothManager bluetoothManager
                = (android.bluetooth.BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        return new BluetoothManagerProxy(bluetoothManager);
    }

    public static BluetoothAdapter provideBluetoothAdapter(@NonNull Context context) {
        android.bluetooth.BluetoothManager bluetoothManager
                = (android.bluetooth.BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        return new BluetoothManagerProxy(bluetoothManager).getAdapter();
    }

    public static BluetoothLeScanProxy provideBluetoothLeScanProxy(BluetoothAdapter bluetoothAdapter) {
        return bluetoothAdapter.getBluetoothLeScanProxy();
    }
}
