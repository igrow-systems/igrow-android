package com.igrow.android.sensors;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.igrow.android.EnvironmentalSensorCollection;
import com.igrow.android.R;
import com.igrow.android.data.EnvironmentalSensor;

public class EnvironmentalSensorsListAdapter
        extends RecyclerView.Adapter<EnvironmentalSensorsListAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(EnvironmentalSensor item);
    }

    private EnvironmentalSensorCollection mDataset;

    protected OnItemClickListener mListener;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mNameTextView;

        public TextView mAddressTextView;

        public ViewHolder(LinearLayout v) {
            super(v);
            mNameTextView = (TextView)v.findViewById(R.id.textview_device_name);
            mAddressTextView = (TextView)v.findViewById(R.id.textview_device_address);
        }

        public void bind(final EnvironmentalSensor item, final OnItemClickListener listener) {

            mNameTextView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });

            mAddressTextView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public EnvironmentalSensorsListAdapter(EnvironmentalSensorCollection dataSet,
                                           OnItemClickListener listener) {
        mDataset = dataSet;
        mListener = listener;
    }

    public void invalidate() {
        notifyDataSetChanged();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public EnvironmentalSensorsListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                         int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(com.igrow.android.R.layout.listitem_device, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder((LinearLayout) v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        EnvironmentalSensor item = mDataset.ITEMS.get(position);

        // - bind this ViewHolder to the event listener
        holder.bind(item, mListener);

        // - replace the contents of the view with that element
        holder.mNameTextView.setText(item.getFullName());
        holder.mAddressTextView.setText(item.getAddress());



    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.ITEMS.size();
    }


}