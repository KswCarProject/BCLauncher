package com.touchus.benchilauncher;

import a_vcard.android.provider.Contacts;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.PendingIntent;
import android.app.Service;
import android.bluetooth.BluetoothA2dp;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothHeadset;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.AnimationDrawable;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioManager;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.v4.media.TransportMediator;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.backaudio.android.driver.IMainboardEventLisenner;
import com.backaudio.android.driver.Mainboard;
import com.backaudio.android.driver.beans.AirInfo;
import com.backaudio.android.driver.beans.CarBaseInfo;
import com.backaudio.android.driver.beans.CarRunInfo;
import com.backaudio.android.driver.bluetooth.bc8mpprotocol.EPhoneStatus;
import com.sun.mail.iap.Response;
import com.touchus.benchilauncher.receiver.MusicControlReceiver;
import com.touchus.benchilauncher.receiver.SystemTimeReceiver;
import com.touchus.benchilauncher.service.BluetoothService;
import com.touchus.benchilauncher.utils.EcarManager;
import com.touchus.benchilauncher.utils.SpUtilK;
import com.touchus.benchilauncher.utils.Utiltools;
import com.touchus.benchilauncher.utils.WifiTool;
import com.touchus.benchilauncher.views.BTConnectDialog;
import com.touchus.benchilauncher.views.MenuSlide;
import com.touchus.benchilauncher.views.MyLinearlayout;
import com.touchus.publicutils.sysconst.BenzModel;
import com.touchus.publicutils.sysconst.PubSysConst;
import com.touchus.publicutils.sysconst.VoiceAssistantConst;
import com.touchus.publicutils.utils.APPSettings;
import com.touchus.publicutils.utils.UtilTools;
import com.touchus.publicutils.view.DesktopLayout;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.apache.log4j.helpers.UtilLoggingLevel;
import org.apache.log4j.net.SyslogAppender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainService extends Service {
    public static String curChannel;
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    /* access modifiers changed from: private */
    public String MYLOG = "listenlogread";
    public BluetoothAdapter adapter;
    private Runnable airplaneRunnable = new Runnable() {
        public void run() {
            MainService.this.dealwithAirplaneMode();
        }
    };
    /* access modifiers changed from: private */
    public AnimationDrawable animationDrawable;
    /* access modifiers changed from: private */
    public LauncherApplication app;
    public AudioManager audioManager;
    /* access modifiers changed from: private */
    public BluetoothA2dp benzA2dp;
    /* access modifiers changed from: private */
    public BluetoothDevice benzDevice;
    /* access modifiers changed from: private */
    public BluetoothHeadset benzhHeadset;
    private BroadcastReceiver bluetoothReceiver = new BroadcastReceiver() {
        @SuppressLint({"NewApi"})
        public void onReceive(Context context, Intent intent) {
            String aString = intent.getAction();
            Log.e("", "btstate Action = " + aString);
            if (SysConst.isBT()) {
                if ("android.bluetooth.a2dp.profile.action.CONNECTION_STATE_CHANGED".equals(aString)) {
                    int btA2dpStatus = intent.getIntExtra("android.bluetooth.profile.extra.STATE", 0);
                    switch (btA2dpStatus) {
                        case 0:
                            Log.e("", "btstate a2dp STATE_DISCONNECTED");
                            MainService.this.iBenzA2dpConnected = false;
                            MainService.this.setMusicMute();
                            break;
                        case 1:
                            Log.e("", "btstate a2dp STATE_CONNECTING");
                            MainService.this.iBenzA2dpConnected = false;
                            MainService.this.setMusicMute();
                            if (BenzModel.isBenzGLK()) {
                                MainService.this.mHandler.postDelayed(new Runnable() {
                                    public void run() {
                                        Mainboard.getInstance().forcePress();
                                    }
                                }, 4000);
                                break;
                            }
                            break;
                        case 2:
                            Log.e("", "btstate a2dp STATE_CONNECTED");
                            if (BenzModel.isBenzGLK()) {
                                MainService.this.mHandler.post(MainService.this.removeLoadingRunable);
                            }
                            MainService.this.iBenzA2dpConnected = true;
                            MainService.this.setConnectedMusicVoice();
                            break;
                    }
                    Message message = new Message();
                    message.what = SysConst.BT_A2DP_STATUS;
                    message.obj = Integer.valueOf(btA2dpStatus);
                    MainService.this.mHandler.sendMessage(message);
                } else if ("android.bluetooth.headset.profile.action.CONNECTION_STATE_CHANGED".equals(aString)) {
                    int btHeadsetStatus = intent.getIntExtra("android.bluetooth.profile.extra.STATE", 0);
                    switch (btHeadsetStatus) {
                        case 0:
                            Log.e("", "btstate headset STATE_DISCONNECTED");
                            break;
                        case 1:
                            Log.e("", "btstate headset STATE_CONNECTING");
                            break;
                        case 2:
                            Log.e("", "btstate headset STATE_CONNECTED");
                            break;
                    }
                    Message message2 = new Message();
                    message2.what = SysConst.BT_HEADSET_STATUS;
                    message2.obj = Integer.valueOf(btHeadsetStatus);
                    MainService.this.mHandler.sendMessage(message2);
                } else if ("android.bluetooth.device.action.FOUND".equals(aString)) {
                    BluetoothDevice device = (BluetoothDevice) intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE");
                    Log.e("", "btstate ACTION_FOUND device.getName = " + device.getName());
                    if (MainService.this.carBTname.equals(device.getName())) {
                        MainService.this.benzDevice = device;
                        MainService.this.adapter.cancelDiscovery();
                    }
                } else if ("android.bluetooth.adapter.action.DISCOVERY_FINISHED".equals(aString)) {
                    Log.e("", "btstate ACTION_DISCOVERY_FINISHED");
                    if (BenzModel.isBenzGLK() && MainService.this.benzDevice != null) {
                        MainService.this.benzDevice.createBond();
                    }
                } else if ("android.bluetooth.device.action.BOND_STATE_CHANGED".equals(aString)) {
                    BluetoothDevice device2 = (BluetoothDevice) intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE");
                    if (device2 == null) {
                        Log.e("", "btstate bond failed ");
                    } else if (MainService.this.carBTname.equals(device2.getName()) || MainService.this.testBTname.contains(device2.getName())) {
                        MainService.this.benzDevice = device2;
                        Log.e("", "btstate bond state:" + device2.getBondState());
                        switch (device2.getBondState()) {
                            case MenuSlide.STATE_UP222 /*11*/:
                                if (BenzModel.isBenzGLK()) {
                                    MainService.this.mHandler.postDelayed(new Runnable() {
                                        public void run() {
                                            Mainboard.getInstance().forcePress();
                                        }
                                    }, 4000);
                                    return;
                                }
                                return;
                            case Response.BAD:
                                if (MainService.this.benzhHeadset != null) {
                                    MainService.this.benzhHeadset.setPriority(device2, 0);
                                }
                                if (BenzModel.isBenzGLK() && !MainService.this.iExist) {
                                    MainService.this.mHandler.postDelayed(new Runnable() {
                                        public void run() {
                                            if (MainService.this.benzDevice != null && !MainService.this.getBTA2dpState()) {
                                                Log.e("", "btstate a2dp connect = " + MainService.this.benzDevice.getName());
                                                MainService.this.benzA2dp.connect(MainService.this.benzDevice);
                                            }
                                        }
                                    }, 2000);
                                    return;
                                }
                                return;
                            default:
                                return;
                        }
                    }
                } else if ("android.bluetooth.device.action.PAIRING_REQUEST".equals(aString)) {
                    BluetoothDevice device3 = (BluetoothDevice) intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE");
                    Log.e("", "btstate pairing request");
                    if (device3 == null) {
                        Log.e("", "pairing failed ");
                    } else if (MainService.this.carBTname.equals(device3.getName()) || MainService.this.testBTname.contains(device3.getName())) {
                        MainService.this.mHandler.postDelayed(new Runnable() {
                            public void run() {
                                Mainboard.getInstance().forcePress();
                            }
                        }, 1500);
                    }
                } else if ("android.bluetooth.device.action.PAIRING_CANCEL".equals(aString)) {
                    Log.e("", "btstate pairing cancel");
                }
            }
        }
    };
    Thread canboxtThread = new Thread(new Runnable() {
        public void run() {
            if (!MainService.this.iReadyUpdate) {
                Mainboard.getInstance().requestUpgradeCanbox(MainService.this.totalDataIndex);
                MainService.this.mHandler.postDelayed(MainService.this.canboxtThread, 200);
                return;
            }
            MainService.this.mHandler.removeCallbacks(MainService.this.canboxtThread);
        }
    });
    /* access modifiers changed from: private */
    public String carBTname = "MB Bluetooth";
    public Runnable checkNavigationRunnable = new Runnable() {
        public void run() {
            MainService.this.mHandler.postDelayed(MainService.this.checkNavigationRunnable, 2000);
            if (!LauncherApplication.iAccOff) {
                String topPkg = MainService.this.getCurrentTopAppPkg();
                if (!TextUtils.isEmpty(topPkg) && !topPkg.equals(MainService.this.getPackageName()) && !topPkg.equals(MainService.this.getString(R.string.pkg_navi)) && !topPkg.equals("com.unibroad.notifyreceiverservice") && !topPkg.equals("com.unibroad.benzuserguide") && !topPkg.equals("com.touchus.amaplocation") && !topPkg.equals("com.android.packageinstaller") && !topPkg.equals("cld.navi.kgomap") && !topPkg.equals("com.baidu.navi") && !topPkg.equals("net.easyconn") && !topPkg.equals("com.tima.carnet.vt")) {
                    DesktopLayout.show(MainService.this);
                }
                MainService.this.app.sendShowNavigationBarEvent(false);
            }
        }
    };
    private int[] data = {10, 10, 10, 10, 10, 10, 10};
    private BroadcastReceiver easyConnectReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(SysConst.EVENT_EASYCONN_BT_CHECKSTATUS)) {
                Log.e("easyConnect", "easyConnect checkstatus");
                MainService.this.app.isEasyView = true;
                if (LauncherApplication.isBlueConnectState) {
                    MainService.this.sendEasyConnectBroadcast(SysConst.EVENT_EASYCONN_BT_CONNECTED);
                    MainService.this.app.btservice.requestMusicAudioFocus();
                    return;
                }
                MainService.this.app.openOrCloseBluetooth(true);
                MainService.this.sendEasyConnectBroadcast(SysConst.EVENT_EASYCONN_BT_OPENED);
                MainService.this.sendEasyConnectBroadcast(SysConst.EVENT_EASYCONN_BT_UNCONNECTED);
            } else if (action.equals(SysConst.EVENT_EASYCONN_A2DP_ACQUIRE)) {
                Log.e("easyConnect", "easyConnect a2dp acquire");
                if (LauncherApplication.isBlueConnectState) {
                    MainService.this.app.btservice.requestMusicAudioFocus();
                }
            } else if (action.equals(SysConst.EVENT_EASYCONN_A2DP_RELEASE)) {
                Log.e("easyConnect", "easyConnect a2dp release");
                MainService.this.app.btservice.stopBTMusic();
            } else if (action.equals(SysConst.EVENT_EASYCONN_APP_QUIT)) {
                Log.e("easyConnect", "easyConnect quit");
                MainService.this.app.isEasyView = false;
            } else {
                Log.e("easyConnect", "easyConnect no");
            }
        }
    };
    /* access modifiers changed from: private */
    public EcarAppManagerRunnable ecarAppManagerRunnable = new EcarAppManagerRunnable();
    long endTime;
    /* access modifiers changed from: private */
    public Runnable getConnectCanboxState = new Runnable() {
        public void run() {
            if (!MainService.this.iConnectCanbox) {
                Mainboard.getInstance().connectOrDisConnectCanbox(true);
                MainService.this.mHandler.postDelayed(MainService.this.getConnectCanboxState, 200);
                return;
            }
            MainService.this.mHandler.removeCallbacks(MainService.this.getConnectCanboxState);
        }
    };
    private BroadcastReceiver getWeatherReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            MainService.this.app.currentCityName = intent.getStringExtra("cityName");
            MainService.this.app.weatherTempDes = intent.getStringExtra("cityWeather");
            String hi = intent.getStringExtra("highTemperature");
            String low = intent.getStringExtra("lowTemperature");
            MainService.this.app.weatherTemp = String.valueOf(low) + "-" + hi + "℃";
            Log.e("voice", "Weather app.weatherTempDes = " + MainService.this.app.weatherTempDes + ",app.weatherTemp = " + MainService.this.app.weatherTemp);
            MainService.this.app.sendMessage(1010, (Bundle) null);
        }
    };
    /* access modifiers changed from: private */
    public Runnable getWeatherRunnabale = new Runnable() {
        public void run() {
            if (UtilTools.isNetworkConnected(MainService.this)) {
                MainService.this.getWeather();
            }
            MainService.this.mHandler.postDelayed(MainService.this.getWeatherRunnabale, 1200000);
        }
    };
    private GpsLocationListener gpsLocatListener = new GpsLocationListener(this, (GpsLocationListener) null);
    /* access modifiers changed from: private */
    public LocationManager gpsManager;
    private GpsStatus.Listener gpsStateListener = new GpsStatus.Listener() {
        public void onGpsStatusChanged(int arg0) {
            if (arg0 == 2) {
                if (LauncherApplication.isGPSLocation) {
                    LauncherApplication.isGPSLocation = false;
                }
                if (!LauncherApplication.iAccOff) {
                    MainService.this.setGpsStatus(true);
                }
            } else if (arg0 == 4) {
                GpsStatus gpsStatus = MainService.this.gpsManager.getGpsStatus((GpsStatus) null);
                int maxSatellites = gpsStatus.getMaxSatellites();
                Iterator<GpsSatellite> iters = gpsStatus.getSatellites().iterator();
                int count = 0;
                while (iters.hasNext() && count <= maxSatellites) {
                    if (iters.next().usedInFix()) {
                        count++;
                    }
                }
                if (count > 4) {
                    LauncherApplication.isGPSLocation = true;
                } else {
                    LauncherApplication.isGPSLocation = false;
                }
            }
            MainService.this.app.sendMessage(SysConst.GPS_STATUS, (Bundle) null);
        }
    };
    /* access modifiers changed from: private */
    public boolean iAUX = false;
    /* access modifiers changed from: private */
    public boolean iBenzA2dpConnected = false;
    /* access modifiers changed from: private */
    public boolean iConnectCanbox = false;
    boolean iExist = false;
    boolean iGetWeather = false;
    private boolean iInAccState = false;
    public boolean iIsBTConnect = false;
    public boolean iIsDV = false;
    private boolean iIsEnterAirplaneMode = false;
    public boolean iIsInAndroid = true;
    public boolean iIsInOriginal = false;
    public boolean iIsInRecorder = false;
    public boolean iIsInReversing = false;
    public boolean iIsScrennOff = false;
    /* access modifiers changed from: private */
    public boolean iNaviSound = false;
    /* access modifiers changed from: private */
    public boolean iReadyUpdate = false;
    /* access modifiers changed from: private */
    public boolean iVoiceSound = false;
    /* access modifiers changed from: private */
    public boolean isAMPPowerOpen = true;
    private long lastAirplaneTime = -1;
    /* access modifiers changed from: private */
    public FrameLayout loadingFloatLayout;
    /* access modifiers changed from: private */
    public Logger logger = LoggerFactory.getLogger(MainService.class);
    ActivityManager mActivityManager;
    public MainBinder mBinder = new MainBinder();
    /* access modifiers changed from: private */
    public Mhandler mHandler = new Mhandler(this);
    private BluetoothProfile.ServiceListener mProfileServiceListener = new BluetoothProfile.ServiceListener() {
        public void onServiceDisconnected(int profile) {
        }

        public void onServiceConnected(int profile, BluetoothProfile proxy) {
            if (profile == 1) {
                try {
                    MainService.this.benzhHeadset = (BluetoothHeadset) proxy;
                } catch (Exception e) {
                    Log.e("debug", "uuid BOND_ BluetoothProfile error" + e.toString());
                }
            } else if (profile == 2) {
                MainService.this.benzA2dp = (BluetoothA2dp) proxy;
                if (MainService.this.benzDevice != null && !MainService.this.getBTA2dpState()) {
                    Log.e("", "btstate a2dp connect = " + MainService.this.benzDevice.getName());
                    MainService.this.benzA2dp.connect(MainService.this.benzDevice);
                }
            }
        }
    };
    public ComponentName mRemoteControlClientReceiverComponent;
    /* access modifiers changed from: private */
    public SpUtilK mSpUtilK;
    private WindowManager mWindowManager;
    /* access modifiers changed from: private */
    public int mcuTotalDataIndex = -999;
    public byte[] mcuUpgradeByteData = null;
    Runnable musicRunnable = new Runnable() {
        public void run() {
            MainService.this.setConnectedMusicVoice();
        }
    };
    private BroadcastReceiver networkReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("android.net.conn.CONNECTIVITY_CHANGE") || action.equals("android.net.wifi.WIFI_STATE_CHANGED")) {
                MainService.this.regainWeather();
            }
        }
    };
    /* access modifiers changed from: private */
    public MyLinearlayout noTouchLayout;
    private BroadcastReceiver recoveryReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("com.touchus.factorytest.recovery")) {
                MainService.this.forceStopApp("com.unibroad.usbcardvr");
                MainService.this.sendBroadcast(new Intent("android.intent.action.MASTER_CLEAR"));
            } else if (action.equals("com.touchus.factorytest.sdcardreset")) {
                Utiltools.resetSDCard();
            } else if (action.equals("com.touchus.factorytest.getMcu")) {
                Intent getMcu = new Intent();
                getMcu.setAction("com.touchus.factorytest.Mcu");
                getMcu.putExtra("mcu", MainService.this.app.curMcuVersion);
                MainService.this.sendBroadcast(getMcu);
            } else if (action.equals(PubSysConst.ACTION_FACTORY_BREAKSET)) {
                int pos = intent.getIntExtra(PubSysConst.KEY_BREAKPOS, 0);
                MainService.this.mSpUtilK.putInt(PubSysConst.KEY_BREAKPOS, pos);
                MainService.this.app.breakpos = pos;
                SysConst.storeData[9] = (byte) pos;
                Log.e("", "SysConst.storeData = " + SysConst.storeData);
                Mainboard.getInstance().sendStoreDataToMcu(SysConst.storeData);
                UtilTools.echoFile(new StringBuilder().append(pos).toString(), PubSysConst.GT9XX_INT_TRIGGER);
            } else if (action.equals("com.touchus.factorytest.benzetype")) {
                byte type = (byte) intent.getIntExtra(BenzModel.KEY, 0);
                BenzModel.EBenzTpye[] values = BenzModel.EBenzTpye.values();
                int length = values.length;
                int i = 0;
                while (true) {
                    if (i >= length) {
                        break;
                    }
                    BenzModel.EBenzTpye value = values[i];
                    if (type == value.getCode()) {
                        BenzModel.benzTpye = value;
                        break;
                    }
                    i++;
                }
                MainService.this.app.isSUV = BenzModel.isSUV();
                Mainboard.getInstance().setBenzType(BenzModel.benzTpye);
                if (SysConst.isBT()) {
                    MainService.this.app.ismix = false;
                }
                MainService.this.mSpUtilK.putInt(BenzModel.KEY, BenzModel.benzTpye.getCode());
            }
        }
    };
    public Runnable removeLoadingRunable = new Runnable() {
        public void run() {
            if (MainService.this.loadingFloatLayout != null) {
                MainService.this.removeLoadingFloatView();
                MainService.this.mHandler.removeCallbacks(MainService.this.removeLoadingRunable);
                if (!BenzModel.isBenzGLK() || !SysConst.isBT()) {
                    MainService.this.logger.debug(String.valueOf(MainService.this.MYLOG) + " removeLoading isAUX = " + MainService.this.iAUX + ",getBTA2dpState = " + MainService.this.getBTA2dpState());
                    if (!MainService.this.getIsBTState()) {
                        BTConnectDialog.getInstance().showBTConnectView(MainService.this);
                        return;
                    }
                    BTConnectDialog.getInstance().dissShow();
                    MainService.this.setConnectedMusicVoice();
                }
            }
        }
    };
    private int spaceTime = UtilLoggingLevel.FINER_INT;
    private Runnable startSettingRunnable = new Runnable() {
        public void run() {
            MainService.this.setAirplaneMode(false);
            MainService.this.setGpsStatus(true);
            MainService.this.app.getLanguage();
            MainService.this.app.isVocieWakeup = MainService.this.mSpUtilK.getBoolean(MainService.this.getString(R.string.string_vwakeup), false);
            MainService.this.app.isSpeechKeyOpen = MainService.this.mSpUtilK.getBoolean(MainService.this.getString(R.string.string_voice_setting), false);
            MainService.this.app.isOriginalKeyOpen = MainService.this.mSpUtilK.getBoolean(MainService.this.getString(R.string.string_original_setting), false);
            MainService.this.app.ismix = MainService.this.mSpUtilK.getBoolean(MainService.this.getString(R.string.string_navi_mix), true);
            MainService.this.app.isTestOpen = MainService.this.mSpUtilK.getBoolean("test", false);
            MainService.this.app.isGestureSwitch = MainService.this.mSpUtilK.getBoolean(MainService.this.getString(R.string.string_gesture_switch), true);
            Mainboard.getInstance().setiForbidGesture(MainService.this.app.isGestureSwitch);
            MainService.this.app.radioPos = MainService.this.mSpUtilK.getInt(SysConst.FLAG_CONFIG_RADIO, 0);
            MainService.this.app.naviPos = MainService.this.mSpUtilK.getInt(SysConst.FLAG_CONFIG_NAVI, 0);
            MainService.this.app.usbPos = MainService.this.mSpUtilK.getInt(SysConst.FLAG_CONFIG_USB_NUM, 0);
            MainService.this.app.connectPos = MainService.this.mSpUtilK.getInt(SysConst.FLAG_CONFIG_CONNECT, 0);
            MainService.this.app.connectPos1 = MainService.this.mSpUtilK.getInt(SysConst.FLAG_CONFIG_CONNECT1, 0);
            MainService.this.app.screenPos = MainService.this.mSpUtilK.getInt(SysConst.FLAG_CONFIG_SCREEN, 0);
            MainService.this.app.playpos = MainService.this.mSpUtilK.getInt(SysConst.FLAG_CONFIG_PLAY, 0);
            Log.e("", "app.radioPos = " + MainService.this.app.radioPos + ",app.naviPos = " + MainService.this.app.naviPos + ",app.usbPos = " + MainService.this.app.usbPos + ",app.connectPos = " + MainService.this.app.connectPos + ",app.screenPos = " + MainService.this.app.screenPos);
            if (MainService.this.mSpUtilK.getInt(SysConst.FLAG_REVERSING_VOICE, 0) == 1) {
                MainService.this.app.eMediaSet = Mainboard.EReverserMediaSet.NOMAL;
            } else {
                MainService.this.app.eMediaSet = Mainboard.EReverserMediaSet.MUTE;
            }
            if (MainService.this.app.radioPos == 0) {
                if (MainService.this.app.connectPos == 0) {
                    LauncherApplication.isBT = true;
                } else {
                    LauncherApplication.isBT = false;
                }
            } else if (MainService.this.app.connectPos1 == 0) {
                LauncherApplication.isBT = true;
            } else {
                LauncherApplication.isBT = false;
            }
            if (MainService.this.app.playpos == 1) {
                LauncherApplication.isBT = false;
                MainService.this.iAUX = true;
            }
            if (SysConst.isBT()) {
                MainService.this.addBluetoothReceiver();
                MainService.this.app.ismix = false;
                MainService.this.mSpUtilK.putBoolean(MainService.this.getString(R.string.string_navi_mix), false);
                SysConst.musicDecVolume = 0.3f;
                MainService.this.startLocalBT();
            }
            if (MainService.this.app.ismix) {
                SysConst.musicDecVolume = (float) (((double) MainService.this.mSpUtilK.getInt(SysConst.mixPro, 5)) / 10.0d);
            }
            MainService.this.setEffectData(MainService.this.mSpUtilK.getInt(SysConst.effectpos, 0));
        }
    };
    long startTime;
    private SystemTimeReceiver systemTimeReceiver;
    /* access modifiers changed from: private */
    public List<String> testBTname = new ArrayList();
    /* access modifiers changed from: private */
    public int totalDataIndex = -999;
    private BroadcastReceiver unibroadAudioFocusReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            String pkgName = intent.getStringExtra("pkgName");
            Log.d("unibroadAudioFocus", "action = " + action + ",pkgName=" + pkgName);
            int durationHint = intent.getIntExtra("durationHint", 0);
            int status = intent.getIntExtra("status", 0);
            if (!TextUtils.isEmpty(pkgName)) {
                boolean iOpen = false;
                int mediaVolume = SysConst.num[MainService.this.mSpUtilK.getInt(SysConst.mediaVoice, 5)];
                int dvVolume = SysConst.num[MainService.this.mSpUtilK.getInt(SysConst.dvVoice, 5)];
                int naviVolume = SysConst.num[MainService.this.mSpUtilK.getInt(SysConst.naviVoice, 3)];
                if (!MainService.this.iAUX && (MainService.this.app.isTestOpen || (MainService.this.benzDevice != null && MainService.this.testBTname.contains(MainService.this.benzDevice.getName())))) {
                    MainService.this.iAUX = true;
                }
                if (pkgName.equals("com.unibroad.voiceassistant") || pkgName.equals("com.txznet.txz") || pkgName.equals("com.txznet.txzyzs1") || pkgName.equals("com.iflytek.speechcloud")) {
                    Log.e("", "VoiceSound unibroad show = " + MainService.this.iVoiceSound);
                    if (!MainService.this.iVoiceSound && !MainService.this.app.isCalling) {
                        MainService.this.iVoiceSound = true;
                        MainService.this.app.setTypeMute(2, true);
                        MainService.this.setMusicMute();
                        if (MainService.this.app.playpos != 1 || (!Build.MODEL.equals("c200") && !Build.MODEL.equals("c200_en") && !Build.MODEL.equals("c200_ks"))) {
                            Mainboard.getInstance().setAllHornSoundValue(SysConst.mediaBasicNum, naviVolume, 0, 0, 0);
                            Mainboard.getInstance().setAllHornSoundValue(SysConst.basicNum, naviVolume, 0, 0, 0);
                        } else {
                            Mainboard.getInstance().setAllHornSoundValue(SysConst.mediaBasicNum, naviVolume, 0, naviVolume, naviVolume);
                            Mainboard.getInstance().setAllHornSoundValue(SysConst.basicNum, naviVolume, 0, naviVolume, naviVolume);
                        }
                        MainService.this.iNaviSound = true;
                        MainService.this.openOrCloseRelay(true);
                        return;
                    }
                    return;
                }
                MainService.this.mHandler.removeCallbacks(MainService.this.musicRunnable);
                if (intent.getAction().equals(SysConst.EVENT_UNIBROAD_AUDIO_REGAIN)) {
                    MainService.this.iVoiceSound = false;
                    if (MainService.this.app.lastmusicType != 0 && MainService.this.app.isOpenAMP) {
                        MainService.this.app.abandonMainGainAudioFocus();
                    }
                    Log.d("unibroadAudioFocus", "AUDIO_REGAIN pkgName:" + pkgName + " , openOrCloseRelay: " + false + ",isCalling : " + MainService.this.app.isCalling + ",app.musicType = " + MainService.this.app.musicType);
                    if (MainService.this.iIsDV) {
                        Mainboard.getInstance().setDVSoundValue(SysConst.basicNum, dvVolume, 0, 0, 0);
                        Log.d("unibroadAudioFocus", " DV openOrCloseRelay: " + false);
                        MainService.this.openOrCloseRelay(true);
                        return;
                    }
                    if (SysConst.isBT()) {
                        Mainboard.getInstance().setAllHornSoundValue(SysConst.mediaBasicNum, naviVolume, 0, 0, 0);
                    } else {
                        Mainboard.getInstance().setAllHornSoundValue(SysConst.mediaBasicNum, naviVolume, 0, mediaVolume, mediaVolume);
                    }
                    if (!pkgName.contains("tencent")) {
                        MainService.this.app.setTypeMute(2, false);
                    }
                    if (pkgName.equals(MainService.this.getPackageName()) && MainService.this.app.musicType == 0) {
                        MainService.this.setMusicMute();
                    } else if (MainService.this.getBTA2dpState()) {
                        MainService.this.mHandler.postDelayed(MainService.this.musicRunnable, 500);
                    } else {
                        MainService.this.setMusicMute();
                    }
                } else {
                    Log.d("unibroadAudioFocus", "requestAudiofocus action : " + action + " audiofocus pkgName:" + pkgName + " , durationHint: " + durationHint + ",status : " + status + ", app.isCalling : " + MainService.this.app.isCalling + ",app.musicType = " + MainService.this.app.musicType);
                    if (MainService.this.app.isCalling) {
                        iOpen = true;
                        MainService.this.app.setTypeMute(2, true);
                        MainService.this.setMusicMute();
                    } else {
                        if (pkgName.equals(MainService.this.app.getNaviAPP()) || pkgName.equals("com.amap.navi.demo") || APPSettings.isNavgation(pkgName) || pkgName.equals("com.coagent.ecar") || pkgName.equals("com.coagent.voip") || MainService.this.app.musicType == 6) {
                            if (!MainService.this.iVoiceSound) {
                                if (SysConst.isBT()) {
                                    iOpen = true;
                                    Mainboard.getInstance().setAllHornSoundValue(SysConst.basicNum, naviVolume, 0, 0, 0);
                                } else if (!MainService.this.app.ismix || !MainService.this.iAUX) {
                                    iOpen = true;
                                    MainService.this.setMusicMute();
                                    Mainboard.getInstance().setAllHornSoundValue(0, naviVolume, 0, 0, 0);
                                    Mainboard.getInstance().setAllHornSoundValue(SysConst.basicNum, naviVolume, 0, 0, 0);
                                } else if (MainService.this.app.playpos != 1 || MainService.this.app.isOpenAMP) {
                                    Mainboard.getInstance().setAllHornSoundValue(SysConst.mediaBasicNum, naviVolume, 0, mediaVolume, mediaVolume);
                                } else {
                                    Mainboard.getInstance().setAllHornSoundValue(SysConst.basicNum, naviVolume, 0, naviVolume, naviVolume);
                                    MainService.this.iNaviSound = true;
                                    MainService.this.openOrCloseRelay(true);
                                    return;
                                }
                                MainService.this.app.setTypeMute(2, false);
                            } else {
                                MainService.this.app.setTypeMute(2, true);
                            }
                            if (!MainService.this.getBTA2dpState() || (!SysConst.isBT() && !MainService.this.app.ismix)) {
                                MainService.this.setMusicMute();
                            }
                        } else {
                            if (pkgName.equals(MainService.this.getPackageName()) && MainService.this.app.musicType == 0) {
                                MainService.this.setMusicMute();
                                MainService.this.iVoiceSound = false;
                            } else if (!MainService.this.iVoiceSound) {
                                if (!pkgName.equals(MainService.this.getPackageName())) {
                                    MainService.this.app.musicType = 5;
                                }
                                if (MainService.this.iIsDV) {
                                    Mainboard.getInstance().setDVSoundValue(SysConst.basicNum, dvVolume, 0, 0, 0);
                                    Log.d("unibroadAudioFocus", " DV openOrCloseRelay: " + false);
                                    MainService.this.openOrCloseRelay(true);
                                    return;
                                }
                                if (SysConst.isBT()) {
                                    Mainboard.getInstance().setAllHornSoundValue(SysConst.mediaBasicNum, naviVolume, 0, 0, 0);
                                } else {
                                    Mainboard.getInstance().setAllHornSoundValue(SysConst.mediaBasicNum, naviVolume, 0, mediaVolume, mediaVolume);
                                }
                                if (!pkgName.contains("tencent")) {
                                    MainService.this.app.setTypeMute(2, false);
                                }
                                Log.d("unibroadAudioFocus", "iAUX = " + MainService.this.iAUX + ",app.musicType = " + MainService.this.app.musicType);
                                if (!MainService.this.getIsBTState() || MainService.this.app.playpos == 1) {
                                    MainService.this.btMusicConnect();
                                    Log.d("unibroadAudioFocus", "MusicUnConnect");
                                } else {
                                    MainService.this.mHandler.postDelayed(MainService.this.musicRunnable, 500);
                                    Log.d("unibroadAudioFocus", "MusicConnect");
                                }
                            } else {
                                return;
                            }
                        }
                    }
                }
                Log.d("unibroadAudioFocus", "openOrCloseRelay: " + iOpen + ",isCalling : " + MainService.this.app.isCalling);
                MainService.this.iNaviSound = iOpen;
                MainService.this.openOrCloseRelay(iOpen);
            }
        }
    };
    /* access modifiers changed from: private */
    public byte[] upgradeByteData = null;
    private BroadcastReceiver voiceShowOrHideReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("com.txznet.txz.record.show")) {
                MainService.this.iVoiceSound = true;
                MainService.this.app.setTypeMute(2, true);
                MainService.this.setMusicMute();
                int mediaVoice = SysConst.num[MainService.this.mSpUtilK.getInt(SysConst.mediaVoice, 3)];
                int naviVoice = SysConst.num[MainService.this.mSpUtilK.getInt(SysConst.naviVoice, 3)];
                if (MainService.this.app.playpos != 1 || (!Build.MODEL.equals("c200") && !Build.MODEL.equals("c200_en") && !Build.MODEL.equals("c200_ks"))) {
                    Mainboard.getInstance().setAllHornSoundValue(SysConst.basicNum, naviVoice, 0, 0, 0);
                } else {
                    Mainboard.getInstance().setAllHornSoundValue(SysConst.mediaBasicNum, mediaVoice, 0, mediaVoice, mediaVoice);
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (MainService.this.app.playpos != 1 || (!Build.MODEL.equals("c200") && !Build.MODEL.equals("c200_en") && !Build.MODEL.equals("c200_ks"))) {
                    Mainboard.getInstance().setAllHornSoundValue(SysConst.basicNum, naviVoice, 0, 0, 0);
                } else {
                    Mainboard.getInstance().setAllHornSoundValue(SysConst.basicNum, mediaVoice, 0, mediaVoice, mediaVoice);
                }
                MainService.this.openOrCloseRelay(true);
                if (!MainService.this.iIsInAndroid || !MainService.this.iIsInReversing) {
                    MainService.this.removeNoTouchScreens();
                    MainService.this.app.interAndroidView();
                }
                if (MainService.this.app.iIsScreenClose.get()) {
                    MainService.this.app.closeOrWakeupScreen(false);
                }
                Log.e("", "VoiceSound show");
            } else if (action.equals("com.txznet.txz.record.dismiss")) {
                Log.e("", "VoiceSound hide");
                MainService.this.openOrCloseRelay(false);
                MainService.this.mHandler.postDelayed(new Runnable() {
                    public void run() {
                        MainService.this.iVoiceSound = false;
                    }
                }, 1000);
            }
        }
    };

    public void onCreate() {
        super.onCreate();
        this.app = (LauncherApplication) getApplication();
        this.app.serviceHandler = this.mHandler;
        this.mSpUtilK = new SpUtilK(getApplicationContext());
        this.audioManager = (AudioManager) getSystemService("audio");
        this.mActivityManager = (ActivityManager) getSystemService("activity");
        this.startTime = System.currentTimeMillis();
        Log.e("", "listenlog kill KernelMCU  = " + this.startTime);
        Utiltools.stopKernelMCU();
        addWeatherReceiver();
        addNetworkReceiver();
        addEasyConnectReceiver();
        addUnibroadAudioFocusReceiver();
        addRecoveryReceiver();
        addMusicReceiver();
        addVoiceShowOrHideReceiver();
        registerGPSListener();
        registerSystemTimeReceiver();
        if (Build.MODEL.equals("c200_ks")) {
            startEcarRunnable();
        }
        Log.e("", "listenlog initMCU  = " + (System.currentTimeMillis() - this.startTime));
        this.startTime = System.currentTimeMillis();
        broadcastWakeUpEvent();
        initMcuAndCanbox();
        this.app.requestMainAudioFocus();
        this.mHandler.postDelayed(this.startSettingRunnable, 1500);
        this.mHandler.postDelayed(this.getWeatherRunnabale, 20000);
        this.app.setTypeMute(3, true);
        this.app.setTypeMute(2, false);
        if (this.app.getWifiApState()) {
            WifiTool.openWifiAp(this, WifiTool.getCurrentWifiApConfig(this));
        }
        UtilTools.echoFile(new StringBuilder().append(this.app.breakpos).toString(), PubSysConst.GT9XX_INT_TRIGGER);
        this.testBTname.add("A2");
        this.testBTname.add("icar_500724");
    }

    public void onDestroy() {
        unregisterReceiver(this.getWeatherReceiver);
        unregisterReceiver(this.networkReceiver);
        unregisterReceiver(this.unibroadAudioFocusReceiver);
        unregisterReceiver(this.voiceShowOrHideReceiver);
        unregisterReceiver(this.recoveryReceiver);
        unregisterReceiver(this.easyConnectReceiver);
        if (SysConst.isBT()) {
            unregisterReceiver(this.bluetoothReceiver);
        }
        if (Build.MODEL.equals("c200_ks")) {
            EcarManager.getInstance(getApplicationContext()).unregisterEcarReceiver();
        }
        unregisterMusicReceiver();
        unregisterGPSListener();
        unregisterSystemTimeReceiver();
        this.app.abandonMainAudioFocus();
        super.onDestroy();
    }

    private void initMcuAndCanbox() {
        new Thread(new Runnable() {
            public void run() {
                Mainboard.getInstance().setMainboardEventLisenner(new MainListenner());
                Mainboard.getInstance().readyToWork();
                Mainboard.getInstance().getMCUVersionNumber();
                Mainboard.getInstance().getReverseSetFromMcu();
                Mainboard.getInstance().getStoreDataFromMcu();
                Mainboard.getInstance().getBrightnessValue();
                Mainboard.getInstance().getReverseMediaSetFromMcu();
                Mainboard.getInstance().getBenzType();
                Log.e("", "listenlog QUERY CarLayer = " + (System.currentTimeMillis() - MainService.this.startTime));
                MainService.this.startTime = System.currentTimeMillis();
            }
        }).start();
        this.iConnectCanbox = false;
        this.mHandler.postDelayed(this.getConnectCanboxState, 200);
    }

    /* access modifiers changed from: private */
    public void setEffectData(int currentpos) {
        switch (currentpos) {
            case 0:
                this.data = this.app.getEffectData(this.data);
                break;
            case 1:
                this.data = new int[]{10, 14, 17, 13, 8, 13, 10};
                break;
            case 2:
                this.data = new int[]{15, 13, 11, 10, 9, 12, 17};
                break;
            case 3:
                this.data = new int[]{8, 12, 13, 15, 13, 11, 8};
                break;
            case 4:
                this.data = new int[]{16, 17, 15, 11, 12, 14, 15};
                break;
        }
        this.app.setEffect(this.data, currentpos);
    }

    private void startEcarRunnable() {
        if (Build.MODEL.contains("c200_ks")) {
            EcarManager.getInstance(getApplicationContext()).registerEcarReceiver();
            EcarManager.getInstance(getApplicationContext()).startEcarBootService();
            this.mHandler.removeCallbacks(this.ecarAppManagerRunnable);
            this.mHandler.postDelayed(this.ecarAppManagerRunnable, 10000);
        }
    }

    public class EcarAppManagerRunnable implements Runnable {
        public EcarAppManagerRunnable() {
        }

        public void run() {
            if (!UtilTools.isServiceWork(MainService.this.getApplicationContext(), "com.ecar.AppManager.AppManagerService")) {
                EcarManager.getInstance(MainService.this.getApplicationContext()).startAppManagerService();
            }
            MainService.this.mHandler.postDelayed(MainService.this.ecarAppManagerRunnable, 10000);
        }
    }

    public void enterIntoUpgradeCanboxCheck() {
        new Thread(new Runnable() {
            public void run() {
                File file = new File(MainService.this.getString(R.string.path_upgrade_canbox));
                if (file.exists()) {
                    ByteArrayOutputStream bos = new ByteArrayOutputStream((int) file.length());
                    BufferedInputStream in = null;
                    try {
                        BufferedInputStream in2 = new BufferedInputStream(new FileInputStream(file));
                        try {
                            byte[] buffer = new byte[1024];
                            while (true) {
                                int len = in2.read(buffer, 0, 1024);
                                if (-1 == len) {
                                    break;
                                }
                                bos.write(buffer, 0, len);
                            }
                            MainService.this.upgradeByteData = bos.toByteArray();
                            try {
                                in2.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            try {
                                bos.close();
                                BufferedInputStream bufferedInputStream = in2;
                            } catch (IOException e2) {
                                e2.printStackTrace();
                                BufferedInputStream bufferedInputStream2 = in2;
                            }
                        } catch (IOException e3) {
                            e = e3;
                            in = in2;
                        } catch (Throwable th) {
                            th = th;
                            in = in2;
                            try {
                                in.close();
                            } catch (IOException e4) {
                                e4.printStackTrace();
                            }
                            try {
                                bos.close();
                            } catch (IOException e5) {
                                e5.printStackTrace();
                            }
                            throw th;
                        }
                    } catch (IOException e6) {
                        e = e6;
                        try {
                            e.printStackTrace();
                            try {
                                in.close();
                            } catch (IOException e7) {
                                e7.printStackTrace();
                            }
                            try {
                                bos.close();
                            } catch (IOException e8) {
                                e8.printStackTrace();
                            }
                            Log.d("driverlog", "canbox:upgradeByteData.length:" + MainService.this.upgradeByteData.length);
                            MainService.this.totalDataIndex = (MainService.this.upgradeByteData.length + TransportMediator.KEYCODE_MEDIA_PAUSE) / 128;
                            Log.d("driverlog", "canbox:totalDataIndex:" + MainService.this.totalDataIndex);
                            MainService.this.iReadyUpdate = false;
                            MainService.this.mHandler.postDelayed(MainService.this.canboxtThread, 200);
                        } catch (Throwable th2) {
                            th = th2;
                            in.close();
                            bos.close();
                            throw th;
                        }
                    }
                    Log.d("driverlog", "canbox:upgradeByteData.length:" + MainService.this.upgradeByteData.length);
                    MainService.this.totalDataIndex = (MainService.this.upgradeByteData.length + TransportMediator.KEYCODE_MEDIA_PAUSE) / 128;
                    Log.d("driverlog", "canbox:totalDataIndex:" + MainService.this.totalDataIndex);
                    MainService.this.iReadyUpdate = false;
                    MainService.this.mHandler.postDelayed(MainService.this.canboxtThread, 200);
                }
            }
        }).start();
    }

    public void enterIntoUpgradeMcuCheck() {
        new Thread(new Runnable() {
            public void run() {
                File file = new File(MainService.this.getString(R.string.path_upgrade_mcu));
                if (file.exists()) {
                    ByteArrayOutputStream bos = new ByteArrayOutputStream((int) file.length());
                    BufferedInputStream in = null;
                    try {
                        BufferedInputStream in2 = new BufferedInputStream(new FileInputStream(file));
                        try {
                            byte[] buffer = new byte[1024];
                            in2.skip(10240);
                            while (true) {
                                int len = in2.read(buffer, 0, 1024);
                                if (-1 == len) {
                                    break;
                                }
                                bos.write(buffer, 0, len);
                            }
                            MainService.this.mcuUpgradeByteData = bos.toByteArray();
                            try {
                                in2.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            try {
                                bos.close();
                                BufferedInputStream bufferedInputStream = in2;
                            } catch (IOException e2) {
                                e2.printStackTrace();
                                BufferedInputStream bufferedInputStream2 = in2;
                            }
                        } catch (IOException e3) {
                            e = e3;
                            in = in2;
                        } catch (Throwable th) {
                            th = th;
                            in = in2;
                            try {
                                in.close();
                            } catch (IOException e4) {
                                e4.printStackTrace();
                            }
                            try {
                                bos.close();
                            } catch (IOException e5) {
                                e5.printStackTrace();
                            }
                            throw th;
                        }
                    } catch (IOException e6) {
                        e = e6;
                        try {
                            e.printStackTrace();
                            try {
                                in.close();
                            } catch (IOException e7) {
                                e7.printStackTrace();
                            }
                            try {
                                bos.close();
                            } catch (IOException e8) {
                                e8.printStackTrace();
                            }
                            Log.d("driverlog", "mcu:upgradeByteData.length:" + MainService.this.mcuUpgradeByteData.length);
                            MainService.this.mcuTotalDataIndex = (MainService.this.mcuUpgradeByteData.length + TransportMediator.KEYCODE_MEDIA_PAUSE) / 128;
                            Log.d("driverlog", "mcu:totalDataIndex:" + MainService.this.mcuTotalDataIndex);
                            ByteArrayOutputStream tempBuffer = new ByteArrayOutputStream();
                            tempBuffer.write(MainService.this.mcuUpgradeByteData, 0, 4);
                            Mainboard.getInstance().requestUpgradeMcu(tempBuffer.toByteArray());
                        } catch (Throwable th2) {
                            th = th2;
                            in.close();
                            bos.close();
                            throw th;
                        }
                    }
                    Log.d("driverlog", "mcu:upgradeByteData.length:" + MainService.this.mcuUpgradeByteData.length);
                    MainService.this.mcuTotalDataIndex = (MainService.this.mcuUpgradeByteData.length + TransportMediator.KEYCODE_MEDIA_PAUSE) / 128;
                    Log.d("driverlog", "mcu:totalDataIndex:" + MainService.this.mcuTotalDataIndex);
                    ByteArrayOutputStream tempBuffer2 = new ByteArrayOutputStream();
                    tempBuffer2.write(MainService.this.mcuUpgradeByteData, 0, 4);
                    Mainboard.getInstance().requestUpgradeMcu(tempBuffer2.toByteArray());
                }
            }
        }).start();
    }

    public void createLoadingFloatView(String showText) {
        if (this.loadingFloatLayout == null) {
            if (TextUtils.isEmpty(showText)) {
                showText = getString(R.string.msg_loading);
            }
            if (this.mWindowManager == null) {
                this.mWindowManager = (WindowManager) getSystemService("window");
            }
            WindowManager.LayoutParams wmParams = new WindowManager.LayoutParams();
            wmParams.type = 2003;
            wmParams.format = 1;
            wmParams.flags = 131072;
            wmParams.gravity = 51;
            wmParams.x = 677;
            wmParams.y = SyslogAppender.LOG_LOCAL4;
            wmParams.width = 415;
            wmParams.height = 147;
            this.loadingFloatLayout = (FrameLayout) LayoutInflater.from(getApplication()).inflate(R.layout.popup_loading, (ViewGroup) null);
            TextView showTview = (TextView) this.loadingFloatLayout.findViewById(R.id.curLoadingTview);
            final ImageView loadingImg = (ImageView) this.loadingFloatLayout.findViewById(R.id.loadingImg);
            TextView manual = (TextView) this.loadingFloatLayout.findViewById(R.id.manual);
            if (!showText.equals(getString(R.string.msg_change_audio))) {
                manual.setVisibility(8);
            } else {
                manual.setVisibility(0);
                manual.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Mainboard.getInstance().showCarLayer(Mainboard.ECarLayer.ORIGINAL);
                        MainService.this.mHandler.removeCallbacks(MainService.this.removeLoadingRunable);
                        MainService.this.removeLoadingFloatView();
                    }
                });
            }
            showTview.setText(showText);
            this.mWindowManager.addView(this.loadingFloatLayout, wmParams);
            this.mHandler.postDelayed(new Runnable() {
                public void run() {
                    loadingImg.setImageResource(R.drawable.loading_anim);
                    MainService.this.animationDrawable = (AnimationDrawable) loadingImg.getDrawable();
                    MainService.this.animationDrawable.start();
                }
            }, 100);
        }
    }

    public void createNoTouchScreens() {
        if (this.noTouchLayout == null) {
            if (this.mWindowManager == null) {
                this.mWindowManager = (WindowManager) getSystemService("window");
            }
            WindowManager.LayoutParams wmParams = new WindowManager.LayoutParams();
            wmParams.type = VoiceAssistantConst.VALUE_APP_ONLINE_MUSIC;
            wmParams.format = 1;
            wmParams.flags = 40;
            wmParams.gravity = 51;
            wmParams.x = 0;
            wmParams.y = 0;
            wmParams.width = 1280;
            wmParams.height = 480;
            this.noTouchLayout = (MyLinearlayout) LayoutInflater.from(getApplication()).inflate(R.layout.notouch_view, (ViewGroup) null);
            this.noTouchLayout.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View arg0, MotionEvent ev) {
                    Log.e("", "listenlog MyLinearlayout x:" + ev.getX() + " y:" + ev.getY());
                    if (MainService.this.app.service == null || MainService.this.iIsInAndroid) {
                        return false;
                    }
                    Log.e("", "listenlog mytouch MyLinearlayout iIsInReversing = " + MainService.this.app.service.iIsInReversing + ",iIsInOriginal = " + MainService.this.app.service.iIsInOriginal + ",iIsBTConnect = " + MainService.this.app.service.iIsBTConnect + ",iIsInRecorder = " + MainService.this.app.service.iIsBTConnect + ",iIsScrennOff = " + MainService.this.app.service.iIsScrennOff);
                    Log.e("", "noTouchLayout exist");
                    return true;
                }
            });
            Log.e("", "listenlog creat noTouchLayout  = " + (System.currentTimeMillis() - this.startTime));
            try {
                this.mWindowManager.addView(this.noTouchLayout, wmParams);
            } catch (Exception e) {
                Log.d("launcherlog", "canbox:error:mWindowManager.addView(onNoTouchLayout)");
            }
            Bundle bundle = new Bundle();
            bundle.putBoolean("isRed", true);
            this.app.sendMessage(SysConst.ORIGINAL_FLAG, bundle);
        }
    }

    public void removeNoTouchScreens() {
        if (this.noTouchLayout != null) {
            try {
                this.mWindowManager.removeView(this.noTouchLayout);
                this.noTouchLayout = null;
            } catch (Exception e) {
                Log.d("launcherlog", "canbox:error:mWindowManager.removeView(onNoTouchLayout)");
            }
            Bundle bundle = new Bundle();
            bundle.putBoolean("isRed", false);
            this.app.sendMessage(SysConst.ORIGINAL_FLAG, bundle);
        }
    }

    public void removeLoadingFloatView() {
        if (this.loadingFloatLayout != null) {
            try {
                this.animationDrawable.stop();
                this.animationDrawable = null;
            } catch (Exception e) {
                Log.d("launcherlog", "canbox:error:animationDrawable.stop()");
            }
            try {
                this.mWindowManager.removeViewImmediate(this.loadingFloatLayout);
                this.loadingFloatLayout = null;
            } catch (Exception e2) {
                Log.d("launcherlog", "canbox:error:mWindowManager.removeView(bootFloatLayout)");
            }
        }
    }

    /* access modifiers changed from: private */
    public synchronized void setAirplaneMode(boolean enable) {
        this.iIsEnterAirplaneMode = enable;
        if (this.lastAirplaneTime == -1) {
            dealwithAirplaneMode();
        } else {
            long temp = System.currentTimeMillis() - this.lastAirplaneTime;
            this.mHandler.removeCallbacks(this.airplaneRunnable);
            if (temp > ((long) this.spaceTime)) {
                dealwithAirplaneMode();
            } else {
                this.mHandler.postDelayed(this.airplaneRunnable, ((long) this.spaceTime) - temp);
            }
        }
    }

    /* access modifiers changed from: private */
    public void dealwithAirplaneMode() {
        boolean find = true;
        this.lastAirplaneTime = System.currentTimeMillis();
        if (Settings.Global.getInt(getContentResolver(), "airplane_mode_on", 0) != 1) {
            find = false;
        }
        if (find && this.iIsEnterAirplaneMode) {
            return;
        }
        if (find || this.iIsEnterAirplaneMode) {
            this.app.changeAirplane(this.iIsEnterAirplaneMode);
        }
    }

    /* access modifiers changed from: private */
    public void setGpsStatus(boolean enabled) {
        Settings.Secure.setLocationProviderEnabled(getContentResolver(), "gps", enabled);
    }

    private void broadcastWakeUpEvent() {
        Intent intent = new Intent();
        intent.setAction(SysConst.EVENT_MCU_WAKE_UP);
        intent.setFlags(32);
        sendBroadcast(intent);
    }

    private void broadcastEnterstandbyMode() {
        Intent intent = new Intent();
        intent.setAction(SysConst.EVENT_MCU_ENTER_STANDBY);
        sendBroadcast(intent);
    }

    /* access modifiers changed from: private */
    public synchronized void inOrOutAcc(boolean iEnterAcc) {
        Log.e("mcu", "reciev:" + iEnterAcc);
        if (iEnterAcc) {
            enterAcc();
        } else {
            wakeUpFromAcc();
        }
    }

    private void enterAcc() {
        Mainboard.getInstance().readyToStandby();
        if (!LauncherApplication.iAccOff) {
            Log.e("ENTERACC", "enterAcc");
            LauncherApplication.iAccOff = true;
            this.iVoiceSound = false;
            this.iNaviSound = false;
            this.iAUX = false;
            this.app.isOpenAMP = false;
            openOrCloseRelay(false);
            this.app.dismissDialog();
            this.app.requestMainAudioFocus();
            broadcastEnterstandbyMode();
            removeLoadingFloatView();
            this.app.btservice.setBTEnterACC(LauncherApplication.iAccOff);
            EcarManager.getInstance(this).sendAccOffEcar();
            if (this.loadingFloatLayout != null) {
                removeLoadingFloatView();
            }
            this.mHandler.removeCallbacks(this.getWeatherRunnabale);
            checkHadNaviRunning();
            stopOtherAPP();
            if (this.app.launcherHandler != null) {
                this.app.launcherHandler.obtainMessage(1024).sendToTarget();
            }
        }
    }

    private void wakeUpFromAcc() {
        Mainboard.getInstance().readyToWork();
        if (LauncherApplication.iAccOff || !LauncherApplication.iFirst) {
            LauncherApplication.iFirst = true;
            Log.e("ENTERACC", "wakeUpFromAcc");
            LauncherApplication.iAccOff = false;
            this.app.btservice.setBTEnterACC(LauncherApplication.iAccOff);
            this.app.initModel();
            this.mHandler.postDelayed(this.startSettingRunnable, 1500);
            initMcuAndCanbox();
            this.mHandler.removeCallbacks(this.getWeatherRunnabale);
            this.mHandler.postDelayed(this.getWeatherRunnabale, 40000);
            EcarManager.getInstance(this).sendAccOnToEcar();
            autoStartNaviAPP();
            Log.e("ENTERACC", "wakeUpFromAcc end");
            broadcastWakeUpEvent();
        }
    }

    private void autoStartNaviAPP() {
        if (this.app.getNeedStartNaviPkgs()) {
            try {
                this.mHandler.postDelayed(new Runnable() {
                    public void run() {
                        MainService.this.autoStartApp();
                    }
                }, 500);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /* access modifiers changed from: private */
    public void autoStartApp() {
        this.app.startAppByPkg(this.app.getNaviAPP());
        this.app.setNeedStartNaviPkg(false);
    }

    public void stopOtherAPP() {
        List<ActivityManager.RunningTaskInfo> runningTaskInfos = this.mActivityManager.getRunningTasks(10);
        if (runningTaskInfos != null) {
            Log.e("", "enteracc start stop app = " + runningTaskInfos.size());
            for (int i = 0; i < runningTaskInfos.size(); i++) {
                String cmpNameTemp = runningTaskInfos.get(i).topActivity.getPackageName();
                Log.e("", "enteracc start stop cmpNameTemp = " + cmpNameTemp);
                if (!TextUtils.isEmpty(cmpNameTemp) && !cmpNameTemp.equals(getPackageName())) {
                    this.mActivityManager.forceStopPackage(cmpNameTemp);
                }
            }
        }
    }

    public void checkMediaRunning() {
        if (this.app.musicType == 1 || this.app.musicType == 3) {
            this.app.setNeedStartMedia(true);
        }
        this.app.setNeedStartMedia(false);
    }

    public boolean checkHadNaviRunning() {
        List<ActivityManager.RunningTaskInfo> runningTaskInfos = this.mActivityManager.getRunningTasks(10);
        if (runningTaskInfos == null) {
            return false;
        }
        String navi = this.app.getNaviAPP();
        boolean isHasNavi = false;
        String cmpNameTemp = runningTaskInfos.get(0).topActivity.toString();
        if (!TextUtils.isEmpty(cmpNameTemp) && cmpNameTemp.contains(navi)) {
            isHasNavi = true;
        }
        this.app.setNeedStartNaviPkg(isHasNavi);
        return isHasNavi;
    }

    public void addMusicReceiver() {
        if (this.mRemoteControlClientReceiverComponent == null) {
            this.mRemoteControlClientReceiverComponent = new ComponentName(getPackageName(), MusicControlReceiver.class.getName());
            Log.e("", "musicControl addMusicReceiver " + this.mRemoteControlClientReceiverComponent);
            Intent mediaButtonIntent = new Intent("android.intent.action.MEDIA_BUTTON");
            mediaButtonIntent.setComponent(this.mRemoteControlClientReceiverComponent);
            this.audioManager.registerMediaButtonIntent(PendingIntent.getBroadcast(this, 0, mediaButtonIntent, 0), this.mRemoteControlClientReceiverComponent);
        }
    }

    public void unregisterMusicReceiver() {
        if (this.mRemoteControlClientReceiverComponent != null) {
            Log.e("", "musicControl unregisterMusicReceiver " + this.mRemoteControlClientReceiverComponent);
            this.audioManager.unregisterMediaButtonEventReceiver(this.mRemoteControlClientReceiverComponent);
            this.mRemoteControlClientReceiverComponent = null;
        }
    }

    private void addWeatherReceiver() {
        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction(SysConst.EVENT_UNIBROAD_WEATHERDATA);
        registerReceiver(this.getWeatherReceiver, mFilter);
    }

    private void addNetworkReceiver() {
        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        mFilter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        registerReceiver(this.networkReceiver, mFilter);
    }

    private void addUnibroadAudioFocusReceiver() {
        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction(SysConst.EVENT_UNIBROAD_AUDIO_FOCUS);
        mFilter.addAction(SysConst.EVENT_UNIBROAD_AUDIO_REGAIN);
        registerReceiver(this.unibroadAudioFocusReceiver, mFilter);
    }

    private void addVoiceShowOrHideReceiver() {
        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction("com.txznet.txz.record.show");
        mFilter.addAction("com.txznet.txz.record.dismiss");
        registerReceiver(this.voiceShowOrHideReceiver, mFilter);
    }

    private void addRecoveryReceiver() {
        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction("com.touchus.factorytest.recovery");
        mFilter.addAction("com.touchus.factorytest.sdcardreset");
        mFilter.addAction("com.touchus.factorytest.getMcu");
        mFilter.addAction("com.touchus.factorytest.benzetype");
        mFilter.addAction(PubSysConst.ACTION_FACTORY_BREAKSET);
        registerReceiver(this.recoveryReceiver, mFilter);
    }

    /* access modifiers changed from: private */
    public void addBluetoothReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.bluetooth.device.action.FOUND");
        filter.addAction("android.bluetooth.adapter.action.DISCOVERY_FINISHED");
        filter.addAction("android.bluetooth.device.action.BOND_STATE_CHANGED");
        filter.addAction("android.bluetooth.device.action.PAIRING_CANCEL");
        filter.addAction("android.bluetooth.device.action.PAIRING_REQUEST");
        filter.addAction("android.bluetooth.a2dp.profile.action.CONNECTION_STATE_CHANGED");
        filter.addAction("android.bluetooth.headset.profile.action.CONNECTION_STATE_CHANGED");
        registerReceiver(this.bluetoothReceiver, filter);
    }

    private void addEasyConnectReceiver() {
        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction(SysConst.EVENT_EASYCONN_BT_CHECKSTATUS);
        mFilter.addAction(SysConst.EVENT_EASYCONN_A2DP_ACQUIRE);
        mFilter.addAction(SysConst.EVENT_EASYCONN_A2DP_RELEASE);
        mFilter.addAction(SysConst.EVENT_EASYCONN_APP_QUIT);
        registerReceiver(this.easyConnectReceiver, mFilter);
    }

    public void sendVoiceWakeupState(boolean iOpen) {
        Intent intent = new Intent();
        intent.setAction("com.unibroad.vwakeup.OPEN");
        intent.putExtra("open", iOpen);
        sendBroadcast(intent);
    }

    /* access modifiers changed from: private */
    public String getCurrentTopAppPkg() {
        List<ActivityManager.RunningTaskInfo> runningTaskInfos = this.mActivityManager.getRunningTasks(1);
        if (runningTaskInfos == null || runningTaskInfos.size() == 0) {
            return "";
        }
        return runningTaskInfos.get(0).topActivity.getPackageName();
    }

    public void getWeather() {
        Intent intent = new Intent();
        intent.setAction("com.unibroad.getweather");
        intent.putExtra("cityName", this.app.gpsCityName);
        sendBroadcast(intent);
    }

    public void regainWeather() {
        this.mHandler.removeCallbacks(this.getWeatherRunnabale);
        this.mHandler.postDelayed(this.getWeatherRunnabale, 2000);
    }

    public void sendEasyConnectBroadcast(String action) {
        Intent intent = new Intent();
        intent.setAction(action);
        if (action.equals(SysConst.EVENT_EASYCONN_BT_OPENED)) {
            intent.putExtra(Contacts.PeopleColumns.NAME, BluetoothService.deviceName);
        }
        sendBroadcast(intent);
    }

    public void closeNaviThread() {
        this.mHandler.removeCallbacks(this.checkNavigationRunnable);
        DesktopLayout.hide();
    }

    public void openNaviThread() {
        this.mHandler.postDelayed(this.checkNavigationRunnable, 500);
    }

    /* access modifiers changed from: private */
    public void startLocalBT() {
        if (this.adapter == null) {
            this.adapter = BluetoothAdapter.getDefaultAdapter();
            if (!this.adapter.isEnabled()) {
                this.adapter.enable();
            }
            if (Build.MODEL.contains("c200_zlh")) {
                this.adapter.setName("ZLH");
            } else {
                this.adapter.setName("ANDROID BT");
            }
            this.adapter.setScanMode(23);
            Log.e("", "btstate isEnabled = " + this.adapter.isEnabled());
            Iterator<BluetoothDevice> it = this.adapter.getBondedDevices().iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                BluetoothDevice bluetoothDevice = it.next();
                if (this.carBTname.equals(bluetoothDevice.getName())) {
                    this.benzDevice = bluetoothDevice;
                    Log.e("", "btstate benzDevice = " + this.benzDevice.getName());
                    break;
                } else if (this.testBTname.contains(bluetoothDevice.getName())) {
                    this.benzDevice = bluetoothDevice;
                    Log.e("", "btstate benzDevice = " + this.benzDevice.getName());
                }
            }
            this.adapter.getProfileProxy(this, this.mProfileServiceListener, 2);
            this.adapter.getProfileProxy(this, this.mProfileServiceListener, 1);
        }
    }

    private void changeAudioSource() {
        Message message = new Message();
        message.what = 2001;
        if (BenzModel.benzCan == BenzModel.EBenzCAN.ZMYT) {
            message.obj = 40;
        } else {
            message.obj = 25;
        }
        if (!getBTA2dpState()) {
            requestBtAudio(true);
            this.mHandler.sendMessage(message);
        } else if (!this.iAUX) {
            requestBtAudio(false);
            this.mHandler.sendMessage(message);
        }
    }

    private void requestBtAudio(boolean isForce) {
        boolean z;
        boolean z2;
        boolean z3;
        boolean z4 = true;
        if (BenzModel.benzCan == BenzModel.EBenzCAN.WCL) {
            Mainboard instance = Mainboard.getInstance();
            BenzModel.EBenzTpye eBenzTpye = BenzModel.benzTpye;
            boolean z5 = this.app.radioPos != 0;
            if (this.app.naviPos == 0) {
                z3 = true;
            } else {
                z3 = false;
            }
            if (LauncherApplication.isBT) {
                z4 = false;
            }
            instance.forceRequestBTAudio2(isForce, eBenzTpye, z5, z3, z4, this.app.usbPos + 1);
            return;
        }
        Mainboard instance2 = Mainboard.getInstance();
        BenzModel.EBenzTpye eBenzTpye2 = BenzModel.benzTpye;
        if (this.app.radioPos == 0) {
            z = false;
        } else {
            z = true;
        }
        if (this.app.naviPos == 0) {
            z2 = true;
        } else {
            z2 = false;
        }
        if (LauncherApplication.isBT) {
            z4 = false;
        }
        instance2.forceRequestBTAudio(isForce, eBenzTpye2, z, z2, z4, this.app.usbPos + 1);
    }

    public void btMusicConnect() {
        if (this.app.playpos == 1) {
            if (!this.app.isOpenAMP) {
                this.app.isOpenAMP = true;
                this.app.abandonMainGainAudioFocus();
                Mainboard.getInstance().openOrCloseMediaRelay(true);
                if (Build.MODEL.equals("c200") || Build.MODEL.equals("c200_en") || Build.MODEL.equals("c200_ks")) {
                    openOrCloseRelay(true);
                }
            }
            setConnectedMusicVoice();
        } else if (this.app.screenPos == 6) {
        } else {
            if (SysConst.isBT()) {
                Log.e("", "Audio type is BT");
                if (this.adapter == null || !this.adapter.isEnabled()) {
                    startLocalBT();
                } else {
                    this.adapter.setScanMode(23);
                }
                this.mHandler.postDelayed(new Runnable() {
                    public void run() {
                        MainService.this.GLK_BTConnect();
                    }
                }, 30000);
                connect(this.benzA2dp);
                return;
            }
            Log.e("", "Audio type is AUX");
            if (!this.iAUX) {
                Message message = new Message();
                message.what = 2001;
                if (BenzModel.benzCan == BenzModel.EBenzCAN.ZMYT) {
                    message.obj = 40;
                } else {
                    message.obj = 25;
                }
                this.mHandler.sendMessage(message);
                requestBtAudio(false);
            }
        }
    }

    /* access modifiers changed from: private */
    public void GLK_BTConnect() {
        if (!getIsBTState()) {
            Set<BluetoothDevice> listSet = this.adapter.getBondedDevices();
            Iterator<BluetoothDevice> it = listSet.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                BluetoothDevice bluetoothDevice = it.next();
                if (this.carBTname.equals(bluetoothDevice.getName())) {
                    this.benzDevice = bluetoothDevice;
                    Log.e("", "btstate benzDevice111 = " + this.benzDevice.getName());
                    if (this.benzDevice != null && !getBTA2dpState()) {
                        Log.e("", "btstate a2dp connect = " + this.benzDevice.getName());
                        this.benzA2dp.connect(this.benzDevice);
                    }
                    this.iExist = true;
                }
            }
            if (listSet.size() == 0) {
                this.iExist = false;
                this.adapter.startDiscovery();
            }
        }
    }

    /* access modifiers changed from: private */
    public boolean getBTA2dpState() {
        boolean z = false;
        this.logger.debug("btstate SysConst.isBT = " + SysConst.isBT() + ",iAUX = " + this.iAUX);
        if (!SysConst.isBT()) {
            return this.iAUX;
        }
        if (this.benzA2dp == null) {
            return false;
        }
        Log.e("", "btstate iBenzA2dpConnected = " + this.iBenzA2dpConnected);
        Log.e("", "btstate  A2dpState = " + this.benzA2dp.getState(this.benzDevice));
        boolean z2 = this.iBenzA2dpConnected;
        if (this.benzA2dp.getState(this.benzDevice) == 2) {
            z = true;
        }
        return z | z2;
    }

    public boolean getIsBTState() {
        return getBTA2dpState() && this.iAUX;
    }

    private int getBTHeadsetState() {
        Log.e("", "btstate  HeadsetState = " + this.benzhHeadset.getConnectionState(this.benzDevice));
        return this.benzhHeadset.getConnectionState(this.benzDevice);
    }

    private void connect(BluetoothA2dp a2dp) {
        if (a2dp == null) {
            Log.e("", "btstate a2dp null");
        } else if (!this.iIsInOriginal) {
            changeAudioSource();
        }
    }

    public void forceStopApp(String pkg) {
        this.mActivityManager.forceStopPackage(pkg);
    }

    /* access modifiers changed from: private */
    public void openRelay() {
        if (!this.iIsScrennOff) {
            if (this.iVoiceSound || this.iNaviSound || this.app.playpos != 1 || this.app.isOpenAMP) {
                try {
                    Mainboard.getInstance().openOrCloseRelay(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void closeRelay(boolean iForceClose) {
        if (this.app.isOpenAMP && (Build.MODEL.equals("c200") || Build.MODEL.equals("c200_en") || Build.MODEL.equals("c200_ks"))) {
            return;
        }
        if (iForceClose || (!this.iVoiceSound && !this.iNaviSound)) {
            try {
                Mainboard.getInstance().openOrCloseRelay(false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void openOrCloseRelay(boolean iOpen) {
        if (iOpen) {
            this.mHandler.postDelayed(new Runnable() {
                public void run() {
                    MainService.this.openRelay();
                }
            }, 200);
        } else {
            this.mHandler.postDelayed(new Runnable() {
                public void run() {
                    MainService.this.closeRelay(false);
                }
            }, 100);
        }
    }

    /* access modifiers changed from: private */
    public void setConnectedMusicVoice() {
        this.app.setTypeMute(3, false);
        if (this.app.btservice.blueMusicFocus) {
            LauncherApplication launcherApplication = this.app;
            this.app.getClass();
            launcherApplication.setTypeMute(200, false);
            return;
        }
        LauncherApplication launcherApplication2 = this.app;
        this.app.getClass();
        launcherApplication2.setTypeMute(200, true);
    }

    /* access modifiers changed from: private */
    public void setMusicMute() {
        this.app.setTypeMute(3, true);
        LauncherApplication launcherApplication = this.app;
        this.app.getClass();
        launcherApplication.setTypeMute(200, true);
    }

    private void registerGPSListener() {
        this.gpsManager = (LocationManager) getSystemService("location");
        this.gpsManager.requestLocationUpdates("gps", 2000, 6.0f, this.gpsLocatListener);
        this.gpsManager.addGpsStatusListener(this.gpsStateListener);
    }

    private void unregisterGPSListener() {
        this.gpsManager.removeGpsStatusListener(this.gpsStateListener);
        this.gpsManager.removeUpdates(this.gpsLocatListener);
    }

    private class GpsLocationListener implements LocationListener {
        private GpsLocationListener() {
        }

        /* synthetic */ GpsLocationListener(MainService mainService, GpsLocationListener gpsLocationListener) {
            this();
        }

        public void onLocationChanged(Location location) {
            if (location != null && !MainService.this.app.isOriginalTime) {
                Log.d("launcherlog", "location : " + location.getTime());
                MainService.this.getGpsTime(location);
            }
        }

        public void onProviderDisabled(String provider) {
            Log.d("launcherlog", "ProviderDisabled : " + provider);
        }

        public void onProviderEnabled(String provider) {
            Log.d("launcherlog", "ProviderEnabled : " + provider);
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.d("launcherlog", "StatusChanged : " + provider + status);
        }
    }

    public static String transferLongToDate(Long millSec) {
        return sdf.format(new Date(millSec.longValue()));
    }

    /* access modifiers changed from: private */
    public void getGpsTime(Location location) {
        String[] y;
        String[] d = sdf.format(new Date(location.getTime())).split(" ");
        if (d != null && d.length > 0 && (y = d[0].split("/")) != null && y.length == 3) {
            int year = Integer.parseInt(y[0]);
            int month = Integer.parseInt(y[1]);
            int day = Integer.parseInt(y[2]);
            String[] t = d[1].split(":");
            int hour = Integer.parseInt(t[0]);
            int min = Integer.parseInt(t[1]);
            int second = Integer.parseInt(t[2]);
            Log.d("launcherlog", "location : min = " + min + ",app.min =" + this.app.min);
            if (this.app.min != min) {
                this.app.year = year;
                this.app.month = month;
                this.app.day = day;
                this.app.hour = hour;
                this.app.min = min;
                this.app.second = second;
                Calendar cal = Calendar.getInstance();
                cal.set(year, month - 1, day, hour, min, second);
                SystemClock.setCurrentTimeMillis(cal.getTimeInMillis());
                this.app.sendMessage(1009, (Bundle) null);
            }
        }
    }

    private void registerSystemTimeReceiver() {
        this.systemTimeReceiver = new SystemTimeReceiver();
        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction("android.intent.action.DATE_CHANGED");
        mFilter.addAction("android.intent.action.TIME_SET");
        mFilter.addAction("android.intent.action.TIME_TICK");
        registerReceiver(this.systemTimeReceiver, mFilter);
    }

    private void unregisterSystemTimeReceiver() {
        if (this.systemTimeReceiver != null) {
            unregisterReceiver(this.systemTimeReceiver);
        }
    }

    /* access modifiers changed from: private */
    public void handlerMsg(Message msg) {
        if (msg.what == 2001) {
            createLoadingFloatView(getString(R.string.msg_change_audio));
            this.mHandler.postDelayed(this.removeLoadingRunable, (long) (((Integer) msg.obj).intValue() * 1000));
        } else if (msg.what == 6001) {
            Bundle bundle = msg.getData();
            byte code = bundle.getByte(SysConst.IDRIVER_ENUM);
            byte state = bundle.getByte(SysConst.IDRIVER_STATE_ENUM);
            if (code != Mainboard.EIdriverEnum.POWER_OFF.getCode() || state != Mainboard.EIdriverEnum.DOWN.getCode()) {
                return;
            }
            if (!this.iInAccState) {
                this.iInAccState = true;
                Mainboard.getInstance().enterIntoAcc();
                return;
            }
            this.iInAccState = false;
            Mainboard.getInstance().wakeupMcu();
        } else if (msg.what == 1123) {
            int index = msg.arg1;
            int offset = index * 128;
            if (index < this.totalDataIndex) {
                ByteArrayOutputStream tempBuffer = new ByteArrayOutputStream();
                if (index < this.totalDataIndex - 1) {
                    tempBuffer.write(this.upgradeByteData, offset, 128);
                } else {
                    tempBuffer.write(this.upgradeByteData, offset, this.upgradeByteData.length - offset);
                }
                Mainboard.getInstance().sendUpdateCanboxInfoByIndex(index, tempBuffer.toByteArray());
            }
        } else if (msg.what == 1130) {
            Mainboard.getInstance().sendUpgradeMcuHeaderInfo(this.mcuTotalDataIndex);
        } else if (msg.what == 1133) {
            int index2 = msg.arg1;
            int offset2 = index2 * 128;
            if (index2 < this.mcuTotalDataIndex) {
                ByteArrayOutputStream tempBuffer2 = new ByteArrayOutputStream();
                if (index2 < this.mcuTotalDataIndex - 1) {
                    tempBuffer2.write(this.mcuUpgradeByteData, offset2, 128);
                } else {
                    tempBuffer2.write(this.mcuUpgradeByteData, offset2, this.mcuUpgradeByteData.length - offset2);
                }
                Mainboard.getInstance().sendUpdateMcuInfoByIndex(index2, tempBuffer2.toByteArray());
            }
        } else if (msg.what == 1033) {
            this.app.sendStartVoiceAssistantEvent();
        } else if (msg.what == 1036) {
            UtilTools.sendKeyeventToSystem(3);
            final int pos = ((Integer) msg.obj).intValue();
            this.mHandler.postDelayed(new Runnable() {
                public void run() {
                    MainService.this.app.launcherHandler.obtainMessage(SysConst.VOICE_WAKEUP_MENU, Integer.valueOf(pos)).sendToTarget();
                }
            }, 200);
        } else if (msg.what == 1041) {
            if (this.benzA2dp != null) {
                Log.e("", "btstate a2dp iBenzA2dpConnected = " + this.iBenzA2dpConnected);
                if (getIsBTState()) {
                    this.mHandler.post(this.removeLoadingRunable);
                    BTConnectDialog.getInstance().dissShow();
                    setConnectedMusicVoice();
                    return;
                }
                setMusicMute();
            }
        } else if (msg.what == 1042 && this.benzhHeadset != null) {
            switch (getBTHeadsetState()) {
                case 0:
                    Log.e("", "btstate headset RECONNECTED");
                    return;
                default:
                    return;
            }
        }
    }

    static class Mhandler extends Handler {
        private WeakReference<MainService> target;

        public Mhandler(MainService activity) {
            this.target = new WeakReference<>(activity);
        }

        public void handleMessage(Message msg) {
            if (this.target.get() != null) {
                ((MainService) this.target.get()).handlerMsg(msg);
            }
        }
    }

    class MainListenner implements IMainboardEventLisenner {
        private static /* synthetic */ int[] $SWITCH_TABLE$com$backaudio$android$driver$Mainboard$ECarLayer;
        private static /* synthetic */ int[] $SWITCH_TABLE$com$backaudio$android$driver$Mainboard$EReverseTpye;
        private static /* synthetic */ int[] $SWITCH_TABLE$com$backaudio$android$driver$Mainboard$EReverserMediaSet;
        Runnable requestAudioRunnable = new Runnable() {
            public void run() {
                MainService.this.app.requestMainGainAudioFocus();
            }
        };

        static /* synthetic */ int[] $SWITCH_TABLE$com$backaudio$android$driver$Mainboard$ECarLayer() {
            int[] iArr = $SWITCH_TABLE$com$backaudio$android$driver$Mainboard$ECarLayer;
            if (iArr == null) {
                iArr = new int[Mainboard.ECarLayer.values().length];
                try {
                    iArr[Mainboard.ECarLayer.ANDROID.ordinal()] = 1;
                } catch (NoSuchFieldError e) {
                }
                try {
                    iArr[Mainboard.ECarLayer.BT_CONNECT.ordinal()] = 6;
                } catch (NoSuchFieldError e2) {
                }
                try {
                    iArr[Mainboard.ECarLayer.DV.ordinal()] = 3;
                } catch (NoSuchFieldError e3) {
                }
                try {
                    iArr[Mainboard.ECarLayer.ORIGINAL.ordinal()] = 5;
                } catch (NoSuchFieldError e4) {
                }
                try {
                    iArr[Mainboard.ECarLayer.ORIGINAL_REVERSE.ordinal()] = 7;
                } catch (NoSuchFieldError e5) {
                }
                try {
                    iArr[Mainboard.ECarLayer.QUERY.ordinal()] = 10;
                } catch (NoSuchFieldError e6) {
                }
                try {
                    iArr[Mainboard.ECarLayer.RECORDER.ordinal()] = 2;
                } catch (NoSuchFieldError e7) {
                }
                try {
                    iArr[Mainboard.ECarLayer.REVERSE.ordinal()] = 9;
                } catch (NoSuchFieldError e8) {
                }
                try {
                    iArr[Mainboard.ECarLayer.REVERSE_360.ordinal()] = 4;
                } catch (NoSuchFieldError e9) {
                }
                try {
                    iArr[Mainboard.ECarLayer.SCREEN_OFF.ordinal()] = 8;
                } catch (NoSuchFieldError e10) {
                }
                $SWITCH_TABLE$com$backaudio$android$driver$Mainboard$ECarLayer = iArr;
            }
            return iArr;
        }

        static /* synthetic */ int[] $SWITCH_TABLE$com$backaudio$android$driver$Mainboard$EReverseTpye() {
            int[] iArr = $SWITCH_TABLE$com$backaudio$android$driver$Mainboard$EReverseTpye;
            if (iArr == null) {
                iArr = new int[Mainboard.EReverseTpye.values().length];
                try {
                    iArr[Mainboard.EReverseTpye.ADD_REVERSE.ordinal()] = 1;
                } catch (NoSuchFieldError e) {
                }
                try {
                    iArr[Mainboard.EReverseTpye.ORIGINAL_REVERSE.ordinal()] = 2;
                } catch (NoSuchFieldError e2) {
                }
                try {
                    iArr[Mainboard.EReverseTpye.REVERSE_360.ordinal()] = 3;
                } catch (NoSuchFieldError e3) {
                }
                $SWITCH_TABLE$com$backaudio$android$driver$Mainboard$EReverseTpye = iArr;
            }
            return iArr;
        }

        static /* synthetic */ int[] $SWITCH_TABLE$com$backaudio$android$driver$Mainboard$EReverserMediaSet() {
            int[] iArr = $SWITCH_TABLE$com$backaudio$android$driver$Mainboard$EReverserMediaSet;
            if (iArr == null) {
                iArr = new int[Mainboard.EReverserMediaSet.values().length];
                try {
                    iArr[Mainboard.EReverserMediaSet.FLAT.ordinal()] = 2;
                } catch (NoSuchFieldError e) {
                }
                try {
                    iArr[Mainboard.EReverserMediaSet.MUTE.ordinal()] = 1;
                } catch (NoSuchFieldError e2) {
                }
                try {
                    iArr[Mainboard.EReverserMediaSet.NOMAL.ordinal()] = 3;
                } catch (NoSuchFieldError e3) {
                }
                try {
                    iArr[Mainboard.EReverserMediaSet.QUERY.ordinal()] = 4;
                } catch (NoSuchFieldError e4) {
                }
                $SWITCH_TABLE$com$backaudio$android$driver$Mainboard$EReverserMediaSet = iArr;
            }
            return iArr;
        }

        MainListenner() {
        }

        public void onEnterStandbyMode() {
            MainService.this.logger.debug(String.valueOf(MainService.this.MYLOG) + "<<<onEnterStandbyMode");
            MainService.this.inOrOutAcc(true);
        }

        public void onWakeUp(Mainboard.ECarLayer eCarLayer) {
            MainService.this.inOrOutAcc(false);
            onShowOrHideCarLayer(eCarLayer);
            MainService.this.logger.debug(String.valueOf(MainService.this.MYLOG) + "<<<onWakeUp ECarLayer = " + eCarLayer.toString());
        }

        public void obtainVersionDate(String versionDate) {
            MainService.this.logger.debug(String.valueOf(MainService.this.MYLOG) + "<<<obtainVersionDate\t\tversionDate--" + versionDate);
        }

        public void obtainVersionNumber(String versionNumber) {
            MainService.this.logger.debug(String.valueOf(MainService.this.MYLOG) + "<<<obtainVersionNumber\t\tversionNumber--" + versionNumber);
            MainService.this.app.curMcuVersion = versionNumber;
        }

        public void onCanboxUpgradeState(Mainboard.ECanboxUpgrade info) {
            if (info == Mainboard.ECanboxUpgrade.READY_UPGRADING) {
                MainService.this.mHandler.removeCallbacks(MainService.this.canboxtThread);
                MainService.this.iReadyUpdate = true;
                Message msg = MainService.this.mHandler.obtainMessage(SysConst.UPGRADE_CUR_DATA);
                msg.arg1 = 0;
                msg.sendToTarget();
            } else if (info == Mainboard.ECanboxUpgrade.FINISH) {
                MainService.this.app.sendMessage(SysConst.UPGRADE_FINISH, (Bundle) null);
                MainService.this.iReadyUpdate = false;
            } else {
                MainService.this.app.sendMessage(SysConst.UPGRADE_ERROR, (Bundle) null);
            }
            MainService.this.logger.debug(String.valueOf(MainService.this.MYLOG) + "<<<onCanboxUpgradeState\t\tEUpgrade--" + info.toString());
        }

        public void onCanboxUpgradeForGetDataByIndex(int nextIndex) {
            Log.d("driverlog", "canbox:nextIndex:" + (nextIndex + 1));
            Message msg = MainService.this.mHandler.obtainMessage(SysConst.UPGRADE_CUR_DATA);
            msg.arg1 = nextIndex + 1;
            msg.sendToTarget();
            MainService.this.logger.debug(String.valueOf(MainService.this.MYLOG) + "<<<onCanboxUpgradeForGetDataByIndex\t\tnextIndex--" + (nextIndex + 1));
            int per = (int) (((((double) nextIndex) * 1.0d) / ((double) MainService.this.totalDataIndex)) * 100.0d);
            if (per <= 100) {
                Bundle bundle = new Bundle();
                bundle.putString(SysConst.FLAG_UPGRADE_PER, String.valueOf(per) + "%");
                MainService.this.app.sendMessage(SysConst.UPGRADE_UPGRADE_PER, bundle);
            }
        }

        public void onMcuUpgradeState(Mainboard.EUpgrade info) {
            if (info == Mainboard.EUpgrade.GET_HERDER) {
                MainService.this.mHandler.obtainMessage(SysConst.MCU_UPGRADE_HEADER).sendToTarget();
            } else if (info == Mainboard.EUpgrade.FINISH) {
                MainService.this.app.sendMessage(SysConst.MCU_UPGRADE_FINISH, (Bundle) null);
            } else if (info == Mainboard.EUpgrade.ERROR) {
                MainService.this.app.sendMessage(SysConst.UPGRADE_ERROR, (Bundle) null);
            }
        }

        public void onMcuUpgradeForGetDataByIndex(int nextIndex) {
            MainService.this.logger.debug("driverlog", (Object) "mcu:nextIndex:" + nextIndex);
            Message msg = MainService.this.mHandler.obtainMessage(SysConst.MCU_UPGRADE_CUR_DATA);
            msg.arg1 = nextIndex;
            msg.sendToTarget();
            int per = (int) (((((double) nextIndex) * 1.0d) / ((double) MainService.this.mcuTotalDataIndex)) * 100.0d);
            if (per <= 100) {
                Bundle bundle = new Bundle();
                bundle.putString(SysConst.FLAG_UPGRADE_PER, String.valueOf(per) + "%");
                MainService.this.app.sendMessage(SysConst.UPGRADE_UPGRADE_PER, bundle);
            }
        }

        public void onCanboxInfo(String info) {
            MainService.this.logger.debug(String.valueOf(MainService.this.MYLOG) + "<<<onCanboxInfo\t\tinfo--" + info);
            MainService.this.app.curCanboxVersion = info;
            MainService.this.iConnectCanbox = true;
            if (!TextUtils.isEmpty(MainService.this.app.curCanboxVersion)) {
                Log.e("", "onCanboxInfo before BenzModel.benzTpye = " + BenzModel.benzTpye);
                if (info.contains("BEZ")) {
                    BenzModel.benzCan = BenzModel.EBenzCAN.XBS;
                } else if (info.contains("YT") || info.contains("YX")) {
                    BenzModel.benzCan = BenzModel.EBenzCAN.ZMYT;
                } else if (info.contains("Benz")) {
                    BenzModel.benzCan = BenzModel.EBenzCAN.ZHTD;
                } else if (info.contains("BENZ")) {
                    BenzModel.benzCan = BenzModel.EBenzCAN.WCL;
                }
                MainService.this.app.sendMessage(SysConst.CANBOX_INFO_UPDATE, (Bundle) null);
            }
        }

        public void onHandleIdriver(Mainboard.EIdriverEnum value, Mainboard.EBtnStateEnum state) {
            boolean z;
            if (MainService.this.loadingFloatLayout == null && state == Mainboard.EBtnStateEnum.BTN_DOWN) {
                MainService.this.logger.debug(String.valueOf(MainService.this.MYLOG) + "<<<onHandleIdriver\t\tEIdriverEnum--" + value.toString() + ";\tEBtnStateEnum--" + state.toString());
                Log.e("systemtime", " recv " + System.currentTimeMillis());
                if (value == Mainboard.EIdriverEnum.SPEECH) {
                    if (MainService.this.app.isSpeechKeyOpen) {
                        if (BenzModel.benzCan == BenzModel.EBenzCAN.XBS) {
                            if (!MainService.this.iVoiceSound) {
                                MainService.this.app.serviceHandler.sendEmptyMessage(SysConst.START_VOICE_APP);
                            }
                        } else if (MainService.this.iVoiceSound) {
                            UtilTools.sendKeyeventToSystem(4);
                        } else {
                            MainService.this.app.serviceHandler.sendEmptyMessage(SysConst.START_VOICE_APP);
                        }
                        Mainboard.getInstance().showCarLayer(Mainboard.ECarLayer.ANDROID);
                        return;
                    }
                    Mainboard.getInstance().showCarLayer(Mainboard.ECarLayer.ORIGINAL);
                } else if (value != Mainboard.EIdriverEnum.SPEAKOFF || !MainService.this.iVoiceSound) {
                    if (!MainService.this.iIsInReversing && MainService.this.iIsInAndroid) {
                        if (MainService.this.app.playpos == 1 && MainService.this.app.isOpenAMP) {
                            int volume = MainService.this.mSpUtilK.getInt(SysConst.mediaVoice, 5);
                            if (value == Mainboard.EIdriverEnum.VOL_ADD || value == Mainboard.EIdriverEnum.K_VOL_ADD) {
                                int volume2 = volume + 1;
                                if (volume2 > 9) {
                                    volume2 = 9;
                                }
                                Mainboard.getInstance().setAllHornSoundValue(SysConst.mediaBasicNum, SysConst.num[volume2], 0, SysConst.num[volume2], SysConst.num[volume2]);
                                MainService.this.mSpUtilK.putInt(SysConst.mediaVoice, volume2);
                            } else if (value == Mainboard.EIdriverEnum.VOL_DES || value == Mainboard.EIdriverEnum.K_VOL_DES) {
                                int volume3 = volume - 1;
                                if (volume3 < 1) {
                                    volume3 = 1;
                                }
                                Mainboard.getInstance().setAllHornSoundValue(SysConst.mediaBasicNum, SysConst.num[volume3], 0, SysConst.num[volume3], SysConst.num[volume3]);
                                MainService.this.mSpUtilK.putInt(SysConst.mediaVoice, volume3);
                            } else if (value == Mainboard.EIdriverEnum.MUTE) {
                                MainService mainService = MainService.this;
                                if (MainService.this.isAMPPowerOpen) {
                                    z = false;
                                } else {
                                    z = true;
                                }
                                mainService.isAMPPowerOpen = z;
                                Mainboard.getInstance().powerAudio(MainService.this.isAMPPowerOpen);
                            }
                        }
                        if (!MainService.this.app.iCurrentInLauncher || MainService.this.iVoiceSound) {
                            int code = -1;
                            if (value == Mainboard.EIdriverEnum.LEFT) {
                                code = 21;
                            } else if (value == Mainboard.EIdriverEnum.TURN_LEFT) {
                                code = 21;
                                if (MainService.this.getCurrentTopAppPkg().equals(MainService.this.getString(R.string.pkg_navi))) {
                                    MainService.this.decMap(true);
                                }
                            } else if (value == Mainboard.EIdriverEnum.TURN_RIGHT) {
                                code = 22;
                                if (MainService.this.getCurrentTopAppPkg().equals(MainService.this.getString(R.string.pkg_navi))) {
                                    MainService.this.decMap(false);
                                }
                            } else if (value == Mainboard.EIdriverEnum.RIGHT) {
                                code = 22;
                            } else if (value == Mainboard.EIdriverEnum.UP) {
                                code = 19;
                            } else if (value == Mainboard.EIdriverEnum.DOWN) {
                                code = 20;
                            } else if (value == Mainboard.EIdriverEnum.PRESS) {
                                if (!UtilTools.isFastDoubleClick()) {
                                    code = 23;
                                } else {
                                    return;
                                }
                            } else if (value == Mainboard.EIdriverEnum.BACK) {
                                code = 4;
                            } else if (value == Mainboard.EIdriverEnum.HOME) {
                                code = 3;
                            } else if (value == Mainboard.EIdriverEnum.STAR_BTN) {
                                code = 3;
                            } else if (value == Mainboard.EIdriverEnum.NAVI) {
                                code = 3;
                            } else if (value == Mainboard.EIdriverEnum.BT || value == Mainboard.EIdriverEnum.PICK_UP) {
                                if (BluetoothService.bluetoothStatus != EPhoneStatus.UNCONNECT) {
                                    UtilTools.sendKeyeventToSystem(3);
                                    Bundle bundle = new Bundle();
                                    bundle.putByte(SysConst.IDRIVER_ENUM, value.getCode());
                                    bundle.putByte(SysConst.IDRIVER_STATE_ENUM, state.getCode());
                                    MainService.this.app.sendMessage(SysConst.IDRVIER_STATE, bundle);
                                } else {
                                    return;
                                }
                            }
                            if (code != -1) {
                                UtilTools.sendKeyeventToSystem(code);
                                return;
                            }
                            return;
                        }
                        Bundle bundle2 = new Bundle();
                        bundle2.putByte(SysConst.IDRIVER_ENUM, value.getCode());
                        bundle2.putByte(SysConst.IDRIVER_STATE_ENUM, state.getCode());
                        MainService.this.app.sendMessage(SysConst.IDRVIER_STATE, bundle2);
                    }
                } else if (MainService.this.app.isSpeechKeyOpen) {
                    UtilTools.sendKeyeventToSystem(4);
                }
            }
        }

        public void onAirInfo(AirInfo info) {
            MainService.this.app.airInfo = info;
            Bundle bun = new Bundle();
            bun.putParcelable(SysConst.FLAG_AIR_INFO, info);
            MainService.this.app.sendMessage(1012, bun);
            MainService.this.logger.debug(String.valueOf(MainService.this.MYLOG) + "<<<onAirInfo\t\tAirInfo--" + info.toString());
        }

        public void onCarBaseInfo(CarBaseInfo info) {
            MainService.this.app.carBaseInfo = info;
            Bundle bun = new Bundle();
            bun.putParcelable(SysConst.FLAG_CAR_BASE_INFO, info);
            MainService.this.app.sendMessage(1007, bun);
            Log.d(MainService.this.MYLOG, "<<<onCarBaseInfo\t\tCarBaseInfo--" + info.toString());
        }

        public void onTime(int year, int month, int day, int timeFormat, int time, int min, int sec) {
            MainService.this.logger.debug(String.valueOf(MainService.this.MYLOG) + "<<<settingSystemOfTime:\tyear--" + year + ";\tmonth--" + month + ";\tday--" + day + ";\thour--" + time + ";\tmin--" + min + ";\tsecond--" + sec + ";\ttimeFormat--" + timeFormat);
            if (MainService.this.app.year == year && MainService.this.app.month == month && MainService.this.app.day == day) {
                MainService.this.app.isOriginalTime = true;
            } else {
                MainService.this.app.isOriginalTime = false;
            }
            if (MainService.this.app.min != min && MainService.this.app.isOriginalTime) {
                MainService.this.app.year = year;
                MainService.this.app.month = month;
                MainService.this.app.day = day;
                MainService.this.app.hour = time;
                MainService.this.app.min = min;
                MainService.this.app.second = sec;
                Calendar cal = Calendar.getInstance();
                cal.set(year, month - 1, day, time, min, sec);
                SystemClock.setCurrentTimeMillis(cal.getTimeInMillis());
                MainService.this.app.sendMessage(1009, (Bundle) null);
            }
        }

        public void onOriginalCarView(Mainboard.EControlSource source, boolean iOriginal) {
            MainService.this.logger.debug(String.valueOf(MainService.this.MYLOG) + "<<<onOriginalCarView:\tsource--" + source + ";\tiOriginal--" + iOriginal + ",source == EControlSource.AUX = " + (source == Mainboard.EControlSource.AUX));
            if (MainService.this.app.playpos == 1) {
                LauncherApplication.isBT = false;
                MainService.this.iAUX = true;
            } else if (MainService.this.app.isTestOpen) {
                MainService.this.iAUX = true;
                MainService.this.mHandler.post(MainService.this.removeLoadingRunable);
            } else if (source == Mainboard.EControlSource.AUX) {
                MainService.this.logger.debug(String.valueOf(MainService.this.MYLOG) + " isAUX = true");
                if (!MainService.this.iAUX) {
                    MainService.this.iAUX = true;
                    MainService.this.mHandler.removeCallbacks(this.requestAudioRunnable);
                    if (MainService.this.getIsBTState() && !MainService.this.iIsInAndroid) {
                        MainService.this.app.interAndroidView();
                    }
                    if (MainService.this.getIsBTState() && MainService.this.app.lastmusicType != 0) {
                        MainService.this.app.abandonMainGainAudioFocus();
                    }
                }
                if (!BenzModel.isBenzGLK()) {
                    MainService.this.mHandler.post(MainService.this.removeLoadingRunable);
                } else if (MainService.this.getIsBTState()) {
                    MainService.this.mHandler.post(MainService.this.removeLoadingRunable);
                }
            } else {
                if (MainService.this.iAUX) {
                    if (BenzModel.benzCan == BenzModel.EBenzCAN.ZMYT && MainService.this.getIsBTState() && MainService.this.iIsInAndroid) {
                        MainService.this.app.interOriginalView();
                    }
                    MainService.this.iAUX = false;
                    if (MainService.this.app.musicType != 0 && !MainService.this.iVoiceSound && !MainService.this.iNaviSound) {
                        MainService.this.mHandler.postDelayed(this.requestAudioRunnable, 1000);
                    }
                }
                MainService.this.logger.debug(String.valueOf(MainService.this.MYLOG) + " isAUX = " + MainService.this.iAUX);
            }
        }

        public void onCarRunningInfo(CarRunInfo info) {
            Log.d(MainService.this.MYLOG, "<<<\tonCarRunningInfo:\tCarRunInfo--" + info.toString());
            MainService.this.app.carRunInfo = info;
            MainService.this.app.speed = info.getCurSpeed();
            MainService.this.app.rSpeed = info.getRevolutions();
            Bundle bun = new Bundle();
            bun.putParcelable(SysConst.FLAG_RUNNING_STATE, info);
            MainService.this.app.sendMessage(1008, bun);
        }

        public void onShowOrHideCarLayer(Mainboard.ECarLayer eCarLayer) {
            Log.d(MainService.this.MYLOG, "<<<\tonShowOrHideCarLayer:\tECarLayer--" + eCarLayer.toString() + ",onNoTouchLayout = " + MainService.this.noTouchLayout);
            int mediaVolume = SysConst.num[MainService.this.mSpUtilK.getInt(SysConst.mediaVoice, 5)];
            switch ($SWITCH_TABLE$com$backaudio$android$driver$Mainboard$ECarLayer()[eCarLayer.ordinal()]) {
                case 1:
                    MainService.this.iIsInAndroid = true;
                    MainService.this.iIsInOriginal = false;
                    MainService.this.iIsInRecorder = false;
                    MainService.this.iIsBTConnect = false;
                    MainService.this.iIsScrennOff = false;
                    MainService.this.iIsInReversing = false;
                    MainService.this.sendVoiceWakeupState(MainService.this.app.isVocieWakeup);
                    break;
                case 2:
                    MainService.this.iIsInAndroid = false;
                    MainService.this.iIsInOriginal = false;
                    MainService.this.iIsInRecorder = true;
                    MainService.this.iIsBTConnect = false;
                    MainService.this.iIsScrennOff = false;
                    MainService.this.iIsInReversing = false;
                    break;
                case 3:
                    MainService.this.iIsInAndroid = false;
                    MainService.this.iIsInOriginal = false;
                    MainService.this.iIsInRecorder = false;
                    MainService.this.iIsBTConnect = false;
                    MainService.this.iIsScrennOff = false;
                    MainService.this.iIsInReversing = false;
                    break;
                case 4:
                case 7:
                case 9:
                    MainService.this.iIsInAndroid = false;
                    MainService.this.iIsInOriginal = false;
                    MainService.this.iIsInRecorder = false;
                    MainService.this.iIsBTConnect = false;
                    MainService.this.iIsScrennOff = false;
                    MainService.this.iIsInReversing = true;
                    MainService.this.sendVoiceWakeupState(false);
                    if (!(MainService.this.app.eMediaSet == Mainboard.EReverserMediaSet.NOMAL || SysConst.basicNum == 0 || MainService.this.app.eMediaSet != Mainboard.EReverserMediaSet.MUTE)) {
                        SysConst.basicNum = 0;
                        Mainboard.getInstance().setAllHornSoundValue(SysConst.basicNum, SysConst.num[MainService.this.mSpUtilK.getInt("mun", 5)], 0, mediaVolume, mediaVolume);
                        MainService.this.setMusicMute();
                        break;
                    }
                case 5:
                    MainService.this.iIsInAndroid = false;
                    MainService.this.iIsInOriginal = true;
                    MainService.this.iIsInRecorder = false;
                    MainService.this.iIsBTConnect = false;
                    MainService.this.iIsScrennOff = false;
                    MainService.this.iIsInReversing = false;
                    if (MainService.this.app.playpos == 1 && MainService.this.app.isOpenAMP) {
                        Mainboard.getInstance().openOrCloseMediaRelay(false);
                        MainService.this.app.isOpenAMP = false;
                        MainService.this.app.requestMainGainAudioFocus();
                        MainService.this.setMusicMute();
                        MainService.this.iIsDV = false;
                        Mainboard.getInstance().setDVAudio(false);
                        break;
                    }
                case 6:
                    MainService.this.iIsInAndroid = false;
                    MainService.this.iIsInOriginal = false;
                    MainService.this.iIsInRecorder = false;
                    MainService.this.iIsBTConnect = true;
                    MainService.this.iIsInReversing = false;
                    MainService.this.iIsScrennOff = false;
                    break;
                case 8:
                    MainService.this.iIsInAndroid = false;
                    MainService.this.iIsInOriginal = false;
                    MainService.this.iIsInRecorder = false;
                    MainService.this.iIsBTConnect = false;
                    MainService.this.iIsScrennOff = true;
                    MainService.this.iIsInReversing = false;
                    break;
            }
            if (!MainService.this.iIsInReversing && SysConst.basicNum != 23) {
                SysConst.basicNum = 23;
                if ((MainService.this.iVoiceSound || MainService.this.iNaviSound) && !MainService.this.app.ismix) {
                    Mainboard.getInstance().setAllHornSoundValue(SysConst.basicNum, SysConst.num[MainService.this.mSpUtilK.getInt(SysConst.naviVoice, 3)], 0, mediaVolume, mediaVolume);
                } else {
                    Mainboard.getInstance().setAllHornSoundValue(SysConst.mediaBasicNum, SysConst.num[MainService.this.mSpUtilK.getInt(SysConst.naviVoice, 3)], 0, mediaVolume, mediaVolume);
                }
                if (MainService.this.getBTA2dpState() || MainService.this.app.playpos == 1) {
                    MainService.this.setConnectedMusicVoice();
                }
            }
            if (MainService.this.iIsInAndroid) {
                MainService.this.removeNoTouchScreens();
            } else {
                MainService.this.mHandler.postDelayed(new Runnable() {
                    public void run() {
                        MainService.this.createNoTouchScreens();
                    }
                }, 200);
            }
        }

        public void onHornSoundValue(int fLeft, int fRight, int rLeft, int rRight) {
        }

        public void logcatCanbox(String str) {
            Intent intent = new Intent();
            intent.setAction("com.unibroad.logcatcanbox");
            intent.putExtra("canbox", str);
            MainService.this.sendBroadcast(intent);
        }

        public void obtainStoreData(List<Byte> data) {
            boolean z;
            Log.d(MainService.this.MYLOG, "<<<\tobtainStoreData:\tdata--" + data);
            if (data.get(0).byteValue() == 1) {
                MainService.this.app.isOriginalKeyOpen = true;
            } else {
                MainService.this.app.isOriginalKeyOpen = false;
            }
            MainService.this.mSpUtilK.putBoolean(MainService.this.getString(R.string.string_original_setting), MainService.this.app.isOriginalKeyOpen);
            SysConst.storeData[0] = data.get(0).byteValue();
            if (data.get(1).byteValue() == 1) {
                MainService.this.app.isSpeechKeyOpen = true;
            } else {
                MainService.this.app.isSpeechKeyOpen = false;
            }
            MainService.this.mSpUtilK.putBoolean(MainService.this.getString(R.string.string_voice_setting), MainService.this.app.isSpeechKeyOpen);
            SysConst.storeData[1] = data.get(1).byteValue();
            if (data.get(2).byteValue() == 1) {
                MainService.this.app.isVocieWakeup = true;
            } else {
                MainService.this.app.isVocieWakeup = false;
            }
            MainService.this.mSpUtilK.putBoolean(MainService.this.getString(R.string.string_vwakeup), MainService.this.app.isVocieWakeup);
            SysConst.storeData[2] = data.get(2).byteValue();
            if (data.get(3).byteValue() == 0) {
                MainService.this.app.ismix = true;
            } else {
                MainService.this.app.ismix = false;
            }
            MainService.this.mSpUtilK.putBoolean(MainService.this.getString(R.string.string_navi_mix), MainService.this.app.ismix);
            SysConst.storeData[3] = data.get(3).byteValue();
            SysConst.storeData[4] = data.get(4).byteValue();
            MainService.this.mSpUtilK.putInt(SysConst.FLAG_CONFIG_RADIO, data.get(4).byteValue());
            MainService.this.app.radioPos = data.get(4).byteValue();
            SysConst.storeData[5] = data.get(5).byteValue();
            MainService.this.mSpUtilK.putInt(SysConst.FLAG_CONFIG_NAVI, data.get(5).byteValue());
            MainService.this.app.naviPos = data.get(5).byteValue();
            SysConst.storeData[6] = data.get(6).byteValue();
            MainService.this.mSpUtilK.putInt(SysConst.FLAG_CONFIG_CONNECT, data.get(6).byteValue());
            MainService.this.app.connectPos = data.get(6).byteValue();
            SysConst.storeData[10] = data.get(10).byteValue();
            MainService.this.mSpUtilK.putInt(SysConst.FLAG_CONFIG_CONNECT1, data.get(10).byteValue());
            MainService.this.app.connectPos1 = data.get(10).byteValue();
            SysConst.storeData[7] = data.get(7).byteValue();
            MainService.this.mSpUtilK.putInt(SysConst.FLAG_CONFIG_USB_NUM, data.get(7).byteValue());
            MainService.this.app.usbPos = data.get(7).byteValue();
            SysConst.storeData[9] = data.get(9).byteValue();
            MainService.this.mSpUtilK.putInt(PubSysConst.KEY_BREAKPOS, data.get(9).byteValue());
            MainService.this.app.breakpos = data.get(9).byteValue();
            UtilTools.echoFile(new StringBuilder().append(MainService.this.app.breakpos).toString(), PubSysConst.GT9XX_INT_TRIGGER);
            if (data.get(11).byteValue() == 0) {
                MainService.this.app.isGestureSwitch = true;
            } else {
                MainService.this.app.isGestureSwitch = false;
            }
            SysConst.storeData[11] = data.get(11).byteValue();
            MainService.this.mSpUtilK.putBoolean(MainService.this.getString(R.string.string_gesture_switch), MainService.this.app.isGestureSwitch);
            MainService.this.app.playpos = data.get(12).byteValue();
            SysConst.storeData[12] = data.get(12).byteValue();
            MainService.this.mSpUtilK.putInt(SysConst.FLAG_CONFIG_PLAY, data.get(12).byteValue());
            if (MainService.this.app.radioPos == 0) {
                if (MainService.this.app.connectPos == 0) {
                    LauncherApplication.isBT = true;
                } else {
                    LauncherApplication.isBT = false;
                }
            } else if (MainService.this.app.connectPos1 == 0) {
                LauncherApplication.isBT = true;
            } else {
                LauncherApplication.isBT = false;
            }
            if (MainService.this.app.playpos == 1) {
                LauncherApplication.isBT = false;
                MainService.this.iAUX = true;
            }
            if (!LauncherApplication.isBT) {
                SysConst.storeData[8] = data.get(8).byteValue();
                LauncherApplication access$5 = MainService.this.app;
                if (data.get(8).byteValue() == 1) {
                    z = true;
                } else {
                    z = false;
                }
                access$5.isAirhide = z;
                MainService.this.mSpUtilK.putBoolean(MainService.this.getString(R.string.string_air_hide), MainService.this.app.isAirhide);
                return;
            }
            MainService.this.app.ismix = false;
            MainService.this.mSpUtilK.putBoolean(MainService.this.getString(R.string.string_navi_mix), false);
            SysConst.musicDecVolume = 0.3f;
        }

        public void obtainReverseType(Mainboard.EReverseTpye eReverseTpye) {
            int type = 1;
            switch ($SWITCH_TABLE$com$backaudio$android$driver$Mainboard$EReverseTpye()[eReverseTpye.ordinal()]) {
                case 1:
                    type = 1;
                    break;
                case 2:
                    type = 0;
                    break;
                case 3:
                    type = 2;
                    break;
            }
            MainService.this.mSpUtilK.putInt(SysConst.FLAG_REVERSING_TYPE, type);
            Log.d(MainService.this.MYLOG, "<<<\tobtainReverseType:\tEReverseTpye--" + eReverseTpye + "type = " + type);
        }

        public void obtainBenzType(BenzModel.EBenzTpye eBenzTpye) {
            Log.e("", "obtainBenzType before benzTpye = " + eBenzTpye);
            BenzModel.benzTpye = eBenzTpye;
            MainService.this.mSpUtilK.putInt(BenzModel.KEY, eBenzTpye.getCode());
            Log.d(MainService.this.MYLOG, "<<<\tobtainBenzType:\tEBenzTpye--" + eBenzTpye);
            Bundle bun = new Bundle();
            bun.putParcelable(SysConst.FLAG_RUNNING_STATE, MainService.this.app.carRunInfo);
            MainService.this.app.sendMessage(1008, bun);
        }

        public void obtainReverseMediaSet(Mainboard.EReverserMediaSet eMediaSet) {
            switch ($SWITCH_TABLE$com$backaudio$android$driver$Mainboard$EReverserMediaSet()[eMediaSet.ordinal()]) {
                case 1:
                    MainService.this.mSpUtilK.putInt(SysConst.FLAG_REVERSING_VOICE, 0);
                    break;
                case 3:
                    MainService.this.mSpUtilK.putInt(SysConst.FLAG_REVERSING_VOICE, 1);
                    break;
            }
            MainService.this.app.eMediaSet = eMediaSet;
            Log.d(MainService.this.MYLOG, "<<<\tobtainReverseMediaSet:\tEReverserMediaSet--" + eMediaSet);
        }

        public void obtainBenzSize(int benzType) {
            MainService.this.app.screenPos = benzType;
            MainService.this.mSpUtilK.putInt(SysConst.FLAG_CONFIG_SCREEN, benzType);
            Log.d(MainService.this.MYLOG, "<<<\tobtainBenzSize:\ttype--" + benzType);
        }

        public void onAUXActivateStutas(Mainboard.EAUXStutas eStutas) {
            Bundle bun = new Bundle();
            bun.putSerializable(SysConst.FLAG_AUX_ACTIVATE_STUTAS, eStutas);
            MainService.this.app.sendMessage(SysConst.AUX_ACTIVATE_STUTAS, bun);
            Log.d(MainService.this.MYLOG, "<<<\tonAUXActivateStutas: eStutas--" + eStutas);
        }

        public void obtainBrightness(int value) {
            Log.d(MainService.this.MYLOG, "<<<\tobtainBrightness: value--" + value);
            MainService.this.mSpUtilK.putInt(SysConst.brightness, value);
        }

        public void obtainLanguageMediaSet(Mainboard.ELanguage eLanguage) {
            Log.d(MainService.this.MYLOG, "<<<\tobtainLanguageMediaSet: eLanguage--" + eLanguage);
        }

        public void obtainCoordinate(int x, int y) {
            Log.d(MainService.this.MYLOG, "<<<\tobtainCoordinate: x=" + x + ",y = " + y);
            LauncherApplication.X = x;
            LauncherApplication.Y = y;
        }

        public void obtainDVState(boolean isPlaying) {
            if (!isPlaying) {
                MainService.this.iIsDV = false;
                Mainboard.getInstance().setDVAudio(MainService.this.iIsDV);
                MainService.this.app.abandonDVAudioFocus();
                return;
            }
            MainService.this.iIsDV = true;
        }
    }

    /* access modifiers changed from: private */
    public void decMap(boolean isdec) {
        int i = 1;
        Intent intent = new Intent();
        intent.setAction("AUTONAVI_STANDARD_BROADCAST_RECV");
        intent.putExtra("KEY_TYPE", 10027);
        intent.putExtra("EXTRA_TYPE", 1);
        if (!isdec) {
            i = 0;
        }
        intent.putExtra("EXTRA_OPERA", i);
        sendBroadcast(intent);
    }

    public IBinder onBind(Intent intent) {
        return this.mBinder;
    }

    public class MainBinder extends Binder {
        public MainBinder() {
        }

        public MainService getService() {
            return MainService.this;
        }
    }
}
