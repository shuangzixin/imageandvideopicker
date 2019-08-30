package com.app.imagepicker.datasource;

import android.app.Activity;
import android.content.CursorLoader;
import android.os.Bundle;
import com.app.imagepicker.constants.CursorLoadConstants;
import com.app.imagepicker.loader.VideoFolderLoader;

/**
 * 类功能描述
 *
 * Created by stefan on 2018/1/22.
 */
public class VideoFolderDataSource extends DataSource {

    public VideoFolderDataSource(Activity activity, DataSourceController c) {
        super(activity, c);
    }

    @Override CursorLoader getCursorLoader(Bundle args) {
        return VideoFolderLoader.newInstance(mActivity);
    }

    @Override
    int getLoaderId() {
        return CursorLoadConstants.LOAD_VIDEO_FOLDERS;
    }
}
