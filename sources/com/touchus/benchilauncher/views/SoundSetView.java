package com.touchus.benchilauncher.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import com.touchus.benchilauncher.LauncherApplication;
import com.touchus.benchilauncher.R;
import com.touchus.benchilauncher.inface.OnSoundClickListener;

public class SoundSetView extends RelativeLayout implements View.OnClickListener {
    public static int MIXPRO = 1;
    public static int SOUND = 0;
    private LauncherApplication app;
    private String baseType;
    private Context context;
    /* access modifiers changed from: private */
    public SeekBar mSoundSeekBar;
    /* access modifiers changed from: private */
    public TextView mSoundTv;
    private int max;
    /* access modifiers changed from: private */
    public int num;
    /* access modifiers changed from: private */
    public OnSoundClickListener onSoundClickListener;
    private ImageView sound_add;
    private ImageView sound_jianjian;
    /* access modifiers changed from: private */
    public int type;

    public SoundSetView(Context context2) {
        this(context2, (AttributeSet) null);
    }

    public SoundSetView(Context context2, AttributeSet attrs) {
        super(context2, attrs);
        this.max = 10;
        this.num = 10;
        this.type = 0;
        this.baseType = "音量";
        this.context = context2;
        this.app = (LauncherApplication) context2.getApplicationContext();
        ((LayoutInflater) context2.getSystemService("layout_inflater")).inflate(R.layout.sound_set, this);
        this.mSoundTv = (TextView) findViewById(R.id.sound_tv);
        this.sound_jianjian = (ImageView) findViewById(R.id.sound_jianjian);
        this.mSoundSeekBar = (SeekBar) findViewById(R.id.sound_seekbar);
        this.sound_add = (ImageView) findViewById(R.id.sound_add);
        this.sound_jianjian.setOnClickListener(this);
        this.sound_add.setOnClickListener(this);
        this.mSoundSeekBar.setMax(this.max);
        this.mSoundSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress < 1) {
                    SoundSetView.this.mSoundSeekBar.setProgress(1);
                    return;
                }
                SoundSetView.this.num = progress;
                if (SoundSetView.this.type == SoundSetView.SOUND) {
                    SoundSetView.this.mSoundTv.setText(new StringBuilder(String.valueOf(SoundSetView.this.num)).toString());
                } else {
                    SoundSetView.this.mSoundTv.setText(String.valueOf(SoundSetView.this.num * 10) + "%");
                }
                if (SoundSetView.this.onSoundClickListener != null) {
                    SoundSetView.this.onSoundClickListener.click(SoundSetView.this, SoundSetView.this.num, fromUser);
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
            case R.id.sound_jianjian:
                if (this.num > 1) {
                    this.num--;
                    break;
                }
                break;
            case R.id.sound_add:
                if (this.num < this.max) {
                    this.num++;
                    break;
                }
                break;
        }
        setSoundValue(this.num, this.type);
        this.onSoundClickListener.click(this, this.num, false);
    }

    public void setSoundValue(int soundValue, int type2) {
        this.type = type2;
        this.num = soundValue;
        if (type2 == SOUND) {
            this.mSoundTv.setText(new StringBuilder(String.valueOf(this.num)).toString());
        } else {
            this.mSoundTv.setText(String.valueOf(this.num * 10) + "%");
        }
        this.mSoundSeekBar.setProgress(this.num);
    }

    public void setSpeakText(String str) {
        this.baseType = str;
    }

    public void addSoundValue() {
        this.sound_add.performClick();
    }

    public void decSoundValue() {
        this.sound_jianjian.performClick();
    }

    public void setOnSoundClickListener(OnSoundClickListener onSoundClickListener2) {
        this.onSoundClickListener = onSoundClickListener2;
    }
}
