package com.app.imagepicker.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import androidx.core.content.ContextCompat;

import com.app.imagepicker.R;

/**
 * 类功能描述
 *
 * Created by stefan on 2018/1/22.
 */
public class ClipImageBorderView extends View {
    private int horizontalPadding;
    private int verticalPadding;
    private int width;
    private int borderColor = Color.WHITE;
    private int bgColor;
    private int borderWidth = 1;

    private Paint paint;

    public ClipImageBorderView(Context context) {
        this(context, null);
    }

    public ClipImageBorderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClipImageBorderView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        borderWidth = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, borderWidth, getResources()
                        .getDisplayMetrics());
        bgColor = ContextCompat.getColor(context, R.color.crop_image_bg);
        paint = new Paint();
        paint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        width = getWidth() - 2 * horizontalPadding;
        //verticalPadding = (getHeight() - width) / 2;
        paint.setColor(bgColor);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(0, 0, horizontalPadding, getHeight(), paint);
        canvas.drawRect(getWidth() - horizontalPadding, 0, getWidth(), getHeight(), paint);
        canvas.drawRect(horizontalPadding, 0, getWidth() - horizontalPadding, verticalPadding, paint);
        canvas.drawRect(horizontalPadding, getHeight() - verticalPadding, getWidth()
                - horizontalPadding, getHeight(), paint);
        paint.setColor(borderColor);
        paint.setStrokeWidth(borderWidth);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(horizontalPadding, verticalPadding, getWidth()
                - horizontalPadding, getHeight() - verticalPadding, paint);
    }

    public void setHorizontalPadding(int mHorizontalPadding) {
        this.horizontalPadding = mHorizontalPadding;
    }

    public void setVerticalPadding(int verticalPadding) {
        this.verticalPadding = verticalPadding;
    }
}