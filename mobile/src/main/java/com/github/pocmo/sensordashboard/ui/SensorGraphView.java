package com.github.pocmo.sensordashboard.ui;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.hardware.SensorManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.github.pocmo.sensordashboard.R;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by juhani on 01/11/14.
 */
public class SensorGraphView extends View {

    private static final int CIRCLE_SIZE_ACCURACY_HIGH = 4;
    private static final int CIRCLE_SIZE_ACCURACY_MEDIUM = 10;
    private static final int CIRCLE_SIZE_ACCURACY_LOW = 20;


    private static final int MAX_DATA_SIZE = 300;
    private static final int CIRCLE_SIZE_DEFAULT = 4;

    // FIXME don't hardcode 9
    private Paint[] rectPaints = new Paint[9];

    private Paint infoPaint;

    private LinkedList<Float>[] normalisedDataPoints;
    private LinkedList<Integer>[] dataPointsAccuracy;
    private float zeroline = 0;
    private String maxValueLabel = "";
    private String minValueValue = "";

    private boolean[] drawSensors = new boolean[20];

    public SensorGraphView(Context context, AttributeSet attrs) {
        super(context, attrs);

        Resources res = context.getResources();

        rectPaints[0] = new Paint();
        rectPaints[0].setColor(res.getColor(R.color.graph_color_1));

        rectPaints[1] = new Paint();
        rectPaints[1].setColor(res.getColor(R.color.graph_color_2));

        rectPaints[2] = new Paint();
        rectPaints[2].setColor(res.getColor(R.color.graph_color_3));

        rectPaints[3] = new Paint();
        rectPaints[3].setColor(res.getColor(R.color.graph_color_4));

        rectPaints[4] = new Paint();
        rectPaints[4].setColor(res.getColor(R.color.graph_color_5));

        rectPaints[5] = new Paint();
        rectPaints[5].setColor(res.getColor(R.color.graph_color_6));

        rectPaints[6] = new Paint();
        rectPaints[6].setColor(res.getColor(R.color.graph_color_7));

        rectPaints[7] = new Paint();
        rectPaints[7].setColor(res.getColor(R.color.graph_color_8));

        rectPaints[8] = new Paint();
        rectPaints[8].setColor(res.getColor(R.color.graph_color_9));


        infoPaint = new Paint();
        infoPaint.setColor(res.getColor(R.color.graph_color_info));
        infoPaint.setTextSize(48f);
        infoPaint.setAntiAlias(true);

    }


    public void setDrawSensors(boolean[] drawSensors) {
        this.drawSensors = drawSensors;
        invalidate();
    }

    public void setNormalisedDataPoints(LinkedList<Float>[] normalisedDataPoints, LinkedList<Integer>[] dataPointsAccuracy) {

        this.normalisedDataPoints = normalisedDataPoints;

        for (int i = 0; i < this.normalisedDataPoints.length; ++i) {
            if (this.normalisedDataPoints[i].size() > MAX_DATA_SIZE) {

                List tmp = this.normalisedDataPoints[i].subList(this.normalisedDataPoints[i].size() - MAX_DATA_SIZE - 1, this.normalisedDataPoints[i].size() - 1);
                this.normalisedDataPoints[i] = new LinkedList<Float>();
                this.normalisedDataPoints[i].addAll(tmp);
            }
        }


        this.dataPointsAccuracy = dataPointsAccuracy;

        for (int i = 0; i < this.dataPointsAccuracy.length; ++i) {
            if (this.dataPointsAccuracy[i].size() > MAX_DATA_SIZE) {

                List tmp = this.dataPointsAccuracy[i].subList(this.dataPointsAccuracy[i].size() - MAX_DATA_SIZE - 1, this.dataPointsAccuracy[i].size() - 1);
                this.dataPointsAccuracy[i] = new LinkedList<Integer>();
                this.dataPointsAccuracy[i].addAll(tmp);
            }
        }


        for (int i = 0; i < this.dataPointsAccuracy.length; ++i) {


            LinkedList<Integer> tmp = new LinkedList<Integer>();
            for (Integer integer : this.dataPointsAccuracy[i]) {

                tmp.add(dataPointAccuracyToDotSize(integer));
            }
            this.dataPointsAccuracy[i] = tmp;

        }


        invalidate();
    }


    public void setMaxValueLabel(String maxValue) {
        this.maxValueLabel = maxValue;
    }

    public void setMinValueLabel(String minValue) {
        this.minValueValue = minValue;
    }

    public void setZeroLine(float zeroline) {
        this.zeroline = zeroline;
    }

    public void addNewDataPoint(float point, int accuracy, int index) {
        if (index >= normalisedDataPoints.length) {
            throw new ArrayIndexOutOfBoundsException("index too large!!");
        }

        this.normalisedDataPoints[index].add(point);

        if (this.normalisedDataPoints[index].size() > MAX_DATA_SIZE) {
            this.normalisedDataPoints[index].removeFirst();
        }


        this.dataPointsAccuracy[index].add(dataPointAccuracyToDotSize(accuracy));

        if (this.dataPointsAccuracy[index].size() > MAX_DATA_SIZE) {
            this.dataPointsAccuracy[index].removeFirst();
        }


        invalidate();
    }


    private int dataPointAccuracyToDotSize(int accuracy) {

        switch (accuracy) {
            case SensorManager.SENSOR_STATUS_ACCURACY_HIGH:
                return CIRCLE_SIZE_ACCURACY_HIGH;
            case SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM:
                return CIRCLE_SIZE_ACCURACY_MEDIUM;
            case SensorManager.SENSOR_STATUS_ACCURACY_LOW:
                return CIRCLE_SIZE_ACCURACY_LOW;
            default:
                return CIRCLE_SIZE_DEFAULT;
        }

    }


    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (normalisedDataPoints.length <= 0) {
            return;
        }


        int height = canvas.getHeight();
        int width = canvas.getWidth();

        float zeroLine = height - (height * zeroline);


        canvas.drawLine(0, zeroLine, width, zeroLine, infoPaint);
        if (zeroline < 0.8f && zeroline > 0.2f) {
            canvas.drawText("0", width - 70, zeroLine - 5, infoPaint);
        }

        canvas.drawText(maxValueLabel, width - 70, 60, infoPaint);
        canvas.drawText(minValueValue, width - 70, height - 40, infoPaint);


        int maxValues = MAX_DATA_SIZE;


        int pointSpan = width / maxValues;


        float previousX = -1;
        float previousY = -1;
        for (int i = 0; i < this.normalisedDataPoints.length; ++i) {




            if (!drawSensors[i]) {
                continue;
            }

            if (this.normalisedDataPoints[i] == null) {
                continue;
            }
            int currentX = 0;//width - pointSpan;
            int index = 0;
            for (Float dataPoint : this.normalisedDataPoints[i]) {


                float y = height - (height * dataPoint);


//                Log.d("Sensor Graph View", "The length of dataPointsAccuracy: " + Integer.toString(dataPointsAccuracy.length));
//                Log.d("Sensor Graph View", "The length of rectPaints: " + Integer.toString(rectPaints.length));


                //////////////////////////////////////////////////////////////////////////////
                /////////Here is a small patch that handle the case when the number of sensors is too large.
                if(i>=rectPaints.length){
                    canvas.drawCircle(currentX, y, dataPointsAccuracy[i].get(index), rectPaints[rectPaints.length-1]);

                    if (previousX != -1 && previousY != -1) {
                        canvas.drawLine(previousX, previousY, currentX, y, rectPaints[rectPaints.length-1]);

                    }

                }
                else{
                    canvas.drawCircle(currentX, y, dataPointsAccuracy[i].get(index), rectPaints[i]);

                    if (previousX != -1 && previousY != -1) {
                        canvas.drawLine(previousX, previousY, currentX, y, rectPaints[i]);

                    }
                }






                previousX = currentX;
                previousY = y;

                currentX += pointSpan;
                ++index;
            }
            previousX = -1;
            previousY = -1;


        }


    }


}
