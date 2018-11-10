package com.igrow.android.bluetooth;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ServiceTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.igrow.android.Injection;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.TimeoutException;

import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static org.hamcrest.CoreMatchers.any;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.when;

/**
 * Created by jsr on 14/12/2016.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class BluetoothLeScanServiceImplTest {

    @Rule
    public final ServiceTestRule mServiceRule = new ServiceTestRule();

    private BluetoothLeScanService mBluetoothLeScanService;

    @Mock
    private Context mContextMock;

    @Mock
    private BluetoothManager mBluetoothManagerMock;

    @Mock
    private BluetoothAdapter mBluetoothAdapterMock;


    private BroadcastReceiver mTestReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {

            }
        }
    };



    @Before
    public void setupMockSystemServices() {

        MockitoAnnotations.initMocks(this);

        when(mBluetoothManagerMock.getAdapter()).thenReturn(mBluetoothAdapterMock);

        Injection.setBluetoothManagerMock(mBluetoothManagerMock);

    }

    @Test
    public void initialize() throws Exception {

    }

    @Test
    public void bindService_bluetoothDisabled_isNotInitialized() throws Exception {

        when(mBluetoothAdapterMock.isEnabled())
                .thenReturn(false);

        bindService();

        boolean isServiceInitialized = mBluetoothLeScanService.isInitialized();

        assertThat(isServiceInitialized, is(false));

    }

    @Test
    public void bindService_bluetoothEnabled_isInitialized() throws Exception {


        when(mBluetoothAdapterMock.isEnabled())
                .thenReturn(true);

        bindService();

        boolean isServiceInitialized = mBluetoothLeScanService.isInitialized();

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
        mBluetoothLeScanService = ((LocalBinder) binder).getService();

        assertThat(mBluetoothLeScanService, is(notNullValue()));

    }

}