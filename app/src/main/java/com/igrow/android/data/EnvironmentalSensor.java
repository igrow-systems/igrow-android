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

package com.igrow.android.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.location.Location;
import android.support.annotation.NonNull;

import com.google.common.base.Strings;
import com.igrow.android.data.source.local.IGrowTypeConverters;
import com.igrow.model.ObservationCollection;

import java.util.UUID;

/**
 * Created by jsr on 30/05/16.
 */
@Entity(tableName = "environmental_sensors",
        indices = {@Index(value = {"address"}, unique = true)})
//@TypeConverters({IGrowTypeConverters.class})
public class EnvironmentalSensor {

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "id")
    private UUID mSensorId;

    @ColumnInfo(name = "address")
    private String mAddress;

    @ColumnInfo(name = "full_name")
    private String mFullName;

    @ColumnInfo(name = "rssi")
    private int mRSSI;

    @ColumnInfo(name = "timestamp")
    private long mTimestamp;

    @ColumnInfo(name = "last_seq")
    private int mLastSequenceNum;

    //@ColumnInfo(name = "location")
    @Ignore
    private Location mLocation;

    @Ignore
    private ObservationCollection mObservationCollection;

    public static class EnvironmentalSensorBuilder {

        private UUID sensorid;

        private String address;

        private String fullName;

        private int rssi;

        private long timestamp;

        private int lastSequenceNum;

        private Location location;

        private ObservationCollection observationCollection;

        public EnvironmentalSensorBuilder setId(UUID sensorId) {
            this.sensorid = sensorId;
            return this;
        }

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

        public EnvironmentalSensorBuilder setLastSequenceNum(int lastSequenceNum) {
            this.lastSequenceNum = lastSequenceNum;
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
            if (sensorid == null) {
                return new EnvironmentalSensor(address, fullName, rssi,
                        timestamp, location, observationCollection);
            } else {
                return new EnvironmentalSensor(sensorid, address, fullName, rssi,
                        timestamp, lastSequenceNum,
                        location, observationCollection);
            }
        }
    }

    public EnvironmentalSensor(String address, String fullName, int rssi, long timestamp,
                               Location location, ObservationCollection observationCollection) {
        this.mSensorId = UUID.randomUUID();
        this.mAddress = address;
        this.mFullName = fullName;
        this.mRSSI = rssi;
        this.mTimestamp = timestamp;
        this.mLastSequenceNum = 0;  // TODO: 0 indicates we haven't yet read a value for this sensor
        // find a better way, an explicit state enum perhaps
        this.mLocation = location;
        this.mObservationCollection = observationCollection;
    }

    public EnvironmentalSensor(UUID sensorId,
                               String address,
                               String fullName,
                               int rssi,
                               long timestamp,
                               int lastSequenceNum,
                               Location location,
                               ObservationCollection observationCollection) {
        this.mSensorId = sensorId;
        this.mAddress = address;
        this.mFullName = fullName;
        this.mRSSI = rssi;
        this.mTimestamp = timestamp;
        this.mLastSequenceNum = lastSequenceNum;
        this.mLocation = location;
        this.mObservationCollection = observationCollection;
    }

    /**
     * satisfy the Room persistence framework
     */
    public EnvironmentalSensor() {

    }

    public UUID getSensorId() {
        return mSensorId;
    }

    public String getAddress() {
        return mAddress;
    }

    public String getFullName() {
        return mFullName;
    }

    public int getRSSI() {
        return mRSSI;
    }

    public long getTimestamp() {
        return mTimestamp;
    }

    public int getLastSequenceNum() { return mLastSequenceNum; }

    public Location getLocation() {
        return mLocation;
    }

    public ObservationCollection getObservationCollection() {
        return mObservationCollection;
    }

    public void setSensorId(UUID sensorId) {
        mSensorId = sensorId;
    }

    public void setAddress(String address) {
        this.mAddress = address;
    }

    public void setFullName(String fullName) {
        this.mFullName = fullName;
    }

    public void setTimestamp(long timestamp) {
        this.mTimestamp = timestamp;
    }

    public void setLastSequenceNum(int lastSequenceNum) { this.mLastSequenceNum = lastSequenceNum; }

    public void setRSSI(int rssi) {
        this.mRSSI = rssi;
    }

    public void setLocation(Location location) {
        this.mLocation = location;
    }

    public void setObservationCollection(ObservationCollection observationCollection) {
        this.mObservationCollection = observationCollection;
    }

    public boolean isEmpty() {
        return Strings.isNullOrEmpty(mAddress)
                && Strings.isNullOrEmpty(mFullName);
    }

}
