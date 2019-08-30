/*
 *
 *  * Copyright (C) 2017 CIIC Guanaitong, Co.,Ltd.
 *  * All rights reserved.
 *
 */

package com.app.imagepicker.listeners;

import com.app.imagepicker.beans.VideoItem;
import java.util.ArrayList;

/**
 * 类功能描述
 *
 * Created by stefan on 2018/1/22.
 */
public interface OnVideoItemListener {

    void onSelected(VideoItem item);

    void onUnSelected(VideoItem item) ;

    void onMaxChecked(int max);

    void onPreview(VideoItem item, int pos, boolean checked, ArrayList<VideoItem> mSelectedItems);

    void onTakePhoto();
}
