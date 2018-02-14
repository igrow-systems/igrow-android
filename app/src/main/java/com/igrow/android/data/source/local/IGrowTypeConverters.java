package com.igrow.android.data.source.local;

import android.arch.persistence.room.TypeConverter;

import java.util.Date;
import java.util.UUID;

/**
 * Created by jsr on 3/01/18.
 */

public class IGrowTypeConverters {

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

    @TypeConverter
    public UUID fromString(String value) {
        return value == null ? null : UUID.fromString(value);
    }

    @TypeConverter
    public String uuidToString(UUID uuid) {
        return uuid == null ? null : uuidToString(uuid);
    }
}