package com.igrow.android;

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

import static com.igrow.android.BluetoothLeScanService.ACTION_ERROR_NOT_INITIALIZED;
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
public class EnvironmentalSensorListActivity extends FragmentActivity
        implements EnvironmentalSensorRecyclerViewFragment.Callbacks {

    private final static String TAG = EnvironmentalSensorListActivity.class.getSimpleName();

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    private EnvironmentalSensorCollection mSensors;

    private EnvironmentalSensorRecyclerViewFragment mRecyclerViewFragment;

    private int REQUEST_ENABLE_BT = 1;

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "Received Broadcast Intent: "
                    + intent.toString());

            switch (intent.getAction()) {
                case ACTION_ERROR_NOT_INITIALIZED:
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
                            = intent.getParcelableExtra(EXTRA_UPDATE_PARCELABLE);
                    onSensorScanUpdate(sensorScanUpdate);
                    break;
            }

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_environmentalsensor_list);

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

        // TODO: If exposing deep links into your app, handle intents here.
    }

    @Override
    protected void onResume() {
        super.onResume();

        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_ERROR_NOT_INITIALIZED);
        filter.addAction(ACTION_SCAN_UPDATE);
        registerReceiver(mBroadcastReceiver, filter);

        Intent intent = new Intent(this, BluetoothLeScanService.class);
        startService(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();

        unregisterReceiver(mBroadcastReceiver);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_ENABLE_BT) {
            switch (resultCode) {
                case RESULT_OK:
                    // attempt to restart the service
                    Intent intent = new Intent(this, BluetoothLeScanService.class);
                    startService(intent);
                    break;
                case RESULT_CANCELED:
                    // Inform the user that they can't interact with sensors if
                    // bluetooth is disabled, only view existing data.
                    break;
            }
        }
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


    }

    public void onClick(View v) {

    }

}
