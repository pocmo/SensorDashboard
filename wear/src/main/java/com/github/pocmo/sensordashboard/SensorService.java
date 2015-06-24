package com.github.pocmo.sensordashboard;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.util.Log;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SensorService extends Service implements SensorEventListener {
    private static final String TAG = "SensorDashboard/SensorService";

    private final static int SENS_ACCELEROMETER = Sensor.TYPE_ACCELEROMETER;
    private final static int SENS_MAGNETIC_FIELD = Sensor.TYPE_MAGNETIC_FIELD;
    // 3 = @Deprecated Orientation
    private final static int SENS_GYROSCOPE = Sensor.TYPE_GYROSCOPE;
    private final static int SENS_LIGHT = Sensor.TYPE_LIGHT;
    private final static int SENS_PRESSURE = Sensor.TYPE_PRESSURE;
    // 7 = @Deprecated Temperature
    private final static int SENS_PROXIMITY = Sensor.TYPE_PROXIMITY;
    private final static int SENS_GRAVITY = Sensor.TYPE_GRAVITY;
    private final static int SENS_LINEAR_ACCELERATION = Sensor.TYPE_LINEAR_ACCELERATION;
    private final static int SENS_ROTATION_VECTOR = Sensor.TYPE_ROTATION_VECTOR;
    private final static int SENS_HUMIDITY = Sensor.TYPE_RELATIVE_HUMIDITY;
    // TODO: there's no Android Wear devices yet with a body temperature monitor
    private final static int SENS_AMBIENT_TEMPERATURE = Sensor.TYPE_AMBIENT_TEMPERATURE;
    private final static int SENS_MAGNETIC_FIELD_UNCALIBRATED = Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED;
    private final static int SENS_GAME_ROTATION_VECTOR = Sensor.TYPE_GAME_ROTATION_VECTOR;
    private final static int SENS_GYROSCOPE_UNCALIBRATED = Sensor.TYPE_GYROSCOPE_UNCALIBRATED;
    private final static int SENS_SIGNIFICANT_MOTION = Sensor.TYPE_SIGNIFICANT_MOTION;
    private final static int SENS_STEP_DETECTOR = Sensor.TYPE_STEP_DETECTOR;
    private final static int SENS_STEP_COUNTER = Sensor.TYPE_STEP_COUNTER;
    private final static int SENS_GEOMAGNETIC = Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR;
    private final static int SENS_HEARTRATE = Sensor.TYPE_HEART_RATE;

    SensorManager mSensorManager;

    private Sensor mHeartrateSensor;

    private DeviceClient client;

    @Override
    public void onCreate() {
        super.onCreate();

        client = DeviceClient.getInstance(this);

        Notification.Builder builder = new Notification.Builder(this);
        builder.setContentTitle("Sensor Dashboard");
        builder.setContentText("Collecting sensor data..");
        builder.setSmallIcon(R.drawable.ic_launcher);

        startForeground(1, builder.build());

        startMeasurement();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        stopMeasurement();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    protected void startMeasurement() {
        mSensorManager = ((SensorManager) getSystemService(SENSOR_SERVICE));

        Sensor accelerometerSensor = mSensorManager.getDefaultSensor(SENS_ACCELEROMETER);
        Sensor ambientTemperatureSensor = mSensorManager.getDefaultSensor(SENS_AMBIENT_TEMPERATURE);
        Sensor gameRotationVectorSensor = mSensorManager.getDefaultSensor(SENS_GAME_ROTATION_VECTOR);
        Sensor geomagneticSensor = mSensorManager.getDefaultSensor(SENS_GEOMAGNETIC);
        Sensor gravitySensor = mSensorManager.getDefaultSensor(SENS_GRAVITY);
        Sensor gyroscopeSensor = mSensorManager.getDefaultSensor(SENS_GYROSCOPE);
        Sensor gyroscopeUncalibratedSensor = mSensorManager.getDefaultSensor(SENS_GYROSCOPE_UNCALIBRATED);
        mHeartrateSensor = mSensorManager.getDefaultSensor(SENS_HEARTRATE);
        Sensor heartrateSamsungSensor = mSensorManager.getDefaultSensor(65562);
        Sensor lightSensor = mSensorManager.getDefaultSensor(SENS_LIGHT);
        Sensor linearAccelerationSensor = mSensorManager.getDefaultSensor(SENS_LINEAR_ACCELERATION);
        Sensor magneticFieldSensor = mSensorManager.getDefaultSensor(SENS_MAGNETIC_FIELD);
        Sensor magneticFieldUncalibratedSensor = mSensorManager.getDefaultSensor(SENS_MAGNETIC_FIELD_UNCALIBRATED);
        Sensor pressureSensor = mSensorManager.getDefaultSensor(SENS_PRESSURE);
        Sensor proximitySensor = mSensorManager.getDefaultSensor(SENS_PROXIMITY);
        Sensor humiditySensor = mSensorManager.getDefaultSensor(SENS_HUMIDITY);
        Sensor rotationVectorSensor = mSensorManager.getDefaultSensor(SENS_ROTATION_VECTOR);
        Sensor significantMotionSensor = mSensorManager.getDefaultSensor(SENS_SIGNIFICANT_MOTION);
        Sensor stepCounterSensor = mSensorManager.getDefaultSensor(SENS_STEP_COUNTER);
        Sensor stepDetectorSensor = mSensorManager.getDefaultSensor(SENS_STEP_DETECTOR);

        Log.i("Here","finished creating sensor manager");


        boolean started=false;





        while(!started)
        {
            // Register the listener
            if (mSensorManager != null) {
                if (accelerometerSensor != null) {
                    mSensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
                    started=true;
                } else {
                    Log.d(TAG, "No Accelerometer found");
                }

                if (ambientTemperatureSensor != null) {
                    mSensorManager.registerListener(this, ambientTemperatureSensor, SensorManager.SENSOR_DELAY_NORMAL);
                    started=true;
                } else {
                    Log.d(TAG, "Ambient Temperature Sensor not found");
                }

                if (gameRotationVectorSensor != null) {
                    mSensorManager.registerListener(this, gameRotationVectorSensor, SensorManager.SENSOR_DELAY_NORMAL);
                    started=true;
                } else {
                    Log.d(TAG, "Gaming Rotation Vector Sensor not found");
                }

                if (geomagneticSensor != null) {
                    mSensorManager.registerListener(this, geomagneticSensor, SensorManager.SENSOR_DELAY_NORMAL);
                    started=true;
                } else {
                    Log.d(TAG, "No Geomagnetic Sensor found");
                }

                if (gravitySensor != null) {
                    mSensorManager.registerListener(this, gravitySensor, SensorManager.SENSOR_DELAY_NORMAL);
                    started=true;
                } else {
                    Log.w(TAG, "No Gravity Sensor");
                }

                if (gyroscopeSensor != null) {
                    mSensorManager.registerListener(this, gyroscopeSensor, SensorManager.SENSOR_DELAY_NORMAL);
                    started=true;
                } else {
                    Log.w(TAG, "No Gyroscope Sensor found");
                }

                if (gyroscopeUncalibratedSensor != null) {
                    mSensorManager.registerListener(this, gyroscopeUncalibratedSensor, SensorManager.SENSOR_DELAY_NORMAL);
                    started=true;
                } else {
                    Log.w(TAG, "No Uncalibrated Gyroscope Sensor found");
                }

                if (mHeartrateSensor != null) {
                    final int measurementDuration   = 10;   // Seconds
                    final int measurementBreak      = 5;    // Seconds

                    ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
                    scheduler.scheduleAtFixedRate(
                            new Runnable() {
                                @Override
                                public void run() {
                                    Log.v(TAG, "register Heartrate Sensor");
                                    mSensorManager.registerListener(SensorService.this, mHeartrateSensor, SensorManager.SENSOR_DELAY_NORMAL);

                                    try {
                                        Thread.sleep(measurementDuration * 1000);
                                    } catch (InterruptedException e) {
                                        Log.e(TAG, "Interrupted while waitting to unregister Heartrate Sensor");
                                    }

                                    Log.v(TAG, "unregister Heartrate Sensor");
                                    mSensorManager.unregisterListener(SensorService.this, mHeartrateSensor);
                                }
                            }, 3, measurementDuration + measurementBreak, TimeUnit.SECONDS);

                    started=true;
                } else {
                    Log.d(TAG, "No Heartrate Sensor found");
                }

                if (heartrateSamsungSensor != null) {
                    mSensorManager.registerListener(this, heartrateSamsungSensor, SensorManager.SENSOR_DELAY_FASTEST);
                    started=true;
                } else {
                    Log.d(TAG, "Samsungs Heartrate Sensor not found");
                }

                if (lightSensor != null) {
                    mSensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
                    started=true;
                } else {
                    Log.d(TAG, "No Light Sensor found");
                }

                if (linearAccelerationSensor != null) {
                    mSensorManager.registerListener(this, linearAccelerationSensor, SensorManager.SENSOR_DELAY_NORMAL);
                    started=true;
                } else {
                    Log.d(TAG, "No Linear Acceleration Sensor found");
                }

                if (magneticFieldSensor != null) {
                    mSensorManager.registerListener(this, magneticFieldSensor, SensorManager.SENSOR_DELAY_NORMAL);
                    started=true;
                } else {
                    Log.d(TAG, "No Magnetic Field Sensor found");
                }

                if (magneticFieldUncalibratedSensor != null) {
                    mSensorManager.registerListener(this, magneticFieldUncalibratedSensor, SensorManager.SENSOR_DELAY_NORMAL);
                    started=true;
                } else {
                    Log.d(TAG, "No uncalibrated Magnetic Field Sensor found");
                }

                if (pressureSensor != null) {
                    mSensorManager.registerListener(this, pressureSensor, SensorManager.SENSOR_DELAY_NORMAL);
                    started=true;
                } else {
                    Log.d(TAG, "No Pressure Sensor found");
                }

                if (proximitySensor != null) {
                    mSensorManager.registerListener(this, proximitySensor, SensorManager.SENSOR_DELAY_NORMAL);
                    started=true;
                } else {
                    Log.d(TAG, "No Proximity Sensor found");
                }

                if (humiditySensor != null) {
                    mSensorManager.registerListener(this, humiditySensor, SensorManager.SENSOR_DELAY_NORMAL);
                    started=true;
                } else {
                    Log.d(TAG, "No Humidity Sensor found");
                }

                if (rotationVectorSensor != null) {
                    mSensorManager.registerListener(this, rotationVectorSensor, SensorManager.SENSOR_DELAY_NORMAL);
                    started=true;
                } else {
                    Log.d(TAG, "No Rotation Vector Sensor found");
                }

                if (significantMotionSensor != null) {
                    mSensorManager.registerListener(this, significantMotionSensor, SensorManager.SENSOR_DELAY_NORMAL);
                    started=true;
                } else {
                    Log.d(TAG, "No Significant Motion Sensor found");
                }

                if (stepCounterSensor != null) {
                    mSensorManager.registerListener(this, stepCounterSensor, SensorManager.SENSOR_DELAY_NORMAL);
                    started=true;
                } else {
                    Log.d(TAG, "No Step Counter Sensor found");
                }

                if (stepDetectorSensor != null) {
                    mSensorManager.registerListener(this, stepDetectorSensor, SensorManager.SENSOR_DELAY_NORMAL);
                    started=true;
                } else {
                    Log.d(TAG, "No Step Detector Sensor found");
                }
            }


            if(!started)
            {
                Log.d(TAG, "Try again to find sensors");
                 accelerometerSensor = mSensorManager.getDefaultSensor(SENS_ACCELEROMETER);
                 ambientTemperatureSensor = mSensorManager.getDefaultSensor(SENS_AMBIENT_TEMPERATURE);
                 gameRotationVectorSensor = mSensorManager.getDefaultSensor(SENS_GAME_ROTATION_VECTOR);
                 geomagneticSensor = mSensorManager.getDefaultSensor(SENS_GEOMAGNETIC);
                 gravitySensor = mSensorManager.getDefaultSensor(SENS_GRAVITY);
                 gyroscopeSensor = mSensorManager.getDefaultSensor(SENS_GYROSCOPE);
                 gyroscopeUncalibratedSensor = mSensorManager.getDefaultSensor(SENS_GYROSCOPE_UNCALIBRATED);
                mHeartrateSensor = mSensorManager.getDefaultSensor(SENS_HEARTRATE);
                 heartrateSamsungSensor = mSensorManager.getDefaultSensor(65562);
                 lightSensor = mSensorManager.getDefaultSensor(SENS_LIGHT);
                 linearAccelerationSensor = mSensorManager.getDefaultSensor(SENS_LINEAR_ACCELERATION);
                 magneticFieldSensor = mSensorManager.getDefaultSensor(SENS_MAGNETIC_FIELD);
                 magneticFieldUncalibratedSensor = mSensorManager.getDefaultSensor(SENS_MAGNETIC_FIELD_UNCALIBRATED);
                 pressureSensor = mSensorManager.getDefaultSensor(SENS_PRESSURE);
                 proximitySensor = mSensorManager.getDefaultSensor(SENS_PROXIMITY);
                 humiditySensor = mSensorManager.getDefaultSensor(SENS_HUMIDITY);
                 rotationVectorSensor = mSensorManager.getDefaultSensor(SENS_ROTATION_VECTOR);
                 significantMotionSensor = mSensorManager.getDefaultSensor(SENS_SIGNIFICANT_MOTION);
                 stepCounterSensor = mSensorManager.getDefaultSensor(SENS_STEP_COUNTER);
                 stepDetectorSensor = mSensorManager.getDefaultSensor(SENS_STEP_DETECTOR);

            }
            else{
                Log.d(TAG, "Sensor found");
            }

        }



    }

    private void stopMeasurement() {
        if (mSensorManager != null)
            mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        client.sendSensorData(event.sensor.getType(), event.accuracy, event.timestamp, event.values);
  //      Log.d(TAG, "Sensor data changed");
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
