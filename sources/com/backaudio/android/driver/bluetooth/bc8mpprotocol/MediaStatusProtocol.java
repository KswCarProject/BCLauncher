package com.backaudio.android.driver.bluetooth.bc8mpprotocol;

public class MediaStatusProtocol extends AbstractLineProtocol {
    private EMediaStatus mediaStatus;

    public MediaStatusProtocol(String key, String value) {
        super(key, value);
        if ("0".equals(value)) {
            this.mediaStatus = EMediaStatus.UNCONNECT;
        } else if ("1".equals(value)) {
            this.mediaStatus = EMediaStatus.CONNECTIING;
        } else if ("2".equals(value)) {
            this.mediaStatus = EMediaStatus.CONNECTED;
        }
    }

    public void setMediaStatus(EMediaStatus mediaStatus2) {
        this.mediaStatus = mediaStatus2;
    }

    public EMediaStatus getMediaStatus() {
        return this.mediaStatus;
    }
}
