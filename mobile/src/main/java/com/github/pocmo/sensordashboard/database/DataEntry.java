package com.github.pocmo.sensordashboard.database;

import io.realm.RealmObject;
import io.realm.annotations.RealmClass;

@RealmClass
public class DataEntry extends RealmObject {
    private String androidDevice;
    private long timestamp;
    private float x;
    private float y;
    private float z;
    private int accuracy;

    private String datasource;
    private long datatype;

    public String getAndroidDevice() {
        return androidDevice;
    }

    public void setAndroidDevice(final String pAndroidDevice) {
        androidDevice = pAndroidDevice;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(final long pTimestamp) {
        timestamp = pTimestamp;
    }

    public float getX() {
        return x;
    }

    public void setX(final float pX) {
        x = pX;
    }

    public float getY() {
        return y;
    }

    public void setY(final float pY) {
        y = pY;
    }

    public float getZ() {
        return z;
    }

    public void setZ(final float pZ) {
        z = pZ;
    }

    public int getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(final int pAccuracy) {
        accuracy = pAccuracy;
    }

    public String getDatasource() {
        return datasource;
    }

    public void setDatasource(final String pDatasource) {
        datasource = pDatasource;
    }

    public long getDatatype() {
        return datatype;
    }

    public void setDatatype(final long pDatatype) {
        datatype = pDatatype;
    }
}
