package com.touchus.benchilauncher.service;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.util.Log;
import com.touchus.benchilauncher.LauncherApplication;
import com.touchus.benchilauncher.SysConst;
import com.touchus.publicutils.bean.MediaBean;
import com.touchus.publicutils.media.MediaUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.mail.internet.HeaderTokenizer;

public class MusicPlayControl {
    public static final int PLAY_LOOP_LOOP = 1001;
    public static final int PLAY_LOOP_RANDOM = 1003;
    public static final int PLAY_SINGE_LOOP = 1002;
    private Context context;
    public MediaBean currentMusic = null;
    public String currentPlayPath;
    public boolean iFlatAudioFocus = false;
    public boolean iHoldAudioFocus = false;
    public boolean iIsManualPause = false;
    public boolean iNeedUpsetPlayOrder = false;
    /* access modifiers changed from: private */
    public LauncherApplication mApp;
    private AudioManager.OnAudioFocusChangeListener mAudioFocusListener = new AudioManager.OnAudioFocusChangeListener() {
        public void onAudioFocusChange(int focusChange) {
            switch (focusChange) {
                case HeaderTokenizer.Token.COMMENT /*-3*/:
                    Log.d("requestAudioFocus", "music  AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK");
                    MusicPlayControl.this.setHoldAudioFocus(false, false);
                    MusicPlayControl.this.iFlatAudioFocus = true;
                    if (SysConst.isBT() || MusicPlayControl.this.mApp.ismix) {
                        MediaUtils.setMusicFlat(MusicPlayControl.this.musicPlayer, SysConst.musicNorVolume, SysConst.musicDecVolume);
                        return;
                    } else if (MusicPlayControl.this.isMusicPlaying()) {
                        MusicPlayControl.this.pauseMusic();
                        return;
                    } else {
                        return;
                    }
                case -2:
                    Log.d("requestAudioFocus", "music  AudioManager.AUDIOFOCUS_LOSS_TRANSIENT");
                    MusicPlayControl.this.setHoldAudioFocus(true, false);
                    if (MusicPlayControl.this.isMusicPlaying()) {
                        MusicPlayControl.this.pauseMusic();
                        return;
                    }
                    return;
                case -1:
                    Log.d("requestAudioFocus", "music AudioManager.AUDIOFOCUS_LOSS   iHoldAudioFocus:" + MusicPlayControl.this.iHoldAudioFocus);
                    MusicPlayControl.this.stopLocalMusic();
                    return;
                case 1:
                    Log.d("requestAudioFocus", "music  AudioManager.AUDIOFOCUS_GAIN");
                    MediaUtils.setMusicFlat(MusicPlayControl.this.musicPlayer, SysConst.musicDecVolume, SysConst.musicNorVolume);
                    MusicPlayControl.this.setHoldAudioFocus(true, true);
                    MusicPlayControl.this.mApp.musicType = 1;
                    if (!MusicPlayControl.this.mPause) {
                        MusicPlayControl.this.replayMusic();
                    }
                    MusicPlayControl.this.iFlatAudioFocus = false;
                    return;
                default:
                    return;
            }
        }
    };
    public AudioManager mAudioManager;
    public Handler mHandler = null;
    /* access modifiers changed from: private */
    public boolean mPause;
    public int musicIndex = -1;
    private List<MediaBean> musicList = new ArrayList();
    /* access modifiers changed from: private */
    public MediaPlayer musicPlayer = null;
    private int playLoopType = 1001;

    public MusicPlayControl(Context cont) {
        this.context = cont;
        this.mApp = (LauncherApplication) cont.getApplicationContext();
        this.musicPlayer = new MediaPlayer();
        this.mAudioManager = (AudioManager) this.context.getSystemService("audio");
    }

    public void destroy() {
        if (this.mHandler != null) {
            this.mHandler.removeCallbacksAndMessages((Object) null);
        }
        unrequestMusicAudioFocus();
        setHoldAudioFocus(false, true);
        this.musicPlayer.release();
    }

    public boolean isMusicPlaying() {
        return LauncherApplication.iPlaying;
    }

    public int getPosition() {
        try {
            return this.musicPlayer.getCurrentPosition();
        } catch (Exception e) {
            return 0;
        }
    }

    public void setPlayList(List<MediaBean> list) {
        this.musicList = list;
    }

    public List<MediaBean> getMusicList() {
        return this.musicList;
    }

    public synchronized void playMusic(int position) {
        try {
            if (this.musicList != null && this.musicList.size() > 0) {
                if (position != -1) {
                    if (this.musicIndex != position) {
                        this.musicIndex = position;
                    }
                }
                if (this.musicIndex >= this.musicList.size()) {
                    this.musicIndex = 0;
                }
                LauncherApplication.musicIndex = this.musicIndex;
                LauncherApplication.mSpUtils.putInt(SysConst.FLAG_MUSIC_POS, this.musicIndex);
                play(position);
                requestAudioFocus();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return;
    }

    public void playNextMusic() {
        if (this.musicList != null && this.musicList.size() > 0) {
            if (this.playLoopType == 1003) {
                randomMusic();
            } else {
                this.musicIndex++;
                this.musicIndex = this.musicIndex < this.musicList.size() ? this.musicIndex : 0;
            }
            playMusic(this.musicIndex);
        }
    }

    public void playPreviousMusic() {
        if (this.musicList != null && this.musicList.size() > 0) {
            if (this.playLoopType == 1003) {
                randomMusic();
            } else {
                this.musicIndex--;
                this.musicIndex = this.musicIndex >= 0 ? this.musicIndex : this.musicList.size() - 1;
            }
            playMusic(this.musicIndex);
        }
    }

    private void randomMusic() {
        int size = this.musicList.size();
        if (size <= 1) {
            this.musicIndex = 0;
            return;
        }
        Random rdm = new Random();
        int currentIndex = rdm.nextInt(size);
        while (currentIndex == this.musicIndex) {
            currentIndex = rdm.nextInt(size);
        }
        this.musicIndex = currentIndex;
    }

    public void pauseMusic() {
        try {
            this.musicPlayer.pause();
            settingPlayState(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopMusic() {
        try {
            this.musicPlayer.stop();
            settingPlayState(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void replayMusic() {
        if (!this.iHoldAudioFocus) {
            pauseMusic();
            return;
        }
        try {
            this.musicPlayer.start();
            if (!this.iFlatAudioFocus) {
                requestAudioFocus();
            }
            settingPlayState(true);
        } catch (Exception e) {
        }
    }

    public int getPlayLoopType() {
        return this.playLoopType;
    }

    public void setPlayLoopType(int type) {
        this.playLoopType = type;
    }

    public void changeCurrentPlayProgress(int progress) {
        this.musicPlayer.seekTo(progress);
    }

    public void setPauseState(boolean flag) {
        this.mPause = flag;
    }

    public void settingPlayState(boolean flag) {
        if (flag && LauncherApplication.iPlaying) {
            return;
        }
        if (flag || LauncherApplication.iPlaying) {
            LauncherApplication.iPlaying = flag;
            if (this.mHandler != null) {
                this.mHandler.obtainMessage(SysConst.UPDATE_MUSIC_PLAY_STATE).sendToTarget();
            }
        }
    }

    /* access modifiers changed from: private */
    public int getNextIndex() {
        int index = this.musicIndex;
        if (this.playLoopType == 1001) {
            int index2 = index + 1;
            if (index2 < this.musicList.size()) {
                return index2;
            }
            return 0;
        } else if (this.playLoopType != 1003) {
            return index;
        } else {
            int size = this.musicList.size();
            if (size <= 1) {
                return 0;
            }
            Random rdm = new Random();
            int currentIndex = rdm.nextInt(size);
            while (currentIndex == index) {
                currentIndex = rdm.nextInt(size);
            }
            return currentIndex;
        }
    }

    private void play(int position) throws IOException {
        this.currentMusic = this.musicList.get(position);
        settingPlayState(false);
        if (this.musicPlayer != null) {
            this.musicPlayer.stop();
            this.musicPlayer.release();
            this.musicPlayer = null;
            MediaUtils.removeCallbacks();
            this.musicPlayer = new MediaPlayer();
        }
        this.musicPlayer.setDataSource(this.currentMusic.getData());
        this.musicPlayer.setAudioStreamType(3);
        this.musicPlayer.setLooping(false);
        this.musicPlayer.prepareAsync();
        this.musicPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            public void onPrepared(MediaPlayer mp) {
                mp.start();
                MusicPlayControl.this.settingPlayState(true);
            }
        });
        this.musicPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                MusicPlayControl.this.musicIndex = MusicPlayControl.this.getNextIndex();
                MusicPlayControl.this.playMusic(MusicPlayControl.this.musicIndex);
            }
        });
        this.musicPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            public boolean onError(MediaPlayer mp, int what, int extra) {
                mp.reset();
                return false;
            }
        });
        if (this.mHandler != null) {
            this.mHandler.obtainMessage(SysConst.UPDATE_CUR_MUSIC_PLAY_INFO).sendToTarget();
        }
    }

    /* access modifiers changed from: private */
    public synchronized void setHoldAudioFocus(boolean flag, boolean iSetLongFocusValue) {
        this.iHoldAudioFocus = flag;
    }

    public synchronized void setHoldAudioFocus(boolean flag) {
        this.iHoldAudioFocus = flag;
    }

    public void requestAudioFocus() {
        this.mApp.musicType = 1;
        int flag = this.mAudioManager.requestAudioFocus(this.mAudioFocusListener, 3, 1);
        Log.e("Music requestAudioFocus", "flag" + flag);
        if (flag == 1) {
            setHoldAudioFocus(true, true);
        } else if (this.mAudioManager.requestAudioFocus(this.mAudioFocusListener, 3, 1) == 1) {
            setHoldAudioFocus(true, true);
        } else {
            setHoldAudioFocus(false, true);
        }
        LauncherApplication.iPlayingAuto = true;
    }

    public void unrequestMusicAudioFocus() {
        this.mAudioManager.abandonAudioFocus(this.mAudioFocusListener);
    }

    public void stopLocalMusic() {
        setHoldAudioFocus(false, true);
        if (this.mApp.musicPlayControl != null && isMusicPlaying()) {
            pauseMusic();
            settingPlayState(false);
            destroy();
            this.mPause = false;
            MediaUtils.removeCallbacks();
            this.mApp.musicPlayControl = null;
            saveMusicDate();
            LauncherApplication.iPlayingAuto = false;
        }
        unrequestMusicAudioFocus();
    }

    private void saveMusicDate() {
    }
}
