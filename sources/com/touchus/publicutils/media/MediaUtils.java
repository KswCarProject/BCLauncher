package com.touchus.publicutils.media;

import android.media.MediaPlayer;
import android.os.Handler;
import android.util.Log;

public class MediaUtils {
    /* access modifiers changed from: private */
    public static float mFromValume;
    static Handler mHandler = new Handler();
    /* access modifiers changed from: private */
    public static float mLastValume;
    /* access modifiers changed from: private */
    public static MediaPlayer mMediaPlayer;
    /* access modifiers changed from: private */
    public static float mToValume;
    static Runnable mediaFlatThread = new Runnable() {
        public void run() {
            Log.e("MediaUtils", "mediaFlatThread mFromValume = " + MediaUtils.mFromValume + ",mToValume = " + MediaUtils.mToValume);
            if (MediaUtils.mFromValume <= MediaUtils.mToValume) {
                MediaUtils.mMediaPlayer.setVolume(MediaUtils.mToValume, MediaUtils.mToValume);
                return;
            }
            MediaUtils.mFromValume = MediaUtils.mFromValume - 0.1f;
            MediaUtils.mLastValume = MediaUtils.mFromValume;
            MediaUtils.mMediaPlayer.setVolume(MediaUtils.mFromValume, MediaUtils.mFromValume);
            MediaUtils.mHandler.postDelayed(MediaUtils.mediaFlatThread, 200);
        }
    };
    static Runnable mediaNorThread = new Runnable() {
        public void run() {
            Log.e("MediaUtils", "mediaNorThread mFromValume = " + MediaUtils.mFromValume + ",mToValume = " + MediaUtils.mToValume);
            if (MediaUtils.mFromValume >= MediaUtils.mToValume) {
                MediaUtils.mMediaPlayer.setVolume(MediaUtils.mToValume, MediaUtils.mToValume);
                return;
            }
            MediaUtils.mFromValume = MediaUtils.mFromValume + 0.1f;
            MediaUtils.mLastValume = MediaUtils.mFromValume;
            MediaUtils.mMediaPlayer.setVolume(MediaUtils.mFromValume, MediaUtils.mFromValume);
            MediaUtils.mHandler.postDelayed(MediaUtils.mediaNorThread, 350);
        }
    };

    public static void removeCallbacks() {
        mHandler.removeCallbacks(mediaFlatThread);
        mHandler.removeCallbacks(mediaNorThread);
    }

    public static void setMusicFlat(MediaPlayer mediaPlayer, float fromValume, float toValume) {
        mMediaPlayer = mediaPlayer;
        mFromValume = fromValume;
        mToValume = toValume;
        Log.e("MediaUtils", "setMusicFlat mFromValume = " + mFromValume + ",mToValume = " + toValume);
        if (fromValume < toValume) {
            if (fromValume < mLastValume) {
                mFromValume = mLastValume;
            }
            mHandler.removeCallbacks(mediaFlatThread);
            mHandler.post(mediaNorThread);
            return;
        }
        if (fromValume > mLastValume) {
            mFromValume = mLastValume;
        }
        mHandler.removeCallbacks(mediaNorThread);
        mHandler.post(mediaFlatThread);
    }
}
