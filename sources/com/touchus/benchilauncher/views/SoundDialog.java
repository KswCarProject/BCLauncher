package com.touchus.benchilauncher.views;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import com.backaudio.android.driver.Mainboard;
import com.touchus.benchilauncher.LauncherApplication;
import com.touchus.benchilauncher.R;
import com.touchus.benchilauncher.SysConst;
import com.touchus.benchilauncher.utils.SpUtilK;
import java.lang.ref.WeakReference;

public class SoundDialog extends Dialog {
    public SoundDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    public static class Builder {
        private Context context;
        private LauncherApplication mApp;
        private SoundDialog mDialog;
        private byte mIDRIVERENUM;
        private byte mIDRIVERSTATEENUM;
        public DaohangDialogHandler mSettingHandler = new DaohangDialogHandler(this);
        private ImageView mSoundAdd;
        private ImageView mSoundJianjian;
        /* access modifiers changed from: private */
        public SeekBar mSoundSeekBar;
        /* access modifiers changed from: private */
        public TextView mSoundTv;
        /* access modifiers changed from: private */
        public int max = 10;
        /* access modifiers changed from: private */
        public int mun = 0;
        /* access modifiers changed from: private */
        public SpUtilK spUtilK;
        /* access modifiers changed from: private */
        public int type;

        public Builder(Context context2) {
            this.context = context2;
        }

        public SoundDialog create(int voltype) {
            this.spUtilK = new SpUtilK(this.context);
            this.type = voltype;
            if (this.type == 0) {
                this.mun = this.spUtilK.getInt(SysConst.naviVoice, 5);
            } else {
                this.mun = this.spUtilK.getInt(SysConst.callVoice, 3);
            }
            this.mDialog = new SoundDialog(this.context, R.style.Dialog);
            View layout = ((LayoutInflater) this.context.getSystemService("layout_inflater")).inflate(R.layout.dialog_sound_layout, (ViewGroup) null);
            this.mDialog.addContentView(layout, new ViewGroup.LayoutParams(-1, -2));
            this.mSoundSeekBar = (SeekBar) layout.findViewById(R.id.sound_seekbar);
            this.mSoundAdd = (ImageView) layout.findViewById(R.id.sound_add);
            this.mSoundJianjian = (ImageView) layout.findViewById(R.id.sound_jianjian);
            this.mSoundTv = (TextView) layout.findViewById(R.id.sound_tv);
            this.mDialog.setContentView(layout);
            this.mSoundSeekBar.setProgress(this.mun);
            this.mSoundSeekBar.setMax(this.max);
            this.mSoundTv.setText(new StringBuilder(String.valueOf(this.mun)).toString());
            this.mApp = (LauncherApplication) this.context.getApplicationContext();
            if (this.type == 0) {
                this.mApp.registerHandler(this.mSettingHandler);
            }
            this.mSoundSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    Builder.this.mun = progress;
                    Builder.this.mSoundTv.setText(new StringBuilder(String.valueOf(Builder.this.mun)).toString());
                    if (Builder.this.type == 0) {
                        Builder.this.spUtilK.putInt(SysConst.naviVoice, Builder.this.mun);
                        return;
                    }
                    Builder.this.spUtilK.putInt(SysConst.callVoice, Builder.this.mun);
                    Mainboard.getInstance().setAllHornSoundValue(SysConst.basicNum, SysConst.callnum[Builder.this.mun], 0, 0, 0);
                }

                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                public void onStopTrackingTouch(SeekBar seekBar) {
                }
            });
            this.mSoundAdd.setOnClickListener(new View.OnClickListener() {
                public void onClick(View arg0) {
                    if (Builder.this.mun < Builder.this.max) {
                        Builder builder = Builder.this;
                        builder.mun = builder.mun + 1;
                    }
                    Builder.this.mSoundTv.setText(new StringBuilder(String.valueOf(Builder.this.mun)).toString());
                    Builder.this.mSoundSeekBar.setProgress(Builder.this.mun);
                }
            });
            this.mSoundJianjian.setOnClickListener(new View.OnClickListener() {
                public void onClick(View arg0) {
                    if (Builder.this.mun > 0) {
                        Builder builder = Builder.this;
                        builder.mun = builder.mun - 1;
                    }
                    Builder.this.mSoundTv.setText(new StringBuilder(String.valueOf(Builder.this.mun)).toString());
                    Builder.this.mSoundSeekBar.setProgress(Builder.this.mun);
                }
            });
            return this.mDialog;
        }

        public void unregisterHandlerr() {
            if (this.type == 0) {
                this.mApp.unregisterHandler(this.mSettingHandler);
            }
        }

        static class DaohangDialogHandler extends Handler {
            private WeakReference<Builder> target;

            public DaohangDialogHandler(Builder activity) {
                this.target = new WeakReference<>(activity);
            }

            public void handleMessage(Message msg) {
                if (this.target.get() != null) {
                    ((Builder) this.target.get()).handlerMsgUSB(msg);
                }
            }
        }

        public void handlerMsgUSB(Message msg) {
            if (msg.what == 6001) {
                Bundle temp = msg.getData();
                this.mIDRIVERENUM = temp.getByte(SysConst.IDRIVER_ENUM);
                this.mIDRIVERSTATEENUM = temp.getByte(SysConst.IDRIVER_STATE_ENUM);
                if (this.mIDRIVERENUM == Mainboard.EIdriverEnum.TURN_RIGHT.getCode() || this.mIDRIVERENUM == Mainboard.EIdriverEnum.RIGHT.getCode()) {
                    if (this.mun < this.max) {
                        this.mun++;
                    }
                    this.mSoundTv.setText(new StringBuilder(String.valueOf(this.mun)).toString());
                    this.mSoundSeekBar.setProgress(this.mun);
                } else if (this.mIDRIVERENUM == Mainboard.EIdriverEnum.TURN_LEFT.getCode() || this.mIDRIVERENUM == Mainboard.EIdriverEnum.LEFT.getCode()) {
                    if (this.mun > 0) {
                        this.mun--;
                    }
                    this.mSoundTv.setText(new StringBuilder(String.valueOf(this.mun)).toString());
                    this.mSoundSeekBar.setProgress(this.mun);
                } else if (this.mIDRIVERENUM == Mainboard.EIdriverEnum.BACK.getCode() || this.mIDRIVERENUM == Mainboard.EIdriverEnum.HOME.getCode() || this.mIDRIVERENUM == Mainboard.EIdriverEnum.STAR_BTN.getCode()) {
                    this.mDialog.dismiss();
                }
            }
        }
    }
}
