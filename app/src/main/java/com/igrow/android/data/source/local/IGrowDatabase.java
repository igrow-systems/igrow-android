package com.igrow.android.data.source.local;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.igrow.android.data.EnvironmentalSensor;

/**
 * Created by jsr on 2/01/18.
 */

@Database(entities = {EnvironmentalSensor.class}, version = 1)
public abstract class IGrowDatabase extends RoomDatabase {
    public abstract EnvironmentalSensorsDao environmentalSensorDao();
}
