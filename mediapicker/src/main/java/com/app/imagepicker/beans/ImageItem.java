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
public class ImageItem implements Parcelable {

    public String id;
    public String path;
    public String name;
    public long dateAdded;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ImageItem imageItem = (ImageItem) o;

        if (id != null ? !id.equals(imageItem.id) : imageItem.id != null) return false;
        return path != null ? path.equals(imageItem.path) : imageItem.path == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (path != null ? path.hashCode() : 0);
        return result;
    }

    public long dateModified;
    public long dateTaken;

    @Override
    public String toString() {
        return "ImageItem{" +
                "id=" + id +
                ", path='" + path + '\'' +
                ", name='" + name + '\'' +
                ", dateAdded=" + dateAdded +
                ", dateModified=" + dateModified +
                ", dateTaken=" + dateTaken +
                '}';
    }

    public static ImageItem valueOf(Cursor cursor) {
        ImageItem item = new ImageItem();
        item.id = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID));
        item.dateAdded = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_ADDED));
        item.dateTaken = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_TAKEN));
        item.dateModified = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_MODIFIED));
        item.name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME));
        item.path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
        return item;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.path);
        dest.writeString(this.name);
        dest.writeLong(this.dateAdded);
        dest.writeLong(this.dateModified);
        dest.writeLong(this.dateTaken);
    }

    public ImageItem() {
    }

    protected ImageItem(Parcel in) {
        this.id = in.readString();
        this.path = in.readString();
        this.name = in.readString();
        this.dateAdded = in.readLong();
        this.dateModified = in.readLong();
        this.dateTaken = in.readLong();
    }

    public static final Creator<ImageItem> CREATOR = new Creator<ImageItem>() {
        @Override
        public ImageItem createFromParcel(Parcel source) {
            return new ImageItem(source);
        }

        @Override
        public ImageItem[] newArray(int size) {
            return new ImageItem[size];
        }
    };
}
