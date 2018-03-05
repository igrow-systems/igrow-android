package com.igrow.android.sensors;

import com.igrow.android.R;
import com.igrow.android.data.EnvironmentalSensor;
import com.igrow.android.recyclerview.RecyclerViewAdapter;
import com.igrow.android.sensor.EnvironmentalSensorViewModel;

import java.util.ArrayList;

public class EnvironmentalSensorsRecyclerViewAdapter
        extends RecyclerViewAdapter<EnvironmentalSensor, EnvironmentalSensorViewModel> {

    interface OnItemClickListener extends RecyclerViewAdapter.OnItemClickListener<EnvironmentalSensor> {
        void onItemClick(EnvironmentalSensor item);
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public EnvironmentalSensorsRecyclerViewAdapter(ArrayList<EnvironmentalSensor> dataSet,
                                                   OnItemClickListener listener) {
        super();
        mListener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.sensor_item;
    }
}