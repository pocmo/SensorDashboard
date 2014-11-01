package com.github.pocmo.sensordashboard;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.github.pocmo.sensordashboard.data.DataController;
import com.github.pocmo.sensordashboard.data.Sensor;

import java.util.LinkedList;


public class MainActivity extends ActionBarActivity {

    private TextView pagerIndicatorText;
    private ViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        setSupportActionBar(toolbar);


        pagerIndicatorText = (TextView) findViewById(R.id.pager_indicator);
        pager = (ViewPager) findViewById(R.id.pager);

        pager.setAdapter(new ScreenSlidePagerAdapter(getSupportFragmentManager(), DataController.getInstance(this).getSensors()));

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        private LinkedList<Sensor> sensors;

        public ScreenSlidePagerAdapter(FragmentManager fm, LinkedList<Sensor> symbols) {
            super(fm);
            this.sensors = symbols;
        }

        private Sensor getItemObject(int position) {
            return sensors.get(position);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            return SensorFragment.newInstance(sensors.get(position).getId());
        }

        @Override
        public int getCount() {
            return sensors.size();
        }
    }

}
