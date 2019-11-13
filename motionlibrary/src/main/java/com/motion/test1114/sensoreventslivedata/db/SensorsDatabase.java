package com.motion.test1114.sensoreventslivedata.db;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;

import java.util.concurrent.Executors;

/**
 * Created by Asif on 11/14/2018.
 */

@Database(entities = {SensorEventData.class}, version = 1, exportSchema = false)
public abstract class SensorsDatabase extends RoomDatabase {

    private static SensorsDatabase INSTANCE;


    public abstract SensorDao sensorsEventsDao();

    private static final Object sLock = new Object();

    public static SensorsDatabase getInstance(Context context) {
        synchronized (sLock) {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        SensorsDatabase.class, "SensorsEventData.db")
                        .allowMainThreadQueries()
                        .addCallback(new Callback() {
                            @Override
                            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                super.onCreate(db);

                            }
                        })
                        .build();
            }
            return INSTANCE;
        }
    }
}
