package com.igrow.android.data.source.local;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.igrow.android.data.EnvironmentalSensor;

import java.util.List;

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
     * Select a task by id.
     *
     * @param taskId the task id.
     * @return the task with taskId.
     */
    @Query("SELECT * FROM environmental_sensors WHERE entryid = :taskId")
    EnvironmentalSensor getEnvironmentalSensorById(String taskId);

    /**
     * Insert a task in the database. If the task already exists, replace it.
     *
     * @param task the task to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertEnvironmentalSensor(EnvironmentalSensor task);

    /**
     * Update a task.
     *
     * @param task task to be updated
     * @return the number of environmental sensors updated. This should always be 1.
     */
    @Update
    int updateEnvironmentalSensor(EnvironmentalSensor task);

    /**
     * Update the complete status of a task
     *
     * @param sensorId    id of the task
     * @param completed status to be updated
     */
    @Query("UPDATE environmental_sensors SET completed = :completed WHERE entryid = :sensorId")
    void updateCompleted(String taskId, boolean completed);

    /**
     * Delete a task by id.
     *
     * @return the number of environmental sensors deleted. This should always be 1.
     */
    @Query("DELETE FROM environmental_sensors WHERE entryid = :taskId")
    int deleteEnvironmentalSensorById(String taskId);

    /**
     * Delete all environmental sensors.
     */
    @Query("DELETE FROM environmental_sensors")
    void deleteEnvironmentalSensors();

    /**
     * Delete all completed environmental sensors from the table.
     *
     * @return the number of environmental sensors deleted.
     */
    @Query("DELETE FROM environmental_sensors WHERE completed = 1")
    int deleteCompletedEnvironmentalSensors();
}
