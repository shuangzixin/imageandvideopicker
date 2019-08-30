/*
 *
 *  * Copyright (C) 2017 CIIC Guanaitong, Co.,Ltd.
 *  * All rights reserved.
 *
 */

package com.app.imagepicker.datasource;

import android.app.Activity;
import android.content.CursorLoader;
import android.os.Bundle;
import com.app.imagepicker.beans.VideoFolder;
import com.app.imagepicker.constants.CursorLoadConstants;
import com.app.imagepicker.loader.VideoItemLoader;

/**
 * 类功能描述
 *
 * Created by stefan on 2018/1/22.
 */
public class VideoItemDataSource extends DataSource {

    public VideoItemDataSource(Activity activity, DataSourceController c) {
        super(activity, c);
    }

    @Override CursorLoader getCursorLoader(Bundle args) {
        if (args != null && args.containsKey(CursorLoadConstants.LOAD_VIDEO_FOLDERS_BUNDEL_KEY)) {
            VideoFolder folder = args.getParcelable(CursorLoadConstants.LOAD_VIDEO_FOLDERS_BUNDEL_KEY);
            return VideoItemLoader.newInstance(mActivity, folder);
        }
        return VideoItemLoader.newInstance(mActivity, null);
    }

    @Override
    int getLoaderId() {
        return CursorLoadConstants.LOAD_VIDEO_ITEMS;
    }
}
