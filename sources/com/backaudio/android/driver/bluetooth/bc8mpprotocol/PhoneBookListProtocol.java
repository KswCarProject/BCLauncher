package com.backaudio.android.driver.bluetooth.bc8mpprotocol;

public class PhoneBookListProtocol extends AbstractPlayLoadProtocol {
    public PhoneBookListProtocol(String key, String value) {
        super(key, value);
    }

    public void addUnit(AbstractPlayLoadProtocol phoneBookUnit) {
        super.push(phoneBookUnit.getPlayload(), 0, phoneBookUnit.getPlayload().length);
    }
}
