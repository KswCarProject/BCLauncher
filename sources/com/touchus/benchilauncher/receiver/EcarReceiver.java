package com.touchus.benchilauncher.receiver;

import a_vcard.android.provider.Contacts;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import com.touchus.benchilauncher.LauncherApplication;
import com.touchus.benchilauncher.SysConst;
import com.touchus.benchilauncher.utils.EcarManager;
import com.touchus.benchilauncher.views.FloatSystemCallDialog;

public class EcarReceiver extends BroadcastReceiver {
    private String BluetoothConnect = "BluetoothConnect";
    private String BluetoothEndCall = "BluetoothEndCall";
    private String BluetoothMakeCall = "BluetoothMakeCall";
    private String HideCallUI = "HideCallUI";
    private LauncherApplication app;
    private String btStateQurey = "BluetoothQueryState";
    private EcarManager ecarManager;
    private Context mContext;
    private String naviCmd = "StartMap";
    private String tag = "EcarReceiver";
    private String ttsSpeak = "TTSSpeak";

    public void onReceive(Context context, Intent intent) {
        this.mContext = context;
        if (this.app == null) {
            this.app = (LauncherApplication) context.getApplicationContext();
        }
        if (this.ecarManager == null) {
            this.ecarManager = EcarManager.getInstance(context);
        }
        String action = intent.getAction();
        Log.e(this.tag, "action : " + action);
        if (EcarManager.ECAR_Action_SEND.equals(action)) {
            String cmd = intent.getStringExtra(EcarManager._CMD_);
            Log.e(this.tag, "ECAR_Action_SEND cmd : " + cmd);
            if (this.naviCmd.equals(cmd)) {
                String pointName = intent.getStringExtra("gaode_poiName");
                String iLat = intent.getStringExtra("gaode_latitude");
                this.ecarManager.startNaviFromEcar(pointName, intent.getStringExtra("gaode_longitude"), iLat);
            } else if (this.ttsSpeak.equals(cmd)) {
            } else {
                if (this.btStateQurey.equals(cmd)) {
                    if (LauncherApplication.isBlueConnectState) {
                        this.ecarManager.sendBluetoothState(context, 2);
                    } else {
                        this.ecarManager.sendBluetoothState(context, 1);
                    }
                } else if (this.BluetoothMakeCall.equals(cmd)) {
                    Log.e(this.tag, "name:" + intent.getStringExtra(Contacts.PeopleColumns.NAME));
                    String phonenum = intent.getStringExtra(Contacts.PhonesColumns.NUMBER);
                    if (this.app.btservice != null && LauncherApplication.isBlueConnectState) {
                        if (!TextUtils.isEmpty(phonenum)) {
                            EcarManager.isEcarCall = true;
                            this.app.btservice.CallOut(phonenum);
                            this.ecarManager.SendCallState(context, 4);
                            return;
                        }
                        Log.e(this.tag, "phonenum is null.");
                    }
                } else if (this.BluetoothEndCall.equals(cmd)) {
                    LauncherApplication.getInstance().isCalling = false;
                    EcarManager.isEcarCall = false;
                    this.app.btservice.cutdownCurrentCalling();
                    this.ecarManager.SendCallState(context, 3);
                } else if (this.BluetoothConnect.equals(cmd)) {
                    if (!LauncherApplication.isBlueConnectState) {
                        this.app.serviceHandler.obtainMessage(SysConst.VOICE_WAKEUP_MENU, 3).sendToTarget();
                    }
                } else if (this.HideCallUI.equals(cmd)) {
                    String tmp = intent.getStringExtra("oper");
                    if (TextUtils.isEmpty(tmp)) {
                        return;
                    }
                    if (tmp.equals("hide")) {
                        if (!FloatSystemCallDialog.getInstance().isDestory()) {
                            FloatSystemCallDialog.getInstance().dissShow();
                        }
                    } else if (tmp.equals("show") && FloatSystemCallDialog.getInstance().isDestory()) {
                        FloatSystemCallDialog.getInstance().show();
                    }
                }
            }
        } else if (EcarManager.talkingAcition.equals(action) || EcarManager.inComingAction.equals(action)) {
            LauncherApplication.getInstance().isCalling = true;
            this.app.btservice.requestAudioFocus();
        } else if (EcarManager.idleAciton.equals(action)) {
            LauncherApplication.getInstance().isCalling = false;
            this.app.btservice.unrequestAudioFocus();
        }
    }
}
