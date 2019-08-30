/*
 *
 *  * Copyright (C) 2017 CIIC Guanaitong, Co.,Ltd.
 *  * All rights reserved.
 *
 */

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
public class VideoItem implements Parcelable {

    public String id;
    public String path;
    public String name;
    public long dateAdded;
    public long duration;
    public long size;

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        VideoItem imageItem = (VideoItem) o;

        if (id != null ? !id.equals(imageItem.id) : imageItem.id != null) return false;
        return path != null ? path.equals(imageItem.path) : imageItem.path == null;
    }

    @Override public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (path != null ? path.hashCode() : 0);
        return result;
    }

    public long dateModified;
    public long dateTaken;

    @Override public String toString() {
        return "VideoItem{"
            + "id="
            + id
            + ", path='"
            + path
            + '\''
            + ", name='"
            + name
            + '\''
            + ", dateAdded="
            + dateAdded
            + ", duration="
            + duration
            + ", size="
            + size
            + ", dateModified="
            + dateModified
            + ", dateTaken="
            + dateTaken
            + '}';
    }

    public static VideoItem valueOf(Cursor cursor) {
        VideoItem item = new VideoItem();
        item.id = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID));
        item.duration =
            cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));
        item.size =
            cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE));
        item.dateAdded =
            cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_ADDED));
        item.dateTaken =
            cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_TAKEN));
        item.dateModified =
            cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_MODIFIED));
        item.name =
            cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME));
        item.path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
        return item;
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.path);
        dest.writeString(this.name);
        dest.writeLong(this.duration);
        dest.writeLong(this.size);
        dest.writeLong(this.dateAdded);
        dest.writeLong(this.dateModified);
        dest.writeLong(this.dateTaken);
    }

    public VideoItem() {
    }

    protected VideoItem(Parcel in) {
        this.id = in.readString();
        this.path = in.readString();
        this.name = in.readString();
        this.duration = in.readLong();
        this.size = in.readLong();
        this.dateAdded = in.readLong();
        this.dateModified = in.readLong();
        this.dateTaken = in.readLong();
    }

    public static final Creator<VideoItem> CREATOR = new Creator<VideoItem>() {
        @Override public VideoItem createFromParcel(Parcel source) {
            return new VideoItem(source);
        }

        @Override public VideoItem[] newArray(int size) {
            return new VideoItem[size];
        }
    };
}
