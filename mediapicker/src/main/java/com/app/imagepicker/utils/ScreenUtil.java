/*
 *
 *  * Copyright (C) 2017 CIIC Guanaitong, Co.,Ltd.
 *  * All rights reserved.
 *
 */

package com.app.imagepicker.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

/**
 * 类功能描述
 *
 * Created by stefan on 2018/1/22.
 */
public class ScreenUtil {
    private int mScreenHeight;
    private int mScreenWidth;

    private static class SingletonHolder {
        private static final ScreenUtil INSTANCE = new ScreenUtil();
    }

    private ScreenUtil() {
    }

    public static ScreenUtil getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public int getScreenHeight(Context context) {
        if (mScreenHeight == 0) {
            checkDisplay(context);
        }
        return mScreenHeight;
    }

    public int getScreenWidth(Context context) {
        if (mScreenWidth == 0) {
            checkDisplay(context);
        }
        return mScreenWidth;
    }

    private void checkDisplay(Context c) {
        WindowManager manager = (WindowManager) c.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        if (manager != null) {
            Display display = manager.getDefaultDisplay();
            if (display != null) {
                DisplayMetrics displayMetrics = new DisplayMetrics();
                display.getMetrics(displayMetrics);
                mScreenHeight = displayMetrics.heightPixels;
                mScreenWidth = displayMetrics.widthPixels;
            }
        }
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}