package com.igrow.android.listsensors;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.igrow.android.data.EnvironmentalSensor;
import com.igrow.android.data.source.EnvironmentalSensorsRepository;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by jsr on 2/01/18.
 */

public class EnvironmentalSensorListViewModel extends ViewModel {

    private MutableLiveData<List<EnvironmentalSensor>> mEnvironmentalSensorList;

    private EnvironmentalSensorsRepository mEnvironmentalSensorsRepository;

    @Inject
    public EnvironmentalSensorListViewModel(EnvironmentalSensorsRepository environmentalSensorsRepository) {
        this.mEnvironmentalSensorsRepository = environmentalSensorsRepository;
    }

    public void init(String userId) {

    }

    public MutableLiveData<List<EnvironmentalSensor>> getEnvironmentalSensorList() {
        return mEnvironmentalSensorList;
    }
}
