package com.igrow.android.data.source.local;

import android.arch.persistence.room.TypeConverter;

import java.util.Date;

/**
 * Created by jsr on 3/01/18.
 */

public class TypeConverters {

    @TypeConverter
    public Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public Long dateToTimestamp(Date date) {
        if (date == null) {
            return null;
        } else {
            return date.getTime();
        }
    }

    //public Location
}