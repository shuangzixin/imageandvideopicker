/*
 *
 *  * Copyright (C) 2017 CIIC Guanaitong, Co.,Ltd.
 *  * All rights reserved.
 *
 */

package com.app.imagepicker;

import android.widget.ImageView;

/**
 * 类功能描述
 *
 * Created by stefan on 2018/1/22.
 */
public interface ImageLoader {


    void loadImage(ImageView imageView, String uri, int width, int height);

    void loadImage(ImageView imageView, String uri);

}
