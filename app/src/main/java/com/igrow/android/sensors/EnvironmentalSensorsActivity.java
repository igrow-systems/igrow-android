package com.igrow.android.sensors;

import android.arch.lifecycle.ViewModelProviders;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;

import com.igrow.android.ViewModelFactory;
import com.igrow.android.bluetooth.EnvironmentalSensorBLEScanUpdate;
import com.igrow.android.sensor.EnvironmentalSensorCollection;
import com.igrow.android.R;
import com.igrow.android.bluetooth.BluetoothLeScanServiceImpl;
import com.igrow.android.data.EnvironmentalSensor;
import com.igrow.android.sensordetail.EnvironmentalSensorDetailActivity;
import com.igrow.android.sensordetail.EnvironmentalSensorDetailFragment;

import static com.igrow.android.bluetooth.BluetoothLeScanServiceImpl.ERROR_NOT_INITIALIZED;
import static com.igrow.android.bluetooth.BluetoothLeScanServiceImpl.ACTION_SCAN_UPDATE;
import static com.igrow.android.bluetooth.BluetoothLeScanServiceImpl.EXTRA_UPDATE_PARCELABLE;


/**
 * An activity representing a list of EnvironmentalSensors. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link EnvironmentalSensorDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 * <p/>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link EnvironmentalSensorsFragment} and the item details
 * (if present) is a {@link EnvironmentalSensorDetailFragment}.
 * <p/>
 * This activity also implements the required
 * {@link EnvironmentalSensorsFragment.Callbacks} interface
 * to listen for item selections.
 */
public class EnvironmentalSensorsActivity extends FragmentActivity
        implements EnvironmentalSensorsFragment.Callbacks {

    private final static String TAG = EnvironmentalSensorsActivity.class.getSimpleName();

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    private EnvironmentalSensorsViewModel mViewModel;

    private EnvironmentalSensorsFragment mRecyclerViewFragment;

    private final static int REQUEST_ENABLE_BT = 1;

    private final static int REQUEST_PICK_SENSOR = 2;

    private boolean mIsBluetoothLeScanServiceStarted = false;

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent broadcastIntent) {
            Log.d(TAG, "Received broadcast Intent: "
                    + broadcastIntent.toString());

            switch (broadcastIntent.getAction()) {
                case ERROR_NOT_INITIALIZED:
                    // explicitly stop the service here to avoid the
                    // service calling stopSelf() and the framework
                    // repeatedly attempting to resurrect the service
                    Intent intent = new Intent(EnvironmentalSensorsActivity.this,
                            BluetoothLeScanServiceImpl.class);
                    stopService(intent);
                    mIsBluetoothLeScanServiceStarted = false;
                    // Likely reason is that bluetooth is disabled so try to enable
                    Intent intentBtEnabled = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    // The REQUEST_ENABLE_BT constant passed to startActivityForResult() is a
                    // locally defined integer (which must be greater than 0), that the system
                    // passes back to you in your onActivityResult()
                    // implementation as the requestCode parameter.
                    startActivityForResult(intentBtEnabled, REQUEST_ENABLE_BT);
                    break;
                case ACTION_SCAN_UPDATE:
                    EnvironmentalSensorBLEScanUpdate sensorScanUpdate
                            = broadcastIntent.getParcelableExtra(EXTRA_UPDATE_PARCELABLE);
                    onSensorScanUpdate(sensorScanUpdate);
                    break;
            }

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_environmentalsensor_list);

        // TODO: If exposing deep links into your app, handle intents here.

        //setupViewFragment(savedInstanceState);

        mViewModel = obtainViewModel(this);
    }

    void setupViewFragment(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            mRecyclerViewFragment = new EnvironmentalSensorsFragment();
            transaction.replace(R.id.fragment_environmentalsensor_list, mRecyclerViewFragment);
            transaction.commit();
        }

        if (findViewById(R.id.environmentalsensor_detail_fragment) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-large and
            // res/values-sw600dp). If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;

            // In two-pane mode, list items should be given the
            // 'activated' state when touched.
//            ((EnvironmentalSensorsFragment) getSupportFragmentManager()
//                    .findFragmentById(com.igrow.android.R.id.fragment_environmentalsensor_detail))
//                    .setActivateOnItemClick(true);
        }
    }

    public static EnvironmentalSensorsViewModel obtainViewModel(FragmentActivity activity) {
        // Use a Factory to inject dependencies into the ViewModel
        ViewModelFactory factory = ViewModelFactory.getInstance(activity.getApplication());

        EnvironmentalSensorsViewModel viewModel =
                ViewModelProviders.of(activity, factory).get(EnvironmentalSensorsViewModel.class);

        return viewModel;
    }

    @Override
    protected void onStart() {
        super.onStart();

        IntentFilter filter = new IntentFilter();
        filter.addAction(ERROR_NOT_INITIALIZED);
        filter.addAction(ACTION_SCAN_UPDATE);

        registerReceiver(mBroadcastReceiver, filter);

        //Intent intent = new Intent(this, BluetoothLeScanServiceImpl.class);
        //startService(intent);
        //mIsBluetoothLeScanServiceStarted = true;
    }

    @Override
    protected void onStop() {

        //Intent intent = new Intent(EnvironmentalSensorsActivity.this,
        //        BluetoothLeScanServiceImpl.class);
        //stopService(intent);
        //mIsBluetoothLeScanServiceStarted = false;

        unregisterReceiver(mBroadcastReceiver);

        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    @Override
    protected void onPause() {


        super.onPause();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_ENABLE_BT:
                switch (resultCode) {
                    case RESULT_OK:
                        // attempt to restart the service
                        Intent intent = new Intent(this, BluetoothLeScanServiceImpl.class);
                        startService(intent);
                        mIsBluetoothLeScanServiceStarted = true;
                        break;
                    case RESULT_CANCELED:
                        // Inform the user that they can't interact with sensors if
                        // bluetooth is disabled, only view existing data.

                        break;
                }
                break;
            case REQUEST_PICK_SENSOR:
                switch (resultCode) {
                    case RESULT_OK:
                        // add the sensor the user picked to the
                        // collection of known sensors
                        EnvironmentalSensor sensor =
                                new EnvironmentalSensor.EnvironmentalSensorBuilder()
                                        .setAddress(data.getStringExtra(
                                                EnvironmentalSensorsScanActivity.EXTRA_DEVICE_ADDRESS))
                                        .setFullName("Unknown Sensor")
                                        .setRssi(0).build();
                        EnvironmentalSensorCollection.addItem(sensor);
                        break;
                    case RESULT_CANCELED:
                        // do nothing for now
                        break;
                }
                break;
        }
    }

    /**
     * Callback method from {@link EnvironmentalSensorsFragment.Callbacks}
     * indicating that the item with the given ID was selected.
     */
    @Override
    public void onItemSelected(String id) {
        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putString(EnvironmentalSensorDetailFragment.ARG_ITEM_ID, id);
            EnvironmentalSensorDetailFragment fragment = new EnvironmentalSensorDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.environmentalsensor_detail_fragment, fragment)
                    .commit();

        } else {
            // In single-pane mode, simply start the detail activity
            // for the selected item ID.
            Intent detailIntent = new Intent(this, EnvironmentalSensorDetailActivity.class);
            detailIntent.putExtra(EnvironmentalSensorDetailFragment.ARG_ITEM_ID, id);
            startActivity(detailIntent);
        }
    }

    private void onSensorScanUpdate(EnvironmentalSensorBLEScanUpdate sensorScanUpdate) {


    }

    public void onClick(View v) {

        Intent intentPickSensor = new Intent(this,
                EnvironmentalSensorsScanActivity.class);
        intentPickSensor.setAction(Intent.ACTION_PICK);
        // The REQUEST_ENABLE_BT constant passed to startActivityForResult() is a
        // locally defined integer (which must be greater than 0), that the system
        // passes back to you in your onActivityResult()
        // implementation as the requestCode parameter.
        startActivityForResult(intentPickSensor, REQUEST_PICK_SENSOR);
    }


}
