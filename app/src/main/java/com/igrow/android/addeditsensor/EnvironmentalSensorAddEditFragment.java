/*
 * Copyright 2016, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.igrow.android.addeditsensor;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.igrow.android.R;
import com.igrow.android.SnackbarMessage;
import com.igrow.android.databinding.AddEditSensorFragBinding;
import com.igrow.android.util.SnackbarUtils;

import java.util.UUID;


/**
 * Main UI for the add task screen. Users can enter a task title and description.
 */
public class EnvironmentalSensorAddEditFragment extends Fragment {

    public final static String ARGUMENT_EDIT_SENSOR_ID = "com.igrow.android.ARGUMENT_EDIT_SENSOR_ID";

    public final static String ARGUMENT_SENSOR_ADDRESS = "com.igrow.android.ARGUMENT_SENSOR_ADDRESS";

    public final static String ARGUMENT_SENSOR_NAME = "com.igrow.android.ARGUMENT_SENSOR_NAME";

    private EnvironmentalSensorAddEditViewModel mViewModel;

    private AddEditSensorFragBinding mViewDataBinding;

    public static EnvironmentalSensorAddEditFragment newInstance() {
        return new EnvironmentalSensorAddEditFragment();
    }

    public EnvironmentalSensorAddEditFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setupFab();

        setupSnackbar();

        setupActionBar();

        loadData();
    }

    private void loadData() {
        // Add or edit an existing sensor?
        if (getArguments() != null
                && getArguments().get(ARGUMENT_EDIT_SENSOR_ID) != null) {
            String uuidString = getArguments().getString(ARGUMENT_EDIT_SENSOR_ID);
            mViewModel.start(UUID.fromString(uuidString));
        } else {
            mViewModel.start(null);
            mViewModel.address.setValue(getArguments().getString(ARGUMENT_SENSOR_ADDRESS));
            mViewModel.fullname.setValue(getArguments().getString(ARGUMENT_SENSOR_NAME));
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.add_edit_sensor_frag, container, false);
        if (mViewDataBinding == null) {
            mViewDataBinding = AddEditSensorFragBinding.bind(root);
        }

        mViewModel = EnvironmentalSensorAddEditActivity.obtainViewModel(getActivity());

        mViewDataBinding.setViewmodel(mViewModel);

        setHasOptionsMenu(true);
        setRetainInstance(false);

        return mViewDataBinding.getRoot();
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
        FloatingActionButton fab = getActivity().findViewById(R.id.fab_edit_sensor_done);
        fab.setImageResource(R.drawable.ic_done);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.saveSensor();
            }
        });
    }

    private void setupActionBar() {
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        if (actionBar == null) {
            return;
        }
        if (getArguments() != null && getArguments().get(ARGUMENT_EDIT_SENSOR_ID) != null) {
            actionBar.setTitle(R.string.edit_sensor);
        } else {
            actionBar.setTitle(R.string.add_sensor);
        }
    }
}
