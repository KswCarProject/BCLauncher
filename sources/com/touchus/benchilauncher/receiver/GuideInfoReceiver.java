package com.touchus.benchilauncher.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.sun.mail.iap.Response;
import com.touchus.benchilauncher.LauncherApplication;
import com.touchus.benchilauncher.SysConst;

public class GuideInfoReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        LauncherApplication app = (LauncherApplication) context.getApplicationContext();
        if (app != null && SysConst.LANGUAGE <= 1) {
            Log.e("GuideInfoReceiver", "guideInfo coming " + intent.getIntExtra("KEY_TYPE", 0) + ",intent.getExtras = " + intent.getExtras());
            if (intent.getIntExtra("KEY_TYPE", 0) == 10001) {
                Log.e("GuideInfoReceiver", "guideInfo coming " + intent.getExtras());
                LauncherApplication.pageCount = 5;
                app.sendMessage(SysConst.UPDATE_NAVI, intent.getExtras());
            } else if (intent.getIntExtra("KEY_TYPE", 0) == 10019) {
                switch (intent.getIntExtra("EXTRA_STATE", 0)) {
                    case 8:
                    case 10:
                        LauncherApplication.pageCount = 5;
                        app.sendMessage(SysConst.UPDATE_NAVI_START, (Bundle) null);
                        return;
                    case 9:
                    case Response.BAD:
                        LauncherApplication.pageCount = 4;
                        app.sendMessage(SysConst.UPDATE_NAVI_END, (Bundle) null);
                        return;
                    case 39:
                        app.sendMessage(SysConst.UPDATE_NAVI_END, (Bundle) null);
                        return;
                    default:
                        return;
                }
            }
        }
    }
}
