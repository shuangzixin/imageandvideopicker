package com.app.imagepicker.listeners;

import com.app.imagepicker.beans.VideoItem;
import java.util.ArrayList;

/**
 * 类功能描述
 *
 * Created by stefan on 2018/1/22.
 */
public interface OnVideoPreviewListener {

    void onSelected(VideoItem item, ArrayList<VideoItem> selectedItems);

    void onBottomBarVisibilityChanged(int visibility);
}
