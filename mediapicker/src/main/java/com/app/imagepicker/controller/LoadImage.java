package com.app.imagepicker.controller;

import android.app.Activity;
import android.database.Cursor;
import com.app.imagepicker.beans.ImageFolder;
import com.app.imagepicker.beans.ImageItem;
import com.app.imagepicker.datasource.DataSourceController;
import com.app.imagepicker.datasource.ImageFolderDataSource;
import com.app.imagepicker.datasource.ImageItemDataSource;
import java.util.ArrayList;
import java.util.List;

/**
 * 类功能描述
 *
 * Created by stefan on 2018/1/22.
 */
public class LoadImage {


    private ImageItemDataSource mImageItemSource;

    private ImageFolderDataSource mImageFolderSource;

    private LoadImageListener mListener;

    public LoadImage(Activity activity, LoadImageListener loadImageListener) {
        mImageItemSource = new ImageItemDataSource(activity, new ImageItemController());
        mImageFolderSource = new ImageFolderDataSource(activity, new ImageFolderController());
        mListener = loadImageListener;
    }

    public void loadAllImages() {
        mImageItemSource.startLoader();
    }

    public void loadImages(ImageFolder folder) {
        mImageItemSource.startLoader(folder);
    }



    public void reloadImages(ImageFolder folder) {
        mImageItemSource.resetLoader(folder);
    }


    public void loadImageFolders() {
        mImageFolderSource.startLoader();
    }


    class ImageItemController implements DataSourceController {

        @Override
        public void onLoadFinished(Cursor data) {

            if (data != null) {
                List<ImageItem> items = new ArrayList<>();
                int count = data.getCount();
                if (count <= 0) {
                    return;
                }
                data.moveToFirst();
                do {
                    ImageItem item = ImageItem.valueOf(data);
                    items.add(item);
                } while (data.moveToNext());

                mListener.loadImages(items);

            }

        }

        @Override
        public void onLoadReset() {

        }
    }

    class ImageFolderController implements DataSourceController {

        @Override
        public void onLoadFinished(Cursor data) {
            if (data != null) {
                List<ImageFolder> folders = new ArrayList<>();
                int count = data.getCount();
                if (count <= 0) {
                    return;
                }
                data.moveToFirst();
                do {
                    ImageFolder folder = ImageFolder.valueOf(data);
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
