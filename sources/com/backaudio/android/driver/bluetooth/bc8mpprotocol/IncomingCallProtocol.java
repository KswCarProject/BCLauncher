package com.backaudio.android.driver.bluetooth.bc8mpprotocol;

public class IncomingCallProtocol extends AbstractLineProtocol {
    private String phone;

    public IncomingCallProtocol(String key, String value) {
        super(key, value);
        this.phone = value;
    }

    public String getPhone() {
        return this.phone;
    }
}
