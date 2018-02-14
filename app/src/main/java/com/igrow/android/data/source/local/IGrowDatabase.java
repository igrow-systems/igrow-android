package com.igrow.android.data.source.local;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import com.igrow.android.data.EnvironmentalSensor;

/**
 * Created by jsr on 2/01/18.
 */

@Database(entities = {EnvironmentalSensor.class}, version = 1)
@TypeConverters({IGrowTypeConverters.class})
public abstract class IGrowDatabase extends RoomDatabase {

    private static IGrowDatabase INSTANCE;

    private static final Object sLock = new Object();

    public static IGrowDatabase getInstance(Context context) {
        synchronized (sLock) {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        IGrowDatabase.class, "igrow.db")
                        .build();
            }
            return INSTANCE;
        }
    }

    public abstract EnvironmentalSensorsDao environmentalSensorsDao();
}
