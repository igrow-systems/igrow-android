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

package com.igrow.android.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.location.Location;

import com.igrow.model.ObservationCollection;

/**
 * Created by jsr on 30/05/16.
 */
@Entity(tableName = "environmental_sensors",
        indices = {@Index("address"),
        @Index(value = {"address"}, unique = true)})
public class EnvironmentalSensor {

    @PrimaryKey
    @ColumnInfo(name = "address")
    private String mAddress;

    @ColumnInfo(name = "full_name")
    private String mFullName;

    @ColumnInfo(name = "rssi")
    private int mRSSI;

    @ColumnInfo(name = "timestamp")
    private int mTimestamp;

    @ColumnInfo(name = "location")
    private Location mLocation;

    @Ignore
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

    public String getId() { return mAddress; }

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
