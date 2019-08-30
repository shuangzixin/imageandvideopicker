package com.app.imagepicker.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import com.app.imagepicker.utils.extra.AlbumMediaScannerConnectionClient;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;

import static android.text.TextUtils.concat;
import static java.io.File.separator;
import static java.lang.String.format;
import static java.util.Locale.getDefault;

/**
 * Created by stefan on 2017/8/10.
 */
public class FileUtil {

    /**
     * 缓存文件根目录名
     */
    private static final String FILE_DIR = "mediapicker";
    /**
     * 限制图片最大宽度进行压缩
     */
    private static final int MAX_WIDTH = 720;
    /**
     * 限制图片最大高度进行压缩
     */
    private static final int MAX_HEIGHT = 1280;
    /**
     * 上传最大图片限制
     */
    private static final int MAX_UPLOAD_PHOTO_SIZE = 300 * 1024;
    public static final String DIR_PATH =
        Environment.getExternalStorageDirectory() + File.separator + FILE_DIR;
    private static final String CACHE_PATH = "OkHttpCache";
    private static final String CACHE = ".cache";    //cache directory
    private static final String TMP_SUFFIX = ".tmp";  //temp file
    private static final String LMF_SUFFIX = ".lmf";  //last modify file
    //Dir hint
    private static final String DIR_EXISTS_HINT = "Path [%s] exists.";
    private static final String DIR_NOT_EXISTS_HINT = "Path [%s] not exists, so create.";
    private static final String DIR_CREATE_SUCCESS = "Path [%s] create success.";
    private static final String DIR_CREATE_FAILED = "Path [%s] create failed.";
    private static final String FILE_DELETE_SUCCESS = "File [%s] delete success.";
    private static final String FILE_DELETE_FAILED = "File [%s] delete failed.";

    public static File getCacheDir(Context context) {
        File file = context.getApplicationContext().getCacheDir();

        if (file == null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
                file = context.getExternalCacheDir();
            }
            if (file != null) {
                file = new File(file, CACHE_PATH);
            }
        }

        if (file == null) {
            file = new File(Environment.getExternalStorageDirectory(), CACHE_PATH);
        }
        return file;
    }

    /**
     * mp4文件名
     */
    public static String getUploadVideoFile(Context context) {
        String path = Environment.getExternalStorageDirectory()
            + File.separator
            + FILE_DIR
            + File.separator
            + "video"
            + File.separator;
        return getUploadCachePath(context, path) + FILE_DIR + "_" + String.valueOf(
            System.currentTimeMillis()) + ".mp4";
    }

    /**
     * mp4封面保存路径
     */
    public static String getUploadVideoCoverFile(Context context) {
        String path = Environment.getExternalStorageDirectory()
            + File.separator
            + FILE_DIR
            + File.separator
            + "video"
            + File.separator;
        return getUploadCachePath(context, path) + FILE_DIR + "_cover_" + String.valueOf(
            System.currentTimeMillis()) + ".jpg";
    }

    /**
     * jpg文件名
     */
    public static String getUploadPhotoFile(Context context) {
        String path = Environment.getExternalStorageDirectory()
            + File.separator
            + FILE_DIR
            + File.separator
            + "photo"
            + File.separator;
        return getUploadCachePath(context, path) + FILE_DIR + "_" + String.valueOf(
            System.currentTimeMillis()) + ".jpg";
    }

    public static boolean saveImage(Context context, Bitmap bmp, String imgPath) {
        try {
            byte[] b = ImageUtil.convertBitmapToByte(bmp);
            FileUtil.writeByteToFile(imgPath, b);
            // 更新系统相册
            AlbumMediaScannerConnectionClient albumMediaScannerConnectionClient =
                new AlbumMediaScannerConnectionClient();
            MediaScannerConnection mConnection =
                new MediaScannerConnection(context, albumMediaScannerConnectionClient);
            albumMediaScannerConnectionClient.setImgPath(imgPath);
            albumMediaScannerConnectionClient.setmConnection(mConnection);

            mConnection.connect();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void writeByteToFile(String fullPath, byte[] data) {
        BufferedOutputStream bos = null;
        try {
            File file = new File(fullPath);
            if (!file.exists()) {
                File parent = file.getParentFile();
                if (!parent.exists()) {
                    parent.mkdirs();
                }
                file.createNewFile();
            }
            bos = new BufferedOutputStream(new FileOutputStream(file));

            bos.write(data);
            bos.flush();
        } catch (Exception e) {
            Log.w("aaa", "Write byte to File Exception. " + fullPath, e);
        } finally {
            try {
                if (bos != null) {
                    bos.close();
                }
            } catch (IOException e) {
                Log.w("aaa", e.toString());
            }
        }
    }

    /**
     * 保存拍摄图片
     *
     * @param isFrontFacing 是否为前置拍摄
     */
    public static boolean savePhoto(Context context, String photoPath, byte[] data,
                                    boolean isFrontFacing) {
        if (photoPath != null && data != null) {
            FileOutputStream fos = null;
            try {
                Bitmap preBitmap = compressBitmap(data, MAX_WIDTH, MAX_HEIGHT);
                if (isFrontFacing) {
                    Matrix matrix = new Matrix();
                    matrix.postScale(1, -1);
                    Bitmap newBitmap = Bitmap.createBitmap(preBitmap, 0, 0, preBitmap.getWidth(),
                        preBitmap.getHeight(), matrix, true);
                    preBitmap.recycle();
                    preBitmap = newBitmap;
                }
                byte[] newDatas = compressBitmapToBytes(preBitmap, MAX_UPLOAD_PHOTO_SIZE);
                fos = new FileOutputStream(photoPath);
                fos.write(newDatas);
                LogUtil.i("FileUtils", "compress over ");
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                LogUtil.i("FileUtils", e.toString());
            } finally {
                closeCloseable(fos);
            }
        }
        return false;
    }

    /**
     * 把字节流按照图片方式大小进行压缩
     */
    public static Bitmap compressBitmap(byte[] datas, int w, int h) {
        if (datas != null) {
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inJustDecodeBounds = true;
            BitmapFactory.decodeByteArray(datas, 0, datas.length, opts);
            if (opts.outWidth != 0 && opts.outHeight != 0) {
                LogUtil.i("FileUtils", opts.outWidth + " " + opts.outHeight);
                int scaleX = opts.outWidth / w;
                int scaleY = opts.outHeight / h;
                int scale = 1;
                if (scaleX >= scaleY && scaleX >= 1) {
                    scale = scaleX;
                }
                if (scaleX < scaleY && scaleY >= 1) {
                    scale = scaleY;
                }
                opts.inJustDecodeBounds = false;
                opts.inSampleSize = scale;
                LogUtil.i("FileUtils", "compressBitmap inSampleSize " + datas.length + " " + scale);
                return BitmapFactory.decodeByteArray(datas, 0, datas.length, opts);
            }
        }
        return null;
    }

    /**
     * 质量压缩图片
     */
    public static byte[] compressBitmapToBytes(Bitmap bitmap, int maxSize) {
        if (bitmap == null) return null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, baos);
        byte[] datas = baos.toByteArray();
        int options = 80;
        int longs = datas.length;
        while (longs > maxSize && options > 0) {
            LogUtil.i("FileUtils", "compressBitmapToBytes " + longs + "  " + options);
            baos.reset();
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);
            datas = baos.toByteArray();
            longs = datas.length;
            options -= 20;
        }
        return datas;
    }

    /**
     * 关闭资源
     */
    public static void closeCloseable(Closeable close) {
        if (close != null) {
            try {
                close.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取上传的路径
     */
    public static String getUploadCachePath(Context context, String path) {
        if (isSDCardExist()) {
            File directory = new File(path);
            if (!directory.exists()) directory.mkdirs();
            return path;
        } else {
            File directory = new File(context.getCacheDir(), FILE_DIR);
            if (!directory.exists()) directory.mkdirs();
            return directory.getAbsolutePath();
        }
    }

    /**
     * 把文件插入到系统图库
     */
    public static void saveToMedia(Context context, String photoPath) {
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(), photoPath,
                getFileName(photoPath), null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 最后通知图库更新
        context.sendBroadcast(
            new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(photoPath))));
    }

    /**
     * 根据文件绝对路径获取文件名
     */
    public static String getFileName(String filePath) {
        if (TextUtils.isEmpty(filePath)) return "";
        return filePath.substring(filePath.lastIndexOf(File.separator) + 1);
    }

    /**
     * 删除指定路径的文件
     */
    public static boolean deleteFileWithPath(String filePath) {
        boolean status;
        SecurityManager checker = new SecurityManager();

        if (!TextUtils.isEmpty(filePath)) {
            File newPath = new File(filePath);
            checker.checkDelete(newPath.toString());
            if (newPath.isFile()) {
                try {
                    newPath.delete();
                    status = true;
                } catch (SecurityException se) {
                    se.printStackTrace();
                    status = false;
                }
            } else {
                status = false;
            }
        } else {
            status = false;
        }
        return status;
    }

    /**
     * SD卡是否存在
     */
    public static boolean isSDCardExist() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * return files
     *
     * @param saveName saveName
     * @param savePath savePath
     * @return file, tempFile, lmfFile
     */
    public static File[] getFiles(String saveName, String savePath) {
        String[] paths = getPaths(saveName, savePath);
        return new File[] { new File(paths[0]), new File(paths[1]), new File(paths[2]) };
    }

    /**
     * return file paths
     *
     * @param saveName saveName
     * @param savePath savePath
     * @return filePath, tempPath, lmfPath
     */
    public static String[] getPaths(String saveName, String savePath) {
        String cachePath = concat(savePath, separator, CACHE).toString();
        String filePath = concat(savePath, separator, saveName).toString();
        String tempPath = concat(cachePath, separator, saveName, TMP_SUFFIX).toString();
        String lmfPath = concat(cachePath, separator, saveName, LMF_SUFFIX).toString();
        return new String[] { filePath, tempPath, lmfPath };
    }

    /**
     * delete files
     *
     * @param files files
     */
    public static void deleteFiles(File... files) {
        for (File each : files) {
            if (each.exists()) {
                boolean flag = each.delete();
                if (flag) {
                    LogUtil.log(format(getDefault(), FILE_DELETE_SUCCESS, each.getName()));
                } else {
                    LogUtil.log(format(getDefault(), FILE_DELETE_FAILED, each.getName()));
                }
            }
        }
    }

    /**
     * create dirs with params path
     *
     * @param paths paths
     */
    public static void mkdirs(String... paths) {
        for (String each : paths) {
            File file = new File(each);
            if (file.exists() && file.isDirectory()) {
                LogUtil.log(DIR_EXISTS_HINT, each);
            } else {
                LogUtil.log(DIR_NOT_EXISTS_HINT, each);
                boolean flag = file.mkdirs();
                if (flag) {
                    LogUtil.log(DIR_CREATE_SUCCESS, each);
                } else {
                    LogUtil.log(DIR_CREATE_FAILED, each);
                }
            }
        }
    }

    public static void closeSilently(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @param dir
     * @return
     */
    public static boolean deleteDirectory(File dir) {
        if (dir.exists()) {
            File[] files = dir.listFiles();
            if (files != null) {
                for (File file : files) {
                    deleteDirectory(file);
                }
            }
        }
        return deleteFile(dir);
    }

    /**
     * @param file
     * @return
     */
    public static boolean deleteFile(File file) {
        if (file.exists()) {
            return file.delete();
        }
        return true;
    }

    /**
     * @param filePath
     * @return
     */
    public static boolean deleteFile(String filePath) {
        File file = new File(filePath);
        return deleteFile(file);
    }

    /**
     * @param filePath
     * @return
     */
    public static boolean exist(String filePath) {
        File file = new File(filePath);
        return file.exists();
    }

    /**
     * get root directory
     */
    public static File getStoreDir(Context applicationContext) {
        File dataDir = null;
        if (Environment.MEDIA_MOUNTED.equalsIgnoreCase(Environment.getExternalStorageState())) {
            dataDir = Environment.getExternalStorageDirectory();
        } else {
            dataDir = applicationContext.getApplicationContext().getFilesDir();
        }
        return dataDir;
    }

    public static String getInnerCacheDir(Context context) {
        String cachePath = "";
        //if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
        //    || !Environment.isExternalStorageRemovable()) {
        //    if (context.getExternalCacheDir() != null) {
        //        cachePath = context.getExternalCacheDir().getPath();
        //    }
        //} else {
        //    cachePath = context.getCacheDir().getPath();
        //}
        //
        //if (TextUtils.isEmpty(cachePath)) {
        //    cachePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/cache";
        //}
        cachePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/cache";

        return cachePath;
    }

    /**
     * 获取指定文件大小
     */
    public static long getFileSize(File file) {
        long size = 0;
        try {
            if (file.exists()) {
                FileInputStream fis = null;
                fis = new FileInputStream(file);
                size = fis.available();
            } else {

                Log.e("获取文件大小", "文件不存在!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    /**
     * 获取指定文件大小
     */
    public static long getFileSize(String filePath) {
        long size = 0;
        try {
            File file = new File(filePath);
            if (file.exists()) {
                FileInputStream fis = null;
                fis = new FileInputStream(file);
                size = fis.available();
            } else {
                Log.e("获取文件大小", "文件不存在!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    /**
     * 获取指定文件夹
     */
    public static long getFileSizes(File f) {
        long size = 0;
        File flist[] = f.listFiles();
        for (int i = 0; i < flist.length; i++) {
            if (flist[i].isDirectory()) {
                size = size + getFileSizes(flist[i]);
            } else {
                size = size + getFileSize(flist[i]);
            }
        }
        return size;
    }

    /**
     * 获取指定文件夹
     */
    public static long getFileSizes(String filePath) {
        long size = 0;
        File f = new File(filePath);
        File flist[] = f.listFiles();
        for (int i = 0; i < flist.length; i++) {
            if (flist[i].isDirectory()) {
                size = size + getFileSizes(flist[i]);
            } else {
                size = size + getFileSize(flist[i]);
            }
        }
        return size;
    }

    /**
     * 转换文件大小
     */
    public static String toFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        String wrongSize = "0B";
        if (fileS == 0) {
            return wrongSize;
        }
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "KB";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "GB";
        }
        return fileSizeString;
    }
}
