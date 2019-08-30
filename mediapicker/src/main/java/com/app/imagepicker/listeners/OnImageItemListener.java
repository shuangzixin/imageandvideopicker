/*
 *
 *  * Copyright (C) 2017 CIIC Guanaitong, Co.,Ltd.
 *  * All rights reserved.
 *
 */

package com.app.imagepicker.listeners;

import com.app.imagepicker.beans.ImageItem;
import java.util.ArrayList;

/**
 * 类功能描述
 *
 * Created by stefan on 2018/1/22.
 */
public interface OnImageItemListener {

    void onSelected(ImageItem item);

    void onUnSelected(ImageItem item) ;

    void onMaxChecked(int max);

    void onPreview(ImageItem item, int pos, boolean checked, ArrayList<ImageItem> mSelectedItems);

    void onTakePhoto();
}
