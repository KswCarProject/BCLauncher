package com.touchus.benchilauncher.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.touchus.benchilauncher.R;
import com.touchus.publicutils.bean.Person;
import java.util.ArrayList;
import java.util.List;

public class CallRecordAdapter extends BaseAdapter {
    private List<Person> callRecords;
    private Context context;
    private int selectPosition;
    private int type;

    public CallRecordAdapter(Context context2) {
        this(context2, 0);
    }

    public CallRecordAdapter(Context context2, int type2) {
        this.selectPosition = 0;
        this.type = 0;
        this.callRecords = new ArrayList();
        this.context = context2;
        this.type = type2;
    }

    public void setData(List<Person> callRecords2) {
        if (this.callRecords.size() > 0) {
            this.callRecords.clear();
        }
        this.callRecords.addAll(callRecords2);
    }

    public int getCount() {
        return this.callRecords.size();
    }

    public Object getItem(int position) {
        return Integer.valueOf(position);
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(this.context, R.layout.item_callphone_record, (ViewGroup) null);
            viewHolder = new ViewHolder(this, (ViewHolder) null);
            viewHolder.img_phone_state = (ImageView) convertView.findViewById(R.id.img_callphone_state);
            viewHolder.tv_yunyinshang = (TextView) convertView.findViewById(R.id.tv_call_yunyinshang);
            viewHolder.tv_phone_number = (TextView) convertView.findViewById(R.id.tv_phone_number);
            viewHolder.tv_call_time = (TextView) convertView.findViewById(R.id.tv_callphone_time);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (position == this.selectPosition) {
            convertView.setBackgroundResource(R.drawable.list_xuanzhong);
        } else {
            convertView.setBackgroundColor(0);
        }
        initData(viewHolder, this.callRecords.get(position));
        return convertView;
    }

    private void initData(ViewHolder viewHolder, Person callRecord) {
        setCallState(viewHolder, callRecord.getFlag());
        viewHolder.tv_yunyinshang.setText(callRecord.getName());
        viewHolder.tv_phone_number.setText(callRecord.getPhone());
        if (this.type == 0) {
            viewHolder.tv_call_time.setText(callRecord.getRemark());
        } else {
            viewHolder.tv_call_time.setVisibility(8);
        }
    }

    private void setCallState(ViewHolder viewHolder, int callState) {
        switch (callState) {
            case 1412:
                viewHolder.img_phone_state.setImageResource(R.drawable.yibo_yijie);
                return;
            case 1413:
                viewHolder.img_phone_state.setImageResource(R.drawable.yibo_yibo);
                return;
            case 1414:
                viewHolder.img_phone_state.setImageResource(R.drawable.yibo_weijie);
                return;
            default:
                viewHolder.img_phone_state.setVisibility(4);
                return;
        }
    }

    public void setSelectItem(int position) {
        this.selectPosition = position;
    }

    private class ViewHolder {
        public ImageView img_phone_state;
        public TextView tv_call_time;
        public TextView tv_phone_number;
        public TextView tv_yunyinshang;

        private ViewHolder() {
        }

        /* synthetic */ ViewHolder(CallRecordAdapter callRecordAdapter, ViewHolder viewHolder) {
            this();
        }
    }
}
