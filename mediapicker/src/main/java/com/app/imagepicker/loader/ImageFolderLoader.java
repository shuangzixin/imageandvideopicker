package com.app.imagepicker.loader;

import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.MergeCursor;
import android.provider.MediaStore;
import com.app.imagepicker.R;

/**
 * 类功能描述
 *
 * Created by stefan on 2018/1/22.
 */
public class ImageFolderLoader extends CursorLoader {

    private static final String[] PROJECTION = {
            MediaStore.Images.Media.BUCKET_ID,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Images.Media.DATA,
            "count(bucket_id) as count"};


    public static ImageFolderLoader newInstance(Context c) {
        return new ImageFolderLoader(c, 0);
    }

    private ImageFolderLoader(Context context, int minSize) {
        super(context, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, PROJECTION, " _size > ? or _size is null)  group by bucket_id , (bucket_display_name ",
                new String[]{ String.valueOf(minSize)}, " bucket_display_name asc");
    }


    @Override
    public Cursor loadInBackground() {
        Cursor albums = super.loadInBackground();
        MatrixCursor allAlbum = new MatrixCursor(PROJECTION);

        long count = 0;
        if (albums.getCount() > 0) {
            while (albums.moveToNext()) {
                count += albums.getLong(3);
            }
        }
        allAlbum.addRow(
                new String[]{
                        String.valueOf(-1),
                        getContext().getString(R.string.general_all_pictures),
                        "",
                        String.valueOf(count)});
        return new MergeCursor(new Cursor[]{allAlbum, albums});
    }
}
