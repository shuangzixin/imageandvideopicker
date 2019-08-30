package com.app.imagepicker.controller;

import com.app.imagepicker.beans.VideoFolder;
import com.app.imagepicker.beans.VideoItem;
import java.util.List;

/**
 * 类功能描述
 *
 * Created by stefan on 2018/1/22.
 */
public interface LoadVideoListener {

    void loadFolders(List<VideoFolder> folders);

    void loadVideos(List<VideoItem> items);

}
