/*
 *
 *  * Copyright (C) 2017 CIIC Guanaitong, Co.,Ltd.
 *  * All rights reserved.
 *
 */

package com.app.imagepicker.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;

import com.app.imagepicker.ImagePicker;
import com.app.imagepicker.R;
import com.app.imagepicker.beans.VideoItem;
import com.app.imagepicker.listeners.OnVideoPreviewListener;
import com.app.imagepicker.widget.TextureVideoPlayer;

import java.util.ArrayList;

/**
 * 类功能描述
 *
 * Created by stefan on 2018/1/22.
 */
public class VideoPreviewAdapter extends PagerAdapter {

    private final ArrayList<VideoItem> mItems;

    private final ArrayList<VideoItem> mSelectedItems;

    private final int mMax;
    private OnVideoPreviewListener mListener;

    public VideoPreviewAdapter(ArrayList<VideoItem> items, ArrayList<VideoItem> selectedItems,
        int max) {
        mItems = items;
        mSelectedItems = selectedItems;
        mMax = max;
    }

    public void setOnPreviewListener(OnVideoPreviewListener listener) {
        mListener = listener;
    }

    @Override public Object instantiateItem(final ViewGroup container, final int position) {
        final View view = LayoutInflater.from(container.getContext())
            .inflate(R.layout.layout_video_preview_item, null);
        final TextureVideoPlayer tvVideo = view.findViewById(R.id.tv_video);
        final ImageView ivFirstPic = view.findViewById(R.id.iv_first_pic);
        final ImageView ivPause = view.findViewById(R.id.iv_pause);
        final VideoItem item = mItems.get(position);

        ivFirstPic.setVisibility(View.VISIBLE);
        ivPause.setVisibility(View.VISIBLE);
        ImagePicker.getInstance().getConfig().getImageLoader().loadImage(ivFirstPic, item.path);

        tvVideo.setOnVideoPlayingListener(new TextureVideoPlayer.OnVideoPlayingListener() {
            @Override public void onVideoSizeChanged(int vWidth, int vHeight) {

            }

            @Override public void onStart() {
                ivFirstPic.setVisibility(View.GONE);
                ivPause.setVisibility(View.GONE);
            }

            @Override public void onPlaying(int duration, int percent) {

            }

            @Override public void onPause() {

            }

            @Override public void onRestart() {

            }

            @Override public void onPlayingFinish() {
                ivFirstPic.setVisibility(View.VISIBLE);
                ivPause.setVisibility(View.VISIBLE);
            }

            @Override public void onTextureDestory() {
                if (tvVideo != null) {
                    tvVideo.pause();
                }
            }
        });
        ivPause.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                tvVideo.stop();
                tvVideo.setUrl(item.path);
                tvVideo.play();
            }
        });

        return view;
    }

    private void hideBottomBar(View bottom) {
        bottom.setVisibility(View.GONE);
    }

    private void showBottomBar(final View bottom) {
        bottom.setVisibility(View.VISIBLE);
    }

    private View.OnClickListener mPotoTapListener;

    public void setPotoTapListener(View.OnClickListener l) {
        mPotoTapListener = l;
    }

    @Override public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override public int getCount() {
        return mItems.size();
    }

    @Override public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    public boolean isMax() {
        return mSelectedItems.size() >= mMax;
    }
}
