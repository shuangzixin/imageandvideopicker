package com.app.imagepicker.controller;

import com.app.imagepicker.beans.ImageFolder;
import com.app.imagepicker.beans.ImageItem;
import java.util.List;

/**
 * 类功能描述
 *
 * Created by stefan on 2018/1/22.
 */
public interface LoadImageListener {

    void loadFolders(List<ImageFolder> folders);

    void loadImages(List<ImageItem> items);

}
