package cn.kuwo.autosdk.api;

import android.app.ActivityManager;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;
import cn.kuwo.autosdk.bean.Music;
import cn.kuwo.autosdk.g;
import cn.kuwo.autosdk.h;
import cn.kuwo.autosdk.n;
import cn.kuwo.autosdk.u;
import cn.kuwo.autosdk.v;
import java.util.List;
import org.apache.log4j.Priority;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class KWAPI {
    private static final String AUTO_PLAY = "auto_play";
    private static final String CLOSE_DESKLYRIC = "cn.kuwo.kwmusicauto.action.CLOSE_DESKLYRIC";
    private static final String CLOSE_TOAST = "cn.kuwo.kwmusicauto.action.CLOSE_TOAST";
    private static final String ENDTYPE = "ENDTYPE";
    private static final String EXIT = "cn.kuwo.kwmusicauto.action.EXIT";
    private static final String EXIT_KWMUSICAPP = "cn.kuwo.kwmusicauto.action.EXITAPP";
    private static final String EXIT_PLAY_MUSIC = "exit_play_music";
    private static final String EXTRA = "EXTRA";
    private static final String HAS_MV = "hasMv";
    private static final String KEY_FULL_SCREEN = "key_full_screen";
    private static final String KUWO_CARMUSIC_MEDIABUTTON_ACTION = "cn.kuwo.kwmusicauto.action.MEDIA_BUTTON";
    private static final String KUWO_KEY = "kuwo_key";
    private static final String KW_CAR_PROCESS_NAME = "cn.kuwo.kwmusiccar";
    private static final String MUSIC = "music";
    private static final String MUSIC_ALBUM = "album";
    private static final String MUSIC_ALL_LIST_KEY = "music_all_list_key";
    private static final String MUSIC_ARTISTID = "artistid";
    private static final String MUSIC_INDEX_KEY = "music_index_key";
    private static final String MUSIC_NAME = "name";
    private static final String MUSIC_RID = "rid";
    private static final String MUSIC_SINGER = "singer";
    private static final String MUSIC_SOURCE = "source";
    private static final String MV_QUALITY = "mvQuality";
    private static final String NoFoundAPP = "未找到可用的安装程序！";
    private static final String OPEN_DESKLYRIC = "cn.kuwo.kwmusicauto.action.OPEN_DESKLYRIC";
    private static final String OPEN_TOAST = "cn.kuwo.kwmusicauto.action.OPEN_TOAST";
    private static final String PALY_MUSIC = "cn.kuwo.kwmusicauto.action.PLAY_MUSIC";
    private static final String PLAYERMODE = "PLAYERMODE";
    private static final String PLAYERSTATUS = "PLAYERSTATUS";
    private static final String PLAYER_MODE = "cn.kuwo.kwmusicauto.action.PLAYER_MODE";
    private static final String PLAYER_STATUS = "cn.kuwo.kwmusicauto.action.PLAYER_STATUS";
    private static final String PLAYMUSIC_ALBUM = "play_music_album";
    private static final String PLAYMUSIC_ARTIST = "play_music_artist";
    private static final String PLAYMUSIC_NAME = "play_music_name";
    private static final String PLAY_ALL_MUSIC = "cn.kuwo.kwmusicauto.action.PLAY_ALL_MUSIC";
    private static final String PLAY_END = "cn.kuwo.kwmusicauto.action.PLAY_END";
    private static final String SEARCH_MUSIC = "cn.kuwo.kwmusicauto.action.SEARCH_MUSIC";
    private static final String SET_FULL_SCREEN = "cn.kuwo.kwmusicauto.action.SET_FULL_SCREEN";
    private static final String START_KWMUSICAPP = "cn.kuwo.kwmusicauto.action.STARTAPP";
    private static final CarExitReceiver mCarExitReceiver = new CarExitReceiver((CarExitReceiver) null);
    private static final CarPlayerModeReceiver mCarPlayerModeReceiver = new CarPlayerModeReceiver((CarPlayerModeReceiver) null);
    private static String mKey = "";
    /* access modifiers changed from: private */
    public static KWAPI mKwapi = null;
    private static final CarPlayEndReceiver mPlayEndReceiver = new CarPlayEndReceiver((CarPlayEndReceiver) null);
    private static final CarPlayerStatusReceiver mPlayerStatusReceiver = new CarPlayerStatusReceiver((CarPlayerStatusReceiver) null);
    private static g mSearchMgr = null;
    /* access modifiers changed from: private */
    public OnExitListener mExitListener = null;
    private boolean mExitRegistered = false;
    private OnPlayEndListener mPlayEndListener = null;
    private boolean mPlayEndRegistered = false;
    /* access modifiers changed from: private */
    public OnPlayerModeListener mPlayerModeListener = null;
    private boolean mPlayerModeRegistered = false;
    private OnPlayerStatusListener mPlayerStatusListener = null;
    private boolean mPlayerStatusRegistered = false;

    class CarExitReceiver extends BroadcastReceiver {
        private CarExitReceiver() {
        }

        /* synthetic */ CarExitReceiver(CarExitReceiver carExitReceiver) {
            this();
        }

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (!TextUtils.isEmpty(action) && KWAPI.mKwapi != null && action.equals(KWAPI.EXIT) && KWAPI.mKwapi.mExitListener != null) {
                KWAPI.mKwapi.mExitListener.onExit();
            }
        }
    }

    class CarPlayEndReceiver extends BroadcastReceiver {
        private CarPlayEndReceiver() {
        }

        /* synthetic */ CarPlayEndReceiver(CarPlayEndReceiver carPlayEndReceiver) {
            this();
        }

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (!TextUtils.isEmpty(action) && KWAPI.mKwapi != null && action.equals(KWAPI.PLAY_END) && KWAPI.mKwapi.getOnPlayEndListener() != null) {
                int intExtra = intent.getIntExtra(KWAPI.ENDTYPE, 0);
                PlayEndType playEndType = PlayEndType.END_COMPLETE;
                if (intExtra == 1) {
                    playEndType = PlayEndType.END_USER;
                } else if (intExtra == 2) {
                    playEndType = PlayEndType.END_ERROR;
                }
                KWAPI.mKwapi.getOnPlayEndListener().onPlayEnd(playEndType);
            }
        }
    }

    class CarPlayerModeReceiver extends BroadcastReceiver {
        private CarPlayerModeReceiver() {
        }

        /* synthetic */ CarPlayerModeReceiver(CarPlayerModeReceiver carPlayerModeReceiver) {
            this();
        }

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (!TextUtils.isEmpty(action) && KWAPI.mKwapi != null && action.equals(KWAPI.PLAYER_MODE) && KWAPI.mKwapi.mPlayerModeListener != null) {
                KWAPI.mKwapi.mPlayerModeListener.onPlayerMode(intent.getIntExtra(KWAPI.PLAYERMODE, 0));
            }
        }
    }

    class CarPlayerStatusReceiver extends BroadcastReceiver {
        private CarPlayerStatusReceiver() {
        }

        /* synthetic */ CarPlayerStatusReceiver(CarPlayerStatusReceiver carPlayerStatusReceiver) {
            this();
        }

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (!TextUtils.isEmpty(action) && KWAPI.mKwapi != null && action.equals(KWAPI.PLAYER_STATUS) && KWAPI.mKwapi.getOnPlayerStatusListener() != null) {
                int intExtra = intent.getIntExtra(KWAPI.PLAYERSTATUS, 0);
                PlayerStatus playerStatus = PlayerStatus.INIT;
                if (intExtra == 1) {
                    playerStatus = PlayerStatus.PLAYING;
                } else if (intExtra == 2) {
                    playerStatus = PlayerStatus.BUFFERING;
                } else if (intExtra == 3) {
                    playerStatus = PlayerStatus.PAUSE;
                } else if (intExtra == 4) {
                    playerStatus = PlayerStatus.STOP;
                }
                String stringExtra = intent.getStringExtra(KWAPI.PLAYMUSIC_NAME);
                String stringExtra2 = intent.getStringExtra(KWAPI.PLAYMUSIC_ARTIST);
                String stringExtra3 = intent.getStringExtra(KWAPI.PLAYMUSIC_ALBUM);
                Music music = null;
                if (stringExtra != null) {
                    music = new Music();
                    music.name = stringExtra;
                    music.album = stringExtra3;
                    music.artist = stringExtra2;
                }
                KWAPI.mKwapi.getOnPlayerStatusListener().onPlayerStatus(playerStatus, music);
            }
        }
    }

    private KWAPI() {
    }

    private boolean checkInstalledPackage(Context context, String str) {
        PackageInfo packageInfo;
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        try {
            packageInfo = context.getPackageManager().getPackageInfo(str, 0);
        } catch (PackageManager.NameNotFoundException e) {
            packageInfo = null;
        }
        return packageInfo != null;
    }

    public static KWAPI createKWAPI(Context context, String str) {
        if (mKwapi == null || TextUtils.isEmpty(mKey)) {
            n.a(context, str);
            mKwapi = new KWAPI();
            mKey = str;
        }
        return mKwapi;
    }

    /* access modifiers changed from: private */
    public OnPlayEndListener getOnPlayEndListener() {
        return this.mPlayEndListener;
    }

    /* access modifiers changed from: private */
    public OnPlayerStatusListener getOnPlayerStatusListener() {
        return this.mPlayerStatusListener;
    }

    private String packJson(List list) {
        JSONArray jSONArray = new JSONArray();
        int i = 0;
        while (true) {
            int i2 = i;
            if (i2 >= list.size()) {
                return jSONArray.toString();
            }
            JSONObject jSONObject = new JSONObject();
            try {
                jSONObject.put(MUSIC_RID, ((Music) list.get(i2)).rid);
                jSONObject.put("name", ((Music) list.get(i2)).name);
                jSONObject.put(MUSIC_SINGER, ((Music) list.get(i2)).artist);
                jSONObject.put(MUSIC_ALBUM, ((Music) list.get(i2)).album);
                jSONObject.put(MUSIC_ARTISTID, ((Music) list.get(i2)).artistId);
                jSONObject.put(MV_QUALITY, ((Music) list.get(i2)).mvQuality);
                jSONObject.put(HAS_MV, ((Music) list.get(i2)).hasMv);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            jSONArray.put(jSONObject);
            i = i2 + 1;
        }
    }

    private void registerExitReceiver(Context context) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.setPriority(Priority.OFF_INT);
        intentFilter.addAction(EXIT);
        context.registerReceiver(mCarExitReceiver, intentFilter);
    }

    private void registerPlayEndReceiver(Context context) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(PLAY_END);
        context.registerReceiver(mPlayEndReceiver, intentFilter);
    }

    private void registerPlayerModeReceiver(Context context) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.setPriority(Priority.OFF_INT);
        intentFilter.addAction(PLAYER_MODE);
        context.registerReceiver(mCarPlayerModeReceiver, intentFilter);
    }

    private void registerPlayerStatusReceiver(Context context) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.setPriority(Priority.OFF_INT);
        intentFilter.addAction(PLAYER_STATUS);
        context.registerReceiver(mPlayerStatusReceiver, intentFilter);
    }

    private void searchOnlineMusic(String str, SearchMode searchMode, OnSearchListener onSearchListener) {
        if (mSearchMgr == null) {
            mSearchMgr = new h();
        }
        mSearchMgr.a(str, searchMode, onSearchListener);
    }

    private void unRegisterExitReceiver(Context context) {
        context.unregisterReceiver(mCarExitReceiver);
    }

    private void unRegisterPlayEndReceiver(Context context) {
        context.unregisterReceiver(mPlayEndReceiver);
    }

    private void unRegisterPlayerModeReceiver(Context context) {
        context.unregisterReceiver(mCarPlayerModeReceiver);
    }

    private void unRegisterPlayerStatusReceiver(Context context) {
        context.unregisterReceiver(mPlayerStatusReceiver);
    }

    public void closeToast(Context context) {
        Intent intent = new Intent(CLOSE_TOAST);
        intent.putExtra(KUWO_KEY, mKey);
        context.sendBroadcast(intent);
    }

    public void exitAPP(Context context) {
        Intent intent = new Intent(EXIT_KWMUSICAPP);
        intent.putExtra(KUWO_KEY, mKey);
        context.sendBroadcast(intent);
    }

    public boolean isKuwoRunning(Context context) {
        if (Build.VERSION.SDK_INT < 22) {
            List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = ((ActivityManager) context.getSystemService("activity")).getRunningAppProcesses();
            if (runningAppProcesses == null || runningAppProcesses.size() == 0) {
                return false;
            }
            for (int i = 0; i < runningAppProcesses.size(); i++) {
                if (KW_CAR_PROCESS_NAME.equals(runningAppProcesses.get(i).processName)) {
                    return true;
                }
            }
            return false;
        }
        List<v> a = u.a();
        if (a == null) {
            return false;
        }
        for (v vVar : a) {
            if (KW_CAR_PROCESS_NAME.equals(vVar.e)) {
                return true;
            }
        }
        return false;
    }

    public void openToast(Context context) {
        Intent intent = new Intent(OPEN_TOAST);
        intent.putExtra(KUWO_KEY, mKey);
        context.sendBroadcast(intent);
    }

    public void playClientMusics(Context context, String str, String str2, String str3) {
        Intent intent = new Intent(SEARCH_MUSIC);
        intent.setFlags(268435456);
        Bundle bundle = new Bundle();
        bundle.putString("name", str);
        bundle.putString(MUSIC_SINGER, str2);
        bundle.putString(MUSIC_ALBUM, str3);
        bundle.putString(KUWO_KEY, mKey);
        intent.putExtras(bundle);
        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, NoFoundAPP, 1).show();
        }
    }

    public void playLocalMusic(Context context, String str) {
        Intent intent = new Intent(SEARCH_MUSIC);
        intent.setFlags(268435456);
        Bundle bundle = new Bundle();
        bundle.putString(MUSIC_SOURCE, str);
        bundle.putString(KUWO_KEY, mKey);
        intent.putExtras(bundle);
        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, NoFoundAPP, 1).show();
        }
    }

    public void playMusic(Context context, List list, int i, boolean z, boolean z2) {
        if (list == null) {
            Toast.makeText(context, "播放的歌曲信息有误！", 1).show();
        } else if (i < list.size()) {
            Intent intent = new Intent(PLAY_ALL_MUSIC);
            intent.setFlags(268435456);
            Bundle bundle = new Bundle();
            bundle.putString(MUSIC_ALL_LIST_KEY, packJson(list));
            bundle.putString(KUWO_KEY, mKey);
            bundle.putInt(MUSIC_INDEX_KEY, i);
            if (z) {
                try {
                    bundle.putBoolean(EXIT_PLAY_MUSIC, z2);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(context, NoFoundAPP, 1).show();
                }
            } else if (isKuwoRunning(context)) {
                intent.putExtras(bundle);
                context.sendBroadcast(intent);
            } else {
                bundle.putBoolean(EXIT_PLAY_MUSIC, true);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        } else {
            Toast.makeText(context, "播放的歌曲位置有误！", 1).show();
        }
    }

    public void registerExitListener(Context context, OnExitListener onExitListener) {
        if (!this.mExitRegistered) {
            registerExitReceiver(context);
            this.mExitRegistered = true;
        }
        this.mExitListener = onExitListener;
    }

    public void registerPlayEndListener(Context context, OnPlayEndListener onPlayEndListener) {
        if (!this.mPlayEndRegistered) {
            registerPlayEndReceiver(context);
            this.mPlayEndRegistered = true;
        }
        this.mPlayEndListener = onPlayEndListener;
    }

    public void registerPlayerModeListener(Context context, OnPlayerModeListener onPlayerModeListener) {
        if (!this.mPlayerModeRegistered) {
            registerPlayerModeReceiver(context);
            this.mPlayerModeRegistered = true;
        }
        this.mPlayerModeListener = onPlayerModeListener;
    }

    public void registerPlayerStatusListener(Context context, OnPlayerStatusListener onPlayerStatusListener) {
        if (!this.mPlayerStatusRegistered) {
            registerPlayerStatusReceiver(context);
            this.mPlayerStatusRegistered = true;
        }
        this.mPlayerStatusListener = onPlayerStatusListener;
    }

    public void searchOnlineMusic(String str, OnSearchListener onSearchListener) {
        searchOnlineMusic(str, SearchMode.ALL, onSearchListener);
    }

    public void setFullScreen(Context context, boolean z) {
        Intent intent = new Intent(SET_FULL_SCREEN);
        intent.putExtra(KEY_FULL_SCREEN, z);
        context.sendBroadcast(intent);
    }

    public void setPlayMode(Context context, PlayMode playMode) {
        Intent intent = new Intent(KUWO_CARMUSIC_MEDIABUTTON_ACTION);
        intent.putExtra(EXTRA, playMode.getPlayMode());
        intent.putExtra(KUWO_KEY, mKey);
        context.sendBroadcast(intent);
    }

    public void setPlayState(Context context, PlayState playState) {
        Intent intent = new Intent(KUWO_CARMUSIC_MEDIABUTTON_ACTION);
        intent.putExtra(EXTRA, playState.getPlayState());
        intent.putExtra(KUWO_KEY, mKey);
        context.sendBroadcast(intent);
    }

    public void startAPP(Context context, boolean z) {
        Intent intent = new Intent(START_KWMUSICAPP);
        intent.setFlags(268435456);
        Bundle bundle = new Bundle();
        bundle.putString(KUWO_KEY, mKey);
        bundle.putBoolean(AUTO_PLAY, z);
        intent.putExtras(bundle);
        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, NoFoundAPP, 1).show();
        }
    }

    public void unRegisterExitListener(Context context) {
        if (this.mExitRegistered) {
            unRegisterExitReceiver(context);
            this.mExitRegistered = false;
        }
    }

    public void unRegisterPlayEndListener(Context context) {
        if (this.mPlayEndRegistered) {
            unRegisterPlayEndReceiver(context);
            this.mPlayEndRegistered = false;
        }
    }

    public void unRegisterPlayerModeListener(Context context) {
        if (this.mPlayerModeRegistered) {
            unRegisterPlayerModeReceiver(context);
            this.mPlayerModeRegistered = false;
        }
    }

    public void unRegisterPlayerStatusListener(Context context) {
        if (this.mPlayerStatusRegistered) {
            unRegisterPlayerStatusReceiver(context);
            this.mPlayerStatusRegistered = false;
        }
    }
}
