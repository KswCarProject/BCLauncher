package com.touchus.benchilauncher.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.touchus.benchilauncher.R;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Priority;

public class CycleWheelView extends ListView {
    private static final int COLOR_DIVIDER_DEFALUT = Color.parseColor("#747474");
    private static final int COLOR_SOLID_DEFAULT = Color.parseColor("#3e4043");
    private static final int COLOR_SOLID_SELET_DEFAULT = Color.parseColor("#323335");
    private static final int HEIGHT_DIVIDER_DEFAULT = 2;
    public static final String TAG = CycleWheelView.class.getSimpleName();
    private static final int WHEEL_SIZE_DEFAULT = 3;
    /* access modifiers changed from: private */
    public boolean cylceEnable;
    /* access modifiers changed from: private */
    public int dividerColor = COLOR_DIVIDER_DEFALUT;
    /* access modifiers changed from: private */
    public int dividerHeight = 2;
    private CycleWheelViewAdapter mAdapter;
    private float mAlphaGradual = 0.7f;
    private int mCurrentPositon;
    private Handler mHandler;
    /* access modifiers changed from: private */
    public int mItemHeight;
    /* access modifiers changed from: private */
    public int mItemLabelTvId;
    /* access modifiers changed from: private */
    public int mItemLayoutId;
    private WheelItemSelectedListener mItemSelectedListener;
    private int mLabelColor = -7829368;
    private int mLabelSelectColor = -1;
    private List<String> mLabels;
    /* access modifiers changed from: private */
    public int mWheelSize = 3;
    /* access modifiers changed from: private */
    public int seletedSolidColor = COLOR_SOLID_SELET_DEFAULT;
    /* access modifiers changed from: private */
    public int solidColor = COLOR_SOLID_DEFAULT;

    public interface WheelItemSelectedListener {
        void onItemSelected(int i, String str);
    }

    public CycleWheelView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public CycleWheelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CycleWheelView(Context context) {
        super(context);
    }

    private void init() {
        this.mHandler = new Handler();
        this.mItemLayoutId = R.layout.item_cyclewheel;
        this.mItemLabelTvId = R.id.tv_label_item_wheel;
        this.mAdapter = new CycleWheelViewAdapter();
        setVerticalScrollBarEnabled(false);
        setScrollingCacheEnabled(false);
        setCacheColorHint(0);
        setFadingEdgeLength(0);
        setOverScrollMode(2);
        setDividerHeight(0);
        setAdapter(this.mAdapter);
        setOnScrollListener(new AbsListView.OnScrollListener() {
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                View itemView;
                if (scrollState == 0 && (itemView = CycleWheelView.this.getChildAt(0)) != null) {
                    float deltaY = itemView.getY();
                    if (deltaY != 0.0f) {
                        if (Math.abs(deltaY) < ((float) (CycleWheelView.this.mItemHeight / 2))) {
                            CycleWheelView.this.smoothScrollBy(CycleWheelView.this.getDistance(deltaY), 50);
                        } else {
                            CycleWheelView.this.smoothScrollBy(CycleWheelView.this.getDistance(((float) CycleWheelView.this.mItemHeight) + deltaY), 50);
                        }
                    }
                }
            }

            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                CycleWheelView.this.refreshItems();
            }
        });
    }

    /* access modifiers changed from: private */
    public int getDistance(float scrollDistance) {
        if (Math.abs(scrollDistance) <= 2.0f) {
            return (int) scrollDistance;
        }
        if (Math.abs(scrollDistance) < 12.0f) {
            return scrollDistance > 0.0f ? 2 : -2;
        }
        return (int) (scrollDistance / 6.0f);
    }

    /* access modifiers changed from: private */
    public void refreshItems() {
        int position;
        int offset = this.mWheelSize / 2;
        int firstPosition = getFirstVisiblePosition();
        if (getChildAt(0) != null) {
            if (Math.abs(getChildAt(0).getY()) <= ((float) (this.mItemHeight / 2))) {
                position = firstPosition + offset;
            } else {
                position = firstPosition + offset + 1;
            }
            if (position != this.mCurrentPositon) {
                this.mCurrentPositon = position;
                if (this.mItemSelectedListener != null) {
                    this.mItemSelectedListener.onItemSelected(getSelection(), getSelectLabel());
                }
                resetItems(firstPosition, position, offset);
            }
        }
    }

    private void resetItems(int firstPosition, int position, int offset) {
        for (int i = (position - offset) - 1; i < position + offset + 1; i++) {
            View itemView = getChildAt(i - firstPosition);
            if (itemView != null) {
                TextView labelTv = (TextView) itemView.findViewById(this.mItemLabelTvId);
                if (position == i) {
                    labelTv.setTextColor(this.mLabelSelectColor);
                    itemView.setAlpha(1.0f);
                } else {
                    labelTv.setTextColor(this.mLabelColor);
                    itemView.setAlpha((float) Math.pow((double) this.mAlphaGradual, (double) Math.abs(i - position)));
                }
            }
        }
    }

    public void setLabels(List<String> labels) {
        this.mLabels = labels;
        this.mAdapter.setData(this.mLabels);
        this.mAdapter.notifyDataSetChanged();
        initView();
    }

    public void setOnWheelItemSelectedListener(WheelItemSelectedListener mItemSelectedListener2) {
        this.mItemSelectedListener = mItemSelectedListener2;
    }

    public List<String> getLabels() {
        return this.mLabels;
    }

    public void setCycleEnable(boolean enable) {
        if (this.cylceEnable != enable) {
            this.cylceEnable = enable;
            this.mAdapter.notifyDataSetChanged();
            setSelection(getSelection());
        }
    }

    public void setSelection(final int position) {
        this.mHandler.post(new Runnable() {
            public void run() {
                CycleWheelView.super.setSelection(CycleWheelView.this.getPosition(position));
            }
        });
    }

    /* access modifiers changed from: private */
    public int getPosition(int positon) {
        if (this.mLabels == null || this.mLabels.size() == 0) {
            return 0;
        }
        if (!this.cylceEnable) {
            return positon;
        }
        return positon + (this.mLabels.size() * (1073741823 / this.mLabels.size()));
    }

    public int getSelection() {
        if (this.mCurrentPositon == 0) {
            this.mCurrentPositon = this.mWheelSize / 2;
        }
        return (this.mCurrentPositon - (this.mWheelSize / 2)) % this.mLabels.size();
    }

    public String getSelectLabel() {
        int position = getSelection();
        if (position < 0) {
            position = 0;
        }
        try {
            return this.mLabels.get(position);
        } catch (Exception e) {
            return "";
        }
    }

    public void setWheelItemLayout(int itemResId, int labelTvId) {
        this.mItemLayoutId = itemResId;
        this.mItemLabelTvId = labelTvId;
        this.mAdapter = new CycleWheelViewAdapter();
        this.mAdapter.setData(this.mLabels);
        setAdapter(this.mAdapter);
        initView();
    }

    public void setLabelColor(int labelColor) {
        this.mLabelColor = labelColor;
        resetItems(getFirstVisiblePosition(), this.mCurrentPositon, this.mWheelSize / 2);
    }

    public void setLabelSelectColor(int labelSelectColor) {
        this.mLabelSelectColor = labelSelectColor;
        resetItems(getFirstVisiblePosition(), this.mCurrentPositon, this.mWheelSize / 2);
    }

    public void setAlphaGradual(float alphaGradual) {
        this.mAlphaGradual = alphaGradual;
        resetItems(getFirstVisiblePosition(), this.mCurrentPositon, this.mWheelSize / 2);
    }

    public void setWheelSize(int wheelSize) throws CycleWheelViewException {
        if (wheelSize < 3 || wheelSize % 2 != 1) {
            throw new CycleWheelViewException("Wheel Size Error , Must Be 3,5,7,9...");
        }
        this.mWheelSize = wheelSize;
        initView();
    }

    public void setSolid(int unselectedSolidColor, int selectedSolidColor) {
        this.solidColor = unselectedSolidColor;
        this.seletedSolidColor = selectedSolidColor;
        initView();
    }

    public void setDivider(int dividerColor2, int dividerHeight2) {
        this.dividerColor = dividerColor2;
        this.dividerHeight = dividerHeight2;
    }

    private void initView() {
        this.mItemHeight = measureHeight();
        getLayoutParams().height = this.mItemHeight * this.mWheelSize;
        this.mAdapter.setData(this.mLabels);
        this.mAdapter.notifyDataSetChanged();
        setBackgroundDrawable(new Drawable() {
            public void draw(Canvas canvas) {
                int viewWidth = CycleWheelView.this.getWidth();
                Paint dividerPaint = new Paint();
                dividerPaint.setColor(CycleWheelView.this.dividerColor);
                dividerPaint.setStrokeWidth((float) CycleWheelView.this.dividerHeight);
                Paint seletedSolidPaint = new Paint();
                seletedSolidPaint.setColor(CycleWheelView.this.seletedSolidColor);
                Paint solidPaint = new Paint();
                solidPaint.setColor(CycleWheelView.this.solidColor);
                canvas.drawRect(0.0f, 0.0f, (float) viewWidth, (float) (CycleWheelView.this.mItemHeight * (CycleWheelView.this.mWheelSize / 2)), solidPaint);
                canvas.drawRect(0.0f, (float) (CycleWheelView.this.mItemHeight * ((CycleWheelView.this.mWheelSize / 2) + 1)), (float) viewWidth, (float) (CycleWheelView.this.mItemHeight * CycleWheelView.this.mWheelSize), solidPaint);
                canvas.drawRect(0.0f, (float) (CycleWheelView.this.mItemHeight * (CycleWheelView.this.mWheelSize / 2)), (float) viewWidth, (float) (CycleWheelView.this.mItemHeight * ((CycleWheelView.this.mWheelSize / 2) + 1)), seletedSolidPaint);
                canvas.drawLine(0.0f, (float) (CycleWheelView.this.mItemHeight * (CycleWheelView.this.mWheelSize / 2)), (float) viewWidth, (float) (CycleWheelView.this.mItemHeight * (CycleWheelView.this.mWheelSize / 2)), dividerPaint);
                canvas.drawLine(0.0f, (float) (CycleWheelView.this.mItemHeight * ((CycleWheelView.this.mWheelSize / 2) + 1)), (float) viewWidth, (float) (CycleWheelView.this.mItemHeight * ((CycleWheelView.this.mWheelSize / 2) + 1)), dividerPaint);
            }

            public void setAlpha(int alpha) {
            }

            public void setColorFilter(ColorFilter cf) {
            }

            public int getOpacity() {
                return 0;
            }
        });
    }

    private int measureHeight() {
        View itemView = LayoutInflater.from(getContext()).inflate(this.mItemLayoutId, (ViewGroup) null);
        itemView.setLayoutParams(new ViewGroup.LayoutParams(-1, -2));
        itemView.measure(View.MeasureSpec.makeMeasureSpec(0, 0), View.MeasureSpec.makeMeasureSpec(0, 0));
        return itemView.getMeasuredHeight();
    }

    public class CycleWheelViewException extends Exception {
        private static final long serialVersionUID = 1;

        public CycleWheelViewException(String detailMessage) {
            super(detailMessage);
        }
    }

    public class CycleWheelViewAdapter extends BaseAdapter {
        private List<String> mData = new ArrayList();

        public CycleWheelViewAdapter() {
        }

        public void setData(List<String> mWheelLabels) {
            this.mData.clear();
            this.mData.addAll(mWheelLabels);
        }

        public int getCount() {
            if (CycleWheelView.this.cylceEnable) {
                return Priority.OFF_INT;
            }
            return (this.mData.size() + CycleWheelView.this.mWheelSize) - 1;
        }

        public Object getItem(int position) {
            return "";
        }

        public long getItemId(int position) {
            return (long) position;
        }

        public boolean isEnabled(int position) {
            return false;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(CycleWheelView.this.getContext()).inflate(CycleWheelView.this.mItemLayoutId, (ViewGroup) null);
            }
            TextView textView = (TextView) convertView.findViewById(CycleWheelView.this.mItemLabelTvId);
            if (position < CycleWheelView.this.mWheelSize / 2 || (!CycleWheelView.this.cylceEnable && position >= this.mData.size() + (CycleWheelView.this.mWheelSize / 2))) {
                textView.setText("");
                convertView.setVisibility(4);
            } else {
                textView.setText(this.mData.get((position - (CycleWheelView.this.mWheelSize / 2)) % this.mData.size()));
                convertView.setVisibility(0);
            }
            return convertView;
        }
    }
}
