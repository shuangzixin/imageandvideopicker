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
public class VideoFolder implements Parcelable {

    public String id;//文件夹ID
    public String name;
    public long count;//个数
    public String cover;//封面

    @Override
    public String toString() {
        return "VideoFolder{" +
                "name='" + name + '\'' +
                ", count=" + count +
                ", cover='" + cover + '\'' +
                ", id='" + id + '\'' +
                '}';
    }


    public static VideoFolder valueOf(Cursor cursor) {
        VideoFolder folder = new VideoFolder();
        folder.id = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_ID));
        folder.cover = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
        folder.name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_DISPLAY_NAME));
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

    public VideoFolder() {
    }

    protected VideoFolder(Parcel in) {
        this.name = in.readString();
        this.count = in.readInt();
        this.cover = in.readString();
        this.id = in.readString();
    }

    public static final Creator<VideoFolder> CREATOR = new Creator<VideoFolder>() {
        @Override
        public VideoFolder createFromParcel(Parcel source) {
            return new VideoFolder(source);
        }

        @Override
        public VideoFolder[] newArray(int size) {
            return new VideoFolder[size];
        }
    };
}
