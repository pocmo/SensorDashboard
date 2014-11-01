package com.github.pocmo.sensordashboard.events;

import com.github.pocmo.sensordashboard.data.Sensor;

public class NewSensorEvent {
    private Sensor sensor;

    public NewSensorEvent(Sensor sensor) {
        this.sensor = sensor;
    }

    public Sensor getSensor() {
        return sensor;
    }
}
