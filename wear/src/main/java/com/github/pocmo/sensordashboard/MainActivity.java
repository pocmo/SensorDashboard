package com.github.pocmo.sensordashboard;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import java.util.HashMap;
import java.util.Random;

public class MainActivity extends Activity implements SensorEventListener {

    private static final String TAG = "MainActivity";

    private DeviceClient client;
    private Random random;

    private final static int SENS_ACCELEROMETER = Sensor.TYPE_ACCELEROMETER;
    private final static int SENS_AMBIENT_TEMPERATURE = Sensor.TYPE_AMBIENT_TEMPERATURE;
    private final static int SENS_GAME_ROTATION_VECTOR = Sensor.TYPE_GAME_ROTATION_VECTOR;
    private final static int SENS_GEOMAGNETIC = Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR;
    private final static int SENS_GRAVITY = Sensor.TYPE_GRAVITY;
    private final static int SENS_GYROSCOPE = Sensor.TYPE_GYROSCOPE;
    private final static int SENS_GYROSCOPE_UNCALIBRATED = Sensor.TYPE_GYROSCOPE_UNCALIBRATED;
    private final static int SENS_HEARTRATE = Sensor.TYPE_HEART_RATE;
    private final static int SENS_LIGHT = Sensor.TYPE_LIGHT;
    private final static int SENS_LINEAR_ACCELERATION = Sensor.TYPE_LINEAR_ACCELERATION;
    private final static int SENS_MAGNETIC_FIELD = Sensor.TYPE_MAGNETIC_FIELD;
    private final static int SENS_MAGNETIC_FIELD_UNCALIBRATED = Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED;
    private final static int SENS_PRESSURE = Sensor.TYPE_PRESSURE;
    private final static int SENS_PROXIMITY = Sensor.TYPE_PROXIMITY;
    private final static int SENS_HUMIDITY = Sensor.TYPE_RELATIVE_HUMIDITY;
    private final static int SENS_ROTATION_VECTOR = Sensor.TYPE_ROTATION_VECTOR;
    private final static int SENS_SIGNIFICANT_MOTION = Sensor.TYPE_SIGNIFICANT_MOTION;
    private final static int SENS_STEP_COUNTER = Sensor.TYPE_STEP_COUNTER;
    private final static int SENS_STEP_DETECTOR = Sensor.TYPE_STEP_DETECTOR;

    SensorManager mSensorManager;

    private Sensor mAccelerometerSensor;
    private Sensor mAmbientTemperatureSensor;
    private Sensor mGameRotationVectorSensor;
    private Sensor mGeomagneticSensor;
    private Sensor mGravitySensor;
    private Sensor mGyroscopeSensor;
    private Sensor mGyroscopeUncalibratedSensor;
    private Sensor mHeartrateSensor;
    private Sensor mLightSensor;
    private Sensor mLinearAccelerationSensor;
    private Sensor mMagneticFieldSensor;
    private Sensor mMagneticFieldUncalibratedSensor;
    private Sensor mPressureSensor;
    private Sensor mProximitySensor;
    private Sensor mHumiditySensor;
    private Sensor mRotationVectorSensor;
    private Sensor mSignificantMotionSensor;
    private Sensor mStepCounterSensor;
    private Sensor mStepDetectorSensor;

    private HashMap<Integer, Long> lastTimeStamps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        lastTimeStamps = new HashMap<Integer, Long>();

        random = new Random();
        client = DeviceClient.getInstance(this);

        // TODO: Keep the Wear screen always on (for testing only!)
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        //Sensor and sensor manager
        mSensorManager = ((SensorManager) getSystemService(SENSOR_SERVICE));

        mAccelerometerSensor = mSensorManager.getDefaultSensor(SENS_ACCELEROMETER);
        mAmbientTemperatureSensor = mSensorManager.getDefaultSensor(SENS_AMBIENT_TEMPERATURE);
        mGameRotationVectorSensor = mSensorManager.getDefaultSensor(SENS_GAME_ROTATION_VECTOR);
        mGeomagneticSensor = mSensorManager.getDefaultSensor(SENS_GEOMAGNETIC);
        mGravitySensor = mSensorManager.getDefaultSensor(SENS_GRAVITY);
        mGyroscopeSensor = mSensorManager.getDefaultSensor(SENS_GYROSCOPE);
        mGyroscopeUncalibratedSensor = mSensorManager.getDefaultSensor(SENS_GYROSCOPE_UNCALIBRATED);
        mHeartrateSensor = mSensorManager.getDefaultSensor(SENS_HEARTRATE);
        mLightSensor = mSensorManager.getDefaultSensor(SENS_LIGHT);
        mLinearAccelerationSensor = mSensorManager.getDefaultSensor(SENS_LINEAR_ACCELERATION);
        mMagneticFieldSensor = mSensorManager.getDefaultSensor(SENS_MAGNETIC_FIELD);
        mMagneticFieldUncalibratedSensor = mSensorManager.getDefaultSensor(SENS_MAGNETIC_FIELD_UNCALIBRATED);
        mPressureSensor = mSensorManager.getDefaultSensor(SENS_PRESSURE);
        mProximitySensor = mSensorManager.getDefaultSensor(SENS_PROXIMITY);
        mHumiditySensor = mSensorManager.getDefaultSensor(SENS_HUMIDITY);
        mRotationVectorSensor = mSensorManager.getDefaultSensor(SENS_ROTATION_VECTOR);
        mSignificantMotionSensor = mSensorManager.getDefaultSensor(SENS_SIGNIFICANT_MOTION);
        mStepCounterSensor = mSensorManager.getDefaultSensor(SENS_STEP_COUNTER);
        mStepDetectorSensor = mSensorManager.getDefaultSensor(SENS_STEP_DETECTOR);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Register the listener
        if (mSensorManager != null) {
            if (mAccelerometerSensor != null) {
                mSensorManager.registerListener(this, mAccelerometerSensor, SensorManager.SENSOR_DELAY_FASTEST);
            } else {
                Log.w(TAG, "No Accelerometer found");
            }

            if (mAmbientTemperatureSensor != null) {
                mSensorManager.registerListener(this, mAmbientTemperatureSensor, SensorManager.SENSOR_DELAY_FASTEST);
            } else {
                Log.w(TAG, "Ambient Temperature Sensor not found");
            }

            if (mGameRotationVectorSensor != null) {
                mSensorManager.registerListener(this, mGameRotationVectorSensor, SensorManager.SENSOR_DELAY_FASTEST);
            } else {
                Log.w(TAG, "Gaming Rotation Vector Sensor not found");
            }

            if (mGeomagneticSensor != null) {
                mSensorManager.registerListener(this, mGeomagneticSensor, SensorManager.SENSOR_DELAY_FASTEST);
            } else {
                Log.w(TAG, "No Geomagnetic Sensor found");
            }

            if (mGravitySensor != null) {
                mSensorManager.registerListener(this, mGravitySensor, SensorManager.SENSOR_DELAY_FASTEST);
            } else {
                Log.w(TAG, "No Gravity Sensor");
            }

            if (mGyroscopeSensor != null) {
                mSensorManager.registerListener(this, mGyroscopeSensor, SensorManager.SENSOR_DELAY_FASTEST);
            } else {
                Log.w(TAG, "No Gyroscope Sensor found");
            }

            if (mGyroscopeUncalibratedSensor != null) {
                mSensorManager.registerListener(this, mGyroscopeUncalibratedSensor, SensorManager.SENSOR_DELAY_FASTEST);
            } else {
                Log.w(TAG, "No Uncalibrated Gyroscope Sensor found");
            }

            if (mHeartrateSensor != null) {
                mSensorManager.registerListener(this, mHeartrateSensor, SensorManager.SENSOR_DELAY_FASTEST);
            } else {
                Log.d(TAG, "No Heartrate Sensor found");
            }

            if (mLightSensor != null) {
                mSensorManager.registerListener(this, mLightSensor, SensorManager.SENSOR_DELAY_FASTEST);
            } else {
                Log.d(TAG, "No Light Sensor found");
            }

            if (mLinearAccelerationSensor != null) {
                mSensorManager.registerListener(this, mLinearAccelerationSensor, SensorManager.SENSOR_DELAY_FASTEST);
            } else {
                Log.d(TAG, "No Linear Acceleration Sensor found");
            }

            if (mMagneticFieldSensor != null) {
                mSensorManager.registerListener(this, mMagneticFieldSensor, SensorManager.SENSOR_DELAY_FASTEST);
            } else {
                Log.d(TAG, "No Magnetic Field Sensor found");
            }

            if (mMagneticFieldUncalibratedSensor != null) {
                mSensorManager.registerListener(this, mMagneticFieldUncalibratedSensor, SensorManager.SENSOR_DELAY_FASTEST);
            } else {
                Log.d(TAG, "No uncalibrated Magnetic Field Sensor found");
            }

            if (mPressureSensor != null) {
                mSensorManager.registerListener(this, mPressureSensor, SensorManager.SENSOR_DELAY_FASTEST);
            } else {
                Log.d(TAG, "No Pressure Sensor found");
            }

            if (mProximitySensor != null) {
                mSensorManager.registerListener(this, mProximitySensor, SensorManager.SENSOR_DELAY_FASTEST);
            } else {
                Log.d(TAG, "No Proximity Sensor found");
            }

            if (mHumiditySensor != null) {
                mSensorManager.registerListener(this, mHumiditySensor, SensorManager.SENSOR_DELAY_FASTEST);
            } else {
                Log.d(TAG, "No Humidity Sensor found");
            }

            if (mRotationVectorSensor != null) {
                mSensorManager.registerListener(this, mRotationVectorSensor, SensorManager.SENSOR_DELAY_FASTEST);
            } else {
                Log.d(TAG, "No Rotation Vector Sensor found");
            }

            if (mSignificantMotionSensor != null) {
                mSensorManager.registerListener(this, mSignificantMotionSensor, SensorManager.SENSOR_DELAY_FASTEST);
            } else {
                Log.d(TAG, "No Significant Motion Sensor found");
            }

            if (mStepCounterSensor != null) {
                mSensorManager.registerListener(this, mStepCounterSensor, SensorManager.SENSOR_DELAY_FASTEST);
            } else {
                Log.d(TAG, "No Step Counter Sensor found");
            }

            if (mStepDetectorSensor != null) {
                mSensorManager.registerListener(this, mStepDetectorSensor, SensorManager.SENSOR_DELAY_FASTEST);
            } else {
                Log.d(TAG, "No Step Detector Sensor found");
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mSensorManager != null)
            mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Long lastTimeStampForSensor = lastTimeStamps.get(event.sensor.getType());

        if (lastTimeStampForSensor == null || (event.timestamp - lastTimeStampForSensor) > 100000000) {

            client.sendSensorData(event.sensor.getType(), event.accuracy, event.timestamp, event.values);

            lastTimeStamps.put(event.sensor.getType(), event.timestamp);
        }
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


    public void onBeep(View view) {
        client.sendSensorData(0, 1, System.currentTimeMillis(), new float[]{random.nextFloat()});
    }
}
