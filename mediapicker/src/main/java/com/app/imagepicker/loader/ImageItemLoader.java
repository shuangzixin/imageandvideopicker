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

import com.app.imagepicker.beans.ImageFolder;

/**
 * 类功能描述
 * <p>
 * Created by stefan on 2018/1/22.
 */
public class ImageItemLoader extends CursorLoader {

    private static final String[] PROJECTION = {
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.BUCKET_ID,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.DATE_TAKEN,
            MediaStore.Images.Media.DATE_ADDED,
            MediaStore.Images.Media.DATE_MODIFIED
    };

    private static final String SELECTION_SIZE = MediaStore.Images.Media.SIZE + " > ? or "
            + MediaStore.Images.Media.SIZE + " is null";

    public static ImageItemLoader newInstance(Context context, ImageFolder folder) {
        if (folder == null || "-1".equals(folder.id)) {
            return new ImageItemLoader(context);
        }
        return new ImageItemLoader(context, folder);
    }


    private ImageItemLoader(Context context, ImageFolder folder) {
        this(context, MediaStore.Images.Media.BUCKET_ID + " =? and (" + SELECTION_SIZE + ")", new String[]{folder.id, String.valueOf(0)});
    }


    private ImageItemLoader(Context context) {
        this(context, SELECTION_SIZE, new String[]{String.valueOf(0)});
    }

    private ImageItemLoader(Context context, String selection, String[] selectionArgs) {
        super(context, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, PROJECTION, selection, selectionArgs, MediaStore.Images.Media.DATE_MODIFIED + " DESC");
    }
}
