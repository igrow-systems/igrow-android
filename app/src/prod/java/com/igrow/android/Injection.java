package com.igrow.android;

import android.content.Context;
import android.support.annotation.NonNull;


import com.igrow.android.data.source.EnvironmentalSensorsRepository;
import com.igrow.android.data.source.local.EnvironmentalSensorsLocalDataSource;
import com.igrow.android.data.source.local.IGrowDatabase;
import com.igrow.android.data.source.remote.EnvironmentalSensorsRemoteDataSource;
import com.igrow.android.util.AppExecutors;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Enables injection of production implementations for
 * {@link EnvironmentalSensorsDataSource} at compile time.
 */
public class Injection {

    public static EnvironmentalSensorsRepository provideEnvironmentalSensorsRepository(@NonNull Context context) {
        checkNotNull(context);
        IGrowDatabase database = IGrowDatabase.getInstance(context);
        return EnvironmentalSensorsRepository.getInstance(EnvironmentalSensorsRemoteDataSource.getInstance(),
                EnvironmentalSensorsLocalDataSource.getInstance(new AppExecutors(),
                        database.environmentalSensorsDao()));
    }


}
