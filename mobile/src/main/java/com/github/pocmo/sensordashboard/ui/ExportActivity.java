package com.github.pocmo.sensordashboard.ui;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.github.pocmo.sensordashboard.R;
import com.github.pocmo.sensordashboard.database.DataEntry;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;

public class ExportActivity extends Activity {
    private Realm mRealm;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_export);

        Button exportButton = (Button) findViewById(R.id.exportButton);
        exportButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        final Handler handler = new Handler();  //Optional. Define as a variable in your activity.

                        Runnable r = new Runnable() {
                            @Override
                            public void run() {
                                // your code here
                                handler.post(new Runnable() {
                                    public void run() {
                                        exportFile();
                                    }
                                });
                            }
                        };

                        Thread t = new Thread(r);
                        t.start();
                    }
                }

        );
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void exportFile() {
        mRealm = Realm.getInstance(this);

        RealmResults<DataEntry> result = mRealm.where(DataEntry.class).findAll();
        final int total_row = result.size();
        final int total_col = 8;
        Log.e("SensorDashboard", "total_row = " + total_row);
        final String fileprefix = "export";
        final String date = new SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date());
        final String filename = String.format("%s_%s.txt", fileprefix, date);

        final String directory = Environment.getExternalStorageDirectory().getAbsolutePath() + "/SensorDashboard";

        final File logfile = new File(directory, filename);
        final File logPath = logfile.getParentFile();

        if (!logPath.isDirectory() && !logPath.mkdirs()) {
            Log.e("SensorDashbaord", "Could not create directory for log files");
        }

        try {
            FileWriter filewriter = new FileWriter(logfile);

            String TestString = "";

            FileOutputStream fOut = openFileOutput(filename, MODE_WORLD_READABLE);

            OutputStreamWriter osw = new OutputStreamWriter(fOut);

            // Write the string to the file
            for (int i = 1; i < total_row; i++) {

                TestString += result.get(i).getAndroidDevice().toString();
                TestString += " ,";
                TestString += String.valueOf(result.get(i).getTimestamp());
                TestString += " ,";
                TestString += String.valueOf(result.get(i).getX());
                TestString += " ,";
                TestString += String.valueOf(result.get(i).getY());
                TestString += " ,";
                TestString += String.valueOf(result.get(i).getZ());
                TestString += " ,";
                TestString += String.valueOf(result.get(i).getAccuracy());
                TestString += " ,";
                TestString += String.valueOf(result.get(i).getDatasource());
                TestString += " ,";
                TestString += String.valueOf(result.get(i).getDatatype());
                TestString += "\n";
            }
            filewriter.write(TestString);
            filewriter.flush();
            filewriter.close();
        } catch (IOException ioe) {
            Log.e("SensorDashbaord", "IOException while writing Logfile");
        }
    }
}
