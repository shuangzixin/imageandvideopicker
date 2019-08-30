package com.app.imagepicker;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 类功能描述
 *
 * Created by stefan on 2018/1/22.
 */
public class GridInsetDecoration extends RecyclerView.ItemDecoration {


    private int offset;

    public GridInsetDecoration(int offset) {
        this.offset = offset;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {
        GridLayoutManager.LayoutParams layoutParams =
                (GridLayoutManager.LayoutParams) view.getLayoutParams();
        int position = layoutParams.getViewAdapterPosition();
        if (position == RecyclerView.NO_POSITION) {
            outRect.set(0, 0, 0, 0);
            return;
        }
        outRect.set(offset, offset, offset, offset);
    }

}
