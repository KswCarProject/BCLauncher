package com.touchus.benchilauncher.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import com.touchus.benchilauncher.R;
import com.touchus.benchilauncher.inface.OnBrightnessClickListener;

public class BrightnessSetView extends RelativeLayout implements View.OnClickListener {
    /* access modifiers changed from: private */
    public SeekBar mBrightnessSeekBar;
    /* access modifiers changed from: private */
    public TextView mBrightnessTv;
    private int max;
    /* access modifiers changed from: private */
    public int num;
    /* access modifiers changed from: private */
    public OnBrightnessClickListener onBrightnessClickListener;
    private ImageView value_add;
    private ImageView value_dec;

    public BrightnessSetView(Context context) {
        this(context, (AttributeSet) null);
    }

    public BrightnessSetView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.max = 100;
        this.num = 80;
        ((LayoutInflater) context.getSystemService("layout_inflater")).inflate(R.layout.brightness_layout, this);
        this.mBrightnessTv = (TextView) findViewById(R.id.brightness_tv);
        this.value_dec = (ImageView) findViewById(R.id.brightness_dec);
        this.mBrightnessSeekBar = (SeekBar) findViewById(R.id.brightness_seekbar);
        this.value_add = (ImageView) findViewById(R.id.brightness_add);
        this.value_dec.setOnClickListener(this);
        this.value_add.setOnClickListener(this);
        this.mBrightnessSeekBar.setMax(this.max);
        this.mBrightnessSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress < 1) {
                    BrightnessSetView.this.mBrightnessSeekBar.setProgress(1);
                    return;
                }
                BrightnessSetView.this.num = progress;
                BrightnessSetView.this.mBrightnessTv.setText(new StringBuilder(String.valueOf(BrightnessSetView.this.num)).toString());
                if (BrightnessSetView.this.onBrightnessClickListener != null) {
                    BrightnessSetView.this.onBrightnessClickListener.click(BrightnessSetView.this.num);
                }
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.brightness_dec:
                if (this.num <= 5) {
                    this.num = 0;
                    break;
                } else {
                    this.num -= 5;
                    break;
                }
            case R.id.brightness_add:
                if (this.num >= this.max - 5) {
                    this.num = this.max;
                    break;
                } else {
                    this.num += 5;
                    break;
                }
        }
        setValue(this.num);
        this.onBrightnessClickListener.click(this.num);
    }

    public void setValue(int soundValue) {
        this.num = soundValue;
        this.mBrightnessTv.setText(new StringBuilder(String.valueOf(this.num)).toString());
        this.mBrightnessSeekBar.setProgress(this.num);
    }

    public void addValue() {
        this.value_add.performClick();
    }

    public void decValue() {
        this.value_dec.performClick();
    }

    public void setOnBrightnessClickListener(OnBrightnessClickListener onBrightnessClickListener2) {
        this.onBrightnessClickListener = onBrightnessClickListener2;
    }
}
