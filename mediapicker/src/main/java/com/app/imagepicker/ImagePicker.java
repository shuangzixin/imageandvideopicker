/*
 *
 *  * Copyright (C) 2017 CIIC Guanaitong, Co.,Ltd.
 *  * All rights reserved.
 *
 */

package com.app.imagepicker;

/**
 * 类功能描述
 *
 * Created by stefan on 2018/1/22.
 */
public class ImagePicker {
    private static class SingletonHolder {
        private static final ImagePicker INSTANCE = new ImagePicker();
    }

    private ImagePicker() {

    }

    public static ImagePicker getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private ImagePickerConfiguration mConfig;

    public void init(ImagePickerConfiguration configuration) {
        mConfig = configuration;
    }

    public ImagePickerConfiguration getConfig() {
        return mConfig;
    }

}