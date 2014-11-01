package com.github.pocmo.sensordashboard.data;

import android.util.Log;

import com.github.pocmo.sensordashboard.events.BusProvider;
import com.github.pocmo.sensordashboard.events.SensorRangeEvent;
import com.github.pocmo.sensordashboard.events.SensorUpdatedEvent;

import java.util.LinkedList;

public class Sensor {
    private static final String TAG = "SensorDashboard/Sensor";
    private static final int MAX_DATA_POINTS = 1000;

    private long id;
    private String name;
    private float minValue = Integer.MAX_VALUE;
    private float maxValue = Integer.MIN_VALUE;

    private LinkedList<SensorDataPoint> dataPoints = new LinkedList<SensorDataPoint>();

    public Sensor(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public float getMaxValue() {
        return maxValue;
    }

    public float getMinValue() {
        return minValue;
    }

    public synchronized LinkedList<SensorDataPoint> getDataPoints() {
        return (LinkedList<SensorDataPoint>) dataPoints.clone();
    }

    public synchronized void addDataPoint(SensorDataPoint dataPoint) {
        dataPoints.addLast(dataPoint);

        if (dataPoints.size() > MAX_DATA_POINTS) {
            dataPoints.removeFirst();
        }

        boolean newLimits = false;

        for (float value : dataPoint.getValues()) {
            if (value > maxValue) {
                maxValue = value;
                newLimits = true;
            }
            if (value < minValue) {
                minValue = value;
                newLimits = true;
            }
        }

        if (newLimits) {
            Log.d(TAG, "New range for sensor " + id + ": " + minValue + " - " + maxValue);

            BusProvider.postOnMainThread(new SensorRangeEvent(this));
        }

        BusProvider.postOnMainThread(new SensorUpdatedEvent(this, dataPoint));
    }

    public long getId() {
        return id;
    }
}
