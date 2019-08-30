package com.app.imagepicker.listeners;

import com.app.imagepicker.beans.ImageItem;
import java.util.ArrayList;

/**
 * 类功能描述
 *
 * Created by stefan on 2018/1/22.
 */
public interface OnImagePreviewListener {

    void onSelected(ImageItem item, ArrayList<ImageItem> selectedItems);

    void onBottomBarVisibilityChanged(int visibility);
}
