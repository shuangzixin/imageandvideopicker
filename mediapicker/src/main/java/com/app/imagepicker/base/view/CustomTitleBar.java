package com.app.imagepicker.base.view;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.app.imagepicker.R;
import com.readystatesoftware.systembartint.SystemBarTintManager;

/**
 * 类功能描述
 *
 * Created by stefan on 2018/1/12.
 */
public class CustomTitleBar extends LinearLayout {

    private LayoutInflater mInflater;
    private View mTitleBarView;
    private RelativeLayout mRvBack;
    private ImageView mIvBack;
    private TextView mTvTitleLeft;
    private TextView mTvTitleMiddle;
    private TextView mTvTitleRight;
    private RelativeLayout mRvMenu01;
    private ImageView mIvMenu01;
    private RelativeLayout mRvMenu02;
    private ImageView mIvMenu02;
    private RelativeLayout mRvMenuCheckbox;
    private CheckBox mCbSelect;

    // 页面变量
    private Activity mActivity;
    private Resources mResources;
    private OnTitleBarBackListener mBackListener;

    public CustomTitleBar(Context context) {
        super(context);
        init(context);
    }

    public CustomTitleBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        mResources = context.getResources();
        mInflater = LayoutInflater.from(context);
        mTitleBarView = mInflater.inflate(R.layout.widget_custom_titlebar, null);
        mRvBack = mTitleBarView.findViewById(R.id.rv_back);
        mIvBack = mTitleBarView.findViewById(R.id.iv_back);
        mTvTitleLeft = mTitleBarView.findViewById(R.id.tv_title_left);
        mTvTitleMiddle = mTitleBarView.findViewById(R.id.tv_title_middle);
        mTvTitleRight = mTitleBarView.findViewById(R.id.tv_title_right);
        mRvMenu01 = mTitleBarView.findViewById(R.id.rv_menu01);
        mIvMenu01 = mTitleBarView.findViewById(R.id.iv_menu01);
        mRvMenu02 = mTitleBarView.findViewById(R.id.rv_menu02);
        mIvMenu02 = mTitleBarView.findViewById(R.id.iv_menu02);
        mRvMenuCheckbox = mTitleBarView.findViewById(R.id.rv_menu_checkbox);
        mCbSelect = mTitleBarView.findViewById(R.id.cb_select);

        LayoutParams llp =
            new LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
        addView(mTitleBarView, llp);
    }

    public void setActivity(Activity activity) {
        setActivity(activity, null);
    }

    public void setActivity(Activity activity, OnTitleBarBackListener listener) {
        mActivity = activity;
        mBackListener = listener;
        if (mActivity != null) {
            mRvBack.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mBackListener != null) {
                        mBackListener.onBtnBackPressed();
                    } else {
                        mActivity.onBackPressed();
                    }
                }
            });
        }
    }

    /**
     * 设置标题栏背景色
     */
    @Override
    @SuppressLint("ResourceType") public void setBackgroundColor(int resId) {
        mTitleBarView.setBackgroundColor(mResources.getColor(resId));
    }

    /**
     * 设置标题栏背景色(包括状态栏)
     */
    public void setBackgroundColorWithBar(int resId) {
        setWindowStatusBarColor(resId);
        mTitleBarView.setBackgroundColor(mResources.getColor(resId));
    }

    public void setWindowStatusBarColor(int resId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(mActivity, true);
        }
        SystemBarTintManager tintManager = new SystemBarTintManager(mActivity);
        tintManager.setStatusBarTintEnabled(true);
        // 使用颜色资源
        tintManager.setStatusBarTintResource(resId);
    }

    @TargetApi(19) private void setTranslucentStatus(Activity activity, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    /**
     * 设置返回按钮icon
     */
    public void setBackBtnIcon(int res) {
        mRvBack.setVisibility(View.VISIBLE);
        mIvBack.setImageResource(res);
    }

    /**
     * 设置返回按钮背景
     */
    public void setBackBtnBackground(int res) {
        mRvBack.setVisibility(View.VISIBLE);
        mRvBack.setBackgroundResource(res);
    }

    /**
     * 设置居中主标题颜色
     */
    public void setMiddleTitleColor(int color) {
        mTvTitleMiddle.setVisibility(View.VISIBLE);
        mTvTitleMiddle.setTextColor(color);
    }

    /**
     * 设置居中主标题
     */
    public void setMiddleTitle(int resid) {
        mTvTitleMiddle.setVisibility(View.VISIBLE);
        mTvTitleMiddle.setText(resid);
    }

    /**
     * 设置居中主标题
     */
    public void setMiddleTitle(CharSequence text) {
        mTvTitleMiddle.setVisibility(View.VISIBLE);
        mTvTitleMiddle.setText(text);
    }

    /**
     * 设置居左主标题颜色
     */
    public void setLeftTitleColor(int color) {
        mTvTitleLeft.setVisibility(View.VISIBLE);
        mTvTitleLeft.setTextColor(color);
    }

    /**
     * 设置居左主标题
     */
    public void setLeftTitle(int resid) {
        mTvTitleLeft.setVisibility(View.VISIBLE);
        mTvTitleLeft.setText(resid);
    }

    /**
     * 设置居左主标题
     */
    public void setLeftTitle(CharSequence text) {
        mTvTitleLeft.setVisibility(View.VISIBLE);
        mTvTitleLeft.setText(text);
    }

    /**
     * 设置居左主标题点击监听
     */
    public void setLeftTitleOnClickListener(OnClickListener listener) {
        if (listener == null) {
            return;
        }
        mTvTitleLeft.setVisibility(View.VISIBLE);
        mTvTitleLeft.setOnClickListener(listener);
    }

    /**
     * 设置居右主标题颜色
     */
    public void setRightTitleColor(int color) {
        mTvTitleRight.setVisibility(View.VISIBLE);
        mTvTitleRight.setTextColor(color);
    }

    /**
     * 设置居右主标题
     */
    public void setRightTitle(int resid) {
        mTvTitleRight.setVisibility(View.VISIBLE);
        mTvTitleRight.setText(resid);
    }

    /**
     * 设置居右主标题
     */
    public void setRightTitle(CharSequence text) {
        mTvTitleRight.setVisibility(View.VISIBLE);
        mTvTitleRight.setText(text);
    }

    /**
     * 设置居右主标题点击监听
     */
    public void setRightTitleOnClickListener(OnClickListener listener) {
        if (listener == null) {
            return;
        }
        mTvTitleRight.setVisibility(View.VISIBLE);
        mTvTitleRight.setOnClickListener(listener);
    }

    /**
     * 设置菜单一按钮icon
     */
    public void setMenu01Icon(int res) {
        mRvMenu01.setVisibility(View.VISIBLE);
        mIvMenu01.setImageResource(res);
    }

    /**
     * 设置菜单一按钮背景
     */
    public void setMenu01Background(int res) {
        mRvMenu01.setVisibility(View.VISIBLE);
        mRvMenu01.setBackgroundResource(res);
    }

    /**
     * 设置菜单一按钮点击事件
     */
    public void setMenu01OnClickListener(OnClickListener listener) {
        if (listener == null) {
            return;
        }
        mRvMenu01.setVisibility(View.VISIBLE);
        mRvMenu01.setOnClickListener(listener);
    }

    /**
     * 设置菜单二按钮icon不可见
     */
    public void setMenu02IconVisibility(int visibility) {
        mRvMenu02.setVisibility(visibility);
    }

    /**
     * 设置菜单二按钮icon
     */
    public void setMenu02Icon(int res) {
        mRvMenu02.setVisibility(View.VISIBLE);
        mIvMenu02.setImageResource(res);
    }

    /**
     * 获取菜单二按钮
     */
    public RelativeLayout getMenu02() {
        return mRvMenu02;
    }

    /**
     * 获取菜单二按钮图标
     */
    public ImageView getMenu02Icon() {
        return mIvMenu02;
    }

    /**
     * 设置菜单二按钮背景
     */
    public void setMenu02Background(int res) {
        mRvMenu02.setVisibility(View.VISIBLE);
        mRvMenu02.setBackgroundResource(res);
    }

    /**
     * 设置菜单二按钮点击事件
     */
    public void setMenu02OnClickListener(OnClickListener listener) {
        if (listener == null) {
            return;
        }
        mRvMenu02.setVisibility(View.VISIBLE);
        mRvMenu02.setOnClickListener(listener);
    }

    /**
     * 设置菜单二按钮背景
     */
    public void setmRvMenuCheckboxBackground(int res) {
        mRvMenuCheckbox.setVisibility(View.VISIBLE);
        mRvMenuCheckbox.setBackgroundResource(res);
    }

    /**
     * 设置菜单二按钮点击事件
     */
    public void setmRvMenuCheckboxOnClickListener(OnClickListener listener) {
        if (listener == null) {
            return;
        }
        mRvMenuCheckbox.setVisibility(View.VISIBLE);
        mCbSelect.setOnClickListener(listener);
    }

    /**
     * 获取CheckBox的选中状态
     */
    public boolean getCheckBoxIsChecked() {
        return mCbSelect.isChecked();
    }

    /**
     * 设置CheckBox的状态
     */
    public void setCheckBoxChecked(boolean checked) {
        mCbSelect.setChecked(checked);
    }

    public interface OnTitleBarBackListener {
        public void onBtnBackPressed();
    }
}
