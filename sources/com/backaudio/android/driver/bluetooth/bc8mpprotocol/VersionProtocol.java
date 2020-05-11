package com.backaudio.android.driver.bluetooth.bc8mpprotocol;

public class VersionProtocol extends AbstractLineProtocol {
    public VersionProtocol(String key, String value) {
        super(key, value);
    }

    public String getVersion() {
        return super.getValue();
    }
}
