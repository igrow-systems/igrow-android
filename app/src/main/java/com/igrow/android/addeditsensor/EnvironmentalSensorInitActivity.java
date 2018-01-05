/*
 * Copyright 2016 iGrow Systems Limited. All rights reserved.
 *
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.igrow.android.addeditsensor;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;

import com.igrow.android.R;
import com.igrow.android.sensors.EnvironmentalSensorsFragment;
import com.igrow.android.sensordetail.EnvironmentalSensorDetailActivity;
import com.igrow.android.sensordetail.EnvironmentalSensorDetailFragment;


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
public class EnvironmentalSensorInitActivity extends FragmentActivity
        implements EnvironmentalSensorsFragment.Callbacks {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    private CardView mCardView;

    private RecyclerView.LayoutManager mLayoutManager;

    private RecyclerView.Adapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.igrow.android.R.layout.fragment_environmentalsensor_list);

        if (findViewById(R.id.environmentalsensor_detail_fragment) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-large and
            // res/values-sw600dp). If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;

            // In two-pane mode, list items should be given the
            // 'activated' state when touched.
//            ((EnvironmentalSensorsFragment) getSupportFragmentManager()
//                    .findFragmentById(com.igrow.android.R.id.environmentalsensor_list))
//                    .setActivateOnItemClick(true);
        }


        // TODO: If exposing deep links into your app, handle intents here.
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
}
