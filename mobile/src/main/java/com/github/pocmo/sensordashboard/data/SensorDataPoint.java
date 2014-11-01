package com.github.pocmo.sensordashboard.data;

public class SensorDataPoint {
    private long timestamp;
    private float[] values;
    private int accuracy;

    public SensorDataPoint(long timestamp, int accuracy, float[] values) {
        this.timestamp = timestamp;
        this.accuracy = accuracy;
        this.values = values;
    }

    public float[] getValues() {
        return values;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public int getAccuracy() {
        return accuracy;
    }
}
