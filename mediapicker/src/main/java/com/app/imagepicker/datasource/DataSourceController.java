package com.app.imagepicker.datasource;

import android.database.Cursor;

/**
 * 类功能描述
 *
 * Created by stefan on 2018/1/22.
 */
public interface DataSourceController {

    void onLoadFinished(Cursor data);

    void onLoadReset();

}
