package com.touchus.benchilauncher.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import cn.kuwo.autosdk.api.PlayState;
import com.touchus.benchilauncher.LauncherApplication;

public class MusicControlReceiver extends BroadcastReceiver {
    int count = 0;

    public void onReceive(Context context, Intent intent) {
        LauncherApplication app = (LauncherApplication) context.getApplicationContext();
        String intentAction = intent.getAction();
        KeyEvent keyEvent = (KeyEvent) intent.getParcelableExtra("android.intent.extra.KEY_EVENT");
        Log.i("musicControl", "Action ---->" + intentAction + "  KeyEvent----->" + keyEvent.toString());
        Log.i("musicControl", "app.musicType ---->" + app.musicType);
        if ("android.intent.action.MEDIA_BUTTON".equals(intentAction)) {
            int keyCode = keyEvent.getKeyCode();
            if (keyEvent.getAction() != 0 || app.service == null) {
                return;
            }
            if (87 == keyCode) {
                if (app.musicType == 1) {
                    if (app.musicPlayControl != null) {
                        app.musicPlayControl.playNextMusic();
                    }
                } else if (app.musicType == 2) {
                    app.btservice.playNext();
                } else if (app.musicType == 5 && app.kwapi.isKuwoRunning(context)) {
                    app.kwapi.setPlayState(context, PlayState.STATE_NEXT);
                }
            } else if (127 == keyCode) {
                if (app.musicType == 1) {
                    if (app.musicPlayControl != null) {
                        app.musicPlayControl.pauseMusic();
                    }
                } else if (app.musicType == 2) {
                    app.btservice.pausePlaySync(false);
                } else if (app.musicType == 5 && app.kwapi.isKuwoRunning(context)) {
                    app.kwapi.setPlayState(context, PlayState.STATE_PAUSE);
                }
            } else if (126 == keyCode) {
                if (app.musicType == 1) {
                    if (app.musicPlayControl != null) {
                        app.musicPlayControl.replayMusic();
                    }
                } else if (app.musicType == 2) {
                    app.btservice.pausePlaySync(true);
                } else if (app.musicType == 5 && app.kwapi.isKuwoRunning(context)) {
                    if (this.count % 2 == 1) {
                        app.kwapi.setPlayState(context, PlayState.STATE_PAUSE);
                    } else {
                        app.kwapi.setPlayState(context, PlayState.STATE_PLAY);
                    }
                }
            } else if (88 == keyCode) {
                if (app.musicType == 1) {
                    if (app.musicPlayControl != null) {
                        app.musicPlayControl.playPreviousMusic();
                    }
                } else if (app.musicType == 2) {
                    app.btservice.playPrev();
                } else if (app.musicType == 5 && app.kwapi.isKuwoRunning(context)) {
                    app.kwapi.setPlayState(context, PlayState.STATE_PRE);
                }
            } else if (86 != keyCode) {
            } else {
                if (app.musicType == 1) {
                    if (app.musicPlayControl != null) {
                        app.musicPlayControl.stopMusic();
                    }
                } else if (app.musicType == 2) {
                    app.btservice.stopMusic();
                } else if (app.musicType == 5 && app.kwapi.isKuwoRunning(context)) {
                    app.kwapi.setPlayState(context, PlayState.STATE_PAUSE);
                }
            }
        }
    }
}
