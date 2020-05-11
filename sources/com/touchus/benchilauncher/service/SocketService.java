package com.touchus.benchilauncher.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import com.touchus.benchilauncher.LauncherApplication;
import com.touchus.benchilauncher.utils.ServerFinally;
import java.lang.ref.WeakReference;

public class SocketService extends Service {
    LauncherApplication mApp;
    private Mhandler mHandler = new Mhandler(this);
    ServerFinally server;

    public void onCreate() {
        super.onCreate();
        this.mApp = (LauncherApplication) getApplication();
        Log.e("", "SocketService onCreate");
        this.server = new ServerFinally(this.mHandler);
        new Thread(this.server).start();
    }

    public void handlerMsg(Message msg) {
        if (msg.what == 17) {
            this.mApp.requestPAPAGOAudioFocus();
        } else if (msg.what == 18) {
            this.mApp.abandonPAPAGOAudioFocus();
        }
    }

    static class Mhandler extends Handler {
        private WeakReference<SocketService> target;

        public Mhandler(SocketService activity) {
            this.target = new WeakReference<>(activity);
        }

        public void handleMessage(Message msg) {
            if (this.target.get() != null) {
                ((SocketService) this.target.get()).handlerMsg(msg);
            }
        }
    }

    public IBinder onBind(Intent intent) {
        return new MainBinder();
    }

    public void onDestroy() {
        super.onDestroy();
    }

    public class MainBinder extends Binder {
        public MainBinder() {
        }

        public SocketService getService() {
            return SocketService.this;
        }
    }
}
