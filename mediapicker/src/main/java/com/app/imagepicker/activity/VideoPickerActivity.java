/*
 *
 *  * Copyright (C) 2017 CIIC Guanaitong, Co.,Ltd.
 *  * All rights reserved.
 *
 */

package com.app.imagepicker.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListPopupWindow;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.imagepicker.GridInsetDecoration;
import com.app.imagepicker.R;
import com.app.imagepicker.adapter.VideoFolderAdapter;
import com.app.imagepicker.adapter.VideoItemAdapter;
import com.app.imagepicker.base.activity.BaseActivity;
import com.app.imagepicker.beans.VideoFolder;
import com.app.imagepicker.beans.VideoItem;
import com.app.imagepicker.constants.PickMode;
import com.app.imagepicker.controller.LoadVideo;
import com.app.imagepicker.controller.LoadVideoListener;
import com.app.imagepicker.listeners.OnVideoItemListener;
import com.app.imagepicker.utils.CollectionUtil;
import com.app.imagepicker.utils.TakePhotoHelper;
import com.app.imagepicker.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import static com.app.imagepicker.activity.ImageCropActivity.RESULT_PATH;

/**
 * 类功能描述
 *
 * Created by stefan on 2018/1/22.
 */
public class VideoPickerActivity extends BaseActivity implements LoadVideoListener {

    private static final String TAG = "VideoPickerActivity";

    public static final String EXTRA_PICK_MODE = TAG + ".extra.pick.mode";
    public static final String EXTRA_MAX_COUNT = TAG + ".extra.pick.maxCount";
    public static final String EXTRA_SELECTED = TAG + ".extra.pick.selected";
    public static final String EXTRA_ROW_COUNT = TAG + ".extra.pick.rowCount";
    public static final String EXTRA_SHOW_CAMERA = TAG + ".extra.pick.showCamera";
    public static final String EXTRA_CUSTOM_PICK_TEXT_HINT_RES = TAG + ".extra.pick.textHint";
    public static final String EXTRA_AVATAR_PATH = TAG + ".extra.pick.avatar.path";

    private int mMaxCount;
    private int mPickMode;
    private int mRowCount;
    private boolean mShowCamera = false;
    private String mAvatarFilePath;

    private @StringRes
    int mPickTextHintRes;

    public static final String EXTRA_FILE_CHOOSE_INTERCEPTOR = "EXTRA_FILE_CHOOSE_INTERCEPTOR";

    public static final int REQUEST_CODE_PICKER_PREVIEW = 100;
    public static final int REQUEST_CODE_CROP_IMAGE = 101;

    private RecyclerView mRecyclerView;
    private VideoItemAdapter mVideoItemAdapter;

    //private ImageView mNavIcon;
    //private TextView mTitle;
    /**
     * general_ok
     */
    private TextView mSureTv;

    private VideoFolder mCurrFolder;
    /**
     * 全部视频
     */
    private Button mBtnDir;
    private ListPopupWindow mFolderPopupWindow;
    private View mFooterView;
    private List<VideoFolder> mFolders;
    private List<VideoItem> mVideoItems = new ArrayList<>();
    private VideoFolderAdapter mVideoFolderAdapter;

    private ArrayList<VideoItem> mSelectedItems;
    private TextView mSelectedSize;
    private LoadVideo mLoadVideo;

    public static Intent getIntent(int maxCount, int pickMode, int rowCount,
        ArrayList<VideoItem> selected, boolean showCamera, @StringRes int pickRes,
        String avatarFilePath) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_MAX_COUNT, maxCount);
        intent.putExtra(EXTRA_PICK_MODE, pickMode);
        intent.putParcelableArrayListExtra(EXTRA_SELECTED, selected);
        intent.putExtra(EXTRA_ROW_COUNT, rowCount);
        intent.putExtra(EXTRA_SHOW_CAMERA, showCamera);
        intent.putExtra(EXTRA_CUSTOM_PICK_TEXT_HINT_RES, pickRes);
        //        intent.putExtra(EXTRA_FILE_CHOOSE_INTERCEPTOR, fileChooseInterceptor);
        intent.putExtra(EXTRA_AVATAR_PATH, avatarFilePath);
        return intent;
    }

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mLoadVideo = new LoadVideo(this, this);

        mLoadVideo.loadVideoFolders();

        mLoadVideo.loadAllVideos();
    }

    @Override protected void initView() {
        mRecyclerView = findViewById(R.id.recycler_view);
        mSureTv = findViewById(R.id.sureTv);
        mSelectedSize = findViewById(R.id.selected_image_size);
        mFooterView = findViewById(R.id.footer_panel);
        mBtnDir = findViewById(R.id.btn_dir);

        mTitleBar.setActivity(this);
        mTitleBar.setMiddleTitle(R.string.general_all_videos);
        mTitleBar.setBackBtnIcon(R.drawable.default_back);
        updateSize(mSelectedItems);
        GridLayoutManager layoutManager = new GridLayoutManager(this, mRowCount);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(
            new GridInsetDecoration(getResources().getDimensionPixelSize(R.dimen.dp_1)));

        mSureTv.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                setResultAction();
                finish();
            }
        });
        mBtnDir.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                backgroundAlpha(0.3f);
                if (mFolderPopupWindow == null) {
                    createPopupFolderList();
                }
                if (mFolderPopupWindow.isShowing()) {
                    mFolderPopupWindow.dismiss();
                } else {
                    mFolderPopupWindow.show();
                }
            }
        });
    }

    private void updateSize(ArrayList<VideoItem> items) {
        int count = CollectionUtil.isEmpty(items) ? 0 : items.size();
        if (count > 0) {
            mSelectedSize.setVisibility(View.VISIBLE);
            mSelectedSize.setText(String.valueOf(count));
        } else {
            mSelectedSize.setVisibility(View.GONE);
        }
    }

    private void setResultAction() {
        ArrayList<VideoItem> items = new ArrayList<>();
        if (mVideoItemAdapter != null) {
            items = mVideoItemAdapter.getSelectedItems();
        }

        Intent bundle = new Intent();
        bundle.putParcelableArrayListExtra(EXTRA_SELECTED, items);
        setResult(RESULT_OK, bundle);
    }

    @Override public int getLayoutResourceId() {
        return R.layout.activity_video_picker;
    }

    @Override protected void handleIntent(Intent intent) {
        mMaxCount = intent.getIntExtra(EXTRA_MAX_COUNT, 1);
        mPickMode = intent.getIntExtra(EXTRA_PICK_MODE, PickMode.MODE_VIDEO);
//        mShowCamera = intent.getBooleanExtra(EXTRA_SHOW_CAMERA, false);
        mShowCamera = false;
        mRowCount = intent.getIntExtra(EXTRA_ROW_COUNT, 4);
        mAvatarFilePath = intent.getStringExtra(EXTRA_AVATAR_PATH);
        mPickTextHintRes =
            intent.getIntExtra(EXTRA_CUSTOM_PICK_TEXT_HINT_RES, R.string.general_send);

        mSelectedItems = intent.getParcelableArrayListExtra(EXTRA_SELECTED);
    }

    @Override public void loadFolders(List<VideoFolder> folders) {
        mFolders = folders;
    }

    @Override public void loadVideos(List<VideoItem> items) {
        mVideoItems.clear();
        mVideoItems = items;
        if (mVideoItemAdapter == null) {
            initVideoItemAdapter(items);
        } else {
            mVideoItemAdapter.updateItems(items);
        }
    }

    private void initVideoItemAdapter(final List<VideoItem> items) {
        mVideoItemAdapter =
            new VideoItemAdapter(mRecyclerView, items, mMaxCount, mShowCamera, mPickMode);
        final TakePhotoHelper mHelper = new TakePhotoHelper(this);
        mVideoItemAdapter.setSelectItems(mSelectedItems);
        mVideoItemAdapter.setVideoItemListener(new OnVideoItemListener() {
            @Override public void onSelected(VideoItem item) {
                updateSize(mVideoItemAdapter.getSelectedItems());
            }

            @Override public void onUnSelected(VideoItem item) {
                updateSize(mVideoItemAdapter.getSelectedItems());
            }

            @Override public void onMaxChecked(int max) {
                ToastUtil.getInstance()
                    .show(VideoPickerActivity.this,
                        getResources().getString(R.string.string_beyong_max_video, max));
            }

            @Override public void onPreview(VideoItem item, int pos, boolean checked,
                ArrayList<VideoItem> mSelectedItems) {

                ArrayList<VideoItem> list = new ArrayList<>();
                list.addAll(mVideoItems);
                VideoPreviewActivity.start(VideoPickerActivity.this, list, mSelectedItems,
                    mMaxCount, pos, checked, REQUEST_CODE_PICKER_PREVIEW);
            }

            @Override public void onTakePhoto() {
                mHelper.takePhoto();
            }
        });
        mRecyclerView.setAdapter(mVideoItemAdapter);
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PICKER_PREVIEW) {
            if (data != null) {
                ArrayList<VideoItem> selected =
                    data.getParcelableArrayListExtra(VideoPreviewActivity.EXTRA_SELECTED_VIDEOS);
                mVideoItemAdapter.setSelectItems(selected);

                updateSize(selected);

                boolean finish = data.getBooleanExtra(VideoPreviewActivity.EXTRA_FINISH, false);

                if (finish) {
                    setResultAction();
                    finish();
                }
            }
        } else if (REQUEST_CODE_CROP_IMAGE == requestCode) {
            if (data != null) {

                String destFile = data.getStringExtra(RESULT_PATH);

                Intent bundle = new Intent();
                bundle.putExtra(RESULT_PATH, destFile);
                setResult(RESULT_OK, bundle);
                finish();
            }
        }
    }

    /**
     * 创建弹出的ListView
     */
    private void createPopupFolderList() {
        mFolderPopupWindow = new ListPopupWindow(this);
        final int width = getResources().getDisplayMetrics().widthPixels;
        final int height = getResources().getDisplayMetrics().heightPixels;
        //mFolderPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mVideoFolderAdapter = new VideoFolderAdapter(this, mFolders, 0);
        mFolderPopupWindow.setAdapter(mVideoFolderAdapter);
        mFolderPopupWindow.setContentWidth(width);
        mFolderPopupWindow.setWidth(width);
        mFolderPopupWindow.setHeight(height * 5 / 8);
        mFolderPopupWindow.setAnchorView(mFooterView);
        mFolderPopupWindow.setModal(true);

        mFolderPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override public void onDismiss() {
                backgroundAlpha(1f);
            }
        });

        mFolderPopupWindow.setAnimationStyle(R.style.popupwindow_anim_style);

        mFolderPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                mFolderPopupWindow.dismiss();

                VideoFolder folder = mFolders.get(position);
                mCurrFolder = folder;

                mVideoFolderAdapter.setCurrPos(position);

                mTitleBar.setMiddleTitle(mCurrFolder.name);
                mBtnDir.setText(mCurrFolder.name);

                mLoadVideo.reloadVideos(folder);
            }
        });
    }

    // 设置屏幕透明度
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; // 0.0~1.0
        getWindow().setAttributes(lp);
    }
}
