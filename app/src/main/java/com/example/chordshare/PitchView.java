package com.example.chordshare;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class PitchView extends View {

    private float centerPitch, currentPitch;
    private int width, height;
    private final Paint paint = new Paint();

    public PitchView(Context context) {
        super(context);
    }

    public PitchView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PitchView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setCenterPitch(float centerPitch) {
        this.centerPitch = centerPitch;
        invalidate();
    }

    public void setCurrentPitch(float currentPitch) {
        this.currentPitch = currentPitch;
        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float halfWidth = width / 2;
        paint.setStrokeWidth(11.0f);
        paint.setColor(Color.GREEN);
        canvas.drawLine(halfWidth, 0, halfWidth, height, paint);

        float dx = currentPitch - centerPitch;
        float maxDx = width / 2f;

        float xPosition;
        if (Math.abs(dx) <= maxDx) {
            xPosition = halfWidth + (dx / maxDx) * halfWidth;
        } else {
            xPosition = (dx > 0) ? width : 0;
        }

        paint.setStrokeWidth(11.0f);
        paint.setColor(Color.BLUE);
        canvas.drawLine(halfWidth, height, xPosition, 0, paint);
    }
}
