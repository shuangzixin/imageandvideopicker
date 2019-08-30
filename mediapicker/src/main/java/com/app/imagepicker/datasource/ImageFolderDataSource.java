package com.app.imagepicker.datasource;

import android.app.Activity;
import android.content.CursorLoader;
import android.os.Bundle;
import com.app.imagepicker.constants.CursorLoadConstants;
import com.app.imagepicker.loader.ImageFolderLoader;

/**
 * 类功能描述
 *
 * Created by stefan on 2018/1/22.
 */
public class ImageFolderDataSource extends DataSource {

    public ImageFolderDataSource(Activity activity, DataSourceController c) {
        super(activity, c);
    }

    @Override CursorLoader getCursorLoader(Bundle args) {
        return ImageFolderLoader.newInstance(mActivity);
    }

    @Override
    int getLoaderId() {
        return CursorLoadConstants.LOAD_IMAGE_FOLDERS;
    }
}
