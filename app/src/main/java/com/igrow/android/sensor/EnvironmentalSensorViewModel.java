package com.igrow.android.sensor;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.igrow.android.data.EnvironmentalSensor;
import com.igrow.android.recyclerview.ItemViewModel;

/**
 * Created by jsr on 15/04/2017.
 */

public class EnvironmentalSensorViewModel extends ItemViewModel<EnvironmentalSensor> {

    public MutableLiveData<EnvironmentalSensor> sensor;

    public MutableLiveData<String> fullname = new MutableLiveData<>();

    public MutableLiveData<String> address = new MutableLiveData<>();

    public MutableLiveData<Integer> rssi = new MutableLiveData<>();

    public EnvironmentalSensorViewModel() {
        sensor = new MutableLiveData<>();
    }

    @Override
    public void setItem(EnvironmentalSensor item) {
        sensor.setValue(item);
        fullname.setValue(item.getFullName());
        address.setValue(item.getAddress());
        rssi.setValue(item.getRSSI());
    }
}
