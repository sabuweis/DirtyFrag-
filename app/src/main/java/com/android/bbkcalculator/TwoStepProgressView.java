package com.android.bbkcalculator;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class TwoStepProgressView extends View {
    private Paint paintBackground;
    private Paint paintProgress1;
    private Paint paintProgress2;
    private float progress1 = 0f;
    private float progress2 = 0f;

    public TwoStepProgressView(Context context) {
        super(context);
        init();
    }

    public TwoStepProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TwoStepProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paintBackground = new Paint();
        paintBackground.setColor(0xFF2E2E2E);
        paintBackground.setStyle(Paint.Style.FILL);

        paintProgress1 = new Paint();
        paintProgress1.setColor(0xFF00C853);
        paintProgress1.setStyle(Paint.Style.FILL);

        paintProgress2 = new Paint();
        paintProgress2.setColor(0xFF00E673);
        paintProgress2.setStyle(Paint.Style.FILL);
    }

    public void setProgress(float step1, float step2) {
        this.progress1 = Math.max(0f, Math.min(1f, step1));
        this.progress2 = Math.max(0f, Math.min(1f, step2));
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();
        int radius = height / 2;

        // Draw background
        canvas.drawRoundRect(0, 0, width, height, radius, radius, paintBackground);

        // Draw progress 1 (full width)
        if (progress1 > 0) {
            float w1 = width * progress1;
            canvas.drawRoundRect(0, 0, w1, height, radius, radius, paintProgress1);
        }

        // Draw progress 2 (second step on top)
        if (progress2 > 0) {
            float w2 = width * progress2;
            canvas.drawRoundRect(0, 0, w2, height, radius, radius, paintProgress2);
        }
    }
}