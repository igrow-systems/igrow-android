package com.igrow.android.bluetooth;

import android.bluetooth.BluetoothGatt;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ServiceTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.igrow.android.Injection;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.when;

/**
 * Created by jsr on 13/12/2016.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class BluetoothLeServiceImplTest {

    @Rule
    public final ServiceTestRule mServiceRule = new ServiceTestRule();

    @Mock
    BluetoothManager mBluetoothManagerMock;

    @Mock
    BluetoothAdapter mBluetoothAdapterMock;

    private static final String mBluetoothDeviceAddress = "AA-AA-AA-AA";

    @Mock
    BluetoothGatt mBluetoothGatt;

    BluetoothLeServiceImpl mBluetoothLeServiceImpl;


    private BroadcastReceiver mTestReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {

            }
        }
    };


    @Before
    public void setUp() throws Exception {

        mServiceRule.startService(buildStartServiceIntent());

    }

    //@Before
    public void setupMockSystemServices() {

        MockitoAnnotations.initMocks(this);

        when(mBluetoothManagerMock.getAdapter()).thenReturn(mBluetoothAdapterMock);

        Injection.setBluetoothManagerMock(mBluetoothManagerMock);

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void executeJob_AttemptsConnection() throws Exception {

        InstrumentationRegistry.getTargetContext().sendBroadcast(buildExecuteJobIntent());



    }

    @Test
    public void readCharacteristic() throws Exception {

    }

    @Test
    public void setCharacteristicNotification() throws Exception {

    }

    @Test
    public void getSupportedGattServices() throws Exception {

    }

    private Intent buildStartServiceIntent() {
        final Intent intent = new Intent(InstrumentationRegistry.getTargetContext(), BluetoothLeServiceImpl.class);
        return intent;
    }

    private Intent buildExecuteJobIntent() {
        final Intent intent = new Intent(InstrumentationRegistry.getTargetContext(), BluetoothLeServiceImpl.class);
        intent.setAction("com.firebase.jobdispatcher.ACTION_EXECUTE");
        return intent;
    }

}