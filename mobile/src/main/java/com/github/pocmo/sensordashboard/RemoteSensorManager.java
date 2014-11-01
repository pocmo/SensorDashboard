package com.github.pocmo.sensordashboard;

import android.content.Context;
import android.util.SparseArray;

import com.github.pocmo.sensordashboard.data.Sensor;
import com.github.pocmo.sensordashboard.data.SensorDataPoint;
import com.github.pocmo.sensordashboard.events.BusProvider;
import com.github.pocmo.sensordashboard.events.NewSensorEvent;
import com.github.pocmo.sensordashboard.events.SensorUpdatedEvent;
import com.squareup.otto.Bus;

import java.util.ArrayList;
import java.util.List;

public class RemoteSensorManager {
    private static RemoteSensorManager instance;

    private Context context;
    private SparseArray<Sensor> sensorMapping;
    private ArrayList<Sensor> sensors;

    public static synchronized RemoteSensorManager getInstance(Context context) {
        if (instance == null) {
            instance = new RemoteSensorManager(context.getApplicationContext());
        }

        return instance;
    }

    private RemoteSensorManager(Context context) {
        this.context = context;
        this.sensorMapping = new SparseArray<Sensor>();
        this.sensors = new ArrayList<Sensor>();
    }

    public List<Sensor> getSensors() {
        return (List<Sensor>) sensors.clone();
    }

    public Sensor getSensor(long id) {
        return sensorMapping.get((int) id);
    }

    private Sensor createSensor(int id) {
        Sensor sensor = new Sensor(id, "Unknown");

        sensors.add(sensor);
        sensorMapping.append(id, sensor);

        BusProvider.postOnMainThread(new NewSensorEvent(sensor));

        return sensor;
    }

    private Sensor getOrCreateSensor(int id) {
        Sensor sensor = sensorMapping.get(id);

        if (sensor == null) {
            sensor = createSensor(id);
        }

        return sensor;
    }

    public synchronized void addSensorData(int sensorType, int accuracy, long timestamp, float[] values) {
        Sensor sensor = getOrCreateSensor(sensorType);

        // TODO: We probably want to pull sensor data point objects from a pool here
        SensorDataPoint dataPoint = new SensorDataPoint(timestamp, accuracy, values);

        sensor.addDataPoint(dataPoint);

        BusProvider.postOnMainThread(new SensorUpdatedEvent(sensor, dataPoint));
    }
}
