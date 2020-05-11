package com.touchus.benchilauncher.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.backaudio.android.driver.Mainboard;
import com.touchus.benchilauncher.Launcher;
import com.touchus.benchilauncher.LauncherApplication;
import com.touchus.benchilauncher.R;
import com.touchus.benchilauncher.SysConst;
import com.touchus.benchilauncher.base.BaseFragment;
import com.touchus.benchilauncher.service.BluetoothService;
import com.touchus.benchilauncher.views.MyTextView;
import java.lang.ref.WeakReference;

public class FragmentMusicLanYa extends BaseFragment {
    private ImageView[] arr = new ImageView[3];
    /* access modifiers changed from: private */
    public int count = 0;
    /* access modifiers changed from: private */
    public boolean isNoXuanZhuan = true;
    /* access modifiers changed from: private */
    public boolean isPlaying = true;
    /* access modifiers changed from: private */
    public LauncherApplication mApp;
    /* access modifiers changed from: private */
    public ImageView mBofang;
    private Launcher mContext;
    private byte mIDRIVERENUM;
    private TextView mMTitle;
    public MusicLanyaHandler mMusicHandler = new MusicLanyaHandler(this);
    private View mRootView;
    /* access modifiers changed from: private */
    public ImageView mShang;
    /* access modifiers changed from: private */
    public ImageView mXiayiqu;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.mContext = (Launcher) getActivity();
        this.mRootView = inflater.inflate(R.layout.fragment_music_lanya, (ViewGroup) null);
        this.mContext = (Launcher) getActivity();
        this.mApp = (LauncherApplication) this.mContext.getApplication();
        initView();
        initListener();
        return this.mRootView;
    }

    public void onStart() {
        this.mApp.btservice.requestMusicAudioFocus();
        this.mApp.btservice.readMediaStatus();
        this.mApp.registerHandler(this.mMusicHandler);
        settingPlayName();
        if (LauncherApplication.isBlueConnectState && BluetoothService.mediaPlayState == 1418) {
            this.mApp.service.btMusicConnect();
        }
        super.onStart();
    }

    public void onStop() {
        this.mApp.unregisterHandler(this.mMusicHandler);
        super.onStop();
    }

    private void settingPlayName() {
        if (!LauncherApplication.isBlueConnectState) {
            this.mMTitle.setText(this.mContext.getString(R.string.NOSOURCElanya));
            this.mShang.setClickable(false);
            this.mBofang.setClickable(false);
            this.mXiayiqu.setClickable(false);
            return;
        }
        this.mShang.setClickable(true);
        this.mBofang.setClickable(true);
        this.mXiayiqu.setClickable(true);
        if (BluetoothService.mediaPlayState == 1) {
            this.mMTitle.setText(this.mContext.getString(R.string.bluetooth_music_noplay));
        } else if (BluetoothService.mediaPlayState == 1418) {
            if (!TextUtils.isEmpty(this.mApp.btservice.music)) {
                this.mMTitle.setText(this.mApp.btservice.music);
            }
            this.isPlaying = true;
            this.mApp.btservice.ipause = false;
            if (this.count == 1) {
                this.mBofang.setImageResource(R.drawable.anniu_zanting_h);
            } else {
                this.mBofang.setImageResource(R.drawable.anniu_zanting_n);
            }
        } else if (BluetoothService.mediaPlayState == 1419) {
            this.mMTitle.setText(this.mContext.getString(R.string.PAUSE_));
            this.isPlaying = false;
            if (this.count == 1) {
                this.mBofang.setImageResource(R.drawable.anniu_bofang_h);
            } else {
                this.mBofang.setImageResource(R.drawable.anniu_bofang_n);
            }
        } else if (BluetoothService.mediaPlayState == 1420) {
            this.mMTitle.setText(this.mContext.getString(R.string.bluetooth_music_noplay));
            this.isPlaying = false;
            if (this.count == 1) {
                this.mBofang.setImageResource(R.drawable.anniu_bofang_h);
            } else {
                this.mBofang.setImageResource(R.drawable.anniu_bofang_n);
            }
        }
    }

    private void initListener() {
        this.mXiayiqu.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FragmentMusicLanYa.this.isNoXuanZhuan = true;
                FragmentMusicLanYa.this.count = 2;
                FragmentMusicLanYa.this.seclectTrue(2);
                FragmentMusicLanYa.this.setImageSeclect(2);
                FragmentMusicLanYa.this.mApp.btservice.playNext();
            }
        });
        this.mShang.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FragmentMusicLanYa.this.isNoXuanZhuan = true;
                FragmentMusicLanYa.this.count = 0;
                FragmentMusicLanYa.this.seclectTrue(0);
                FragmentMusicLanYa.this.setImageSeclect(0);
                FragmentMusicLanYa.this.mApp.btservice.playPrev();
            }
        });
        this.mBofang.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                boolean z;
                FragmentMusicLanYa.this.count = 1;
                FragmentMusicLanYa.this.seclectTrue(1);
                if (!FragmentMusicLanYa.this.isPlaying) {
                    FragmentMusicLanYa.this.mBofang.setImageResource(R.drawable.anniu_zanting_h);
                    FragmentMusicLanYa.this.isPlaying = !FragmentMusicLanYa.this.isPlaying;
                    FragmentMusicLanYa.this.mApp.btservice.requestMusicAudioFocus();
                    FragmentMusicLanYa.this.mApp.btservice.pausePlaySync(true);
                    FragmentMusicLanYa.this.mApp.btservice.ipause = false;
                    return;
                }
                FragmentMusicLanYa.this.mBofang.setImageResource(R.drawable.anniu_bofang_h);
                FragmentMusicLanYa fragmentMusicLanYa = FragmentMusicLanYa.this;
                if (FragmentMusicLanYa.this.isPlaying) {
                    z = false;
                } else {
                    z = true;
                }
                fragmentMusicLanYa.isPlaying = z;
                FragmentMusicLanYa.this.mApp.btservice.pausePlaySync(false);
                FragmentMusicLanYa.this.mApp.btservice.ipause = true;
            }
        });
    }

    private void initView() {
        this.mMTitle = (MyTextView) this.mRootView.findViewById(R.id.tv_musictitle);
        this.mShang = (ImageView) this.mRootView.findViewById(R.id.shangyiqu);
        this.mBofang = (ImageView) this.mRootView.findViewById(R.id.bofang);
        this.mXiayiqu = (ImageView) this.mRootView.findViewById(R.id.xiayiqu);
        this.arr[0] = this.mShang;
        this.arr[1] = this.mBofang;
        this.arr[2] = this.mXiayiqu;
    }

    public void onDestroy() {
        super.onDestroy();
    }

    public boolean onBack() {
        LauncherApplication.iPlayingAuto = false;
        return false;
    }

    static class MusicLanyaHandler extends Handler {
        private WeakReference<FragmentMusicLanYa> target;

        public MusicLanyaHandler(FragmentMusicLanYa activity) {
            this.target = new WeakReference<>(activity);
        }

        public void handleMessage(Message msg) {
            if (this.target.get() != null) {
                ((FragmentMusicLanYa) this.target.get()).handlerMsg(msg);
            }
        }
    }

    public void handlerMsg(Message msg) {
        boolean z;
        boolean z2;
        if (LauncherApplication.isBlueConnectState) {
            switch (msg.what) {
                case BluetoothService.PLAY_STATE_PLAYING /*1418*/:
                case BluetoothService.PLAY_STATE_PAUSE /*1419*/:
                case BluetoothService.PLAY_STATE_STOP /*1420*/:
                    break;
                case BluetoothService.MEDIAINFO /*1421*/:
                    if (!TextUtils.isEmpty(this.mApp.btservice.music)) {
                        this.mMTitle.setText(this.mApp.btservice.music);
                        break;
                    }
                    break;
            }
            settingPlayName();
            if (msg.what == 6001) {
                this.mIDRIVERENUM = msg.getData().getByte(SysConst.IDRIVER_ENUM);
                if (this.mIDRIVERENUM == Mainboard.EIdriverEnum.TURN_RIGHT.getCode() || this.mIDRIVERENUM == Mainboard.EIdriverEnum.RIGHT.getCode()) {
                    this.isNoXuanZhuan = false;
                    if (this.count < 2) {
                        this.count++;
                    }
                    seclectTrue(this.count);
                } else if (this.mIDRIVERENUM == Mainboard.EIdriverEnum.TURN_LEFT.getCode() || this.mIDRIVERENUM == Mainboard.EIdriverEnum.LEFT.getCode()) {
                    this.isNoXuanZhuan = false;
                    if (this.count > 0) {
                        this.count--;
                    }
                    seclectTrue(this.count);
                } else if (this.mIDRIVERENUM == Mainboard.EIdriverEnum.PRESS.getCode()) {
                    if (this.count == 0) {
                        this.isPlaying = true;
                        setImageSeclect(0);
                        this.mApp.btservice.playPrev();
                    } else if (this.count == 1) {
                        if (!this.isPlaying) {
                            this.mBofang.setImageResource(R.drawable.anniu_zanting_h);
                            if (this.isPlaying) {
                                z2 = false;
                            } else {
                                z2 = true;
                            }
                            this.isPlaying = z2;
                            this.mApp.btservice.requestMusicAudioFocus();
                            this.mApp.btservice.pausePlaySync(true);
                            this.mApp.btservice.ipause = false;
                        } else {
                            this.mBofang.setImageResource(R.drawable.anniu_bofang_h);
                            if (this.isPlaying) {
                                z = false;
                            } else {
                                z = true;
                            }
                            this.isPlaying = z;
                            this.mApp.btservice.pausePlaySync(false);
                            this.mApp.btservice.ipause = true;
                        }
                    } else if (this.count == 2) {
                        this.isPlaying = true;
                        setImageSeclect(2);
                        this.mApp.btservice.playNext();
                    }
                    seclectTrue(this.count);
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void seclectTrue(int seclectCuttun) {
        for (int i = 0; i < this.arr.length; i++) {
            if (this.isNoXuanZhuan) {
                if (seclectCuttun != 1) {
                    this.mBofang.setImageResource(R.drawable.anniu_zanting_n);
                    this.isPlaying = true;
                } else if (this.isPlaying) {
                    this.mBofang.setImageResource(R.drawable.anniu_zanting_h);
                } else {
                    this.mBofang.setImageResource(R.drawable.anniu_bofang_h);
                }
            } else if (seclectCuttun != 1) {
                if (this.isPlaying) {
                    this.mBofang.setImageResource(R.drawable.anniu_zanting_n);
                } else {
                    this.mBofang.setImageResource(R.drawable.anniu_bofang_n);
                }
            } else if (this.isPlaying) {
                this.mBofang.setImageResource(R.drawable.anniu_zanting_h);
            } else {
                this.mBofang.setImageResource(R.drawable.anniu_bofang_h);
            }
            if (seclectCuttun == i) {
                this.arr[i].setSelected(true);
            } else {
                this.arr[i].setSelected(false);
            }
        }
    }

    /* access modifiers changed from: private */
    public void setImageSeclect(int postion) {
        if (postion == 0) {
            this.mShang.setImageResource(R.drawable.anniu_shangyiqu_h);
            this.mMusicHandler.postDelayed(new Runnable() {
                public void run() {
                    FragmentMusicLanYa.this.mShang.setImageResource(R.drawable.anniu_shangyiqu_n);
                }
            }, 500);
        } else if (postion == 2) {
            this.mXiayiqu.setImageResource(R.drawable.anniu_xiayiqu_h);
            this.mMusicHandler.postDelayed(new Runnable() {
                public void run() {
                    FragmentMusicLanYa.this.mXiayiqu.setImageResource(R.drawable.anniu_xiayiqu_n);
                }
            }, 500);
        }
    }
}
