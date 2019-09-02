package com.app.imagepicker.base.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;

import com.app.imagepicker.R;
import com.app.imagepicker.base.anotations.Injection;
import com.app.imagepicker.base.view.CustomTitleBar;
import com.app.imagepicker.base.view.IBaseView;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import butterknife.ButterKnife;

/**
 * Created by stefan on 2017/5/3.
 */
public abstract class BaseActivity extends RxAppCompatActivity
    implements IBaseView, View.OnClickListener {

    private boolean mIsActive;

    protected View mContentView;
    protected View mLine;
    protected CustomTitleBar mTitleBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Injection.bindInject(this, this);

        setContentView(R.layout.activity_base);

        mTitleBar = findViewById(R.id.widget_custom_titlebar);
        mLine = findViewById(R.id.line);
        ViewStub stub = findViewById(R.id.container);
        stub.setLayoutResource(getLayoutResourceId());
        mContentView = stub.inflate();

        mIsActive = true;

        if (getIntent() != null) {
            handleIntent(getIntent());
        }

        ButterKnife.bind(this);
        initView();
        initData();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    /**
     * handle data from intent passed
     *
     * @param intent the intent that pass here from last activity
     */
    protected void handleIntent(Intent intent) {

    }

    protected void initView() {

    }

    protected void initData() {

    }

    @LayoutRes
    protected abstract int getLayoutResourceId();

    /**
     * Response click event.
     *
     * @param v The control which is clicked.
     */
    @Override
    public void onClick(View v) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mIsActive = false;
    }

    @Override
    public boolean isActive() {
        return mIsActive;
    }

    @Override
    public Context getContext() {
        return this;
    }

    protected boolean hideSoftInput() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (getCurrentFocus() != null) {
            if (getCurrentFocus().getWindowToken() != null) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                return true;
            }
        }
        return false;
    }

    protected boolean showSoftInputForced() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (getCurrentFocus() != null) {
            imm.showSoftInput(getCurrentFocus(), InputMethodManager.SHOW_FORCED);
            return true;
        }
        return false;
    }

    protected boolean showSoftInput() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (getCurrentFocus() != null) {
            imm.showSoftInput(getCurrentFocus(), 0);
            return true;
        }
        return false;
    }

    protected void toggleSoftInput() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

}
