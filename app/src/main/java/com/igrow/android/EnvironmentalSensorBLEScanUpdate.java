package com.igrow.android;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jsr on 16/04/2017.
 */

public class EnvironmentalSensorBLEScanUpdate implements Parcelable {

    private String mAddress;

    private int mRSSI;

    public EnvironmentalSensorBLEScanUpdate(String address, int RSSI) {
        this.mAddress = address;
        this.mRSSI = RSSI;
    }

    public EnvironmentalSensorBLEScanUpdate(String address) {
        this.mAddress = address;
    }

    public String getAddress() {
        return mAddress;
    }

    public int getRSSI() {
        return mRSSI;
    }

    public void setRSSI(int RSSI) {
        this.mRSSI = RSSI;
    }

    /* everything below here is for implementing Parcelable */

    // 99.9% of the time you can just ignore this
    @Override
    public int describeContents() {
        return 0;
    }

    // write your object's data to the passed-in Parcel
    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(mAddress);
        out.writeInt(mRSSI);
    }

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<EnvironmentalSensorBLEScanUpdate> CREATOR = new Parcelable.Creator<EnvironmentalSensorBLEScanUpdate>() {
        public EnvironmentalSensorBLEScanUpdate createFromParcel(Parcel in) {
            return new EnvironmentalSensorBLEScanUpdate(in);
        }

        public EnvironmentalSensorBLEScanUpdate[] newArray(int size) {
            return new EnvironmentalSensorBLEScanUpdate[size];
        }
    };

    // example constructor that takes a Parcel and gives you an object populated with it's values
    private EnvironmentalSensorBLEScanUpdate(Parcel in) {
        mAddress = in.readString();
        mRSSI = in.readInt();
    }
}
