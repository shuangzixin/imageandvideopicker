package com.app.imagepicker.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.app.imagepicker.R;

/**
 * 类功能描述
 *
 * Created by stefan on 2018/1/22.
 */
public class SquareRelelativeLayout extends RelativeLayout {

    public ImageView photo;
    public TextView videoDuration;
    public CheckBox checkBox;

    public SquareRelelativeLayout(Context context) {
        super(context);
    }

    public SquareRelelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareRelelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        checkBox = (CheckBox) findViewById(R.id.checkbox);
        videoDuration = (TextView) findViewById(R.id.video_duration);
        photo = (ImageView) findViewById(R.id.image);
    }

    public void setPhotoView(ImageView imageView) {
        ViewGroup.LayoutParams layoutParams =
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
        this.photo = imageView;
        addView(imageView, 0, layoutParams);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getDefaultSize(0, widthMeasureSpec),
                getDefaultSize(0, heightMeasureSpec));
        int childWidthSize = getMeasuredWidth();
        heightMeasureSpec = widthMeasureSpec = MeasureSpec.makeMeasureSpec(
                childWidthSize, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

}
