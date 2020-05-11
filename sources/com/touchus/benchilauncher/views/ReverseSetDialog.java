package com.touchus.benchilauncher.views;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import com.backaudio.android.driver.Mainboard;
import com.touchus.benchilauncher.Launcher;
import com.touchus.benchilauncher.LauncherApplication;
import com.touchus.benchilauncher.R;
import com.touchus.benchilauncher.SysConst;
import com.touchus.benchilauncher.utils.SpUtilK;
import java.lang.ref.WeakReference;

public class ReverseSetDialog extends Dialog {
    private Context context;
    private RadioButton english;
    private RadioGroup languageType;
    private LauncherApplication mApp;
    public ReverseialogHandler mHandler = new ReverseialogHandler(this);
    private byte mIDRIVERENUM;
    private Launcher mMMainActivity;
    private SpUtilK mSpUtilK;
    /* access modifiers changed from: private */
    public int selectPos = 0;
    private RadioButton simplified;
    private RadioButton traditional;

    public ReverseSetDialog(Context context2) {
        super(context2);
    }

    public ReverseSetDialog(Context context2, int themeResId) {
        super(context2, themeResId);
        this.context = context2;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_language_layout);
        this.mApp = (LauncherApplication) this.context.getApplicationContext();
        initView();
        initSetup();
        initData();
    }

    /* access modifiers changed from: protected */
    public void onStart() {
        this.mApp.registerHandler(this.mHandler);
        super.onStart();
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        this.mApp.unregisterHandler(this.mHandler);
        super.onStop();
    }

    private void initSetup() {
        this.languageType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.simplified:
                        ReverseSetDialog.this.selectPos = 0;
                        break;
                    case R.id.traditional:
                        ReverseSetDialog.this.selectPos = 1;
                        break;
                    case R.id.english:
                        ReverseSetDialog.this.selectPos = 2;
                        break;
                }
                ReverseSetDialog.this.seclectTrue(ReverseSetDialog.this.selectPos);
                ReverseSetDialog.this.press();
            }
        });
        seclectTrue(this.selectPos);
    }

    private void initView() {
        this.languageType = (RadioGroup) findViewById(R.id.language_type);
        this.simplified = (RadioButton) findViewById(R.id.simplified);
        this.traditional = (RadioButton) findViewById(R.id.traditional);
        this.english = (RadioButton) findViewById(R.id.english);
    }

    private void initData() {
        this.mMMainActivity = (Launcher) this.context;
        this.mSpUtilK = new SpUtilK((Context) this.mMMainActivity);
        this.selectPos = this.mSpUtilK.getInt(SysConst.FLAG_REVERSING_TYPE, 0);
        this.simplified.setText(this.context.getString(R.string.originalreverse));
        this.traditional.setText(this.context.getString(R.string.newaddreverse));
        this.english.setText(this.context.getString(R.string.addreverse360));
        ((RadioButton) this.languageType.getChildAt(this.selectPos)).setChecked(true);
    }

    public void unregisterHandlerr() {
        this.mApp.unregisterHandler(this.mHandler);
    }

    static class ReverseialogHandler extends Handler {
        private WeakReference<ReverseSetDialog> target;

        public ReverseialogHandler(ReverseSetDialog activity) {
            this.target = new WeakReference<>(activity);
        }

        public void handleMessage(Message msg) {
            if (this.target.get() != null) {
                ((ReverseSetDialog) this.target.get()).handlerMsgUSB(msg);
            }
        }
    }

    public void handlerMsgUSB(Message msg) {
        if (msg.what == 6001) {
            this.mIDRIVERENUM = msg.getData().getByte(SysConst.IDRIVER_ENUM);
            if (this.mIDRIVERENUM == Mainboard.EIdriverEnum.TURN_RIGHT.getCode() || this.mIDRIVERENUM == Mainboard.EIdriverEnum.DOWN.getCode()) {
                if (this.selectPos < this.languageType.getChildCount() - 1) {
                    this.selectPos++;
                }
                seclectTrue(this.selectPos);
            } else if (this.mIDRIVERENUM == Mainboard.EIdriverEnum.TURN_LEFT.getCode() || this.mIDRIVERENUM == Mainboard.EIdriverEnum.UP.getCode()) {
                if (this.selectPos > 0) {
                    this.selectPos--;
                }
                seclectTrue(this.selectPos);
            } else if (this.mIDRIVERENUM == Mainboard.EIdriverEnum.PRESS.getCode()) {
                press();
            } else if (this.mIDRIVERENUM == Mainboard.EIdriverEnum.BACK.getCode() || this.mIDRIVERENUM == Mainboard.EIdriverEnum.HOME.getCode() || this.mIDRIVERENUM == Mainboard.EIdriverEnum.STAR_BTN.getCode()) {
                dismiss();
            }
        }
    }

    /* access modifiers changed from: private */
    public void press() {
        this.mApp.iLanguageType = this.selectPos;
        this.mSpUtilK.putInt(SysConst.FLAG_REVERSING_TYPE, this.selectPos);
        Mainboard.getInstance().sendReverseSetToMcu(this.selectPos);
        dismiss();
    }

    /* access modifiers changed from: private */
    public void seclectTrue(int seclectCuttun) {
        for (int i = 0; i < this.languageType.getChildCount(); i++) {
            if (seclectCuttun == i) {
                this.languageType.getChildAt(i).setSelected(true);
            } else {
                this.languageType.getChildAt(i).setSelected(false);
            }
        }
    }
}
