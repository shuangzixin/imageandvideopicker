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
import com.app.imagepicker.beans.ImageFolder;
import com.app.imagepicker.constants.CursorLoadConstants;
import com.app.imagepicker.loader.ImageItemLoader;

/**
 * 类功能描述
 *
 * Created by stefan on 2018/1/22.
 */
public class ImageItemDataSource extends DataSource {

    public ImageItemDataSource(Activity activity, DataSourceController c) {
        super(activity, c);
    }

    @Override CursorLoader getCursorLoader(Bundle args) {
        if (args != null && args.containsKey(CursorLoadConstants.LOAD_IMAGE_FOLDERS_BUNDEL_KEY)) {
            ImageFolder folder = args.getParcelable(CursorLoadConstants.LOAD_IMAGE_FOLDERS_BUNDEL_KEY);
            return ImageItemLoader.newInstance(mActivity, folder);
        }
        return ImageItemLoader.newInstance(mActivity, null);
    }

    @Override
    int getLoaderId() {
        return CursorLoadConstants.LOAD_IMAGE_ITEMS;
    }
}
