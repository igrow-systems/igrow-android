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

import android.location.Location;

import com.igrow.model.ObservationCollection;

/**
 * Created by jsr on 30/05/16.
 */
public class EnvironmentalSensor {

    private String mAddress;

    private String mFullName;

    private int mRSSI;

    private int mTimestamp;

    private Location mLocation;

    private ObservationCollection mObservationCollection;

    public static class EnvironmentalSensorBuilder {

        private String address;
        private String fullName;
        private int rssi;
        private int timestamp;
        private Location location;
        private ObservationCollection observationCollection;

        public EnvironmentalSensorBuilder setAddress(String address) {
            this.address = address;
            return this;
        }

        public EnvironmentalSensorBuilder setFullName(String fullName) {
            this.fullName = fullName;
            return this;
        }

        public EnvironmentalSensorBuilder setRssi(int rssi) {
            this.rssi = rssi;
            return this;
        }

        public EnvironmentalSensorBuilder setTimestamp(int timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public EnvironmentalSensorBuilder setLocation(Location location) {
            this.location = location;
            return this;
        }

        public EnvironmentalSensorBuilder setObservationCollection(ObservationCollection observationCollection) {
            this.observationCollection = observationCollection;
            return this;
        }

        public EnvironmentalSensor build() {
            return new EnvironmentalSensor(address, fullName, rssi, timestamp, location, observationCollection);
        }
    }

    public EnvironmentalSensor(String address, String fullName, int rssi, int timestamp,
                               Location location, ObservationCollection observationCollection) {
        this.mAddress = address;
        this.mFullName = fullName;
        this.mRSSI = rssi;
        this.mTimestamp = timestamp;
        this.mLocation = location;
        this.mObservationCollection = observationCollection;
    }

    public String getAddress() { return mAddress; }

    public String getFullName() {
        return mFullName;
    }

    public int getRSSI() { return mRSSI; }

    public int getTimestamp() {
        return mTimestamp;
    }

    public Location getLocation() { return mLocation; }

    public ObservationCollection getObservationCollection() {
        return mObservationCollection;
    }

    public void setAddress(String address) {
        this.mAddress = address;
    }

    public void setFullName(String fullName) {
        this.mFullName = fullName;
    }

    public void setTimestamp(int timestamp) {
        this.mTimestamp = timestamp;
    }

    public void setRSSI(int rssi) {
        this.mRSSI = rssi;
    }

    public void setLocation(Location location) {
        this.mLocation = location;
    }

    public void setObservationCollection(ObservationCollection observationCollection) {
        this.mObservationCollection = observationCollection;
    }

}
