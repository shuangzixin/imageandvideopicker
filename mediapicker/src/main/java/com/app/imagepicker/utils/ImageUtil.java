package com.app.imagepicker.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 类功能描述
 *
 * Created by stefan on 2018/1/22.
 */
public class ImageUtil {

    // Constants used for the Orientation Exif tag.
    public static final int ORIENTATION_UNDEFINED = 0;
    public static final int ORIENTATION_NORMAL = 1;
    public static final int ORIENTATION_FLIP_HORIZONTAL = 2; // left right reversed mirror
    public static final int ORIENTATION_ROTATE_180 = 3;
    public static final int ORIENTATION_FLIP_VERTICAL = 4; // upside down mirror
    public static final int ORIENTATION_TRANSPOSE = 5; // flipped about top-left <--> bottom-right
    // axis
    public static final int ORIENTATION_ROTATE_90 = 6; // rotate 90 cw to right it
    public static final int ORIENTATION_TRANSVERSE = 7; // flipped about top-right <--> bottom-left
    // axis
    public static final int ORIENTATION_ROTATE_270 = 8; // rotate 270 to right it
    private static final int[] PHOTO_SIZE_ARRAY = new int[]{1280, 896, 512};
    private static final int PHOTO_SIZE_BIG = 0;
    private static final int PHOTO_SIZE_MIDDLE = 1;
    private static final int PHOTO_SIZE_SMALL = 2;

    public static Point computeCompressedSize(String path, double maxPixels) {
        final PointF originSize = getBmpSize(path);
        if (originSize.x == 0f && originSize.y == 0f) {
            return new Point(0, 0);
        } else {
            double scaleFactor = Math.sqrt(originSize.x * 1d * originSize.y / maxPixels);
            if (scaleFactor < 1d) {
                scaleFactor = 1d;
            }
            return new Point((int) (originSize.x / scaleFactor),
                (int) (originSize.y / scaleFactor));
        }
    }

    public static PointF getBmpSize(String path) {
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, bmOptions);

        try {
            ExifInterface exifInterface = new ExifInterface(path);
            if (bmOptions.outHeight == -1 || bmOptions.outWidth == -1) {
                bmOptions.outHeight = exifInterface.getAttributeInt(ExifInterface.TAG_IMAGE_LENGTH,
                    ExifInterface.ORIENTATION_NORMAL);
                bmOptions.outWidth = exifInterface.getAttributeInt(ExifInterface.TAG_IMAGE_WIDTH,
                    ExifInterface.ORIENTATION_NORMAL);
            }

            int orientationAttr = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL);
            if (orientationAttr == ORIENTATION_ROTATE_90
                || orientationAttr == ORIENTATION_ROTATE_270) {
                return new PointF(Math.max(0F, bmOptions.outHeight),
                    Math.max(0F, bmOptions.outWidth));
            } else {
                return new PointF(Math.max(0F, bmOptions.outWidth),
                    Math.max(0F, bmOptions.outHeight));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new PointF(0F, 0F);
    }

    public static byte[] convertBitmapToByte(Bitmap bitmap) {
        byte[] array = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int qualityLevel = 80;
        Bitmap.CompressFormat format = Bitmap.CompressFormat.JPEG;
        boolean result = bitmap.compress(format, qualityLevel,
            baos);
        if (result) {
            array = baos.toByteArray();
        } else {
            int bitmapWidth = bitmap.getWidth();
            int bitmapHeight = bitmap.getHeight();
            int[] temp = new int[bitmapWidth * bitmapHeight];
            bitmap.getPixels(temp, 0, bitmap.getWidth(), 0, 0,
                bitmap.getWidth(), bitmap.getHeight());
            System.arraycopy(temp, 0, array, 0, temp.length);
        }
        return array;
    }

    /**
     * 旋转图片
     *
     * @param degree
     * @param bitmap
     * @return Bitmap
     */
    public static Bitmap rotaingImgView(int degree, Bitmap bitmap) {
        // 旋转图片 动作
        Matrix matrix = new Matrix();
        ;
        matrix.postRotate(degree);
        // 创建新的图片
        if (bitmap != null) {
            Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            return resizedBitmap;
        } else {
            return null;
        }
    }

    public static Bitmap loadImageForPath(Context context, String filePath) {
        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        try {
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(filePath), null,
                options);
            if (options.outWidth > 0 && options.outHeight > 0) {
                options.inSampleSize = computeSampleSize(options);
                options.inPreferredConfig = Bitmap.Config.RGB_565;
                options.inJustDecodeBounds = false;
                bitmap = BitmapFactory.decodeStream(new FileInputStream(
                    filePath), null, options);
            }
        } catch (OutOfMemoryError oom) {
            Log.w("aaa", "loadImage out of memory", oom);
        } catch (FileNotFoundException e) {
            Log.w("aaa", "FileNotFoundException", e);
            e.printStackTrace();
        }
        return bitmap;
    }

    public static int computeSampleSize(BitmapFactory.Options options) {
        // int imageLevel = GlobalSetting.getInstance().getImageLevel();
        int sizeLevel = PHOTO_SIZE_BIG;
        // switch (imageLevel) {
        // case Constants.IMAGE_HIGH_LEVEL:
        // sizeLevel = PHOTO_SIZE_BIG;
        // break;
        // case Constants.IMAGE_NORMAL_LEVEL:
        // sizeLevel = PHOTO_SIZE_MIDDLE;
        // break;
        // case Constants.IMAGE_LOW_LEVEL:
        // sizeLevel = PHOTO_SIZE_SMALL;
        // break;
        // default:
        // break;
        // }
        int width = PHOTO_SIZE_ARRAY[sizeLevel];

        int initialSize = computeInitialSampleSize(options.outWidth,
            options.outHeight, -1, width * width);
        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }
        Log.d("aaa", "=============> max width : " + width + "  SampleSize"
            + roundedSize);
        return roundedSize;
    }

    private static int computeInitialSampleSize(double outWidth,
        double outHeight, int minSideLength, int maxNumOfPixels) {
        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
            .sqrt(outWidth * outHeight / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
            Math.floor(outWidth / minSideLength),
            Math.floor(outHeight / minSideLength));
        if (upperBound < lowerBound) {
            return lowerBound;
        }
        if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }

    public static Bitmap loadBitmap(String path, int inSampleSize) {
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = inSampleSize;
        String exifPath = path;
        Matrix matrix = null;
        if (exifPath != null) {
            ExifInterface exif;
            try {
                exif = new ExifInterface(exifPath);
                int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
                matrix = new Matrix();
                switch (orientation) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        matrix.postRotate(90);
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        matrix.postRotate(180);
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        matrix.postRotate(270);
                        break;
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        Bitmap b = null;
        if (path != null) {
            try {
                b = BitmapFactory.decodeFile(path, bmOptions);
                if (b != null) {
                    Bitmap newBitmap =
                        createBitmap(b, 0, 0, b.getWidth(), b.getHeight(), matrix, true);
                    if (newBitmap != b) {
                        b.recycle();
                        b = newBitmap;
                    }
                }
            } catch (OutOfMemoryError e) {
                try {
                    bmOptions.inSampleSize *= 2;
                    b = BitmapFactory.decodeFile(path, bmOptions);
                    if (b != null) {
                        Bitmap newBitmap =
                            createBitmap(b, 0, 0, b.getWidth(), b.getHeight(), matrix, true);
                        if (newBitmap != b) {
                            b.recycle();
                            b = newBitmap;
                        }
                    }
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        return b;
    }

    public static Bitmap loadBitmap(String path, float expectedWidth, float expectedHeight,
                                    int rollbackSize) {
        final PointF originSize = getBmpSize(path);
        final float scaleFactor =
            expectedWidth == 0 ? Math.max(originSize.x / rollbackSize, originSize.y / rollbackSize)
                : originSize.x / expectedWidth;
        int inSampleSize = scaleFactor < 1 ? 1 : (int) scaleFactor;
        return loadBitmap(path, inSampleSize);
    }

    private static void checkXYSign(int x, int y) {
        if (x < 0) {
            throw new IllegalArgumentException("x must be >= 0");
        }
        if (y < 0) {
            throw new IllegalArgumentException("y must be >= 0");
        }
    }

    private static void checkWidthHeight(int width, int height) {
        if (width <= 0) {
            throw new IllegalArgumentException("width must be > 0");
        }
        if (height <= 0) {
            throw new IllegalArgumentException("height must be > 0");
        }
    }

    public static Bitmap createBitmap(Bitmap source, int x, int y, int width, int height, Matrix m,
                                      boolean filter) {
        checkXYSign(x, y);
        checkWidthHeight(width, height);
        if (x + width > source.getWidth()) {
            throw new IllegalArgumentException("x + width must be <= bitmap.width()");
        }
        if (y + height > source.getHeight()) {
            throw new IllegalArgumentException("y + height must be <= bitmap.height()");
        }
        if (!source.isMutable()
            && x == 0
            && y == 0
            && width == source.getWidth()
            && height == source.getHeight()
            && (m == null || m.isIdentity())) {
            return source;
        }

        int neww = width;
        int newh = height;
        Canvas canvas = new Canvas();
        Bitmap bitmap;
        Paint paint;

        Rect srcR = new Rect(x, y, x + width, y + height);
        RectF dstR = new RectF(0, 0, width, height);

        Bitmap.Config newConfig = Bitmap.Config.ARGB_8888;
        final Bitmap.Config config = source.getConfig();
        if (config != null) {
            switch (config) {
                case RGB_565:
                    newConfig = Bitmap.Config.RGB_565;
                    break;
                case ALPHA_8:
                    newConfig = Bitmap.Config.ALPHA_8;
                    break;
                case ARGB_4444:
                case ARGB_8888:
                default:
                    newConfig = Bitmap.Config.ARGB_8888;
                    break;
            }
        }

        if (m == null || m.isIdentity()) {
            bitmap = createBitmap(neww, newh, newConfig);
            paint = null;
        } else {
            final boolean transformed = !m.rectStaysRect();
            RectF deviceR = new RectF();
            m.mapRect(deviceR, dstR);
            neww = Math.round(deviceR.width());
            newh = Math.round(deviceR.height());
            bitmap = createBitmap(neww, newh, transformed ? Bitmap.Config.ARGB_8888 : newConfig);
            canvas.translate(-deviceR.left, -deviceR.top);
            canvas.concat(m);
            paint = new Paint();
            paint.setFilterBitmap(filter);
            if (transformed) {
                paint.setAntiAlias(true);
            }
        }
        bitmap.setDensity(source.getDensity());
        bitmap.setHasAlpha(source.hasAlpha());
        if (Build.VERSION.SDK_INT >= 19) {
            bitmap.setPremultiplied(source.isPremultiplied());
        }
        canvas.setBitmap(bitmap);
        canvas.drawBitmap(source, srcR, dstR, paint);
        try {
            canvas.setBitmap(null);
        } catch (Exception e) {
            // don't promt, this will crash on 2.x
        }
        return bitmap;
    }

    public static Bitmap createBitmap(int width, int height, Bitmap.Config config) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, config);
        if (config == Bitmap.Config.ARGB_8888 || config == Bitmap.Config.ARGB_4444) {
            bitmap.eraseColor(Color.TRANSPARENT);
        }
        return bitmap;
    }

    public static String scaleAndSaveImage(String path, String cacheFilePath, Bitmap bitmap,
                                           int quality) {
        if (bitmap == null) {
            return null;
        }
        float photoW = bitmap.getWidth();
        float photoH = bitmap.getHeight();
        if (photoW == 0 || photoH == 0) {
            return null;
        }
        String fileName = getImageCacheFileName(path);
        scaleAndSaveImageInternal(cacheFilePath, bitmap, quality);
        return fileName;
    }

    private static void scaleAndSaveImageInternal(String filePath, Bitmap bitmap, int quality) {
        Bitmap scaledBitmap = bitmap;
        File file = new File(filePath);
        try {
            file.createNewFile();
            FileOutputStream stream = null;
            stream = new FileOutputStream(filePath);
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, quality, stream);
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (scaledBitmap != bitmap) {
            scaledBitmap.recycle();
        }
    }

    public static String getImageCacheFileName(String filePath) {
        if (!TextUtils.isEmpty(filePath)) {
            File file = new File(filePath);
            if (file.exists()) {
                return filePath.hashCode() + "_" + file.lastModified() + ".jpg";
            }
        }
        return filePath;
    }

    public static Bitmap decodeBitmap(Context context, Uri uri, int sampleSize) {
        InputStream is = null;
        try {
            is = context.getContentResolver().openInputStream(uri);
            BitmapFactory.Options option = new BitmapFactory.Options();
            option.inSampleSize = sampleSize;
            return BitmapFactory.decodeStream(is, null, option);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        } finally {
            FileUtil.closeSilently(is);
        }
        return null;
    }

    public static boolean saveBitmap(Bitmap bitmap, String path, Bitmap.CompressFormat format,
                                     int quality) {
        File f = new File(path);
        //if (f.exists()) {
        //    f.delete();
        //}

        FileOutputStream fOut = null;
        try {
            if (!f.exists()) {
                f.createNewFile();
            }
            //f.createNewFile();
            fOut = new FileOutputStream(f);
            bitmap.compress(format, quality, fOut);
            fOut.flush();
            return true;
        } catch (IOException e1) {
            e1.printStackTrace();
            return false;
        } finally {
            FileUtil.closeSilently(fOut);
        }
    }

    public static Bitmap drawableToBitmap(Drawable drawable, int height, int width) {
        Bitmap bitmap;
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(height, width, Bitmap.Config.ARGB_8888);
        } else {
            bitmap =
                Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(),
                    Bitmap.Config.ARGB_8888);
        }
        Canvas canvas = new Canvas(bitmap);
        canvas.drawARGB(0, 0, 0, 0);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    public static Bitmap getCircleBitmap(Bitmap bm, int size) {
        Bitmap bitmap = ThumbnailUtils.extractThumbnail(bm, size, size);
        Bitmap output =
            Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xffffffff;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setFilterBitmap(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawOval(rectF, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        bm.recycle();
        return output;
    }

    public static int adjustAlpha(int color, float factor) {
        int alpha = Math.round(Color.alpha(color) * factor);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return Color.argb(alpha, red, green, blue);
    }

    /**
     * 图片去色,返回灰度图片
     * @param bmpOriginal 传入的图片
     * @param sat 饱和度 0灰色 100过度彩色，50正常
     * @return 去色后的图片
     */
    public static Bitmap toGrayScale(Bitmap bmpOriginal, int sat) {
        int width, height;
        height = bmpOriginal.getHeight();
        width = bmpOriginal.getWidth();

        Bitmap bmpGrayScale = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        Canvas c = new Canvas(bmpGrayScale);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(bmpOriginal, 0, 0, paint);
        return bmpGrayScale;
    }

    /**
     * 图片去色,返回灰度图片
     * @param dwOriginal 传入的图片
     * @param sat 饱和度 0灰色 100过度彩色，50正常
     * @return 去色后的图片
     */
    public static Bitmap toGrayScale(Drawable dwOriginal, int sat) {
        int w = dwOriginal.getIntrinsicWidth();
        int h = dwOriginal.getIntrinsicHeight();

        // 取 drawable 的颜色格式
        Bitmap.Config config = dwOriginal.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
            : Bitmap.Config.RGB_565;
        // 建立对应 bitmap
        Bitmap bmpGrayScale = Bitmap.createBitmap(w, h, config);
        return toGrayScale(bmpGrayScale, sat);
    }
}
