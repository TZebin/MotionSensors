package com.motion.test1114.service;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.arch.lifecycle.LifecycleService;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.motion.test1114.motionsensors.ChopDetector;
import com.motion.test1114.motionsensors.FlipDetector;
import com.motion.test1114.motionsensors.LightDetector;
import com.motion.test1114.motionsensors.MotionSensor;
import com.motion.test1114.motionsensors.MovementDetector;
import com.motion.test1114.motionsensors.OrientationDetector;
import com.motion.test1114.motionsensors.PickupDeviceDetector;
import com.motion.test1114.motionsensors.ProximityDetector;
import com.motion.test1114.motionsensors.RotationAngleDetector;
import com.motion.test1114.motionsensors.ScoopDetector;
import com.motion.test1114.motionsensors.ShakeDetector;
import com.motion.test1114.motionsensors.SoundLevelDetector;
import com.motion.test1114.motionsensors.StepListener;
import com.motion.test1114.motionsensors.TiltDirectionDetector;
import com.motion.test1114.motionsensors.UniversalNotificationDetector;
import com.motion.test1114.motionsensors.WaveDetector;
import com.motion.test1114.motionsensors.WristTwistDetector;
import com.motion.test1114.sensoreventslivedata.SensorsViewModel;
import com.motion.test1114.sensoreventslivedata.db.SensorDao;
import com.motion.test1114.sensoreventslivedata.db.SensorEventData;
import com.motion.test1114.sensoreventslivedata.db.SensorsDatabase;
import com.motion.test1114.utils.Constants;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static android.support.v4.app.NotificationCompat.PRIORITY_LOW;


/*
 * this is an example of a service that prompts itself to a foreground service with a persistent
 * notification.  Which is now required by Oreo otherwise, a background service without an app will be killed.
 *
 */

public class MotionForeGroundService extends Service implements CompoundButton.OnCheckedChangeListener, ShakeDetector.ShakeListener, FlipDetector.FlipListener, LightDetector.LightListener,
        OrientationDetector.OrientationListener, ProximityDetector.ProximityListener, WaveDetector.WaveListener, SoundLevelDetector.SoundLevelListener, MovementDetector.MovementListener,
        ChopDetector.ChopListener, WristTwistDetector.WristTwistListener, RotationAngleDetector.RotationAngleListener, TiltDirectionDetector.TiltDirectionListener, StepListener,
        ScoopDetector.ScoopListener, PickupDeviceDetector.PickupDeviceListener, UniversalNotificationDetector.UniversalListener {


    private final static String TAG = "MotionForegroundService";

    private SensorDao sensorDao;
    private ExecutorService executorService;
    SensorEventData eventData;

    public MotionForeGroundService() {
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

    }

    @Override
    public void onChop() {

    }

    @Override
    public void onFaceDown() {

    }

    @Override
    public void onFaceUp() {

    }

    @Override
    public void onDark() {

    }

    @Override
    public void onLight() {

    }

    @Override
    public void onMovement() {

    }

    @Override
    public void onStationary() {

    }

    @Override
    public void onBottomSideUp() {
        Notification notification = getNotification("MotionService: Bottom Side UP");
        startForeground(Constants.NOTIFICATION_ID_FOREGROUND_SERVICE, notification);
    }

    @Override
    public void onLeftSideUp() {
        Notification notification = getNotification("MotionService: Left Side UP");
        startForeground(Constants.NOTIFICATION_ID_FOREGROUND_SERVICE, notification);
    }

    @Override
    public void onRightSideUp() {
        Notification notification = getNotification("MotionService: Right Side UP");
        startForeground(Constants.NOTIFICATION_ID_FOREGROUND_SERVICE, notification);
    }

    @Override
    public void onTopSideUp() {
        Notification notification = getNotification("MotionService: Top Side UP");
        startForeground(Constants.NOTIFICATION_ID_FOREGROUND_SERVICE, notification);
    }

    @Override
    public void onDevicePickedUp() {

    }

    @Override
    public void onDevicePutDown() {

    }

    @Override
    public void onFar() {

    }

    @Override
    public void onNear() {

    }

    @Override
    public void onRotation(float angleInAxisX, float angleInAxisY, float angleInAxisZ) {
        Notification notification = getNotification("MotionService: Rotation detected"+" "+"Rotation in Axis Detected(deg):\nX="
                + angleInAxisX
                + ",\nY="
                + angleInAxisY
                + ",\nZ="
                + angleInAxisZ);
        startForeground(Constants.NOTIFICATION_ID_FOREGROUND_SERVICE, notification);

    }

    @Override
    public void onScooped() {

    }

    @Override
    public void onShakeDetected() {
        Notification notification = getNotification("MotionService: Shake detected");
        startForeground(Constants.NOTIFICATION_ID_FOREGROUND_SERVICE, notification);
    }

    @Override
    public void onShakeStopped() {
        Notification notification = getNotification("MotionService: Shake stopped");
        startForeground(Constants.NOTIFICATION_ID_FOREGROUND_SERVICE, notification);
    }

    @Override
    public void onSoundDetected(float level) {

    }

    @Override
    public void stepInformation(int noOfSteps, float distanceInMeter, int stepActivityType) {

    }

    @Override
    public void onTiltInAxisX(int direction) {

    }

    @Override
    public void onTiltInAxisY(int direction) {

    }

    @Override
    public void onTiltInAxisZ(int direction) {

    }

    @Override
    public void onWave() {

    }

    @Override
    public void onWristTwist() {

    }




    @Override
    public void onCreate() {

        super.onCreate();

        sensorDao = SensorsDatabase.getInstance(getApplicationContext()).sensorsEventsDao();
        executorService = Executors.newSingleThreadExecutor();

        eventData = new SensorEventData();

        // Init MotionSensor
        MotionSensor.getInstance().init(this);
        MotionSensor.getInstance().startUniversalMotionDetection(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();
        Notification notification = getNotification("MotionService is running");
        startForeground(Constants.NOTIFICATION_ID_FOREGROUND_SERVICE, notification);  //not sure what the ID needs to be.


        // If we get killed, after returning from here, restart
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // We don't provide binding, so return null
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show();

        MotionSensor.getInstance().stop();
        MotionSensor.getInstance().stopUniversalDetection(this);
    }

    /*// build a persistent notification and return it.
    public Notification getNotification(String message) {

        return new NotificationCompat.Builder(getApplicationContext(), Constants.NOTIFICATION_STRING_FOREGROUND_SERVICE)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setOngoing(true)  //persistent notification!
                .setChannelId(Constants.NOTIFICATION_STRING_FOREGROUND_SERVICE)
                .setContentTitle("Service")   //Title message top row.
                .setContentText(message)  //message when looking at the notification, second row
                .build();  //finally build and return a Notification.
    }*/

    public Notification getNotification(String message) {
        String channel;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            channel = createChannel(message);
        else {
            channel = "";
        }
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, channel).setSmallIcon(android.R.drawable.ic_menu_mylocation).setContentTitle("Universal Motion Detector Service");
        Notification notification = mBuilder
                .setPriority(PRIORITY_LOW)
                .setCategory(Notification.CATEGORY_SERVICE)
                .setContentText(message)
                .build();


        return notification;
    }

    @NonNull
    @TargetApi(26)
    private synchronized String createChannel(String message) {
        NotificationManager mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        String name = "Motion Service";
        int importance = NotificationManager.IMPORTANCE_LOW;

        NotificationChannel mChannel = new NotificationChannel("Motion Service", name, importance);

        mChannel.enableLights(true);
        mChannel.setLightColor(Color.BLUE);
        if (mNotificationManager != null) {
            mNotificationManager.createNotificationChannel(mChannel);
        } else {
            stopSelf();
        }
        return "Motion Service";
    }

    @Override
    public void onUniversalShakeDetected() {
        Notification notification = getNotification("Universal MotionService: Shake started");
        startForeground(Constants.NOTIFICATION_ID_FOREGROUND_SERVICE, notification);

        eventData.setEventName("Universal MotionService: Shake Detector ");
        eventData.setEventType("Shake Detected");
        sensorDao.save(eventData);
    }

    @Override
    public void onUniversalShakeStopped() {
        Notification notification = getNotification("Universal MotionService: Shake stopped");
        startForeground(Constants.NOTIFICATION_ID_FOREGROUND_SERVICE, notification);

        eventData.setEventName("Universal MotionService: Shake Detector ");
        eventData.setEventType("Shake Stopped");
        sensorDao.save(eventData);

    }

    @Override
    public void onUniversalBottomSideUp() {
        Notification notification = getNotification("Universal MotionService: Orientation Bottom Side Up");
        startForeground(Constants.NOTIFICATION_ID_FOREGROUND_SERVICE, notification);

        eventData.setEventName("Universal MotionService: Orientation Detector ");
        eventData.setEventType("Bottom Side Up");
        sensorDao.save(eventData);
    }

    @Override
    public void onUniversalLeftSideUp() {
        Notification notification = getNotification("Universal MotionService: Orientation Left Side Up");
        startForeground(Constants.NOTIFICATION_ID_FOREGROUND_SERVICE, notification);

        eventData.setEventName("Universal MotionService: Orientation Detector ");
        eventData.setEventType("Left Side Up");
        sensorDao.save(eventData);
    }

    @Override
    public void onUniversalRightSideUp() {
        Notification notification = getNotification("Universal MotionService: Orientation Right Side Up");
        startForeground(Constants.NOTIFICATION_ID_FOREGROUND_SERVICE, notification);

        eventData.setEventName("Universal MotionService: Orientation Detector ");
        eventData.setEventType("Right Side Up");
        sensorDao.save(eventData);
    }

    @Override
    public void onUniversalTopSideUp() {
        Notification notification = getNotification("Universal MotionService: Orientation Top Side Up");
        startForeground(Constants.NOTIFICATION_ID_FOREGROUND_SERVICE, notification);

        eventData.setEventName("Universal MotionService: Orientation Detector ");
        eventData.setEventType("Top Side Up");
        sensorDao.save(eventData);
    }

    @Override
    public void onUniversalMovement() {
        Notification notification = getNotification("Universal MotionService: Movement detected");
        startForeground(Constants.NOTIFICATION_ID_FOREGROUND_SERVICE, notification);

        eventData.setEventName("Universal MotionService: Movement Detector ");
        eventData.setEventType("Movement");
        sensorDao.save(eventData);
    }

    @Override
    public void onUniversalStationary() {
        Notification notification = getNotification("Universal MotionService: Movement stationary");
        startForeground(Constants.NOTIFICATION_ID_FOREGROUND_SERVICE, notification);

        eventData.setEventName("Universal MotionService: Movement Detector ");
        eventData.setEventType("Stationary");
        sensorDao.save(eventData);
    }

    @Override
    public void onUniversalFlipFaceDown() {
        Notification notification = getNotification("Universal MotionService: Flip Face Down");
        startForeground(Constants.NOTIFICATION_ID_FOREGROUND_SERVICE, notification);

        eventData.setEventName("Universal MotionService: Flip Detector ");
        eventData.setEventType("Flip Face Down");
        sensorDao.save(eventData);
    }

    @Override
    public void onUniversalFlipFaceUp() {
        Notification notification = getNotification("Universal MotionService: Flip Face Up");
        startForeground(Constants.NOTIFICATION_ID_FOREGROUND_SERVICE, notification);

        eventData.setEventName("Universal MotionService: Flip Detector ");
        eventData.setEventType("Flip Face Up");
        sensorDao.save(eventData);
    }

    @Override
    public void onUniversalWristTwist() {
        Notification notification = getNotification("Universal MotionService: Wrist twist");
        startForeground(Constants.NOTIFICATION_ID_FOREGROUND_SERVICE, notification);

        eventData.setEventName("Universal MotionService: Wrist Twist Detector ");
        eventData.setEventType("Wrist twist detected");
        sensorDao.save(eventData);
    }
}
