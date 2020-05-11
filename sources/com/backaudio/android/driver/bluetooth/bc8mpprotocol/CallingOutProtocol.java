package com.backaudio.android.driver.bluetooth.bc8mpprotocol;

public class CallingOutProtocol extends AbstractLineProtocol {
    private String phoneNumber;

    public CallingOutProtocol(String key, String value) {
        super(key, value);
        this.phoneNumber = value;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }
}
