package com.touchus.benchilauncher.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.touchus.benchilauncher.R;
import com.touchus.benchilauncher.inface.OnSoundClickListener;
import com.touchus.benchilauncher.views.VerticalSeekBar;

public class EQSetView extends RelativeLayout {
    private TextView mEQName;
    private VerticalSeekBar mEQSeekBar;
    /* access modifiers changed from: private */
    public TextView mEQValue;
    boolean mfromAuto;
    /* access modifiers changed from: private */
    public int num;
    /* access modifiers changed from: private */
    public OnSoundClickListener onSoundClickListener;

    public EQSetView(Context context) {
        this(context, (AttributeSet) null);
    }

    public EQSetView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mfromAuto = false;
        ((LayoutInflater) context.getSystemService("layout_inflater")).inflate(R.layout.eq_set, this);
        this.mEQName = (TextView) findViewById(R.id.eq_name);
        this.mEQValue = (TextView) findViewById(R.id.eq_value);
        this.mEQSeekBar = (VerticalSeekBar) findViewById(R.id.eq_seekbar);
        this.mEQSeekBar.setMax(20);
        this.mEQSeekBar.setOnSeekBarChangeListener(new VerticalSeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(VerticalSeekBar seekBar, int progress, boolean fromUser) {
                EQSetView.this.num = progress;
                EQSetView.this.mEQValue.setText(new StringBuilder(String.valueOf(EQSetView.this.num - 10)).toString());
                if (EQSetView.this.onSoundClickListener != null) {
                    EQSetView.this.onSoundClickListener.click(EQSetView.this, EQSetView.this.num, !EQSetView.this.mfromAuto);
                }
                if (EQSetView.this.mfromAuto) {
                    EQSetView.this.mfromAuto = false;
                }
            }

            public void onStartTrackingTouch(VerticalSeekBar seekBar) {
            }

            public void onStopTrackingTouch(VerticalSeekBar seekBar) {
            }
        });
    }

    public void setSoundValue(String EQTypeName, int soundValue) {
        this.num = soundValue;
        this.mEQName.setText(EQTypeName);
        this.mEQValue.setText(new StringBuilder(String.valueOf(this.num - 10)).toString());
        this.mfromAuto = true;
        this.mEQSeekBar.setProgress(this.num);
    }

    public void setOnSoundClickListener(OnSoundClickListener onSoundClickListener2) {
        this.onSoundClickListener = onSoundClickListener2;
    }
}
