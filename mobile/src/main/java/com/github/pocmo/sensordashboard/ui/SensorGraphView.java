package com.github.pocmo.sensordashboard.ui;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.hardware.SensorManager;
import android.util.AttributeSet;
import android.view.View;

import com.github.pocmo.sensordashboard.R;
import com.github.pocmo.sensordashboard.data.TagData;

import java.util.ArrayList;
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
    private Paint tagPaint;

    private ArrayList<Float>[] normalisedDataPoints;
    private ArrayList<Integer>[] dataPointsAccuracy;
    private ArrayList<Long>[] dataPointsTimeStamps;

    private LinkedList<TagData> tags = new LinkedList<>();
    private float zeroline = 0;
    private String maxValueLabel = "";
    private String minValueValue = "";

    private boolean[] drawSensors = new boolean[6];

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

        tagPaint = new Paint();
        tagPaint.setColor(res.getColor(R.color.graph_color_info));
        tagPaint.setAntiAlias(true);

    }


    public void setDrawSensors(boolean[] drawSensors) {
        this.drawSensors = drawSensors;
        invalidate();
    }

    public void setNormalisedDataPoints(ArrayList<Float>[] normalisedDataPoints, ArrayList<Integer>[] dataPointsAccuracy, ArrayList<Long>[] dataPointsTimeStamps) {

        this.dataPointsTimeStamps = dataPointsTimeStamps;


        for (int i = 0; i < this.dataPointsTimeStamps.length; ++i) {
            if (this.dataPointsTimeStamps[i].size() > MAX_DATA_SIZE) {

                List tmp = this.dataPointsTimeStamps[i].subList(this.dataPointsTimeStamps[i].size() - MAX_DATA_SIZE - 1, this.dataPointsTimeStamps[i].size() - 1);
                this.dataPointsTimeStamps[i] = new ArrayList<>();
                this.dataPointsTimeStamps[i].addAll(tmp);
            }
        }


        this.normalisedDataPoints = normalisedDataPoints;

        for (int i = 0; i < this.normalisedDataPoints.length; ++i) {
            if (this.normalisedDataPoints[i].size() > MAX_DATA_SIZE) {

                List tmp = this.normalisedDataPoints[i].subList(this.normalisedDataPoints[i].size() - MAX_DATA_SIZE - 1, this.normalisedDataPoints[i].size() - 1);
                this.normalisedDataPoints[i] = new ArrayList<>();
                this.normalisedDataPoints[i].addAll(tmp);
            }
        }


        this.dataPointsAccuracy = dataPointsAccuracy;

        for (int i = 0; i < this.dataPointsAccuracy.length; ++i) {
            if (this.dataPointsAccuracy[i].size() > MAX_DATA_SIZE) {

                List tmp = this.dataPointsAccuracy[i].subList(this.dataPointsAccuracy[i].size() - MAX_DATA_SIZE - 1, this.dataPointsAccuracy[i].size() - 1);
                this.dataPointsAccuracy[i] = new ArrayList<>();
                this.dataPointsAccuracy[i].addAll(tmp);
            }
        }


        for (int i = 0; i < this.dataPointsAccuracy.length; ++i) {


            ArrayList<Integer> tmp = new ArrayList<>();
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

    public void addNewTag(TagData tagData) {
        this.tags.add(tagData);

        // allow max / 2 tags... that's probably enough
        if (this.tags.size() > MAX_DATA_SIZE / 2) {
            this.tags.removeFirst();
        }
    }

    public void addNewDataPoint(float point, int accuracy, int index, long timestamp) {
        if (index >= normalisedDataPoints.length) {
            throw new ArrayIndexOutOfBoundsException("index too large!!");
        }

        this.dataPointsTimeStamps[index].add(timestamp);

        if (this.dataPointsTimeStamps[index].size() > MAX_DATA_SIZE) {
            this.dataPointsTimeStamps[index].remove(0);
        }


        this.normalisedDataPoints[index].add(point);

        if (this.normalisedDataPoints[index].size() > MAX_DATA_SIZE) {
            this.normalisedDataPoints[index].remove(0);
        }


        this.dataPointsAccuracy[index].add(dataPointAccuracyToDotSize(accuracy));

        if (this.dataPointsAccuracy[index].size() > MAX_DATA_SIZE) {
            this.dataPointsAccuracy[index].remove(0);
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

        boolean firstSensorDrawn = true;
        long previousTimeStamp = -1;
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
            int lastDrawnTagIndex = -1;
            for (Float dataPoint : this.normalisedDataPoints[i]) {


                float y = height - (height * dataPoint);


                canvas.drawCircle(currentX, y, dataPointsAccuracy[i].get(index), rectPaints[i]);


                if (previousX != -1 && previousY != -1) {
                    canvas.drawLine(previousX, previousY, currentX, y, rectPaints[i]);

                }


                // draw tags here
                if (firstSensorDrawn) {

                    if (previousTimeStamp != -1) {
                        int nextIndexToDraw = findStartingIndexForTag(previousTimeStamp / 1000000, dataPointsTimeStamps[i].get(index) / 1000000, lastDrawnTagIndex + 1);
                        if (nextIndexToDraw != -1) {
                            drawTag(canvas, this.tags.get(nextIndexToDraw), previousX + ((currentX - previousX) / 2));
                            lastDrawnTagIndex = nextIndexToDraw;
                        }
                    }
                    previousTimeStamp = dataPointsTimeStamps[i].get(index);
                }

                previousX = currentX;
                previousY = y;

                currentX += pointSpan;
                ++index;
            }


            firstSensorDrawn = false;
            previousX = -1;
            previousY = -1;


        }

    }

    private void drawTag(Canvas canvas, TagData tag, float x) {
        canvas.drawRect(x - 3, 0 + 1, x + 3, canvas.getHeight() - 1, tagPaint);
    }


    private int findStartingIndexForTag(long startTimestamp, long endTimestamp, int startIndex) {

        for (int i = startIndex; i < this.tags.size(); ++i) {
            if (this.tags.get(i).getTimestamp() > startTimestamp && this.tags.get(i).getTimestamp() <= endTimestamp) {
                return i;
            }


        }

        return -1;
    }

}
