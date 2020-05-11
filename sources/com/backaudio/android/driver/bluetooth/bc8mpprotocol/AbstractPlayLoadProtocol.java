package com.backaudio.android.driver.bluetooth.bc8mpprotocol;

import java.io.ByteArrayOutputStream;

public class AbstractPlayLoadProtocol extends AbstractLineProtocol {
    private int needDataLength = 0;
    private ByteArrayOutputStream playload;

    public AbstractPlayLoadProtocol(String key, String value) {
        super(key, value);
        try {
            this.needDataLength = Integer.parseInt(value);
            this.playload = new ByteArrayOutputStream(this.needDataLength);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public int getNeedDataLength() {
        return this.needDataLength;
    }

    public void push(byte[] buffer, int off, int len) {
        this.playload.write(buffer, off, len);
        this.needDataLength -= len;
    }

    public byte[] getPlayload() {
        return this.playload.toByteArray();
    }

    public void reset() {
        this.playload.reset();
    }
}
