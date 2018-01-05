package com.igrow.android.sensors;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.content.Context;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableList;

import com.igrow.android.R;
import com.igrow.android.SingleLiveEvent;
import com.igrow.android.SnackbarMessage;
import com.igrow.android.addeditsensor.EnvironmentalSensorAddEditActivity;
import com.igrow.android.data.EnvironmentalSensor;
import com.igrow.android.data.source.EnvironmentalSensorsDataSource;
import com.igrow.android.data.source.EnvironmentalSensorsRepository;
import com.igrow.android.sensordetail.EnvironmentalSensorDetailActivity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by jsr on 2/01/18.
 */

public class EnvironmentalSensorsViewModel extends AndroidViewModel {

    // These observable fields will update Views automatically
    public final ObservableList<EnvironmentalSensor> sensors = new ObservableArrayList<>();

    public final ObservableBoolean dataLoading = new ObservableBoolean(false);

    public final ObservableBoolean empty = new ObservableBoolean(false);

    public final ObservableBoolean tasksAddViewVisible = new ObservableBoolean();

    //private MutableLiveData<List<EnvironmentalSensor>> mEnvironmentalSensorList;

    private final EnvironmentalSensorsRepository mEnvironmentalSensorsRepository;

    private final ObservableBoolean mIsDataLoadingError = new ObservableBoolean(false);

    private final SnackbarMessage mSnackbarText = new SnackbarMessage();

    private final SingleLiveEvent<String> mOpenEnvironmentalSensorEvent = new SingleLiveEvent<>();

    private final SingleLiveEvent<Void> mNewEnvironmentalSensorEvent = new SingleLiveEvent<>();

    private final Context mContext;

    @Inject
    public EnvironmentalSensorsViewModel(Application context, EnvironmentalSensorsRepository environmentalSensorsRepository) {
        super(context);
        this.mContext = context.getApplicationContext();
        this.mEnvironmentalSensorsRepository = environmentalSensorsRepository;
    }

    public void start() {
        loadEnvironmentalSensors(false);
    }

    public void loadEnvironmentalSensors(boolean forceUpdate) {
        loadEnvironmentalSensors(forceUpdate, true);
    }

    SnackbarMessage getSnackbarMessage() {
        return mSnackbarText;
    }

    SingleLiveEvent<String> getOpenEnvironmentalSensorEvent() {
        return mOpenEnvironmentalSensorEvent;
    }

    SingleLiveEvent<Void> getNewEnvironmentalSensorEvent() {
        return mNewEnvironmentalSensorEvent;
    }

    private void showSnackbarMessage(Integer message) {
        mSnackbarText.setValue(message);
    }

    /**
     * Called by the Data Binding library and the FAB's click listener.
     */
    public void addNewEnvironmentalSensor() {
        mNewEnvironmentalSensorEvent.call();
    }

    void handleActivityResult(int requestCode, int resultCode) {
        if (EnvironmentalSensorAddEditActivity.REQUEST_CODE == requestCode) {
            switch (resultCode) {
                case EnvironmentalSensorDetailActivity.EDIT_RESULT_OK:
                    mSnackbarText.setValue(R.string.successfully_saved_sensor_message);
                    break;
                case EnvironmentalSensorAddEditActivity.ADD_EDIT_RESULT_OK:
                    mSnackbarText.setValue(R.string.successfully_added_sensor_message);
                    break;
                case EnvironmentalSensorDetailActivity.DELETE_RESULT_OK:
                    mSnackbarText.setValue(R.string.successfully_deleted_sensor_message);
                    break;
            }
        }
    }

    /**
     * @param forceUpdate   Pass in true to refresh the data in the {@link EnvironmentalSensorsDataSource}
     * @param showLoadingUI Pass in true to display a loading icon in the UI
     */
    private void loadEnvironmentalSensors(boolean forceUpdate, final boolean showLoadingUI) {
        if (showLoadingUI) {
            dataLoading.set(true);
        }
        if (forceUpdate) {

            mEnvironmentalSensorsRepository.refreshEnvironmentalSensors();
        }

        mEnvironmentalSensorsRepository.getEnvironmentalSensors(new EnvironmentalSensorsDataSource.LoadEnvironmentalSensorsCallback() {
            @Override
            public void onEnvironmentalSensorsLoaded(List<EnvironmentalSensor> environmentalSensors) {
                List<EnvironmentalSensor> environmentalSensorsToShow = new ArrayList<>();

                // We filter the environmental sensors based on the requestType
                for (EnvironmentalSensor environmentalSensor : environmentalSensors) {
//                    switch (mCurrentFiltering) {
//                        case ALL_TASKS:
//                            environmentalSensorsToShow.add(environmentalSensor);
//                            break;
//                        case ACTIVE_TASKS:
//                            if (environmentalSensor.isActive()) {
//                                environmentalSensorsToShow.add(environmentalSensor);
//                            }
//                            break;
//                        case COMPLETED_TASKS:
//                            if (environmentalSensor.isCompleted()) {
//                                environmentalSensorsToShow.add(environmentalSensor);
//                            }
//                            break;
//                        default:
//                            environmentalSensorsToShow.add(environmentalSensor);
//                            break;
//                    }
                }
                if (showLoadingUI) {
                    dataLoading.set(false);
                }
                mIsDataLoadingError.set(false);

                sensors.clear();
                sensors.addAll(environmentalSensorsToShow);
                empty.set(sensors.isEmpty());
            }

            @Override
            public void onDataNotAvailable() {
                mIsDataLoadingError.set(true);
            }
        });
    }
}
