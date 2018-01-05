package com.igrow.android;

import android.annotation.SuppressLint;
import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.VisibleForTesting;

import com.igrow.android.data.source.EnvironmentalSensorsRepository;
import com.igrow.android.sensors.EnvironmentalSensorsViewModel;

/**
 * Created by jsr on 2/01/18.
 */

/**
 * A creator is used to inject the product ID into the ViewModel
 * <p>
 * This creator is to showcase how to inject dependencies into ViewModels. It's not
 * actually necessary in this case, as the product ID can be passed in a public method.
 */
public class ViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    @SuppressLint("StaticFieldLeak")
    private static volatile ViewModelFactory INSTANCE;

    private final Application mApplication;

    private final EnvironmentalSensorsRepository mEnvironmentalSensorsRepository;

    public static ViewModelFactory getInstance(Application application) {

        if (INSTANCE == null) {
            synchronized (ViewModelFactory.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ViewModelFactory(application,
                            Injection.provideEnvironmentalSensorsRepository(application.getApplicationContext()));
                }
            }
        }
        return INSTANCE;
    }

    @VisibleForTesting
    public static void destroyInstance() {
        INSTANCE = null;
    }

    private ViewModelFactory(Application application, EnvironmentalSensorsRepository repository) {
        mApplication = application;
        mEnvironmentalSensorsRepository = repository;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(EnvironmentalSensorsViewModel.class)) {
            //noinspection unchecked
            return (T) new EnvironmentalSensorsViewModel(mApplication, mEnvironmentalSensorsRepository);
        } /*else if (modelClass.isAssignableFrom(TaskDetailViewModel.class)) {
            //noinspection unchecked
            return (T) new TaskDetailViewModel(mApplication, mEnvironmentalSensorsRepository);
        } else if (modelClass.isAssignableFrom(AddEditTaskViewModel.class)) {
            //noinspection unchecked
            return (T) new AddEditTaskViewModel(mApplication, mEnvironmentalSensorsRepository);
        } else if (modelClass.isAssignableFrom(EnvironmentalSensorsViewModel.class)) {
            //noinspection unchecked
            return (T) new EnvironmentalSensorsViewModel(mApplication, mEnvironmentalSensorsRepository);
        }*/
        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }
}

