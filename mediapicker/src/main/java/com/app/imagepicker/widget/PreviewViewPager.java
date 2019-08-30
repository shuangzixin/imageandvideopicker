package com.app.imagepicker.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

/**
 * 类功能描述
 *
 * Created by stefan on 2018/1/22.
 */
public class PreviewViewPager extends ViewPager {
    private int touchSlop;
    private boolean scrollEnabled = true;
    private boolean isDisallowIntercept = false;


    public PreviewViewPager(Context context) {
        this(context, null);
    }

    public PreviewViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        final ViewConfiguration vc = ViewConfiguration.get(getContext());
        touchSlop = vc.getScaledTouchSlop();
    }

    public void setScrollEnabled(boolean scrollEnabled) {
        this.scrollEnabled = scrollEnabled;
    }

    public boolean isDisallowIntercept() {
        return isDisallowIntercept;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (!scrollEnabled || event.getPointerCount() > 1) {
            return false;
        }
        try {
            return super.onInterceptTouchEvent(event);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    @Override
    public void requestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        isDisallowIntercept = disallowIntercept;
        super.requestDisallowInterceptTouchEvent(disallowIntercept);
    }

    // fix index out of range in event dispatch
    // see https://code.google.com/p/android/issues/detail?id=60464
    @Override
    public boolean dispatchTouchEvent(@NonNull MotionEvent ev) {
        if (ev.getPointerCount() > 1 && isDisallowIntercept) {
            requestDisallowInterceptTouchEvent(false);
            boolean handled = super.dispatchTouchEvent(ev);
            requestDisallowInterceptTouchEvent(true);
            return handled;
        } else {
            return super.dispatchTouchEvent(ev);
        }
    }
}
