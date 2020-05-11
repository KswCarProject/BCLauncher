package com.touchus.benchilauncher.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.touchus.benchilauncher.LauncherApplication;
import com.touchus.benchilauncher.SysConst;

public class PapagoAudioReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        LauncherApplication app = (LauncherApplication) context.getApplicationContext();
        if (app != null) {
            Log.e("PapagoAudioReceiver", "PAPAGO Action  = " + intent.getAction());
            if (intent.getAction().equals(SysConst.EVENT_UNIBROAD_PAPAGOGAIN)) {
                app.requestPAPAGOAudioFocus();
            } else if (intent.getAction().equals(SysConst.EVENT_UNIBROAD_PAPAGOLOSS)) {
                app.abandonPAPAGOAudioFocus();
            }
        }
    }
}
