package com.igrow.android.sensors;

public class EnvironmentalSensorAddSensorEventArgs {


    private String mAddress;

    private String mName;

    public EnvironmentalSensorAddSensorEventArgs(String address, String name) {
        this.mAddress = address;
        this.mName = name;
    }

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String address) {
        this.mAddress = address;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }


}
