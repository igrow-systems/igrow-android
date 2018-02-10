package com.igrow.android.sensors;

import android.app.Activity;
import android.content.Context;
import android.databinding.ObservableList;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.igrow.android.SnackbarMessage;
import com.igrow.android.databinding.SensorsFragBinding;
import com.igrow.android.R;
import com.igrow.android.data.EnvironmentalSensor;
import com.igrow.android.sensordetail.EnvironmentalSensorDetailFragment;
import com.igrow.android.util.SnackbarUtils;

import java.util.ArrayList;

/**
 * A list fragment representing a list of EnvironmentalSensors. This fragment
 * also supports tablet devices by allowing list items to be given an
 * 'activated' state upon selection. This helps indicate which item is
 * currently being viewed in a {@link EnvironmentalSensorDetailFragment}.
 * <p/>
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public class EnvironmentalSensorsFragment extends Fragment {

    private static final String TAG = EnvironmentalSensorsFragment.class.getSimpleName();
    /**
     * The serialization (saved instance state) Bundle key representing the
     * activated item position. Only used on tablets.
     */
    private static final String STATE_ACTIVATED_POSITION = "activated_position";
    /**
     * A dummy implementation of the {@link Callbacks} interface that does
     * nothing. Used only when this fragment is not attached to an activity.
     */
    private static Callbacks sDummyCallbacks = new Callbacks() {
        @Override
        public void onItemSelected(String id) {
        }
    };

    protected RecyclerView mRecyclerView;

    protected RecyclerView.LayoutManager mLayoutManager;

    protected EnvironmentalSensorsScanViewModel mViewModel;

    protected SensorsFragBinding mSensorsFragBinding;

    protected EnvironmentalSensorsRecyclerViewAdapter mListAdapter;
    /**
     * The fragment's current callback object, which is notified of list item
     * clicks.
     */
    private Callbacks mCallbacks = sDummyCallbacks;
    /**
     * The current activated item position. Only used on tablets.
     */
    private int mActivatedPosition = ListView.INVALID_POSITION;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public EnvironmentalSensorsFragment() {
    }

    public static EnvironmentalSensorsFragment newInstance() {
        return new EnvironmentalSensorsFragment();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //String userId = getArguments().getString(UID_KEY);


        setupSnackbar();

        setupFab();

        setupListAdapter();

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
        FloatingActionButton fab =
                (FloatingActionButton) getActivity().findViewById(R.id.fab_environmentalsensor_list);

        //fab.setImageResource(R.drawable.ic_add);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mTasksViewModel.addNewTask();
//            }
//        });
    }

    private void setupListAdapter() {

        mRecyclerView = mSensorsFragBinding.sensorsRecyclerview;

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

        mSensorsFragBinding = SensorsFragBinding.inflate(inflater, container, false);

        mViewModel = EnvironmentalSensorsScanActivity.obtainViewModel(getActivity());

        mSensorsFragBinding.setViewmodel(mViewModel);

        setHasOptionsMenu(true);

        return mSensorsFragBinding.getRoot();

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

        Activity activity = getActivity();

        // Activities containing this fragment must implement its callbacks.
        if (!(activity instanceof Callbacks)) {
            throw new IllegalStateException("Activity must implement fragment's callbacks.");
        }

        mCallbacks = (Callbacks) activity;
    }

    @Override
    public void onResume() {
        super.onResume();
        mViewModel.start();
    }

    @Override
    public void onDetach() {
        super.onDetach();

        // Reset the active callbacks interface to the dummy implementation.
        mCallbacks = sDummyCallbacks;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mActivatedPosition != ListView.INVALID_POSITION) {
            // Serialize and persist the activated item position.
            outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
        }
    }

    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public interface Callbacks {
        /**
         * Callback for when an item has been selected.
         */
        void onItemSelected(String id);
    }

}
