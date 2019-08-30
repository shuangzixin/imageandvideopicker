/*
 *
 *  * Copyright (C) 2017 CIIC Guanaitong, Co.,Ltd.
 *  * All rights reserved.
 *
 */

package com.app.imagepicker.datasource;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import com.app.imagepicker.beans.ImageFolder;
import com.app.imagepicker.beans.VideoFolder;
import com.app.imagepicker.constants.CursorLoadConstants;

/**
 * 类功能描述
 *
 * Created by stefan on 2018/1/22.
 */
public abstract class DataSource implements LoaderManager.LoaderCallbacks<Cursor> {


    Activity mActivity;

    private DataSourceController mController;

    DataSource(Activity activity, DataSourceController c) {
        mActivity = activity;
        mController = c;
    }

    abstract CursorLoader getCursorLoader(Bundle args);

    public void destoryLoader() {
        mActivity.getLoaderManager().destroyLoader(getLoaderId());
    }

    public void startLoader(ImageFolder folder) {
        Bundle args = new Bundle();
        args.putParcelable(CursorLoadConstants.LOAD_IMAGE_FOLDERS_BUNDEL_KEY, folder);
        mActivity.getLoaderManager().initLoader(getLoaderId(), args, this);
    }

    public void startLoader(VideoFolder folder) {
        Bundle args = new Bundle();
        args.putParcelable(CursorLoadConstants.LOAD_VIDEO_FOLDERS_BUNDEL_KEY, folder);
        mActivity.getLoaderManager().initLoader(getLoaderId(), args, this);
    }

    public void startLoader() {
        mActivity.getLoaderManager().initLoader(getLoaderId(), null, this);
    }

    public void resetLoader() {
        mActivity.getLoaderManager().restartLoader(getLoaderId(), null, this);
    }

    public void resetLoader(ImageFolder folder) {
        Bundle args = new Bundle();
        args.putParcelable(CursorLoadConstants.LOAD_IMAGE_FOLDERS_BUNDEL_KEY, folder);
        mActivity.getLoaderManager().restartLoader(getLoaderId(), args, this);
    }

    public void resetLoader(VideoFolder folder) {
        Bundle args = new Bundle();
        args.putParcelable(CursorLoadConstants.LOAD_VIDEO_FOLDERS_BUNDEL_KEY, folder);
        mActivity.getLoaderManager().restartLoader(getLoaderId(), args, this);
    }


    abstract int getLoaderId();

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return getCursorLoader(args);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mController.onLoadFinished(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mController.onLoadReset();
    }
}
