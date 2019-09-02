package com.app.imagepicker.adapter;

import android.graphics.Bitmap;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.StringRes;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 类功能描述
 *
 * Created by stefan on 2018/1/22.
 */
public class RecyclerHolder extends RecyclerView.ViewHolder {

    private final SparseArray<View> mViews;


    public RecyclerHolder(View itemView) {
        super(itemView);
        this.mViews = new SparseArray<View>();
    }

    /**
     * 通过控件的Id获取对于的控件，如果没有则加入views
     *
     * @param viewId
     * @return
     */
//    @SuppressWarnings("unchecked")
    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = itemView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    /**
     * 为TextView设置字符串
     *
     * @param viewId
     * @param text
     * @return
     */
    public RecyclerHolder setText(int viewId, String text) {
        TextView view = getView(viewId);
        view.setText(text);
        return this;
    }

    public RecyclerHolder setTextColor(int viewId, @ColorInt int color) {
        TextView view = getView(viewId);
        view.setTextColor(color);
        return this;
    }

    public RecyclerHolder setBackgroundResource(int viewId, int resId) {
        View view = getView(viewId);
        view.setBackgroundResource(resId);
        return this;
    }


    /**
     * 为TextView设置字符串
     *
     * @param viewId
     * @param resId
     * @return
     */
    public RecyclerHolder setText(int viewId, @StringRes int resId) {
        TextView view = getView(viewId);
        view.setText(resId);
        return this;
    }

    /**
     * 为ImageView设置图片
     *
     * @param viewId
     * @param drawableId
     * @return
     */
    public RecyclerHolder setImageResource(int viewId, int drawableId) {
        ImageView view = getView(viewId);
        view.setImageResource(drawableId);

        return this;
    }


    public <T extends View> RecyclerHolder setOnClickListener(int viewId, View.OnClickListener listener) {
        T view = getView(viewId);
        view.setOnClickListener(listener);
        return this;
    }

    /**
     * 为ImageView设置图片
     *
     * @param viewId
     * @param bm
     * @return
     */
    public RecyclerHolder setImageBitmap(int viewId, Bitmap bm) {
        ImageView view = getView(viewId);
        view.setImageBitmap(bm);
        return this;
    }

    public RecyclerHolder setVisible(int viewId, int visibility) {
        getView(viewId).setVisibility(visibility);
        return this;
    }

}

