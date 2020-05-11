package com.touchus.benchilauncher.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;
import com.touchus.benchilauncher.Launcher;
import com.touchus.benchilauncher.R;
import com.touchus.benchilauncher.bean.BeanWifi;
import java.util.ArrayList;

public class AdpWifiAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<BeanWifi> listData;
    public Launcher mLauncher;
    public int rowCount = 5;

    public AdpWifiAdapter(Context context2, ArrayList<BeanWifi> listData2) {
        this.context = context2;
        this.listData = listData2;
        this.rowCount = 5;
    }

    public int getCount() {
        return this.listData.size();
    }

    public Object getItem(int arg0) {
        return this.listData.get(arg0);
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(this.context).inflate(R.layout.item_wifi_listview, (ViewGroup) null);
            holder = new ViewHolder();
            holder.nameTview = (TextView) convertView.findViewById(R.id.nameItem);
            holder.layoutSpace = (Space) convertView.findViewById(R.id.layoutSpace);
            holder.signImg = (ImageView) convertView.findViewById(R.id.signItem);
            holder.selectImg = (ImageView) convertView.findViewById(R.id.selectItem);
            holder.loadingImg = (ImageView) convertView.findViewById(R.id.loadingItem);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        BeanWifi temp = this.listData.get(position);
        if (temp.getLevel() > 0) {
            holder.signImg.setVisibility(0);
            holder.signImg.setImageLevel(temp.getLevel());
        } else {
            holder.signImg.setVisibility(4);
        }
        holder.nameTview.setText(temp.getName());
        holder.layoutSpace.setLayoutParams(new LinearLayout.LayoutParams(1, parent.getHeight() / this.rowCount));
        if (temp.getState() == 1) {
            holder.selectImg.setVisibility(0);
            holder.loadingImg.setVisibility(8);
        } else {
            holder.selectImg.setVisibility(4);
            if (temp.getState() == 2) {
                holder.loadingImg.setVisibility(0);
            } else {
                holder.loadingImg.setVisibility(8);
            }
        }
        return convertView;
    }

    public ArrayList<BeanWifi> getListData() {
        return this.listData;
    }

    static class ViewHolder {
        Space layoutSpace;
        ImageView loadingImg;
        TextView nameTview;
        ImageView selectImg;
        ImageView signImg;

        ViewHolder() {
        }
    }
}
