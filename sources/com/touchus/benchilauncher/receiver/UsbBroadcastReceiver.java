package com.touchus.benchilauncher.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.touchus.benchilauncher.LauncherApplication;
import com.touchus.benchilauncher.SysConst;
import com.touchus.benchilauncher.service.MusicLoader;
import com.touchus.benchilauncher.service.PictrueUtil;
import com.touchus.publicutils.media.VideoService;

public class UsbBroadcastReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        LauncherApplication app = (LauncherApplication) context.getApplicationContext();
        if (!"android.intent.action.MEDIA_CHECKING".equals(action) && !"android.intent.action.MEDIA_MOUNTED".equals(action)) {
            if ("android.intent.action.MEDIA_EJECT".equals(action)) {
                LauncherApplication.musicList.clear();
                LauncherApplication.videoList.clear();
                LauncherApplication.imageList.clear();
            } else if ("android.intent.action.MEDIA_SCANNER_STARTED".equals(action)) {
                app.isScanner = true;
            } else if ("android.intent.action.MEDIA_UNMOUNTED".equals(action)) {
                app.isScanner = false;
            } else if ("android.intent.action.MEDIA_SCANNER_FINISHED".equals(action)) {
                LauncherApplication.musicList.clear();
                LauncherApplication.videoList.clear();
                LauncherApplication.imageList.clear();
                LauncherApplication.musicList.addAll(MusicLoader.instance(context.getContentResolver()).getMusicList());
                LauncherApplication.videoList.addAll(new VideoService(context).getVideoList());
                LauncherApplication.imageList.addAll(new PictrueUtil(context).getPicList());
                app.isScanner = false;
            }
        }
        Bundle bundle = new Bundle();
        bundle.putString(SysConst.FLAG_USB_LISTENER, action);
        app.sendMessage(SysConst.USB_LISTENER, bundle);
    }
}
