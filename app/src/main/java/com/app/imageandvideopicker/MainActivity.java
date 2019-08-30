package com.app.imageandvideopicker;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.app.imagepicker.AndroidImagePicker;
import com.app.imagepicker.activity.ImageCropActivity;
import com.app.imagepicker.activity.ImagePickerActivity;
import com.app.imagepicker.activity.VideoPickerActivity;
import com.app.imagepicker.base.activity.BaseActivity;
import com.app.imagepicker.beans.ImageItem;
import com.app.imagepicker.beans.VideoItem;
import com.app.imagepicker.constants.PickMode;
import com.app.imagepicker.utils.LogUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    @BindView(R.id.tv_only_image)
    TextView tvOnlyImage;
    @BindView(R.id.tv_image_crop)
    TextView tvImageCrop;
    @BindView(R.id.tv_only_video)
    TextView tvOnlyVideo;
    @BindView(R.id.tv_result)
    TextView tvResult;

    private ArrayList<ImageItem> mImageSelectedItems = new ArrayList<>();
    private ArrayList<VideoItem> mVideoSelectedItems = new ArrayList<>();
    private static final int REQUEST_CODE_SELECT_IMAGE = 0x100;
    private static final int REQUEST_CODE_CROP_IMAGE = 0x101;
    private static final int REQUEST_CODE_SELECT_VIDEO = 0x102;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        super.initView();
        mTitleBar.setMiddleTitle("选择图片或视频");
    }

    @Override
    @OnClick({R.id.tv_only_image, R.id.tv_image_crop, R.id.tv_only_video})
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_only_image:
                AndroidImagePicker.from(MainActivity.this)
                        .pickMode(PickMode.MODE_IMAGE)
                        .rowCount(4)
                        .maxCount(4)
                        .setImageSelected(mImageSelectedItems)
                        .showCamera(true)
                        .startForResult(REQUEST_CODE_SELECT_IMAGE);
                break;
            case R.id.tv_image_crop:
                AndroidImagePicker.from(MainActivity.this)
                        .pickMode(PickMode.MODE_CROP)
                        .rowCount(4)
                        .maxCount(1)
                        .showCamera(true)
                        .startForResult(REQUEST_CODE_CROP_IMAGE);
                break;
            case R.id.tv_only_video:
                AndroidImagePicker.from(MainActivity.this)
                        .pickMode(PickMode.MODE_VIDEO)
                        .rowCount(4)
                        .maxCount(4)
                        .setVideoSelected(mVideoSelectedItems)
                        .showCamera(true)
                        .startForResult(REQUEST_CODE_SELECT_VIDEO);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        StringBuilder stringBuilder = new StringBuilder();
        if (requestCode == REQUEST_CODE_SELECT_IMAGE) {
            if (data != null) {
                ArrayList<ImageItem> extra =
                        data.getParcelableArrayListExtra(ImagePickerActivity.EXTRA_SELECTED);
                LogUtil.i("aaa", "extra.size():" + extra.size());
                if (extra != null && extra.size() > 0) {
                    for (int i = 0; i < extra.size(); i++) {
                        stringBuilder.append((i+1) + ":" + extra.get(i).path + "\n");
                    }
                }

            }
        } else if (requestCode == REQUEST_CODE_CROP_IMAGE) {
            if (data != null) {
                String path = data.getStringExtra(ImageCropActivity.RESULT_PATH);
                LogUtil.i("aaa", "path:" + path);
                stringBuilder.append(path + "\n");
            }
        } else if (requestCode == REQUEST_CODE_SELECT_VIDEO) {
            if (data != null) {
                ArrayList<VideoItem> extra =
                        data.getParcelableArrayListExtra(VideoPickerActivity.EXTRA_SELECTED);
                LogUtil.i("aaa", "extra.size():" + extra.size());
                if (extra != null && extra.size() > 0) {
                    for (int i = 0; i < extra.size(); i++) {
                        stringBuilder.append((i+1) + ":  " + extra.get(i).path + "\n");
                    }
                }
            }
        }
        tvResult.setText(TextUtils.isEmpty(stringBuilder.toString()) ? "" : stringBuilder.toString());
    }
}
