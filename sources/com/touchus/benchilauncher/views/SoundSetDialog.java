package com.touchus.benchilauncher.views;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.backaudio.android.driver.Mainboard;
import com.touchus.benchilauncher.Launcher;
import com.touchus.benchilauncher.LauncherApplication;
import com.touchus.benchilauncher.R;
import com.touchus.benchilauncher.SysConst;
import com.touchus.benchilauncher.inface.OnSoundClickListener;
import com.touchus.benchilauncher.utils.SpUtilK;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class SoundSetDialog extends Dialog {
    public SoundSetDialog(Context context) {
        super(context);
    }

    public SoundSetDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    public static class Builder {
        private List<TextView> arr = new ArrayList();
        private Context context;
        /* access modifiers changed from: private */
        public int count = 0;
        private SoundSetView dvSetView;
        private TextView dv_tv;
        /* access modifiers changed from: private */
        public boolean isFirst = false;
        private LauncherApplication mApp;
        private SoundSetDialog mDialog;
        private byte mIDRIVERENUM;
        private Launcher mMMainActivity;
        public YuyingDialogHandler mSettingHandler = new YuyingDialogHandler(this);
        /* access modifiers changed from: private */
        public SpUtilK mSpUtilK;
        private SoundSetView mediaSetView;
        private TextView media_tv;
        private SoundSetView mixSetView;
        private TextView mix_tv;
        private SoundSetView naviSetView;
        private TextView navi_tv;
        private int size = 4;
        private List<SoundSetView> soundViews = new ArrayList();

        public Builder(Context context2) {
            this.context = context2;
        }

        public SoundSetDialog create() {
            this.mMMainActivity = (Launcher) this.context;
            this.mSpUtilK = new SpUtilK(this.context);
            this.mApp = (LauncherApplication) this.mMMainActivity.getApplication();
            this.mApp.registerHandler(this.mSettingHandler);
            this.isFirst = true;
            this.mDialog = new SoundSetDialog(this.context, R.style.Dialog);
            View layout = ((LayoutInflater) this.context.getSystemService("layout_inflater")).inflate(R.layout.dialog_sound_set_layout, (ViewGroup) null);
            this.mDialog.addContentView(layout, new ViewGroup.LayoutParams(-1, -2));
            this.navi_tv = (TextView) layout.findViewById(R.id.navi_tv);
            this.media_tv = (TextView) layout.findViewById(R.id.media_tv);
            this.dv_tv = (TextView) layout.findViewById(R.id.dv_tv);
            this.mix_tv = (TextView) layout.findViewById(R.id.mix_tv);
            this.naviSetView = (SoundSetView) layout.findViewById(R.id.naviSound);
            this.mediaSetView = (SoundSetView) layout.findViewById(R.id.mediaSound);
            this.dvSetView = (SoundSetView) layout.findViewById(R.id.dvSound);
            this.mixSetView = (SoundSetView) layout.findViewById(R.id.mixSound);
            this.arr.add(this.navi_tv);
            this.soundViews.add(this.naviSetView);
            if (!SysConst.isBT()) {
                this.arr.add(this.media_tv);
                this.soundViews.add(this.mediaSetView);
                this.media_tv.setVisibility(0);
                this.mediaSetView.setVisibility(0);
                if (this.mApp.ismix) {
                    this.arr.add(this.mix_tv);
                    this.soundViews.add(this.mixSetView);
                    this.mix_tv.setVisibility(0);
                    this.mixSetView.setVisibility(0);
                } else {
                    this.mix_tv.setVisibility(8);
                    this.mixSetView.setVisibility(8);
                }
            } else {
                this.media_tv.setVisibility(8);
                this.mediaSetView.setVisibility(8);
                this.mix_tv.setVisibility(8);
                this.mixSetView.setVisibility(8);
            }
            this.dv_tv.setVisibility(8);
            this.dvSetView.setVisibility(8);
            this.navi_tv.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Builder.this.count = 0;
                    Builder.this.seclectTrue(Builder.this.count);
                }
            });
            this.media_tv.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Builder.this.count = 1;
                    Builder.this.seclectTrue(Builder.this.count);
                }
            });
            this.mix_tv.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Builder.this.count = 2;
                    Builder.this.seclectTrue(Builder.this.count);
                }
            });
            this.dv_tv.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Builder.this.count = 3;
                    Builder.this.seclectTrue(Builder.this.count);
                }
            });
            this.naviSetView.setOnSoundClickListener(new OnSoundClickListener() {
                public void click(View view, int value, boolean fromUser) {
                    Builder.this.count = 0;
                    Builder.this.seclectTrue(Builder.this.count);
                    Builder.this.mSpUtilK.putInt(SysConst.naviVoice, value);
                }
            });
            this.mediaSetView.setOnSoundClickListener(new OnSoundClickListener() {
                public void click(View view, int value, boolean fromUser) {
                    Builder.this.count = 1;
                    Builder.this.seclectTrue(Builder.this.count);
                    Builder.this.mSpUtilK.putInt(SysConst.mediaVoice, value);
                    if (!Builder.this.isFirst) {
                        Mainboard.getInstance().setAllHornSoundValue(SysConst.mediaBasicNum, SysConst.num[value], 0, SysConst.num[value], SysConst.num[value]);
                    }
                    Builder.this.isFirst = false;
                }
            });
            this.mixSetView.setOnSoundClickListener(new OnSoundClickListener() {
                public void click(View view, int value, boolean fromUser) {
                    Builder.this.count = 2;
                    Builder.this.seclectTrue(Builder.this.count);
                    Builder.this.mSpUtilK.putInt(SysConst.mixPro, value);
                    SysConst.musicDecVolume = (float) (((double) value) / 10.0d);
                }
            });
            this.dvSetView.setOnSoundClickListener(new OnSoundClickListener() {
                public void click(View view, int value, boolean fromUser) {
                    Builder.this.count = 3;
                    Builder.this.seclectTrue(Builder.this.count);
                    Builder.this.mSpUtilK.putInt(SysConst.dvVoice, value);
                }
            });
            if (SysConst.DV_CUSTOM) {
                this.dvSetView.setSoundValue(this.mSpUtilK.getInt(SysConst.dvVoice, 5), SoundSetView.SOUND);
            }
            this.mediaSetView.setSoundValue(this.mSpUtilK.getInt(SysConst.mediaVoice, 5), SoundSetView.SOUND);
            this.mediaSetView.setSpeakText(this.context.getString(R.string.name_media_sound));
            this.mixSetView.setSoundValue(this.mSpUtilK.getInt(SysConst.mixPro, 5), SoundSetView.MIXPRO);
            this.mixSetView.setSpeakText(this.context.getString(R.string.name_media_mix));
            this.naviSetView.setSoundValue(this.mSpUtilK.getInt(SysConst.naviVoice, 3), SoundSetView.SOUND);
            this.naviSetView.setSpeakText(this.context.getString(R.string.name_navi_sound));
            this.mDialog.setContentView(layout);
            seclectTrue(this.count);
            return this.mDialog;
        }

        public void unregisterHandlerr() {
            this.mApp.unregisterHandler(this.mSettingHandler);
        }

        static class YuyingDialogHandler extends Handler {
            private WeakReference<Builder> target;

            public YuyingDialogHandler(Builder activity) {
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
                this.mIDRIVERENUM = msg.getData().getByte(SysConst.IDRIVER_ENUM);
                if (this.mIDRIVERENUM == Mainboard.EIdriverEnum.DOWN.getCode()) {
                    if (this.count < this.arr.size() - 1) {
                        this.count++;
                    }
                    seclectTrue(this.count);
                } else if (this.mIDRIVERENUM == Mainboard.EIdriverEnum.UP.getCode()) {
                    if (this.count > 0) {
                        this.count--;
                    }
                    seclectTrue(this.count);
                } else if (this.mIDRIVERENUM == Mainboard.EIdriverEnum.TURN_RIGHT.getCode()) {
                    this.soundViews.get(this.count).addSoundValue();
                } else if (this.mIDRIVERENUM == Mainboard.EIdriverEnum.TURN_LEFT.getCode()) {
                    this.soundViews.get(this.count).decSoundValue();
                } else if (this.mIDRIVERENUM == Mainboard.EIdriverEnum.PRESS.getCode() || this.mIDRIVERENUM == Mainboard.EIdriverEnum.BACK.getCode() || this.mIDRIVERENUM == Mainboard.EIdriverEnum.HOME.getCode() || this.mIDRIVERENUM == Mainboard.EIdriverEnum.STAR_BTN.getCode()) {
                    this.mDialog.dismiss();
                }
            }
        }

        /* access modifiers changed from: private */
        public void seclectTrue(int seclectCuttun) {
            for (int i = 0; i < this.arr.size(); i++) {
                if (seclectCuttun == i) {
                    this.arr.get(i).setSelected(true);
                } else {
                    this.arr.get(i).setSelected(false);
                }
            }
        }
    }
}
