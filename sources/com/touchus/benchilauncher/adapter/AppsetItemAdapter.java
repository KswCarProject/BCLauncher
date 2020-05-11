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
import com.touchus.benchilauncher.LauncherApplication;
import com.touchus.benchilauncher.R;
import com.touchus.benchilauncher.bean.AppInfo;
import java.util.List;

public class AppsetItemAdapter extends BaseAdapter {
    private LauncherApplication app;
    private Context context;
    private List<AppInfo> listData;
    private int seclectIndex;

    public AppsetItemAdapter(LauncherApplication app2, Context context2, List<AppInfo> listData2) {
        this.app = app2;
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
        holder.appIcon.setImageDrawable(this.listData.get(position).getAppIcon());
        holder.appName.setText(this.listData.get(position).getAppName());
        holder.appICheck.setChecked(this.app.getNaviAPP().equals(this.listData.get(position).getPackageName()));
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
