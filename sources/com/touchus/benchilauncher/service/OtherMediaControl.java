package com.touchus.benchilauncher.service;

import android.app.ActivityManager;
import android.content.Context;
import android.media.AudioManager;
import android.text.TextUtils;
import android.util.Log;
import com.touchus.benchilauncher.LauncherApplication;
import java.util.List;
import javax.mail.internet.HeaderTokenizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OtherMediaControl {
    private LauncherApplication app = LauncherApplication.getInstance();
    private Context context = LauncherApplication.getContext();
    Logger logger = LoggerFactory.getLogger(OtherMediaControl.class);
    private AudioManager.OnAudioFocusChangeListener mOtherMediaAudioFocusListener = new AudioManager.OnAudioFocusChangeListener() {
        public void onAudioFocusChange(int focusChange) {
            switch (focusChange) {
                case HeaderTokenizer.Token.COMMENT /*-3*/:
                    OtherMediaControl.this.logger.debug(String.valueOf(OtherMediaControl.this.packageName) + " requestAudioFocus OtherMedia CAN_DUCK " + OtherMediaControl.this.type);
                    return;
                case -2:
                    OtherMediaControl.this.logger.debug(String.valueOf(OtherMediaControl.this.packageName) + " requestAudioFocus OtherMedia TRANSIENT " + OtherMediaControl.this.type);
                    return;
                case -1:
                    OtherMediaControl.this.logger.debug(String.valueOf(OtherMediaControl.this.packageName) + " requestAudioFocus OtherMedia LOSS " + OtherMediaControl.this.type);
                    OtherMediaControl.this.abandonOtherMediaAudioFocus();
                    OtherMediaControl.this.stopOtherAPP(OtherMediaControl.this.packageName);
                    return;
                case 1:
                    OtherMediaControl.this.logger.debug(String.valueOf(OtherMediaControl.this.packageName) + " requestAudioFocus OtherMedia GAIN " + OtherMediaControl.this.type);
                    return;
                default:
                    return;
            }
        }
    };
    /* access modifiers changed from: private */
    public String packageName;
    /* access modifiers changed from: private */
    public int type;

    public OtherMediaControl(String packagename, int type2) {
        this.packageName = packagename;
        this.type = type2;
        requestOtherMediaAudioFocus();
    }

    public void requestOtherMediaAudioFocus() {
        if (this.app.musicType != this.type) {
            this.app.musicType = this.type;
            Log.e(String.valueOf(this.packageName) + " OtherMedia requestAudioFocus", "app.musicType = " + this.app.musicType + ",flag" + ((AudioManager) this.context.getSystemService("audio")).requestAudioFocus(this.mOtherMediaAudioFocusListener, 3, 1));
        }
    }

    public void abandonOtherMediaAudioFocus() {
        ((AudioManager) this.context.getSystemService("audio")).abandonAudioFocus(this.mOtherMediaAudioFocusListener);
    }

    public void stopOtherAPP(String packagename) {
        ActivityManager mActivityManager = (ActivityManager) this.context.getSystemService("activity");
        List<ActivityManager.RunningTaskInfo> runningTaskInfos = mActivityManager.getRunningTasks(10);
        if (runningTaskInfos != null) {
            for (int i = 0; i < runningTaskInfos.size(); i++) {
                String cmpNameTemp = runningTaskInfos.get(i).topActivity.getPackageName();
                Log.e("", "enteracc start stop cmpNameTemp = " + cmpNameTemp);
                if (!TextUtils.isEmpty(cmpNameTemp) && packagename.equals(cmpNameTemp)) {
                    mActivityManager.forceStopPackage(cmpNameTemp);
                }
            }
        }
    }
}
