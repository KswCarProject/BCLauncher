package com.touchus.benchilauncher.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import com.backaudio.android.driver.Mainboard;
import com.touchus.benchilauncher.Launcher;
import com.touchus.benchilauncher.LauncherApplication;
import com.touchus.benchilauncher.R;
import com.touchus.benchilauncher.SysConst;
import com.touchus.benchilauncher.base.BaseFragment;
import com.touchus.benchilauncher.inface.OnItemClickListener;
import com.touchus.benchilauncher.inface.OnItemSelectedListener;
import com.touchus.benchilauncher.views.LoopRotarySwitchView;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

public class FragmentMediaMenu extends BaseFragment implements View.OnClickListener {
    private TextView currentPlayInfoTview;
    String[] currentShowData = null;
    private LauncherApplication mApp;
    Launcher mContext;
    public USBMusicHandler mHandler = new USBMusicHandler(this);
    private ImageView mIvMenu;
    private String mKey = "";
    private LoopRotarySwitchView mLoopRotarySwitchView;
    private View mRootView;
    Map<String, String[]> rotateData = new HashMap();
    String[] texts1;
    String[] texts2;
    String[] texts3;
    String[] texts4;
    private int width;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.mRootView = inflater.inflate(R.layout.fragment_menu11, (ViewGroup) null);
        this.mContext = (Launcher) getActivity();
        this.mApp = (LauncherApplication) this.mContext.getApplication();
        initView();
        initData();
        initRotateData();
        initLoopRotarySwitchView();
        initLinstener();
        if (this.mApp.musicPlayControl != null && LauncherApplication.iPlaying && LauncherApplication.iPlayingAuto) {
            this.mContext.changeRightTo(new FragmentMusicOutUSB());
        }
        if (this.mApp.musicType == 2 && LauncherApplication.iPlayingAuto) {
            this.mContext.changeRightTo(new FragmentMusicLanYa());
        }
        return this.mRootView;
    }

    public void onStart() {
        if (LauncherApplication.menuSelectType == -1) {
            LauncherApplication.menuSelectType = 0;
        }
        if (LauncherApplication.menuSelectType == 0) {
            this.currentShowData = this.texts1;
        } else if (LauncherApplication.menuSelectType == 1) {
            this.currentShowData = this.texts2;
        } else if (LauncherApplication.menuSelectType == 2) {
            this.currentShowData = this.texts3;
        } else if (LauncherApplication.menuSelectType == 3) {
            this.currentShowData = this.texts4;
        }
        this.mLoopRotarySwitchView.setChildViewText(this.currentShowData);
        super.onStart();
    }

    public void onResume() {
        this.mApp.registerHandler(this.mHandler);
        super.onResume();
    }

    public void onPause() {
        this.mApp.unregisterHandler(this.mHandler);
        super.onPause();
    }

    public void onDestroy() {
        super.onDestroy();
    }

    public boolean onBack() {
        return false;
    }

    public void onClick(View v) {
        if (v.getId() == R.id.iv_menu_xia) {
            handleClick();
        }
    }

    /* access modifiers changed from: private */
    public void settingCurrentShowData(int position) {
        if (getActivity() != null) {
            this.mKey = this.currentShowData[position];
            String[] tempData = this.rotateData.get(this.mKey);
            String[] newData = new String[5];
            for (int i = 0; i < tempData.length; i++) {
                if (i < position) {
                    newData[(position - 1) - i] = tempData[(tempData.length - 1) - i];
                } else {
                    newData[i] = tempData[i - position];
                }
            }
            this.currentShowData = newData;
            this.mLoopRotarySwitchView.setChildViewText(newData);
            this.mLoopRotarySwitchView.setTextViewText();
            selectStateChange(this.mKey);
        }
    }

    private void initLinstener() {
        this.mIvMenu.setOnClickListener(this);
        this.mLoopRotarySwitchView.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void selected(int position, View view) {
                FragmentMediaMenu.this.settingCurrentShowData(position);
            }
        });
        this.mLoopRotarySwitchView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(int item, View view) {
                FragmentMediaMenu.this.handleClick();
            }
        });
    }

    private void selectStateChange(String temp) {
        if (temp.equals(getString(R.string.music))) {
            this.mIvMenu.setImageResource(R.drawable.usbmusic_tu);
            LauncherApplication.menuSelectType = 0;
        } else if (temp.equals(getString(R.string.video))) {
            this.mIvMenu.setImageResource(R.drawable.cd);
            LauncherApplication.menuSelectType = 1;
        } else if (temp.equals(getString(R.string.picture))) {
            this.mIvMenu.setImageResource(R.drawable.sd);
            LauncherApplication.menuSelectType = 2;
        } else if (temp.equals(getString(R.string.btmusic))) {
            this.mIvMenu.setImageResource(R.drawable.btmusic_tu);
            LauncherApplication.menuSelectType = 3;
        }
    }

    /* access modifiers changed from: private */
    public void handleClick() {
        if (getActivity() != null) {
            if (this.mKey.equals(getString(R.string.music))) {
                this.mContext.changeRightTo(new FragmentMusicOutUSB());
            } else if (this.mKey.equals(getString(R.string.video))) {
                this.mContext.changeRightTo(new FragmentMusicOutUSB());
            } else if (this.mKey.equals(getString(R.string.picture))) {
                this.mContext.changeRightTo(new FragmentMusicOutUSB());
            } else if (this.mKey.equals(getString(R.string.btmusic))) {
                this.mContext.changeRightTo(new FragmentMusicLanYa());
            }
        }
    }

    private void initLoopRotarySwitchView() {
        this.mLoopRotarySwitchView.setR((float) (this.width / 5));
        this.mLoopRotarySwitchView.setChildViewText(this.currentShowData);
    }

    private void initView() {
        this.mLoopRotarySwitchView = (LoopRotarySwitchView) this.mRootView.findViewById(R.id.mLoopRotarySwitchView);
        this.currentPlayInfoTview = (TextView) this.mRootView.findViewById(R.id.currentPalyInfo);
        this.mIvMenu = (ImageView) this.mRootView.findViewById(R.id.iv_menu_xia);
        DisplayMetrics dm = new DisplayMetrics();
        ((WindowManager) getActivity().getSystemService("window")).getDefaultDisplay().getMetrics(dm);
        this.width = dm.widthPixels;
    }

    private void initData() {
        for (int i = 0; i < 5; i++) {
            this.mLoopRotarySwitchView.addView(LayoutInflater.from(this.mContext).inflate(R.layout.loopview_item_view0, (ViewGroup) null));
        }
    }

    private void initRotateData() {
        this.texts1 = new String[]{this.mContext.getString(R.string.music), this.mContext.getString(R.string.video), this.mContext.getString(R.string.picture), this.mContext.getString(R.string.picture), this.mContext.getString(R.string.btmusic)};
        this.texts2 = new String[]{this.mContext.getString(R.string.video), this.mContext.getString(R.string.picture), this.mContext.getString(R.string.btmusic), this.mContext.getString(R.string.btmusic), this.mContext.getString(R.string.music)};
        this.texts3 = new String[]{this.mContext.getString(R.string.picture), this.mContext.getString(R.string.btmusic), this.mContext.getString(R.string.music), this.mContext.getString(R.string.music), this.mContext.getString(R.string.video)};
        this.texts4 = new String[]{this.mContext.getString(R.string.btmusic), this.mContext.getString(R.string.music), this.mContext.getString(R.string.video), this.mContext.getString(R.string.video), this.mContext.getString(R.string.picture)};
        this.rotateData.put(this.texts1[0], this.texts1);
        this.rotateData.put(this.texts2[0], this.texts2);
        this.rotateData.put(this.texts3[0], this.texts3);
        this.rotateData.put(this.texts4[0], this.texts4);
        this.currentShowData = this.texts1;
    }

    static class USBMusicHandler extends Handler {
        private WeakReference<FragmentMediaMenu> target;

        public USBMusicHandler(FragmentMediaMenu activity) {
            this.target = new WeakReference<>(activity);
        }

        public void handleMessage(Message msg) {
            if (this.target.get() != null) {
                ((FragmentMediaMenu) this.target.get()).handlerMsgUSB(msg);
            }
        }
    }

    public void handlerMsgUSB(Message msg) {
        if (getActivity() != null && msg.what == 6001) {
            byte code = msg.getData().getByte(SysConst.IDRIVER_ENUM);
            if (code == Mainboard.EIdriverEnum.PRESS.getCode()) {
                handleClick();
            } else if (code == Mainboard.EIdriverEnum.TURN_RIGHT.getCode() || code == Mainboard.EIdriverEnum.RIGHT.getCode()) {
                this.mLoopRotarySwitchView.setAutoScrollDirection(LoopRotarySwitchView.AutoScrollDirection.left);
                this.mLoopRotarySwitchView.doRotain();
            } else if (code == Mainboard.EIdriverEnum.TURN_LEFT.getCode() || code == Mainboard.EIdriverEnum.LEFT.getCode()) {
                this.mLoopRotarySwitchView.setAutoScrollDirection(LoopRotarySwitchView.AutoScrollDirection.right);
                this.mLoopRotarySwitchView.doRotain();
            }
        }
    }
}
