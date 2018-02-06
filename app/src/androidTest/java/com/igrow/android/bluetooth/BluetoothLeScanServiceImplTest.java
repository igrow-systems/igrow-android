package com.igrow.android.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ServiceTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
//import org.powermock.api.mockito.PowerMockito;

import java.util.concurrent.TimeoutException;

import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static org.hamcrest.CoreMatchers.any;
import static org.hamcrest.core.Is.is;

/**
 * Created by jsr on 14/12/2016.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class BluetoothLeScanServiceImplTest {

    @Rule
    public final ServiceTestRule mServiceRule = new ServiceTestRule();

    private BluetoothLeScanServiceImpl mBluetoothLeScanServiceImpl;

    @Mock
    private Context mContextMock;

    @Mock
    private BluetoothManager mBluetoothManagerMock;

    //@Mock
    private BluetoothAdapter mBluetoothAdapterMock;

    // BluetoothManager obtained from target app context
    private BluetoothManager mBluetoothManager;


    private BroadcastReceiver mTestReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {

            }
        }
    };



    @Before
    public void setupMockSystemServices() {

        //MockitoAnnotations.initMocks(this);

        //mBluetoothAdapterMock = PowerMockito.mock(BluetoothAdapter.class);

        //when(mBluetoothManagerMock.getAdapter()).thenReturn(mBluetoothAdapterMock);

        //when(mContextMock.getSystemService(Context.BLUETOOTH_SERVICE))
        //        .thenReturn(mBluetoothManagerMock);

    }

    @Test
    public void initialize() throws Exception {

    }

    @Test
    public void bindService_bluetoothDisabled_isNotInitialized() throws Exception {

        mBluetoothManager = (BluetoothManager) InstrumentationRegistry
                .getTargetContext()
                .getSystemService(Context.BLUETOOTH_SERVICE);

        boolean isBluetoothEnabled = mBluetoothManager.getAdapter().isEnabled();
        if (isBluetoothEnabled) {
            // force bluetooth disabled
            mBluetoothManager.getAdapter().disable();
        }

        bindService();

        boolean isServiceInitialized = mBluetoothLeScanServiceImpl.isInitialized();

        if (isBluetoothEnabled) {
            // return bluetoothAdapter to its original state (we hope!)
            mBluetoothManager.getAdapter().enable();
        }

        assertThat(isServiceInitialized, is(false));

    }

    @Test
    public void bindService_bluetoothEnabled_isInitialized() throws Exception {

        mBluetoothManager = (BluetoothManager) InstrumentationRegistry
                .getTargetContext()
                .getSystemService(Context.BLUETOOTH_SERVICE);

        boolean isBluetoothEnabled = mBluetoothManager.getAdapter().isEnabled();
        if (!isBluetoothEnabled) {
            // force bluetooth disabled
            mBluetoothManager.getAdapter().enable();
        }

        bindService();

        boolean isServiceInitialized = mBluetoothLeScanServiceImpl.isInitialized();

        if (!isBluetoothEnabled) {
            // return bluetoothAdapter to its original state (we hope!)
            mBluetoothManager.getAdapter().disable();
        }

        assertThat(isServiceInitialized, is(true));

    }

    @Test
    public void stop() throws Exception {

    }

    @Test
    public void close() throws Exception {

    }

    private void bindService() throws TimeoutException {

        // Create the service Intent.
        Intent serviceIntent =
                new Intent(InstrumentationRegistry.getTargetContext(), BluetoothLeScanServiceImpl.class);

        // Data can be passed to the service via the Intent.
        //serviceIntent.putExtra(LocalService.SEED_KEY, 42L);

        // Bind the service and grab a reference to the binder.
        IBinder binder = mServiceRule.bindService(serviceIntent);

        // Get the reference to the service, or you can call public methods on the binder directly.
        mBluetoothLeScanServiceImpl = ((BluetoothLeScanServiceImpl.LocalBinder) binder).getService();

        // Verify that the service is working correctly.
        assertThat(/*service.getRandomInt()*/ 333, is(any(Integer.class)));

    }

}