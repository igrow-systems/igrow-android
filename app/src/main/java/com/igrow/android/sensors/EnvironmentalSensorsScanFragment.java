package com.igrow.android.sensors;

import android.content.Context;
import android.databinding.ObservableList;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.igrow.android.SnackbarMessage;
import com.igrow.android.data.EnvironmentalSensor;
import com.igrow.android.databinding.SensorsScanFragBinding;
import com.igrow.android.sensordetail.EnvironmentalSensorDetailFragment;
import com.igrow.android.util.SnackbarUtils;

/**
 * A list fragment representing a list of EnvironmentalSensors. This fragment
 * also supports tablet devices by allowing list items to be given an
 * 'activated' state upon selection. This helps indicate which item is
 * currently being viewed in a {@link EnvironmentalSensorDetailFragment}.
 * <p/>
 */
public class EnvironmentalSensorsScanFragment extends Fragment {

    private static final String TAG = EnvironmentalSensorsScanFragment.class.getSimpleName();
    /**
     * The serialization (saved instance state) Bundle key representing the
     * activated item position. Only used on tablets.
     */
    private static final String STATE_ACTIVATED_POSITION = "activated_position";

    protected RecyclerView mRecyclerView;

    protected EnvironmentalSensorsScanViewModel mViewModel;

    protected SensorsScanFragBinding mSensorsScanFragBinding;

    /**
     * The current activated item position. Only used on tablets.
     */
    private int mActivatedPosition = ListView.INVALID_POSITION;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public EnvironmentalSensorsScanFragment() {
    }

    public static EnvironmentalSensorsScanFragment newInstance() {
        return new EnvironmentalSensorsScanFragment();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setupSnackbar();

        setupFab();

        setupRecyclerViewAdapter();

        //setupRefreshLayout();

    }


    private void setupSnackbar() {
        mViewModel.getSnackbarMessage().observe(this, new SnackbarMessage.SnackbarObserver() {
            @Override
            public void onNewMessage(@StringRes int snackbarMessageResourceId) {
                SnackbarUtils.showSnackbar(getView(), getString(snackbarMessageResourceId));
            }
        });
    }

    private void setupFab() {
        //FloatingActionButton fab =
        //        (FloatingActionButton) getActivity().findViewById(R.id.fab_environmentalsensor_list);

        //fab.setImageResource(R.drawable.ic_add);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mTasksViewModel.addNewTask();
//            }
//        });
    }

    private void setupRecyclerViewAdapter() {

        mRecyclerView = mSensorsScanFragBinding.sensorsRecyclerview;

        mViewModel.sensors.addOnListChangedCallback(new ObservableList.OnListChangedCallback<ObservableList<EnvironmentalSensor>>() {
            @Override
            public void onChanged(ObservableList<EnvironmentalSensor> environmentalSensors) {
                mViewModel.getAdapter().setItems(mViewModel.sensors);
            }

            @Override
            public void onItemRangeChanged(ObservableList<EnvironmentalSensor> environmentalSensors, int i, int i1) {

            }

            @Override
            public void onItemRangeInserted(ObservableList<EnvironmentalSensor> environmentalSensors, int i, int i1) {
                mViewModel.getAdapter().setItems(mViewModel.sensors);
            }

            @Override
            public void onItemRangeMoved(ObservableList<EnvironmentalSensor> environmentalSensors, int i, int i1, int i2) {

            }

            @Override
            public void onItemRangeRemoved(ObservableList<EnvironmentalSensor> environmentalSensors, int i, int i1) {

            }
        });

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mSensorsScanFragBinding = SensorsScanFragBinding.inflate(inflater, container, false);

        mViewModel = EnvironmentalSensorsScanActivity.obtainViewModel(getActivity());

        mSensorsScanFragBinding.setViewmodel(mViewModel);

        setHasOptionsMenu(true);

        return mSensorsScanFragBinding.getRoot();

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Restore the previously serialized activated item position.
        if (savedInstanceState != null
                && savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
            //setActivatedPosition(savedInstanceState.getInt(STATE_ACTIVATED_POSITION));
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onResume() {
        super.onResume();
        mViewModel.start();
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mActivatedPosition != ListView.INVALID_POSITION) {
            // Serialize and persist the activated item position.
            outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
        }
    }

}
