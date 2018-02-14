package com.igrow.android.data.source.local;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.igrow.android.data.EnvironmentalSensor;

import java.util.List;
import java.util.UUID;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

/**
 * Created by jsr on 2/01/18.
 */

@Dao
public interface EnvironmentalSensorsDao {

    /**
     * Select all environmental sensors from the environmental sensors table.
     *
     * @return all environmental sensors.
     */
    @Query("SELECT * FROM environmental_sensors")
    List<EnvironmentalSensor> getEnvironmentalSensors();

    /**
     * Select a environmental sensor by id.
     *
     * @param sensorId the environmental sensor id.
     * @return the environmental sensor with sensorId.
     */
    @Query("SELECT * FROM environmental_sensors WHERE id = :sensorId")
    EnvironmentalSensor getEnvironmentalSensorById(UUID sensorId);

    /**
     * Insert a environmental sensor in the database. If the environmental sensor already exists, replace it.
     *
     * @param task the environmental sensor to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertEnvironmentalSensor(EnvironmentalSensor task);

    /**
     * Update a environmental sensor.
     *
     * @param task environmental sensor to be updated
     * @return the number of environmental sensors updated. This should always be 1.
     */
    @Update
    int updateEnvironmentalSensor(EnvironmentalSensor task);

    /**
     * Delete a environmental sensor by id.
     *
     * @return the number of environmental sensors deleted. This should always be 1.
     */
    @Query("DELETE FROM environmental_sensors WHERE id = :sensorId")
    int deleteEnvironmentalSensorById(UUID sensorId);

    /**
     * Delete all environmental sensors.
     */
    @Query("DELETE FROM environmental_sensors")
    void deleteEnvironmentalSensors();

}
