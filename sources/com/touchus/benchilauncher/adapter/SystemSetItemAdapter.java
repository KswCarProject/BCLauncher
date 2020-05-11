package com.touchus.benchilauncher.adapter;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.touchus.benchilauncher.R;
import com.touchus.benchilauncher.bean.SettingInfo;
import java.util.List;

public class SystemSetItemAdapter extends BaseAdapter {
    private Context context;
    private List<SettingInfo> listData;
    private int seclectIndex;

    public SystemSetItemAdapter(Context context2, List<SettingInfo> listData2) {
        this.context = context2;
        this.listData = listData2;
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
            convertView = View.inflate(this.context, R.layout.dialog_appset_item, (ViewGroup) null);
            holder = new ViewHolder();
            holder.appIcon = (ImageView) convertView.findViewById(R.id.itemimg);
            holder.appName = (TextView) convertView.findViewById(R.id.itemtext);
            holder.appICheck = (CheckBox) convertView.findViewById(R.id.itemcheck);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        SettingInfo info = this.listData.get(position);
        holder.appIcon.setVisibility(8);
        holder.appName.setText(info.getItem());
        if (info.isCheckItem()) {
            holder.appICheck.setVisibility(0);
            holder.appICheck.setChecked(info.isChecked());
        } else {
            holder.appICheck.setVisibility(8);
        }
        LinearLayout itemView = (LinearLayout) convertView.findViewById(R.id.itemView);
        if (this.seclectIndex == position) {
            itemView.setBackgroundResource(R.drawable.set_xuanzhong);
        } else {
            itemView.setBackgroundColor(ViewCompat.MEASURED_SIZE_MASK);
        }
        return convertView;
    }

    static class ViewHolder {
        CheckBox appICheck;
        ImageView appIcon;
        TextView appName;

        ViewHolder() {
        }
    }

    public void setSeclectIndex(int seclectIndex2) {
        this.seclectIndex = seclectIndex2;
        notifyDataSetChanged();
    }
}
