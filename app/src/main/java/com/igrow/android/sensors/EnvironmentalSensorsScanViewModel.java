package com.igrow.android.sensors;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.content.Context;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableList;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.igrow.android.R;
import com.igrow.android.SingleLiveEvent;
import com.igrow.android.SnackbarMessage;
import com.igrow.android.addeditsensor.EnvironmentalSensorAddEditActivity;
import com.igrow.android.bluetooth.EnvironmentalSensorBLEScanUpdate;
import com.igrow.android.data.EnvironmentalSensor;
import com.igrow.android.data.source.EnvironmentalSensorsDataSource;
import com.igrow.android.data.source.EnvironmentalSensorsRepository;
import com.igrow.android.recyclerview.RecyclerViewAdapter;
import com.igrow.android.recyclerview.RecyclerViewViewModel;
import com.igrow.android.sensor.EnvironmentalSensorCollection;
import com.igrow.android.sensordetail.EnvironmentalSensorDetailActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

/**
 * Created by jsr on 2/01/18.
 */

public class EnvironmentalSensorsScanViewModel extends RecyclerViewViewModel {

    // These observable fields will update Views automatically
    public final ObservableList<EnvironmentalSensor> sensors = new ObservableArrayList<>();

    public final ObservableBoolean dataLoading = new ObservableBoolean(false);

    public final ObservableField<String> currentFilteringLabel = new ObservableField<>();

    public final ObservableField<String> noSensorsLabel = new ObservableField<>();

    public final ObservableField<Drawable> noSensorIconRes = new ObservableField<>();

    public final ObservableBoolean empty = new ObservableBoolean(false);

    public final ObservableBoolean sensorsAddViewVisible = new ObservableBoolean();

    private final EnvironmentalSensorsRepository mEnvironmentalSensorsRepository;

    private final ObservableBoolean mIsDataLoadingError = new ObservableBoolean(false);

    private final SnackbarMessage mSnackbarText = new SnackbarMessage();

    private EnvironmentalSensorsFilterType mCurrentFiltering = EnvironmentalSensorsFilterType.ALL_SENSORS;

    private final SingleLiveEvent<EnvironmentalSensorAddSensorEventArgs> mAddEnvironmentalSensorEvent = new SingleLiveEvent<>();

    private final SingleLiveEvent<Void> mNewEnvironmentalSensorEvent = new SingleLiveEvent<>();

    private final SingleLiveEvent<Void> mUpdateEnvironmentalSensorEvent = new SingleLiveEvent<>();

    private final EnvironmentalSensorsRecyclerViewAdapter mAdapter;

    private final Context mContext;

    private int mCurrentSelectionIndex;

    @Inject
    public EnvironmentalSensorsScanViewModel(Application context, EnvironmentalSensorsRepository environmentalSensorsRepository) {
        super(context);
        this.mContext = context.getApplicationContext();
        this.mEnvironmentalSensorsRepository = environmentalSensorsRepository;
        this.mAdapter = new EnvironmentalSensorsRecyclerViewAdapter(new ArrayList<>(0),
                new EnvironmentalSensorsRecyclerViewAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(EnvironmentalSensor sensor) {
                        mCurrentSelectionIndex = sensors.indexOf(sensor);
                        EnvironmentalSensorAddSensorEventArgs eventArgs = new EnvironmentalSensorAddSensorEventArgs(
                                sensor.getAddress(),
                                sensor.getFullName());
                        mAddEnvironmentalSensorEvent.setValue(eventArgs);
                    }
                });
    }

    public void start() {
        loadEnvironmentalSensors(false);
    }

    public void loadEnvironmentalSensors(boolean forceUpdate) {
        loadEnvironmentalSensors(forceUpdate, true);
    }

    /**
     * Sets the current task filtering type.
     *
     * @param requestType Can be {@link EnvironmentalSensorsFilterType#ALL_SENSORS},
     *                    {@link EnvironmentalSensorsFilterType#COMPLETED_SENSORS}, or
     *                    {@link EnvironmentalSensorsFilterType#ACTIVE_SENSORS}
     */
    public void setFiltering(EnvironmentalSensorsFilterType requestType) {
        mCurrentFiltering = requestType;

        // Depending on the filter type, set the filtering label, icon drawables, etc.
        switch (requestType) {
            case ALL_SENSORS:
                currentFilteringLabel.set(mContext.getString(R.string.label_all));
                noSensorsLabel.set(mContext.getResources().getString(R.string.no_sensors_all));
                noSensorIconRes.set(getApplication().getApplicationContext().getResources().getDrawable(
                        R.drawable.ic_assignment_turned_in_24dp));
                sensorsAddViewVisible.set(true);
                break;
            case ACTIVE_SENSORS:
                currentFilteringLabel.set(mContext.getString(R.string.label_active));
                noSensorsLabel.set(mContext.getResources().getString(R.string.no_sensors_active));
                noSensorIconRes.set(getApplication().getApplicationContext().getResources().getDrawable(
                        R.drawable.ic_check_circle_24dp));
                sensorsAddViewVisible.set(false);
                break;
//            case COMPLETED_SENSORS:
//                currentFilteringLabel.set(mContext.getString(R.string.label_completed));
//                noSensorsLabel.set(mContext.getResources().getString(R.string.no_sensors_completed));
//                noSensorIconRes.set(mContext.getResources().getDrawable(
//                        R.drawable.ic_verified_user_24dp));
//                sensorsAddViewVisible.set(false);
//                break;
        }
    }

    SnackbarMessage getSnackbarMessage() {
        return mSnackbarText;
    }

    SingleLiveEvent<EnvironmentalSensorAddSensorEventArgs> getAddEnvironmentalSensorEvent() {
        return mAddEnvironmentalSensorEvent;
    }

    SingleLiveEvent<Void> getNewEnvironmentalSensorEvent() {
        return mNewEnvironmentalSensorEvent;
    }

    SingleLiveEvent<Void> getUpdateEnvironmentalSensorEvent() {
        return mUpdateEnvironmentalSensorEvent;
    }

    private void showSnackbarMessage(Integer message) {
        mSnackbarText.setValue(message);
    }

    /**
     * Called by the Data Binding library and the FAB's click listener.
     */
    public void handleEnvironmentalSensorUpdate(EnvironmentalSensorBLEScanUpdate sensorScanUpdate) {

        EnvironmentalSensor existingSensor = null;
        // TODO: we ought to keep an associative container updated in line with the list
        // TODO: think about a RecyclerViewAdapter not backed by a list, but ordered associative container type
        for (EnvironmentalSensor sensor : sensors) {
            if (sensor.getAddress().equals(sensorScanUpdate.getAddress())) {
                existingSensor = sensor;
            }
        }

        if (existingSensor != null) {

            existingSensor.setRSSI(sensorScanUpdate.getRSSI());

            mUpdateEnvironmentalSensorEvent.call();

        } else {
            EnvironmentalSensor sensor = new EnvironmentalSensor.EnvironmentalSensorBuilder()
                    .setAddress(sensorScanUpdate.getAddress())
                    .setFullName("Unknown sensor")
                    .setRssi(sensorScanUpdate.getRSSI()).build();

            sensors.add(sensor);
            empty.set(false);

            mNewEnvironmentalSensorEvent.call();
        }


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
                    environmentalSensorsToShow.add(environmentalSensor);
                    sensorsAddViewVisible.set(true);
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

    @Override
    protected RecyclerViewAdapter getAdapter() {
        return mAdapter;
    }

    @Override
    protected RecyclerView.LayoutManager createLayoutManager() {
        return new LinearLayoutManager(mContext);
    }
}
