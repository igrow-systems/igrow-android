package com.igrow.android;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import static com.igrow.android.BluetoothLeScanService.ACTION_SCAN_UPDATE;
import static com.igrow.android.BluetoothLeScanService.EXTRA_UPDATE_PARCELABLE;


/**
 * An activity representing a list of EnvironmentalSensors. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link EnvironmentalSensorDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 * <p/>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link EnvironmentalSensorRecyclerViewFragment} and the item details
 * (if present) is a {@link EnvironmentalSensorDetailFragment}.
 * <p/>
 * This activity also implements the required
 * {@link EnvironmentalSensorRecyclerViewFragment.Callbacks} interface
 * to listen for item selections.
 */
public class EnvironmentalSensorListScanActivity extends FragmentActivity
        implements EnvironmentalSensorRecyclerViewFragment.Callbacks {

    private final static String TAG = EnvironmentalSensorListScanActivity.class.getSimpleName();

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    private EnvironmentalSensorCollection mSensors;

    private EnvironmentalSensorRecyclerViewFragment mRecyclerViewFragment;

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "Received Broadcast Intent: "
                    + intent.toString());
            EnvironmentalSensorBLEScanUpdate sensorScanUpdate
                    = intent.getParcelableExtra(EXTRA_UPDATE_PARCELABLE);
            onSensorScanUpdate(sensorScanUpdate);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_environmentalsensor_scan_list);

        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            mRecyclerViewFragment = new EnvironmentalSensorRecyclerViewFragment();
            mRecyclerViewFragment.setDataSource(mSensors);
            transaction.replace(R.id.environmentalsensor_list_fragment, mRecyclerViewFragment);
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
//            ((EnvironmentalSensorRecyclerViewFragment) getSupportFragmentManager()
//                    .findFragmentById(com.igrow.android.R.id.environmentalsensor_list))
//                    .setActivateOnItemClick(true);
        }

        Intent intent = new Intent(this, BluetoothLeScanService.class);
        startService(intent);

        // TODO: If exposing deep links into your app, handle intents here.
    }

    @Override
    protected void onResume() {
        super.onResume();

        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_SCAN_UPDATE);
        registerReceiver(mBroadcastReceiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();

        unregisterReceiver(mBroadcastReceiver);
    }

    /**
     * Callback method from {@link EnvironmentalSensorRecyclerViewFragment.Callbacks}
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
        if (EnvironmentalSensorCollection.ITEM_MAP.containsKey(sensorScanUpdate.getAddress())) {
            EnvironmentalSensor sensor = EnvironmentalSensorCollection.ITEM_MAP.get(sensorScanUpdate.getAddress());
            sensor.setRSSI(sensorScanUpdate.getRSSI());
            mRecyclerViewFragment.setDataSource(mSensors);
        } else {
            EnvironmentalSensor sensor = new EnvironmentalSensor.EnvironmentalSensorBuilder()
                    .setAddress(sensorScanUpdate.getAddress())
                    .setFullName("Unknown Sensor")
                    .setRssi(sensorScanUpdate.getRSSI()).build();
            EnvironmentalSensorCollection.addItem(sensor);
            mRecyclerViewFragment.setDataSource(mSensors);
        }
    }
}
