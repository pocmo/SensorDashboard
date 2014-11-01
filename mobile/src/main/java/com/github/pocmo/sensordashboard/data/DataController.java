package com.github.pocmo.sensordashboard.data;

import android.content.Context;

import java.util.LinkedList;

/**
 * Created by juhani on 01/11/14.
 */
public class DataController {

    private LinkedList<Sensor> sensors = new LinkedList<Sensor>();
    private Context context;

    private static DataController instance = null;


    private DataController(Context context) {
        this.context = context.getApplicationContext();

        generateDymmyData();
    }


    public Sensor getSensor(long id) {
        for (Sensor sensor : this.sensors) {
            if (sensor.getId() == id) {
                return sensor;
            }
        }
        return null;
    }

    private void generateDymmyData() {

        Sensor sensor = new Sensor(1, "motion");
        sensor.addDataPoint(new SensorDataPoint(System.currentTimeMillis() - 2000, 20f));
        sensor.addDataPoint(new SensorDataPoint(System.currentTimeMillis() - 1000, 24f));
        sensor.addDataPoint(new SensorDataPoint(System.currentTimeMillis() - 0, 22f));
        sensors.add(sensor);

        sensor = new Sensor(2, "temperature");
        sensor.addDataPoint(new SensorDataPoint(System.currentTimeMillis() - 2000, 20f));
        sensor.addDataPoint(new SensorDataPoint(System.currentTimeMillis() - 1000, 10f));
        sensor.addDataPoint(new SensorDataPoint(System.currentTimeMillis() - 0, 2f));
        sensors.add(sensor);

        sensor = new Sensor(3, "speed");
        sensor.addDataPoint(new SensorDataPoint(System.currentTimeMillis() - 2000, 70f));
        sensor.addDataPoint(new SensorDataPoint(System.currentTimeMillis() - 1000, 20f));
        sensor.addDataPoint(new SensorDataPoint(System.currentTimeMillis() - 500, 30f));
        sensors.add(sensor);

    }

    public static DataController getInstance(Context context) {
        if (instance == null) {
            instance = new DataController(context);
        }
        return instance;
    }


    public LinkedList<Sensor> getSensors() {
        return sensors;
    }
}
