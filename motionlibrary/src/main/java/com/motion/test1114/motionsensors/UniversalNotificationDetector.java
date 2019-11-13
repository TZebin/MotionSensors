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

package com.motion.test1114.motionsensors;

import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.media.ExifInterface;

import static android.hardware.Sensor.TYPE_ACCELEROMETER;
import static android.hardware.Sensor.TYPE_MAGNETIC_FIELD;

/**
 * The type Shake detector.
 */
public class UniversalNotificationDetector extends SensorDetector {

    /**
     * The interface Shake listener.
     */
    public interface UniversalListener {

        /**
         * On shake detected.
         */
        void onUniversalShakeDetected();

        void onUniversalShakeStopped();

        /**
         * On bottom side up.
         */
        void onUniversalBottomSideUp();

        /**
         * On left side up.
         */
        void onUniversalLeftSideUp();

        /**
         * On right side up.
         */
        void onUniversalRightSideUp();

        /**
         * On top side up.
         */
        void onUniversalTopSideUp();

        /**
         * On movement.
         */
        void onUniversalMovement();

        /**
         * On stationary.
         */
        void onUniversalStationary();

        /**
         * On flip face down.
         */
        void onUniversalFlipFaceDown();

        /**
         * On flip  face up.
         */
        void onUniversalFlipFaceUp();

        /**
         * On wrist twist.
         */
        void onUniversalWristTwist();
    }

    // Shake detector part
    private boolean isShaking = false;

    private long lastTimeShakeDetected = System.currentTimeMillis();

    private float mAccel;

    private float mAccelCurrent = SensorManager.GRAVITY_EARTH;

    private final UniversalListener universalListener;

    private final float threshold;

    private final long timeBeforeDeclaringShakeStopped;


    // Orientation detector part
    private static final int ORIENTATION_PORTRAIT = ExifInterface.ORIENTATION_ROTATE_90; // 6

    private static final int ORIENTATION_LANDSCAPE_REVERSE = ExifInterface.ORIENTATION_ROTATE_180;// 3

    private static final int ORIENTATION_LANDSCAPE = ExifInterface.ORIENTATION_NORMAL; // 1

    private static final int ORIENTATION_PORTRAIT_REVERSE = ExifInterface.ORIENTATION_ROTATE_270; // 8

    private float averagePitch = 0;

    private float averageRoll = 0;

    private int eventOccurred = 0;

    /**
     * The M geomagnetic.
     */
    private float[] mGeomagnetic;

    /**
     * The M gravity.
     */
    private float[] mGravity;

    private int orientation = ORIENTATION_PORTRAIT;

    private final float[] pitches;

    private final float[] rolls;

    private final int smoothness;


    // Movement detector
    private boolean isMoving = false;

    private long lastTimeMovementDetected = System.currentTimeMillis();

    private final long timeBeforeDeclaringStationary;


    //Wrist twist detector
    private boolean isGestureInProgress = false;

    private long lastTimeWristTwistDetected = System.currentTimeMillis();
    private final long timeForWristTwistGesture;



    /**
     * Instantiates a new UniversalListener.
     *
     * @param universalListener the universal listener
     */
    public UniversalNotificationDetector(UniversalListener universalListener) {
        this(1, 3f, 1000, 5000, 1000, universalListener);
    }

    /**
     * Instantiates a new Shake detector.
     *
     * @param threshold         the threshold
     * @param universalListener the shake listener
     */
    public UniversalNotificationDetector(int smoothness, float threshold, long timeBeforeDeclaringShakeStopped,long timeForWristTwistGesture, long timeBeforeDeclaringStationary,
                                         UniversalListener universalListener) {
        super(TYPE_ACCELEROMETER, TYPE_MAGNETIC_FIELD);
        this.universalListener = universalListener;
        this.threshold = threshold;
        this.timeBeforeDeclaringShakeStopped = timeBeforeDeclaringShakeStopped;
        this.timeBeforeDeclaringStationary = timeBeforeDeclaringStationary;
        this.timeForWristTwistGesture = timeForWristTwistGesture;


        this.smoothness = smoothness;
        pitches = new float[smoothness];
        rolls = new float[smoothness];
    }

    @Override
    protected void onSensorEvent(SensorEvent sensorEvent) {
        // Shake detection
        float x = sensorEvent.values[0];
        float y = sensorEvent.values[1];
        float z = sensorEvent.values[2];
        float mAccelLast = mAccelCurrent;
        mAccelCurrent = (float) Math.sqrt(x * x + y * y + z * z);
        float delta = mAccelCurrent - mAccelLast;
        mAccel = mAccel * 0.9f + delta;
        // Make this higher or lower according to how much
        // motion you want to detect
        if (mAccel > threshold) {
            lastTimeShakeDetected = System.currentTimeMillis();
            isShaking = true;
            universalListener.onUniversalShakeDetected();
        } else {
            long timeDelta = (System.currentTimeMillis() - lastTimeShakeDetected);
            if (timeDelta > timeBeforeDeclaringShakeStopped && isShaking) {
                isShaking = false;
                universalListener.onUniversalShakeStopped();
            }
        }


        //Orientation detection
        if (sensorEvent.sensor.getType() == TYPE_ACCELEROMETER) {
            mGravity = sensorEvent.values;
        }
        if (sensorEvent.sensor.getType() == TYPE_MAGNETIC_FIELD) {
            mGeomagnetic = sensorEvent.values;
        }
        if (mGravity != null && mGeomagnetic != null) {
            float[] R = new float[9];
            float[] I = new float[9];
            boolean success = SensorManager.getRotationMatrix(R, I, mGravity, mGeomagnetic);
            if (success) {
                float[] orientationData = new float[3];
                SensorManager.getOrientation(R, orientationData);
                averagePitch = addValue(orientationData[1], pitches);
                averageRoll = addValue(orientationData[2], rolls);
                orientation = calculateOrientation();
                switch (orientation) {
                    case ORIENTATION_PORTRAIT:
                        if (eventOccurred != 1) {
                            eventOccurred = 1;
                            universalListener.onUniversalTopSideUp();
                        }
                        break;
                    case ORIENTATION_LANDSCAPE:
                        if (eventOccurred != 2) {
                            eventOccurred = 2;
                            universalListener.onUniversalRightSideUp();
                        }
                        break;
                    case ORIENTATION_PORTRAIT_REVERSE:
                        if (eventOccurred != 3) {
                            eventOccurred = 3;
                            universalListener.onUniversalBottomSideUp();
                        }
                        break;
                    case ORIENTATION_LANDSCAPE_REVERSE:
                        if (eventOccurred != 4) {
                            eventOccurred = 4;
                            universalListener.onUniversalBottomSideUp();
                        }
                        break;
                    default:
                        // do nothing
                        break;
                }
            }
        }



    /* Logic:
    If there is no external force on the device, vector sum of accelerometer sensor values
    will be only gravity. If there is a change in vector sum of gravity, then there is a force.
    If this force is significant, you can assume device is moving.
    If vector sum is equal to gravity with +/- threshold its stable lying on table.
     */
        mAccelCurrent = (float) Math.sqrt(x * x + y * y + z * z);

        // Make this higher or lower according to how much
        // motion you want to detect
        if (delta > threshold) {
            lastTimeMovementDetected = System.currentTimeMillis();
            isMoving = true;
            universalListener.onUniversalMovement();
        } else {
            long timeDelta = (System.currentTimeMillis() - lastTimeMovementDetected);
            if (timeDelta > timeBeforeDeclaringStationary && isMoving) {
                isMoving = false;
                universalListener.onUniversalStationary();
            }
        }


        // Flip detector
        if (z > 9 && z < 10 && eventOccurred != 1) {
            eventOccurred = 1;
            universalListener.onUniversalFlipFaceUp();
        } else if (z > -10 && z < -9 && eventOccurred != 2) {
            eventOccurred = 2;
            universalListener.onUniversalFlipFaceDown();
        }


        //Wrist twist detector
        // Make this higher or lower according to how much
        // motion you want to detect
        if (x < -9.8f && y > -3f && z < (-threshold)) {
            lastTimeWristTwistDetected = System.currentTimeMillis();
            isGestureInProgress = true;
        } else {
            long timeDelta = (System.currentTimeMillis() - lastTimeWristTwistDetected);
            if (timeDelta > timeForWristTwistGesture && isGestureInProgress) {
                isGestureInProgress = false;
                universalListener.onUniversalWristTwist();
            }
        }
    }


    private float addValue(float value, float[] values) {
        float temp_value = (float) Math.round(Math.toDegrees(value));
        float average = 0;
        for (int i = 1; i < smoothness; i++) {
            values[i - 1] = values[i];
            average += values[i];
        }
        values[smoothness - 1] = temp_value;
        average = (average + temp_value) / smoothness;
        return average;
    }

    private int calculateOrientation() {
        // finding local orientation dip
        if ((orientation == ORIENTATION_PORTRAIT || orientation == ORIENTATION_PORTRAIT_REVERSE) && (
                averageRoll > -30
                        && averageRoll < 30)) {
            if (averagePitch > 0) {
                return ORIENTATION_PORTRAIT_REVERSE;
            } else {
                return ORIENTATION_PORTRAIT;
            }
        } else {
            // divides between all orientations
            if (Math.abs(averagePitch) >= 30) {
                if (averagePitch > 0) {
                    return ORIENTATION_PORTRAIT_REVERSE;
                } else {
                    return ORIENTATION_PORTRAIT;
                }
            } else {
                if (averageRoll > 0) {
                    return ORIENTATION_LANDSCAPE_REVERSE;
                } else {
                    return ORIENTATION_LANDSCAPE;
                }
            }
        }
    }
}
