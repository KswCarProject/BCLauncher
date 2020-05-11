package com.backaudio.android.driver.bluetooth.bc8mpprotocol;

public class PhoneBookCtrlStatusProtocol extends AbstractLineProtocol {
    private EPhoneBookCtrlStatus phoneBookCtrlStatus;

    public PhoneBookCtrlStatusProtocol(String key, String value) {
        super(key, value);
        if (value.equals("0")) {
            this.phoneBookCtrlStatus = EPhoneBookCtrlStatus.UNCONNECT;
        } else if (value.equals("1")) {
            this.phoneBookCtrlStatus = EPhoneBookCtrlStatus.CONECTING;
        } else if (value.equals("2")) {
            this.phoneBookCtrlStatus = EPhoneBookCtrlStatus.CONNECTED;
        } else if (value.equals("3")) {
            this.phoneBookCtrlStatus = EPhoneBookCtrlStatus.DOWNLOADING;
        }
    }

    public EPhoneBookCtrlStatus getPhoneBookCtrlStatus() {
        return this.phoneBookCtrlStatus;
    }
}
