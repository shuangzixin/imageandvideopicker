/*
 *
 *  * Copyright (C) 2017 CIIC Guanaitong, Co.,Ltd.
 *  * All rights reserved.
 *
 */

package com.app.imagepicker.activity;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.viewpager.widget.ViewPager;

import com.app.imagepicker.R;
import com.app.imagepicker.adapter.ImagePreviewAdapter;
import com.app.imagepicker.base.activity.BaseActivity;
import com.app.imagepicker.beans.ImageItem;
import com.app.imagepicker.listeners.OnImagePreviewListener;
import com.app.imagepicker.utils.CollectionUtil;
import com.app.imagepicker.utils.LogUtil;
import com.app.imagepicker.utils.ToastUtil;
import com.app.imagepicker.widget.PreviewViewPager;

import java.util.ArrayList;

/**
 * 类功能描述
 *
 * Created by stefan on 2018/1/22.
 */
public class ImagePreviewActivity extends BaseActivity {

    private static final String TAG = "ImagePreviewActivity";
    private static final String EXTRA_ALL_IMAGES = TAG + ".extra.all.images";
    private static final String EXTRA_MAX_COUNT = TAG + ".extra.max.count";
    private static final String EXTRA_CURRENT_POS = TAG + ".extra.curr.pos";
    private static final String EXTRA_IS_CHECKED = TAG + ".extra.is.checked";
    public static final String EXTRA_SELECTED_IMAGES = ImagePickerActivity.EXTRA_SELECTED;
    public static final String EXTRA_FINISH =  TAG + ".extra.finish";

    private PreviewViewPager mViewpager;
    private RelativeLayout mPickerBottom;
    private TextView mSureTv;
    private TextView mHasSelectedSize;
    private TextView mSelectedSize;

    private ArrayList<ImageItem> mItems;
    private ArrayList<ImageItem> mSelectedItems;

    private int mMaxCount;
    private int mCurrentPosition;

    public static void start(Activity context, ArrayList<ImageItem> items,
        ArrayList<ImageItem> selectedItems, int maxCount, int currPos, boolean isChecked,
        int requestCode) {
        Intent starter = new Intent(context, ImagePreviewActivity.class);
        starter.putParcelableArrayListExtra(EXTRA_ALL_IMAGES, items);
        starter.putParcelableArrayListExtra(EXTRA_SELECTED_IMAGES, selectedItems);

        starter.putExtra(EXTRA_MAX_COUNT, maxCount);
        starter.putExtra(EXTRA_CURRENT_POS, currPos);
        starter.putExtra(EXTRA_IS_CHECKED, isChecked);

        context.startActivityForResult(starter, requestCode);
    }

    @Override protected void handleIntent(Intent intent) {

        mItems = intent.getParcelableArrayListExtra(EXTRA_ALL_IMAGES);
        mSelectedItems = intent.getParcelableArrayListExtra(EXTRA_SELECTED_IMAGES);

        if (mSelectedItems == null) {
            mSelectedItems = new ArrayList<>();
        }

        mMaxCount = intent.getIntExtra(EXTRA_MAX_COUNT, 1);
        mCurrentPosition = intent.getIntExtra(EXTRA_CURRENT_POS, 0);
    }

    @Override protected void initView() {
        mViewpager = findViewById(R.id.viewpager);
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
        ImageItem item = mItems.get(mCurrentPosition);
        if (mSelectedItems.contains(item)) {
            mTitleBar.setCheckBoxChecked(true);
        } else {
            mTitleBar.setCheckBoxChecked(false);
        }
        ImagePreviewAdapter
            mImagePreviewAdapter = new ImagePreviewAdapter(mItems, mSelectedItems, mMaxCount);
        mImagePreviewAdapter.setPotoTapListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                updateScreen();
            }
        });
        mImagePreviewAdapter.setOnPreviewListener(new OnImagePreviewListener() {
            @Override public void onSelected(ImageItem item, ArrayList<ImageItem> selectedItems) {
                updateTitle();
            }

            @Override public void onBottomBarVisibilityChanged(int visibility) {
                mTitleBar.setVisibility(visibility);
            }
        });
        mViewpager.setAdapter(mImagePreviewAdapter);
        mViewpager.setCurrentItem(mCurrentPosition);
        mTitleBar.setmRvMenuCheckboxOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                boolean isChecked = mTitleBar.getCheckBoxIsChecked();
                LogUtil.i("aaa", "isChecked:" + isChecked);
                if (isChecked) {
                    if (mSelectedItems.size() >= mMaxCount) {
                        mTitleBar.setCheckBoxChecked(false);
                        ToastUtil.getInstance().show(ImagePreviewActivity.this, getResources().getString(R.string.string_beyong_max, mMaxCount));
                        return;
                    }
                    ImageItem item = mItems.get(mViewpager.getCurrentItem());
                    mSelectedItems.add(item);
                } else {
                    ImageItem item = mItems.get(mViewpager.getCurrentItem());
                    mSelectedItems.remove(item);
                }
                updateTitle();
            }
        });
        mViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override public void onPageScrolled(int position, float positionOffset,
                int positionOffsetPixels) {

            }

            @Override public void onPageSelected(int position) {
                ImageItem item = mItems.get(position);
                if (mSelectedItems.contains(item)) {
                    mTitleBar.setCheckBoxChecked(true);
                } else {
                    mTitleBar.setCheckBoxChecked(false);
                }
            }

            @Override public void onPageScrollStateChanged(int state) {

            }
        });
        mSureTv.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                setResultAction(true);
                finish();
            }
        });

        updateTitle();
    }

    @Override public int getLayoutResourceId() {
        return R.layout.activity_image_preview;
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
            mSelectedSize.setVisibility(View.VISIBLE);
            mSelectedSize.setText(String.valueOf(count));
        } else {
            mSelectedSize.setVisibility(View.GONE);
        }

        mHasSelectedSize.setText(getResources().getString(R.string.string_has_selected, count));

        //mTitle.setText(String.format("%s/%d", mSelectedItems.size(), mMaxCount));

    }

    private void setResultAction(boolean finish) {
        Intent data = new Intent();
        if(mSelectedItems.size() == 0){
            ImageItem item = mItems.get(mViewpager.getCurrentItem());
            mSelectedItems.add(item);
        }
        data.putParcelableArrayListExtra(EXTRA_SELECTED_IMAGES, mSelectedItems);
        data.putExtra(EXTRA_FINISH, finish);
        setResult(RESULT_OK, data);
    }
}
