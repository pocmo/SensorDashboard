package com.github.pocmo.sensordashboard;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.pocmo.sensordashboard.data.DataController;
import com.github.pocmo.sensordashboard.data.Sensor;
import com.github.pocmo.sensordashboard.events.BusProvider;


/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * Use the {@link SensorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SensorFragment extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private long sensorId;
    private Sensor sensor;


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

        sensor = DataController.getInstance(getActivity()).getSensor(sensorId);

        View view = inflater.inflate(R.layout.fragment_symbol, container, false);


        ((TextView) view.findViewById(R.id.title)).setText(sensor.getName());

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();


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


}
