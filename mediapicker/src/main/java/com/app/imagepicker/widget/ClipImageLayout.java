/*
 *
 *  * Copyright (C) 2017 CIIC Guanaitong, Co.,Ltd.
 *  * All rights reserved.
 *
 */

package com.app.imagepicker.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import androidx.annotation.RequiresApi;

import com.app.imagepicker.utils.LogUtil;


/**
 * 类功能描述
 *
 * Created by stefan on 2018/1/22.
 */
public class ClipImageLayout extends RelativeLayout {
    private ClipZoomImageView zoomImageView;
    private ClipImageBorderView clipImageView;
    private int horizontalPadding = 0;
    private int verticalPadding = 0;
    private int width = 0;
    private int height = 0;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public ClipImageLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        zoomImageView = new ClipZoomImageView(context);
        clipImageView = new ClipImageBorderView(context);

        android.view.ViewGroup.LayoutParams lp =
            new LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                android.view.ViewGroup.LayoutParams.MATCH_PARENT);

        this.addView(zoomImageView, lp);
        this.addView(clipImageView, lp);

        //horizontalPadding =
        //    (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, horizontalPadding,
        //        getResources().getDisplayMetrics());
    }

    @Override protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        getPadding();
        zoomImageView.setHorizontalPadding(horizontalPadding);
        zoomImageView.setVerticalPadding(verticalPadding);
        zoomImageView.setCropWidth(width);
        zoomImageView.setCropHeight(height);
        clipImageView.setHorizontalPadding(horizontalPadding);
        clipImageView.setVerticalPadding(verticalPadding);
        clipImageView.invalidate();
    }

    public void setImageDrawable(Drawable drawable) {
        zoomImageView.setImageDrawable(drawable);
    }

    public void setImageBitmap(Bitmap bitmap) {
        zoomImageView.setBitmapWidth(bitmap.getWidth());
        zoomImageView.setBitmapHeight(bitmap.getHeight());
        zoomImageView.setImageBitmap(bitmap);
    }

    public void setHorizontalPadding(int mHorizontalPadding) {
        this.horizontalPadding = mHorizontalPadding;
    }

    public void setVerticalPadding(int verticalPadding) {
        this.verticalPadding = verticalPadding;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Bitmap clip() {
        return zoomImageView.clip();
    }

    private void getPadding() {
        LogUtil.i("aaa", "getWidth():" + getWidth() + "   getHeight():" + getHeight());
        LogUtil.i("aaa", "height():" + width + "   height():" + height);
        double radioTotal = getWidth() * 1.0 / getHeight();
        double radioSet = width * 1.0 / height;
        if (radioTotal > radioSet) {
            if (height > getHeight()) {
                width = width * getHeight() / height;
                height = getHeight();
            }
        } else if (radioTotal < radioSet) {
            if (width > getWidth()) {
                height = height * getWidth() / width;
                width = getWidth();
            }
        } else {
            if (height > getHeight()) {
                height = getHeight();
                width = getWidth();
            }
        }
        horizontalPadding = (getWidth() - width) / 2;
        verticalPadding = (getHeight() - height) / 2;
    }
}
