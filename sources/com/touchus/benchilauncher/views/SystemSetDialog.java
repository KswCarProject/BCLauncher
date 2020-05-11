package com.touchus.benchilauncher.views;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import com.backaudio.android.driver.Mainboard;
import com.touchus.benchilauncher.LauncherApplication;
import com.touchus.benchilauncher.R;
import com.touchus.benchilauncher.SysConst;
import com.touchus.benchilauncher.adapter.SystemSetItemAdapter;
import com.touchus.benchilauncher.bean.SettingInfo;
import com.touchus.benchilauncher.utils.SpUtilK;
import com.touchus.publicutils.sysconst.BenzModel;
import com.touchus.publicutils.view.ListViewForScrollView;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class SystemSetDialog extends Dialog implements AdapterView.OnItemClickListener {
    private Context context;
    private int currentSelectIndex = 0;
    private LauncherApplication mApp;
    public MHandler mHandler = new MHandler(this);
    private SystemSetItemAdapter mSetAdapter;
    private ListViewForScrollView mSetListView;
    private SpUtilK mSpUtilK;
    private SystemSetItemAdapter mSysAdapter;
    private ListViewForScrollView mSysListView;
    private List<SettingInfo> setInfos = new ArrayList();
    private List<SettingInfo> sysInfos = new ArrayList();
    private int type = 1;

    public SystemSetDialog(Context context2) {
        super(context2);
        this.context = context2;
    }

    public SystemSetDialog(Context context2, int themeResId) {
        super(context2, themeResId);
        this.context = context2;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.dialog_system_layout);
        this.mApp = (LauncherApplication) this.context.getApplicationContext();
        this.mSpUtilK = new SpUtilK(this.context);
        initData();
        initView();
        super.onCreate(savedInstanceState);
    }

    private void initView() {
        this.mSetListView = (ListViewForScrollView) findViewById(R.id.setlist);
        this.mSetAdapter = new SystemSetItemAdapter(this.context, this.setInfos);
        this.mSetListView.setAdapter(this.mSetAdapter);
        this.mSetListView.setOnItemClickListener(this);
        this.mSysListView = (ListViewForScrollView) findViewById(R.id.systemlist);
        this.mSysAdapter = new SystemSetItemAdapter(this.context, this.sysInfos);
        this.mSysListView.setAdapter(this.mSysAdapter);
        this.mSysListView.setOnItemClickListener(this);
        this.mSetAdapter.setSeclectIndex(0);
        this.mSysAdapter.setSeclectIndex(-1);
    }

    private void initData() {
        this.mApp.isVocieWakeup = this.mSpUtilK.getBoolean(this.context.getString(R.string.string_vwakeup), false);
        this.mApp.isSpeechKeyOpen = this.mSpUtilK.getBoolean(this.context.getString(R.string.string_voice_setting), false);
        this.mApp.isOriginalKeyOpen = this.mSpUtilK.getBoolean(this.context.getString(R.string.string_original_setting), false);
        this.mApp.ismix = this.mSpUtilK.getBoolean(this.context.getString(R.string.string_navi_mix), true);
        this.mApp.isAirhide = this.mSpUtilK.getBoolean(this.context.getString(R.string.string_air_hide), false);
        this.mApp.isGestureSwitch = this.mSpUtilK.getBoolean(this.context.getString(R.string.string_gesture_switch), true);
        Log.e("", "SystemSetDialog mApp.isVocieWakeup = " + this.mApp.isVocieWakeup + ",mApp.isSpeechKeyOpen = " + this.mApp.isSpeechKeyOpen + ",mApp.isOriginalKeyOpen = " + this.mApp.isOriginalKeyOpen);
        if (this.mApp.playpos != 1 && !SysConst.isBT()) {
            this.setInfos.add(new SettingInfo(this.context.getString(R.string.string_navi_mix), true, this.mApp.ismix));
        }
        if (SysConst.LANGUAGE == 0) {
            this.setInfos.add(new SettingInfo(this.context.getString(R.string.string_vwakeup), true, this.mApp.isVocieWakeup));
            this.setInfos.add(new SettingInfo(this.context.getString(R.string.string_voice_setting), true, this.mApp.isSpeechKeyOpen));
        }
        if (BenzModel.benzCan == BenzModel.EBenzCAN.ZMYT && (Build.MODEL.equals("c200") || Build.MODEL.equals("c200_en"))) {
            this.setInfos.add(new SettingInfo(this.context.getString(R.string.string_original_setting), true, this.mApp.isOriginalKeyOpen));
        }
        this.setInfos.add(new SettingInfo(this.context.getString(R.string.string_air_hide), true, this.mApp.isAirhide));
        this.sysInfos.add(new SettingInfo(this.context.getString(R.string.msg_factory_set), false));
        this.sysInfos.add(new SettingInfo(this.context.getString(R.string.string_reboot_system), false));
        this.sysInfos.add(new SettingInfo(this.context.getString(R.string.string_restore_factory), false));
    }

    /* access modifiers changed from: protected */
    public void onStart() {
        this.mApp.registerHandler(this.mHandler);
        super.onStart();
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        this.mHandler.removeCallbacksAndMessages((Object) null);
        this.mApp.unregisterHandler(this.mHandler);
        super.onStop();
    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.setlist:
                this.type = 1;
                pressItem(position);
                return;
            case R.id.systemlist:
                this.type = 2;
                pressItem(position);
                return;
            default:
                return;
        }
    }

    private void pressItem(int position) {
        boolean z;
        int i;
        boolean z2;
        int i2;
        boolean z3;
        int i3;
        boolean z4;
        int i4;
        boolean z5;
        int i5;
        boolean z6;
        int i6;
        boolean z7 = false;
        this.currentSelectIndex = position;
        Log.e("", "SystemSetDialog type = " + this.type + ",mApp.isVocieWakeup = " + this.mApp.isVocieWakeup + ",mApp.isSpeechKeyOpen = " + this.mApp.isSpeechKeyOpen + ",mApp.isOriginalKeyOpen = " + this.mApp.isOriginalKeyOpen);
        if (this.type == 1) {
            String itemString = this.setInfos.get(position).getItem();
            boolean iCheck = this.mSpUtilK.getBoolean(itemString, false);
            if (itemString.equals(this.context.getString(R.string.string_vwakeup))) {
                LauncherApplication launcherApplication = this.mApp;
                if (iCheck) {
                    z6 = false;
                } else {
                    z6 = true;
                }
                launcherApplication.isVocieWakeup = z6;
                this.mSpUtilK.putBoolean(itemString, this.mApp.isVocieWakeup);
                this.mApp.service.sendVoiceWakeupState(this.mApp.isVocieWakeup);
                byte[] bArr = SysConst.storeData;
                if (this.mApp.isVocieWakeup) {
                    i6 = 1;
                } else {
                    i6 = 0;
                }
                bArr[2] = (byte) i6;
            } else if (itemString.equals(this.context.getString(R.string.string_voice_setting))) {
                LauncherApplication launcherApplication2 = this.mApp;
                if (iCheck) {
                    z5 = false;
                } else {
                    z5 = true;
                }
                launcherApplication2.isSpeechKeyOpen = z5;
                this.mSpUtilK.putBoolean(itemString, this.mApp.isSpeechKeyOpen);
                byte[] bArr2 = SysConst.storeData;
                if (this.mApp.isSpeechKeyOpen) {
                    i5 = 1;
                } else {
                    i5 = 0;
                }
                bArr2[1] = (byte) i5;
            } else if (itemString.equals(this.context.getString(R.string.string_original_setting))) {
                LauncherApplication launcherApplication3 = this.mApp;
                if (iCheck) {
                    z4 = false;
                } else {
                    z4 = true;
                }
                launcherApplication3.isOriginalKeyOpen = z4;
                this.mSpUtilK.putBoolean(itemString, this.mApp.isOriginalKeyOpen);
                byte[] bArr3 = SysConst.storeData;
                if (this.mApp.isOriginalKeyOpen) {
                    i4 = 1;
                } else {
                    i4 = 0;
                }
                bArr3[0] = (byte) i4;
            } else if (itemString.equals(this.context.getString(R.string.string_navi_mix))) {
                LauncherApplication launcherApplication4 = this.mApp;
                if (iCheck) {
                    z3 = false;
                } else {
                    z3 = true;
                }
                launcherApplication4.ismix = z3;
                this.mSpUtilK.putBoolean(itemString, this.mApp.ismix);
                byte[] bArr4 = SysConst.storeData;
                if (this.mApp.ismix) {
                    i3 = 0;
                } else {
                    i3 = 1;
                }
                bArr4[3] = (byte) i3;
            } else if (itemString.equals(this.context.getString(R.string.string_air_hide))) {
                LauncherApplication launcherApplication5 = this.mApp;
                if (iCheck) {
                    z2 = false;
                } else {
                    z2 = true;
                }
                launcherApplication5.isAirhide = z2;
                this.mSpUtilK.putBoolean(itemString, this.mApp.isAirhide);
                byte[] bArr5 = SysConst.storeData;
                if (this.mApp.isAirhide) {
                    i2 = 1;
                } else {
                    i2 = 0;
                }
                bArr5[8] = (byte) i2;
            } else if (itemString.equals(this.context.getString(R.string.string_gesture_switch))) {
                LauncherApplication launcherApplication6 = this.mApp;
                if (iCheck) {
                    z = false;
                } else {
                    z = true;
                }
                launcherApplication6.isGestureSwitch = z;
                this.mSpUtilK.putBoolean(itemString, this.mApp.isGestureSwitch);
                byte[] bArr6 = SysConst.storeData;
                if (this.mApp.isGestureSwitch) {
                    i = 0;
                } else {
                    i = 1;
                }
                bArr6[11] = (byte) i;
                Mainboard.getInstance().setiForbidGesture(this.mApp.isGestureSwitch);
            }
            SettingInfo settingInfo = this.setInfos.get(position);
            if (!iCheck) {
                z7 = true;
            }
            settingInfo.setChecked(z7);
            Mainboard.getInstance().sendStoreDataToMcu(SysConst.storeData);
            this.mSetAdapter.setSeclectIndex(position);
            this.mSysAdapter.setSeclectIndex(-1);
            return;
        }
        String itemString2 = this.sysInfos.get(position).getItem();
        if (itemString2.equals(this.context.getString(R.string.string_reboot_system))) {
            showRecoveryDlg(itemString2);
        } else if (itemString2.equals(this.context.getString(R.string.string_restore_factory))) {
            showRecoveryDlg(itemString2);
        } else if (itemString2.equals(this.context.getString(R.string.msg_factory_set))) {
            FactorySetDialog wifiialog = new FactorySetDialog(this.context, R.style.Dialog);
            wifiialog.width = 500;
            wifiialog.height = 200;
            wifiialog.show();
        }
        this.mSetAdapter.setSeclectIndex(-1);
        this.mSysAdapter.setSeclectIndex(position);
    }

    private void showRecoveryDlg(String itemString) {
        SystemOperateDialog dialog = new SystemOperateDialog(this.context, R.style.Dialog);
        dialog.setTitle(itemString);
        dialog.show();
    }

    static class MHandler extends Handler {
        private WeakReference<SystemSetDialog> target;

        public MHandler(SystemSetDialog activity) {
            this.target = new WeakReference<>(activity);
        }

        public void handleMessage(Message msg) {
            if (this.target.get() != null) {
                ((SystemSetDialog) this.target.get()).handlerMsg(msg);
            }
        }
    }

    public void handlerMsg(Message msg) {
        if (msg.what == 6001) {
            byte idriverCode = msg.getData().getByte(SysConst.IDRIVER_ENUM);
            if (idriverCode == Mainboard.EIdriverEnum.TURN_RIGHT.getCode() || idriverCode == Mainboard.EIdriverEnum.DOWN.getCode()) {
                if (this.type == 1 && this.currentSelectIndex < this.setInfos.size() - 1) {
                    this.currentSelectIndex++;
                } else if (this.type == 1 && this.currentSelectIndex == this.setInfos.size() - 1) {
                    this.type++;
                    this.currentSelectIndex = 0;
                } else if (this.type == 2 && this.currentSelectIndex < this.sysInfos.size() - 1) {
                    this.currentSelectIndex++;
                }
                seclectTrue(this.currentSelectIndex);
            } else if (idriverCode == Mainboard.EIdriverEnum.TURN_LEFT.getCode() || idriverCode == Mainboard.EIdriverEnum.UP.getCode()) {
                if (this.type == 2 && this.currentSelectIndex > 0) {
                    this.currentSelectIndex--;
                } else if (this.type == 2 && this.currentSelectIndex == 0) {
                    this.type--;
                    this.currentSelectIndex = this.setInfos.size() - 1;
                } else if (this.type == 1 && this.currentSelectIndex > 0) {
                    this.currentSelectIndex--;
                }
                seclectTrue(this.currentSelectIndex);
            } else if (idriverCode == Mainboard.EIdriverEnum.PRESS.getCode()) {
                pressItem(this.currentSelectIndex);
            } else if (idriverCode == Mainboard.EIdriverEnum.BACK.getCode() || idriverCode == Mainboard.EIdriverEnum.HOME.getCode() || idriverCode == Mainboard.EIdriverEnum.STAR_BTN.getCode()) {
                dismiss();
            }
        }
    }

    private void seclectTrue(int currentSelectIndex2) {
        if (this.type == 1) {
            this.mSetAdapter.setSeclectIndex(currentSelectIndex2);
            this.mSysAdapter.setSeclectIndex(-1);
        } else if (this.type == 2) {
            this.mSetAdapter.setSeclectIndex(-1);
            this.mSysAdapter.setSeclectIndex(currentSelectIndex2);
        }
    }
}
