/*
 * Copyright 2018 iGrow Systems Limited. All rights reserved.
 *
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.igrow.android.addeditsensor;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.igrow.android.R;
import com.igrow.android.ViewModelFactory;
import com.igrow.android.util.ActivityUtils;


/**
 * An activity started when adding or editing an EnvironmentalSensor.
 *
 */
public class EnvironmentalSensorAddEditActivity extends AppCompatActivity
        implements AddEditSensorNavigator {

    public static final int REQUEST_CODE = 1;

    public static final int ADD_EDIT_RESULT_OK = RESULT_FIRST_USER + 1;

    @Override
    public void onSensorSaved() {
        setResult(ADD_EDIT_RESULT_OK);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_edit_sensor_act);

        // TODO: If exposing deep links into your app, handle intents here.

        // Set up the toolbar.
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        EnvironmentalSensorAddEditFragment addEditSensorFragment = obtainViewFragment();

        ActivityUtils.replaceFragmentInActivity(getSupportFragmentManager(),
                addEditSensorFragment, R.id.contentFrame);

        subscribeToNavigationChanges();
    }

    private void subscribeToNavigationChanges() {
        EnvironmentalSensorAddEditViewModel viewModel = obtainViewModel(this);

        // The activity observes the navigation events in the ViewModel
        viewModel.getSensorUpdatedEvent().observe(this, new Observer<Void>() {
            @Override
            public void onChanged(@Nullable Void _) {
                EnvironmentalSensorAddEditActivity.this.onSensorSaved();
            }
        });
    }

    public static EnvironmentalSensorAddEditViewModel obtainViewModel(FragmentActivity activity) {
        // Use a Factory to inject dependencies into the ViewModel
        ViewModelFactory factory = ViewModelFactory.getInstance(activity.getApplication());

        return ViewModelProviders.of(activity, factory).get(EnvironmentalSensorAddEditViewModel.class);
    }

    @NonNull
    private EnvironmentalSensorAddEditFragment obtainViewFragment() {
        // View Fragment
        EnvironmentalSensorAddEditFragment addEditTaskFragment = (EnvironmentalSensorAddEditFragment) getSupportFragmentManager()
                .findFragmentById(R.id.contentFrame);

        if (addEditTaskFragment == null) {
            addEditTaskFragment = EnvironmentalSensorAddEditFragment.newInstance();

            if (getIntent().getStringExtra(EnvironmentalSensorAddEditFragment.ARGUMENT_EDIT_SENSOR_ID) != null) {
                // Send the sensor ID to the fragment
                Bundle bundle = new Bundle();
                bundle.putString(EnvironmentalSensorAddEditFragment.ARGUMENT_EDIT_SENSOR_ID,
                        getIntent().getStringExtra(EnvironmentalSensorAddEditFragment.ARGUMENT_EDIT_SENSOR_ID));
                addEditTaskFragment.setArguments(bundle);
            } else {
                // send the address and name
                Bundle bundle = new Bundle();
                bundle.putString(EnvironmentalSensorAddEditFragment.ARGUMENT_SENSOR_ADDRESS,
                        getIntent().getStringExtra(EnvironmentalSensorAddEditFragment.ARGUMENT_SENSOR_ADDRESS));
                bundle.putString(EnvironmentalSensorAddEditFragment.ARGUMENT_SENSOR_NAME,
                        getIntent().getStringExtra(EnvironmentalSensorAddEditFragment.ARGUMENT_SENSOR_NAME));
                addEditTaskFragment.setArguments(bundle);
            }
        }
        return addEditTaskFragment;
    }
}
