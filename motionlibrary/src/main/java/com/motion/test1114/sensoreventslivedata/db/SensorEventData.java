package com.motion.test1114.sensoreventslivedata.db;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by Asif on 11/14/2019.
 */

@Entity
public class SensorEventData {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String eventName;
    private String eventType;

    public SensorEventData() {
    }

    @Ignore
    public SensorEventData(String eventName, String eventType) {
        this.eventName = eventName;
        this.eventType = eventType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }
}
