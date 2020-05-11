package com.backaudio.android.driver.bluetooth.bc8mpprotocol;

public class MediaPlayStatusProtocol extends AbstractLineProtocol {
    private EMediaPlayStatus mediaPlayStatus;

    public MediaPlayStatusProtocol(String key, String value) {
        super(key, value);
        if (key.equals("A2DPSTAT")) {
            if (value.equals("0")) {
                this.mediaPlayStatus = EMediaPlayStatus.STOP;
            } else if (value.equals("3")) {
                this.mediaPlayStatus = EMediaPlayStatus.PLAYING;
            } else if (value.equals("2")) {
                this.mediaPlayStatus = EMediaPlayStatus.PAUSE;
            } else if (this.mediaPlayStatus == null) {
                this.mediaPlayStatus = EMediaPlayStatus.STOP;
            }
        } else if (!key.equals("PLAYSTAT")) {
        } else {
            if (value.equals("0")) {
                this.mediaPlayStatus = EMediaPlayStatus.STOP;
            } else if (value.equals("1")) {
                this.mediaPlayStatus = EMediaPlayStatus.PLAYING;
            } else if (value.equals("2")) {
                this.mediaPlayStatus = EMediaPlayStatus.PAUSE;
            }
        }
    }

    public void setMediaPlayStatus(EMediaPlayStatus mediaPlayStatus2) {
        this.mediaPlayStatus = mediaPlayStatus2;
    }

    public EMediaPlayStatus getMediaPlayStatus() {
        return this.mediaPlayStatus;
    }
}
