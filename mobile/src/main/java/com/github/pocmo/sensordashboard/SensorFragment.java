package com.github.pocmo.sensordashboard;


import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.pocmo.sensordashboard.data.Sensor;
import com.github.pocmo.sensordashboard.data.SensorDataPoint;
import com.github.pocmo.sensordashboard.events.BusProvider;
import com.github.pocmo.sensordashboard.events.SensorRangeEvent;
import com.github.pocmo.sensordashboard.events.SensorUpdatedEvent;
import com.github.pocmo.sensordashboard.ui.SensorGraphView;
import com.squareup.otto.Subscribe;

import java.text.MessageFormat;
import java.util.LinkedList;


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

    private boolean[] drawSensors = new boolean[SensorGraphView.NUM_DRAW_SENSOR];

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


        LinkedList<Float>[] normalisedValues = new LinkedList[dataPoints.getFirst().getValues().length];
        LinkedList<Integer>[] accuracyValues = new LinkedList[dataPoints.getFirst().getValues().length];

        for (int i = 0; i < normalisedValues.length; ++i) {
            normalisedValues[i] = new LinkedList<Float>();
            accuracyValues[i] = new LinkedList<Integer>();
        }


        for (SensorDataPoint dataPoint : dataPoints) {

            for (int i = 0; i < dataPoint.getValues().length; ++i) {
                float normalised = (dataPoint.getValues()[i] - sensor.getMinValue()) / spread;
                normalisedValues[i].add(normalised);
                accuracyValues[i].add(dataPoint.getAccuracy());
            }
        }


        this.sensorview.setNormalisedDataPoints(normalisedValues, accuracyValues);
        this.sensorview.setZeroLine((0 - sensor.getMinValue()) / spread);

        this.sensorview.setMaxValueLabel(MessageFormat.format("{0,number,#}", sensor.getMaxValue()));
        this.sensorview.setMinValueLabel(MessageFormat.format("{0,number,#}", sensor.getMinValue()));

    }

    @Override
    public void onResume() {
        super.onResume();
        initialiseSensorData();
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
    public void onSensorUpdatedEvent(SensorUpdatedEvent event) {
        if (event.getSensor().getId() == this.sensor.getId()) {


            for (int i = 0; i < event.getDataPoint().getValues().length; ++i) {
                float normalised = (event.getDataPoint().getValues()[i] - sensor.getMinValue()) / spread;
                this.sensorview.addNewDataPoint(normalised, event.getDataPoint().getAccuracy(), i);
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
