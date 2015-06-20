package com.github.pocmo.sensordashboard;


import android.app.Activity;
import android.content.res.Resources;
import android.nfc.Tag;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.pocmo.sensordashboard.data.Sensor;
import com.github.pocmo.sensordashboard.data.SensorDataPoint;
import com.github.pocmo.sensordashboard.database.DataEntry;
import com.github.pocmo.sensordashboard.events.BusProvider;
import com.github.pocmo.sensordashboard.events.SensorRangeEvent;
import com.github.pocmo.sensordashboard.events.SensorUpdatedEvent;
import com.github.pocmo.sensordashboard.events.TagAddedEvent;
import com.github.pocmo.sensordashboard.ui.SensorGraphView;
import com.squareup.otto.Subscribe;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.LinkedList;

import io.realm.Realm;
import io.realm.RealmResults;


/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * Use the {@link SensorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SensorFragment extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private long sensorId;
    private Sensor sensor;
    private SensorGraphView sensorview;
    private float spread;

    private Realm mRealm;
    private String mAndroidId;

    private boolean[] drawSensors = new boolean[6];

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param sensorId Parameter 1.
     * @return A new instance of fragment SymbolFragment.
     */

    public static SensorFragment newInstance(long sensorId) {
        SensorFragment fragment = new SensorFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_PARAM1, sensorId);
        fragment.setArguments(args);
        return fragment;
    }

    public SensorFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            sensorId = getArguments().getLong(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        sensor = RemoteSensorManager.getInstance(getActivity()).getSensor(sensorId);


        final View view = inflater.inflate(R.layout.fragment_sensor, container, false);


        ((TextView) view.findViewById(R.id.title)).setText(sensor.getName());

        sensorview = (SensorGraphView) view.findViewById(R.id.graph_view);

        Resources res = getResources();

        view.findViewById(R.id.legend1).setBackgroundColor(res.getColor(R.color.graph_color_1));
        view.findViewById(R.id.legend2).setBackgroundColor(res.getColor(R.color.graph_color_2));
        view.findViewById(R.id.legend3).setBackgroundColor(res.getColor(R.color.graph_color_3));
        view.findViewById(R.id.legend4).setBackgroundColor(res.getColor(R.color.graph_color_4));
        view.findViewById(R.id.legend5).setBackgroundColor(res.getColor(R.color.graph_color_5));
        view.findViewById(R.id.legend6).setBackgroundColor(res.getColor(R.color.graph_color_6));


// this could be better.. btu hey, it's a hackathon!
        view.findViewById(R.id.legend1_container).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawSensors[0] = !drawSensors[0];
                sensorview.setDrawSensors(drawSensors);
                v.setSelected(drawSensors[0]);
            }
        });

        view.findViewById(R.id.legend2_container).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawSensors[1] = !drawSensors[1];
                sensorview.setDrawSensors(drawSensors);
                v.setSelected(drawSensors[1]);
            }
        });


        view.findViewById(R.id.legend3_container).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawSensors[2] = !drawSensors[2];
                sensorview.setDrawSensors(drawSensors);
                v.setSelected(drawSensors[2]);
            }
        });


        view.findViewById(R.id.legend4_container).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawSensors[3] = !drawSensors[3];
                sensorview.setDrawSensors(drawSensors);
                v.setSelected(drawSensors[3]);
            }
        });


        view.findViewById(R.id.legend5_container).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawSensors[4] = !drawSensors[4];
                sensorview.setDrawSensors(drawSensors);
                v.setSelected(drawSensors[4]);
            }
        });


        view.findViewById(R.id.legend6_container).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawSensors[5] = !drawSensors[5];
                sensorview.setDrawSensors(drawSensors);
                v.setSelected(drawSensors[5]);
            }
        });

        view.findViewById(R.id.legend1_container).setSelected(true);
        view.findViewById(R.id.legend2_container).setSelected(true);
        view.findViewById(R.id.legend3_container).setSelected(true);
        view.findViewById(R.id.legend4_container).setSelected(true);
        view.findViewById(R.id.legend5_container).setSelected(true);
        view.findViewById(R.id.legend6_container).setSelected(true);


        for (int i = 0; i < drawSensors.length; ++i) {
            drawSensors[i] = true;
        }


        sensorview.setDrawSensors(drawSensors);

        return view;
    }


    private void initialiseSensorData() {
        spread = sensor.getMaxValue() - sensor.getMinValue();
        LinkedList<SensorDataPoint> dataPoints = sensor.getDataPoints();

        if (dataPoints == null || dataPoints.isEmpty()) {
            Log.w("sensor data", "no data found for sensor " + sensor.getId() + " " + sensor.getName());
            return;
        }


        ArrayList<Float>[] normalisedValues = new ArrayList[dataPoints.getFirst().getValues().length];
        ArrayList<Integer>[] accuracyValues = new ArrayList[dataPoints.getFirst().getValues().length];
        ArrayList<Long>[] timestampValues = new ArrayList[dataPoints.getFirst().getValues().length];


        for (int i = 0; i < normalisedValues.length; ++i) {
            normalisedValues[i] = new ArrayList<>();
            accuracyValues[i] = new ArrayList<>();
            timestampValues[i] = new ArrayList<>();
        }


        for (SensorDataPoint dataPoint : dataPoints) {

            for (int i = 0; i < dataPoint.getValues().length; ++i) {
                float normalised = (dataPoint.getValues()[i] - sensor.getMinValue()) / spread;
                normalisedValues[i].add(normalised);
                accuracyValues[i].add(dataPoint.getAccuracy());
                timestampValues[i].add(dataPoint.getTimestamp());
            }
        }


        this.sensorview.setNormalisedDataPoints(normalisedValues, accuracyValues, timestampValues, RemoteSensorManager.getInstance(getActivity()).getTags());
        this.sensorview.setZeroLine((0 - sensor.getMinValue()) / spread);

        this.sensorview.setMaxValueLabel(MessageFormat.format("{0,number,#}", sensor.getMaxValue()));
        this.sensorview.setMinValueLabel(MessageFormat.format("{0,number,#}", sensor.getMinValue()));

    }

    @Override
    public void onResume() {
        super.onResume();
        initialiseSensorData();

        mRealm = Realm.getInstance(getActivity());
        mAndroidId = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        BusProvider.getInstance().register(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        BusProvider.getInstance().unregister(this);
    }


    @Subscribe
    public void onTagAddedEvent(TagAddedEvent event) {
        this.sensorview.addNewTag(event.getTag());
    }

    @Subscribe
    public void onSensorUpdatedEvent(SensorUpdatedEvent event) {
        if (event.getSensor().getId() == this.sensor.getId()) {

            mRealm.beginTransaction();
            DataEntry entry = mRealm.createObject(DataEntry.class);
            entry.setAndroidDevice(mAndroidId);
            entry.setTimestamp(event.getDataPoint().getTimestamp());
            if (event.getDataPoint().getValues().length > 0) {
                entry.setX(event.getDataPoint().getValues()[0]);
            } else {
                entry.setX(0.0f);
            }

            if (event.getDataPoint().getValues().length > 1) {
                entry.setY(event.getDataPoint().getValues()[1]);
            } else {
                entry.setY(0.0f);
            }

            if (event.getDataPoint().getValues().length > 2) {
                entry.setZ(event.getDataPoint().getValues()[2]);
            } else {
                entry.setZ(0.0f);
            }

            entry.setAccuracy(event.getDataPoint().getAccuracy());
            entry.setDatasource("Acc");
            entry.setDatatype(event.getSensor().getId());
            mRealm.commitTransaction();

            for (int i = 0; i < event.getDataPoint().getValues().length; ++i) {
                float normalised = (event.getDataPoint().getValues()[i] - sensor.getMinValue()) / spread;
                this.sensorview.addNewDataPoint(normalised, event.getDataPoint().getAccuracy(), i, event.getDataPoint().getTimestamp());
            }
        }
    }


    @Subscribe
    public void onSensorRangeEvent(SensorRangeEvent event) {
        if (event.getSensor().getId() == this.sensor.getId()) {
            initialiseSensorData();
        }
    }

}
