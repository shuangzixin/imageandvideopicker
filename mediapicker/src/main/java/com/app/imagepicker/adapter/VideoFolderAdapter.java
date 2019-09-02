package com.app.imagepicker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.imagepicker.ImagePicker;
import com.app.imagepicker.R;
import com.app.imagepicker.beans.VideoFolder;
import com.app.imagepicker.utils.LogUtil;

import java.util.List;

/**
 * 类功能描述
 *
 * Created by stefan on 2018/1/22.
 */
public class VideoFolderAdapter extends BaseAdapter {


    private List<VideoFolder> mFolders;
    private Context mContext;
    private int mCoverSize;
    private int mCurrPos;

    public VideoFolderAdapter(Context context, List<VideoFolder> folders, int pos) {
        mContext = context;
        mFolders = folders;
        mCoverSize = context.getResources().getDimensionPixelSize(R.dimen.dp_72);
        mCurrPos = pos;
    }

    public void setCurrPos(int pos) {
        mCurrPos = pos;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mFolders.size();
    }

    @Override
    public VideoFolder getItem(int position) {
        return mFolders.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.layout_video_pick_folder_item, parent, false);
            holder = new ViewHolder(convertView);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        VideoFolder data = getItem(position);

        holder.name.setText(data.name);
        holder.size.setText(String.format("%d%s", data.count, mContext.getResources().getString(R.string.one)));
        LogUtil.i("aaa", "data.cover: " + data.cover);
        ImagePicker.getInstance().getConfig().getImageLoader().loadImage(holder.cover, data.cover, mCoverSize, mCoverSize);

        //try {
        //    MediaMetadataRetriever media = new MediaMetadataRetriever();
        //    media.setDataSource(data.cover);
        //    holder.cover.setImageBitmap(media.getFrameAtTime());
        //}catch (IllegalArgumentException e){
        //    e.printStackTrace();
        //}

        if (mCurrPos == position) {
            holder.indicator.setVisibility(View.VISIBLE);
        } else {
            holder.indicator.setVisibility(View.INVISIBLE);
        }
        return convertView;

    }

    static class ViewHolder {
        ImageView cover;
        TextView name;
        TextView size;
        ImageView indicator;

        ViewHolder(View view) {
            cover = (ImageView) view.findViewById(R.id.cover);
            name = (TextView) view.findViewById(R.id.name);
            size = (TextView) view.findViewById(R.id.size);
            indicator = (ImageView) view.findViewById(R.id.indicator);
            view.setTag(this);
        }

    }

}
