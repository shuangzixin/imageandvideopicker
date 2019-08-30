/*
 *
 *  * Copyright (C) 2017 CIIC Guanaitong, Co.,Ltd.
 *  * All rights reserved.
 *
 */

package com.app.imagepicker.activity;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.imagepicker.R;
import com.app.imagepicker.adapter.VideoPreviewAdapter;
import com.app.imagepicker.base.activity.BaseActivity;
import com.app.imagepicker.beans.VideoItem;
import com.app.imagepicker.listeners.OnVideoPreviewListener;
import com.app.imagepicker.utils.CollectionUtil;
import com.app.imagepicker.utils.LogUtil;
import com.app.imagepicker.utils.ToastUtil;
import com.app.imagepicker.widget.TextureVideoPlayer;

import java.util.ArrayList;

/**
 * 类功能描述
 *
 * Created by stefan on 2018/1/22.
 */
public class VideoPreviewActivity extends BaseActivity {

    private static final String TAG = "VideoPreviewActivity";
    private static final String EXTRA_ALL_IMAGES = TAG + ".extra.all.images";
    private static final String EXTRA_MAX_COUNT = TAG + ".extra.max.count";
    private static final String EXTRA_CURRENT_POS = TAG + ".extra.curr.pos";
    private static final String EXTRA_IS_CHECKED = TAG + ".extra.is.checked";
    public static final String EXTRA_SELECTED_VIDEOS = VideoPickerActivity.EXTRA_SELECTED;
    public static final String EXTRA_FINISH =  TAG + ".extra.finish";

    //private PreviewViewPager mViewpager;
    private RelativeLayout mRlTotal;
    private TextureVideoPlayer mTvVideo;
    private ImageView mIvFirstPic;
    private ImageView mIvPause;
    private RelativeLayout mPickerBottom;
    private TextView mSureTv;
    private TextView mHasSelectedSize;
    private TextView mSelectedSize;

    private ArrayList<VideoItem> mItems;
    private ArrayList<VideoItem> mSelectedItems;

    private int mMaxCount;
    private int mCurrentPosition;

    public static void start(Activity context, ArrayList<VideoItem> items,
        ArrayList<VideoItem> selectedItems, int maxCount, int currPos, boolean isChecked,
        int requestCode) {
        Intent starter = new Intent(context, VideoPreviewActivity.class);
        starter.putParcelableArrayListExtra(EXTRA_ALL_IMAGES, items);
        starter.putParcelableArrayListExtra(EXTRA_SELECTED_VIDEOS, selectedItems);

        starter.putExtra(EXTRA_MAX_COUNT, maxCount);
        starter.putExtra(EXTRA_CURRENT_POS, currPos);
        starter.putExtra(EXTRA_IS_CHECKED, isChecked);

        context.startActivityForResult(starter, requestCode);
    }

    @Override protected void handleIntent(Intent intent) {

        mItems = intent.getParcelableArrayListExtra(EXTRA_ALL_IMAGES);
        mSelectedItems = intent.getParcelableArrayListExtra(EXTRA_SELECTED_VIDEOS);

        if (mSelectedItems == null) {
            mSelectedItems = new ArrayList<>();
        }

        mMaxCount = intent.getIntExtra(EXTRA_MAX_COUNT, 1);
        mCurrentPosition = intent.getIntExtra(EXTRA_CURRENT_POS, 0);
    }

    @Override protected void initView() {
        Window w = getWindow();
        w.addFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        //mViewpager = findViewById(R.id.viewpager);
        mRlTotal = findViewById(R.id.rl_total);
        mTvVideo = findViewById(R.id.tv_video);
        mIvFirstPic = findViewById(R.id.iv_first_pic);
        mIvPause = findViewById(R.id.iv_pause);
        mPickerBottom = findViewById(R.id.bottom_bar);
        mSelectedSize = findViewById(R.id.selected_image_size);
        mHasSelectedSize = findViewById(R.id.has_selected_size);
        mSureTv = findViewById(R.id.sureTv);

        mTitleBar.setActivity(this);
        mTitleBar.setBackBtnIcon(R.drawable.default_back);
        RelativeLayout.LayoutParams params =
            (RelativeLayout.LayoutParams) mContentView.getLayoutParams();
        params.addRule(RelativeLayout.BELOW, 0);
        mContentView.setLayoutParams(params);
        final VideoItem item = mItems.get(mCurrentPosition);
        if (mSelectedItems.contains(item)) {
            mTitleBar.setCheckBoxChecked(true);
        } else {
            mTitleBar.setCheckBoxChecked(false);
        }
        ArrayList<VideoItem> mPreview = new ArrayList<>();
        mPreview.add(mItems.get(mCurrentPosition));
        VideoPreviewAdapter
            mVideoPreviewAdapter = new VideoPreviewAdapter(mPreview, mSelectedItems, mMaxCount);
        mVideoPreviewAdapter.setPotoTapListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                updateScreen();
            }
        });
        mVideoPreviewAdapter.setOnPreviewListener(new OnVideoPreviewListener() {
            @Override public void onSelected(VideoItem item, ArrayList<VideoItem> selectedItems) {
                updateTitle();
            }

            @Override public void onBottomBarVisibilityChanged(int visibility) {
                mTitleBar.setVisibility(visibility);
            }
        });
        //mViewpager.setAdapter(mVideoPreviewAdapter);
        //mViewpager.setCurrentItem(mCurrentPosition);
        mTitleBar.setmRvMenuCheckboxOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                boolean isChecked = mTitleBar.getCheckBoxIsChecked();
                LogUtil.i("aaa", "isChecked:" + isChecked);
                if (isChecked) {
                    if (mSelectedItems.size() >= mMaxCount) {
                        mTitleBar.setCheckBoxChecked(false);
                        ToastUtil.getInstance().show(VideoPreviewActivity.this, getResources().getString(R.string.string_beyong_max_video, mMaxCount));
                        return;
                    }
                    //VideoItem item = mItems.get(mViewpager.getCurrentItem());
                    //mSelectedItems.add(item);
                } else {
                    //VideoItem item = mItems.get(mViewpager.getCurrentItem());
                    //mSelectedItems.remove(item);
                }
                updateTitle();
            }
        });
        //mViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
        //    @Override public void onPageScrolled(int position, float positionOffset,
        //        int positionOffsetPixels) {
        //
        //    }
        //
        //    @Override public void onPageSelected(int position) {
        //        VideoItem item = mItems.get(position);
        //        if (mSelectedItems.contains(item)) {
        //            mTitleBar.setCheckBoxChecked(true);
        //        } else {
        //            mTitleBar.setCheckBoxChecked(false);
        //        }
        //    }
        //
        //    @Override public void onPageScrollStateChanged(int state) {
        //
        //    }
        //});
        mTvVideo.setOnVideoPlayingListener(new TextureVideoPlayer.OnVideoPlayingListener() {
            @Override public void onVideoSizeChanged(int vWidth, int vHeight) {

            }

            @Override public void onStart() {
                mIvFirstPic.setVisibility(View.GONE);
            }

            @Override public void onPlaying(int duration, int percent) {

            }

            @Override public void onPause() {

            }

            @Override public void onRestart() {

            }

            @Override public void onPlayingFinish() {
                mIvFirstPic.setVisibility(View.VISIBLE);
                mIvPause.setVisibility(View.VISIBLE);
            }

            @Override public void onTextureDestory() {
                if (mTvVideo != null) {
                    mTvVideo.pause();
                }
            }
        });
        mRlTotal.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                if(mIvFirstPic.getVisibility() == View.VISIBLE){
                    mIvPause.setVisibility(View.GONE);
                    mTvVideo.stop();
                    mTvVideo.setUrl(item.path);
                    mTvVideo.play();
                }else{
                    if(mTvVideo.isPlaying()){
                        mIvPause.setVisibility(View.VISIBLE);
                    }else{
                        mIvPause.setVisibility(View.GONE);
                    }
                    mTvVideo.pause();
                }
            }
        });
        mSureTv.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                setResultAction(true);
                finish();
            }
        });

        try {
            MediaMetadataRetriever media = new MediaMetadataRetriever();
            media.setDataSource(item.path);
            mIvFirstPic.setImageBitmap(media.getFrameAtTime());
        }catch (IllegalArgumentException e){
            e.printStackTrace();
        }
        //ImagePicker.getInstance().getConfig().getImageLoader().loadImage(mIvFirstPic, item.path);
        updateTitle();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTvVideo.release();
    }

    @Override public int getLayoutResourceId() {
        return R.layout.activity_video_preview;
    }

    private void fullScreen() {
        mTitleBar.setVisibility(View.GONE);
        mPickerBottom.setVisibility(View.GONE);
    }

    private void exitFullScreen() {
        mTitleBar.setVisibility(View.VISIBLE);
        mPickerBottom.setVisibility(View.VISIBLE);
    }

    private void updateScreen() {
        if (mTitleBar.getVisibility() == View.VISIBLE) {
            fullScreen();
        } else {
            exitFullScreen();
        }
    }

    private void updateTitle() {

        int count = CollectionUtil.isEmpty(mSelectedItems) ? 0 : mSelectedItems.size();
        if (count > 0) {
            //mSelectedSize.setVisibility(View.VISIBLE);
            mSelectedSize.setText(String.valueOf(count));
        } else {
            mSelectedSize.setVisibility(View.GONE);
        }

        mHasSelectedSize.setText(getResources().getString(R.string.string_has_selected_video, count));

        //mTitle.setText(String.format("%s/%d", mSelectedItems.size(), mMaxCount));

    }

    private void setResultAction(boolean finish) {
        Intent data = new Intent();
        if(mSelectedItems.size() == 0){
            mSelectedItems.add(mItems.get(mCurrentPosition));
        }
        data.putParcelableArrayListExtra(EXTRA_SELECTED_VIDEOS, mSelectedItems);
        data.putExtra(EXTRA_FINISH, finish);
        setResult(RESULT_OK, data);
    }
}
