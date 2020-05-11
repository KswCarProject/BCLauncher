package com.touchus.benchilauncher.fragment;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import com.backaudio.android.driver.Mainboard;
import com.backaudio.android.driver.beans.CarBaseInfo;
import com.touchus.benchilauncher.Launcher;
import com.touchus.benchilauncher.LauncherApplication;
import com.touchus.benchilauncher.MainService;
import com.touchus.benchilauncher.R;
import com.touchus.benchilauncher.SysConst;
import com.touchus.benchilauncher.base.BaseFragment;
import com.touchus.benchilauncher.utils.SpUtilK;
import com.touchus.benchilauncher.views.MyTextView;
import com.touchus.publicutils.bean.MediaBean;
import com.touchus.publicutils.utils.TimeUtils;
import java.io.File;
import java.lang.ref.WeakReference;
import java.util.List;
import javax.mail.internet.HeaderTokenizer;
import org.apache.log4j.net.SyslogAppender;

public class FragmentVideoPlay extends BaseFragment implements View.OnTouchListener {
    /* access modifiers changed from: private */
    public static String LAST_VIDEO_PLAY_NAME = "lastVideoPlayName";
    /* access modifiers changed from: private */
    public static String LAST_VIDEO_PLAY_POSITION = "lastVideoPlayPosition";
    /* access modifiers changed from: private */
    public boolean PasueFlag = false;
    private ImageButton[] arr = new ImageButton[7];
    private Runnable autoSettingHideOrShowRunnable = new Runnable() {
        public void run() {
            FragmentVideoPlay.this.hidePlayControlBar();
        }
    };
    int changeProgress;
    private int countSufaceVieew = 0;
    int curProgress;
    /* access modifiers changed from: private */
    public int currentPlayIndex;
    /* access modifiers changed from: private */
    public int currentPosition = -1;
    private boolean iIsPlaySeekbarShow = true;
    /* access modifiers changed from: private */
    public boolean iNeedHeartThread = false;
    /* access modifiers changed from: private */
    public int indexCount;
    private CarBaseInfo info;
    /* access modifiers changed from: private */
    public boolean isBigView = false;
    private boolean isOpenSound = false;
    /* access modifiers changed from: private */
    public boolean isPrepare = false;
    private ViewGroup.LayoutParams lp;
    /* access modifiers changed from: private */
    public LauncherApplication mApp;
    private TextView mBoTime;
    Context mContext;
    private LinearLayout mDibuCaidan;
    private ViewGroup.LayoutParams mDibucandan;
    private ImageButton mFangsuo;
    private FrameLayout mFlsurfaceView;
    private byte mIDRIVERENUM;
    private Launcher mMMainActivity;
    private AudioManager.OnAudioFocusChangeListener mMainAudioFocusListener = new AudioManager.OnAudioFocusChangeListener() {
        public void onAudioFocusChange(int focusChange) {
            switch (focusChange) {
                case HeaderTokenizer.Token.COMMENT /*-3*/:
                    if (SysConst.isBT() || FragmentVideoPlay.this.mApp.ismix) {
                        FragmentVideoPlay.this.mediaPlayer.setVolume(SysConst.musicDecVolume, SysConst.musicDecVolume);
                        return;
                    } else if (FragmentVideoPlay.this.mediaPlayer != null) {
                        FragmentVideoPlay.this.mediaPlayer.pause();
                        FragmentVideoPlay.this.PasueFlag = true;
                        FragmentVideoPlay.this.vedio_pause.setVisibility(0);
                        return;
                    } else {
                        return;
                    }
                case -2:
                    if (FragmentVideoPlay.this.mediaPlayer != null) {
                        FragmentVideoPlay.this.mediaPlayer.pause();
                        FragmentVideoPlay.this.PasueFlag = true;
                        FragmentVideoPlay.this.vedio_pause.setVisibility(0);
                        return;
                    }
                    return;
                case 1:
                    FragmentVideoPlay.this.mApp.musicType = 3;
                    FragmentVideoPlay.this.mediaPlayer.setVolume(SysConst.musicNorVolume, SysConst.musicNorVolume);
                    if (FragmentVideoPlay.this.mediaPlayer != null && FragmentVideoPlay.this.PasueFlag) {
                        FragmentVideoPlay.this.mediaPlayer.start();
                        FragmentVideoPlay.this.vedio_pause.setVisibility(8);
                        FragmentVideoPlay.this.PasueFlag = false;
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    };
    public MusicLanyaHandler mMusicHandler = new MusicLanyaHandler(this);
    private LinearLayout mPaomadeng;
    private View mRootView;
    private SeekBar mSeekBar;
    private LinearLayout mSeekBeijing;
    private ImageButton mSound;
    private LinearLayout mToolBar;
    /* access modifiers changed from: private */
    public ImageButton mVedioBofang;
    private ImageButton mVedioCaidan;
    private MyTextView mVedioName;
    private ImageButton mVedioShnag;
    private ImageButton mVedioXia;
    private ImageButton mYoushengdao;
    private TextView mZongTime;
    private ImageButton mZuoshengdao;
    public List<MediaBean> mediaBeans;
    /* access modifiers changed from: private */
    public MediaPlayer mediaPlayer;
    int moveX;
    /* access modifiers changed from: private */
    public SpUtilK share;
    int startX;
    /* access modifiers changed from: private */
    public SurfaceView surfaceView;
    private SurfaceViewCallBack surfaceViewCallBack;
    /* access modifiers changed from: private */
    public Runnable updateRunnable = new Runnable() {
        public void run() {
            if (FragmentVideoPlay.this.iNeedHeartThread) {
                FragmentVideoPlay.this.updateCurrentPlayInfo();
                FragmentVideoPlay.this.mMusicHandler.removeCallbacks(FragmentVideoPlay.this.updateRunnable);
                FragmentVideoPlay.this.mMusicHandler.postDelayed(FragmentVideoPlay.this.updateRunnable, 1000);
            }
        }
    };
    /* access modifiers changed from: private */
    public ImageView vedio_pause;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.mContext = getActivity();
        this.mMMainActivity = (Launcher) getActivity();
        this.mApp = (LauncherApplication) this.mMMainActivity.getApplication();
        this.mApp.registerHandler(this.mMusicHandler);
        long start = System.currentTimeMillis();
        Log.e("", "videoshow = " + start);
        this.share = new SpUtilK((Context) getActivity());
        this.mRootView = inflater.inflate(R.layout.fragment_show, (ViewGroup) null);
        initView();
        this.mediaPlayer = new MediaPlayer();
        this.currentPlayIndex = getArguments().getInt("position");
        if (this.mediaBeans != null) {
            MediaBean temp = this.mediaBeans.get(this.currentPlayIndex);
            String lastName = this.share.getString(LAST_VIDEO_PLAY_NAME, "-");
            if (temp == null || !lastName.equals(temp.getDisplay_name())) {
                this.currentPosition = -1;
            } else {
                this.currentPosition = this.share.getInt(LAST_VIDEO_PLAY_POSITION, -1);
            }
        } else {
            this.currentPosition = -1;
        }
        this.surfaceView = (SurfaceView) this.mRootView.findViewById(R.id.videoSurfaceView);
        this.surfaceView.getHolder().setFixedSize(SyslogAppender.LOG_LOCAL6, SyslogAppender.LOG_LOCAL2);
        this.surfaceView.getHolder().setKeepScreenOn(true);
        this.surfaceView.setClickable(true);
        this.surfaceViewCallBack = new SurfaceViewCallBack(this, (SurfaceViewCallBack) null);
        this.surfaceView.getHolder().addCallback(this.surfaceViewCallBack);
        initListener();
        Log.e("", "videoshow = " + (start - System.currentTimeMillis()));
        setAutoYincanMuen();
        seclectTrue(0);
        requestAudioFocus();
        return this.mRootView;
    }

    public void onStart() {
        this.iNeedHeartThread = true;
        if (this.mediaPlayer == null) {
            this.mediaPlayer = new MediaPlayer();
        }
        super.onStart();
    }

    public void onStop() {
        super.onStop();
    }

    public void onDestroy() {
        abandonAudioFocus();
        if (this.mediaPlayer != null) {
            this.mediaPlayer.release();
            this.mediaPlayer = null;
        }
        this.iNeedHeartThread = false;
        this.mMusicHandler.removeCallbacks(this.autoSettingHideOrShowRunnable);
        this.mApp.unregisterHandler(this.mMusicHandler);
        super.onDestroy();
    }

    private void setAutoYincanMuen() {
        this.mMusicHandler.removeCallbacks(this.autoSettingHideOrShowRunnable);
        this.mMusicHandler.postDelayed(this.autoSettingHideOrShowRunnable, 5000);
    }

    public void next() {
        this.currentPlayIndex++;
        if (this.mediaBeans.size() <= this.currentPlayIndex) {
            this.currentPlayIndex = 0;
        }
        play(this.currentPlayIndex);
    }

    public void fangdaVedio() {
        this.lp = this.mFlsurfaceView.getLayoutParams();
        this.lp.width = 1280;
        this.lp.height = 480;
        this.mFlsurfaceView.setLayoutParams(this.lp);
        this.mDibucandan = this.mDibuCaidan.getLayoutParams();
        this.mDibucandan.width = 1280;
        this.mDibucandan.height = 480;
        this.mDibuCaidan.setLayoutParams(this.mDibucandan);
        this.mFangsuo.setImageResource(R.drawable.bt_selector_suofangda);
        this.mToolBar.setGravity(1);
        if (this.countSufaceVieew % 3 == 0) {
            this.mYoushengdao.setImageResource(R.drawable.shipin_xia_10);
            ViewGroup.LayoutParams lp2 = this.surfaceView.getLayoutParams();
            lp2.width = 1280;
            lp2.height = 480;
            this.surfaceView.setLayoutParams(lp2);
        } else if (this.countSufaceVieew % 3 == 2) {
            this.mYoushengdao.setImageResource(R.drawable.shipin_xia_05);
            ViewGroup.LayoutParams lp3 = this.surfaceView.getLayoutParams();
            lp3.width = 1060;
            lp3.height = 300;
            this.surfaceView.setLayoutParams(lp3);
        }
        ((RelativeLayout.LayoutParams) this.mSeekBeijing.getLayoutParams()).setMargins(0, 358, 330, 0);
        RelativeLayout.LayoutParams layoutParams1 = (RelativeLayout.LayoutParams) this.mPaomadeng.getLayoutParams();
        layoutParams1.width = 480;
        this.mVedioName.setGravity(17);
        layoutParams1.setMargins(400, 0, 0, 10);
    }

    public void suoXiaoVedio() {
        this.lp = this.mFlsurfaceView.getLayoutParams();
        this.lp.width = 800;
        this.lp.height = 480;
        this.mFlsurfaceView.setLayoutParams(this.lp);
        this.mDibucandan = this.mDibuCaidan.getLayoutParams();
        this.mDibucandan.width = 800;
        this.mDibucandan.height = 480;
        this.mDibuCaidan.setLayoutParams(this.mDibucandan);
        this.mFangsuo.setImageResource(R.drawable.bt_selector_suofang);
        this.mToolBar.setGravity(5);
        ((RelativeLayout.LayoutParams) this.mSeekBeijing.getLayoutParams()).setMargins(54, 358, 74, 0);
        RelativeLayout.LayoutParams layoutParams1 = (RelativeLayout.LayoutParams) this.mPaomadeng.getLayoutParams();
        this.mVedioName.setGravity(3);
        layoutParams1.width = 480;
        layoutParams1.setMargins(490, 0, 0, 10);
        if (this.countSufaceVieew % 3 == 0) {
            this.mYoushengdao.setImageResource(R.drawable.shipin_xia_10);
            ViewGroup.LayoutParams lp2 = this.surfaceView.getLayoutParams();
            lp2.width = 800;
            lp2.height = 480;
            this.surfaceView.setLayoutParams(lp2);
        } else if (this.countSufaceVieew % 3 == 2) {
            this.mYoushengdao.setImageResource(R.drawable.shipin_xia_05);
            ViewGroup.LayoutParams lp3 = this.surfaceView.getLayoutParams();
            lp3.width = 540;
            lp3.height = 263;
            this.surfaceView.setLayoutParams(lp3);
        }
    }

    private void initListener() {
        this.vedio_pause.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FragmentVideoPlay.this.mediaPlayer.start();
                FragmentVideoPlay.this.vedio_pause.setVisibility(8);
                FragmentVideoPlay.this.mVedioBofang.setImageResource(R.drawable.shipin_xia_zanting);
                FragmentVideoPlay.this.PasueFlag = !FragmentVideoPlay.this.PasueFlag;
            }
        });
        this.mSound.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FragmentVideoPlay.this.indexCount = 0;
                FragmentVideoPlay.this.press(FragmentVideoPlay.this.indexCount);
            }
        });
        this.mZuoshengdao.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FragmentVideoPlay.this.indexCount = 1;
                FragmentVideoPlay.this.press(FragmentVideoPlay.this.indexCount);
            }
        });
        this.mYoushengdao.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FragmentVideoPlay.this.indexCount = 5;
                FragmentVideoPlay.this.press(FragmentVideoPlay.this.indexCount);
            }
        });
        this.mVedioCaidan.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FragmentVideoPlay.this.indexCount = 6;
                FragmentVideoPlay.this.press(FragmentVideoPlay.this.indexCount);
            }
        });
        this.mFlsurfaceView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
            }
        });
        this.surfaceView.setOnTouchListener(this);
        this.mFangsuo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!FragmentVideoPlay.this.isBigView) {
                    FragmentVideoPlay.this.isBigView = true;
                    FragmentVideoPlay.this.fangdaVedio();
                    return;
                }
                FragmentVideoPlay.this.isBigView = false;
                FragmentVideoPlay.this.suoXiaoVedio();
            }
        });
        this.mVedioShnag.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FragmentVideoPlay.this.indexCount = 2;
                FragmentVideoPlay.this.press(FragmentVideoPlay.this.indexCount);
            }
        });
        this.mVedioXia.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FragmentVideoPlay.this.indexCount = 4;
                FragmentVideoPlay.this.press(FragmentVideoPlay.this.indexCount);
            }
        });
        this.mVedioBofang.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FragmentVideoPlay.this.indexCount = 3;
                FragmentVideoPlay.this.press(FragmentVideoPlay.this.indexCount);
            }
        });
        this.mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (FragmentVideoPlay.this.mediaPlayer != null) {
                    int progress = seekBar.getProgress();
                    int playerMax = FragmentVideoPlay.this.mediaPlayer.getDuration();
                    FragmentVideoPlay.this.currentPosition = (playerMax * progress) / seekBar.getMax();
                    FragmentVideoPlay.this.mediaPlayer.seekTo(FragmentVideoPlay.this.currentPosition);
                }
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }
        });
    }

    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == 0) {
            this.startX = (int) event.getX();
            this.curProgress = this.mSeekBar.getProgress();
            this.moveX = 0;
            return true;
        } else if (event.getAction() == 2) {
            this.moveX = (int) (event.getX() - ((float) this.startX));
            if (this.moveX >= (100 - this.curProgress) * 10) {
                this.changeProgress = 100;
            } else if (this.moveX <= (-this.curProgress) * 10) {
                this.changeProgress = 0;
            } else {
                this.changeProgress = this.curProgress + (this.moveX / 10);
            }
            if ((this.moveX >= 5 || this.moveX <= -5) && this.iIsPlaySeekbarShow) {
                this.currentPosition = (this.mediaPlayer.getDuration() * this.changeProgress) / 100;
                this.mediaPlayer.seekTo(this.currentPosition);
                this.mMusicHandler.post(this.updateRunnable);
            }
            Log.e("mVideo", "onTouch moveX = " + this.moveX);
            return true;
        } else if (event.getAction() != 1) {
            return false;
        } else {
            if (this.moveX >= 5 || this.moveX <= -5) {
                return true;
            }
            showOrHidePlaySeekbar();
            return true;
        }
    }

    private void showOrFangSuo() {
        if (this.countSufaceVieew % 3 == 0) {
            if (this.isBigView) {
                this.mYoushengdao.setImageResource(R.drawable.shipin_xia_10);
                ViewGroup.LayoutParams lp2 = this.surfaceView.getLayoutParams();
                lp2.width = 1280;
                lp2.height = 480;
                this.surfaceView.setLayoutParams(lp2);
                return;
            }
            this.mYoushengdao.setImageResource(R.drawable.shipin_xia_10);
            ViewGroup.LayoutParams lp3 = this.surfaceView.getLayoutParams();
            lp3.width = 800;
            lp3.height = 480;
            this.surfaceView.setLayoutParams(lp3);
        } else if (this.countSufaceVieew % 3 == 1) {
            this.mYoushengdao.setImageResource(R.drawable.shipin_xia_15);
            ViewGroup.LayoutParams lp4 = this.surfaceView.getLayoutParams();
            lp4.width = 2000;
            lp4.height = 1000;
            this.surfaceView.setLayoutParams(lp4);
        } else if (this.countSufaceVieew % 3 != 2) {
        } else {
            if (this.isBigView) {
                this.mYoushengdao.setImageResource(R.drawable.shipin_xia_05);
                ViewGroup.LayoutParams lp5 = this.surfaceView.getLayoutParams();
                lp5.width = 1060;
                lp5.height = 300;
                this.surfaceView.setLayoutParams(lp5);
                return;
            }
            this.mYoushengdao.setImageResource(R.drawable.shipin_xia_05);
            ViewGroup.LayoutParams lp6 = this.surfaceView.getLayoutParams();
            lp6.width = 540;
            lp6.height = 263;
            this.surfaceView.setLayoutParams(lp6);
        }
    }

    private void showOrHidePlaySeekbar() {
        if (this.iIsPlaySeekbarShow) {
            hidePlayControlBar();
        } else {
            showPlayControlBar();
        }
    }

    private void showPlayControlBar() {
        this.mToolBar.setVisibility(0);
        this.mFangsuo.setVisibility(0);
        this.mDibuCaidan.setVisibility(0);
        this.mSeekBeijing.setVisibility(0);
        this.mPaomadeng.setVisibility(0);
        this.mMMainActivity.showButtom();
        this.mMMainActivity.showTop();
        setAutoYincanMuen();
        this.iIsPlaySeekbarShow = true;
    }

    /* access modifiers changed from: private */
    public void hidePlayControlBar() {
        this.mFangsuo.setVisibility(8);
        this.mDibuCaidan.setVisibility(8);
        this.mToolBar.setVisibility(8);
        this.mSeekBeijing.setVisibility(8);
        this.mPaomadeng.setVisibility(8);
        if (this.isBigView) {
            this.mMMainActivity.hideButtom();
            this.mMMainActivity.hideTop();
        } else {
            this.mMMainActivity.showButtom();
            this.mMMainActivity.showTop();
        }
        this.iIsPlaySeekbarShow = false;
    }

    private void initView() {
        this.mMMainActivity.showButtom();
        this.mSeekBar = (SeekBar) this.mRootView.findViewById(R.id.video_toolbar_time_seekbar);
        this.mToolBar = (LinearLayout) this.mRootView.findViewById(R.id.video_toolbar_rlPlayProg);
        this.mZongTime = (TextView) this.mRootView.findViewById(R.id.video_toolbar_time_zong);
        this.mBoTime = (TextView) this.mRootView.findViewById(R.id.video_toolbar_time_bofang);
        this.vedio_pause = (ImageView) this.mRootView.findViewById(R.id.vedio_pause);
        this.mVedioShnag = (ImageButton) this.mRootView.findViewById(R.id.vedio_shang);
        this.mVedioXia = (ImageButton) this.mRootView.findViewById(R.id.vedio_xia);
        this.mVedioBofang = (ImageButton) this.mRootView.findViewById(R.id.vedio_bofang);
        this.mVedioCaidan = (ImageButton) this.mRootView.findViewById(R.id.vedio_caidan);
        this.mFangsuo = (ImageButton) this.mRootView.findViewById(R.id.fangsuoshipin);
        this.mDibuCaidan = (LinearLayout) this.mRootView.findViewById(R.id.dibu_caidan);
        this.mFlsurfaceView = (FrameLayout) this.mRootView.findViewById(R.id.fl_surfaceview);
        this.mSound = (ImageButton) this.mRootView.findViewById(R.id.vedio_sound);
        this.mZuoshengdao = (ImageButton) this.mRootView.findViewById(R.id.zuoshengdao);
        this.mYoushengdao = (ImageButton) this.mRootView.findViewById(R.id.youshengdao);
        this.mVedioName = (MyTextView) this.mRootView.findViewById(R.id.vedio_name);
        this.mPaomadeng = (LinearLayout) this.mRootView.findViewById(R.id.name_paomadeng);
        this.mSeekBeijing = (LinearLayout) this.mRootView.findViewById(R.id.seek_bar_beijing);
        this.arr[0] = this.mSound;
        this.arr[1] = this.mZuoshengdao;
        this.arr[2] = this.mVedioShnag;
        this.arr[3] = this.mVedioBofang;
        this.arr[4] = this.mVedioXia;
        this.arr[5] = this.mYoushengdao;
        this.arr[6] = this.mVedioCaidan;
    }

    public boolean onBack() {
        return false;
    }

    private final class SurfaceViewCallBack implements SurfaceHolder.Callback {
        private SurfaceViewCallBack() {
        }

        /* synthetic */ SurfaceViewCallBack(FragmentVideoPlay fragmentVideoPlay, SurfaceViewCallBack surfaceViewCallBack) {
            this();
        }

        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        }

        public void surfaceCreated(SurfaceHolder holder) {
            if (FragmentVideoPlay.this.mediaBeans != null && FragmentVideoPlay.this.currentPlayIndex >= 0 && FragmentVideoPlay.this.mediaBeans.size() > FragmentVideoPlay.this.currentPlayIndex && FragmentVideoPlay.this.mediaBeans.get(FragmentVideoPlay.this.currentPlayIndex).getData() != null && new File(FragmentVideoPlay.this.mediaBeans.get(FragmentVideoPlay.this.currentPlayIndex).getData()).exists()) {
                if (!FragmentVideoPlay.this.PasueFlag) {
                    FragmentVideoPlay.this.play(FragmentVideoPlay.this.currentPlayIndex);
                } else {
                    FragmentVideoPlay.this.mediaPlayer.setDisplay(FragmentVideoPlay.this.surfaceView.getHolder());
                }
                if (FragmentVideoPlay.this.currentPosition >= 0) {
                    FragmentVideoPlay.this.mediaPlayer.seekTo(FragmentVideoPlay.this.currentPosition);
                }
            }
        }

        public void surfaceDestroyed(SurfaceHolder holder) {
            if (FragmentVideoPlay.this.mediaPlayer != null) {
                Log.e("videoshow", "mediaPlayer isPlaying= " + FragmentVideoPlay.this.mediaPlayer.isPlaying());
                if (FragmentVideoPlay.this.mediaPlayer.isPlaying()) {
                    FragmentVideoPlay.this.currentPosition = FragmentVideoPlay.this.mediaPlayer.getCurrentPosition();
                    FragmentVideoPlay.this.share.putInt(FragmentVideoPlay.LAST_VIDEO_PLAY_POSITION, FragmentVideoPlay.this.currentPosition);
                    FragmentVideoPlay.this.share.putString(FragmentVideoPlay.LAST_VIDEO_PLAY_NAME, FragmentVideoPlay.this.mediaBeans.get(FragmentVideoPlay.this.currentPlayIndex).getDisplay_name());
                    FragmentVideoPlay.this.mediaPlayer.stop();
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void play(int position) {
        this.mMusicHandler.removeCallbacks(this.updateRunnable);
        if (this.mediaBeans.size() != 0) {
            LauncherApplication.videoIndex = position;
            LauncherApplication.mSpUtils.putInt(SysConst.FLAG_VIDEO_POS, position);
            try {
                this.mediaPlayer.reset();
                this.mediaPlayer.setDataSource(this.mediaBeans.get(position).getData());
                this.mediaPlayer.setDisplay(this.surfaceView.getHolder());
                this.isPrepare = true;
                this.mediaPlayer.prepareAsync();
                this.mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                    public boolean onError(MediaPlayer arg0, int arg1, int arg2) {
                        FragmentVideoPlay.this.resetPositon();
                        return false;
                    }
                });
                if (this.mediaBeans != null) {
                    MediaBean temp = this.mediaBeans.get(this.currentPlayIndex);
                    String lastName = this.share.getString(LAST_VIDEO_PLAY_NAME, "-");
                    if (temp == null || !lastName.equals(temp.getDisplay_name())) {
                        this.currentPosition = -1;
                    } else {
                        this.currentPosition = this.share.getInt(LAST_VIDEO_PLAY_POSITION, -1);
                    }
                } else {
                    this.currentPosition = -1;
                }
                this.mediaPlayer.setOnPreparedListener(new PrepareListener(this.currentPosition));
                this.mVedioName.setText(this.mediaBeans.get(position).getTitle());
                this.mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    public void onCompletion(MediaPlayer mp) {
                        Log.e("videoshow", "onCompletion isPrepare = " + FragmentVideoPlay.this.isPrepare);
                        FragmentVideoPlay.this.resetPositon();
                        if (!FragmentVideoPlay.this.isPrepare) {
                            FragmentVideoPlay.this.next();
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void resetPositon() {
        this.share.putInt(LAST_VIDEO_PLAY_POSITION, -1);
        this.share.putString(LAST_VIDEO_PLAY_NAME, "");
    }

    private final class PrepareListener implements MediaPlayer.OnPreparedListener {
        private int position;

        public PrepareListener(int position2) {
            this.position = position2;
        }

        public void onPrepared(MediaPlayer mp) {
            FragmentVideoPlay.this.isPrepare = false;
            FragmentVideoPlay.this.mediaPlayer.start();
            FragmentVideoPlay.this.vedio_pause.setVisibility(8);
            FragmentVideoPlay.this.mMusicHandler.post(FragmentVideoPlay.this.updateRunnable);
            if (this.position > 0) {
                FragmentVideoPlay.this.mediaPlayer.seekTo(this.position);
            }
        }
    }

    /* access modifiers changed from: private */
    public void updateCurrentPlayInfo() {
        try {
            this.currentPosition = this.mediaPlayer.getCurrentPosition();
            int mMax = this.mediaPlayer.getDuration();
            this.mSeekBar.setProgress((this.currentPosition * this.mSeekBar.getMax()) / mMax);
            this.mBoTime.setText(TimeUtils.secToTimeString((long) (this.currentPosition / 1000)));
            this.mZongTime.setText(TimeUtils.secToTimeString((long) (mMax / 1000)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static class MusicLanyaHandler extends Handler {
        private WeakReference<FragmentVideoPlay> target;

        public MusicLanyaHandler(FragmentVideoPlay activity) {
            this.target = new WeakReference<>(activity);
        }

        public void handleMessage(Message msg) {
            if (this.target.get() != null) {
                ((FragmentVideoPlay) this.target.get()).handlerMsg(msg);
            }
        }
    }

    public void handlerMsg(Message msg) {
        boolean z = true;
        boolean z2 = false;
        if (msg.what == 6001) {
            this.mIDRIVERENUM = msg.getData().getByte(SysConst.IDRIVER_ENUM);
            if (this.mIDRIVERENUM == Mainboard.EIdriverEnum.TURN_RIGHT.getCode()) {
                showPlayControlBar();
                this.mToolBar.setVisibility(0);
                this.mFangsuo.setVisibility(0);
                this.mDibuCaidan.setVisibility(0);
                if (this.indexCount < this.arr.length - 1) {
                    this.indexCount++;
                }
                seclectTrue(this.indexCount);
                setAutoYincanMuen();
            } else if (this.mIDRIVERENUM == Mainboard.EIdriverEnum.PRESS.getCode()) {
                showPlayControlBar();
                press(this.indexCount);
            } else if (this.mIDRIVERENUM == Mainboard.EIdriverEnum.TURN_LEFT.getCode()) {
                showPlayControlBar();
                this.mToolBar.setVisibility(0);
                this.mFangsuo.setVisibility(0);
                this.mDibuCaidan.setVisibility(0);
                if (this.indexCount > 0) {
                    this.indexCount--;
                }
                seclectTrue(this.indexCount);
                setAutoYincanMuen();
            } else if (this.mIDRIVERENUM == Mainboard.EIdriverEnum.LEFT.getCode()) {
                if (!this.isBigView) {
                    this.isBigView = true;
                    fangdaVedio();
                }
            } else if (this.mIDRIVERENUM == Mainboard.EIdriverEnum.RIGHT.getCode()) {
                if (this.isBigView) {
                    this.isBigView = false;
                    suoXiaoVedio();
                }
            } else if (this.mIDRIVERENUM == Mainboard.EIdriverEnum.UP.getCode()) {
                if (this.PasueFlag) {
                    this.mediaPlayer.start();
                    this.mVedioBofang.setImageResource(R.drawable.shipin_xia_zanting);
                    if (!this.PasueFlag) {
                        z2 = true;
                    }
                    this.PasueFlag = z2;
                    this.vedio_pause.setVisibility(8);
                }
            } else if (this.mIDRIVERENUM == Mainboard.EIdriverEnum.DOWN.getCode() && !this.PasueFlag) {
                this.mediaPlayer.pause();
                this.mVedioBofang.setImageResource(R.drawable.shipin_xia_bofang);
                if (this.PasueFlag) {
                    z = false;
                }
                this.PasueFlag = z;
                this.vedio_pause.setVisibility(0);
            }
        } else if (msg.what == 1007) {
            this.info = this.mApp.carBaseInfo;
            if (!this.info.isiFlash()) {
                return;
            }
            if (this.info.isiLeftBackOpen() || this.info.isiLeftFrontOpen() || this.info.isiRightBackOpen() || this.info.isiRightFrontOpen()) {
                suoXiaoVedio();
                showOrFangSuo();
                showPlayControlBar();
                return;
            }
            setAutoYincanMuen();
        }
    }

    /* access modifiers changed from: private */
    public void press(int index) {
        boolean z = false;
        switch (index) {
            case 0:
                if (!this.isOpenSound) {
                    this.mSound.setImageResource(R.drawable.shipin_xia_mute);
                    if (!this.isOpenSound) {
                        z = true;
                    }
                    this.isOpenSound = z;
                    this.mediaPlayer.setVolume(0.0f, 0.0f);
                    break;
                } else {
                    this.mSound.setImageResource(R.drawable.shipin_xia_sound);
                    if (!this.isOpenSound) {
                        z = true;
                    }
                    this.isOpenSound = z;
                    this.mediaPlayer.setVolume(1.0f, 1.0f);
                    break;
                }
            case 1:
                this.mediaPlayer.seekTo(0);
                resetPositon();
                this.PasueFlag = true;
                this.mVedioBofang.setImageResource(R.drawable.shipin_xia_bofang);
                this.mediaPlayer.pause();
                break;
            case 2:
                resetPositon();
                if (this.currentPlayIndex <= 0) {
                    play(this.currentPlayIndex);
                    break;
                } else {
                    this.currentPlayIndex--;
                    play(this.currentPlayIndex);
                    break;
                }
            case 3:
                if (!this.PasueFlag) {
                    this.mediaPlayer.pause();
                    this.vedio_pause.setVisibility(0);
                    this.mVedioBofang.setImageResource(R.drawable.shipin_xia_bofang);
                    if (!this.PasueFlag) {
                        z = true;
                    }
                    this.PasueFlag = z;
                    break;
                } else {
                    this.mediaPlayer.start();
                    this.vedio_pause.setVisibility(8);
                    this.mVedioBofang.setImageResource(R.drawable.shipin_xia_zanting);
                    if (!this.PasueFlag) {
                        z = true;
                    }
                    this.PasueFlag = z;
                    requestAudioFocus();
                    break;
                }
            case 4:
                resetPositon();
                if (this.currentPlayIndex >= this.mediaBeans.size() - 1) {
                    play(this.currentPlayIndex);
                    break;
                } else {
                    this.currentPlayIndex++;
                    play(this.currentPlayIndex);
                    break;
                }
            case 5:
                this.countSufaceVieew++;
                showOrFangSuo();
                break;
            case 6:
                this.mMMainActivity.handleBackAction();
                break;
        }
        seclectTrue(this.indexCount);
    }

    private void seclectTrue(int seclectCuttun) {
        for (int i = 0; i < this.arr.length; i++) {
            if (seclectCuttun == i) {
                this.arr[i].setSelected(true);
            } else {
                this.arr[i].setSelected(false);
            }
        }
    }

    private void requestAudioFocus() {
        if (getActivity() != null) {
            this.mApp.musicType = 3;
            Log.e("requestAudioFocus", "flag" + ((AudioManager) getActivity().getSystemService("audio")).requestAudioFocus(this.mMainAudioFocusListener, 3, 1));
            MainService mainService = this.mApp.service;
        }
    }

    private void abandonAudioFocus() {
        if (getActivity() != null) {
            ((AudioManager) getActivity().getSystemService("audio")).abandonAudioFocus(this.mMainAudioFocusListener);
        }
    }
}
