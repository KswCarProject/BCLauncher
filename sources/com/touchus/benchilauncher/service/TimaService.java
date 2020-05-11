package com.touchus.benchilauncher.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import com.broadfly.settings.ISettingsService;
import com.touchus.benchilauncher.LauncherApplication;

public class TimaService extends Service {
    LauncherApplication mApp;

    public void onCreate() {
        super.onCreate();
        this.mApp = (LauncherApplication) getApplication();
        Log.e("", "TimaService onCreate");
    }

    public IBinder onBind(Intent intent) {
        return new TimaBinder();
    }

    public void onDestroy() {
        super.onDestroy();
    }

    public class TimaBinder extends ISettingsService.Stub {
        public TimaBinder() {
        }

        public TimaService getService() {
            return TimaService.this;
        }

        public boolean requestBTFocus() throws RemoteException {
            Log.e("", "TimaService requestBTFocus getIsBTState = " + TimaService.this.mApp.service.getIsBTState());
            if (!TimaService.this.mApp.service.getIsBTState()) {
                TimaService.this.mApp.service.btMusicConnect();
            }
            TimaService.this.mApp.btservice.requestMusicAudioFocus();
            return TimaService.this.mApp.service.getIsBTState();
        }

        public boolean abandonBTFocus() throws RemoteException {
            Log.e("", "TimaService abandonBTFocus");
            TimaService.this.mApp.btservice.unrequestMusicAudioFocus();
            TimaService.this.onDestroy();
            return TimaService.this.mApp.btservice.blueMusicFocus;
        }

        public void takePhone(String number) throws RemoteException {
            Log.e("", "TimaService takePhone" + number);
            TimaService.this.mApp.btservice.CallOut(number);
        }

        public boolean isBTconnect() throws RemoteException {
            Log.e("", "TimaService isBTconnect " + LauncherApplication.isBlueConnectState);
            return LauncherApplication.isBlueConnectState;
        }

        public String getBTname() throws RemoteException {
            Log.e("", "TimaService getBTname " + BluetoothService.deviceName);
            return BluetoothService.deviceName;
        }

        public String getPINCode() throws RemoteException {
            Log.e("", "TimaService getPINCode " + BluetoothService.devicePIN);
            return BluetoothService.devicePIN;
        }

        public String getBTmacAddr() throws RemoteException {
            Log.e("", "TimaService getBTmacAddr " + BluetoothService.deviceMAC);
            return BluetoothService.deviceMAC;
        }
    }
}
