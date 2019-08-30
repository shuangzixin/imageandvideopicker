package com.app.imagepicker.utils.extra;

import android.media.MediaScannerConnection;
import android.net.Uri;

/**
 * 类功能描述
 *
 * Created by stefan on 2018/12/25.
 */
public class AlbumMediaScannerConnectionClient
    implements MediaScannerConnection.MediaScannerConnectionClient {

    private MediaScannerConnection mConnection;
    private String imgPath;

    public AlbumMediaScannerConnectionClient() {
        super();
    }

    public MediaScannerConnection getmConnection() {
        return mConnection;
    }

    public void setmConnection(MediaScannerConnection mConnection) {
        this.mConnection = mConnection;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    @Override
    public void onMediaScannerConnected() {
        mConnection.scanFile(imgPath, "image/*");
    }

    @Override
    public void onScanCompleted(String path, Uri uri) {
        mConnection.disconnect();
    }
}
