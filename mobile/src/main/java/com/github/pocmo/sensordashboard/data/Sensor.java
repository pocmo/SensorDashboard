package com.github.pocmo.sensordashboard.data;

import java.util.LinkedList;

/**
 * Created by juhani on 01/11/14.
 */
public class Sensor {
    private long id;
    private String name;
    private float minValue = 0f;
    private float maxValue = 100f;

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

    public LinkedList<SensorDataPoint> getDataPoints() {
        return (LinkedList<SensorDataPoint>) dataPoints.clone();
    }


    public void addDataPoint(SensorDataPoint dataPoint){
        dataPoints.add(dataPoint);
    }

    public long getId() {
        return id;
    }
}
