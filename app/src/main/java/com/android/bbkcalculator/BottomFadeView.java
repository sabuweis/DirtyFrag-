package com.android.bbkcalculator;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

public class BottomFadeView extends View {
    private Paint paint;
    private LinearGradient gradient;

    public BottomFadeView(Context context) {
        super(context);
        init();
    }

    public BottomFadeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BottomFadeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint();
        setWillNotDraw(false);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        gradient = new LinearGradient(
            0, 0,
            0, h,
            new int[]{0x00000000, 0x80000000, 0xFF000000},
            new float[]{0f, 0.6f, 1f},
            Shader.TileMode.CLAMP
        );
        paint.setShader(gradient);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (gradient != null) {
            canvas.drawRect(0, 0, getWidth(), getHeight(), paint);
        }
    }
}