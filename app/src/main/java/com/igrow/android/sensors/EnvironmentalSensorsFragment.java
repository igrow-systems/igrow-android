package com.igrow.android.sensors;

import android.content.Context;
import android.databinding.ObservableList;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.igrow.android.R;
import com.igrow.android.SnackbarMessage;
import com.igrow.android.data.EnvironmentalSensor;
import com.igrow.android.databinding.SensorsFragBinding;
import com.igrow.android.util.SnackbarUtils;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * <p>
 * Use the {@link EnvironmentalSensorsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EnvironmentalSensorsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private EnvironmentalSensorsViewModel mSensorsViewModel;

    private SensorsFragBinding mSensorsFragBinding;

    private EnvironmentalSensorsRecyclerViewAdapter mSensorsRecyclerViewAdapter;

    public EnvironmentalSensorsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EnvironmentalSensorsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EnvironmentalSensorsFragment newInstance(String param1, String param2) {
        EnvironmentalSensorsFragment fragment = new EnvironmentalSensorsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mSensorsFragBinding = SensorsFragBinding.inflate(inflater, container, false);

        mSensorsViewModel = EnvironmentalSensorsActivity.obtainViewModel(getActivity());

        mSensorsFragBinding.setViewmodel(mSensorsViewModel);

        setHasOptionsMenu(true);

        return mSensorsFragBinding.getRoot();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
//            case R.id.menu_clear:
//                mTasksViewModel.clearCompletedTasks();
//                break;
//            case R.id.menu_filter:
//                showFilteringPopUpMenu();
//                break;
            case R.id.menu_refresh:
                mSensorsViewModel.loadEnvironmentalSensors(true);
                break;
        }
        return true;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.sensors_fragment_menu, menu);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setupSnackbar();

        setupFab();

        setupRecyclerViewAdapter();

        //setupRefreshLayout();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onResume() {
        super.onResume();
        mSensorsViewModel.start();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void setupSnackbar() {
        mSensorsViewModel.getSnackbarMessage().observe(this, new SnackbarMessage.SnackbarObserver() {
            @Override
            public void onNewMessage(@StringRes int snackbarMessageResourceId) {
                SnackbarUtils.showSnackbar(getView(), getString(snackbarMessageResourceId));
            }
        });
    }

//    private void showFilteringPopUpMenu() {
//        PopupMenu popup = new PopupMenu(getContext(), getActivity().findViewById(R.id.menu_filter));
//        popup.getMenuInflater().inflate(R.menu.filter_tasks, popup.getMenu());
//
//        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//            public boolean onMenuItemClick(MenuItem item) {
//                switch (item.getItemId()) {
//                    case R.id.active:
//                        mTasksViewModel.setFiltering(TasksFilterType.ACTIVE_TASKS);
//                        break;
//                    case R.id.completed:
//                        mTasksViewModel.setFiltering(TasksFilterType.COMPLETED_TASKS);
//                        break;
//                    default:
//                        mTasksViewModel.setFiltering(TasksFilterType.ALL_TASKS);
//                        break;
//                }
//                mTasksViewModel.loadTasks(false);
//                return true;
//            }
//        });
//
//        popup.show();
//    }

    private void setupFab() {
        FloatingActionButton fab =
                (FloatingActionButton) getActivity().findViewById(R.id.fab_add_sensor);

        fab.setImageResource(R.drawable.ic_add_circle_white_24dp);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSensorsViewModel.addNewSensor();
            }
        });
    }

    private void setupRecyclerViewAdapter() {
        RecyclerView sensorsRecyclerView = mSensorsFragBinding.sensorsRecyclerview;

//        mSensorsRecyclerViewAdapter = new EnvironmentalSensorsRecyclerViewAdapter(
//                new ArrayList<>(0),
//                new EnvironmentalSensorsRecyclerViewAdapter.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(EnvironmentalSensor item) {
//                        mSensorsViewModel.openEnvironmentalSensor(item.getSensorId());
//                    }
//                }
//        );
        sensorsRecyclerView.setAdapter(mSensorsViewModel.getAdapter());

        mSensorsViewModel.sensors.addOnListChangedCallback(new ObservableList.OnListChangedCallback<ObservableList<EnvironmentalSensor>>() {
            @Override
            public void onChanged(ObservableList<EnvironmentalSensor> environmentalSensors) {
                mSensorsViewModel.getAdapter().setItems(mSensorsViewModel.sensors);
            }

            @Override
            public void onItemRangeChanged(ObservableList<EnvironmentalSensor> environmentalSensors, int i, int i1) {

            }

            @Override
            public void onItemRangeInserted(ObservableList<EnvironmentalSensor> environmentalSensors, int i, int i1) {
                mSensorsViewModel.getAdapter().setItems(mSensorsViewModel.sensors);
            }

            @Override
            public void onItemRangeMoved(ObservableList<EnvironmentalSensor> environmentalSensors, int i, int i1, int i2) {

            }

            @Override
            public void onItemRangeRemoved(ObservableList<EnvironmentalSensor> environmentalSensors, int i, int i1) {

            }
        });
    }

//    private void setupRefreshLayout() {
//        ListView listView =  mTasksFragBinding.tasksList;
//        final ScrollChildSwipeRefreshLayout swipeRefreshLayout = mTasksFragBinding.refreshLayout;
//        swipeRefreshLayout.setColorSchemeColors(
//                ContextCompat.getColor(getActivity(), R.color.colorPrimary),
//                ContextCompat.getColor(getActivity(), R.color.colorAccent),
//                ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark)
//        );
//        // Set the scrolling view in the custom SwipeRefreshLayout.
//        swipeRefreshLayout.setScrollUpChild(listView);
//    }

}
