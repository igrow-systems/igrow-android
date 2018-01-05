package com.igrow.android;

import android.content.Context;
import android.support.annotation.NonNull;

import com.igrow.android.data.FakeEnvironmentalSensorsRemoteDataSource;
import com.igrow.android.data.source.EnvironmentalSensorsDataSource;
import com.igrow.android.data.source.EnvironmentalSensorsRepository;
import com.igrow.android.data.source.local.EnvironmentalSensorsLocalDataSource;
import com.igrow.android.data.source.local.IGrowDatabase;
import com.igrow.android.util.AppExecutors;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Enables injection of mock implementations for
 * {@link EnvironmentalSensorsDataSource} at compile time. This is useful for testing, since it allows us to use
 * a fake instance of the class to isolate the dependencies and run a test hermetically.
 */
public class Injection {

    public static EnvironmentalSensorsRepository provideEnvironmentalSensorsRepository(@NonNull Context context) {
        checkNotNull(context);
        IGrowDatabase database = IGrowDatabase.getInstance(context);
        return EnvironmentalSensorsRepository.getInstance(FakeEnvironmentalSensorsRemoteDataSource.getInstance(),
                EnvironmentalSensorsLocalDataSource.getInstance(new AppExecutors(),
                        database.environmentalSensorsDao()));
    }
}