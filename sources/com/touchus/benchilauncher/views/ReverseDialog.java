package com.touchus.benchilauncher.views;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import com.backaudio.android.driver.Mainboard;
import com.touchus.benchilauncher.Launcher;
import com.touchus.benchilauncher.LauncherApplication;
import com.touchus.benchilauncher.R;
import com.touchus.benchilauncher.SysConst;
import com.touchus.benchilauncher.utils.SpUtilK;
import java.lang.ref.WeakReference;

public class ReverseDialog extends Dialog implements View.OnClickListener {
    private RadioButton addreverse360;
    private Context context;
    private LauncherApplication mApp;
    public ReverseialogHandler mHandler = new ReverseialogHandler(this);
    private byte mIDRIVERENUM;
    private Launcher mMMainActivity;
    private SpUtilK mSpUtilK;
    private RadioButton mute;
    private RadioButton newaddreverse;
    private RadioButton normal;
    private RadioButton originalreverse;
    /* access modifiers changed from: private */
    public int reversePos = 0;
    private RadioGroup reverseType;
    private int selectPos = 0;
    /* access modifiers changed from: private */
    public int type = 1;
    private RadioGroup voiceSet;
    /* access modifiers changed from: private */
    public int voicesetPos = 0;

    public ReverseDialog(Context context2) {
        super(context2);
    }

    public ReverseDialog(Context context2, int themeResId) {
        super(context2, themeResId);
        this.context = context2;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_daoche_layout);
        this.mApp = (LauncherApplication) this.context.getApplicationContext();
        initView();
        initData();
        initSetup();
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
        this.originalreverse.setOnClickListener(this);
        this.newaddreverse.setOnClickListener(this);
        this.addreverse360.setOnClickListener(this);
        this.normal.setOnClickListener(this);
        this.mute.setOnClickListener(this);
        ((RadioButton) this.voiceSet.getChildAt(this.voicesetPos)).setChecked(true);
        ((RadioButton) this.reverseType.getChildAt(this.reversePos)).setChecked(true);
        this.reverseType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.originalreverse:
                        ReverseDialog.this.reversePos = 0;
                        break;
                    case R.id.newaddreverse:
                        ReverseDialog.this.reversePos = 1;
                        break;
                    case R.id.addreverse360:
                        ReverseDialog.this.reversePos = 2;
                        break;
                }
                ReverseDialog.this.type = 1;
                ReverseDialog.this.seclectTrue(ReverseDialog.this.reversePos);
                ReverseDialog.this.press();
            }
        });
        this.voiceSet.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.mute:
                        ReverseDialog.this.voicesetPos = 0;
                        break;
                    case R.id.normal:
                        ReverseDialog.this.voicesetPos = 1;
                        break;
                }
                ReverseDialog.this.type = 2;
                ReverseDialog.this.seclectTrue(ReverseDialog.this.voicesetPos);
                ReverseDialog.this.press();
            }
        });
        seclectTrue(this.reversePos);
    }

    private void initView() {
        this.reverseType = (RadioGroup) findViewById(R.id.reverse_type);
        this.voiceSet = (RadioGroup) findViewById(R.id.voiceset);
        this.originalreverse = (RadioButton) findViewById(R.id.originalreverse);
        this.newaddreverse = (RadioButton) findViewById(R.id.newaddreverse);
        this.addreverse360 = (RadioButton) findViewById(R.id.addreverse360);
        this.normal = (RadioButton) findViewById(R.id.normal);
        this.mute = (RadioButton) findViewById(R.id.mute);
    }

    private void initData() {
        this.mMMainActivity = (Launcher) this.context;
        this.mSpUtilK = new SpUtilK((Context) this.mMMainActivity);
        this.reversePos = this.mSpUtilK.getInt(SysConst.FLAG_REVERSING_TYPE, 1);
        this.voicesetPos = this.mSpUtilK.getInt(SysConst.FLAG_REVERSING_VOICE, 0);
        if (this.voicesetPos == 1) {
            this.mApp.eMediaSet = Mainboard.EReverserMediaSet.NOMAL;
            return;
        }
        this.mApp.eMediaSet = Mainboard.EReverserMediaSet.MUTE;
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.originalreverse:
            case R.id.newaddreverse:
            case R.id.addreverse360:
                this.type = 1;
                seclectTrue(this.reversePos);
                return;
            case R.id.mute:
            case R.id.normal:
                this.type = 2;
                seclectTrue(this.voicesetPos);
                return;
            default:
                return;
        }
    }

    public void unregisterHandlerr() {
        this.mApp.unregisterHandler(this.mHandler);
    }

    static class ReverseialogHandler extends Handler {
        private WeakReference<ReverseDialog> target;

        public ReverseialogHandler(ReverseDialog activity) {
            this.target = new WeakReference<>(activity);
        }

        public void handleMessage(Message msg) {
            if (this.target.get() != null) {
                ((ReverseDialog) this.target.get()).handlerMsgUSB(msg);
            }
        }
    }

    public void handlerMsgUSB(Message msg) {
        if (msg.what == 6001) {
            this.mIDRIVERENUM = msg.getData().getByte(SysConst.IDRIVER_ENUM);
            if (this.mIDRIVERENUM == Mainboard.EIdriverEnum.TURN_RIGHT.getCode() || this.mIDRIVERENUM == Mainboard.EIdriverEnum.DOWN.getCode()) {
                if (this.type == 1 && this.reversePos < this.reverseType.getChildCount() - 1) {
                    this.reversePos++;
                    this.selectPos = this.reversePos;
                } else if (this.type == 1 && this.reversePos == this.reverseType.getChildCount() - 1) {
                    this.type++;
                    this.voicesetPos = 0;
                    this.selectPos = this.voicesetPos;
                } else if (this.type == 2 && this.voicesetPos < this.voiceSet.getChildCount() - 1) {
                    this.voicesetPos++;
                    this.selectPos = this.voicesetPos;
                }
                seclectTrue(this.selectPos);
            } else if (this.mIDRIVERENUM == Mainboard.EIdriverEnum.TURN_LEFT.getCode() || this.mIDRIVERENUM == Mainboard.EIdriverEnum.UP.getCode()) {
                if (this.type == 2 && this.voicesetPos > 0) {
                    this.voicesetPos--;
                    this.selectPos = this.voicesetPos;
                } else if (this.type == 2 && this.voicesetPos == 0) {
                    this.type--;
                    this.reversePos = this.reverseType.getChildCount() - 1;
                    this.selectPos = this.reversePos;
                } else if (this.type == 1 && this.reversePos > 0) {
                    this.reversePos--;
                    this.selectPos = this.reversePos;
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
        if (this.type == 2) {
            this.mSpUtilK.putInt(SysConst.FLAG_REVERSING_VOICE, this.voicesetPos);
            if (this.voicesetPos == 1) {
                this.mApp.eMediaSet = Mainboard.EReverserMediaSet.NOMAL;
                Mainboard.getInstance().sendReverseMediaSetToMcu(2);
            } else {
                this.mApp.eMediaSet = Mainboard.EReverserMediaSet.MUTE;
                Mainboard.getInstance().sendReverseMediaSetToMcu(0);
            }
            ((RadioButton) this.voiceSet.getChildAt(this.voicesetPos)).setChecked(true);
            return;
        }
        this.mSpUtilK.putInt(SysConst.FLAG_REVERSING_TYPE, this.reversePos);
        Mainboard.getInstance().sendReverseSetToMcu(this.reversePos);
        ((RadioButton) this.reverseType.getChildAt(this.reversePos)).setChecked(true);
    }

    /* access modifiers changed from: private */
    public void seclectTrue(int seclectCuttun) {
        RadioGroup posGroup;
        if (this.type == 2) {
            posGroup = this.voiceSet;
            this.reverseType.getChildAt(this.reversePos).setSelected(false);
        } else {
            posGroup = this.reverseType;
            this.voiceSet.getChildAt(this.voicesetPos).setSelected(false);
        }
        for (int i = 0; i < posGroup.getChildCount(); i++) {
            if (seclectCuttun == i) {
                posGroup.getChildAt(i).setSelected(true);
            } else {
                posGroup.getChildAt(i).setSelected(false);
            }
        }
    }
}
