package com.igrow.android.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothManager;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ServiceTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.igrow.android.bluetooth.BluetoothLeService;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

/**
 * Created by jsr on 13/12/2016.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class BluetoothLeServiceTest {

    @Rule
    public final ServiceTestRule mServiceRule = new ServiceTestRule();

    @Mock
    BluetoothManager mBluetoothManager;

    @Mock
    BluetoothAdapter mBluetoothAdapter;

    private static final String mBluetoothDeviceAddress = "AA-AA-AA-AA";

    @Mock
    BluetoothGatt mBluetoothGatt;

    BluetoothLeService mBluetoothLeService;

    @Before
    public void setUp() throws Exception {
        mBluetoothLeService = new BluetoothLeService();
    }

    @After
    public void tearDown() throws Exception {
        mBluetoothLeService.close();
    }

    @Test
    public void onBind() throws Exception {

    }

    @Test
    public void onUnbind() throws Exception {

    }

    @Test
    public void initialize() throws Exception {

    }

    @Test
    public void connect() throws Exception {

    }

    @Test
    public void disconnect() throws Exception {

    }

    @Test
    public void close() throws Exception {

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

}