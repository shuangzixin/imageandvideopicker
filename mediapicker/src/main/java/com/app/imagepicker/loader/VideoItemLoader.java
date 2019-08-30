/*
 *
 *  * Copyright (C) 2017 CIIC Guanaitong, Co.,Ltd.
 *  * All rights reserved.
 *
 */

package com.app.imagepicker.loader;

import android.content.Context;
import android.content.CursorLoader;
import android.provider.MediaStore;

import com.app.imagepicker.beans.VideoFolder;

/**
 * 类功能描述
 * <p>
 * Created by stefan on 2018/1/22.
 */
public class VideoItemLoader extends CursorLoader {

    private static final String[] PROJECTION = {
            MediaStore.Video.Media._ID, MediaStore.Video.Media.BUCKET_ID,
            MediaStore.Video.Media.DISPLAY_NAME, MediaStore.Video.Media.DATA,
            MediaStore.Video.Media.DATE_TAKEN, MediaStore.Video.Media.DATE_ADDED,
            MediaStore.Video.Media.DURATION, MediaStore.Video.Media.SIZE,
            MediaStore.Video.Media.DATE_MODIFIED
    };

    private static final String SELECTION_SIZE =
            MediaStore.Video.Media.SIZE + " > ? or " + MediaStore.Video.Media.SIZE + " is null";

    public static VideoItemLoader newInstance(Context context, VideoFolder folder) {
        if (folder == null || "-1".equals(folder.id)) {
            return new VideoItemLoader(context);
        }
        return new VideoItemLoader(context, folder);
    }

    private VideoItemLoader(Context context, VideoFolder folder) {
        this(context, MediaStore.Video.Media.BUCKET_ID + " =? and (" + SELECTION_SIZE + ")",
                new String[]{folder.id, String.valueOf(0)});
    }

    private VideoItemLoader(Context context) {
        this(context, SELECTION_SIZE, new String[]{String.valueOf(0)});
    }

    private VideoItemLoader(Context context, String selection, String[] selectionArgs) {
        super(context, MediaStore.Video.Media.EXTERNAL_CONTENT_URI, PROJECTION, selection,
                selectionArgs, MediaStore.Video.Media.DATE_MODIFIED + " DESC");
    }
}
