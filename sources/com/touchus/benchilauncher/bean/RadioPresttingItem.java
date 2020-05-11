package com.touchus.benchilauncher.bean;

public class RadioPresttingItem {
    public boolean bk;
    public String channel;
    public boolean voice;

    public RadioPresttingItem(boolean visible, boolean bk2, String channel2) {
        this.voice = visible;
        this.bk = bk2;
        this.channel = channel2;
    }
}
