package com.igrow.android;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class EnvironmentalSensorListAdapter
        extends RecyclerView.Adapter<EnvironmentalSensorListAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(DummyContent.DummyItem item);
    }

    private DummyContent mDataset;

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

        public void bind(final DummyContent.DummyItem item, final OnItemClickListener listener) {

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
    public EnvironmentalSensorListAdapter(OnItemClickListener listener) {
        mDataset = new DummyContent();
        mListener = listener;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public EnvironmentalSensorListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(com.igrow.android.R.layout.listitem_device, parent, false);
        // set the view's size, margins, paddings and layout parameters

        //CombinedChart c = (CombinedChart)v.findViewById(R.id.chart);
        ViewHolder vh = new ViewHolder((LinearLayout) v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        DummyContent.DummyItem item = mDataset.ITEMS.get(position);

        // - bind this ViewHolder to the event listener
        holder.bind(item, mListener);

        // - replace the contents of the view with that element
        holder.mNameTextView.setText(item.content);
        holder.mAddressTextView.setText(item.content);



    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.ITEMS.size();
    }


}