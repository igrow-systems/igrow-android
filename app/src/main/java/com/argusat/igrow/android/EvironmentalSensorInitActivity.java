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

package com.argusat.igrow.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;


/**
 * An activity representing a list of EnvironmentalSensors. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link EvironmentalSensorDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 * <p/>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link EvironmentalSensorListFragment} and the item details
 * (if present) is a {@link EvironmentalSensorDetailFragment}.
 * <p/>
 * This activity also implements the required
 * {@link EvironmentalSensorListFragment.Callbacks} interface
 * to listen for item selections.
 */
public class EvironmentalSensorInitActivity extends FragmentActivity
        implements EvironmentalSensorListFragment.Callbacks {

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
        setContentView(R.layout.activity_environmentalsensor_list);

        if (findViewById(R.id.evironmentalsensor_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-large and
            // res/values-sw600dp). If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;

            // In two-pane mode, list items should be given the
            // 'activated' state when touched.
            ((EvironmentalSensorListFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.evironmentalsensor_list))
                    .setActivateOnItemClick(true);
        }


        // TODO: If exposing deep links into your app, handle intents here.
    }

    /**
     * Callback method from {@link EvironmentalSensorListFragment.Callbacks}
     * indicating that the item with the given ID was selected.
     */
    @Override
    public void onItemSelected(String id) {
        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putString(EvironmentalSensorDetailFragment.ARG_ITEM_ID, id);
            EvironmentalSensorDetailFragment fragment = new EvironmentalSensorDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.evironmentalsensor_detail_container, fragment)
                    .commit();

        } else {
            // In single-pane mode, simply start the detail activity
            // for the selected item ID.
            Intent detailIntent = new Intent(this, EvironmentalSensorDetailActivity.class);
            detailIntent.putExtra(EvironmentalSensorDetailFragment.ARG_ITEM_ID, id);
            startActivity(detailIntent);
        }
    }
}
