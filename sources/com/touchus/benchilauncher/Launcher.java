package com.touchus.benchilauncher;

import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.backaudio.android.driver.Mainboard;
import com.backaudio.android.driver.bluetooth.bc8mpprotocol.EPhoneStatus;
import com.touchus.benchilauncher.MainService;
import com.touchus.benchilauncher.activity.main.ButtomBar;
import com.touchus.benchilauncher.activity.main.StateFrag;
import com.touchus.benchilauncher.activity.main.left.CardoorFrag;
import com.touchus.benchilauncher.activity.main.left.LeftClockFrag;
import com.touchus.benchilauncher.activity.main.left.LightFrag;
import com.touchus.benchilauncher.activity.main.left.NaviInfoFrag;
import com.touchus.benchilauncher.activity.main.left.RunningFrag;
import com.touchus.benchilauncher.activity.main.left.WeatherFrag;
import com.touchus.benchilauncher.activity.main.right.Menu.MenuFragment;
import com.touchus.benchilauncher.activity.main.right.call.CallPhoneFragment;
import com.touchus.benchilauncher.base.BaseFragment;
import com.touchus.benchilauncher.fragment.FragmentMediaMenu;
import com.touchus.benchilauncher.fragment.FragmentVideoPlay;
import com.touchus.benchilauncher.fragment.FragmentYingyong;
import com.touchus.benchilauncher.fragment.YiBiaoFragment;
import com.touchus.benchilauncher.inface.IDoorStateParent;
import com.touchus.benchilauncher.service.BluetoothService;
import com.touchus.benchilauncher.utils.DialogUtil;
import com.touchus.benchilauncher.utils.EcarManager;
import com.touchus.benchilauncher.views.ButtomSlide;
import com.touchus.benchilauncher.views.LeftStateSlide;
import com.touchus.benchilauncher.views.SettingDialog;
import com.touchus.benchilauncher.views.SoundDialog;
import com.touchus.publicutils.sysconst.PubSysConst;
import com.touchus.publicutils.utils.APPSettings;
import com.touchus.publicutils.utils.UtilTools;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class Launcher extends FragmentActivity implements IDoorStateParent {
    /* access modifiers changed from: private */
    public LauncherApplication app;
    private ArrayList<Fragment> backList = new ArrayList<>();
    private final ServiceConnection blueconn = new ServiceConnection() {
        public void onServiceConnected(ComponentName name, IBinder binder) {
            Launcher.this.app.btservice = ((BluetoothService.PlayerBinder) binder).getService();
            Launcher.this.initBluetooth();
        }

        public void onServiceDisconnected(ComponentName name) {
            Launcher.this.app.btservice = null;
        }
    };
    private ServiceConnection conn = new ServiceConnection() {
        public void onServiceConnected(ComponentName name, IBinder binder) {
            Launcher.this.app.service = ((MainService.MainBinder) binder).getService();
        }

        public void onServiceDisconnected(ComponentName name) {
            Launcher.this.app.service = null;
        }
    };
    public long lastChangeTimne = -1;
    public byte lastChangeView = Mainboard.EControlSource.MENU.getCode();
    ButtomBar mButtomFrag;
    public ButtomSlide mButtomSlide;
    private Mhandler mHandler = new Mhandler(this);
    public boolean mIsButtomShowed = true;
    public boolean mIsTopShowed = true;
    public LeftStateSlide mLeftStateSlide;
    public boolean mMenuShow = true;
    StateFrag mStateFra;
    public ButtomSlide mTopSlide;
    public FragmentManager manager = getSupportFragmentManager();
    public MenuFragment menuFragment;
    public FrameLayout playVideoLayout;
    private FrameLayout rightLayout;
    private Fragment yibiaoFragment = new YiBiaoFragment();
    private LinearLayout yibiaoLayout;

    private void initObject() {
        if (Build.MODEL.contains("c200_lsx")) {
            this.menuFragment = MenuFragment.newInstance(7);
        } else {
            this.menuFragment = MenuFragment.newInstance(5);
        }
        this.mStateFra = new StateFrag();
        this.mButtomFrag = new ButtomBar();
        this.mButtomSlide = (ButtomSlide) findViewById(R.id.buttom_slide);
        this.mTopSlide = (ButtomSlide) findViewById(R.id.top_slide);
        this.mLeftStateSlide = (LeftStateSlide) findViewById(R.id.left_slide);
        this.rightLayout = (FrameLayout) findViewById(R.id.right);
        this.yibiaoLayout = (LinearLayout) findViewById(R.id.main_yibiao);
        this.playVideoLayout = (FrameLayout) findViewById(R.id.quanpin);
        this.yibiaoLayout.setVisibility(4);
        this.app.iIsYibiaoShowing = false;
        this.playVideoLayout.setVisibility(4);
    }

    private void inflate() {
        if (SysConst.LANGUAGE == 0) {
            LauncherApplication.pageCount = 4;
        } else {
            LauncherApplication.pageCount = 3;
        }
        changeFragment(R.id.main_yibiao, this.yibiaoFragment);
        showFragment(false, this.yibiaoFragment);
        changeFragment(R.id.running, new RunningFrag());
        if (SysConst.LANGUAGE == 0) {
            changeFragment(R.id.weather, new WeatherFrag());
            changeFragment(R.id.navi, new NaviInfoFrag());
        }
        changeFragment(R.id.clockLayout, new LeftClockFrag());
        changeFragment(R.id.lightLayout, new LightFrag());
        changeFragment(R.id.cardoor, new CardoorFrag());
        changeFragment(R.id.top, this.mStateFra);
        changeFragment(R.id.buttom_container, this.mButtomFrag);
        if (Build.MODEL.contains("c200_lsx")) {
            changeFragment(R.id.quanpin, this.menuFragment);
            this.rightLayout.setVisibility(4);
            this.mLeftStateSlide.setVisibility(4);
            this.playVideoLayout.setVisibility(0);
        } else {
            changeFragment(R.id.right, this.menuFragment);
        }
        hideButtom();
    }

    public void changeRightTo(Fragment to) {
        if (this.yibiaoLayout.getVisibility() == 0) {
            this.rightLayout.setVisibility(0);
            this.mLeftStateSlide.setVisibility(0);
            this.yibiaoLayout.setVisibility(4);
            showFragment(false, this.yibiaoFragment);
            this.app.iIsYibiaoShowing = false;
        }
        if (this.playVideoLayout.getVisibility() == 0) {
            removePlayVideo();
            this.rightLayout.setVisibility(0);
            this.mLeftStateSlide.setVisibility(0);
            this.playVideoLayout.setVisibility(8);
        }
        if (!(to instanceof MenuFragment)) {
            if (to instanceof CallPhoneFragment) {
                if (this.backList != null) {
                    this.backList.clear();
                }
                if (getCurrentFragment() instanceof CallPhoneFragment) {
                    return;
                }
            }
            if (to instanceof FragmentMediaMenu) {
                if (this.backList != null) {
                    this.backList.clear();
                }
                if (getCurrentFragment() instanceof FragmentMediaMenu) {
                    return;
                }
            }
            this.backList.add(to);
            showButtom();
        } else {
            this.backList.clear();
            hideButtom();
        }
        if (!Build.MODEL.contains("c200_lsx") || !(to instanceof MenuFragment)) {
            changeFragment(R.id.right, to);
            return;
        }
        changeFragment(R.id.quanpin, to);
        this.rightLayout.setVisibility(4);
        this.mLeftStateSlide.setVisibility(4);
        this.playVideoLayout.setVisibility(0);
    }

    private void changeRightLayoutFragmentByHistory(Fragment to) {
        changeFragment(R.id.right, to);
    }

    private void removePlayVideo() {
        try {
            showButtom();
            showTop();
            this.app.iIsVideoShow = false;
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            Fragment showFrag = getSupportFragmentManager().findFragmentById(R.id.quanpin);
            if (showFrag != null) {
                ft.remove(showFrag);
                ft.commit();
            }
            this.app.sendMessage(SysConst.LISTITEM_UPDATE, (Bundle) null);
        } catch (Exception e) {
        }
    }

    public Fragment getCurrentFragment() {
        if (this.playVideoLayout.getVisibility() != 0 || this.app.iIsVideoShow) {
            return getSupportFragmentManager().findFragmentById(R.id.right);
        }
        return getSupportFragmentManager().findFragmentById(R.id.quanpin);
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("", "NAVIGATION_BAR----------------onCreate");
        requestWindowFeature(1);
        if (SysConst.LANGUAGE == 0) {
            setContentView(R.layout.activity_main);
        } else {
            setContentView(R.layout.activity_main_en);
        }
        this.app = (LauncherApplication) getApplication();
        this.app.registerHandler(this.mHandler);
        this.app.launcherHandler = this.mHandler;
        Intent intent = new Intent(this, MainService.class);
        startService(intent);
        if (this.app.service == null) {
            bindService(intent, this.conn, 1);
        }
        startBluetoothService();
        initObject();
        inflate();
    }

    /* access modifiers changed from: protected */
    public void onStart() {
        Log.e("", "NAVIGATION_BAR----------------onStart== ");
        super.onStart();
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        Log.e("", "NAVIGATION_BAR----------------onResume");
        if (this.app.service != null) {
            this.app.service.closeNaviThread();
        }
        this.app.sendShowNavigationBarEvent(false);
        this.app.iCurrentInLauncher = true;
        this.mStateFra.checkUsb3();
        super.onResume();
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        this.app.iCurrentInLauncher = false;
        if (this.app.service != null) {
            this.app.service.openNaviThread();
        }
        super.onPause();
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        this.app.dismissDialog();
        super.onStop();
    }

    public void onBackPressed() {
    }

    /* access modifiers changed from: protected */
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        this.app.unregisterHandler(this.mHandler);
        try {
            unbindService(this.conn);
            unbindService(this.blueconn);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void verifyScreen(final int x, final int y) {
        this.mHandler.postDelayed(new Runnable() {
            public void run() {
                Log.e("luancher", "verifyScreen handleEvent x = " + x + ",LauncherApplication.X = " + LauncherApplication.X + ",x = " + x + ",LauncherApplication.Y = " + LauncherApplication.Y);
                if (LauncherApplication.X + x > 1281 || LauncherApplication.X + x < 1279) {
                    UtilTools.echoFile("0", PubSysConst.GT9XX_INT_LR);
                } else {
                    UtilTools.echoFile("1", PubSysConst.GT9XX_INT_LR);
                }
                if (LauncherApplication.Y + y > 481 || LauncherApplication.Y + y < 479) {
                    UtilTools.echoFile("0", PubSysConst.GT9XX_INT_UPDN);
                } else {
                    UtilTools.echoFile("1", PubSysConst.GT9XX_INT_UPDN);
                }
            }
        }, 500);
    }

    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (LauncherApplication.iAccOff) {
            return true;
        }
        if (this.app.service == null || !this.app.service.iIsInAndroid || !this.app.iIsScreenClose.get()) {
            return super.dispatchTouchEvent(ev);
        }
        this.app.closeOrWakeupScreen(false);
        return true;
    }

    public void changeFragment(int rootID, Fragment to) {
        if (!isDestroyed()) {
            FragmentTransaction transaction = this.manager.beginTransaction();
            transaction.replace(rootID, to);
            transaction.commitAllowingStateLoss();
        }
    }

    public void showFragment(Boolean ishow, Fragment fragment) {
        if (!isDestroyed()) {
            FragmentTransaction transaction = this.manager.beginTransaction();
            if (ishow.booleanValue()) {
                transaction.show(fragment);
            } else {
                transaction.hide(fragment);
            }
            transaction.commitAllowingStateLoss();
        }
    }

    public void goTo(int index) {
        if (!UtilTools.isFastDoubleClick()) {
            if (SysConst.LANGUAGE == 0) {
                if (Build.MODEL.equals("c200") || Build.MODEL.equals("c200_lsx") || Build.MODEL.equals("benz")) {
                    changeIndex(index);
                } else if (Build.MODEL.equals("c200_ks")) {
                    changeIndex2(index);
                } else {
                    changeIndex1(index);
                }
            } else if (Build.MODEL.contains("c200_en") || Build.MODEL.contains("c200_lsx_en")) {
                changeIndex_en(index);
            } else {
                changeIndex1_en(index);
            }
        }
    }

    public void changeIndex2(int index) {
        switch (index) {
            case 0:
                this.app.startNavi();
                break;
            case 1:
                this.app.interOriginalView();
                break;
            case 2:
                changeRightTo(new FragmentMediaMenu());
                break;
            case 3:
                changeRightTo(new CallPhoneFragment());
                break;
            case 4:
                EcarManager.getInstance(this).startEcar();
                break;
            case 5:
                changeCenterTo();
                LauncherApplication.shutDoorNeedShowYibiao = true;
                break;
            case 6:
                if (!APPSettings.isAvalbe(this, "net.easyconn")) {
                    if (APPSettings.isAvalbe(this, "com.tima.carnet.vt")) {
                        this.app.startTimaService();
                        this.app.startAppByPkg("com.tima.carnet.vt");
                        break;
                    }
                } else {
                    this.app.startAppByPkg("net.easyconn");
                    break;
                }
                break;
            case 7:
                this.app.sendStartVoiceAssistantEvent();
                break;
            case 8:
                changeRightTo(new FragmentYingyong());
                break;
            case 9:
                final SettingDialog.Builder builder = new SettingDialog.Builder(this);
                SettingDialog SettingDialog = builder.create();
                this.app.currentDialog1 = SettingDialog;
                DialogUtil.setDialogLocation(SettingDialog, 100, 0);
                SettingDialog.show();
                SettingDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    public void onDismiss(DialogInterface dialog) {
                        builder.unRigisterHandler();
                        Launcher.this.app.currentDialog1 = null;
                    }
                });
                break;
        }
        if (index > -1 || index < 11) {
            MenuFragment.mCurIndex = index;
        }
    }

    public void changeIndex(int index) {
        switch (index) {
            case 0:
                this.app.startNavi();
                break;
            case 1:
                this.app.interOriginalView();
                break;
            case 2:
                changeRightTo(new FragmentMediaMenu());
                break;
            case 3:
                changeRightTo(new CallPhoneFragment());
                break;
            case 4:
                this.app.sendStartVoiceAssistantEvent();
                break;
            case 5:
                changeCenterTo();
                LauncherApplication.shutDoorNeedShowYibiao = true;
                break;
            case 6:
                if (!APPSettings.isAvalbe(this, "net.easyconn")) {
                    if (APPSettings.isAvalbe(this, "com.tima.carnet.vt")) {
                        this.app.startTimaService();
                        this.app.startAppByPkg("com.tima.carnet.vt");
                        break;
                    }
                } else {
                    this.app.startAppByPkg("net.easyconn");
                    break;
                }
                break;
            case 7:
                changeRightTo(new FragmentYingyong());
                break;
            case 8:
                final SettingDialog.Builder builder = new SettingDialog.Builder(this);
                SettingDialog SettingDialog = builder.create();
                this.app.currentDialog1 = SettingDialog;
                DialogUtil.setDialogLocation(SettingDialog, 100, 0);
                SettingDialog.show();
                SettingDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    public void onDismiss(DialogInterface dialog) {
                        builder.unRigisterHandler();
                        Launcher.this.app.currentDialog1 = null;
                    }
                });
                break;
            case 9:
                LauncherApplication.getInstance().closeOrWakeupScreen(true);
                break;
        }
        if (index > -1 || index < 11) {
            MenuFragment.mCurIndex = index;
        }
    }

    public void changeIndex1(int index) {
        switch (index) {
            case 0:
                this.app.startNavi();
                break;
            case 1:
                this.app.interOriginalView();
                break;
            case 2:
                changeRightTo(new FragmentMediaMenu());
                break;
            case 3:
                changeRightTo(new CallPhoneFragment());
                break;
            case 4:
                changeCenterTo();
                LauncherApplication.shutDoorNeedShowYibiao = true;
                break;
            case 5:
                this.app.sendStartVoiceAssistantEvent();
                break;
            case 6:
                if (!Build.MODEL.equals("c200_hy") && !Build.MODEL.equals("c200_psr") && !Build.MODEL.equals("c200_jly") && !Build.MODEL.equals("benz_hy")) {
                    if (!APPSettings.isAvalbe(this, "net.easyconn")) {
                        if (APPSettings.isAvalbe(this, "com.tima.carnet.vt")) {
                            this.app.startTimaService();
                            this.app.startAppByPkg("com.tima.carnet.vt");
                            break;
                        }
                    } else {
                        this.app.startAppByPkg("net.easyconn");
                        break;
                    }
                } else {
                    this.app.service.iIsInRecorder = true;
                    this.app.service.createNoTouchScreens();
                    Mainboard.getInstance().showCarLayer(Mainboard.ECarLayer.RECORDER);
                    return;
                }
                break;
            case 7:
                changeRightTo(new FragmentYingyong());
                break;
            case 8:
                final SettingDialog.Builder builder = new SettingDialog.Builder(this);
                SettingDialog SettingDialog = builder.create();
                this.app.currentDialog1 = SettingDialog;
                DialogUtil.setDialogLocation(SettingDialog, 100, 0);
                SettingDialog.show();
                SettingDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    public void onDismiss(DialogInterface dialog) {
                        builder.unRigisterHandler();
                        Launcher.this.app.currentDialog1 = null;
                    }
                });
                break;
            case 9:
                LauncherApplication.getInstance().closeOrWakeupScreen(true);
                break;
        }
        if (index > -1 || index < 11) {
            MenuFragment.mCurIndex = index;
        }
    }

    public void changeIndex_en(int index) {
        switch (index) {
            case 0:
                this.app.startNavi();
                break;
            case 1:
                this.app.interOriginalView();
                break;
            case 2:
                changeRightTo(new FragmentMediaMenu());
                break;
            case 3:
                changeRightTo(new CallPhoneFragment());
                break;
            case 4:
                changeCenterTo();
                LauncherApplication.shutDoorNeedShowYibiao = true;
                break;
            case 5:
                if (!APPSettings.isAvalbe(this, "net.easyconn")) {
                    if (APPSettings.isAvalbe(this, "com.tima.carnet.vt")) {
                        this.app.startTimaService();
                        this.app.startAppByPkg("com.tima.carnet.vt");
                        break;
                    }
                } else {
                    this.app.startAppByPkg("net.easyconn");
                    break;
                }
                break;
            case 6:
                if (!Build.MODEL.equals("c200_en")) {
                    this.app.service.iIsInRecorder = true;
                    this.app.service.createNoTouchScreens();
                    Mainboard.getInstance().showCarLayer(Mainboard.ECarLayer.RECORDER);
                    break;
                } else {
                    this.app.startAppByPkg("com.android.chrome");
                    break;
                }
            case 7:
                changeRightTo(new FragmentYingyong());
                break;
            case 8:
                final SettingDialog.Builder builder = new SettingDialog.Builder(this);
                SettingDialog SettingDialog = builder.create();
                this.app.currentDialog1 = SettingDialog;
                DialogUtil.setDialogLocation(SettingDialog, 100, 0);
                SettingDialog.show();
                SettingDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    public void onDismiss(DialogInterface dialog) {
                        builder.unRigisterHandler();
                        Launcher.this.app.currentDialog1 = null;
                    }
                });
                break;
            case 9:
                LauncherApplication.getInstance().closeOrWakeupScreen(true);
                break;
        }
        if (index > -1 || index < 11) {
            MenuFragment.mCurIndex = index;
        }
    }

    public void changeIndex1_en(int index) {
        switch (index) {
            case 0:
                this.app.startNavi();
                break;
            case 1:
                this.app.interOriginalView();
                break;
            case 2:
                changeRightTo(new FragmentMediaMenu());
                break;
            case 3:
                changeRightTo(new CallPhoneFragment());
                break;
            case 4:
                changeCenterTo();
                LauncherApplication.shutDoorNeedShowYibiao = true;
                break;
            case 5:
                if (!Build.MODEL.contains("c200_hy")) {
                    if (!APPSettings.isAvalbe(this, "net.easyconn")) {
                        if (APPSettings.isAvalbe(this, "com.tima.carnet.vt")) {
                            this.app.startTimaService();
                            this.app.startAppByPkg("com.tima.carnet.vt");
                            break;
                        }
                    } else {
                        this.app.startAppByPkg("net.easyconn");
                        break;
                    }
                } else {
                    this.app.startAppByPkg("com.android.chrome");
                    return;
                }
                break;
            case 6:
                this.app.service.iIsInRecorder = true;
                this.app.service.createNoTouchScreens();
                Mainboard.getInstance().showCarLayer(Mainboard.ECarLayer.RECORDER);
                break;
            case 7:
                changeRightTo(new FragmentYingyong());
                break;
            case 8:
                final SettingDialog.Builder builder = new SettingDialog.Builder(this);
                SettingDialog SettingDialog = builder.create();
                this.app.currentDialog1 = SettingDialog;
                DialogUtil.setDialogLocation(SettingDialog, 100, 0);
                SettingDialog.show();
                SettingDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    public void onDismiss(DialogInterface dialog) {
                        builder.unRigisterHandler();
                        Launcher.this.app.currentDialog1 = null;
                    }
                });
                break;
            case 9:
                LauncherApplication.getInstance().closeOrWakeupScreen(true);
                break;
        }
        if (index > -1 || index < 11) {
            MenuFragment.mCurIndex = index;
        }
    }

    public void handHomeAction() {
        this.app.dismissDialog();
        showTop();
        this.app.sendHideOrShowLocateViewEvent(false);
        LauncherApplication.shutDoorNeedShowYibiao = false;
        Fragment fragment = getCurrentFragment();
        this.rightLayout.setVisibility(0);
        this.mLeftStateSlide.setVisibility(0);
        if (this.yibiaoLayout.getVisibility() == 0) {
            this.yibiaoLayout.setVisibility(4);
            showFragment(false, this.yibiaoFragment);
            this.app.iIsYibiaoShowing = false;
        }
        if (this.playVideoLayout.getVisibility() == 0) {
            removePlayVideo();
            this.playVideoLayout.setVisibility(4);
        }
        if (!(fragment instanceof MenuFragment) || MenuFragment.mCurIndex == 0) {
            this.backList.clear();
            if (Build.MODEL.contains("c200_lsx")) {
                changeFragment(R.id.quanpin, this.menuFragment);
                this.rightLayout.setVisibility(4);
                this.mLeftStateSlide.setVisibility(4);
                this.playVideoLayout.setVisibility(0);
            } else {
                changeFragment(R.id.right, this.menuFragment);
            }
        } else {
            this.menuFragment.selectMenu(0);
            if (Build.MODEL.contains("c200_lsx")) {
                this.rightLayout.setVisibility(4);
                this.mLeftStateSlide.setVisibility(4);
                this.playVideoLayout.setVisibility(0);
            }
        }
        hideButtom();
    }

    public void handleBackAction() {
        showTop();
        LauncherApplication.shutDoorNeedShowYibiao = false;
        Fragment fragment = getCurrentFragment();
        if (!(fragment instanceof MenuFragment) && this.backList.size() == 0) {
            changeRightTo(this.menuFragment);
            hideButtom();
        } else if (this.yibiaoLayout.getVisibility() == 0) {
            this.rightLayout.setVisibility(0);
            this.mLeftStateSlide.setVisibility(0);
            this.yibiaoLayout.setVisibility(4);
            showFragment(false, this.yibiaoFragment);
            this.app.iIsYibiaoShowing = false;
            hideButtom();
        } else if (this.playVideoLayout.getVisibility() == 0) {
            removePlayVideo();
            this.rightLayout.setVisibility(0);
            this.mLeftStateSlide.setVisibility(0);
            this.playVideoLayout.setVisibility(8);
        } else if ((fragment instanceof BaseFragment) && !((BaseFragment) fragment).onBack()) {
            if (this.backList.size() > 1) {
                this.backList.remove(this.backList.size() - 1);
                Fragment needto = this.backList.get(this.backList.size() - 1);
                if (needto instanceof CallPhoneFragment) {
                    needto = new CallPhoneFragment();
                }
                changeRightLayoutFragmentByHistory(needto);
                return;
            }
            this.backList.clear();
            changeRightTo(this.menuFragment);
            hideButtom();
        }
    }

    public void changeCenterTo() {
        this.yibiaoLayout.setVisibility(0);
        showFragment(true, this.yibiaoFragment);
        this.app.iIsYibiaoShowing = true;
        showButtom();
        this.rightLayout.setVisibility(4);
        this.mLeftStateSlide.setVisibility(4);
        this.playVideoLayout.setVisibility(8);
    }

    public void changeToPlayVideo(FragmentVideoPlay fragmentShow) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.quanpin, fragmentShow);
        ft.commit();
        this.yibiaoLayout.setVisibility(4);
        showFragment(false, this.yibiaoFragment);
        this.app.iIsYibiaoShowing = false;
        this.app.iIsVideoShow = true;
        this.rightLayout.setVisibility(4);
        this.playVideoLayout.setVisibility(0);
    }

    public void showNaviView() {
        if (SysConst.LANGUAGE == 0) {
            this.mLeftStateSlide.scrollTo(1900, 0);
        }
    }

    public void showCardoor() {
        if (this.yibiaoLayout.getVisibility() != 0) {
            this.mLeftStateSlide.iHandleTouchEvent = false;
            this.mLeftStateSlide.scrollTo(LeftStateSlide.mChildWith * 3, 0);
            if (this.yibiaoLayout.getVisibility() == 0) {
                this.yibiaoLayout.setVisibility(8);
                showFragment(false, this.yibiaoFragment);
                this.rightLayout.setVisibility(0);
                this.mLeftStateSlide.setVisibility(0);
                LauncherApplication.shutDoorNeedShowYibiao = true;
                this.app.iIsYibiaoShowing = false;
            }
        }
    }

    public void resetLeftLayoutToRightState() {
        this.mLeftStateSlide.iHandleTouchEvent = true;
        this.mLeftStateSlide.changeToCurrentView();
        if (LauncherApplication.shutDoorNeedShowYibiao) {
            this.yibiaoLayout.setVisibility(0);
            showFragment(true, this.yibiaoFragment);
            this.mLeftStateSlide.setVisibility(8);
            this.rightLayout.setVisibility(8);
            this.app.iIsYibiaoShowing = true;
        }
    }

    public void showTop() {
        if (!this.mIsTopShowed) {
            this.mIsTopShowed = true;
            this.mTopSlide.smoothScrollYTo(0, 500);
        }
    }

    public void hideTop() {
        if (this.mIsTopShowed) {
            this.mIsTopShowed = false;
            this.mTopSlide.smoothScrollYTo(40, 300);
        }
    }

    public void showButtom() {
        if (!this.mIsButtomShowed) {
            this.mIsButtomShowed = true;
            this.mButtomSlide.smoothScrollYTo(0, 300);
        }
    }

    public void hideButtom() {
        if (this.mIsButtomShowed) {
            this.mIsButtomShowed = false;
            this.mButtomSlide.smoothScrollYTo(-60, 500);
        }
    }

    private void setLeftClockTime(Bundle bundle) {
        Fragment temp = getSupportFragmentManager().findFragmentById(R.id.clockLayout);
        if (temp instanceof LeftClockFrag) {
            ((LeftClockFrag) temp).updateCurrentTime();
        }
    }

    private void startBluetoothService() {
        if (this.app.btservice == null) {
            Intent intent = new Intent(this, BluetoothService.class);
            startService(intent);
            bindService(intent, this.blueconn, 1);
            return;
        }
        initBluetooth();
    }

    class InitBluetoothThread implements Runnable {
        InitBluetoothThread() {
        }

        public void run() {
            Launcher.this.app.btservice.readPairingList();
            Launcher.this.app.btservice.readPhoneStatus();
            Launcher.this.app.btservice.readDeviceAddr();
        }
    }

    /* access modifiers changed from: private */
    public void initBluetooth() {
        try {
            new Thread(new InitBluetoothThread()).start();
            this.app.btservice.accFlag = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void changeViewFromIdriverCar(Bundle bundle) {
        byte code = bundle.getByte(SysConst.IDRIVER_ENUM);
        if (code == Mainboard.EIdriverEnum.HOME.getCode() || code == Mainboard.EIdriverEnum.STAR_BTN.getCode()) {
            this.app.dismissDialog();
        }
        if (this.lastChangeView != code || System.currentTimeMillis() - this.lastChangeTimne >= 2000) {
            this.lastChangeView = code;
            this.lastChangeTimne = System.currentTimeMillis();
            if (code == Mainboard.EIdriverEnum.BT.getCode()) {
                if (!(getCurrentFragment() instanceof CallPhoneFragment) || !this.app.isCallDialog) {
                    changeRightTo(new CallPhoneFragment());
                    this.app.dismissDialog();
                }
            } else if (code == Mainboard.EIdriverEnum.PICK_UP.getCode()) {
                if (BluetoothService.bluetoothStatus != EPhoneStatus.UNCONNECT && !this.app.isCallDialog) {
                    changeRightTo(new CallPhoneFragment());
                    this.app.dismissDialog();
                }
            } else if (code == Mainboard.EIdriverEnum.HOME.getCode()) {
                handHomeAction();
            } else if (code == Mainboard.EIdriverEnum.NAVI.getCode()) {
                this.app.startNavi();
            }
        }
    }

    /* access modifiers changed from: private */
    public void handlerMsg(Message msg) {
        if (!isDestroyed()) {
            Log.e("msg.what", "::" + msg.what);
            if (msg.what == 6001) {
                changeViewFromIdriverCar(msg.getData());
            } else if (msg.what == 6002) {
                StateFrag stateFrag = (StateFrag) getSupportFragmentManager().findFragmentById(R.id.top);
                if (stateFrag != null) {
                    stateFrag.setBt();
                }
            } else if (msg.what == 1013) {
                if (!(getCurrentFragment() instanceof CallPhoneFragment)) {
                    changeRightTo(new CallPhoneFragment());
                }
            } else if (msg.what == 1009) {
                setLeftClockTime(msg.getData());
            } else if (msg.what == 1121 || msg.what == 1131) {
                this.app.mHomeAndBackEnable = true;
                handleBackAction();
                Toast.makeText(this, getString(R.string.msg_upgrade_finish), 0).show();
            } else if (msg.what == 1024) {
                handHomeAction();
            } else if (msg.what == 1030) {
                handHomeAction();
                this.menuFragment.selectMenu(1);
            } else if (msg.what == 1036) {
                UtilTools.sendKeyeventToSystem(3);
                goTo(((Integer) msg.obj).intValue());
            } else if (msg.what == 1040) {
                artSound(1);
            } else if (msg.what == 1046) {
                showNaviView();
            }
        }
    }

    private void artSound(int type) {
        if (this.app.currentDialog4 == null) {
            final SoundDialog.Builder builder = new SoundDialog.Builder(this);
            SoundDialog SettingDialog = builder.create(type);
            DialogUtil.setDialogLocation(SettingDialog, 0, -180);
            SettingDialog.show();
            this.app.currentDialog4 = SettingDialog;
            SettingDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                public void onDismiss(DialogInterface dialog) {
                    builder.unregisterHandlerr();
                    Launcher.this.app.currentDialog4 = null;
                }
            });
        }
    }

    static class Mhandler extends Handler {
        private WeakReference<Launcher> target;

        public Mhandler(Launcher activity) {
            this.target = new WeakReference<>(activity);
        }

        public void handleMessage(Message msg) {
            if (this.target.get() != null) {
                ((Launcher) this.target.get()).handlerMsg(msg);
            }
        }
    }
}
