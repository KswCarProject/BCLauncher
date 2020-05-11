package cn.kuwo.autosdk.api;

import cn.kuwo.autosdk.bean.Music;

public interface OnPlayerStatusListener {
    void onPlayerStatus(PlayerStatus playerStatus, Music music);
}
