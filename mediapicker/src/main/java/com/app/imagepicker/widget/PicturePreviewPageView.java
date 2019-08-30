/*
 *
 *  * Copyright (C) 2017 CIIC Guanaitong, Co.,Ltd.
 *  * All rights reserved.
 *
 */

package com.app.imagepicker.widget;

import android.content.Context;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.app.imagepicker.utils.ScreenUtil;
import com.app.imagepicker.widget.scaleview.ImageSource;
import com.app.imagepicker.widget.scaleview.OnImageEventListener;
import com.app.imagepicker.widget.scaleview.SubsamplingScaleImageView;

/**
 * 类功能描述
 *
 * Created by stefan on 2018/1/22.
 */
public class PicturePreviewPageView extends FrameLayout {


    /**
     * if aspect ratio is grater than 3
     * load picture as long image
     */
    private static final int LONG_IMG_ASPECT_RATIO = 3;
    private static final int LONG_IMG_MINIMUM_LENGTH = 1500;

    private SubsamplingScaleImageView originImageView;

    public PicturePreviewPageView(Context context) {
        super(context);
        init(context);
    }

    public PicturePreviewPageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PicturePreviewPageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        originImageView = new SubsamplingScaleImageView(context);
        addView(originImageView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        originImageView.setOnImageEventListener(new OnImageEventListener() {
            @Override
            public void onImageLoaded(int width, int height) {
                adjustPictureScale(originImageView, width, height);
            }
        });
    }

    public void setMaxScale(float maxScale) {
        originImageView.setMaxScale(maxScale);
    }

    public void setOnClickListener(OnClickListener listener) {
        originImageView.setOnClickListener(listener);
    }

    public void setOriginImage(ImageSource imageSource) {
        originImageView.setImage(imageSource);
    }

    public SubsamplingScaleImageView getOriginImageView() {
        return originImageView;
    }

    private static void adjustPictureScale(SubsamplingScaleImageView view, int width, int height) {
        if (height >= LONG_IMG_MINIMUM_LENGTH
                && height / width >= LONG_IMG_ASPECT_RATIO) {
            int screenWith = ScreenUtil.getInstance().getScreenWidth(view.getContext());
            float scale = screenWith/ (float) width;
            float centerX = screenWith / 2;
            view.setScaleAndCenterWithAnim(scale, new PointF(centerX, 0.0f));
            view.setDoubleTapZoomScale(scale);
        }
    }

}
