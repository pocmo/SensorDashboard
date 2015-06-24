package com.github.pocmo.sensordashboard.data;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by harry on 6/23/15.
 */
public class DataRecord {


    private static final String TAG = "SensorDashboard/DataRecord";
    public File root_dir ;

    private List<Sensor> sensor_list;

    private List<File> file_list;

    private List<OutputStreamWriter> outputWriterLst;




    public DataRecord(String root_path){

        File sdCard = Environment.getExternalStorageDirectory();
        root_dir = new File (sdCard.getAbsolutePath() + "/sensor_data/"+ root_path);
        root_dir.mkdir();

        sensor_list=new LinkedList<Sensor>();
        file_list=new LinkedList<File>();
        outputWriterLst=new LinkedList<OutputStreamWriter>();



    }


    public void record_data(Sensor input_sensor,int accuracy, long timestamp, float[] values){


        File temp_file=new File(root_dir,input_sensor.getName()+".csv");

        if(!sensor_list.contains(input_sensor)){
            sensor_list.add(input_sensor);
            file_list.add(temp_file);
            try {
                FileOutputStream fileout=new FileOutputStream(temp_file);
                OutputStreamWriter outputWriter_temp=new OutputStreamWriter(fileout);
                outputWriterLst.add(outputWriter_temp);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        try {
//            FileOutputStream fileout=new FileOutputStream(temp_file);
//            OutputStreamWriter outputWriter=new OutputStreamWriter(fileout);

            outputWriterLst.get(sensor_list.indexOf(input_sensor)).append(Long.toString(timestamp)+" ");

            for(int i=0;i<values.length;i++){
                outputWriterLst.get(sensor_list.indexOf(input_sensor)).append(Float.toString(values[i]) + " ");
            }
            outputWriterLst.get(sensor_list.indexOf(input_sensor)).append("\n");

            Log.i(TAG, "Record sensor data " + temp_file.toString() + " = " + Arrays.toString(values));


//            outputWriter.append(Long.toString(timestamp)+" ");
//            for(int i=0;i<values.length;i++){
//                outputWriter.append(Float.toString(values[i]) + " ");
//            }
//            outputWriter.append("\n");
//            outputWriter.close();

//            Log.v(TAG, "Record sensor data " + temp_file.toString() + " = " + Arrays.toString(values));

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void flush_data(){

        for(OutputStreamWriter outputWriter_temp:outputWriterLst){

            try {
                outputWriter_temp.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

}
