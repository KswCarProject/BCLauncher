package com.touchus.benchilauncher.service;

import a_vcard.android.provider.Contacts;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;
import com.backaudio.android.driver.Mainboard;
import com.backaudio.android.driver.bluetooth.Bluetooth;
import com.backaudio.android.driver.bluetooth.EVirtualButton;
import com.backaudio.android.driver.bluetooth.IBluetoothEventHandler;
import com.backaudio.android.driver.bluetooth.bc8mpprotocol.AnswerPhoneResult;
import com.backaudio.android.driver.bluetooth.bc8mpprotocol.CallOutResult;
import com.backaudio.android.driver.bluetooth.bc8mpprotocol.CallingOutProtocol;
import com.backaudio.android.driver.bluetooth.bc8mpprotocol.ConnectedDeviceProtocol;
import com.backaudio.android.driver.bluetooth.bc8mpprotocol.DeviceNameProtocol;
import com.backaudio.android.driver.bluetooth.bc8mpprotocol.DeviceRemovedProtocol;
import com.backaudio.android.driver.bluetooth.bc8mpprotocol.DeviceSwitchedProtocol;
import com.backaudio.android.driver.bluetooth.bc8mpprotocol.EConnectedDevice;
import com.backaudio.android.driver.bluetooth.bc8mpprotocol.EMediaPlayStatus;
import com.backaudio.android.driver.bluetooth.bc8mpprotocol.EPhoneBookCtrlStatus;
import com.backaudio.android.driver.bluetooth.bc8mpprotocol.EPhoneStatus;
import com.backaudio.android.driver.bluetooth.bc8mpprotocol.EnterPairingModeResult;
import com.backaudio.android.driver.bluetooth.bc8mpprotocol.HangUpPhoneResult;
import com.backaudio.android.driver.bluetooth.bc8mpprotocol.IncomingCallProtocol;
import com.backaudio.android.driver.bluetooth.bc8mpprotocol.MediaInfoProtocol;
import com.backaudio.android.driver.bluetooth.bc8mpprotocol.MediaPlayStatusProtocol;
import com.backaudio.android.driver.bluetooth.bc8mpprotocol.MediaStatusProtocol;
import com.backaudio.android.driver.bluetooth.bc8mpprotocol.PairingListProtocol;
import com.backaudio.android.driver.bluetooth.bc8mpprotocol.PairingListUnitProtocol;
import com.backaudio.android.driver.bluetooth.bc8mpprotocol.PhoneBookCtrlStatusProtocol;
import com.backaudio.android.driver.bluetooth.bc8mpprotocol.PhoneBookListProtocol;
import com.backaudio.android.driver.bluetooth.bc8mpprotocol.PhoneStatusProtocol;
import com.backaudio.android.driver.bluetooth.bc8mpprotocol.SetPlayStatusProtocol;
import com.backaudio.android.driver.bluetooth.bc8mpprotocol.VersionProtocol;
import com.touchus.benchilauncher.LauncherApplication;
import com.touchus.benchilauncher.R;
import com.touchus.benchilauncher.SysConst;
import com.touchus.benchilauncher.bean.Equip;
import com.touchus.benchilauncher.utils.EcarManager;
import com.touchus.benchilauncher.utils.SpUtilK;
import com.touchus.benchilauncher.utils.ToastTool;
import com.touchus.benchilauncher.views.FloatSystemCallDialog;
import com.touchus.publicutils.bean.Person;
import com.touchus.publicutils.sysconst.VoiceAssistantConst;
import com.touchus.publicutils.utils.LibVcard;
import com.touchus.publicutils.utils.PersonParse;
import com.touchus.publicutils.utils.PinyinComparator;
import com.touchus.publicutils.utils.UtilTools;
import com.touchus.publicutils.utils.VcardUnit;
import com.unibroad.c2py.Parser;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.mail.internet.HeaderTokenizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;

public class BluetoothService extends Service {
    public static final int BOOK_LIST_CANCEL_LOAD = 1408;
    public static final int BOOK_LIST_START_LOAD = 1407;
    public static final int CALL_TALKING = 1411;
    public static final int CALL_TYPE_IN = 1412;
    public static final int CALL_TYPE_MISS = 1414;
    public static final int CALL_TYPE_OUT = 1413;
    public static final int DEVICE_SWITCH = 1410;
    public static final int DRIVER_BOOK_LIST = 1409;
    public static final int END_PAIRMODE = 1402;
    public static final int HANGUP_PHONE = 1416;
    public static final int MEDIAINFO = 1421;
    public static final int ONGOING_NOTIFICATION = 1400;
    public static final int OUTCALL_FLOAT = 1422;
    public static final int PLAY_STATE_PAUSE = 1419;
    public static final int PLAY_STATE_PLAYING = 1418;
    public static final int PLAY_STATE_STOP = 1420;
    public static final int START_PAIRMODE = 1401;
    public static final int TIME_FLAG = 1417;
    public static final int UPDATE_NAME = 1406;
    public static final int UPDATE_PHONENUM = 1415;
    public static EPhoneStatus bluetoothStatus = EPhoneStatus.UNCONNECT;
    public static int currentCallingType;
    public static String currentEquipAddress = "";
    public static String deviceMAC = "";
    public static String deviceName = "Benz";
    public static String devicePIN = "0000";
    public static ArrayList<Equip> equipList = new ArrayList<>();
    public static boolean iIsCallingInButNotalkingState = false;
    /* access modifiers changed from: private */
    public static Logger logger = LoggerFactory.getLogger(BluetoothService.class);
    public static int mediaPlayState = 1;
    public static boolean talkingflag = false;
    public String Apppath = (String.valueOf(Environment.getExternalStorageDirectory().getAbsolutePath()) + "/unibroad/");
    public boolean accFlag = false;
    /* access modifiers changed from: private */
    public LauncherApplication app;
    private BroadcastReceiver assistantReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            try {
                int cmdId = intent.getIntExtra(VoiceAssistantConst.FLAG_ANALYTIC_CMD, -1);
                String strParam1 = intent.getStringExtra(VoiceAssistantConst.FLAG_ARG1_STRING);
                String strParam2 = intent.getStringExtra(VoiceAssistantConst.FLAG_ARG2_STRING);
                BluetoothService.logger.debug("assistantReceiver  cmdId: " + cmdId + " , strParam2: " + strParam2);
                if (cmdId != -1) {
                    switch (cmdId) {
                        case 1011:
                            BluetoothService.currentCallingType = 1413;
                            if (EPhoneStatus.CONNECTING == BluetoothService.bluetoothStatus) {
                                return;
                            }
                            if (EPhoneStatus.CONNECTED != BluetoothService.bluetoothStatus) {
                                BluetoothService.this.app.launcherHandler.obtainMessage(SysConst.VOICE_WAKEUP_MENU, 3).sendToTarget();
                                return;
                            } else if (TextUtils.isEmpty(strParam2) || strParam2.length() < 2 || strParam2.contains(Marker.ANY_MARKER) || strParam2.contains("#")) {
                                ToastTool.showBigShortToast(BluetoothService.this.mContext, (int) R.string.bluetooth_phonenum_error);
                                return;
                            } else {
                                BluetoothService.this.app.phoneNumber = strParam2;
                                BluetoothService.this.bluetooth.call(BluetoothService.this.app.phoneNumber);
                                return;
                            }
                        case 1012:
                        case 1013:
                        case 1014:
                            BluetoothService.this.app.launcherHandler.obtainMessage(SysConst.VOICE_WAKEUP_MENU, 3).sendToTarget();
                            return;
                        case 1015:
                            if ("1".equals(strParam1)) {
                                BluetoothService.this.answerCalling();
                                return;
                            } else {
                                BluetoothService.this.cutdownCurrentCalling();
                                return;
                            }
                        default:
                            return;
                    }
                }
            } catch (Exception e) {
            }
        }
    };
    public boolean blueMusicFocus = false;
    public Bluetooth bluetooth;
    ArrayList<Person> bookList = new ArrayList<>();
    /* access modifiers changed from: private */
    public int booklistIndex = 0;
    public int calltime = 0;
    public boolean changeAudioResult = false;
    public String currentEquipName = "";
    public int downloadType = 0;
    public int downloadflag = -1;
    public boolean histalkflag = false;
    /* access modifiers changed from: private */
    public boolean iHoldFocus = false;
    public boolean iSoundInPhone = false;
    public boolean ipause = false;
    public boolean isAfterInOrOutCall = false;
    boolean isDownloading = false;
    /* access modifiers changed from: private */
    public long lastCloseCallingViewTime = -1;
    /* access modifiers changed from: private */
    public long lastPhoneBookDownloadTime = 0;
    public int lasthisindex = 0;
    public int lastindex = 0;
    private AudioManager.OnAudioFocusChangeListener mAudioFocusListener = new AudioManager.OnAudioFocusChangeListener() {
        public void onAudioFocusChange(int focusChange) {
            switch (focusChange) {
                case HeaderTokenizer.Token.COMMENT /*-3*/:
                case -2:
                case -1:
                    BluetoothService.logger.debug("mAudioFocusListener bluetooth  AUDIOFOCUS_LOSS ,talkingflag:" + BluetoothService.talkingflag);
                    if (BluetoothService.talkingflag) {
                        BluetoothService.this.requestAudioFocus();
                    }
                    BluetoothService.this.iHoldFocus = false;
                    return;
                case 1:
                    BluetoothService.this.iHoldFocus = true;
                    return;
                default:
                    return;
            }
        }
    };
    public AudioManager mAudioManager = null;
    public PlayerBinder mBinder = new PlayerBinder();
    private AudioManager.OnAudioFocusChangeListener mBlueMusicFocusListener = new AudioManager.OnAudioFocusChangeListener() {
        public void onAudioFocusChange(int focusChange) {
            switch (focusChange) {
                case HeaderTokenizer.Token.COMMENT /*-3*/:
                    BluetoothService.logger.debug("requestAudioFocus\tbtmusic AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK" + BluetoothService.mediaPlayState);
                    if (SysConst.isBT() || BluetoothService.this.app.ismix) {
                        BluetoothService.this.setBTVolume((int) (SysConst.musicDecVolume * 100.0f));
                    } else if (BluetoothService.mediaPlayState == 1418) {
                        BluetoothService.this.pausePlaySync(false);
                    }
                    BluetoothService.this.blueMusicFocus = true;
                    break;
                case -2:
                    BluetoothService.logger.debug("requestAudioFocus\tbtmusic AUDIOFOCUS_LOSS_TRANSIENT" + BluetoothService.mediaPlayState);
                    if (BluetoothService.mediaPlayState == 1418 && BluetoothService.this.blueMusicFocus) {
                        BluetoothService.this.pausePlaySync(false);
                    }
                    BluetoothService.this.blueMusicFocus = false;
                    break;
                case -1:
                    BluetoothService.logger.debug("btmusic AUDIOFOCUS_LOSS  " + BluetoothService.mediaPlayState);
                    BluetoothService.this.stopBTMusic();
                    break;
                case 1:
                    BluetoothService.this.blueMusicFocus = true;
                    BluetoothService.logger.debug("requestAudioFocus\tbtmusic AUDIOFOCUS_GAIN" + BluetoothService.mediaPlayState);
                    BluetoothService.this.setBTVolume((int) (SysConst.musicNorVolume * 100.0f));
                    if (1419 == BluetoothService.mediaPlayState && !BluetoothService.this.ipause) {
                        BluetoothService.this.pausePlaySync(true);
                    }
                    BluetoothService.this.app.musicType = 2;
                    break;
            }
            BluetoothService.this.app.sendMessage(BluetoothService.mediaPlayState, (Bundle) null);
        }
    };
    /* access modifiers changed from: private */
    public Context mContext;
    /* access modifiers changed from: private */
    public SpUtilK mSpUtilK;
    public String music = "";
    public boolean onPairingResult = false;
    private Handler serviceHandler = new Handler();
    /* access modifiers changed from: private */
    public SharedPreferences shared_device;
    public boolean showDialogflag = false;
    public String singer = "";
    /* access modifiers changed from: private */
    public Thread timeThread = null;

    public IBinder onBind(Intent arg0) {
        return this.mBinder;
    }

    public void onCreate() {
        super.onCreate();
        Log.e("", "BluetoothService ==== ");
        try {
            this.mContext = this;
            this.bluetooth = Bluetooth.getInstance();
            this.shared_device = getSharedPreferences("device_name", 0);
            this.mSpUtilK = new SpUtilK(getApplicationContext());
            this.mAudioManager = (AudioManager) getSystemService("audio");
            this.bluetooth.addEventHandler(new BluetoothHandler());
            Mainboard.getInstance().addEventHandler(new BluetoothHandler());
            File file = new File(this.Apppath);
            if (!file.exists()) {
                file.mkdirs();
            }
            Parser.initialize();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.app = (LauncherApplication) getApplication();
        this.app.btservice = this;
        this.app.openOrCloseBluetooth(this.app.getNeedToEnterPairMode());
        this.serviceHandler.postDelayed(new Runnable() {
            public void run() {
                try {
                    BluetoothService.this.readPhoneStatus();
                    BluetoothService.this.readPairingList();
                    BluetoothService.this.setDeviceName(BluetoothService.this.mContext);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 1500);
        IntentFilter assiFilter = new IntentFilter();
        assiFilter.addAction(VoiceAssistantConst.EVENT_ANALYTIC_CMD);
        registerReceiver(this.assistantReceiver, assiFilter);
        this.serviceHandler.postDelayed(new Runnable() {
            public void run() {
                BluetoothService.this.sendBookchanged();
            }
        }, 10000);
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, 1, startId);
    }

    public void requestAudioFocus() {
        boolean z = true;
        logger.debug("bluetooth btservice requestAudioFocus");
        int flag = this.mAudioManager.requestAudioFocus(this.mAudioFocusListener, 3, 2);
        int count = 0;
        while (flag != 1 && count < 2) {
            count++;
            flag = this.mAudioManager.requestAudioFocus(this.mAudioFocusListener, 3, 2);
        }
        if (flag != 1) {
            z = false;
        }
        this.iHoldFocus = z;
    }

    public void unrequestAudioFocus() {
        logger.debug("mAudioFocusListener btservice unrequestAudioFocus");
        this.mAudioManager.abandonAudioFocus(this.mAudioFocusListener);
    }

    public void onDestroy() {
        super.onDestroy();
        mediaPlayState = 1;
        unrequestAudioFocus();
        Parser.release();
        stopForeground(true);
    }

    public class PlayerBinder extends Binder {
        public PlayerBinder() {
        }

        public BluetoothService getService() {
            return BluetoothService.this;
        }
    }

    class BluetoothHandler implements IBluetoothEventHandler {
        BluetoothHandler() {
        }

        public void onAnswerPhone(AnswerPhoneResult arg0) {
        }

        public void onCallOut(CallOutResult arg0) {
        }

        public void onHangUpPhone(HangUpPhoneResult arg0) {
            BluetoothService.logger.debug("bluetoothlog onHangUpPhone----");
            BluetoothService.this.unrequestAudioFocus();
            BluetoothService.this.isAfterInOrOutCall = false;
            BluetoothService.this.app.sendMessage(BluetoothService.HANGUP_PHONE, (Bundle) null);
        }

        public void onIncomingCall(IncomingCallProtocol arg0) {
            BluetoothService.logger.debug("FloatSystemCallDialog onIncomingCall arg0: " + arg0.getPhone() + " : ");
            if (BluetoothService.this.app.iIsScreenClose.get()) {
                BluetoothService.this.app.closeOrWakeupScreen(false);
            }
            if (System.currentTimeMillis() - BluetoothService.this.lastCloseCallingViewTime >= 5000) {
                BluetoothService.bluetoothStatus = EPhoneStatus.INCOMING_CALL;
                FloatSystemCallDialog.getInstance().setShowStatus(FloatSystemCallDialog.FloatShowST.INCOMING);
                BluetoothService.this.app.phoneNumber = arg0.getPhone();
                if (TextUtils.isEmpty(BluetoothService.this.app.phoneNumber)) {
                    BluetoothService.this.app.phoneNumber = "";
                }
                BluetoothService.this.app.phoneName = BluetoothService.this.getbookName(BluetoothService.this.app.phoneNumber);
                BluetoothService.this.iHoldFocus = false;
                BluetoothService.currentCallingType = 1412;
                BluetoothService.iIsCallingInButNotalkingState = true;
                BluetoothService.this.isAfterInOrOutCall = true;
                BluetoothService.this.app.sendMessage(BluetoothService.UPDATE_PHONENUM, (Bundle) null);
                BluetoothService.this.sendVoiceAssistantStartEvent();
                if (BluetoothService.this.app.service.iIsInRecorder || BluetoothService.this.app.service.iIsInOriginal || BluetoothService.this.app.service.iIsBTConnect) {
                    BluetoothService.this.app.interAndroidView();
                    BluetoothService.this.app.service.iIsInRecorder = false;
                    BluetoothService.this.app.service.iIsInOriginal = false;
                    BluetoothService.this.app.service.iIsBTConnect = false;
                }
            }
        }

        public void onMediaInfo(MediaInfoProtocol arg0) {
            LauncherApplication.isBlueConnectState = true;
            try {
                BluetoothService.this.music = arg0.getTitle();
                BluetoothService.this.singer = arg0.getArtist();
                if (!TextUtils.isEmpty(BluetoothService.this.singer)) {
                    BluetoothService.this.music = String.valueOf(BluetoothService.this.music) + " - " + BluetoothService.this.singer;
                }
                BluetoothService.this.app.sendMessage(BluetoothService.MEDIAINFO, (Bundle) null);
                BluetoothService.logger.debug("bluetoothprotocal onMediaInfo" + BluetoothService.this.music + "singer::" + BluetoothService.this.singer);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void onMediaPlayStatus(MediaPlayStatusProtocol arg0) {
            BluetoothService.logger.debug("bluetoothprotocal MediaPlayStatus" + arg0.getMediaPlayStatus());
            if (arg0.getMediaPlayStatus() == EMediaPlayStatus.PLAYING) {
                LauncherApplication.isBlueConnectState = true;
                BluetoothService.mediaPlayState = BluetoothService.PLAY_STATE_PLAYING;
                BluetoothService.this.readMediaInfo();
                if (BluetoothService.this.blueMusicFocus) {
                    BluetoothService.this.requestMusicAudioFocus();
                }
            } else if (arg0.getMediaPlayStatus() == EMediaPlayStatus.PAUSE) {
                BluetoothService.mediaPlayState = BluetoothService.PLAY_STATE_PAUSE;
            } else {
                BluetoothService.this.music = " ";
                BluetoothService.this.singer = " ";
                BluetoothService.mediaPlayState = BluetoothService.PLAY_STATE_STOP;
            }
            BluetoothService.this.app.sendMessage(BluetoothService.mediaPlayState, (Bundle) null);
        }

        public void onMediaStatus(MediaStatusProtocol arg0) {
        }

        public void onPairingList(PairingListProtocol list) {
            BluetoothService.logger.debug("onPairingList " + list.getUnits().size());
            BluetoothService.equipList.clear();
            List<PairingListUnitProtocol> tempList = list.getUnits();
            for (int i = 0; i < tempList.size(); i++) {
                Equip temp = new Equip();
                temp.setIndex(i);
                temp.setName(tempList.get(i).getDeviceName());
                temp.setAddress(tempList.get(i).getDeviceAddress());
                BluetoothService.equipList.add(temp);
            }
            if (BluetoothService.equipList.isEmpty()) {
                BluetoothService.logger.debug("#############  equipList.isEmpty()");
                try {
                    Thread.sleep(2000);
                    BluetoothService.this.readPairingList();
                } catch (Exception e) {
                }
            } else {
                BluetoothService.logger.debug("addr : " + BluetoothService.equipList.get(0).getAddress());
            }
        }

        public void onPhoneBookCtrlStatus(PhoneBookCtrlStatusProtocol arg0) {
            if (arg0.getPhoneBookCtrlStatus() == EPhoneBookCtrlStatus.CONNECTED && System.currentTimeMillis() - BluetoothService.this.lastPhoneBookDownloadTime > 10000) {
                BluetoothService.this.downloadflag = 1;
                if (BluetoothService.this.downloadType == 0) {
                    BluetoothService.this.downloadPhoneBook();
                } else {
                    BluetoothService.this.downloadRecordList();
                }
                BluetoothService.this.app.sendMessage(BluetoothService.BOOK_LIST_START_LOAD, (Bundle) null);
            } else if (arg0.getPhoneBookCtrlStatus() == EPhoneBookCtrlStatus.UNCONNECT) {
                BluetoothService.this.downloadflag = -1;
                BluetoothService.this.app.sendMessage(BluetoothService.BOOK_LIST_CANCEL_LOAD, (Bundle) null);
            }
        }

        public void onPhoneBookList(PhoneBookListProtocol arg0) {
            BluetoothService.this.lastPhoneBookDownloadTime = System.currentTimeMillis();
            try {
                List<VcardUnit> list = new LibVcard(arg0.getPlayload()).getList();
                ArrayList<Person> bookList = new ArrayList<>();
                if (BluetoothService.this.downloadType == 0) {
                    File file = new File(String.valueOf(BluetoothService.this.Apppath) + "/" + BluetoothService.currentEquipAddress + "_Per.xml");
                    if (!file.exists()) {
                        file.createNewFile();
                    }
                    bookList.clear();
                    for (int i = 0; i < list.size(); i++) {
                        int phonesize = list.get(i).getNumbers().size();
                        for (int j = 0; j < phonesize; j++) {
                            Person addr = new Person();
                            addr.setId(String.valueOf(j) + "0000" + i);
                            addr.setName(list.get(i).getName());
                            addr.setFlag(0);
                            addr.setPhone(list.get(i).getNumbers().get(j));
                            String sortString = Parser.getAllPinyin(list.get(i).getName()).toUpperCase();
                            if ("我的编号".endsWith(list.get(i).getName())) {
                                String sortString2 = "0000" + sortString;
                            } else {
                                addr.setRemark(sortString);
                                bookList.add(addr);
                            }
                        }
                    }
                    Collections.sort(bookList, new PinyinComparator());
                    PersonParse.save(bookList, new FileOutputStream(file));
                }
            } catch (Exception e) {
                e.printStackTrace();
                BluetoothService.logger.debug("parse error " + e);
            }
            BluetoothService.this.downloadflag = 2;
            BluetoothService.this.app.sendMessage(BluetoothService.DRIVER_BOOK_LIST, (Bundle) null);
            BluetoothService.this.sendBookchanged();
            BluetoothService.this.lastPhoneBookDownloadTime = System.currentTimeMillis();
        }

        public void onPhoneStatus(PhoneStatusProtocol arg0) {
            if (arg0 != null && arg0.getPhoneStatus() != null) {
                BluetoothService.bluetoothStatus = arg0.getPhoneStatus();
                BluetoothService.logger.debug("onPhoneStatus: " + BluetoothService.bluetoothStatus);
                if (arg0.getPhoneStatus() == EPhoneStatus.CALLING_OUT) {
                    LauncherApplication.isBlueConnectState = true;
                    BluetoothService.logger.debug("FloatSystemCallDialog onPhoneStatus: CALLING_OUT");
                    BluetoothService.this.accFlag = true;
                    BluetoothService.this.showDialogflag = true;
                    BluetoothService.currentCallingType = 1413;
                    BluetoothService.iIsCallingInButNotalkingState = true;
                } else if (arg0.getPhoneStatus() == EPhoneStatus.CONNECTED) {
                    BluetoothService.logger.debug("FloatSystemCallDialog onPhoneStatus: CONNECTED");
                    if (!LauncherApplication.isBlueConnectState) {
                        EcarManager.getInstance(BluetoothService.this.mContext).sendBluetoothState(BluetoothService.this.mContext, 2);
                    }
                    LauncherApplication.isBlueConnectState = true;
                    BluetoothService.this.showDialogflag = false;
                    if (BluetoothService.talkingflag) {
                        BluetoothService.talkingflag = false;
                        BluetoothService.this.calltime = 0;
                    }
                    if (BluetoothService.iIsCallingInButNotalkingState) {
                        if (BluetoothService.currentCallingType != 1413 && !BluetoothService.this.histalkflag && !TextUtils.isEmpty(BluetoothService.this.app.phoneNumber)) {
                            BluetoothService.this.setHistoryList(1414);
                        }
                        BluetoothService.iIsCallingInButNotalkingState = false;
                        BluetoothService.this.histalkflag = false;
                        BluetoothService.currentCallingType = 0;
                        BluetoothService.logger.debug("----------------app.phoneNumber = " + BluetoothService.this.app.phoneNumber + ",app.phoneName =" + BluetoothService.this.app.phoneName);
                        BluetoothService.this.app.phoneNumber = "";
                        BluetoothService.this.app.phoneName = "";
                    }
                    BluetoothService.this.sendVoiceAssistantCallEndEvent();
                    BluetoothService.currentEquipAddress = BluetoothService.this.shared_device.getString("addr", "");
                    BluetoothService.this.currentEquipName = BluetoothService.this.shared_device.getString(Contacts.PeopleColumns.NAME, "");
                    BluetoothService.this.app.sendMessage(SysConst.BLUETOOTH_PHONE_STATE, (Bundle) null);
                } else if (arg0.getPhoneStatus() == EPhoneStatus.UNCONNECT) {
                    BluetoothService.logger.debug("FloatSystemCallDialog onPhoneStatus: UNCONNECT");
                    LauncherApplication.isBlueConnectState = false;
                    EcarManager.getInstance(BluetoothService.this.mContext).sendBluetoothState(BluetoothService.this.mContext, 1);
                    BluetoothService.this.app.isCalling = false;
                    BluetoothService.this.isAfterInOrOutCall = false;
                    BluetoothService.this.showDialogflag = false;
                    BluetoothService.this.lastindex = 0;
                    BluetoothService.this.lasthisindex = 0;
                    BluetoothService.this.downloadflag = -1;
                    BluetoothService.talkingflag = false;
                    BluetoothService.iIsCallingInButNotalkingState = false;
                    BluetoothService.this.music = " ";
                    BluetoothService.this.singer = " ";
                    BluetoothService.this.bookList.clear();
                    BluetoothService.this.cutdownCurrentCalling();
                    BluetoothService.this.app.sendMessage(BluetoothService.MEDIAINFO, (Bundle) null);
                    BluetoothService.this.app.sendMessage(SysConst.BLUETOOTH_PHONE_STATE, (Bundle) null);
                } else if (arg0.getPhoneStatus() == EPhoneStatus.TALKING) {
                    LauncherApplication.isBlueConnectState = true;
                    BluetoothService.logger.debug("FloatSystemCallDialog onPhoneStatus: TALKING");
                    if (BluetoothService.this.app.playpos != 1 || (!Build.MODEL.equals("c200") && !Build.MODEL.equals("c200_en") && !Build.MODEL.equals("c200_ks"))) {
                        Mainboard.getInstance().setAllHornSoundValue(SysConst.basicNum, SysConst.callnum[BluetoothService.this.mSpUtilK.getInt(SysConst.callVoice, 3)], 0, 0, 0);
                    } else {
                        Mainboard.getInstance().setAllHornSoundValue(SysConst.basicNum, SysConst.callnum[BluetoothService.this.mSpUtilK.getInt(SysConst.callVoice, 3)], 0, SysConst.callnum[BluetoothService.this.mSpUtilK.getInt(SysConst.callVoice, 3)], SysConst.callnum[BluetoothService.this.mSpUtilK.getInt(SysConst.callVoice, 3)]);
                    }
                    if (!BluetoothService.this.isAfterInOrOutCall) {
                        BluetoothService.logger.debug("FloatSystemCallDialog onPhoneStatus: NoCallTALKING");
                        return;
                    }
                    FloatSystemCallDialog.getInstance().setShowStatus(FloatSystemCallDialog.FloatShowST.TALKING);
                    BluetoothService.iIsCallingInButNotalkingState = false;
                    BluetoothService.this.showDialogflag = true;
                    BluetoothService.this.histalkflag = true;
                    if (!BluetoothService.talkingflag) {
                        BluetoothService.this.calltime = 0;
                        BluetoothService.talkingflag = true;
                        BluetoothService.this.app.sendMessage(BluetoothService.CALL_TALKING, (Bundle) null);
                        BluetoothService.this.timeThread = new Thread(new TimeThread(BluetoothService.this, (TimeThread) null));
                        BluetoothService.this.timeThread.start();
                    }
                    BluetoothService.this.requestAudioFocus();
                    BluetoothService.this.sendVoiceAssistantInTalkingEvent();
                    BluetoothService.logger.debug("bluetoothlog  TALKING...........");
                    if (BluetoothService.currentCallingType == 1412) {
                        BluetoothService.this.setHistoryList(1412);
                    }
                    BluetoothService.currentCallingType = BluetoothService.CALL_TALKING;
                    if (!BluetoothService.this.app.topIsBlueMainFragment() && FloatSystemCallDialog.getInstance().isDestory()) {
                        BluetoothService.this.app.mHandler.obtainMessage(BluetoothService.OUTCALL_FLOAT).sendToTarget();
                    }
                } else if (arg0.getPhoneStatus() == EPhoneStatus.CONNECTING) {
                    BluetoothService.logger.debug("FloatSystemCallDialog onPhoneStatus: CONNECTING");
                    BluetoothService.talkingflag = false;
                    BluetoothService.iIsCallingInButNotalkingState = false;
                } else if (arg0.getPhoneStatus() == EPhoneStatus.INCOMING_CALL) {
                    LauncherApplication.isBlueConnectState = true;
                    BluetoothService.logger.debug("FloatSystemCallDialog onPhoneStatus: INCOMING_CALL");
                    BluetoothService.iIsCallingInButNotalkingState = true;
                    BluetoothService.currentCallingType = 1412;
                    BluetoothService.this.showDialogflag = true;
                    BluetoothService.this.accFlag = true;
                }
            }
        }

        public void onSetPlayStatus(SetPlayStatusProtocol arg0) {
        }

        public void onVersion(VersionProtocol arg0) {
        }

        public void onConnectedDevice(ConnectedDeviceProtocol arg0) {
            if (!BluetoothService.currentEquipAddress.equals(arg0.getDeviceAddress())) {
                BluetoothService.this.currentEquipName = "";
            }
            BluetoothService.currentEquipAddress = arg0.getDeviceAddress();
            BluetoothService.this.currentEquipName = arg0.getDeviceName();
            BluetoothService.logger.debug("onConnectedDevice: " + BluetoothService.currentEquipAddress);
            if (!BluetoothService.equipList.isEmpty() && TextUtils.isEmpty(BluetoothService.this.currentEquipName)) {
                int i = 0;
                while (true) {
                    if (i >= BluetoothService.equipList.size()) {
                        break;
                    }
                    Equip equip = BluetoothService.equipList.get(i);
                    if (BluetoothService.currentEquipAddress.equals(equip.getAddress())) {
                        BluetoothService.this.currentEquipName = equip.getName();
                        break;
                    }
                    i++;
                }
            }
            BluetoothService.this.putDeviceInfo(BluetoothService.this.currentEquipName, BluetoothService.currentEquipAddress);
            BluetoothService.this.app.sendMessage(BluetoothService.UPDATE_NAME, (Bundle) null);
            BluetoothService.this.CreateFile(String.valueOf(BluetoothService.currentEquipAddress) + "_Per.xml");
            BluetoothService.this.CreateFile(String.valueOf(BluetoothService.currentEquipAddress) + "_His.xml");
            BluetoothService.this.CreatePerFile(BluetoothService.currentEquipAddress);
            if (!BluetoothService.this.app.iNeedToChangeLocalContacts(BluetoothService.currentEquipAddress)) {
                BluetoothService.this.app.setLastDeviceName(BluetoothService.currentEquipAddress);
            }
            BluetoothService.this.sendBookchanged();
            BluetoothService.logger.debug("onConnectedDevice: " + BluetoothService.currentEquipAddress);
        }

        public void onDeviceRemoved(DeviceRemovedProtocol arg0) {
        }

        public void ondeviceSwitchedProtocol(DeviceSwitchedProtocol arg0) {
            BluetoothService.logger.debug("FloatSystemCallDialog  [sound] ondeviceSwitchedProtocol: " + arg0.getConnectedDevice());
            if (arg0.getConnectedDevice() == EConnectedDevice.LOCAL) {
                BluetoothService.this.iSoundInPhone = false;
            } else {
                BluetoothService.this.iSoundInPhone = true;
            }
            BluetoothService.logger.debug("bluetooth ondeviceSwitchedProtocol requestAudioFocus isAfterInOrOutCall : " + BluetoothService.this.isAfterInOrOutCall);
        }

        public void onDeviceName(DeviceNameProtocol arg0) {
            BluetoothService.logger.debug("device : " + arg0.getDeviceName());
            BluetoothService.deviceMAC = arg0.getMACAdress();
            BluetoothService.devicePIN = arg0.getPIN();
        }

        public void onPhoneCallingOut(CallingOutProtocol arg0) {
            BluetoothService.bluetoothStatus = EPhoneStatus.CALLING_OUT;
            BluetoothService.logger.debug("FloatSystemCallDialog  onPhoneCallingOut...." + arg0.getPhoneNumber());
            BluetoothService.this.app.phoneNumber = arg0.getPhoneNumber();
            FloatSystemCallDialog.getInstance().setShowStatus(FloatSystemCallDialog.FloatShowST.OUTCALLING);
            BluetoothService.currentCallingType = 1413;
            BluetoothService.this.isAfterInOrOutCall = true;
            if (!TextUtils.isEmpty(BluetoothService.this.app.phoneNumber)) {
                BluetoothService.this.app.sendMessage(BluetoothService.UPDATE_PHONENUM, (Bundle) null);
                BluetoothService.this.app.phoneName = BluetoothService.this.getbookName(BluetoothService.this.app.phoneNumber);
                BluetoothService.this.setHistoryList(1413);
            }
            BluetoothService.this.app.mHandler.obtainMessage(BluetoothService.OUTCALL_FLOAT).sendToTarget();
            BluetoothService.this.requestAudioFocus();
            if (BluetoothService.this.app.service.iIsInRecorder || BluetoothService.this.app.service.iIsInOriginal || BluetoothService.this.app.service.iIsBTConnect) {
                BluetoothService.this.app.interAndroidView();
                BluetoothService.this.app.service.iIsInRecorder = false;
                BluetoothService.this.app.service.iIsInOriginal = false;
                BluetoothService.this.app.service.iIsBTConnect = false;
            }
        }

        public void onPhoneBook(String name, String number) {
            if (BluetoothService.this.booklistIndex < 1) {
                BluetoothService.this.bookList.clear();
                BluetoothService.this.app.sendMessage(BluetoothService.BOOK_LIST_START_LOAD, (Bundle) null);
            }
            BluetoothService.this.downloadflag = 1;
            Person addr = new Person();
            addr.setId(String.valueOf(BluetoothService.this.booklistIndex));
            addr.setName(name);
            addr.setFlag(0);
            addr.setPhone(number.replace(" ", "").replace("-", ""));
            String sortString = Parser.getAllPinyin(name).toUpperCase();
            if ("我的编号".endsWith(name)) {
                String sortString2 = "0000" + sortString;
                return;
            }
            addr.setRemark(sortString);
            BluetoothService bluetoothService = BluetoothService.this;
            bluetoothService.booklistIndex = bluetoothService.booklistIndex + 1;
            BluetoothService.this.bookList.add(addr);
        }

        public void onFinishDownloadPhoneBook() {
            BluetoothService.logger.debug("onFinishDownloadPhoneBook " + BluetoothService.this.bookList.size());
            try {
                Collections.sort(BluetoothService.this.bookList, new PinyinComparator());
                PersonParse.save(BluetoothService.this.bookList, new FileOutputStream(new File(String.valueOf(BluetoothService.this.Apppath) + "/" + BluetoothService.currentEquipAddress + "_Per.xml")));
            } catch (Exception e) {
                e.printStackTrace();
            }
            BluetoothService.this.downloadflag = 2;
            if (BluetoothService.this.downloadType == 0) {
                BluetoothService.this.booklistIndex = 0;
                BluetoothService.this.sendBookchanged();
                BluetoothService.this.app.sendMessage(BluetoothService.DRIVER_BOOK_LIST, (Bundle) null);
                BluetoothService.this.lastPhoneBookDownloadTime = System.currentTimeMillis();
            }
        }

        public void onPairedDevice(String deviceAddress, String deviceName) {
        }

        public void onPairingModeResult(EnterPairingModeResult arg0) {
            BluetoothService.this.onPairingResult = arg0.isSuccess();
            BluetoothService.logger.debug("onPairingModeResult " + BluetoothService.this.onPairingResult);
            BluetoothService.this.app.sendMessage(BluetoothService.START_PAIRMODE, (Bundle) null);
        }

        public void onPairingModeEnd() {
            BluetoothService.this.onPairingResult = false;
            BluetoothService.this.app.sendMessage(BluetoothService.END_PAIRMODE, (Bundle) null);
        }
    }

    public void savePhoneBookList(ArrayList<Person> newBookList) {
        try {
            File file = new File(String.valueOf(this.Apppath) + "/" + currentEquipAddress + "_Per.xml");
            if (!file.exists()) {
                file.createNewFile();
            }
            PersonParse.save(newBookList, new FileOutputStream(file));
        } catch (Exception e) {
            e.printStackTrace();
            logger.debug("save error " + e);
        }
        this.app.sendMessage(DRIVER_BOOK_LIST, (Bundle) null);
    }

    private class TimeThread implements Runnable {
        private TimeThread() {
        }

        /* synthetic */ TimeThread(BluetoothService bluetoothService, TimeThread timeThread) {
            this();
        }

        public void run() {
            while (BluetoothService.talkingflag) {
                try {
                    BluetoothService.this.calltime++;
                    BluetoothService.this.app.sendMessage(BluetoothService.TIME_FLAG, (Bundle) null);
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String getbookName(String curNum) {
        ArrayList<Person> pers = getBookList();
        String name = "";
        int i = 0;
        while (i < pers.size()) {
            try {
                if (curNum.equals(pers.get(i).getPhone())) {
                    name = pers.get(i).getName();
                } else if ((pers.get(i).getPhone().contains(curNum) || curNum.contains(pers.get(i).getPhone())) && curNum.length() > 10 && pers.get(i).getPhone().length() > 10) {
                    name = pers.get(i).getName();
                }
                i++;
            } catch (Exception e) {
                e.printStackTrace();
                logger.debug("get name error e:" + e.getMessage());
            }
        }
        if (TextUtils.isEmpty(name)) {
            return curNum;
        }
        return name;
    }

    /* JADX WARNING: Removed duplicated region for block: B:27:0x00a8 A[SYNTHETIC, Splitter:B:27:0x00a8] */
    /* JADX WARNING: Removed duplicated region for block: B:34:0x00b6 A[SYNTHETIC, Splitter:B:34:0x00b6] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.util.ArrayList<com.touchus.publicutils.bean.Person> getBookList() {
        /*
            r9 = this;
            r2 = 0
            java.util.ArrayList r4 = new java.util.ArrayList
            r4.<init>()
            boolean r6 = com.touchus.benchilauncher.LauncherApplication.isBlueConnectState
            if (r6 != 0) goto L_0x000c
            r5 = r4
        L_0x000b:
            return r5
        L_0x000c:
            java.io.File r1 = new java.io.File     // Catch:{ Exception -> 0x00a2 }
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x00a2 }
            java.lang.String r7 = r9.Apppath     // Catch:{ Exception -> 0x00a2 }
            java.lang.String r7 = java.lang.String.valueOf(r7)     // Catch:{ Exception -> 0x00a2 }
            r6.<init>(r7)     // Catch:{ Exception -> 0x00a2 }
            java.lang.String r7 = "/"
            java.lang.StringBuilder r6 = r6.append(r7)     // Catch:{ Exception -> 0x00a2 }
            java.lang.String r7 = currentEquipAddress     // Catch:{ Exception -> 0x00a2 }
            java.lang.StringBuilder r6 = r6.append(r7)     // Catch:{ Exception -> 0x00a2 }
            java.lang.String r7 = "_Per.xml"
            java.lang.StringBuilder r6 = r6.append(r7)     // Catch:{ Exception -> 0x00a2 }
            java.lang.String r6 = r6.toString()     // Catch:{ Exception -> 0x00a2 }
            r1.<init>(r6)     // Catch:{ Exception -> 0x00a2 }
            boolean r6 = r1.exists()     // Catch:{ Exception -> 0x00a2 }
            if (r6 != 0) goto L_0x003b
            r1.createNewFile()     // Catch:{ Exception -> 0x00a2 }
        L_0x003b:
            java.io.FileInputStream r3 = new java.io.FileInputStream     // Catch:{ Exception -> 0x00a2 }
            r3.<init>(r1)     // Catch:{ Exception -> 0x00a2 }
            java.util.ArrayList r4 = com.touchus.publicutils.utils.PersonParse.getPersons(r3)     // Catch:{ Exception -> 0x00c6, all -> 0x00bf }
            if (r4 != 0) goto L_0x0070
            java.util.ArrayList r5 = new java.util.ArrayList     // Catch:{ Exception -> 0x00c6, all -> 0x00bf }
            r5.<init>()     // Catch:{ Exception -> 0x00c6, all -> 0x00bf }
            org.slf4j.Logger r6 = logger     // Catch:{ Exception -> 0x00c9, all -> 0x00c2 }
            java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x00c9, all -> 0x00c2 }
            java.lang.String r8 = "onPhoneBookList::getBookList::"
            r7.<init>(r8)     // Catch:{ Exception -> 0x00c9, all -> 0x00c2 }
            int r8 = r5.size()     // Catch:{ Exception -> 0x00c9, all -> 0x00c2 }
            java.lang.StringBuilder r7 = r7.append(r8)     // Catch:{ Exception -> 0x00c9, all -> 0x00c2 }
            java.lang.String r8 = "::"
            java.lang.StringBuilder r7 = r7.append(r8)     // Catch:{ Exception -> 0x00c9, all -> 0x00c2 }
            java.lang.String r8 = currentEquipAddress     // Catch:{ Exception -> 0x00c9, all -> 0x00c2 }
            java.lang.StringBuilder r7 = r7.append(r8)     // Catch:{ Exception -> 0x00c9, all -> 0x00c2 }
            java.lang.String r7 = r7.toString()     // Catch:{ Exception -> 0x00c9, all -> 0x00c2 }
            r6.debug(r7)     // Catch:{ Exception -> 0x00c9, all -> 0x00c2 }
            r4 = r5
        L_0x0070:
            org.slf4j.Logger r6 = logger     // Catch:{ Exception -> 0x00c6, all -> 0x00bf }
            java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x00c6, all -> 0x00bf }
            java.lang.String r8 = "onPhoneBookList::getBookList::"
            r7.<init>(r8)     // Catch:{ Exception -> 0x00c6, all -> 0x00bf }
            int r8 = r4.size()     // Catch:{ Exception -> 0x00c6, all -> 0x00bf }
            java.lang.StringBuilder r7 = r7.append(r8)     // Catch:{ Exception -> 0x00c6, all -> 0x00bf }
            java.lang.String r8 = "::"
            java.lang.StringBuilder r7 = r7.append(r8)     // Catch:{ Exception -> 0x00c6, all -> 0x00bf }
            java.lang.String r8 = currentEquipAddress     // Catch:{ Exception -> 0x00c6, all -> 0x00bf }
            java.lang.StringBuilder r7 = r7.append(r8)     // Catch:{ Exception -> 0x00c6, all -> 0x00bf }
            java.lang.String r7 = r7.toString()     // Catch:{ Exception -> 0x00c6, all -> 0x00bf }
            r6.debug(r7)     // Catch:{ Exception -> 0x00c6, all -> 0x00bf }
            if (r3 == 0) goto L_0x0099
            r3.close()     // Catch:{ IOException -> 0x009d }
        L_0x0099:
            r2 = r3
            r5 = r4
            goto L_0x000b
        L_0x009d:
            r0 = move-exception
            r0.printStackTrace()
            goto L_0x0099
        L_0x00a2:
            r0 = move-exception
        L_0x00a3:
            r0.printStackTrace()     // Catch:{ all -> 0x00b3 }
            if (r2 == 0) goto L_0x00ab
            r2.close()     // Catch:{ IOException -> 0x00ae }
        L_0x00ab:
            r5 = r4
            goto L_0x000b
        L_0x00ae:
            r0 = move-exception
            r0.printStackTrace()
            goto L_0x00ab
        L_0x00b3:
            r6 = move-exception
        L_0x00b4:
            if (r2 == 0) goto L_0x00b9
            r2.close()     // Catch:{ IOException -> 0x00ba }
        L_0x00b9:
            throw r6
        L_0x00ba:
            r0 = move-exception
            r0.printStackTrace()
            goto L_0x00b9
        L_0x00bf:
            r6 = move-exception
            r2 = r3
            goto L_0x00b4
        L_0x00c2:
            r6 = move-exception
            r4 = r5
            r2 = r3
            goto L_0x00b4
        L_0x00c6:
            r0 = move-exception
            r2 = r3
            goto L_0x00a3
        L_0x00c9:
            r0 = move-exception
            r4 = r5
            r2 = r3
            goto L_0x00a3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.touchus.benchilauncher.service.BluetoothService.getBookList():java.util.ArrayList");
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x0079 A[SYNTHETIC, Splitter:B:17:0x0079] */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x0086 A[SYNTHETIC, Splitter:B:24:0x0086] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.util.ArrayList<com.touchus.publicutils.bean.Person> getHistoryList() {
        /*
            r9 = this;
            r2 = 0
            java.util.ArrayList r4 = new java.util.ArrayList
            r4.<init>()
            boolean r6 = com.touchus.benchilauncher.LauncherApplication.isBlueConnectState
            if (r6 != 0) goto L_0x000c
            r5 = r4
        L_0x000b:
            return r5
        L_0x000c:
            java.io.File r1 = new java.io.File     // Catch:{ Exception -> 0x006c }
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x006c }
            java.lang.String r7 = r9.Apppath     // Catch:{ Exception -> 0x006c }
            java.lang.String r7 = java.lang.String.valueOf(r7)     // Catch:{ Exception -> 0x006c }
            r6.<init>(r7)     // Catch:{ Exception -> 0x006c }
            java.lang.String r7 = "/"
            java.lang.StringBuilder r6 = r6.append(r7)     // Catch:{ Exception -> 0x006c }
            java.lang.String r7 = currentEquipAddress     // Catch:{ Exception -> 0x006c }
            java.lang.StringBuilder r6 = r6.append(r7)     // Catch:{ Exception -> 0x006c }
            java.lang.String r7 = "_His.xml"
            java.lang.StringBuilder r6 = r6.append(r7)     // Catch:{ Exception -> 0x006c }
            java.lang.String r6 = r6.toString()     // Catch:{ Exception -> 0x006c }
            r1.<init>(r6)     // Catch:{ Exception -> 0x006c }
            java.io.FileInputStream r3 = new java.io.FileInputStream     // Catch:{ Exception -> 0x006c }
            r3.<init>(r1)     // Catch:{ Exception -> 0x006c }
            java.util.ArrayList r4 = com.touchus.publicutils.utils.PersonParse.getPersons(r3)     // Catch:{ Exception -> 0x0092, all -> 0x008f }
            org.slf4j.Logger r6 = logger     // Catch:{ Exception -> 0x0092, all -> 0x008f }
            java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0092, all -> 0x008f }
            java.lang.String r8 = "onPhoneBookList::getHistoryList::"
            r7.<init>(r8)     // Catch:{ Exception -> 0x0092, all -> 0x008f }
            int r8 = r4.size()     // Catch:{ Exception -> 0x0092, all -> 0x008f }
            java.lang.StringBuilder r7 = r7.append(r8)     // Catch:{ Exception -> 0x0092, all -> 0x008f }
            java.lang.String r8 = "::"
            java.lang.StringBuilder r7 = r7.append(r8)     // Catch:{ Exception -> 0x0092, all -> 0x008f }
            java.lang.String r8 = currentEquipAddress     // Catch:{ Exception -> 0x0092, all -> 0x008f }
            java.lang.StringBuilder r7 = r7.append(r8)     // Catch:{ Exception -> 0x0092, all -> 0x008f }
            java.lang.String r7 = r7.toString()     // Catch:{ Exception -> 0x0092, all -> 0x008f }
            r6.debug(r7)     // Catch:{ Exception -> 0x0092, all -> 0x008f }
            if (r3 == 0) goto L_0x0064
            r3.close()     // Catch:{ IOException -> 0x0067 }
        L_0x0064:
            r2 = r3
            r5 = r4
            goto L_0x000b
        L_0x0067:
            r0 = move-exception
            r0.printStackTrace()
            goto L_0x0064
        L_0x006c:
            r0 = move-exception
        L_0x006d:
            org.slf4j.Logger r6 = logger     // Catch:{ all -> 0x0083 }
            java.lang.String r7 = "onPhoneBookList::getHistoryList::Exception"
            r6.debug(r7)     // Catch:{ all -> 0x0083 }
            r0.printStackTrace()     // Catch:{ all -> 0x0083 }
            if (r2 == 0) goto L_0x007c
            r2.close()     // Catch:{ IOException -> 0x007e }
        L_0x007c:
            r5 = r4
            goto L_0x000b
        L_0x007e:
            r0 = move-exception
            r0.printStackTrace()
            goto L_0x007c
        L_0x0083:
            r6 = move-exception
        L_0x0084:
            if (r2 == 0) goto L_0x0089
            r2.close()     // Catch:{ IOException -> 0x008a }
        L_0x0089:
            throw r6
        L_0x008a:
            r0 = move-exception
            r0.printStackTrace()
            goto L_0x0089
        L_0x008f:
            r6 = move-exception
            r2 = r3
            goto L_0x0084
        L_0x0092:
            r0 = move-exception
            r2 = r3
            goto L_0x006d
        */
        throw new UnsupportedOperationException("Method not decompiled: com.touchus.benchilauncher.service.BluetoothService.getHistoryList():java.util.ArrayList");
    }

    /* JADX WARNING: Removed duplicated region for block: B:33:0x00c0 A[SYNTHETIC, Splitter:B:33:0x00c0] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void setHistoryList(int r15) {
        /*
            r14 = this;
            r12 = 0
            java.io.File r9 = new java.io.File     // Catch:{ Exception -> 0x00ae }
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x00ae }
            java.lang.String r2 = r14.Apppath     // Catch:{ Exception -> 0x00ae }
            java.lang.String r2 = java.lang.String.valueOf(r2)     // Catch:{ Exception -> 0x00ae }
            r1.<init>(r2)     // Catch:{ Exception -> 0x00ae }
            java.lang.String r2 = "/"
            java.lang.StringBuilder r1 = r1.append(r2)     // Catch:{ Exception -> 0x00ae }
            java.lang.String r2 = currentEquipAddress     // Catch:{ Exception -> 0x00ae }
            java.lang.StringBuilder r1 = r1.append(r2)     // Catch:{ Exception -> 0x00ae }
            java.lang.String r2 = "_His.xml"
            java.lang.StringBuilder r1 = r1.append(r2)     // Catch:{ Exception -> 0x00ae }
            java.lang.String r1 = r1.toString()     // Catch:{ Exception -> 0x00ae }
            r9.<init>(r1)     // Catch:{ Exception -> 0x00ae }
            boolean r1 = r9.exists()     // Catch:{ Exception -> 0x00ae }
            if (r1 != 0) goto L_0x0030
            r9.createNewFile()     // Catch:{ Exception -> 0x00ae }
        L_0x0030:
            java.io.FileInputStream r13 = new java.io.FileInputStream     // Catch:{ Exception -> 0x00ae }
            r13.<init>(r9)     // Catch:{ Exception -> 0x00ae }
            java.util.ArrayList r11 = com.touchus.publicutils.utils.PersonParse.getPersons(r13)     // Catch:{ Exception -> 0x00d2, all -> 0x00cf }
            int r1 = r11.size()     // Catch:{ Exception -> 0x00d2, all -> 0x00cf }
            r2 = 30
            if (r1 <= r2) goto L_0x0046
            r1 = 29
            r11.remove(r1)     // Catch:{ Exception -> 0x00d2, all -> 0x00cf }
        L_0x0046:
            org.slf4j.Logger r1 = logger     // Catch:{ Exception -> 0x00d2, all -> 0x00cf }
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x00d2, all -> 0x00cf }
            java.lang.String r3 = "app.phoneNumber = "
            r2.<init>(r3)     // Catch:{ Exception -> 0x00d2, all -> 0x00cf }
            com.touchus.benchilauncher.LauncherApplication r3 = r14.app     // Catch:{ Exception -> 0x00d2, all -> 0x00cf }
            java.lang.String r3 = r3.phoneNumber     // Catch:{ Exception -> 0x00d2, all -> 0x00cf }
            java.lang.StringBuilder r2 = r2.append(r3)     // Catch:{ Exception -> 0x00d2, all -> 0x00cf }
            java.lang.String r3 = ",app.phoneName = "
            java.lang.StringBuilder r2 = r2.append(r3)     // Catch:{ Exception -> 0x00d2, all -> 0x00cf }
            com.touchus.benchilauncher.LauncherApplication r3 = r14.app     // Catch:{ Exception -> 0x00d2, all -> 0x00cf }
            java.lang.String r3 = r3.phoneName     // Catch:{ Exception -> 0x00d2, all -> 0x00cf }
            java.lang.StringBuilder r2 = r2.append(r3)     // Catch:{ Exception -> 0x00d2, all -> 0x00cf }
            java.lang.String r2 = r2.toString()     // Catch:{ Exception -> 0x00d2, all -> 0x00cf }
            r1.debug(r2)     // Catch:{ Exception -> 0x00d2, all -> 0x00cf }
            long r6 = java.lang.System.currentTimeMillis()     // Catch:{ Exception -> 0x00d2, all -> 0x00cf }
            com.touchus.publicutils.bean.Person r0 = new com.touchus.publicutils.bean.Person     // Catch:{ Exception -> 0x00d2, all -> 0x00cf }
            java.lang.String r1 = java.lang.String.valueOf(r6)     // Catch:{ Exception -> 0x00d2, all -> 0x00cf }
            com.touchus.benchilauncher.LauncherApplication r2 = r14.app     // Catch:{ Exception -> 0x00d2, all -> 0x00cf }
            java.lang.String r2 = r2.phoneName     // Catch:{ Exception -> 0x00d2, all -> 0x00cf }
            boolean r2 = android.text.TextUtils.isEmpty(r2)     // Catch:{ Exception -> 0x00d2, all -> 0x00cf }
            if (r2 == 0) goto L_0x00a9
            com.touchus.benchilauncher.LauncherApplication r2 = r14.app     // Catch:{ Exception -> 0x00d2, all -> 0x00cf }
            java.lang.String r2 = r2.phoneNumber     // Catch:{ Exception -> 0x00d2, all -> 0x00cf }
        L_0x0084:
            com.touchus.benchilauncher.LauncherApplication r3 = r14.app     // Catch:{ Exception -> 0x00d2, all -> 0x00cf }
            java.lang.String r3 = r3.phoneNumber     // Catch:{ Exception -> 0x00d2, all -> 0x00cf }
            java.lang.String r5 = com.touchus.publicutils.utils.TimeUtils.getSimpleCallDate()     // Catch:{ Exception -> 0x00d2, all -> 0x00cf }
            r4 = r15
            r0.<init>(r1, r2, r3, r4, r5)     // Catch:{ Exception -> 0x00d2, all -> 0x00cf }
            com.touchus.benchilauncher.LauncherApplication r1 = r14.app     // Catch:{ Exception -> 0x00d2, all -> 0x00cf }
            java.lang.String r1 = r1.phoneNumber     // Catch:{ Exception -> 0x00d2, all -> 0x00cf }
            if (r1 == 0) goto L_0x009a
            r1 = 0
            r11.add(r1, r0)     // Catch:{ Exception -> 0x00d2, all -> 0x00cf }
        L_0x009a:
            java.io.FileOutputStream r10 = new java.io.FileOutputStream     // Catch:{ Exception -> 0x00d2, all -> 0x00cf }
            r10.<init>(r9)     // Catch:{ Exception -> 0x00d2, all -> 0x00cf }
            com.touchus.publicutils.utils.PersonParse.save(r11, r10)     // Catch:{ Exception -> 0x00d2, all -> 0x00cf }
            if (r13 == 0) goto L_0x00cd
            r13.close()     // Catch:{ IOException -> 0x00c9 }
            r12 = r13
        L_0x00a8:
            return
        L_0x00a9:
            com.touchus.benchilauncher.LauncherApplication r2 = r14.app     // Catch:{ Exception -> 0x00d2, all -> 0x00cf }
            java.lang.String r2 = r2.phoneName     // Catch:{ Exception -> 0x00d2, all -> 0x00cf }
            goto L_0x0084
        L_0x00ae:
            r8 = move-exception
        L_0x00af:
            r8.printStackTrace()     // Catch:{ all -> 0x00bd }
            if (r12 == 0) goto L_0x00a8
            r12.close()     // Catch:{ IOException -> 0x00b8 }
            goto L_0x00a8
        L_0x00b8:
            r8 = move-exception
            r8.printStackTrace()
            goto L_0x00a8
        L_0x00bd:
            r1 = move-exception
        L_0x00be:
            if (r12 == 0) goto L_0x00c3
            r12.close()     // Catch:{ IOException -> 0x00c4 }
        L_0x00c3:
            throw r1
        L_0x00c4:
            r8 = move-exception
            r8.printStackTrace()
            goto L_0x00c3
        L_0x00c9:
            r8 = move-exception
            r8.printStackTrace()
        L_0x00cd:
            r12 = r13
            goto L_0x00a8
        L_0x00cf:
            r1 = move-exception
            r12 = r13
            goto L_0x00be
        L_0x00d2:
            r8 = move-exception
            r12 = r13
            goto L_0x00af
        */
        throw new UnsupportedOperationException("Method not decompiled: com.touchus.benchilauncher.service.BluetoothService.setHistoryList(int):void");
    }

    public void CallOut(String phonenum) {
        try {
            if (phonenum.length() < 2 || phonenum.contains(Marker.ANY_MARKER) || phonenum.contains("#")) {
                ToastTool.showBigShortToast(this.mContext, (int) R.string.bluetooth_phonenum_error);
                return;
            }
            currentCallingType = 1413;
            bluetoothStatus = EPhoneStatus.CALLING_OUT;
            this.app.phoneNumber = phonenum;
            FloatSystemCallDialog.getInstance().clearView();
            FloatSystemCallDialog.getInstance().setShowStatus(FloatSystemCallDialog.FloatShowST.OUTCALLING);
            this.bluetooth.call(phonenum);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void exitsource() {
        try {
            iIsCallingInButNotalkingState = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        unrequestAudioFocus();
        FloatSystemCallDialog.getInstance().setCallingPhonenumber("-1");
    }

    /* access modifiers changed from: private */
    public void putDeviceInfo(String name, String addr) {
        if (!TextUtils.isEmpty(name)) {
            SharedPreferences.Editor editor = this.shared_device.edit();
            editor.putString(Contacts.PeopleColumns.NAME, name);
            editor.putString("addr", addr);
            editor.commit();
        }
    }

    /* access modifiers changed from: private */
    public void CreateFile(String fileName) {
        try {
            File file1 = new File(String.valueOf(this.Apppath) + "/" + fileName);
            if (!file1.exists()) {
                file1.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
            logger.debug("create file error : " + e.getMessage());
        }
    }

    public void incVolume() {
        try {
            this.bluetooth.incVolume();
        } catch (Exception e) {
        }
    }

    public void decVolume() {
        try {
            this.bluetooth.decVolume();
        } catch (Exception e) {
        }
    }

    /* access modifiers changed from: private */
    public void CreatePerFile(String address) {
        try {
            File filetxt = new File(String.valueOf(this.Apppath) + "/_Per.txt");
            if (!filetxt.exists()) {
                filetxt.createNewFile();
            }
            BufferedWriter writer = new BufferedWriter(new FileWriter(filetxt));
            writer.write(String.valueOf(address) + "_Per.xml");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void cutdownCurrentCalling() {
        this.lastCloseCallingViewTime = System.currentTimeMillis();
        if (currentCallingType != 1413 && !this.histalkflag && !TextUtils.isEmpty(this.app.phoneNumber)) {
            setHistoryList(1414);
        }
        this.app.phoneNumber = "";
        this.app.phoneName = "";
        this.app.isCalling = false;
        unrequestAudioFocus();
        try {
            this.bluetooth.hangUpThePhone();
            if (this.app.currentDialog4 != null) {
                this.app.currentDialog4.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        sendVoiceAssistantCallEndEvent();
    }

    public void answerCalling() {
        try {
            requestAudioFocus();
            this.bluetooth.answerThePhone();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void enterPairingMode() {
        try {
            this.bluetooth.enterPairingModeSync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void leavePairingMode() {
        try {
            this.bluetooth.leavePairingModeSync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void disconnectCurDevice() {
        try {
            this.bluetooth.disconnectCurrentDevice();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void connectDevice(String id) {
        try {
            this.bluetooth.connectDeviceSync(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeDevice(Context mContext2, String id) {
        try {
            this.bluetooth.removeDevice(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void readPhoneStatus() {
        try {
            this.bluetooth.readPhoneStatusSync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void readDeviceAddr() {
        try {
            this.bluetooth.readDeviceAddr();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void readPairingList() {
        try {
            this.bluetooth.readPairingListSync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void switchDevice() {
        this.iSoundInPhone = !this.iSoundInPhone;
        logger.debug("FloatSystemCallDialog iSoundInPhone=" + this.iSoundInPhone);
        try {
            this.bluetooth.switchDevice(this.iSoundInPhone);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void pressVirutalButton(EVirtualButton button) {
        try {
            this.bluetooth.pressVirutalButton(button);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void downloadPhoneBook() {
        if (this.booklistIndex < 1) {
            this.bookList.clear();
            this.app.sendMessage(BOOK_LIST_START_LOAD, (Bundle) null);
        }
        try {
            this.bluetooth.downloadPhoneBookSync(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void downloadRecordList() {
        try {
            this.bluetooth.downloadPhoneBookSync(5);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cancelDownloadPhoneBook() {
        try {
            this.bluetooth.cancelDownloadPhoneBook();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void tryToDownloadPhoneBook() {
        try {
            this.bluetooth.tryToDownloadPhoneBook();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void playNext() {
        try {
            this.bluetooth.playNext();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void playPrev() {
        try {
            this.bluetooth.playPrev();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void readMediaStatus() {
        try {
            this.bluetooth.readMediaStatus();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void readMediaInfo() {
        try {
            this.bluetooth.readMediaInfo();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void pausePlaySync(boolean iPlay) {
        try {
            this.bluetooth.pausePlaySync(iPlay);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void pauseForce() {
        try {
            this.bluetooth.pauseSync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopMusic() {
        try {
            this.bluetooth.stopPlay();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setBTVolume(int volume) {
        try {
            this.bluetooth.setBTVolume(volume);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopBTMusic() {
        this.blueMusicFocus = false;
        stopMusic();
        this.ipause = false;
        LauncherApplication.iPlayingAuto = false;
        unrequestMusicAudioFocus();
    }

    public void requestMusicAudioFocus() {
        this.app.musicType = 2;
        int flag = this.mAudioManager.requestAudioFocus(this.mBlueMusicFocusListener, 3, 1);
        this.app.btservice.blueMusicFocus = flag == 1;
        logger.debug("bluetooth btservice requestMusicAudioFocus" + flag);
        setBTVolume((int) (SysConst.musicNorVolume * 100.0f));
        LauncherApplication.iPlayingAuto = true;
    }

    public void unrequestMusicAudioFocus() {
        logger.debug("mAudioFocusListener btservice unrequestMusicAudioFocus");
        this.mAudioManager.abandonAudioFocus(this.mBlueMusicFocusListener);
        LauncherApplication launcherApplication = this.app;
        this.app.getClass();
        launcherApplication.setTypeMute(200, true);
    }

    public void setDeviceName(Context mContext2) {
        String imeiString;
        if (TextUtils.isEmpty(UtilTools.getIMEI(this))) {
            imeiString = "000000000000000";
        } else {
            imeiString = UtilTools.getIMEI(this);
        }
        try {
            deviceName = String.valueOf(deviceName) + "_" + imeiString.substring(11);
            this.bluetooth.setDeviceName(deviceName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setBTEnterACC(boolean isAccOff) {
        try {
            this.bluetooth.setBTEnterACC(isAccOff);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void enterSystemFloatCallView() {
        logger.debug("FloatSystemCallDialog >>>>>>enterSystemFloatCallView");
        if (!EcarManager.isEcarCall && !this.app.topIsBlueMainFragment()) {
            FloatSystemCallDialog floatCallDialog = FloatSystemCallDialog.getInstance();
            if (floatCallDialog.isDestory()) {
                floatCallDialog.showFloatCallView(getApplicationContext());
            } else {
                floatCallDialog.show();
            }
            this.app.sendMessage(SysConst.CALL_FLOAT, (Bundle) null);
        }
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void sendBookchanged() {
        Intent intent1 = new Intent();
        intent1.setAction(VoiceAssistantConst.EVENT_ADDRESS_BOOK_CHANGE);
        sendBroadcast(intent1);
    }

    /* access modifiers changed from: private */
    public void sendVoiceAssistantStartEvent() {
        Intent intent = new Intent();
        intent.setAction(VoiceAssistantConst.EVENT_START_VOICE_ASSISTANT_FROM_OTHER_APP);
        intent.putExtra("iNeedEnterIncall", "incall");
        intent.putExtra(Contacts.PeopleColumns.NAME, this.app.phoneName);
        intent.putExtra(Contacts.PhonesColumns.NUMBER, this.app.phoneNumber);
        intent.setFlags(32);
        sendBroadcast(intent);
    }

    /* access modifiers changed from: private */
    public void sendVoiceAssistantInTalkingEvent() {
        Intent intent = new Intent();
        intent.setAction(VoiceAssistantConst.EVENT_START_VOICE_ASSISTANT_FROM_OTHER_APP);
        intent.putExtra("iNeedEnterIncall", "talking");
        intent.setFlags(32);
        sendBroadcast(intent);
    }

    /* access modifiers changed from: private */
    public void sendVoiceAssistantCallEndEvent() {
        Intent intent = new Intent();
        intent.setAction(VoiceAssistantConst.EVENT_START_VOICE_ASSISTANT_FROM_OTHER_APP);
        intent.putExtra("iNeedEnterIncall", "end");
        intent.setFlags(32);
        sendBroadcast(intent);
    }
}
