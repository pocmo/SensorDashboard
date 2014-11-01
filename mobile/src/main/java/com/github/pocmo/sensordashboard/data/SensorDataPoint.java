package com.github.pocmo.sensordashboard.data;

/**
 * Created by juhani on 01/11/14.
 */
public class SensorDataPoint {

    private long timestamp;
    private float value;


    public SensorDataPoint(long timestamp, float value) {
        this.timestamp = timestamp;
        this.value = value;
    }

    public float getValue() {
        return value;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
