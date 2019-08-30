/*
 *
 *  * Copyright (C) 2017 CIIC Guanaitong, Co.,Ltd.
 *  * All rights reserved.
 *
 */

package com.app.imagepicker.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.app.imagepicker.R;
import com.app.imagepicker.base.activity.BaseActivity;
import com.app.imagepicker.utils.FileUtil;
import com.app.imagepicker.utils.ImageUtil;
import com.app.imagepicker.widget.ClipImageLayout;

import java.io.File;
import java.io.IOException;

/**
 * 类功能描述
 *
 * Created by stefan on 2018/1/22.
 */
public class ImageCropActivity extends BaseActivity {

    private static final int SIZE_LIMIT = 2048;

    public static final String RESULT_PATH = "crop_image";
    private static final String PARAM_ORIGIN_PATH = "param_origin_path";
    public static final String PARAM_AVATAR_PATH = "param_path";
    public static final String PARAM_WIDTH = "param_width";
    public static final String PARAM_HEIGHT = "param_height";
    private ClipImageLayout mClipLayout;
    /**
     * general_cancel
     */
    private TextView mCancel;
    /**
     * general_ok
     */
    private TextView mConfirm;
    private String mSourcePath;
    private String mDesFilePath;
    //裁剪图片时宽高
    private int mWidth = 0;
    private int mHeight = 0;
    private int sampleSize;

    public static void start(Activity context, String originPhotoPath, String dstFilePath,
        int requestCode, int horizontalPadding, int verticalPadding) {
        Intent starter = new Intent(context, ImageCropActivity.class);
        starter.putExtra(PARAM_ORIGIN_PATH, originPhotoPath);
        starter.putExtra(PARAM_AVATAR_PATH, dstFilePath);
        starter.putExtra(PARAM_WIDTH, horizontalPadding);
        starter.putExtra(PARAM_HEIGHT, verticalPadding);
        context.startActivityForResult(starter, requestCode);
    }

    private void getTempPath() {

        String cacheDir = FileUtil.getInnerCacheDir(this);

        File dir = new File(cacheDir);

        if (!dir.exists()) {
            dir.mkdirs();
        }

        if (TextUtils.isEmpty(mDesFilePath)) {
            mDesFilePath = cacheDir + "/tempImage.jpg";
        } else {
            mDesFilePath = cacheDir + "/" + System.currentTimeMillis() + ".jpg";
        }
    }

    @Override protected void handleIntent(Intent intent) {
        mSourcePath = getIntent().getStringExtra(PARAM_ORIGIN_PATH);
        mDesFilePath = getIntent().getStringExtra(PARAM_AVATAR_PATH);
        mWidth = getIntent().getIntExtra(PARAM_WIDTH, 0);
        mHeight = getIntent().getIntExtra(PARAM_HEIGHT, 0);

        getTempPath();
    }

    private void setSourceUri() {
        this.sampleSize = 0;
        if (!TextUtils.isEmpty(mSourcePath)) {
            new AsyncTask<Void, Void, Void>() {

                @Override protected Void doInBackground(Void... params) {
                    try {
                        sampleSize = calculateBitmapSampleSize(mSourcePath);
                        final Bitmap bitmap = ImageUtil.loadBitmap(mSourcePath, sampleSize);
                        if (bitmap != null) {

                            runOnUiThread(new Runnable() {
                                @Override public void run() {
                                    startCrop(bitmap);
                                }
                            });
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                }
            }.execute();
        }
    }

    private int calculateBitmapSampleSize(String originPath) throws IOException {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(originPath, options);
        int maxSize = SIZE_LIMIT;
        int sampleSize = 1;
        if (options.outHeight > 0 && options.outWidth > 0) {
            while (options.outHeight / sampleSize > maxSize
                || options.outWidth / sampleSize > maxSize) {
                sampleSize = sampleSize << 1;
            }
        } else {
            sampleSize = sampleSize << 2;
        }
        return sampleSize;
    }

    @Override protected void initView() {
        mTitleBar.setVisibility(View.GONE);
        mClipLayout = findViewById(R.id.clip_layout);
        mCancel = findViewById(R.id.cancel);
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                finish();
            }
        });
        mConfirm = findViewById(R.id.confirm);
        mConfirm.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                clipImage();
            }
        });

        mClipLayout.setWidth(mWidth);
        mClipLayout.setHeight(mHeight);
        setSourceUri();
    }

    @Override public int getLayoutResourceId() {
        return R.layout.activity_image_crop;
    }

    private void startCrop(Bitmap bitmap) {
        mClipLayout.setImageBitmap(bitmap);
    }

    private void clipImage() {
        final Bitmap bitmap = mClipLayout.clip();
        new AsyncTask<Void, Void, Boolean>() {

            @Override protected Boolean doInBackground(Void... params) {
                return ImageUtil.saveBitmap(bitmap, mDesFilePath, Bitmap.CompressFormat.JPEG, 85);
            }

            @Override protected void onPostExecute(Boolean success) {
                if (success) {
                    Intent intent = new Intent();
                    intent.putExtra(RESULT_PATH, mDesFilePath);
                    setResult(RESULT_OK, intent);
                }
                finish();
            }
        }.execute();
    }
}
