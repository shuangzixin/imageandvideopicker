package com.app.imagepicker.utils;

import android.content.Context;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;

/**
 * Utils for showing Toast.
 *
 * Created by stefan on 2017/2/16.
 */
public class ToastUtil {
    private static Toast mToast;
    private static int char_length= 20;

    private static class SingletonHolder {
        private static final ToastUtil INSTANCE = new ToastUtil();
    }

    private ToastUtil() {
    }

    public static final ToastUtil getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * Afferent text.
     * @param context
     * @param text
     */
    public void show(Context context , String text){

        if (mToast == null){
            if(text.length() < char_length){
                mToast = Toast.makeText( context, text , Toast.LENGTH_SHORT);
            }else{
                mToast = Toast.makeText( context, text , Toast.LENGTH_LONG);
            }
        }else {
            //If the current Toast does not disappear, then direct display of the content and do not need to reset.
            mToast.setText(text);
        }
        mToast.show();
    }

    /**
     * Afferent resource file.
     * @param context
     * @param resId
     */
    public void show(Context context, @StringRes int resId){
        CharSequence text = context.getResources().getText(resId);
        if (mToast == null){
            if(text.length() < char_length){
                mToast = Toast.makeText( context, text , Toast.LENGTH_SHORT);
            }else{
                mToast = Toast.makeText( context, text , Toast.LENGTH_LONG);
            }
        }else {
            //If the current Toast does not disappear, then direct display of the content and do not need to reset.
            mToast.setText(text);
        }
        mToast.show();
    }

    /**
     * Afferent text and image.
     * @param context
     * @param text
     * @param resImg
     */
    public void showImg(Context context, String text , @DrawableRes int resImg){

        if (mToast == null){
            if(text.length() < char_length){
                mToast = Toast.makeText( context, text , Toast.LENGTH_SHORT);
            }else{
                mToast = Toast.makeText( context, text , Toast.LENGTH_LONG);
            }
        }else {
            //If the current Toast does not disappear, then direct display of the content and do not need to reset.
            mToast.setText(text);
        }
        //Add the image of the operation, there is no set of pictures and text display in one line of operation.
        LinearLayout view = (LinearLayout) mToast.getView();
        ImageView imageView = new ImageView(context);
        imageView.setImageResource(resImg);
        view.addView(imageView);

        mToast.show();
    }
}
