package com.backaudio.android.driver.bluetooth.bc8mpprotocol;

public class SetPlayStatusProtocol extends AbstractLineProtocol {
    private static final String PAUSE = "PP";
    private static final String PLAY_NEXT = "FWD";
    private static final String PLAY_PREV = "BACK";
    private static final String STOP = "STOP";
    private boolean isSuccess = false;
    private ESetPlayStatus playStatus = null;

    public SetPlayStatusProtocol(String key, String value) {
        super(key, value);
        this.isSuccess = value.equals("0");
        if (key.equals(PAUSE)) {
            this.playStatus = ESetPlayStatus.PALY_OR_PAUSE;
        } else if (key.equals(STOP)) {
            this.playStatus = ESetPlayStatus.STOP;
        } else if (key.equals(PLAY_NEXT)) {
            this.playStatus = ESetPlayStatus.PLAY_NEXT;
        } else if (key.equals(PLAY_PREV)) {
            this.playStatus = ESetPlayStatus.PLAY_PREV;
        }
    }

    public ESetPlayStatus getPlayStatus() {
        return this.playStatus;
    }

    public boolean isSuccess() {
        return this.isSuccess;
    }
}
