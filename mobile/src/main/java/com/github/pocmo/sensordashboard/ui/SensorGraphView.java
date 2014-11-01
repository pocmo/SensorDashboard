package com.github.pocmo.sensordashboard.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.LinkedList;

/**
 * Created by juhani on 01/11/14.
 */
public class SensorGraphView extends View {

    private static final int MAX_DATA_SIZE = 200;


    private Paint[] rectPaints = new Paint[3];

    private LinkedList<Float>[] normalisedDataPoints = new LinkedList[3];


    public SensorGraphView(Context context, AttributeSet attrs) {
        super(context, attrs);

        rectPaints[0] = new Paint();
        rectPaints[0].setColor(0xFFFF0000);

        rectPaints[0] = new Paint();
        rectPaints[0].setColor(0xFFFFFF00);

        rectPaints[0] = new Paint();
        rectPaints[0].setColor(0xFFFF00FF);
    }


    public void setNormalisedDataPoints(LinkedList<Float>[] normalisedDataPoints) {

        this.normalisedDataPoints = normalisedDataPoints;


        //TODO remove extra points
        invalidate();
    }


    public void addNewDataPoint(float point, int index) {
        if (index >= normalisedDataPoints.length) {
            throw new ArrayIndexOutOfBoundsException("index too large!!");
        }

        Log.e("TMP", "addNewDataPoint " + point + " in " + index);
        this.normalisedDataPoints[index].add(point);

        if (this.normalisedDataPoints[index].size() > MAX_DATA_SIZE) {
            this.normalisedDataPoints[index].removeFirst();
        }


        Log.e("TMP", "points:  " + this.normalisedDataPoints[index].size());
        invalidate();
    }


    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (normalisedDataPoints.length <= 0) {
            return;
        }


        Log.e("TMP", "onDraw ");

        int height = canvas.getHeight();
        int width = canvas.getWidth();


        int maxValues = MAX_DATA_SIZE;


        int pointSpan = width / maxValues;


        for (int i = 0; i < this.normalisedDataPoints.length; ++i) {

            if (this.normalisedDataPoints[i] == null) {
                continue;
            }
            int currentX = 0;//width - pointSpan;
            for (Float dataPoint : this.normalisedDataPoints[i]) {

                float y = height - (height * dataPoint);


                canvas.drawCircle(currentX, y, 5, rectPaints[i]);
                currentX += pointSpan;


                Log.e("TMP", "drawing:  " + dataPoint + " to: " + currentX + " " + y);
            }


        }


    }


}
