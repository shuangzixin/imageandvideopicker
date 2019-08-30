/*
 *
 *  * Copyright (C) 2017 CIIC Guanaitong, Co.,Ltd.
 *  * All rights reserved.
 *
 */

package com.app.imagepicker.controller;

import android.app.Activity;
import android.database.Cursor;
import com.app.imagepicker.beans.VideoFolder;
import com.app.imagepicker.beans.VideoItem;
import com.app.imagepicker.datasource.DataSourceController;
import com.app.imagepicker.datasource.VideoFolderDataSource;
import com.app.imagepicker.datasource.VideoItemDataSource;
import java.util.ArrayList;
import java.util.List;

/**
 * 类功能描述
 *
 * Created by stefan on 2018/1/22.
 */
public class LoadVideo {


    private VideoItemDataSource mVideoItemSource;

    private VideoFolderDataSource mVideoFolderSource;

    private LoadVideoListener mListener;

    public LoadVideo(Activity activity, LoadVideoListener loadVideoListener) {
        mVideoItemSource = new VideoItemDataSource(activity, new VideoItemController());
        mVideoFolderSource = new VideoFolderDataSource(activity, new VideoFolderController());
        mListener = loadVideoListener;
    }

    public void loadAllVideos() {
        mVideoItemSource.startLoader();
    }

    public void loadVideos(VideoFolder folder) {
        mVideoItemSource.startLoader(folder);
    }



    public void reloadVideos(VideoFolder folder) {
        mVideoItemSource.resetLoader(folder);
    }


    public void loadVideoFolders() {
        mVideoFolderSource.startLoader();
    }


    class VideoItemController implements DataSourceController {

        @Override
        public void onLoadFinished(Cursor data) {

            if (data != null) {
                List<VideoItem> items = new ArrayList<>();
                int count = data.getCount();
                if (count <= 0) {
                    return;
                }
                data.moveToFirst();
                do {
                    VideoItem item = VideoItem.valueOf(data);
                    items.add(item);
                } while (data.moveToNext());

                mListener.loadVideos(items);

            }

        }

        @Override
        public void onLoadReset() {

        }
    }

    class VideoFolderController implements DataSourceController {

        @Override
        public void onLoadFinished(Cursor data) {
            if (data != null) {
                List<VideoFolder> folders = new ArrayList<>();
                int count = data.getCount();
                if (count <= 0) {
                    return;
                }
                data.moveToFirst();
                do {
                    VideoFolder folder = VideoFolder.valueOf(data);
                    folders.add(folder);
                } while (data.moveToNext());

                mListener.loadFolders(folders);

            }
        }

        @Override
        public void onLoadReset() {

        }
    }

}
