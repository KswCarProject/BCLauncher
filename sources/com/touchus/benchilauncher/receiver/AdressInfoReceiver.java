package com.touchus.benchilauncher.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.touchus.benchilauncher.LauncherApplication;
import com.touchus.publicutils.utils.CrashHandler;

public class AdressInfoReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        LauncherApplication app = (LauncherApplication) context.getApplicationContext();
        if (app != null) {
            app.address = intent.getStringExtra("addr");
            app.gpsCityName = intent.getStringExtra("cityName");
            CrashHandler.getInstance(context).setAddress(app.address);
            Log.e("AdressInfoReceiver", "address  = " + app.address + ",city = " + app.gpsCityName);
        }
    }
}
