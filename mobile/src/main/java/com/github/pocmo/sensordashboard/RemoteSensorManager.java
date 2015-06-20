package com.github.pocmo.sensordashboard;

import android.content.Context;
import android.util.Log;
import android.util.SparseArray;

import com.github.pocmo.sensordashboard.data.Sensor;
import com.github.pocmo.sensordashboard.data.SensorDataPoint;
import com.github.pocmo.sensordashboard.data.SensorNames;
import com.github.pocmo.sensordashboard.data.TagData;
import com.github.pocmo.sensordashboard.events.BusProvider;
import com.github.pocmo.sensordashboard.events.NewSensorEvent;
import com.github.pocmo.sensordashboard.events.SensorUpdatedEvent;
import com.github.pocmo.sensordashboard.events.TagAddedEvent;
import com.github.pocmo.sensordashboard.shared.ClientPaths;
import com.github.pocmo.sensordashboard.shared.DataMapKeys;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class RemoteSensorManager {
    private static final String TAG = "SensorDashboard/RemoteSensorManager";
    private static final int CLIENT_CONNECTION_TIMEOUT = 15000;

    private static RemoteSensorManager instance;

    private Context context;
    private ExecutorService executorService;
    private SparseArray<Sensor> sensorMapping;
    private ArrayList<Sensor> sensors;
    private SensorNames sensorNames;
    private GoogleApiClient googleApiClient;

    private LinkedList<TagData> tags = new LinkedList<>();

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
        this.sensorNames = new SensorNames();

        this.googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(Wearable.API)
                .build();

        this.executorService = Executors.newCachedThreadPool();
    }

    public List<Sensor> getSensors() {
        return (List<Sensor>) sensors.clone();
    }

    public Sensor getSensor(long id) {
        return sensorMapping.get((int) id);
    }

    private Sensor createSensor(int id) {
        Sensor sensor = new Sensor(id, sensorNames.getName(id));

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

    public synchronized void addTag(String pTagName) {
        TagData tag = new TagData(pTagName, System.currentTimeMillis());
        this.tags.add(tag);


        BusProvider.postOnMainThread(new TagAddedEvent(tag));
    }

    public LinkedList<TagData> getTags() {
        return (LinkedList<TagData>) tags.clone();
    }

    private boolean validateConnection() {
        if (googleApiClient.isConnected()) {
            return true;
        }

        ConnectionResult result = googleApiClient.blockingConnect(CLIENT_CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS);

        return result.isSuccess();
    }

    public void filterBySensorId(final int sensorId) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                filterBySensorIdInBackground(sensorId);
            }
        });
    }

    ;

    private void filterBySensorIdInBackground(final int sensorId) {
        Log.d(TAG, "filterBySensorId(" + sensorId + ")");

        if (validateConnection()) {
            PutDataMapRequest dataMap = PutDataMapRequest.create("/filter");

            dataMap.getDataMap().putInt(DataMapKeys.FILTER, sensorId);
            dataMap.getDataMap().putLong(DataMapKeys.TIMESTAMP, System.currentTimeMillis());

            PutDataRequest putDataRequest = dataMap.asPutDataRequest();
            Wearable.DataApi.putDataItem(googleApiClient, putDataRequest).setResultCallback(new ResultCallback<DataApi.DataItemResult>() {
                @Override
                public void onResult(DataApi.DataItemResult dataItemResult) {
                    Log.d(TAG, "Filter by sensor " + sensorId + ": " + dataItemResult.getStatus().isSuccess());
                }
            });
        }
    }

    public void startMeasurement() {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                controlMeasurementInBackground(ClientPaths.START_MEASUREMENT);
            }
        });
    }

    public void stopMeasurement() {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                controlMeasurementInBackground(ClientPaths.STOP_MEASUREMENT);
            }
        });
    }

    public void getNodes(ResultCallback<NodeApi.GetConnectedNodesResult> pCallback) {
        Wearable.NodeApi.getConnectedNodes(googleApiClient).setResultCallback(pCallback);
    }

    private void controlMeasurementInBackground(final String path) {
        if (validateConnection()) {
            List<Node> nodes = Wearable.NodeApi.getConnectedNodes(googleApiClient).await().getNodes();

            Log.d(TAG, "Sending to nodes: " + nodes.size());

            for (Node node : nodes) {
                Log.i(TAG, "add node " + node.getDisplayName());
                Wearable.MessageApi.sendMessage(
                        googleApiClient, node.getId(), path, null
                ).setResultCallback(new ResultCallback<MessageApi.SendMessageResult>() {
                    @Override
                    public void onResult(MessageApi.SendMessageResult sendMessageResult) {
                        Log.d(TAG, "controlMeasurementInBackground(" + path + "): " + sendMessageResult.getStatus().isSuccess());
                    }
                });
            }
        } else {
            Log.w(TAG, "No connection possible");
        }
    }
}
