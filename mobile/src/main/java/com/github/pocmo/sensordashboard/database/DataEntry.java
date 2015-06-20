package com.github.pocmo.sensordashboard.database;

import io.realm.RealmObject;
import io.realm.annotations.RealmClass;

@RealmClass
public class DataEntry extends RealmObject {
    private int id;
    private long timestamp;
    private float values;
    private int accuracy;

    private String datasource;
    private String datatype;

    public int getId() {
        return id;
    }

    public void setId(final int pId) {
        id = pId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(final long pTimestamp) {
        timestamp = pTimestamp;
    }

    public float getValues() {
        return values;
    }

    public void setValues(final float pValues) {
        values = pValues;
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

    public String getDatatype() {
        return datatype;
    }

    public void setDatatype(final String pDatatype) {
        datatype = pDatatype;
    }
}
