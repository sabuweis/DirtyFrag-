package com.android.bbkcalculator;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

public class CardStackLayout extends LinearLayout {
    public CardStackLayout(Context context) {
        super(context);
    }

    public CardStackLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CardStackLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}