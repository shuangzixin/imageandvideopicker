# imageandvideopicker
包含只选择图片、选图后裁剪、只选择视频的功能

```
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
```
