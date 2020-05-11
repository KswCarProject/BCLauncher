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
import com.touchus.benchilauncher.inface.OnBrightnessClickListener;
import com.touchus.benchilauncher.utils.SpUtilK;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class BrightnessDialog extends Dialog {
    public BrightnessDialog(Context context) {
        super(context);
    }

    public BrightnessDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    public static class Builder {
        private List<TextView> arr = new ArrayList();
        private BrightnessSetView brightnessSetView;
        private List<BrightnessSetView> brightnessViews = new ArrayList();
        private TextView brightness_tv;
        private Context context;
        /* access modifiers changed from: private */
        public int count = 0;
        private LauncherApplication mApp;
        private BrightnessDialog mDialog;
        private byte mIDRIVERENUM;
        private Launcher mMainActivity;
        public BrightnessDialogHandler mSettingHandler = new BrightnessDialogHandler(this);
        /* access modifiers changed from: private */
        public SpUtilK mSpUtilK;

        public Builder(Context context2) {
            this.context = context2;
        }

        public BrightnessDialog create() {
            this.mMainActivity = (Launcher) this.context;
            this.mSpUtilK = new SpUtilK(this.context);
            this.mApp = (LauncherApplication) this.mMainActivity.getApplication();
            this.mApp.registerHandler(this.mSettingHandler);
            this.mDialog = new BrightnessDialog(this.context, R.style.Dialog);
            View layout = ((LayoutInflater) this.context.getSystemService("layout_inflater")).inflate(R.layout.dialog_brightness_layout, (ViewGroup) null);
            this.mDialog.addContentView(layout, new ViewGroup.LayoutParams(-1, -2));
            this.brightness_tv = (TextView) layout.findViewById(R.id.brightness_tv);
            this.brightnessSetView = (BrightnessSetView) layout.findViewById(R.id.brightnessSet);
            this.arr.add(this.brightness_tv);
            this.brightnessViews.add(this.brightnessSetView);
            this.brightness_tv.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Builder.this.count = 0;
                    Builder.this.seclectTrue(Builder.this.count);
                }
            });
            this.brightnessSetView.setOnBrightnessClickListener(new OnBrightnessClickListener() {
                public void click(int value) {
                    Builder.this.count = 0;
                    Builder.this.seclectTrue(Builder.this.count);
                    Builder.this.mSpUtilK.putInt(SysConst.brightness, value);
                    Mainboard.getInstance().setBrightnessToMcu(value);
                }
            });
            this.brightnessSetView.setValue(this.mSpUtilK.getInt(SysConst.brightness, 80));
            this.mDialog.setContentView(layout);
            seclectTrue(this.count);
            return this.mDialog;
        }

        public void unregisterHandlerr() {
            this.mApp.unregisterHandler(this.mSettingHandler);
        }

        static class BrightnessDialogHandler extends Handler {
            private WeakReference<Builder> target;

            public BrightnessDialogHandler(Builder activity) {
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
                    this.brightnessViews.get(this.count).addValue();
                } else if (this.mIDRIVERENUM == Mainboard.EIdriverEnum.TURN_LEFT.getCode()) {
                    this.brightnessViews.get(this.count).decValue();
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
