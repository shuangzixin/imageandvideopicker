package com.app.imagepicker;

import android.content.Context;

/**
 * 类功能描述
 *
 * Created by stefan on 2018/1/22.
 */
public class ImagePickerConfiguration {

    private ImageLoader imageLoader;
    private Context appContext;
    private int toolbarColor;

    private ImagePickerConfiguration(Builder builder) {
        this.imageLoader = builder.imageLoader;
        this.appContext = builder.context;
        this.toolbarColor = builder.toolbarColor;
    }

    public ImageLoader getImageLoader() {
        return imageLoader;
    }

    public Context getAppContext() {
        return appContext;
    }

    public int getToolbarColor() {
        return toolbarColor;
    }

    public static class Builder {

        private ImageLoader imageLoader;
        private Context context;
        private int toolbarColor;

        public Builder() {
        }



        public Builder setAppContext(Context context) {
            this.context = context;
            return this;
        }

        public Builder setImageLoader(ImageLoader imageLoader) {
            this.imageLoader = imageLoader;
            return this;
        }

        public Builder setToolbaseColor(int color) {
            this.toolbarColor = color;
            return this;
        }

        public ImagePickerConfiguration build() {
            return new ImagePickerConfiguration(this);
        }
    }
}
