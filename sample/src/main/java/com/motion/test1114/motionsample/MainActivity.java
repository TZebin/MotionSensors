/*
 * Copyright (C) 2016 Nishant Srivastava
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.motion.test1114.motionsample;

import android.Manifest.permission;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import com.motion.test1114.motionsensors.ChopDetector.ChopListener;
import com.motion.test1114.motionsensors.FlipDetector.FlipListener;
import com.motion.test1114.motionsensors.LightDetector.LightListener;
import com.motion.test1114.motionsensors.MotionSensor;
import com.motion.test1114.motionsensors.MovementDetector.MovementListener;
import com.motion.test1114.motionsensors.OrientationDetector.OrientationListener;
import com.motion.test1114.motionsensors.PickupDeviceDetector.PickupDeviceListener;
import com.motion.test1114.motionsensors.ProximityDetector.ProximityListener;
import com.motion.test1114.motionsensors.RotationAngleDetector.RotationAngleListener;
import com.motion.test1114.motionsensors.ScoopDetector.ScoopListener;
import com.motion.test1114.motionsensors.ShakeDetector.ShakeListener;
import com.motion.test1114.motionsensors.SoundLevelDetector.SoundLevelListener;
import com.motion.test1114.motionsensors.StepDetectorUtil;
import com.motion.test1114.motionsensors.StepListener;
import com.motion.test1114.motionsensors.TiltDirectionDetector;
import com.motion.test1114.motionsensors.TiltDirectionDetector.TiltDirectionListener;
import com.motion.test1114.motionsensors.UniversalNotificationDetector;
import com.motion.test1114.motionsensors.WaveDetector.WaveListener;
import com.motion.test1114.motionsensors.WristTwistDetector.WristTwistListener;
import com.motion.test1114.sensoreventslivedata.SensorsDataActivity;
import com.motion.test1114.service.MotionForeGroundService;
import com.motion.test1114.utils.Constants;

import java.text.DecimalFormat;

/**
 * The type Main activity.
 */
public class MainActivity extends AppCompatActivity
        implements OnCheckedChangeListener, ShakeListener, FlipListener, LightListener,
        OrientationListener, ProximityListener, WaveListener, SoundLevelListener, MovementListener,
        ChopListener, WristTwistListener, RotationAngleListener, TiltDirectionListener, StepListener,
        ScoopListener, PickupDeviceListener, UniversalNotificationDetector.UniversalListener {

    private static final String LOGTAG = "SensorsDataActivity";

    private static final String recordAudioPermission = permission.RECORD_AUDIO;

    private static final boolean DEBUG = true;

    boolean hasRecordAudioPermission = false;

    private Handler handler;

    private SwitchCompat swt1, swt2, swt3, swt4, swt5, swt6, swt7, swt8, swt9, swt10, swt11, swt12,
            swt13, swt14, swt15;

    private TextView txtViewResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        hasRecordAudioPermission = RuntimePermissionUtil.checkPermissonGranted(this, recordAudioPermission);

        // Init MotionSensor
        MotionSensor.getInstance().init(this);

        // Init UI controls,views and handler
        handler = new Handler();
        txtViewResult = findViewById(R.id.textView_result);

        swt1 = findViewById(R.id.Switch1);
        swt1.setOnCheckedChangeListener(this);
        swt1.setChecked(false);

        swt2 = findViewById(R.id.Switch2);
        swt2.setOnCheckedChangeListener(this);
        swt2.setChecked(false);

        swt3 = findViewById(R.id.Switch3);
        swt3.setOnCheckedChangeListener(this);
        swt3.setChecked(false);

        swt4 = findViewById(R.id.Switch4);
        swt4.setOnCheckedChangeListener(this);
        swt4.setChecked(false);

        swt5 = findViewById(R.id.Switch5);
        swt5.setOnCheckedChangeListener(this);
        swt5.setChecked(false);

        swt6 = findViewById(R.id.Switch6);
        swt6.setOnCheckedChangeListener(this);
        swt6.setChecked(false);

        swt7 = findViewById(R.id.Switch7);
        swt7.setOnCheckedChangeListener(this);
        swt7.setChecked(false);

        swt8 = findViewById(R.id.Switch8);
        swt8.setOnCheckedChangeListener(this);
        swt8.setChecked(false);

        swt9 = findViewById(R.id.Switch9);
        swt9.setOnCheckedChangeListener(this);
        swt9.setChecked(false);

        swt10 = findViewById(R.id.Switch10);
        swt10.setOnCheckedChangeListener(this);
        swt10.setChecked(false);

        swt11 = findViewById(R.id.Switch11);
        swt11.setOnCheckedChangeListener(this);
        swt11.setChecked(false);

        swt12 = findViewById(R.id.Switch12);
        swt12.setOnCheckedChangeListener(this);
        swt12.setChecked(false);

        swt13 = (SwitchCompat) findViewById(R.id.Switch13);
        swt13.setOnCheckedChangeListener(this);
        swt13.setChecked(false);

        swt14 = (SwitchCompat) findViewById(R.id.Switch14);
        swt14.setOnCheckedChangeListener(this);
        swt14.setChecked(false);

        swt15 = (SwitchCompat) findViewById(R.id.Switch15);
        swt15.setOnCheckedChangeListener(this);
        swt15.setChecked(false);

        Button btnTouchEvent = findViewById(R.id.btn_touchevent);
        btnTouchEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, TouchActivity.class));
            }
        });

        Button btnViewEvent = findViewById(R.id.btn_viewevents);
        btnViewEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, SensorsDataActivity.class));
            }
        });

        //MotionSensor.getInstance().startUniversalMotionDetection(this);
        startMotionSensorService();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Stop Detections
        MotionSensor.getInstance().stopShakeDetection(this);
        MotionSensor.getInstance().stopFlipDetection(this);
        MotionSensor.getInstance().stopOrientationDetection(this);
        MotionSensor.getInstance().stopProximityDetection(this);
        MotionSensor.getInstance().stopLightDetection(this);
        MotionSensor.getInstance().stopWaveDetection(this);
        MotionSensor.getInstance().stopSoundLevelDetection();
        MotionSensor.getInstance().stopMovementDetection(this);
        MotionSensor.getInstance().stopChopDetection(this);
        MotionSensor.getInstance().stopWristTwistDetection(this);
        MotionSensor.getInstance().stopRotationAngleDetection(this);
        MotionSensor.getInstance().stopTiltDirectionDetection(this);
        MotionSensor.getInstance().stopStepDetection(this);
        MotionSensor.getInstance().stopPickupDeviceDetection(this);
        MotionSensor.getInstance().stopScoopDetection(this);

        // Set the all switches to off position
        swt1.setChecked(false);
        swt2.setChecked(false);
        swt3.setChecked(false);
        swt4.setChecked(false);
        swt5.setChecked(false);
        swt6.setChecked(false);
        swt7.setChecked(false);
        swt8.setChecked(false);
        swt9.setChecked(false);
        swt10.setChecked(false);
        swt11.setChecked(false);
        swt12.setChecked(false);
        swt13.setChecked(false);
        swt14.setChecked(false);
        swt15.setChecked(false);

        // Reset the result view
        resetResultInView(txtViewResult);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // *** IMPORTANT ***
        // Stop MotionSensor and release the context held by it
        MotionSensor.getInstance().stop();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull final String[] permissions,
            @NonNull final int[] grantResults) {
        if (requestCode == 100) {
            RuntimePermissionUtil.onRequestPermissionsResult(grantResults, new RPResultListener() {
                @Override
                public void onPermissionDenied() {
                    // do nothing
                }

                @Override
                public void onPermissionGranted() {
                    if (RuntimePermissionUtil.checkPermissonGranted(MainActivity.this, recordAudioPermission)) {
                        hasRecordAudioPermission = true;
                        swt7.setChecked(true);
                    }
                }
            });
        }
    }

    @Override
    public void onBottomSideUp() {
        setResultTextView("Bottom Side UP", false);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onCheckedChanged(CompoundButton switchbtn, boolean isChecked) {
        switch (switchbtn.getId()) {

            case R.id.Switch1:
                if (isChecked) {
                    MotionSensor.getInstance().startShakeDetection(10, 2000, this);
                } else {
                    MotionSensor.getInstance().stopShakeDetection(this);
                }
                break;
            case R.id.Switch2:
                if (isChecked) {
                    MotionSensor.getInstance().startFlipDetection(this);
                } else {
                    MotionSensor.getInstance().stopFlipDetection(this);
                }

                break;
            case R.id.Switch3:
                if (isChecked) {
                    MotionSensor.getInstance().startOrientationDetection(this);
                } else {
                    MotionSensor.getInstance().stopOrientationDetection(this);
                }

                break;
            case R.id.Switch4:
                if (isChecked) {
                    MotionSensor.getInstance().startProximityDetection(this);
                } else {
                    MotionSensor.getInstance().stopProximityDetection(this);
                }
                break;
            case R.id.Switch5:
                if (isChecked) {
                    MotionSensor.getInstance().startLightDetection(10, this);
                } else {
                    MotionSensor.getInstance().stopLightDetection(this);
                }
                break;

            case R.id.Switch6:
                if (isChecked) {
                    MotionSensor.getInstance().startWaveDetection(this);
                } else {
                    MotionSensor.getInstance().stopWaveDetection(this);
                }
                break;

            case R.id.Switch7:
                if (isChecked) {
                    if (hasRecordAudioPermission) {
                        MotionSensor.getInstance().startSoundLevelDetection(this, this);
                    } else {
                        RuntimePermissionUtil.requestPermission(MainActivity.this, recordAudioPermission, 100);
                    }

                } else {
                    MotionSensor.getInstance().stopSoundLevelDetection();
                }
                break;
            case R.id.Switch8:
                if (isChecked) {
                    MotionSensor.getInstance().startMovementDetection(this);
                } else {
                    MotionSensor.getInstance().stopMovementDetection(this);
                }
                break;
            case R.id.Switch9:
                if (isChecked) {
                    MotionSensor.getInstance().startChopDetection(30f, 500, this);
                } else {
                    MotionSensor.getInstance().stopChopDetection(this);
                }
                break;
            case R.id.Switch10:
                if (isChecked) {
                    MotionSensor.getInstance().startWristTwistDetection(this);
                } else {
                    MotionSensor.getInstance().stopWristTwistDetection(this);
                }
                break;

            case R.id.Switch11:
                if (isChecked) {
                    MotionSensor.getInstance().startRotationAngleDetection(this);
                } else {
                    MotionSensor.getInstance().stopRotationAngleDetection(this);
                }
                break;

            case R.id.Switch12:
                if (isChecked) {
                    MotionSensor.getInstance().startTiltDirectionDetection(this);
                } else {
                    MotionSensor.getInstance().stopTiltDirectionDetection(this);
                }
                break;
            case R.id.Switch13:
                if (isChecked) {
                    MotionSensor.getInstance().startStepDetection(this, this, StepDetectorUtil.MALE);
                } else {
                    MotionSensor.getInstance().stopStepDetection(this);
                }
                break;

            case R.id.Switch14:
                if (isChecked) {
                    MotionSensor.getInstance().startPickupDeviceDetection(this);
                } else {
                    MotionSensor.getInstance().stopPickupDeviceDetection(this);
                }
                break;

            case R.id.Switch15:
                if (isChecked) {
                    MotionSensor.getInstance().startScoopDetection(this);
                } else {
                    MotionSensor.getInstance().stopScoopDetection(this);
                }
                break;

            default:
                // Do nothing
                break;
        }
    }

    @Override
    public void onChop() {
        setResultTextView("Chop Detected!", false);
    }

    @Override
    public void onDark() {
        setResultTextView("Dark", false);
    }

    @Override
    public void onDevicePickedUp() {
        setResultTextView("Device Picked up Detected!", false);
    }

    @Override
    public void onDevicePutDown() {
        setResultTextView("Device Put down Detected!", false);
    }

    @Override
    public void onFaceDown() {
        setResultTextView("Face Down", false);
    }

    @Override
    public void onFaceUp() {
        setResultTextView("Face UP", false);
    }

    @Override
    public void onFar() {
        setResultTextView("Far", false);
    }

    @Override
    public void onLeftSideUp() {
        setResultTextView("Left Side UP", false);
    }

    @Override
    public void onLight() {
        setResultTextView("Not Dark", false);
    }

    @Override
    public void onMovement() {
        setResultTextView("Movement Detected!", false);
    }

    @Override
    public void onNear() {
        setResultTextView("Near", false);
    }

    @Override
    public void onRightSideUp() {
        setResultTextView("Right Side UP", false);
    }

    @Override
    public void onRotation(float angleInAxisX, float angleInAxisY, float angleInAxisZ) {
        setResultTextView("Rotation in Axis Detected(deg):\nX="
                + angleInAxisX
                + ",\nY="
                + angleInAxisY
                + ",\nZ="
                + angleInAxisZ, true);
    }

    @Override
    public void onScooped() {
        setResultTextView("Scoop Gesture Detected!", false);
    }

    @Override
    public void onShakeDetected() {
        setResultTextView("Shake Detected!", false);
    }

    @Override
    public void onShakeStopped() {
        setResultTextView("Shake Stopped!", false);
    }

    @Override
    public void onSoundDetected(float level) {

        setResultTextView(new DecimalFormat("##.##").format(level) + "dB", true);
    }

    @Override
    public void onStationary() {
        setResultTextView("Device Stationary!", false);
    }

    @Override
    public void onTiltInAxisX(int direction) {
        displayResultForTiltDirectionDetector(direction, "X");
    }

    @Override
    public void onTiltInAxisY(int direction) {
        displayResultForTiltDirectionDetector(direction, "Y");
    }

    @Override
    public void onTiltInAxisZ(int direction) {
        displayResultForTiltDirectionDetector(direction, "Z");
    }

    @Override
    public void onTopSideUp() {
        setResultTextView("Top Side UP", false);
    }

    @Override
    public void onWave()        {
        setResultTextView("Wave Detected!", false);
    }

    @Override
    public void onWristTwist() {
        setResultTextView("Wrist Twist Detected!", false);
    }

    @Override
    public void stepInformation(int noOfSteps, float distanceInMeter, int stepActivityType) {
        String typeOfActivity;
        switch (stepActivityType) {
            case StepDetectorUtil.ACTIVITY_RUNNING:
                typeOfActivity = "Running";
                break;
            case StepDetectorUtil.ACTIVITY_WALKING:
                typeOfActivity = "Walking";
                break;
            default:
                typeOfActivity = "Still";
                break;
        }
        StringBuilder data = new StringBuilder("Steps: ").append(noOfSteps)
                .append("\n")
                .append("Distance: ")
                .append(distanceInMeter)
                .append("\n")
                .append("Activity Type: ")
                .append(typeOfActivity)
                .append("\n");

        setResultTextView(data.toString(), true);
    }

    private void displayResultForTiltDirectionDetector(int direction, String axis) {
        String dir;
        if (direction == TiltDirectionDetector.DIRECTION_CLOCKWISE) {
            dir = "ClockWise";
        } else {
            dir = "AntiClockWise";
        }
        setResultTextView("Tilt in " + axis + " Axis: " + dir, false);
    }

    private void resetResultInView(final TextView txt) {

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                txt.setText(getString(R.string.results_show_here));
            }
        }, 3000);
    }

    private void setResultTextView(final String text, final boolean realtime) {
        if (txtViewResult != null) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    txtViewResult.setText(text);
                    if (!realtime) {
                        resetResultInView(txtViewResult);
                    }
                }
            });

            if (DEBUG) {
                Log.i(LOGTAG, text);
            }
        }
    }

    private void startMotionSensorService(){
        //Intent startIntent = new Intent(SensorsDataActivity.this, MotionSensorService.class);
        Intent startIntent = new Intent(MainActivity.this, MotionForeGroundService.class);

        startIntent.setAction(Constants.ACTION.START_ACTION);
        startService(startIntent);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            startForegroundService(startIntent);
        } else {
            //lower then Oreo, just start the service.
            startService(startIntent);
        }
    }

    @Override
    public void onUniversalShakeDetected() {
        setResultTextView("Universal MotionService: Shake started", false);
    }

    @Override
    public void onUniversalShakeStopped() {
        setResultTextView("Universal MotionService: Shake stopped", false);
    }

    @Override
    public void onUniversalBottomSideUp() {
        setResultTextView("Universal MotionService: Orientation Bottom Side Up", false);
    }

    @Override
    public void onUniversalLeftSideUp() {
        setResultTextView("Universal MotionService: Orientation Left Side Up", false);
    }

    @Override
    public void onUniversalRightSideUp() {
        setResultTextView("Universal MotionService: Orientation Right Side Up", false);
    }

    @Override
    public void onUniversalTopSideUp() {
        setResultTextView("Universal MotionService: Orientation Top Side Up", false);
    }

    @Override
    public void onUniversalMovement() {
        setResultTextView("Universal MotionService: Movement detected", false);
    }


    @Override
    public void onUniversalStationary() {
        setResultTextView("Universal MotionService: Movement stationary", false);
    }

    @Override
    public void onUniversalFlipFaceDown() {
        setResultTextView("Universal MotionService: Flip Face Down", false);

    }

    @Override
    public void onUniversalFlipFaceUp() {
        setResultTextView("Universal MotionService: Flip Face Up", false);
    }

    @Override
    public void onUniversalWristTwist() {
        setResultTextView("Universal MotionService: Wrist twist", false);
    }
}
