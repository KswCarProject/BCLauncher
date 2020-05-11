package com.touchus.benchilauncher.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import com.touchus.benchilauncher.LauncherApplication;
import com.touchus.benchilauncher.SysConst;
import com.touchus.publicutils.sysconst.VoiceAssistantConst;

public class ChangeMenuReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        LauncherApplication app = (LauncherApplication) context.getApplicationContext();
        if (app.launcherHandler != null) {
            String type = intent.getStringExtra(VoiceAssistantConst.FLAG_MAIN_CMD);
            int pos = 0;
            if (VoiceAssistantConst.FLAG_MAIN_NAVI.equals(type)) {
                pos = 0;
            } else if (VoiceAssistantConst.FLAG_MAIN_RADIO.equals(type) || VoiceAssistantConst.FLAG_MAIN_ORIGINAL.equals(type)) {
                pos = 1;
            } else if (VoiceAssistantConst.FLAG_MAIN_MEDIA.equals(type)) {
                pos = 2;
            } else if (VoiceAssistantConst.FLAG_MAIN_PHONE.equals(type)) {
                pos = 3;
            } else if (VoiceAssistantConst.FLAG_MAIN_VOICE.equals(type)) {
                pos = (Build.MODEL.equals("c200") || Build.MODEL.equals("c200_lsx") || Build.MODEL.equals("benz")) ? 4 : Build.MODEL.equals("c200_ks") ? 7 : 5;
            } else if (VoiceAssistantConst.FLAG_MAIN_INSTRUMENT.equals(type)) {
                pos = (Build.MODEL.equals("c200") || Build.MODEL.equals("benz") || Build.MODEL.equals("c200_ks")) ? 5 : 4;
            } else if (VoiceAssistantConst.FLAG_MAIN_CARLIFE.equals(type)) {
                if (Build.MODEL.equals("c200") || Build.MODEL.equals("benz") || Build.MODEL.equals("c200_zlh") || Build.MODEL.equals("c200_ks")) {
                    pos = 6;
                }
            } else if (VoiceAssistantConst.FLAG_MAIN_RECORD.equals(type)) {
                if (Build.MODEL.equals("c200_jly") || Build.MODEL.equals("c200_hy") || Build.MODEL.equals("c200_psr")) {
                    pos = 6;
                }
            } else if (VoiceAssistantConst.FLAG_MAIN_ECAR.equals(type)) {
                if (Build.MODEL.equals("c200_ks")) {
                    pos = 4;
                }
            } else if (VoiceAssistantConst.FLAG_MAIN_APP.equals(type)) {
                pos = 7;
            } else if (VoiceAssistantConst.FLAG_MAIN_SETTING.equals(type)) {
                pos = 8;
            } else if (VoiceAssistantConst.FLAG_MAIN_CLOSE.equals(type)) {
                pos = 9;
            } else {
                return;
            }
            app.serviceHandler.obtainMessage(SysConst.VOICE_WAKEUP_MENU, Integer.valueOf(pos)).sendToTarget();
        }
    }
}
