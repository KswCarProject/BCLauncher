package com.touchus.benchilauncher.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import com.touchus.benchilauncher.R;
import com.touchus.publicutils.bean.MediaBean;
import com.touchus.publicutils.utils.LoadLocalImageUtil;
import java.io.File;
import java.util.List;

public class MediaAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater layoutInflater;
    private List<MediaBean> mediaBeans;
    private int seclectIndex;
    private int type = 0;

    public MediaAdapter(Context context2, List<MediaBean> mediaBeans2) {
        this.mediaBeans = mediaBeans2;
        this.context = context2;
        this.layoutInflater = (LayoutInflater) context2.getSystemService("layout_inflater");
    }

    public int getCount() {
        return this.mediaBeans.size();
    }

    public Object getItem(int position) {
        return this.mediaBeans.get(position);
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = this.layoutInflater.inflate(R.layout.media_list_item, (ViewGroup) null);
        }
        MediaBean mediaBean = this.mediaBeans.get(position);
        final ImageView imageView = (ImageView) convertView.findViewById(R.id.itemImage);
        TextView textView = (TextView) convertView.findViewById(R.id.itemContent);
        if (this.type == 0) {
            imageView.setVisibility(8);
        } else if (this.type == 1) {
            imageView.setVisibility(0);
            imageView.setTag(mediaBean.getData());
            if (mediaBean.getBitmap() == null) {
                LoadLocalImageUtil.getInstance().loadDrawable(mediaBean, LoadLocalImageUtil.VIDEO_TYPE, this.context, mediaBean.getData(), new LoadLocalImageUtil.ImageCallback() {
                    public void imageLoaded(Bitmap bitmip, String tag) {
                        if (imageView.getTag().equals(tag)) {
                            imageView.setImageBitmap(bitmip);
                        }
                    }
                });
            } else {
                imageView.setImageBitmap(mediaBean.getBitmap());
            }
        } else {
            imageView.setVisibility(0);
            Picasso.with(this.context).load(new File(mediaBean.getData())).into(imageView);
        }
        textView.setText(mediaBean.getTitle());
        RelativeLayout itemView = (RelativeLayout) convertView.findViewById(R.id.itemView);
        if (this.seclectIndex == position) {
            itemView.setBackgroundResource(R.drawable.list_xuanzhong);
        } else {
            itemView.setBackgroundColor(ViewCompat.MEASURED_SIZE_MASK);
        }
        return convertView;
    }

    public void setSeclectIndex(int seclectIndex2) {
        this.seclectIndex = seclectIndex2;
    }

    public void notifyDataSetChanged(int type2) {
        this.type = type2;
        notifyDataSetChanged();
    }
}
