package com.touchus.benchilauncher.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import com.touchus.benchilauncher.LauncherApplication;

public class SystemTimeReceiver extends BroadcastReceiver {
    private final String tag = "SystemTimeReceiver";

    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        LauncherApplication app = (LauncherApplication) context.getApplicationContext();
        if (app.isOriginalTime) {
            Log.e("SystemTimeReceiver", "isOriginalTime : true. ");
        } else if ("android.intent.action.TIME_SET".equals(action) || "android.intent.action.TIME_TICK".equals(action) || "android.intent.action.DATE_CHANGED".equals(action)) {
            Time t = new Time();
            t.setToNow();
            app.year = t.year;
            app.month = t.month + 1;
            app.day = t.monthDay;
            app.hour = t.hour;
            app.min = t.minute;
            app.second = t.second;
            Log.d("launcherlog", "location : t = " + t.timezone);
            app.sendMessage(1009, (Bundle) null);
        }
    }
}
