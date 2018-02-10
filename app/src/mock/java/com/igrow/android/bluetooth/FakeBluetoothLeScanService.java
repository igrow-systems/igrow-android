package com.igrow.android.bluetooth;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.util.Random;


/**
 * Created by jsr on 5/02/18.
 */

public class FakeBluetoothLeScanService extends Service implements BluetoothLeScanService {

    public final static String TEST_ADDRESS_1 = "c0:9e:19:a7:ce:9c";

    public final static String TEST_ADDRESS_2 = "91:f2:01:7a:83:02";

    private static FakeBluetoothLeScanService INSTANCE = null;

    private IBinder mBinder = new LocalBinder(this);

    private Context mContext;

    private Thread mBackgroundThread;

    private final static String[] TEST_ADDRESSES = new String[]{TEST_ADDRESS_1, TEST_ADDRESS_2};

    private final static Random mRandomGenerator = new Random();

    // TODO volatile not required here as we synchronized() access?
    private volatile boolean mScanning = false;

    public static FakeBluetoothLeScanService getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (FakeBluetoothLeScanService.class) {
                if (INSTANCE == null) {
                    INSTANCE = new FakeBluetoothLeScanService(context);
                }
            }
        }
        return INSTANCE;
    }

    private FakeBluetoothLeScanService(Context context) {
        mContext = context;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void startScan() {

        synchronized (FakeBluetoothLeScanService.class) {
            if (!mScanning) {
                mScanning = true;
                // periodically fire off scan intents
                mBackgroundThread = new Thread(new Runnable() {
                    public void run() {
                        int currentIndex = 0;
                        while (mScanning) {
                            EnvironmentalSensorBLEScanUpdate sensorScanUpdate
                                    = new EnvironmentalSensorBLEScanUpdate(TEST_ADDRESSES[currentIndex],
                                    mRandomGenerator.nextInt(100));
                            Intent intent = new Intent(BluetoothLeScanService.ACTION_SCAN_UPDATE);
                            intent.putExtra(EXTRA_UPDATE_PARCELABLE, sensorScanUpdate);
                            mContext.sendBroadcast(intent);
                            currentIndex++;
                            if (currentIndex >= TEST_ADDRESSES.length) {
                                currentIndex = 0;
                            }
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException ie) {
                                mScanning = false;
                            }
                        }
                    }
                });
                mBackgroundThread.start();
            }
        }

    }

    @Override
    public void stopScan() {
        synchronized (FakeBluetoothLeScanService.class) {
            mScanning = false;
        }
    }

    @Override
    public void close() {

    }

    @Override
    public boolean isInitialized() {
        return true;
    }

    public IBinder getBinder() {
        return mBinder;
    }
}
