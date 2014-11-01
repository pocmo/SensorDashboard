package com.github.pocmo.sensordashboard;

import android.app.Activity;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {
    private DeviceClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        client = DeviceClient.getInstance(this);
    }

    public void onBeep(View view) {
        client.sendSensorData(
            0,
            1,
            System.currentTimeMillis(),
            new float[] { 1.0f }
        );
    }
}
