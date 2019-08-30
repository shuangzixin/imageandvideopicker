/*
 *
 *  * Copyright (C) 2017 CIIC Guanaitong, Co.,Ltd.
 *  * All rights reserved.
 *
 */

package com.app.imagepicker.widget.scaleview;

import android.graphics.PointF;
import java.io.Serializable;

/**
 * Wraps the scale, center and orientation of a displayed image for easy restoration on screen rotate.
 *
 * Created by stefan on 2018/1/22.
 */
public class ImageViewState implements Serializable {

    private float scale;

    private float centerX;

    private float centerY;

    private int orientation;

    public ImageViewState(float scale, PointF center, int orientation) {
        this.scale = scale;
        this.centerX = center.x;
        this.centerY = center.y;
        this.orientation = orientation;
    }

    public float getScale() {
        return scale;
    }

    public PointF getCenter() {
        return new PointF(centerX, centerY);
    }

    public int getOrientation() {
        return orientation;
    }

}
