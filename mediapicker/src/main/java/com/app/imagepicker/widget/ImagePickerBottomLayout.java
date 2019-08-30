/*
 *
 *  * Copyright (C) 2017 CIIC Guanaitong, Co.,Ltd.
 *  * All rights reserved.
 *
 */

package com.app.imagepicker.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;

import com.app.imagepicker.R;

/**
 * 类功能描述
 *
 * Created by stefan on 2018/1/22.
 */
public class ImagePickerBottomLayout extends RelativeLayout {

    private CheckBox mOriginalCheckbox;
    private TextView mOriginalSize;
    private TextView mSend;
    private int pickTextResHint;

    public ImagePickerBottomLayout(Context context) {
        super(context, null);
    }

    public ImagePickerBottomLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImagePickerBottomLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.layout_image_pick_bottom, this);
        initViews();
    }

    private void initViews() {
        mOriginalCheckbox = (CheckBox) findViewById(R.id.original_checkbox);
        mOriginalSize = (TextView) findViewById(R.id.original_size);
        mSend = (TextView) findViewById(R.id.send);
        mOriginalCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mOriginalSize.setTextColor(isChecked
                        ? ContextCompat.getColor(getContext(), R.color.color_48baf3)
                        : ContextCompat.getColor(getContext(), R.color.gray));
            }
        });

    }

    public void updateSelectedCount(int count) {
        if (count == 0) {
            mSend.setTextColor(ContextCompat.getColor(getContext(), R.color.gray));
            mSend.setEnabled(false);
            mSend.setText(getResources().getString(pickTextResHint));
            hideOriginal();
        } else {
            mSend.setTextColor(ContextCompat.getColor(getContext(), R.color.color_48baf3));
            mSend.setEnabled(true);
            mSend.setText(getResources().getString(pickTextResHint) + " " + getResources().getString(R.string.bracket_num, count));
            showOriginal();
        }
    }

    public void updateSelectedSize(String size) {
        if (TextUtils.isEmpty(size)) {
            hideOriginal();
            mOriginalCheckbox.setChecked(false);
        } else {
            showOriginal();
            mOriginalSize.setText(getResources().getString(R.string.general_original) + " " + getResources().getString(R.string.bracket_str, size));
        }
    }


    private void showOriginal() {
        mOriginalCheckbox.setVisibility(View.VISIBLE);
        mOriginalSize.setVisibility(View.VISIBLE);
    }

    private void hideOriginal() {
        mOriginalCheckbox.setVisibility(View.GONE);
        mOriginalSize.setVisibility(View.GONE);
    }


    public void hide() {
        animate().translationY(getHeight()).setInterpolator(new AccelerateInterpolator(2));
    }

    public void show() {
        animate().translationY(0).setInterpolator(new AccelerateInterpolator(2));
    }

    public void setTextHint(@StringRes int pickTextRes) {
        this.pickTextResHint = pickTextRes;
    }

    public void setSendBtnClickListener(OnClickListener l) {
        mSend.setOnClickListener(l);
    }
}
