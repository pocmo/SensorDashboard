package com.github.pocmo.sensordashboard.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by juhani on 01/11/14.
 */
public class SensorGraphView extends View {

    private static final int MAX_DATA_SIZE = 300;
    private static final int CIRCLE_SIZE = 4;

    // FIXME don't hardcode 9
    private Paint[] rectPaints = new Paint[9];

    private LinkedList<Float>[] normalisedDataPoints;


    public SensorGraphView(Context context, AttributeSet attrs) {
        super(context, attrs);

        rectPaints[0] = new Paint();
        rectPaints[0].setColor(0xFF3f51b5);

        rectPaints[1] = new Paint();
        rectPaints[1].setColor(0xFF9c27b0);

        rectPaints[2] = new Paint();
        rectPaints[2].setColor(0xFF009688);


        rectPaints[3] = new Paint();
        rectPaints[3].setColor(0xFF8bc34a);

        rectPaints[4] = new Paint();
        rectPaints[4].setColor(0xFFffc107);

        rectPaints[5] = new Paint();
        rectPaints[5].setColor(0xFF795548);


        rectPaints[6] = new Paint();
        rectPaints[6].setColor(0xFFe51c23);

        rectPaints[7] = new Paint();
        rectPaints[7].setColor(0xFFcddc39);

        rectPaints[8] = new Paint();
        rectPaints[8].setColor(0xFFff9800);
    }


    public void setNormalisedDataPoints(LinkedList<Float>[] normalisedDataPoints) {

        this.normalisedDataPoints = normalisedDataPoints;


        for (int i = 0; i < this.normalisedDataPoints.length; ++i) {
            if (this.normalisedDataPoints[i].size() > MAX_DATA_SIZE) {

                List tmp = this.normalisedDataPoints[i].subList(this.normalisedDataPoints[i].size() - MAX_DATA_SIZE - 1, this.normalisedDataPoints[i].size() - 1);
                this.normalisedDataPoints[i] = new LinkedList<Float>();
                this.normalisedDataPoints[i].addAll(tmp);
            }


        }

        invalidate();
    }


    public void addNewDataPoint(float point, int index) {
        if (index >= normalisedDataPoints.length) {
            throw new ArrayIndexOutOfBoundsException("index too large!!");
        }

        this.normalisedDataPoints[index].add(point);

        if (this.normalisedDataPoints[index].size() > MAX_DATA_SIZE) {
            this.normalisedDataPoints[index].removeFirst();
        }


        invalidate();
    }


    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (normalisedDataPoints.length <= 0) {
            return;
        }


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




                canvas.drawCircle(currentX, y, CIRCLE_SIZE, rectPaints[i]);
                currentX += pointSpan;

            }


        }


    }


}
