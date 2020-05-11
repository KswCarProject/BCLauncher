package com.touchus.benchilauncher.utils;

import a_vcard.android.provider.Contacts;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.support.v4.view.accessibility.AccessibilityEventCompat;
import android.util.Log;
import com.touchus.benchilauncher.receiver.EcarReceiver;

public class EcarManager {
    public static final int BT_CALL_IDLE = 3;
    public static final int BT_CALL_OFFHOOK = 5;
    public static final int BT_CALL_RINGING = 4;
    public static final int BT_CONNECTED = 2;
    public static final int BT_DISCONNECT = 1;
    public static final int BT_OFF = 0;
    public static String ECAR_Action_RECV = "com.android.ecar.recv";
    public static String ECAR_Action_SEND = "com.android.ecar.send";
    public static String _CMD_ = "ecarSendKey";
    public static String _KEYS_ = "keySet";
    public static String _TYPE_ = "cmdType";
    public static String _TYPE_STANDCMD_ = "standCMD";
    public static String idleAciton = "com.ecar.call.idle";
    public static String inComingAction = "com.ecar.call.incoming";
    public static boolean isEcarCall = false;
    private static Context mContext;
    public static String talkingAcition = "com.ecar.call.offhook";
    private EcarReceiver ecarReceiver;

    public static final class EcarManagerInner {
        public static EcarManager instance = new EcarManager((EcarManager) null);
    }

    private EcarManager() {
    }

    /* synthetic */ EcarManager(EcarManager ecarManager) {
        this();
    }

    public static EcarManager getInstance(Context context) {
        mContext = context;
        return EcarManagerInner.instance;
    }

    public void startEcar() {
        Log.d("EcarLog", "startEcar");
        try {
            Intent intent = new Intent();
            intent.setComponent(new ComponentName("com.coagent.ecar", "com.coagent.ecarnet.car.activity.WelcomeActivity"));
            intent.addFlags(268435456);
            intent.addFlags(AccessibilityEventCompat.TYPE_TOUCH_INTERACTION_END);
            mContext.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void registerEcarReceiver() {
        this.ecarReceiver = new EcarReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ECAR_Action_SEND);
        intentFilter.addAction(inComingAction);
        intentFilter.addAction(talkingAcition);
        intentFilter.addAction(idleAciton);
        mContext.registerReceiver(this.ecarReceiver, intentFilter);
    }

    public void unregisterEcarReceiver() {
        if (this.ecarReceiver != null) {
            mContext.unregisterReceiver(this.ecarReceiver);
        }
    }

    public void sendBluetoothState(Context iContext, int iBluetoothState) {
        if (iContext != null) {
            String tmpState = String.valueOf(iBluetoothState);
            Intent tmpIntent = new Intent(ECAR_Action_RECV);
            tmpIntent.putExtra(_CMD_, "BluetoothState");
            tmpIntent.putExtra(_TYPE_, _TYPE_STANDCMD_);
            tmpIntent.putExtra(_KEYS_, "state");
            tmpIntent.putExtra("state", tmpState);
            iContext.sendBroadcast(tmpIntent);
            Log.d("EcarReceiver", "SendBluetoothState");
        }
    }

    public void sendAccOnToEcar() {
        Log.d("EcarLog", "sendAccOnToEcar");
        Intent intent = new Intent(ECAR_Action_RECV);
        intent.putExtra(_CMD_, "ACC_ON");
        intent.putExtra(_TYPE_, _TYPE_STANDCMD_);
        intent.putExtra(_KEYS_, "");
        mContext.sendBroadcast(intent);
    }

    public void sendAccOffEcar() {
        Log.d("EcarLog", "sendAccOffEcar");
        Intent intent = new Intent(ECAR_Action_RECV);
        intent.putExtra(_CMD_, "ACC_OFF");
        intent.putExtra(_TYPE_, _TYPE_STANDCMD_);
        intent.putExtra(_KEYS_, "");
        mContext.sendBroadcast(intent);
    }

    public void startEcarBootService() {
        Log.d("EcarLog", "startEcarBootService");
        ComponentName componet = new ComponentName("com.coagent.ecar", "com.coagent.ecarnet.car.service.BootService");
        Intent intent = new Intent();
        intent.setComponent(componet);
        mContext.stopService(intent);
        mContext.startService(intent);
    }

    public void startAppManagerService() {
        Log.d("EcarLog", "startAppManagerService");
        ComponentName componet = new ComponentName("com.ecar.AppManager", "com.ecar.AppManager.AppManagerService");
        Intent intent = new Intent();
        intent.setComponent(componet);
        mContext.stopService(intent);
        mContext.startService(intent);
    }

    public void startEcarRecevier() {
        Log.d("EcarLog", "startEcarRecevier");
        Intent intent = new Intent("com.android.ecar.recv");
        intent.putExtra("ecarSendKey", "StartECar");
        mContext.sendBroadcast(intent);
    }

    public void startEcarRenew() {
        Log.d("EcarLog", "startEcarRenew");
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.coagent.ecar", "com.coagent.ecarnet.car.activity.EcarRenewActivity"));
        intent.addFlags(268435456);
        mContext.startActivity(intent);
    }

    public void startEcarRenewReceiver() {
        Log.d("EcarLog", "startEcarRenewReceiver");
        Intent intent = new Intent("com.android.ecar.recv");
        intent.putExtra("ecarSendKey", "StartECarRenew");
        mContext.sendBroadcast(intent);
    }

    public void SendCallState(Context iContext, int iCallState) {
        if (iContext != null) {
            String tmpState = String.valueOf(iCallState);
            Intent tmpIntent = new Intent(ECAR_Action_RECV);
            tmpIntent.putExtra(_CMD_, "CallState");
            tmpIntent.putExtra(_TYPE_, _TYPE_STANDCMD_);
            tmpIntent.putExtra(_KEYS_, "state");
            tmpIntent.putExtra("state", tmpState);
            iContext.sendBroadcast(tmpIntent);
        }
    }

    public static void VoipMakeCall(Context iContext, String name, String number) {
        if (iContext != null && name != null && name.length() > 0 && number != null && number.length() > 0) {
            Intent tmpIntent = new Intent(ECAR_Action_RECV);
            tmpIntent.putExtra(_CMD_, "VoipMakeCall");
            tmpIntent.putExtra(_TYPE_, _TYPE_STANDCMD_);
            tmpIntent.putExtra(_KEYS_, "name,number");
            tmpIntent.putExtra(Contacts.PeopleColumns.NAME, name);
            tmpIntent.putExtra(Contacts.PhonesColumns.NUMBER, number);
            iContext.sendBroadcast(tmpIntent);
        }
    }

    public static void MakeCall(Context iContext) {
        if (iContext != null) {
            Intent tmpIntent = new Intent(ECAR_Action_RECV);
            tmpIntent.putExtra(_CMD_, "MakeCall");
            tmpIntent.putExtra(_TYPE_, _TYPE_STANDCMD_);
            tmpIntent.putExtra(_KEYS_, "");
            iContext.sendBroadcast(tmpIntent);
        }
    }

    public void startOneKeyActivity() {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.coagent.ecar", "com.coagent.ecarnet.car.activity.OneKeyActivity"));
        intent.addFlags(268435456);
        mContext.startActivity(intent);
    }

    public void startVoipActivity() {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.coagent.voip", "com.coagent.voip.VOIPFragmentActivity"));
        intent.addFlags(268435456);
        mContext.startActivity(intent);
    }

    public void startNaviFromEcar(String poiName, String iLon, String iLat) {
        Log.d("ecarlog", "poiName:" + poiName + " iLon: " + iLon + " iLat: " + iLat);
        Intent intent1 = new Intent("android.intent.action.VIEW", Uri.parse("androidauto://navi?sourceApplication=NotifyReceiverService&poiname=" + poiName + "&lat=" + iLat + "&lon=" + iLon + "&dev=0&style=0"));
        intent1.addCategory("android.intent.category.DEFAULT");
        intent1.setPackage("com.autonavi.amapauto");
        intent1.addFlags(268435456);
        mContext.startActivity(intent1);
    }
}
