package com.app.imagepicker.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.viewpager.widget.PagerAdapter;

import com.app.imagepicker.R;
import com.app.imagepicker.beans.ImageItem;
import com.app.imagepicker.listeners.OnImagePreviewListener;
import com.app.imagepicker.widget.PicturePreviewPageView;
import com.app.imagepicker.widget.scaleview.ImageSource;

import java.util.ArrayList;

/**
 * 类功能描述
 *
 * Created by stefan on 2018/1/22.
 */
public class ImagePreviewAdapter extends PagerAdapter {


    private final ArrayList<ImageItem> mItems;

    private final ArrayList<ImageItem> mSelectedItems;

    private final int mMax;
    private OnImagePreviewListener mListener;

    public ImagePreviewAdapter(ArrayList<ImageItem> items, ArrayList<ImageItem> selectedItems, int max) {
        mItems = items;
        mSelectedItems = selectedItems;
        mMax = max;
    }

    public void setOnPreviewListener(OnImagePreviewListener listener) {
        mListener = listener;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        final View view = LayoutInflater.from(container.getContext()).inflate(R.layout.layout_image_preview_item, null);
        final PicturePreviewPageView pageView = (PicturePreviewPageView) view.findViewById(R.id.picture_view);

        pageView.setMaxScale(15);

        pageView.setOnClickListener(mPotoTapListener);

        final ImageItem item = mItems.get(position);

        pageView.setOriginImage(ImageSource.uri(item.path));
        pageView.setBackgroundColor(Color.TRANSPARENT);
        container.addView(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        pageView.setTag(position);


        return view;
    }

    private void hideBottomBar(View bottom) {
        bottom.setVisibility(View.GONE);
    }

    private void showBottomBar(final View bottom) {
        bottom.setVisibility(View.VISIBLE);
    }

    private View.OnClickListener mPotoTapListener; ;

    public void setPotoTapListener(View.OnClickListener l) {
        mPotoTapListener = l;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    public boolean isMax() {
        return mSelectedItems.size() >= mMax;
    }
}
