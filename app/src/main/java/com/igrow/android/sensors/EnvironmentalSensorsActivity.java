package com.igrow.android.sensors;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.igrow.android.R;
import com.igrow.android.ViewModelFactory;
import com.igrow.android.bluetooth.BluetoothLeScanServiceImpl;
import com.igrow.android.bluetooth.EnvironmentalSensorBLEScanUpdate;
import com.igrow.android.data.EnvironmentalSensor;
import com.igrow.android.sensor.EnvironmentalSensorCollection;
import com.igrow.android.sensordetail.EnvironmentalSensorDetailActivity;
import com.igrow.android.sensordetail.EnvironmentalSensorDetailFragment;
import com.igrow.android.statistics.StatisticsActivity;

import java.util.UUID;

import static com.igrow.android.bluetooth.BluetoothLeScanServiceImpl.ACTION_SCAN_UPDATE;
import static com.igrow.android.bluetooth.BluetoothLeScanServiceImpl.ERROR_NOT_INITIALIZED;
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
 * {@link EnvironmentalSensorsScanFragment} and the item details
 * (if present) is a {@link EnvironmentalSensorDetailFragment}.
 */
public class EnvironmentalSensorsActivity extends AppCompatActivity {

    private final static String TAG = EnvironmentalSensorsActivity.class.getSimpleName();

    private DrawerLayout mDrawerLayout;

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    private EnvironmentalSensorsViewModel mViewModel;

    private EnvironmentalSensorsFragment mSensorsFragment;

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
        setContentView(R.layout.environmentalsensors_act);

        // TODO: If exposing deep links into your app, handle intents here.

        setupToolbar();

        setupNavigationDrawer();

        setupViewFragment(savedInstanceState);

        mViewModel = obtainViewModel(this);

        // Subscribe to "new sensor" event
        mViewModel.getNewEnvironmentalSensorEvent().observe(this, new Observer<Void>() {
            @Override
            public void onChanged(@Nullable Void _) {
                addNewSensor();
            }
        });

        // Subscribe to "open sensor" event
        mViewModel.getOpenEnvironmentalSensorEvent().observe(this, new Observer<UUID>() {
            @Override
            public void onChanged(@Nullable UUID id) {
                onItemSelected(id);
            }
        });
    }

    void setupViewFragment(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            mSensorsFragment = EnvironmentalSensorsFragment.newInstance(null, null);
            transaction.replace(R.id.content_frame, mSensorsFragment);
            transaction.commit();
        }

        if (findViewById(R.id.sensor_detail_frag) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-large and
            // res/values-sw600dp). If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;

            // In two-pane mode, list items should be given the
            // 'activated' state when touched.
//            ((EnvironmentalSensorsScanFragment) getSupportFragmentManager()
//                    .findFragmentById(com.igrow.android.R.id.sensor_detail_frag))
//                    .setActivateOnItemClick(true);
        }
    }

//    private void setupViewFragment() {
//        TasksFragment tasksFragment =
//                (TasksFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
//        if (tasksFragment == null) {
//            // Create the fragment
//            tasksFragment = TasksFragment.newInstance();
//            ActivityUtils.replaceFragmentInActivity(
//                    getSupportFragmentManager(), tasksFragment, R.id.contentFrame);
//        }
//    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);
    }

    private void setupNavigationDrawer() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setStatusBarBackground(R.color.colorPrimaryDark);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.list_navigation_menu_item:
                                // Do nothing, we're already on that screen
                                break;
                            case R.id.statistics_navigation_menu_item:
                                Intent intent =
                                        new Intent(EnvironmentalSensorsActivity.this, StatisticsActivity.class);
                                startActivity(intent);
                                break;
                            default:
                                break;
                        }
                        // Close the navigation drawer when an item is selected.
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });
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
    }

    @Override
    protected void onStop() {

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

    public void onItemSelected(UUID id) {
        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putString(EnvironmentalSensorDetailFragment.ARGUMENT_SENSOR_ID, id.toString());
            EnvironmentalSensorDetailFragment fragment = new EnvironmentalSensorDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.sensor_detail_frag, fragment)
                    .commit();

        } else {
            // In single-pane mode, simply start the detail activity
            // for the selected item ID.
            Intent detailIntent = new Intent(this, EnvironmentalSensorDetailActivity.class);
            detailIntent.putExtra(EnvironmentalSensorDetailFragment.ARGUMENT_SENSOR_ID, id);
            startActivity(detailIntent);
        }
    }

    private void onSensorScanUpdate(EnvironmentalSensorBLEScanUpdate sensorScanUpdate) {


    }

    public void addNewSensor() {

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
