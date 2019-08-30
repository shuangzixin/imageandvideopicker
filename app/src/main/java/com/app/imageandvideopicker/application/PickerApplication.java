package com.app.imageandvideopicker.application;

import android.app.Application;
import android.widget.ImageView;

import androidx.core.content.ContextCompat;

import com.app.imageandvideopicker.R;
import com.app.imageloader.GlideImageLoader;
import com.app.imagepicker.ImageLoader;
import com.app.imagepicker.ImagePicker;
import com.app.imagepicker.ImagePickerConfiguration;
import com.bumptech.glide.request.RequestOptions;

/**
 * Application类
 *
 * Created by stefan on 2017/8/29.
 */
public class PickerApplication extends Application {

    private static PickerApplication sAppContext;

    public static PickerApplication getInstance() {
        return sAppContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sAppContext = this;

        //初始化图片选择框架
        initImagePicker();
    }

    private void initImagePicker() {

        ImagePicker.getInstance()
            .init(new ImagePickerConfiguration.Builder().setAppContext(this)
                .setImageLoader(new ImageLoader() {
                    @Override
                    public void loadImage(ImageView imageView, String uri, int width, int height) {
                        GlideImageLoader imageLoader = GlideImageLoader.create(imageView);
                        RequestOptions
                            requestOptions = imageLoader.requestOptions(R.color.image_bg, R.color.image_bg);
                        requestOptions.centerCrop();
                        requestOptions.override(width, height);
                        imageLoader.load(uri, requestOptions);
                    }

                    @Override
                    public void loadImage(ImageView imageView, String uri) {
                        GlideImageLoader imageLoader = GlideImageLoader.create(imageView);
                        RequestOptions
                            requestOptions = imageLoader.requestOptions(R.color.colorPrimary, R.color.colorAccent);
                        requestOptions.centerCrop();
                        imageLoader.load(uri, requestOptions);
                    }
                })
                .setToolbaseColor(ContextCompat.getColor(this, R.color.colorPrimary))
                .build());
    }
}
