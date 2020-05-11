package com.touchus.benchilauncher.activity.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.internal.view.SupportMenu;
import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.touchus.benchilauncher.Launcher;
import com.touchus.benchilauncher.LauncherApplication;
import com.touchus.benchilauncher.R;
import com.touchus.benchilauncher.SysConst;
import com.touchus.benchilauncher.utils.MobileDataSwitcher;
import java.io.File;
import java.lang.ref.WeakReference;

public class StateFrag extends Fragment {
    Launcher activity;
    private LauncherApplication app;
    /* access modifiers changed from: private */
    public Runnable checkWifiSignRunnable = new Runnable() {
        public void run() {
            StateFrag.this.mStateHandler.removeCallbacks(StateFrag.this.checkWifiSignRunnable);
            StateFrag.this.mStateHandler.postDelayed(StateFrag.this.checkWifiSignRunnable, 2000);
            StateFrag.this.checkWifiSign();
        }
    };
    ImageView mBt;
    TextView mDate;
    ImageView mGps;
    ImageView mSdcard;
    ImageView mSignal;
    StateHandler mStateHandler = new StateHandler(this);
    TextView mTime;
    ImageView mUsb3;
    UsbBroadcastReceiver mUsbReceiver;
    ImageView mWifi;
    private TelephonyManager telManager;
    View view;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.main_state_bar, container, false);
        this.app = (LauncherApplication) getActivity().getApplication();
        findID();
        initDate();
        this.telManager = (TelephonyManager) getActivity().getSystemService("phone");
        this.telManager.listen(new PhoneStateMonitor(), 257);
        return this.view;
    }

    private void initDate() {
        stateSignal(this.mSignal, 0);
        stateWifi(this.mWifi, 0);
        setBt();
        checkUsb3();
        setGPS();
    }

    public void onStart() {
        super.onStart();
        this.app.registerHandler(this.mStateHandler);
        this.mStateHandler.postDelayed(this.checkWifiSignRunnable, 2000);
        registerReceiver();
    }

    public void onStop() {
        super.onStop();
        unregisterReceiver();
        this.app.registerHandler(this.mStateHandler);
        this.mStateHandler.removeCallbacks(this.checkWifiSignRunnable);
    }

    public void onDestroy() {
        super.onDestroy();
    }

    private void findID() {
        this.activity = (Launcher) getActivity();
        this.mSignal = (ImageView) this.view.findViewById(R.id.state_bar_signal);
        this.mWifi = (ImageView) this.view.findViewById(R.id.state_bar_wifi);
        this.mBt = (ImageView) this.view.findViewById(R.id.state_bar_bluetooth);
        this.mGps = (ImageView) this.view.findViewById(R.id.state_bar_gps);
        this.mSdcard = (ImageView) this.view.findViewById(R.id.state_bar_sd);
        this.mUsb3 = (ImageView) this.view.findViewById(R.id.state_bar_usb3);
        this.mTime = (TextView) this.view.findViewById(R.id.state_time);
        this.mDate = (TextView) this.view.findViewById(R.id.state_date);
    }

    public void setTimeColor(boolean isRed) {
        if (!SysConst.DEBUG) {
            return;
        }
        if (this.app.service.iIsInAndroid) {
            this.mTime.setTextColor(-1);
        } else {
            this.mTime.setTextColor(SupportMenu.CATEGORY_MASK);
        }
    }

    public void setSignal(int state) {
        stateSignal(this.mSignal, state);
    }

    public void setWifi(int state) {
        stateWifi(this.mWifi, state);
    }

    public void setBt() {
        if (getActivity() != null) {
            if (LauncherApplication.isBlueConnectState) {
                stateImg(this.mBt, 1);
            } else {
                stateImg(this.mBt, 0);
            }
        }
    }

    public void setGPS() {
        if (getActivity() != null) {
            if (LauncherApplication.isGPSLocation) {
                stateImg(this.mGps, 1);
            } else {
                stateImg(this.mGps, 0);
            }
        }
    }

    public void setTime(String desc) {
        this.mTime.setText(desc);
    }

    public void setDate(String desc) {
        this.mDate.setText(desc);
    }

    public void stateSignal(ImageView imgV, int state) {
        if (MobileDataSwitcher.getMobileDataState(this.activity)) {
            switch (state) {
                case 0:
                    imgV.setImageResource(R.drawable.top_4g0);
                    return;
                case 1:
                    imgV.setImageResource(R.drawable.top_4g1);
                    return;
                case 2:
                    imgV.setImageResource(R.drawable.top_4g2);
                    return;
                case 3:
                    imgV.setImageResource(R.drawable.top_4g3);
                    return;
                case 4:
                    imgV.setImageResource(R.drawable.top_4g4);
                    return;
                default:
                    return;
            }
        } else {
            switch (state) {
                case 0:
                    imgV.setImageResource(R.drawable.top_4g0);
                    return;
                case 1:
                    imgV.setImageResource(R.drawable.top_4g1n);
                    return;
                case 2:
                    imgV.setImageResource(R.drawable.top_4g2n);
                    return;
                case 3:
                    imgV.setImageResource(R.drawable.top_4g3n);
                    return;
                case 4:
                    imgV.setImageResource(R.drawable.top_4g4n);
                    return;
                default:
                    return;
            }
        }
    }

    public void stateWifi(ImageView imgV, int state) {
        switch (state) {
            case 0:
                imgV.setImageResource(R.drawable.top_wifi0);
                return;
            case 1:
                imgV.setImageResource(R.drawable.top_wifi1);
                return;
            case 2:
                imgV.setImageResource(R.drawable.top_wifi2);
                return;
            case 3:
                imgV.setImageResource(R.drawable.top_wifi3);
                return;
            case 4:
                imgV.setImageResource(R.drawable.top_wifi4);
                return;
            default:
                return;
        }
    }

    private void stateImg(ImageView imgV, int state) {
        switch (state) {
            case 0:
                imgV.setSelected(false);
                return;
            case 1:
                imgV.setSelected(true);
                return;
            default:
                return;
        }
    }

    private void setTimeInfo(Bundle data) {
        String tTime;
        int year = this.app.year;
        int month = this.app.month;
        int day = this.app.day;
        int time = this.app.hour;
        int min = this.app.min;
        String tMonth = new StringBuilder().append(month).toString();
        if (month < 10) {
            tMonth = "0" + month;
        }
        String tDay = new StringBuilder().append(day).toString();
        if (day < 10) {
            tDay = "0" + day;
        }
        String tMin = new StringBuilder().append(min).toString();
        if (min < 10) {
            tMin = "0" + min;
        }
        this.mDate.setText(String.valueOf(year) + "/" + tMonth + "/" + tDay);
        if (this.app.timeFormat == 24) {
            String tTime2 = new StringBuilder().append(time).toString();
            if (time < 10) {
                tTime2 = "0" + time;
            }
            this.mTime.setText(String.valueOf(tTime2) + ":" + tMin);
            return;
        }
        if (time <= 12) {
            tTime = new StringBuilder().append(time).toString();
        } else {
            tTime = new StringBuilder().append(time - 12).toString();
        }
        this.mTime.setText(String.valueOf(tTime) + ":" + tMin);
    }

    public static boolean isWifiConnect(Context context) {
        return ((ConnectivityManager) context.getSystemService("connectivity")).getNetworkInfo(1).isConnected();
    }

    /* access modifiers changed from: private */
    public void checkWifiSign() {
        if (!isWifiConnect(getActivity())) {
            setWifi(0);
            return;
        }
        int level = ((WifiManager) getActivity().getSystemService("wifi")).getConnectionInfo().getRssi();
        if (level <= 0 && level >= -50) {
            setWifi(4);
        } else if (level < -50 && level >= -70) {
            setWifi(3);
        } else if (level < -70 && level >= -80) {
            setWifi(2);
        } else if (level >= -80 || level < -100) {
            setWifi(0);
        } else {
            setWifi(1);
        }
    }

    public class PhoneStateMonitor extends PhoneStateListener {
        public PhoneStateMonitor() {
        }

        public void onSignalStrengthsChanged(SignalStrength signalStrength) {
            super.onSignalStrengthsChanged(signalStrength);
            if (StateFrag.this.getActivity() != null) {
                int networkType = ((TelephonyManager) StateFrag.this.getActivity().getSystemService("phone")).getNetworkType();
                if (networkType == 1 || networkType == 2) {
                    StateFrag.this.setSignal(1);
                } else if (networkType == 0) {
                    StateFrag.this.setSignal(0);
                } else {
                    int dbm = signalStrength.getGsmSignalStrengthDbm();
                    if (dbm >= -75) {
                        StateFrag.this.setSignal(4);
                    } else if (dbm >= -85) {
                        StateFrag.this.setSignal(4);
                    } else if (dbm >= -95) {
                        StateFrag.this.setSignal(3);
                    } else if (dbm >= -100) {
                        StateFrag.this.setSignal(2);
                    } else {
                        StateFrag.this.setSignal(1);
                    }
                }
            }
        }

        public void onServiceStateChanged(ServiceState serviceState) {
            super.onServiceStateChanged(serviceState);
            if (StateFrag.this.getActivity() != null) {
                int level = 5;
                switch (serviceState.getState()) {
                    case 0:
                        level = 3;
                        break;
                    case 1:
                        level = 5;
                        break;
                    case 2:
                        level = 5;
                        break;
                    case 3:
                        level = 5;
                        break;
                }
                if (level != 3) {
                    StateFrag.this.setSignal(0);
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void handlerMsg(Message msg) {
        switch (msg.what) {
            case 1009:
                setTimeInfo(msg.getData());
                return;
            case SysConst.GPS_STATUS:
                setGPS();
                return;
            case SysConst.ORIGINAL_FLAG:
                setTimeColor(msg.getData().getBoolean("isRed", false));
                return;
            default:
                return;
        }
    }

    static class StateHandler extends Handler {
        public Bundle mBundle;
        private WeakReference<StateFrag> target;

        public StateHandler(StateFrag instance) {
            this.target = new WeakReference<>(instance);
        }

        public void handleMessage(Message msg) {
            if (this.target.get() != null) {
                ((StateFrag) this.target.get()).handlerMsg(msg);
            }
        }
    }

    private void registerReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.MEDIA_MOUNTED");
        filter.addAction("android.intent.action.MEDIA_EJECT");
        filter.addDataScheme("file");
        this.mUsbReceiver = new UsbBroadcastReceiver();
        getActivity().registerReceiver(this.mUsbReceiver, filter);
    }

    private void unregisterReceiver() {
        getActivity().unregisterReceiver(this.mUsbReceiver);
    }

    public void checkUsb3() {
        if (getPathExist(LauncherApplication.usbpaths.get(0)) || getPathExist(LauncherApplication.usbpaths.get(1)) || getPathExist(LauncherApplication.usbpaths.get(2))) {
            stateImg(this.mUsb3, 1);
        } else {
            stateImg(this.mUsb3, 0);
        }
        if (getPathExist(new File("storage/sdcard1"))) {
            stateImg(this.mSdcard, 1);
        } else {
            stateImg(this.mSdcard, 0);
        }
    }

    private boolean getPathExist(File file) {
        if (file == null || !file.exists() || file.length() <= 0) {
            return false;
        }
        return true;
    }

    class UsbBroadcastReceiver extends BroadcastReceiver {
        UsbBroadcastReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("android.intent.action.MEDIA_MOUNTED") || intent.getAction().equals("android.intent.action.MEDIA_EJECT")) {
                StateFrag.this.mStateHandler.postDelayed(new Runnable() {
                    public void run() {
                        StateFrag.this.checkUsb3();
                    }
                }, 500);
            }
        }
    }
}
