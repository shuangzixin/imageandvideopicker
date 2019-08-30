/*
 *
 *  * Copyright (C) 2017 CIIC Guanaitong, Co.,Ltd.
 *  * All rights reserved.
 *
 */

package com.app.imagepicker.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.app.imagepicker.ImagePicker;
import com.app.imagepicker.R;
import com.app.imagepicker.beans.ImageItem;
import com.app.imagepicker.constants.PickMode;
import com.app.imagepicker.listeners.OnImageItemListener;
import com.app.imagepicker.widget.SquareRelelativeLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * 类功能描述
 *
 * Created by stefan on 2018/1/22.
 */
public class ImageItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<ImageItem> mSelectedItems = new ArrayList<>();

    private OnImageItemListener mListener;

    private int mMaxCount;

    private boolean mShowCamera;

    private ArrayList<ImageItem> mItems = new ArrayList<>();

    private Context mContext;
    private boolean isScrolling;
    private int mPickMode;

    public ImageItemAdapter(RecyclerView v, List<ImageItem> datas, int max, boolean showCamera,
        int mode) {
        mContext = v.getContext();
        mMaxCount = max;
        mShowCamera = showCamera;
        mPickMode = mode;

        v.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                isScrolling = !(newState == RecyclerView.SCROLL_STATE_IDLE);
                //                if (!isScrolling) {
                //                    notifyDataSetChanged();
                //                }
            }
        });

        setItems(datas);
    }

    public ArrayList<ImageItem> getSelectedItems() {
        return mSelectedItems;
    }

    public void setSelectItems(ArrayList<ImageItem> items) {
        if (items == null) {
            mSelectedItems = new ArrayList<>();
        } else {
            mSelectedItems = items;
        }
        notifyDataSetChanged();
    }

    private void setItems(List<ImageItem> items) {
        mItems.clear();
        if (mShowCamera) {
            mItems.add(0, null);
        }

        mItems.addAll(items);
    }

    public void updateItems(List<ImageItem> items) {
        setItems(items);
        notifyDataSetChanged();
    }

    public void setImageItemListener(OnImageItemListener listener) {
        mListener = listener;
    }

    @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View root = inflater.inflate(R.layout.layout_image_pick_item, parent, false);
        return new RecyclerHolder(root);
    }

    @Override public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof RecyclerHolder) {
            convert((RecyclerHolder) holder, mItems.get(position), position, isScrolling);
        }
    }

    @Override public int getItemCount() {
        return mItems.size();
    }

    private boolean isMax() {
        return mSelectedItems.size() >= mMaxCount;
    }

    public void convert(RecyclerHolder holder, final ImageItem item, final int position,
        boolean isScrolling) {
        final SquareRelelativeLayout layout = holder.getView(R.id.photo_cell);
        if (mShowCamera && position == 0) {
            layout.checkBox.setVisibility(View.GONE);
            layout.setBackgroundColor(Color.parseColor("#666666"));
            RelativeLayout.LayoutParams lp =
                new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.addRule(RelativeLayout.CENTER_IN_PARENT);

            layout.photo.setLayoutParams(lp);

            layout.photo.setImageResource(R.drawable.ic_attachment_camera);

            layout.photo.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onTakePhoto();
                    }
                }
            });
            return;
        }

        layout.photo.setLayoutParams(
            new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        if (mSelectedItems.contains(item)) {
            layout.photo.setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
            layout.checkBox.setChecked(true);
        } else {
            layout.photo.clearColorFilter();
            layout.checkBox.setChecked(false);
        }

        layout.checkBox.setVisibility(mPickMode == PickMode.MODE_CROP ? View.GONE : View.VISIBLE);

        layout.photo.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                if (mListener != null) {
                    int pos = position;
                    if (mShowCamera) {
                        pos--;
                    }

                    mListener.onPreview(item, pos, layout.checkBox.isChecked(), mSelectedItems);
                }
            }
        });

        ImagePicker.getInstance().getConfig().getImageLoader().loadImage(layout.photo, item.path);

        layout.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                boolean isChecked = layout.checkBox.isChecked();
                if (isChecked) {

                    if (isMax()) {
                        if (mListener != null) {
                            mListener.onMaxChecked(mMaxCount);
                        }
                        layout.checkBox.setChecked(false);
                        return;
                    }

                    mSelectedItems.add(item);
                    layout.photo.setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);

                    if (mListener != null) {
                        mListener.onSelected(item);
                    }
                } else {
                    mSelectedItems.remove(item);
                    layout.photo.clearColorFilter();

                    if (mListener != null) {
                        mListener.onUnSelected(item);
                    }
                }
            }
        });
    }
}
