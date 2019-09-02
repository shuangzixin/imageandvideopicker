package com.app.imagepicker;

import android.app.Activity;
import android.content.Intent;

import androidx.annotation.IntDef;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;

import com.app.imagepicker.activity.ImagePickerActivity;
import com.app.imagepicker.activity.VideoPickerActivity;
import com.app.imagepicker.beans.ImageItem;
import com.app.imagepicker.beans.VideoItem;
import com.app.imagepicker.utils.ScreenUtil;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;

import static com.app.imagepicker.constants.PickMode.MODE_CROP;
import static com.app.imagepicker.constants.PickMode.MODE_IMAGE;
import static com.app.imagepicker.constants.PickMode.MODE_VIDEO;

/**
 * 类功能描述
 * <p>
 * Created by stefan on 2018/1/22.
 */
public class AndroidImagePicker {

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({MODE_IMAGE, MODE_CROP, MODE_VIDEO})
    public @interface PickMode {
    }

    private Activity mActivity;

    private Fragment mFragment;

    private @StringRes
    int pickRes = R.string.general_send;

    /*
    最多选择几张图片
     */
    private int maxCount = 1;

    private int pickMode = MODE_IMAGE;

    //每一行显示的图片个数
    private int rowCount = 4;
    private boolean showCamera = false;
    //裁剪图片时宽高
    private int width = 0;
    private int height = 0;

    private String avatarFilePath;

    private ArrayList<ImageItem> imageSelected;

    private ArrayList<VideoItem> videoSelected;

    private AndroidImagePicker(Activity activity) {
        mActivity = activity;
    }

    private AndroidImagePicker(Fragment fragment) {
        mFragment = fragment;
    }

    public static AndroidImagePicker from(Activity activity) {
        return new AndroidImagePicker(activity);
    }

    public static AndroidImagePicker from(Fragment fragment) {
        return new AndroidImagePicker(fragment);
    }

    public AndroidImagePicker maxCount(int maxCount) {
        this.maxCount = maxCount;
        return this;
    }

    public AndroidImagePicker rowCount(int rowCount) {
        this.rowCount = rowCount;
        return this;
    }

    public AndroidImagePicker setImageSelected(ArrayList<ImageItem> imageSelected) {
        if (imageSelected == null) {
            this.imageSelected = new ArrayList<>();
        } else {
            this.imageSelected = imageSelected;
        }
        return this;
    }

    public AndroidImagePicker setVideoSelected(ArrayList<VideoItem> videoSelected) {
        if (videoSelected == null) {
            this.videoSelected = new ArrayList<>();
        } else {
            this.videoSelected = videoSelected;
        }
        return this;
    }

    public AndroidImagePicker pickMode(@PickMode int mode) {
        this.pickMode = mode;
        return this;
    }

    public AndroidImagePicker cropFilePath(String filePath) {
        this.avatarFilePath = filePath;
        return this;
    }

    public AndroidImagePicker showCamera(boolean showCamera) {
        this.showCamera = showCamera;
        return this;
    }

    public AndroidImagePicker width(int width) {
        if (width <= 0) {
            if (mActivity != null) {
                this.width = ScreenUtil.getInstance().getScreenWidth(mActivity);
            } else if (mFragment != null) {
                this.width = ScreenUtil.getInstance().getScreenWidth(mFragment.getActivity());
            }
        } else {
            this.width = width;
        }
        return this;
    }

    public AndroidImagePicker height(int height) {
        if (height <= 0) {
            if (mActivity != null) {
                this.height = ScreenUtil.getInstance().getScreenWidth(mActivity);
            } else if (mFragment != null) {
                this.height = ScreenUtil.getInstance().getScreenWidth(mFragment.getActivity());
            }
        } else {
            this.height = height;
        }
        return this;
    }

    public void startForResult(int requestCode) {
        ImagePickerConfiguration config = ImagePicker.getInstance().getConfig();
        if (config == null) {
            throw new IllegalArgumentException(
                    "you must call ImagePicker.getInstance().init() first");
        }
        Intent intent;
        if (mActivity != null) {
            if (pickMode == MODE_VIDEO) {
                intent = VideoPickerActivity.getIntent(maxCount, pickMode, rowCount, videoSelected, showCamera, pickRes, avatarFilePath);
                intent.setClass(mActivity, VideoPickerActivity.class);
                mActivity.startActivityForResult(intent, requestCode);
            } else {
                intent = ImagePickerActivity.getIntent(maxCount, pickMode, rowCount, imageSelected, showCamera,
                        width, height, pickRes, avatarFilePath);
                intent.setClass(mActivity, ImagePickerActivity.class);
                mActivity.startActivityForResult(intent, requestCode);
            }
        } else if (mFragment != null) {
            if (pickMode == MODE_VIDEO) {
                intent = VideoPickerActivity.getIntent(maxCount, pickMode, rowCount, videoSelected, showCamera, pickRes, avatarFilePath);
                intent.setClass(mFragment.getActivity(), VideoPickerActivity.class);
                mActivity.startActivityForResult(intent, requestCode);
            } else {
                intent = ImagePickerActivity.getIntent(maxCount, pickMode, rowCount, imageSelected, showCamera,
                        width, height, pickRes, avatarFilePath);
                intent.setClass(mFragment.getActivity(), ImagePickerActivity.class);
                mActivity.startActivityForResult(intent, requestCode);
            }
        } else {
            throw new IllegalArgumentException("you must call from() first");
        }
    }
}
