package com.touchus.benchilauncher.adapter;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.touchus.benchilauncher.R;
import java.util.List;

public class DialogItemAdapter extends BaseAdapter {
    private Context context;
    private List<String> listData;
    private int seclectIndex;

    public DialogItemAdapter(Context context2, List<String> listData2) {
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
            convertView = View.inflate(this.context, R.layout.dialog_item, (ViewGroup) null);
            holder = new ViewHolder();
            holder.nameTview = (TextView) convertView.findViewById(R.id.itemtext);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.nameTview.setText(this.listData.get(position));
        LinearLayout itemView = (LinearLayout) convertView.findViewById(R.id.itemView);
        Log.e("systemtime", " seclectIndex == position   " + (this.seclectIndex == position));
        if (this.seclectIndex == position) {
            itemView.setBackgroundResource(R.drawable.set_xuanzhong);
            Log.e("systemtime", " getView   " + System.currentTimeMillis());
        } else {
            itemView.setBackgroundColor(ViewCompat.MEASURED_SIZE_MASK);
        }
        return convertView;
    }

    static class ViewHolder {
        TextView nameTview;

        ViewHolder() {
        }
    }

    public void setSeclectIndex(int seclectIndex2) {
        this.seclectIndex = seclectIndex2;
    }
}
