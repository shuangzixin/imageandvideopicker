package com.app.imagepicker.beans;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.MediaStore;

/**
 * 类功能描述
 *
 * Created by stefan on 2018/1/22.
 */
public class ImageFolder implements Parcelable {

    public String id;//文件夹ID
    public String name;
    public long count;//个数
    public String cover;//封面

    @Override
    public String toString() {
        return "ImageFolder{" +
                "name='" + name + '\'' +
                ", count=" + count +
                ", cover='" + cover + '\'' +
                ", id='" + id + '\'' +
                '}';
    }


    public static ImageFolder valueOf(Cursor cursor) {
        ImageFolder folder = new ImageFolder();
        folder.id = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_ID));
        folder.cover = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
        folder.name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME));
        folder.count = cursor.getLong(3);
        return folder;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeLong(this.count);
        dest.writeString(this.cover);
        dest.writeString(this.id);
    }

    public ImageFolder() {
    }

    protected ImageFolder(Parcel in) {
        this.name = in.readString();
        this.count = in.readInt();
        this.cover = in.readString();
        this.id = in.readString();
    }

    public static final Creator<ImageFolder> CREATOR = new Creator<ImageFolder>() {
        @Override
        public ImageFolder createFromParcel(Parcel source) {
            return new ImageFolder(source);
        }

        @Override
        public ImageFolder[] newArray(int size) {
            return new ImageFolder[size];
        }
    };
}
