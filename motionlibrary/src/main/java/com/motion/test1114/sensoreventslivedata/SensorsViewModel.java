package com.motion.test1114.sensoreventslivedata;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.motion.test1114.sensoreventslivedata.db.SensorDao;
import com.motion.test1114.sensoreventslivedata.db.SensorEventData;
import com.motion.test1114.sensoreventslivedata.db.SensorsDatabase;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created Asif on 11/14/2019.
 */

public class SensorsViewModel extends AndroidViewModel {

    private SensorDao sensorDao;
    private ExecutorService executorService;

    public SensorsViewModel(@NonNull Application application) {
        super(application);
        sensorDao = SensorsDatabase.getInstance(application).sensorsEventsDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    LiveData<List<SensorEventData>> getAllPosts() {
        return sensorDao.findAll();
    }

    void savePost(SensorEventData post) {
        executorService.execute(() -> sensorDao.save(post));
    }

    void deletePost(SensorEventData post) {
        executorService.execute(() -> sensorDao.delete(post));
    }
}
