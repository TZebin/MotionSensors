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

import static android.content.Context.SENSOR_SERVICE;
import static android.hardware.Sensor.TYPE_ACCELEROMETER;
import static android.hardware.Sensor.TYPE_LIGHT;
import static android.hardware.Sensor.TYPE_MAGNETIC_FIELD;
import static android.hardware.Sensor.TYPE_PROXIMITY;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import com.motion.test1114.motionsensors.FlipDetector.FlipListener;
import com.motion.test1114.motionsensors.LightDetector.LightListener;
import com.motion.test1114.motionsensors.OrientationDetector.OrientationListener;
import com.motion.test1114.motionsensors.ProximityDetector.ProximityListener;
import com.motion.test1114.motionsensors.ShakeDetector.ShakeListener;
import java.lang.reflect.Field;
import java.util.Map;
import org.junit.*;
import org.junit.runner.*;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.Shadows;
import org.robolectric.shadows.ShadowSensorManager;

@RunWith(RobolectricTestRunner.class)
public class MotionSensorTest {

    private MotionSensor motionSensor;

    private ShadowSensorManager shadowSensorManager;

    @Test
    public void detectListenerWithStartFlipDetection() {
        addSensor(TYPE_ACCELEROMETER);
        FlipListener fakeListener = mock(FlipListener.class);
        motionSensor.startFlipDetection(fakeListener);
        FlipDetector detector = getDetector(fakeListener, FlipDetector.class);
        if (detector != null) {
            assertTrue("Sensor Manager must contain sensor event listener for flip",
                    shadowSensorManager.hasListener(detector));
        } else {
            fail(
                    "There should be flip detector in motionSensor. If not, please, check last version of class and update reflection accessing to it field");
        }
    }

    @Test
    public void detectListenerWithStartLightDetection() {
        addSensor(TYPE_LIGHT);
        LightListener fakeListener = mock(LightListener.class);
        motionSensor.startLightDetection(fakeListener);
        LightDetector detector = getDetector(fakeListener, LightDetector.class);
        if (detector != null) {
            assertTrue("Sensor Manager must contain sensor event listener for light",
                    shadowSensorManager.hasListener(detector));
        } else {
            fail(
                    "There should be light detector in motionSensor. If not, please, check last version of class and update reflection accessing to it field");
        }
    }

    @Test
    public void detectListenerWithStartLightDetectionWithCustomThreshold() {
        addSensor(TYPE_LIGHT);
        LightListener fakeListener = mock(LightListener.class);
        motionSensor.startLightDetection(4, fakeListener);
        LightDetector detector = getDetector(fakeListener, LightDetector.class);
        if (detector != null) {
            assertTrue("Sensor Manager must contain sensor event listener for light",
                    shadowSensorManager.hasListener(detector));
        } else {
            fail(
                    "There should be light detector in motionSensor. If not, please, check last version of class and update reflection accessing to it field");
        }
    }

    @Test
    public void detectListenerWithStartOrientationDetection() {
        addSensor(TYPE_ACCELEROMETER);
        addSensor(TYPE_MAGNETIC_FIELD);
        OrientationListener fakeListener = mock(OrientationListener.class);
        motionSensor.startOrientationDetection(fakeListener);
        OrientationDetector detector = getDetector(fakeListener, OrientationDetector.class);
        if (detector != null) {
            assertTrue("Sensor Manager must contain sensor event listener for orientation",
                    shadowSensorManager.hasListener(detector));
        } else {
            fail(
                    "There should be orientation detector in motionSensor. If not, please, check last version of class and update reflection accessing to it field");
        }
    }

    @Test
    public void detectListenerWithStartOrientationDetectionWithCustomSmoothness() {
        addSensor(TYPE_ACCELEROMETER);
        addSensor(TYPE_MAGNETIC_FIELD);
        OrientationListener fakeListener = mock(OrientationListener.class);
        motionSensor.startOrientationDetection(3, fakeListener);
        OrientationDetector detector = getDetector(fakeListener, OrientationDetector.class);
        if (detector != null) {
            assertTrue("Sensor Manager must contain sensor event listener for orientation",
                    shadowSensorManager.hasListener(detector));
        } else {
            fail(
                    "There should be orientation detector in motionSensor. If not, please, check last version of class and update reflection accessing to it field");
        }
    }

    @Test
    public void detectListenerWithStartProximityDetection() {
        addSensor(TYPE_PROXIMITY);
        ProximityListener fakeListener = mock(ProximityListener.class);
        motionSensor.startProximityDetection(fakeListener);
        ProximityDetector detector = getDetector(fakeListener, ProximityDetector.class);
        if (detector != null) {
            assertTrue("Sensor Manager must contain sensor event listener for proximity",
                    shadowSensorManager.hasListener(detector));
        } else {
            fail(
                    "There should be proximity detector in motionSensor. If not, please, check last version of class and update reflection accessing to it field");
        }
    }

    @Test
    public void detectListenerWithStartShakeDetection() {
        addSensor(TYPE_ACCELEROMETER);
        ShakeListener fakeListener = mock(ShakeListener.class);
        motionSensor.startShakeDetection(fakeListener);
        ShakeDetector detector = getDetector(fakeListener, ShakeDetector.class);
        if (detector != null) {
            assertTrue("Sensor Manager must contain sensor event listener for shake",
                    shadowSensorManager.hasListener(detector));
        } else {
            fail(
                    "There should be shake detector in motionSensor. If not, please, check last version of class and update reflection accessing to it field");
        }
    }

    @Test
    public void detectListenerWithStartShakeDetectionWithCustomThreshold() {
        addSensor(TYPE_ACCELEROMETER);
        ShakeListener fakeListener = mock(ShakeListener.class);
        motionSensor.startShakeDetection(4f, 1000, fakeListener);
        ShakeDetector detector = getDetector(fakeListener, ShakeDetector.class);
        if (detector != null) {
            assertTrue("Sensor Manager must contain sensor event listener for shake",
                    shadowSensorManager.hasListener(detector));
        } else {
            fail(
                    "There should be shake detector in motionSensor. If not, please, check last version of class and update reflection accessing to it field");
        }
    }

    @Test
    public void detectNoListenerWithStopFlipDetection() {
        addSensor(TYPE_ACCELEROMETER);
        FlipListener fakeListener = mock(FlipListener.class);
        motionSensor.startFlipDetection(fakeListener);
        FlipDetector detector = getDetector(fakeListener, FlipDetector.class);
        if (detector != null) {
            assertTrue("Sensor Manager must contain sensor event listener for flip",
                    shadowSensorManager.hasListener(detector));
            motionSensor.stopFlipDetection(fakeListener);
            assertFalse("There should be no more sensor event listener in sensor manager",
                    shadowSensorManager.hasListener(detector));
        } else {
            fail(
                    "There should be flip detector in motionSensor. If not, please, check last version of class and update reflection accessing to it field");
        }
    }

    @Test
    public void detectNoListenerWithStopLightDetection() {
        addSensor(TYPE_LIGHT);
        LightListener fakeListener = mock(LightListener.class);
        motionSensor.startLightDetection(fakeListener);
        LightDetector detector = getDetector(fakeListener, LightDetector.class);
        if (detector != null) {
            assertTrue("Sensor Manager must contain sensor event listener for light",
                    shadowSensorManager.hasListener(detector));
            motionSensor.stopLightDetection(fakeListener);
            assertFalse("There should be no more sensor event listener in sensor manager",
                    shadowSensorManager.hasListener(detector));
        } else {
            fail(
                    "There should be light detector in motionSensor. If not, please, check last version of class and update reflection accessing to it field");
        }
    }

    @Test
    public void detectNoListenerWithStopOrientationDetection() {
        addSensor(TYPE_ACCELEROMETER);
        addSensor(TYPE_MAGNETIC_FIELD);
        OrientationListener fakeListener = mock(OrientationListener.class);
        motionSensor.startOrientationDetection(fakeListener);
        OrientationDetector detector = getDetector(fakeListener, OrientationDetector.class);
        if (detector != null) {
            assertTrue("Sensor Manager must contain sensor event listener for orientation",
                    shadowSensorManager.hasListener(detector));
            motionSensor.stopOrientationDetection(fakeListener);
            assertFalse("There should be no more sensor event listener in sensor manager",
                    shadowSensorManager.hasListener(detector));
        } else {
            fail(
                    "There should be orientation detector in motionSensor. If not, please, check last version of class and update reflection accessing to it field");
        }
    }

    @Test
    public void detectNoListenerWithStopProximityDetection() {
        addSensor(TYPE_PROXIMITY);
        ProximityListener fakeListener = mock(ProximityListener.class);
        motionSensor.startProximityDetection(fakeListener);
        ProximityDetector detector = getDetector(fakeListener, ProximityDetector.class);
        if (detector != null) {
            assertTrue("Sensor Manager must contain sensor event listener for proximity",
                    shadowSensorManager.hasListener(detector));
            motionSensor.stopProximityDetection(fakeListener);
            assertFalse("There should be no more sensor event listener in sensor manager",
                    shadowSensorManager.hasListener(detector));
        } else {
            fail(
                    "There should be proximity detector in motionSensor. If not, please, check last version of class and update reflection accessing to it field");
        }
    }

    @Test
    public void detectNoListenerWithStopShakeDetection() {
        addSensor(TYPE_ACCELEROMETER);
        ShakeListener fakeListener = mock(ShakeListener.class);
        motionSensor.startShakeDetection(fakeListener);
        ShakeDetector detector = getDetector(fakeListener, ShakeDetector.class);
        if (detector != null) {
            assertTrue("Sensor Manager must contain sensor event listener for shake",
                    shadowSensorManager.hasListener(detector));
            motionSensor.stopShakeDetection(fakeListener);
            assertFalse("There should be no more sensor event listener in sensor manager",
                    shadowSensorManager.hasListener(detector));
        } else {
            fail(
                    "There should be shake detector in motionSensor. If not, please, check last version of class and update reflection accessing to it field");
        }
    }

    @Test
    public void detectNoListenerWithStoppingTwoSameDetections() {
        addSensor(TYPE_PROXIMITY);
        ProximityListener fakeListener1 = mock(ProximityListener.class);
        ProximityListener fakeListener2 = mock(ProximityListener.class);
        ProximityDetector detector1 = startProximityDetection(fakeListener1);
        ProximityDetector detector2 = startProximityDetection(fakeListener2);
        motionSensor.stopProximityDetection(fakeListener1);
        motionSensor.stopProximityDetection(fakeListener2);
        assertFalse("Sensor manager need to contain no detectors",
                shadowSensorManager.hasListener(detector2));
        assertFalse("Sensor manager need to contain no detectors",
                shadowSensorManager.hasListener(detector1));
    }

    @Before
    public void setUp() {
        Context context = RuntimeEnvironment.application.getApplicationContext();
        shadowSensorManager =
                Shadows.shadowOf((SensorManager) context.getSystemService(SENSOR_SERVICE));

        motionSensor = MotionSensor.getInstance();
        motionSensor.init(context);
    }

    @After
    public void tearDown() {
        motionSensor = null;
        shadowSensorManager = null;
    }

    private void addSensor(int type) {
        shadowSensorManager.addSensor(type, mock(Sensor.class));
    }

    //Hardcode because of can not get appropriate detector from MotionSensor.class
    private <T> T getDetector(Object listener, Class<T> aClass) {
        T result = null;

        try {
            Field field = motionSensor.getClass().getDeclaredField("defaultSensorsMap");
            field.setAccessible(true);
            Map<Object, SensorDetector> defaults = (Map<Object, SensorDetector>) field.get(motionSensor);
            result = aClass.cast(defaults.get(listener));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return result;
    }

    private String getFieldName(Class aClass) {
        if (aClass == ShakeDetector.class) {
            return "shakeDetector";
        } else if (aClass == ProximityDetector.class) {
            return "proximityDetector";
        } else if (aClass == OrientationDetector.class) {
            return "orientationDetector";
        } else if (aClass == LightDetector.class) {
            return "lightDetector";
        } else if (aClass == FlipDetector.class) {
            return "flipDetector";
        } else if (aClass == WaveDetector.class) {
            return "waveDetector";
        } else {
            return null;
        }
    }

    private ProximityDetector startProximityDetection(ProximityListener listener) {
        motionSensor.startProximityDetection(listener);
        return getDetector(listener, ProximityDetector.class);
    }
}
