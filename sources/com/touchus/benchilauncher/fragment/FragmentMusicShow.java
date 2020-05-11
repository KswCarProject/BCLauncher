package com.touchus.benchilauncher.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import com.backaudio.android.driver.Mainboard;
import com.touchus.benchilauncher.Launcher;
import com.touchus.benchilauncher.LauncherApplication;
import com.touchus.benchilauncher.R;
import com.touchus.benchilauncher.SysConst;
import com.touchus.benchilauncher.base.BaseFragment;
import com.touchus.benchilauncher.service.MusicPlayControl;
import com.touchus.benchilauncher.utils.SpUtilK;
import com.touchus.benchilauncher.views.MyTextView;
import com.touchus.publicutils.bean.MediaBean;
import com.touchus.publicutils.sysconst.VoiceAssistantConst;
import com.touchus.publicutils.utils.LoadLocalImageUtil;
import com.touchus.publicutils.utils.TimeUtils;
import com.touchus.publicutils.utils.UtilTools;
import java.lang.ref.WeakReference;

public class FragmentMusicShow extends BaseFragment {
    private static String POS = VoiceAssistantConst.FLAG_MAIN_CMD;
    /* access modifiers changed from: private */
    public static Launcher mContext;
    private ImageView[] arr = new ImageView[5];
    /* access modifiers changed from: private */
    public int count = 0;
    private TextView curIndexTview;
    private int loopType = -1;
    /* access modifiers changed from: private */
    public LauncherApplication mApp;
    private ImageView mBofang;
    private byte mIDRIVERENUM;
    private TextView mMTitle;
    private ImageView mMenuImg;
    public MusicHandlerx mMusicHandlerx = new MusicHandlerx(this);
    /* access modifiers changed from: private */
    public TextView mNowTime;
    private View mRootView;
    /* access modifiers changed from: private */
    public ImageView mShang;
    private ImageView mSingleLoopImg;
    private SpUtilK mSpUtilK;
    /* access modifiers changed from: private */
    public ImageView mXiayiqu;
    private TextView mZongTime;
    /* access modifiers changed from: private */
    public ImageView musicPic;
    private int needPlayIndex = -1;
    /* access modifiers changed from: private */
    public SeekBar seekBar;
    private TextView totalRecordTview;
    /* access modifiers changed from: private */
    public Runnable updatePlayTimeRunnable = new Runnable() {
        public void run() {
            FragmentMusicShow.this.mMusicHandlerx.postDelayed(FragmentMusicShow.this.updatePlayTimeRunnable, 1000);
            int curPro = FragmentMusicShow.this.mApp.musicPlayControl.getPosition();
            FragmentMusicShow.this.seekBar.setProgress(curPro);
            String temp = TimeUtils.secToTimeString((long) (curPro / 1000));
            if (!TextUtils.isEmpty(temp)) {
                FragmentMusicShow.this.mNowTime.setText(temp);
            }
        }
    };

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.mRootView = inflater.inflate(R.layout.fragment_music_usb, (ViewGroup) null);
        mContext = (Launcher) getActivity();
        this.mSpUtilK = new SpUtilK(mContext.getApplicationContext());
        if (savedInstanceState != null) {
            this.count = savedInstanceState.getInt(POS, 0);
        }
        this.loopType = this.mSpUtilK.getInt(SysConst.FLAG_MUSIC_LOOP, 1001);
        this.mApp = (LauncherApplication) mContext.getApplication();
        this.mMTitle = (MyTextView) this.mRootView.findViewById(R.id.tv_musictitle);
        this.musicPic = (ImageView) this.mRootView.findViewById(R.id.music_pic);
        this.mNowTime = (TextView) this.mRootView.findViewById(R.id.now_time);
        this.mZongTime = (TextView) this.mRootView.findViewById(R.id.zong_time);
        this.curIndexTview = (TextView) this.mRootView.findViewById(R.id.curIndexTview);
        this.totalRecordTview = (TextView) this.mRootView.findViewById(R.id.totalRecordTview);
        this.seekBar = (SeekBar) this.mRootView.findViewById(R.id.music_seekbar);
        this.mShang = (ImageView) this.mRootView.findViewById(R.id.shangyiqu);
        this.mBofang = (ImageView) this.mRootView.findViewById(R.id.bofang);
        this.mXiayiqu = (ImageView) this.mRootView.findViewById(R.id.xiayiqu);
        this.mMenuImg = (ImageView) this.mRootView.findViewById(R.id.caidan_sd);
        this.mSingleLoopImg = (ImageView) this.mRootView.findViewById(R.id.xuanhuan_sd);
        this.arr[0] = this.mMenuImg;
        this.arr[1] = this.mSingleLoopImg;
        this.arr[2] = this.mShang;
        this.arr[3] = this.mBofang;
        this.arr[4] = this.mXiayiqu;
        this.needPlayIndex = getArguments().getInt("position");
        this.mMTitle.setFocusable(true);
        initListener();
        if (this.mApp.musicPlayControl == null) {
            UtilTools.sendKeyeventToSystem(4);
            this.mApp.musicPlayControl = new MusicPlayControl(getActivity());
        }
        return this.mRootView;
    }

    public void onStart() {
        this.mApp.registerHandler(this.mMusicHandlerx);
        if (this.mApp.musicPlayControl == null) {
            UtilTools.sendKeyeventToSystem(4);
            this.mApp.musicPlayControl = new MusicPlayControl(getActivity());
        }
        this.mApp.musicPlayControl.mHandler = this.mMusicHandlerx;
        if (this.needPlayIndex == this.mApp.musicPlayControl.musicIndex) {
            this.mApp.musicPlayControl.isMusicPlaying();
        } else if (this.needPlayIndex != -1) {
            this.mApp.musicPlayControl.playMusic(this.needPlayIndex);
        }
        this.mMusicHandlerx.post(this.updatePlayTimeRunnable);
        setCurrentPlayInfo();
        seclectTrue(this.count);
        super.onStart();
    }

    public void onStop() {
        this.mApp.unregisterHandler(this.mMusicHandlerx);
        if (this.mApp.musicPlayControl != null) {
            this.mApp.musicPlayControl.mHandler = null;
        }
        this.mMusicHandlerx.removeCallbacks(this.updatePlayTimeRunnable);
        super.onStop();
    }

    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(POS, this.count);
        super.onSaveInstanceState(outState);
    }

    public void onDestroy() {
        this.seekBar = null;
        super.onDestroy();
    }

    public boolean onBack() {
        LauncherApplication.iPlayingAuto = false;
        return false;
    }

    private void setCurrentPlayInfo() {
        MediaBean musicInfo = this.mApp.musicPlayControl.currentMusic;
        if (getActivity() != null && musicInfo != null) {
            this.mMTitle.setText(musicInfo.getTitle().split("\\.")[0]);
            String zongTime = TimeUtils.secToTimeString((long) (((int) musicInfo.getDuration()) / 1000));
            this.seekBar.setMax((int) musicInfo.getDuration());
            this.mZongTime.setText(zongTime);
            this.mNowTime.setText(TimeUtils.secToTimeString((long) (this.mApp.musicPlayControl.getPosition() / 1000)));
            int temp = this.mApp.musicPlayControl.musicIndex;
            if (temp >= 0) {
                this.curIndexTview.setText(new StringBuilder().append(temp + 1).toString());
            } else {
                this.curIndexTview.setText("");
            }
            if (this.mApp.musicPlayControl.getMusicList() != null) {
                this.totalRecordTview.setText(new StringBuilder().append(this.mApp.musicPlayControl.getMusicList().size()).toString());
            } else {
                this.totalRecordTview.setText("");
            }
            if (musicInfo.getBitmap() == null) {
                this.musicPic.setTag(musicInfo.getData());
                LoadLocalImageUtil.getInstance().loadDrawable(musicInfo, LoadLocalImageUtil.MUSIC_TYPE, mContext, musicInfo.getData(), new LoadLocalImageUtil.ImageCallback() {
                    public void imageLoaded(Bitmap bitmip, String tag) {
                        if (!FragmentMusicShow.this.musicPic.getTag().equals(tag) || bitmip == null) {
                            FragmentMusicShow.this.musicPic.setImageResource(R.drawable.usbmusic_tu);
                        } else {
                            FragmentMusicShow.this.musicPic.setImageBitmap(bitmip);
                        }
                    }
                });
            } else {
                this.musicPic.setImageBitmap(musicInfo.getBitmap());
            }
            setLoopType();
        }
    }

    private void setLoopType() {
        switch (this.loopType) {
            case 1001:
                this.mSingleLoopImg.setImageResource(R.drawable.anniu_allxunhuan_h);
                break;
            case 1002:
                this.mSingleLoopImg.setImageResource(R.drawable.anniu_danquxunhuan_h);
                break;
            case 1003:
                this.mSingleLoopImg.setImageResource(R.drawable.anniu_suiji_h);
                break;
            default:
                this.mSingleLoopImg.setImageResource(R.drawable.anniu_allxunhuan_h);
                break;
        }
        this.mApp.musicPlayControl.setPlayLoopType(this.loopType);
    }

    private void initListener() {
        this.mMenuImg.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FragmentMusicShow.this.count = 0;
                FragmentMusicShow.this.seclectTrue(0);
                FragmentMusicShow.mContext.handleBackAction();
            }
        });
        this.mSingleLoopImg.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FragmentMusicShow.this.count = 1;
                FragmentMusicShow.this.seclectTrue(1);
                FragmentMusicShow.this.pressItem(FragmentMusicShow.this.count);
            }
        });
        this.mShang.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FragmentMusicShow.this.count = 2;
                FragmentMusicShow.this.seclectTrue(FragmentMusicShow.this.count);
                FragmentMusicShow.this.pressItem(FragmentMusicShow.this.count);
            }
        });
        this.mBofang.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FragmentMusicShow.this.count = 3;
                FragmentMusicShow.this.seclectTrue(FragmentMusicShow.this.count);
                FragmentMusicShow.this.pressItem(FragmentMusicShow.this.count);
            }
        });
        this.mXiayiqu.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FragmentMusicShow.this.count = 4;
                FragmentMusicShow.this.seclectTrue(FragmentMusicShow.this.count);
                FragmentMusicShow.this.pressItem(FragmentMusicShow.this.count);
            }
        });
        this.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                FragmentMusicShow.this.mApp.musicPlayControl.changeCurrentPlayProgress(seekBar.getProgress());
            }
        });
    }

    /* access modifiers changed from: private */
    public void pressItem(int position) {
        if (this.mApp.musicPlayControl == null) {
            this.mApp.musicPlayControl = new MusicPlayControl(getActivity());
        }
        switch (position) {
            case 0:
                mContext.handleBackAction();
                return;
            case 1:
                if (this.loopType == 1001) {
                    this.loopType = 1003;
                } else if (this.loopType == 1003) {
                    this.loopType = 1002;
                } else {
                    this.loopType = 1001;
                }
                this.mSpUtilK.putInt(SysConst.FLAG_MUSIC_LOOP, this.loopType);
                setLoopType();
                return;
            case 2:
                prev();
                setImageSeclect(position);
                return;
            case 3:
                if (this.mApp.musicPlayControl.isMusicPlaying()) {
                    this.mBofang.setImageResource(R.drawable.anniu_bofang_h);
                    this.mApp.musicPlayControl.setPauseState(true);
                    this.mApp.musicPlayControl.pauseMusic();
                    this.mApp.musicPlayControl.settingPlayState(false);
                    return;
                }
                this.mBofang.setImageResource(R.drawable.anniu_zanting_h);
                this.mApp.musicPlayControl.requestAudioFocus();
                this.mApp.musicPlayControl.replayMusic();
                this.mApp.musicPlayControl.setPauseState(false);
                this.mApp.musicPlayControl.settingPlayState(true);
                return;
            case 4:
                Next();
                setImageSeclect(position);
                return;
            default:
                return;
        }
    }

    public void Next() {
        this.mApp.musicPlayControl.setPauseState(false);
        this.mApp.musicPlayControl.playNextMusic();
        setCurrentPlayInfo();
    }

    public void prev() {
        this.mApp.musicPlayControl.setPauseState(false);
        this.mApp.musicPlayControl.playPreviousMusic();
        setCurrentPlayInfo();
    }

    static class MusicHandlerx extends Handler {
        private WeakReference<FragmentMusicShow> target;

        public MusicHandlerx(FragmentMusicShow activity) {
            this.target = new WeakReference<>(activity);
        }

        public void handleMessage(Message msg) {
            if (this.target.get() != null) {
                ((FragmentMusicShow) this.target.get()).handlerMsgx(msg);
            }
        }
    }

    public void handlerMsgx(Message msg) {
        if (msg.what == 6001) {
            this.mIDRIVERENUM = msg.getData().getByte(SysConst.IDRIVER_ENUM);
            if (this.mIDRIVERENUM == Mainboard.EIdriverEnum.PRESS.getCode()) {
                pressItem(this.count);
            } else if (this.mIDRIVERENUM == Mainboard.EIdriverEnum.TURN_RIGHT.getCode() || this.mIDRIVERENUM == Mainboard.EIdriverEnum.RIGHT.getCode()) {
                if (this.count < this.arr.length - 1) {
                    this.count++;
                }
                seclectTrue(this.count);
            } else if (this.mIDRIVERENUM == Mainboard.EIdriverEnum.TURN_LEFT.getCode() || this.mIDRIVERENUM == Mainboard.EIdriverEnum.LEFT.getCode()) {
                if (this.count > 0) {
                    this.count--;
                }
                seclectTrue(this.count);
            }
        } else if (msg.what == 7001) {
            if (this.count != 3) {
                this.mBofang.setImageResource(R.drawable.anniu_zanting_n);
            } else {
                this.mBofang.setImageResource(R.drawable.anniu_zanting_h);
            }
            setCurrentPlayInfo();
        } else if (msg.what == 7003) {
            setPlayBtnStatus(this.count);
        }
    }

    /* access modifiers changed from: private */
    public void seclectTrue(int seclectCuttun) {
        setPlayBtnStatus(seclectCuttun);
        for (int i = 0; i < this.arr.length; i++) {
            if (seclectCuttun == i) {
                this.arr[i].setSelected(true);
            } else {
                this.arr[i].setSelected(false);
            }
        }
    }

    private void setPlayBtnStatus(int seclectCuttun) {
        if (this.mApp.musicPlayControl == null) {
            UtilTools.sendKeyeventToSystem(4);
            this.mApp.musicPlayControl = new MusicPlayControl(getActivity());
        }
        if (seclectCuttun != 3) {
            if (this.mApp.musicPlayControl.isMusicPlaying()) {
                this.mBofang.setImageResource(R.drawable.anniu_zanting_n);
            } else {
                this.mBofang.setImageResource(R.drawable.anniu_bofang_n);
            }
        } else if (this.mApp.musicPlayControl.isMusicPlaying()) {
            this.mBofang.setImageResource(R.drawable.anniu_zanting_h);
        } else {
            this.mBofang.setImageResource(R.drawable.anniu_bofang_h);
        }
    }

    private void setImageSeclect(int position) {
        if (position == 2) {
            this.mShang.setImageResource(R.drawable.anniu_shangyiqu_h);
            this.mMusicHandlerx.postDelayed(new Runnable() {
                public void run() {
                    FragmentMusicShow.this.mShang.setImageResource(R.drawable.anniu_shangyiqu_n);
                }
            }, 300);
        } else if (position == 4) {
            this.mXiayiqu.setImageResource(R.drawable.anniu_xiayiqu_h);
            this.mMusicHandlerx.postDelayed(new Runnable() {
                public void run() {
                    FragmentMusicShow.this.mXiayiqu.setImageResource(R.drawable.anniu_xiayiqu_n);
                }
            }, 300);
        }
    }
}
