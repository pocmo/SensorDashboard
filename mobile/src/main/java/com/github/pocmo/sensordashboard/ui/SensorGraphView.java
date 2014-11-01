package com.github.pocmo.sensordashboard.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by juhani on 01/11/14.
 */
public class SensorGraphView extends View {

    private Paint rectPaint;

    public SensorGraphView(Context context, AttributeSet attrs) {
        super(context, attrs);

        rectPaint = new Paint();
        rectPaint.setColor(0xFFFF0000);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        // Draw the pointer

        canvas.drawRect(10, 10, 100, 100, rectPaint);
    }


}
