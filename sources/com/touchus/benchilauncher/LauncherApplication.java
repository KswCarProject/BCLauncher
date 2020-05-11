package com.touchus.benchilauncher;

import android.app.ActivityManagerNative;
import android.app.Application;
import android.app.Dialog;
import android.app.IActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.UserHandle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import cn.kuwo.autosdk.api.KWAPI;
import com.backaudio.android.driver.Mainboard;
import com.backaudio.android.driver.beans.AirInfo;
import com.backaudio.android.driver.beans.CarBaseInfo;
import com.backaudio.android.driver.beans.CarRunInfo;
import com.backaudio.android.driver.bluetooth.Bluetooth;
import com.backaudio.android.driver.bluetooth.bc8mpprotocol.EPhoneStatus;
import com.touchus.benchilauncher.activity.main.left.RunningFrag;
import com.touchus.benchilauncher.fragment.YiBiaoFragment;
import com.touchus.benchilauncher.service.BluetoothService;
import com.touchus.benchilauncher.service.MusicPlayControl;
import com.touchus.benchilauncher.service.SocketService;
import com.touchus.benchilauncher.service.TimaService;
import com.touchus.benchilauncher.utils.EcarManager;
import com.touchus.benchilauncher.utils.SpUtilK;
import com.touchus.benchilauncher.utils.WifiTool;
import com.touchus.benchilauncher.views.FloatSystemCallDialog;
import com.touchus.publicutils.bean.MediaBean;
import com.touchus.publicutils.sysconst.BenzModel;
import com.touchus.publicutils.sysconst.PubSysConst;
import com.touchus.publicutils.sysconst.VoiceAssistantConst;
import com.touchus.publicutils.utils.CrashHandler;
import de.mindpipe.android.logging.log4j.LogConfigurator;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.mail.internet.HeaderTokenizer;
import org.apache.log4j.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LauncherApplication extends Application {
    private static String FLAG_CITY_CODE = "cityCode";
    private static String FLAG_DEVICE_NAME = "lastDevice";
    private static String FLAG_EFFECT_DATA = "effect_data_";
    private static String FLAG_LAST_ENTER_PAIR_STATE = "needEnterPair";
    private static String FLAG_NAVI_APP = "naviapp";
    private static String FLAG_NEED_AUTO_START_MEDIA = "needAutoStartMedia";
    private static String FLAG_NEED_AUTO_START_NAVI = "needAutoStartNavi";
    private static String FLAG_WIFI_AP_ID_KEY = "wifiApIdAndKey";
    private static String FLAG_WIFI_AP_STATE = "wifiApState";
    private static String FLAG_WIFI_HAS_PWD = "hadpwd";
    public static int X = 0;
    public static int X1 = 0;
    public static int Y = 0;
    public static int Y1 = 0;
    public static boolean iAccOff = false;
    public static boolean iFirst = false;
    public static boolean iPlaying = false;
    public static boolean iPlayingAuto = false;
    public static int imageIndex;
    public static List<MediaBean> imageList = new ArrayList();
    public static boolean isBT = true;
    public static boolean isBlueConnectState = false;
    public static boolean isGPSLocation = false;
    public static boolean isVerify = false;
    public static Context mContext;
    public static LauncherApplication mInstance;
    public static SpUtilK mSpUtils;
    public static int menuSelectType = -1;
    public static int musicIndex;
    public static List<MediaBean> musicList = new ArrayList();
    public static int pageCount = 4;
    public static boolean shutDoorNeedShowYibiao = false;
    public static List<File> usbpaths = new ArrayList();
    public static int videoIndex;
    public static List<MediaBean> videoList = new ArrayList();
    public final int BluetoothMusicType = 200;
    private final ServiceConnection Socketconn = new ServiceConnection() {
        public void onServiceConnected(ComponentName name, IBinder binder) {
            Log.e("", "Socket onServiceConnected " + binder.toString());
            LauncherApplication.this.socketserviceStart = true;
        }

        public void onServiceDisconnected(ComponentName name) {
            LauncherApplication.this.socketserviceStart = false;
        }
    };
    private final ServiceConnection Timaconn = new ServiceConnection() {
        public void onServiceConnected(ComponentName name, IBinder binder) {
            Log.e("", "Tima onServiceConnected " + binder.toString());
            LauncherApplication.this.timaserviceStart = true;
        }

        public void onServiceDisconnected(ComponentName name) {
            LauncherApplication.this.timaserviceStart = false;
        }
    };
    public String address;
    public AirInfo airInfo;
    public int breakpos = 0;
    public boolean btConnectDialog = false;
    public BluetoothService btservice = null;
    public CarBaseInfo carBaseInfo;
    public CarRunInfo carRunInfo;
    public int connectPos = 0;
    public int connectPos1 = 0;
    private int countMusic = 0;
    private int countMusicBluetooth = 0;
    private int countRing = 0;
    public String curCanboxVersion = "";
    public String curMcuVersion = "";
    public String currentCityName = "";
    public Dialog currentDialog1 = null;
    public Dialog currentDialog2 = null;
    public Dialog currentDialog3 = null;
    public Dialog currentDialog4 = null;
    public int day;
    public Mainboard.EReverserMediaSet eMediaSet = Mainboard.EReverserMediaSet.MUTE;
    public String gpsCityName;
    public int hour;
    public boolean iCurrentInLauncher = true;
    public AtomicBoolean iIsScreenClose = new AtomicBoolean(false);
    public boolean iIsVideoShow = false;
    public boolean iIsYibiaoShowing = false;
    public int iLanguageType = 0;
    public boolean iMedeaDeviceClose = false;
    public boolean isAirhide = false;
    public boolean isAutoTime = false;
    private boolean isBluetoothMusicMute = false;
    public boolean isCallDialog = false;
    public boolean isCalling = false;
    public boolean isComeFromRecord = false;
    public boolean isEasyBtConnect = false;
    public boolean isEasyView = false;
    public boolean isGestureSwitch = false;
    public boolean isOpenAMP = false;
    public boolean isOriginalKeyOpen = false;
    public boolean isOriginalTime = false;
    public boolean isPhoneNumFromRecord = false;
    public boolean isSUV = false;
    public boolean isScanner = false;
    public boolean isSpeechKeyOpen = true;
    private boolean isStreamMusicMute = false;
    private boolean isStreamRingMute = false;
    public boolean isTestOpen = false;
    public boolean isVocieWakeup = false;
    public boolean ismix;
    public boolean istop = false;
    public KWAPI kwapi;
    public int lastmusicType;
    public Handler launcherHandler;
    Logger logger = LoggerFactory.getLogger(LauncherApplication.class);
    private AudioManager.OnAudioFocusChangeListener mDVAudioFocusListener = new AudioManager.OnAudioFocusChangeListener() {
        public void onAudioFocusChange(int focusChange) {
            switch (focusChange) {
                case HeaderTokenizer.Token.COMMENT /*-3*/:
                    LauncherApplication.this.logger.debug("requestAudioFocus DV CAN_DUCK");
                    return;
                case -2:
                    LauncherApplication.this.logger.debug("requestAudioFocus DV AUDIOFOCUS_LOSS_TRANSIENT");
                    return;
                case -1:
                    LauncherApplication.this.logger.debug("requestAudioFocus DV LOSS");
                    return;
                case 1:
                    LauncherApplication.this.logger.debug("requestAudioFocus DV GAIN");
                    return;
                default:
                    return;
            }
        }
    };
    public Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            Log.e("", "FloatSystemCallDialog handleMessage  msg.what:" + msg.what);
            switch (msg.what) {
                case BluetoothService.CALL_TALKING /*1411*/:
                    LauncherApplication.this.isCalling = true;
                    FloatSystemCallDialog.getInstance().updateTalkStatus();
                    return;
                case BluetoothService.UPDATE_PHONENUM /*1415*/:
                    LauncherApplication.this.interAndroidView();
                    LauncherApplication.this.isCalling = true;
                    if (BluetoothService.currentCallingType == 1412) {
                        FloatSystemCallDialog floatCallingDialog = FloatSystemCallDialog.getInstance();
                        if (floatCallingDialog.getShowStatus() == FloatSystemCallDialog.FloatShowST.INCOMING && LauncherApplication.this.phoneNumber != null && !LauncherApplication.this.phoneNumber.equals(floatCallingDialog.getCallingPhonenumber())) {
                            floatCallingDialog.setCallingPhonenumber(LauncherApplication.this.phoneNumber);
                            floatCallingDialog.clearView();
                            LauncherApplication.this.btservice.enterSystemFloatCallView();
                            return;
                        }
                        return;
                    }
                    return;
                case BluetoothService.HANGUP_PHONE /*1416*/:
                    LauncherApplication.this.isPhoneNumFromRecord = false;
                    LauncherApplication.this.isCalling = false;
                    if (LauncherApplication.this.service != null) {
                        LauncherApplication.this.service.openOrCloseRelay(false);
                    }
                    if (EcarManager.isEcarCall) {
                        EcarManager.isEcarCall = false;
                        EcarManager.getInstance(LauncherApplication.mContext).SendCallState(LauncherApplication.mContext, 3);
                        return;
                    }
                    return;
                case BluetoothService.TIME_FLAG /*1417*/:
                    if (BluetoothService.talkingflag) {
                        LauncherApplication.this.isCalling = true;
                    } else {
                        LauncherApplication.this.isCalling = false;
                    }
                    if (FloatSystemCallDialog.getInstance().getShowStatus() == FloatSystemCallDialog.FloatShowST.TALKING) {
                        FloatSystemCallDialog.getInstance().freshCallingTime();
                    }
                    if (EcarManager.isEcarCall) {
                        EcarManager.getInstance(LauncherApplication.mContext).SendCallState(LauncherApplication.mContext, 5);
                        return;
                    }
                    return;
                case BluetoothService.OUTCALL_FLOAT /*1422*/:
                    if (BluetoothService.bluetoothStatus != EPhoneStatus.INCOMING_CALL && BluetoothService.bluetoothStatus != EPhoneStatus.TALKING && BluetoothService.bluetoothStatus != EPhoneStatus.CALLING_OUT) {
                        return;
                    }
                    if (LauncherApplication.this.btservice == null || LauncherApplication.this.service.iIsInReversing) {
                        FloatSystemCallDialog.getInstance().clearView();
                        return;
                    } else {
                        LauncherApplication.this.btservice.enterSystemFloatCallView();
                        return;
                    }
                case SysConst.BLUETOOTH_PHONE_STATE:
                    if (LauncherApplication.this.isEasyView) {
                        if (LauncherApplication.isBlueConnectState && !LauncherApplication.this.isEasyBtConnect) {
                            LauncherApplication.this.isEasyBtConnect = true;
                            LauncherApplication.this.service.sendEasyConnectBroadcast(SysConst.EVENT_EASYCONN_BT_CONNECTED);
                            LauncherApplication.this.btservice.requestMusicAudioFocus();
                        } else if (!LauncherApplication.isBlueConnectState && LauncherApplication.this.isEasyBtConnect) {
                            LauncherApplication.this.isEasyBtConnect = false;
                            LauncherApplication.this.openOrCloseBluetooth(true);
                            LauncherApplication.this.service.sendEasyConnectBroadcast(SysConst.EVENT_EASYCONN_BT_OPENED);
                            LauncherApplication.this.service.sendEasyConnectBroadcast(SysConst.EVENT_EASYCONN_BT_UNCONNECTED);
                            LauncherApplication.this.btservice.stopBTMusic();
                        }
                    }
                    LauncherApplication.this.isCalling = false;
                    if (!FloatSystemCallDialog.getInstance().isDestory()) {
                        FloatSystemCallDialog.getInstance().dissShow();
                        FloatSystemCallDialog.getInstance().clearView();
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    };
    private ArrayList<Handler> mHandlers = new ArrayList<>();
    public boolean mHomeAndBackEnable = true;
    private AudioManager.OnAudioFocusChangeListener mMainAudioFocusListener = new AudioManager.OnAudioFocusChangeListener() {
        public void onAudioFocusChange(int focusChange) {
            switch (focusChange) {
                case HeaderTokenizer.Token.COMMENT /*-3*/:
                    LauncherApplication.this.logger.debug("requestAudioFocus main CAN_DUCK");
                    return;
                case -2:
                    LauncherApplication.this.logger.debug("requestAudioFocus main TRANSIENT");
                    return;
                case -1:
                    LauncherApplication.this.logger.debug("requestAudioFocus main LOSS");
                    return;
                case 1:
                    LauncherApplication.this.logger.debug("requestAudioFocus main GAIN");
                    return;
                default:
                    return;
            }
        }
    };
    private AudioManager.OnAudioFocusChangeListener mMainAudioGainFocusListener = new AudioManager.OnAudioFocusChangeListener() {
        public void onAudioFocusChange(int focusChange) {
            switch (focusChange) {
                case HeaderTokenizer.Token.COMMENT /*-3*/:
                case -2:
                    LauncherApplication.this.logger.debug("requestAudioFocus Gainmain CAN_DUCK");
                    return;
                case -1:
                    LauncherApplication.this.logger.debug("requestAudioFocus Gainmain LOSS");
                    return;
                case 1:
                    LauncherApplication.this.logger.debug("requestAudioFocus Gainmain GAIN");
                    return;
                default:
                    return;
            }
        }
    };
    public Handler mOriginalViewHandler;
    private AudioManager.OnAudioFocusChangeListener mPAPAGOAudioFocusListener = new AudioManager.OnAudioFocusChangeListener() {
        public void onAudioFocusChange(int focusChange) {
            switch (focusChange) {
                case HeaderTokenizer.Token.COMMENT /*-3*/:
                    LauncherApplication.this.logger.debug("requestAudioFocus PAPAGO CAN_DUCK");
                    return;
                case -2:
                    LauncherApplication.this.logger.debug("requestAudioFocus PAPAGO AUDIOFOCUS_LOSS_TRANSIENT");
                    return;
                case -1:
                    LauncherApplication.this.logger.debug("requestAudioFocus PAPAGO LOSS");
                    return;
                case 1:
                    LauncherApplication.this.logger.debug("requestAudioFocus PAPAGO GAIN");
                    return;
                default:
                    return;
            }
        }
    };
    public int min;
    public int month;
    public MusicPlayControl musicPlayControl = null;
    public int musicType = -1;
    public int naviPos = 0;
    public String phoneName = "";
    public String phoneNumber = "";
    public int playpos = 0;
    public int rSpeed;
    public int radioPos = 0;
    public String recorPhoneNumber = "";
    public int screenPos = 0;
    public int second;
    public MainService service;
    public Handler serviceHandler;
    private SharedPreferences share;
    public boolean socketserviceStart;
    public int speed;
    public boolean timaserviceStart;
    public int timeFormat = 24;
    public int usbPos = 0;
    public String weatherTemp = "";
    public String weatherTempDes = "";
    public int year;

    public static Context getContext() {
        return mContext;
    }

    public boolean topIsBlueMainFragment() {
        return this.istop;
    }

    public void onCreate() {
        CrashHandler.getInstance(getApplicationContext());
        initLogger();
        mContext = getApplicationContext();
        if (mInstance == null) {
            mInstance = this;
        }
        registerHandler(this.mHandler);
        mSpUtils = new SpUtilK(getContext());
        this.share = PreferenceManager.getDefaultSharedPreferences(this);
        initModel();
        this.kwapi = KWAPI.createKWAPI(this, "auto");
        if (getWifiApIdAndKey()[0].equals(getText(R.string.wifi_default_name)) && getWifiApIdAndKey()[1].equals("11111111")) {
            WifiTool.setDefaultWifiAp(this);
        }
        initData();
        startSocketService();
        super.onCreate();
    }

    public void startTimaService() {
        Log.e("", "Tima startTimaService");
        if (!this.timaserviceStart) {
            bindService(new Intent(this, TimaService.class), this.Timaconn, 1);
        }
    }

    public void startSocketService() {
        Log.e("", "Socket startSocketService");
        if (!this.socketserviceStart) {
            bindService(new Intent(this, SocketService.class), this.Socketconn, 1);
        }
    }

    private void initData() {
        musicIndex = mSpUtils.getInt(SysConst.FLAG_MUSIC_POS, 0);
        videoIndex = mSpUtils.getInt(SysConst.FLAG_VIDEO_POS, 0);
        this.breakpos = mSpUtils.getInt(PubSysConst.KEY_BREAKPOS, 0);
        this.isAirhide = mSpUtils.getBoolean(getString(R.string.string_air_hide), false);
        Log.e("LauncherApplication", "musicIndex = " + musicIndex + ",videoIndex = " + videoIndex);
        usbpaths.add(new File("/mnt/usbotg"));
        usbpaths.add(new File("/mnt/usbotg1"));
        usbpaths.add(new File("/mnt/usbotg2"));
    }

    public void initModel() {
        int i = 0;
        int code = mSpUtils.getInt(BenzModel.KEY, 0);
        BenzModel.EBenzTpye[] values = BenzModel.EBenzTpye.values();
        int length = values.length;
        int i2 = 0;
        while (true) {
            if (i2 >= length) {
                break;
            }
            BenzModel.EBenzTpye eBenzTpye = values[i2];
            if (code == eBenzTpye.getCode()) {
                BenzModel.benzTpye = eBenzTpye;
                break;
            }
            i2++;
        }
        int size = mSpUtils.getInt(BenzModel.SIZE_KEY, 0);
        BenzModel.EBenzSize[] values2 = BenzModel.EBenzSize.values();
        int length2 = values2.length;
        while (true) {
            if (i >= length2) {
                break;
            }
            BenzModel.EBenzSize eBenzTpye2 = values2[i];
            if (size == eBenzTpye2.getCode()) {
                BenzModel.benzSize = eBenzTpye2;
                break;
            }
            i++;
        }
        this.isSUV = BenzModel.isSUV();
        if ("benz".equalsIgnoreCase(Build.MODEL) || "ajbenz".equalsIgnoreCase(Build.MODEL)) {
            BenzModel.benzCan = BenzModel.EBenzCAN.XBS;
        } else if (Build.MODEL.contains("c200_hy") || Build.MODEL.contains("c200_psr") || Build.MODEL.contains("c200_zlh") || Build.MODEL.contains("c200_ks") || Build.MODEL.contains("benz_hy")) {
            BenzModel.benzCan = BenzModel.EBenzCAN.WCL;
        }
    }

    public void onTerminate() {
        unregisterHandler(this.mHandler);
        super.onTerminate();
    }

    private void initLogger() {
        try {
            LogConfigurator configurator = new LogConfigurator();
            configurator.setFileName(getApplicationContext().getFilesDir() + File.separator + "log.txt");
            configurator.setRootLevel(Level.DEBUG);
            configurator.setLevel("org.apache", Level.ERROR);
            configurator.setFilePattern("%d - %F:%L - %m  [%t]%n");
            configurator.setMaxFileSize(2097152);
            configurator.setMaxBackupSize(2);
            configurator.setImmediateFlush(true);
            configurator.configure();
            LoggerFactory.getLogger(LauncherApplication.class).info("init log4j");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static LauncherApplication getInstance() {
        return mInstance;
    }

    public void registerHandler(Handler mHandler2) {
        if (!this.mHandlers.contains(mHandler2)) {
            this.mHandlers.add(mHandler2);
        }
        Log.d("launcherlog", "registerHandler =" + mHandler2.getClass());
    }

    public void unregisterHandler(Handler mHandler2) {
        if (this.mHandlers.contains(mHandler2)) {
            this.mHandlers.remove(mHandler2);
            Log.d("launcherlog", "unregisterHandler =" + mHandler2.getClass());
        }
    }

    public void sendMessage(int what, Bundle bundle) {
        if (!iAccOff || what == 1007) {
            Log.d("templog", "mHandlers.size=" + this.mHandlers.size());
            if (what == 6001 && this.mHandlers.size() > 1) {
                byte code = bundle.getByte(SysConst.IDRIVER_ENUM);
                if (this.iIsScreenClose.get() && code != Mainboard.EIdriverEnum.POWER_OFF.getCode()) {
                    wakeupScreen();
                    return;
                } else if (!(code == Mainboard.EIdriverEnum.BACK.getCode() || code == Mainboard.EIdriverEnum.HOME.getCode() || code == Mainboard.EIdriverEnum.STAR_BTN.getCode() || code == Mainboard.EIdriverEnum.BT.getCode() || code == Mainboard.EIdriverEnum.NAVI.getCode() || code == Mainboard.EIdriverEnum.RADIO.getCode() || code == Mainboard.EIdriverEnum.CARSET.getCode() || code == Mainboard.EIdriverEnum.MEDIA.getCode() || code == Mainboard.EIdriverEnum.HANG_UP.getCode() || code == Mainboard.EIdriverEnum.PICK_UP.getCode())) {
                    if (!this.iIsYibiaoShowing) {
                        Handler temp = null;
                        for (int i = this.mHandlers.size() - 1; i >= 0; i--) {
                            temp = this.mHandlers.get(i);
                            if ((!this.isCallDialog || (temp instanceof FloatSystemCallDialog.BluetoothHandler)) && !(temp instanceof RunningFrag.RunningHandler) && !(temp instanceof YiBiaoFragment.MeterHandler)) {
                                break;
                            }
                        }
                        Log.d("templog", "mHandlers.size=" + temp.toString());
                        if (temp != null) {
                            Message msg = temp.obtainMessage(what);
                            msg.setData(bundle);
                            msg.sendToTarget();
                            return;
                        }
                    } else {
                        return;
                    }
                }
            }
            Iterator<Handler> it = this.mHandlers.iterator();
            while (it.hasNext()) {
                Message msg2 = it.next().obtainMessage(what);
                msg2.setData(bundle);
                msg2.sendToTarget();
            }
        }
    }

    public void getLanguage() {
        String language = Locale.getDefault().toString();
        if (language.contains("zh_CN")) {
            mSpUtils.putInt(SysConst.FLAG_LANGUAGE_TYPE, 0);
            Mainboard.getInstance().sendLanguageSetToMcu(0);
        } else if (language.contains("zh_TW")) {
            mSpUtils.putInt(SysConst.FLAG_LANGUAGE_TYPE, 1);
            Mainboard.getInstance().sendLanguageSetToMcu(1);
        } else if (language.contains("en")) {
            mSpUtils.putInt(SysConst.FLAG_LANGUAGE_TYPE, 2);
            Mainboard.getInstance().sendLanguageSetToMcu(2);
        } else if (SysConst.TURKISH_VERSION) {
            mSpUtils.putInt(SysConst.FLAG_LANGUAGE_TYPE, 1);
            Mainboard.getInstance().sendLanguageSetToMcu(1);
        } else {
            Mainboard.getInstance().sendLanguageSetToMcu(2);
        }
    }

    public synchronized void closeOrWakeupScreen(boolean iClose) {
        if (iClose) {
            closeScreen();
        } else {
            wakeupScreen();
        }
    }

    private void closeScreen() {
        if (this.iIsScreenClose.compareAndSet(false, true)) {
            Mainboard.getInstance().closeOrOpenScreen(true);
        }
    }

    private void wakeupScreen() {
        if (this.iIsScreenClose.compareAndSet(true, false)) {
            Mainboard.getInstance().closeOrOpenScreen(false);
        }
    }

    public void startNavi() {
        Intent intent;
        String pkg = getNaviAPP();
        if (!TextUtils.isEmpty(pkg) && (intent = getPackageManager().getLaunchIntentForPackage(pkg)) != null) {
            intent.addFlags(268435456);
            startActivity(intent);
        }
    }

    public boolean startAppByPkg(String pkg) {
        Intent intent;
        if (TextUtils.isEmpty(pkg) || (intent = getPackageManager().getLaunchIntentForPackage(pkg)) == null) {
            return false;
        }
        intent.addFlags(272629760);
        startActivity(intent);
        return true;
    }

    public void changeAirplane(boolean iEnterAirplane) {
        Settings.Global.putInt(getContentResolver(), "airplane_mode_on", iEnterAirplane ? 1 : 0);
        Intent intent = new Intent("android.intent.action.AIRPLANE_MODE");
        intent.putExtra("state", iEnterAirplane);
        sendBroadcastAsUser(intent, UserHandle.ALL);
    }

    public synchronized void sendShowNavigationBarEvent(boolean iShow) {
        sendBroadcast(new Intent(iShow ? "SHOW_NAVIGATION_BAR" : "HIDE_NAVIGATION_BAR"));
    }

    public void sendHideOrShowLocateViewEvent(boolean iShow) {
        String action;
        if (iShow) {
            action = "com.touchus.showlocatefloat";
        } else {
            action = "com.touchus.hidelocatefloat";
        }
        sendBroadcast(new Intent(action));
    }

    public void sendStartVoiceAssistantEvent() {
        Intent intent = new Intent();
        intent.setAction(VoiceAssistantConst.EVENT_START_VOICE_ASSISTANT_FROM_OTHER_APP);
        intent.setFlags(32);
        sendBroadcast(intent);
    }

    public void sendSpeakText(String text) {
        Intent intent = new Intent();
        intent.setAction("com.unibroad.setspeaktext");
        intent.putExtra("text", text);
    }

    public void setNeedToEnterPairMode(boolean flag) {
        SharedPreferences.Editor edit = this.share.edit();
        edit.putBoolean(FLAG_LAST_ENTER_PAIR_STATE, flag);
        edit.commit();
    }

    public boolean getNeedToEnterPairMode() {
        return this.share.getBoolean(FLAG_LAST_ENTER_PAIR_STATE, false);
    }

    public void openOrCloseBluetooth(boolean flag) {
        if (this.btservice != null && this.mHandler != null) {
            if (flag) {
                this.btservice.enterPairingMode();
            } else {
                this.btservice.leavePairingMode();
            }
        }
    }

    public void setEffectData(int[] data) {
        String arrays = Arrays.toString(data);
        SharedPreferences.Editor edit = this.share.edit();
        for (int i = 0; i < data.length; i++) {
            edit.putInt(String.valueOf(FLAG_EFFECT_DATA) + i, data[i]);
        }
        edit.commit();
    }

    public int[] getEffectData(int[] data) {
        for (int i = 0; i < 7; i++) {
            data[i] = this.share.getInt(String.valueOf(FLAG_EFFECT_DATA) + i, 10);
        }
        return data;
    }

    public void setNeedStartMedia(boolean isAuto) {
        SharedPreferences.Editor edit = this.share.edit();
        edit.putBoolean(FLAG_NEED_AUTO_START_MEDIA, isAuto);
        edit.commit();
    }

    public boolean getNeedStartMedia() {
        return this.share.getBoolean(FLAG_NEED_AUTO_START_MEDIA, false);
    }

    public void setNeedStartNaviPkg(boolean isAuto) {
        SharedPreferences.Editor edit = this.share.edit();
        edit.putBoolean(FLAG_NEED_AUTO_START_NAVI, isAuto);
        edit.commit();
    }

    public boolean getNeedStartNaviPkgs() {
        return this.share.getBoolean(FLAG_NEED_AUTO_START_NAVI, false);
    }

    public void setNaviAPP(String packageName) {
        SharedPreferences.Editor edit = this.share.edit();
        edit.putString(FLAG_NAVI_APP, packageName);
        edit.commit();
    }

    public String getNaviAPP() {
        if (SysConst.LANGUAGE == 0) {
            return this.share.getString(FLAG_NAVI_APP, getString(R.string.pkg_navi));
        }
        return this.share.getString(FLAG_NAVI_APP, getString(R.string.pkg_google_navi));
    }

    public void setWifiApState(boolean iOpen) {
        SharedPreferences.Editor edit = this.share.edit();
        edit.putBoolean(FLAG_WIFI_AP_STATE, iOpen);
        edit.commit();
    }

    public boolean getWifiApState() {
        return this.share.getBoolean(FLAG_WIFI_AP_STATE, false);
    }

    public String[] getWifiApIdAndKey() {
        String value = this.share.getString(FLAG_WIFI_AP_ID_KEY, "11111111;" + getText(R.string.wifi_default_name));
        int index = value.indexOf(";");
        return new String[]{value.substring(index + 1), value.substring(0, index)};
    }

    public void setWifiApIdAndKey(String[] idAndKey) {
        SharedPreferences.Editor edit = this.share.edit();
        edit.putString(FLAG_WIFI_AP_ID_KEY, String.valueOf(idAndKey[1]) + ";" + idAndKey[0]);
        edit.commit();
    }

    public void setWifiApHasPwd(boolean haspwd) {
        SharedPreferences.Editor edit = this.share.edit();
        edit.putBoolean(FLAG_WIFI_HAS_PWD, haspwd);
        edit.commit();
    }

    public boolean getWifiApHasPwd() {
        return this.share.getBoolean(FLAG_WIFI_HAS_PWD, true);
    }

    public boolean iNeedToChangeLocalContacts(String equipAddress) {
        String deviceName = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString(FLAG_DEVICE_NAME, "");
        if (TextUtils.isEmpty(deviceName) || !deviceName.equals(equipAddress)) {
            return false;
        }
        return true;
    }

    public void setLastDeviceName(String equipAddress) {
        SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
        edit.putString(FLAG_DEVICE_NAME, equipAddress);
        edit.commit();
    }

    public void updateLanguage(Locale locale) {
        Log.d("ANDROID_LAB", locale.toString());
        try {
            IActivityManager objActMagNative = ActivityManagerNative.getDefault();
            Configuration config = objActMagNative.getConfiguration();
            config.locale = locale;
            objActMagNative.updateConfiguration(config);
            getResources().updateConfiguration(config, getResources().getDisplayMetrics());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void interOriginalView() {
        Mainboard.getInstance().showCarLayer(Mainboard.ECarLayer.ORIGINAL);
    }

    public void interBTConnectView() {
        Mainboard.getInstance().showCarLayer(Mainboard.ECarLayer.ORIGINAL);
    }

    public void interAndroidView() {
        Mainboard.getInstance().showCarLayer(Mainboard.ECarLayer.ANDROID);
    }

    public void dismissDialog() {
        if (this.currentDialog3 != null && this.currentDialog3.isShowing()) {
            this.currentDialog3.dismiss();
        }
        if (this.currentDialog2 != null && this.currentDialog2.isShowing()) {
            this.currentDialog2.dismiss();
        }
        if (this.currentDialog1 != null && this.currentDialog1.isShowing()) {
            this.currentDialog1.dismiss();
        }
        if (this.currentDialog4 != null && this.currentDialog4.isShowing()) {
            this.currentDialog4.dismiss();
        }
    }

    public void requestMainGainAudioFocus() {
        if (this.musicType != 0) {
            this.lastmusicType = this.musicType;
            this.musicType = 0;
            Log.e("Gainmain requestGainAudioFocus", "flag" + ((AudioManager) getSystemService("audio")).requestAudioFocus(this.mMainAudioFocusListener, 3, 2));
        }
    }

    public void abandonMainGainAudioFocus() {
        ((AudioManager) getSystemService("audio")).abandonAudioFocus(this.mMainAudioGainFocusListener);
        this.musicType = this.lastmusicType;
        Log.e("Gainmain abandonMainGainAudioFocus", "musicType = " + this.musicType);
    }

    public void requestMainAudioFocus() {
        if (this.musicType != 0) {
            this.musicType = 0;
            Log.e("Main requestAudioFocus", "flag" + ((AudioManager) getSystemService("audio")).requestAudioFocus(this.mMainAudioFocusListener, 3, 1));
        }
    }

    public void abandonMainAudioFocus() {
        ((AudioManager) getSystemService("audio")).abandonAudioFocus(this.mMainAudioFocusListener);
    }

    public void requestDVAudioFocus() {
        this.musicType = 4;
        Log.e("Main requestDVAudioFocus", "flag" + ((AudioManager) getSystemService("audio")).requestAudioFocus(this.mDVAudioFocusListener, 3, 1));
    }

    public void abandonDVAudioFocus() {
        ((AudioManager) getSystemService("audio")).abandonAudioFocus(this.mDVAudioFocusListener);
        if (this.musicType == 4) {
            this.musicType = 0;
        }
    }

    public void requestPAPAGOAudioFocus() {
        if (this.musicType != 6) {
            this.lastmusicType = this.musicType;
            this.musicType = 6;
            Log.e("Main requestPAPAGOAudioFocus", "flag" + ((AudioManager) getSystemService("audio")).requestAudioFocus(this.mPAPAGOAudioFocusListener, 3, 3));
        }
    }

    public void abandonPAPAGOAudioFocus() {
        ((AudioManager) getSystemService("audio")).abandonAudioFocus(this.mPAPAGOAudioFocusListener);
        this.musicType = this.lastmusicType;
    }

    public void setEffect(int[] data, int pos) {
        int[] data1 = {10, 13, 12, 11, 10, 10, 10};
        if (pos == 0) {
            data1[0] = data[0];
            data1[1] = data[1] + 3;
            data1[2] = data[2] + 2;
            data1[3] = data[3] + 1;
            data1[4] = data[4];
            data1[5] = data[5];
            data1[6] = data[6];
        } else {
            data1 = data;
        }
        Mainboard.getInstance().setEffect(data1);
    }

    public synchronized void setTypeMute(int type, boolean state) {
        this.logger.debug("setTypeMute type" + type + "  state==" + state);
        Log.d("setTypeMute", "type" + type + "  state==" + state);
        AudioManager audioManager = (AudioManager) getSystemService("audio");
        switch (type) {
            case 2:
                if (this.isStreamRingMute != state) {
                    this.isStreamRingMute = state;
                    if (state) {
                        audioManager.setStreamMute(31, true);
                    } else {
                        audioManager.setStreamMute(31, false);
                    }
                    this.countRing++;
                    break;
                }
                break;
            case 3:
                if (this.isStreamMusicMute != state) {
                    this.isStreamMusicMute = state;
                    if (state) {
                        audioManager.setStreamMute(30, true);
                    } else {
                        audioManager.setStreamMute(30, false);
                    }
                    this.countMusic++;
                    break;
                }
                break;
            case 200:
                if (this.isBluetoothMusicMute != state) {
                    this.isBluetoothMusicMute = state;
                    try {
                        Bluetooth.getInstance().setBluetoothMusicMute(state);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    this.countMusicBluetooth++;
                    break;
                }
                break;
        }
        Log.d("setTypeMute", "singular is mute : countMusic==" + this.countMusic + " countMusicBluetooth==" + this.countMusicBluetooth + " countRing==" + this.countRing);
    }
}
