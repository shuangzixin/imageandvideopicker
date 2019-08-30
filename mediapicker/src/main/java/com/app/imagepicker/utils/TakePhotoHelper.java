package com.app.imagepicker.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;

import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 类功能描述
 *
 * Created by stefan on 2018/1/22.
 */
public class TakePhotoHelper {

    private final static String TIMESTAMP_FORMAT = "yyyy_MM_dd_HH_mm_ss";
    private static final String JPG_SUFFIX = ".jpg";

    public final static int CAPTURE_PHOTO_REQUEST_CODE = 1111;


    private Fragment fragment;
    private Activity activity;
    private File photoFolder;
    public static File photoFile;

    public TakePhotoHelper(Fragment fragment) {
        this.fragment = fragment;
        this.photoFolder =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
    }


    public TakePhotoHelper(Activity activity) {
        this.activity = activity;
        this.photoFolder =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
    }

    /**
     * start camera and capture photo
     * in OnActivityResult call getPhoto() to get capture
     */
    public void takePhoto(Uri uri) {
        if (uri != null) {
            Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            if (fragment != null) {
                fragment.startActivityForResult(captureIntent, CAPTURE_PHOTO_REQUEST_CODE);
            } else if (activity != null) {
                activity.startActivityForResult(captureIntent, CAPTURE_PHOTO_REQUEST_CODE);
            }
        }
    }

    public void takePhoto() {
        if (hasCamera()) {
            createPhotoFile();
            if (photoFile == null) {
                return;
            }
            Context context;
            if (fragment != null) {
                context = fragment.getContext();
            } else {
                context = activity;
            }
            Uri uri;
            if (Build.VERSION.SDK_INT >= 24) {
                uri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName()
                        + ".provider", this.photoFile);
            } else {
                uri = Uri.fromFile(photoFile);
            }

            // ignore sony
//            if (DeviceCompat.getROM() == DeviceCompat.ROM.SONY) {
//                Intent intent = new Intent();
//                intent.setClass(fragment.getActivity(), CaptureTempActivity.class);
//                intent.putExtra(CaptureTempActivity.CAPTURE_URI, uri);
//                if (fragment != null) {
//                    fragment.startActivityForResult(intent, CAPTURE_PHOTO_REQUEST_CODE);
//                } else if (activity != null) {
//                    activity.startActivityForResult(intent, CAPTURE_PHOTO_REQUEST_CODE);
//                }
//            } else {
//                takePhoto(uri);
//            }

            takePhoto(uri);
        }
    }

    private void createPhotoFile() {
        if (photoFolder != null) {
            if (!photoFolder.exists()) {
                photoFolder.mkdirs();
            }

            String fileName = new SimpleDateFormat(TIMESTAMP_FORMAT).format(new Date());
            photoFile = new File(photoFolder, fileName + JPG_SUFFIX);
            if (photoFile.exists()) {
                photoFile.delete();
            }
            try {
                photoFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                photoFile = null;
            }
        } else {
            photoFile = null;
        }
    }

    /**
     * check if device has camera
     *
     * @return
     */
    public boolean hasCamera() {
        PackageManager packageManager;
        if (fragment != null) {
            packageManager = fragment.getActivity().getPackageManager();
        } else {
            packageManager = activity.getPackageManager();
        }
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        List<ResolveInfo> list =
                packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    public File getPhoto() {
        return photoFile;
    }

    public void setPhoto(String photoFilePath) {
        this.photoFile = new File(photoFilePath);
    }


}
