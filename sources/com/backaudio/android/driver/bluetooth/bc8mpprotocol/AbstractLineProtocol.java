package com.backaudio.android.driver.bluetooth.bc8mpprotocol;

public abstract class AbstractLineProtocol {
    private String key;
    private String value;

    public AbstractLineProtocol(String key2, String value2) {
        this.key = key2;
        this.value = value2;
    }

    public String getKey() {
        return this.key;
    }

    public String getValue() {
        return this.value;
    }
}
