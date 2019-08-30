/*
 *
 *  * Copyright (C) 2017 CIIC Guanaitong, Co.,Ltd.
 *  * All rights reserved.
 *
 */

package com.app.imagepicker.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.app.imagepicker.R;

/**
 * 类功能描述
 *
 * Created by stefan on 2018/1/22.
 */
public class CounterTextView extends View {

    private final float mCircleRadius;

    private Paint mPaint;
    private Paint mTextPaint;

    private String mText = "1";

    private Rect mTextRect = new Rect();

    public CounterTextView(Context context) {
        this(context, null);
    }

    public CounterTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CounterTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CounterTextView);
        mCircleRadius = a.getDimension(R.styleable.CounterTextView_radius,
            getResources().getDimension(R.dimen.dp_20));

        a.recycle();

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.RED);

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setTextSize(getResources().getDimension(R.dimen.dp_16));
    }

    @Override protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int verticalCenter = getHeight() / 2;

        int horizontalCenter = getWidth() / 2;

        canvas.drawCircle(horizontalCenter, verticalCenter, mCircleRadius, mPaint);

        mTextPaint.getTextBounds(mText, 0, mText.length(), mTextRect);

        int textWidth = mTextRect.width();
        int textHeight = mTextRect.height();




        canvas.drawText(mText, horizontalCenter , verticalCenter ,
            mTextPaint);

        //float textWidth = mTextPaint.measureText(mText);

    }
}
