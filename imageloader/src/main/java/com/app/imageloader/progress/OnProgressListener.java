package com.app.imageloader.progress;

import com.bumptech.glide.load.engine.GlideException;

/**
 * 类功能描述
 *
 * Created by stefan on 2018/1/12.
 */
public interface OnProgressListener {

    void onProgress(String imageUrl, long bytesRead, long totalBytes, boolean isDone,
                    GlideException exception);
}
