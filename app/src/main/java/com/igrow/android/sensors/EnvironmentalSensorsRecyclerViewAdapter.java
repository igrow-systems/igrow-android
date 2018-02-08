package com.igrow.android.sensors;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.igrow.android.databinding.SensorItemBinding;
import com.igrow.android.R;
import com.igrow.android.data.EnvironmentalSensor;
import com.igrow.android.recyclerview.RecyclerViewAdapter;
import com.igrow.android.sensor.EnvironmentalSensorViewModel;

import java.util.ArrayList;
import java.util.List;

public class EnvironmentalSensorsRecyclerViewAdapter
        extends RecyclerViewAdapter<EnvironmentalSensor, EnvironmentalSensorViewModel> {

    public interface OnItemClickListener {
        void onItemClick(EnvironmentalSensor item);
    }

    protected OnItemClickListener mListener;



    // Provide a suitable constructor (depends on the kind of dataset)
    public EnvironmentalSensorsRecyclerViewAdapter(ArrayList<EnvironmentalSensor> dataSet,
                                                   OnItemClickListener listener) {
        super();
    }



}