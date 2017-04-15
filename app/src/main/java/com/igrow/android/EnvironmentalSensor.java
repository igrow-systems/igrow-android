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

package com.igrow.android;

import android.bluetooth.BluetoothDevice;
import android.location.Location;

import com.igrow.model.ObservationCollection;

/**
 * Created by jsr on 30/05/16.
 */
public abstract class EnvironmentalSensor {

    private String mFullName;

    private int mTimestamp;

    private Location mLocation;

    private ObservationCollection mObservationCollection;

    public abstract BluetoothDevice getBluetoothDevice();

    public abstract int getRSSI();

    public abstract int getTimestamp();

    public String getFullName() {
        return mFullName;
    }

    public Location getLocation() { return mLocation; }

    public ObservationCollection getObservationCollection() {
        return mObservationCollection;
    }


}
