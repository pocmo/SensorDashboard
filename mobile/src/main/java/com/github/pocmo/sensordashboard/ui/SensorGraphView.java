package com.github.pocmo.sensordashboard.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
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

    private Paint infoPaint;

    private LinkedList<Float>[] normalisedDataPoints;
    private float zeroline = 0;
    private String maxValueLabel = "";
    private String minValueValue = "";

    public SensorGraphView(Context context, AttributeSet attrs) {
        super(context, attrs);

        rectPaints[0] = new Paint();
        rectPaints[0].setColor(UIContstants.COLOUR_1);

        rectPaints[1] = new Paint();
        rectPaints[1].setColor(UIContstants.COLOUR_2);

        rectPaints[2] = new Paint();
        rectPaints[2].setColor(UIContstants.COLOUR_3);


        rectPaints[3] = new Paint();
        rectPaints[3].setColor(UIContstants.COLOUR_4);

        rectPaints[4] = new Paint();
        rectPaints[4].setColor(UIContstants.COLOUR_5);

        rectPaints[5] = new Paint();
        rectPaints[5].setColor(UIContstants.COLOUR_6);


        rectPaints[6] = new Paint();
        rectPaints[6].setColor(UIContstants.COLOUR_7);

        rectPaints[7] = new Paint();
        rectPaints[7].setColor(UIContstants.COLOUR_8);

        rectPaints[8] = new Paint();
        rectPaints[8].setColor(UIContstants.COLOUR_9);


        infoPaint = new Paint();
        infoPaint.setColor(UIContstants.INFO_PAINT_COLOUR);
        infoPaint.setTextSize(48f);
        infoPaint.setAntiAlias(true);

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


    public void setMaxValueLabel(String maxValue) {
        this.maxValueLabel = maxValue;
    }

    public void setMinValueLabel(String minValue) {
        this.minValueValue = minValue;
    }

    public void setZeroLine(float zeroline) {
        this.zeroline = zeroline;
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

        float zeroLine = height - (height * zeroline);


        canvas.drawLine(0, zeroLine, width, zeroLine, infoPaint);
        if (zeroline < 0.8f && zeroline > 0.2f) {
            canvas.drawText("0", width - 50, zeroLine - 5, infoPaint);
        }

        canvas.drawText(maxValueLabel, width - 70, 60, infoPaint);
        canvas.drawText(minValueValue, width - 70, height - 40, infoPaint);


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
