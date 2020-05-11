package com.backaudio.android.driver.bluetooth.bc8mpprotocol;

public class PhoneStatusProtocol extends AbstractLineProtocol {
    private EPhoneStatus phoneStatus;

    public PhoneStatusProtocol(String key, String value) {
        super(key, value);
        if (value.equals("0")) {
            this.phoneStatus = EPhoneStatus.UNCONNECT;
        } else if (value.equals("1")) {
            this.phoneStatus = EPhoneStatus.CONNECTING;
        } else if (value.equals("2")) {
            this.phoneStatus = EPhoneStatus.CONNECTED;
        } else if (value.equals("3")) {
            this.phoneStatus = EPhoneStatus.INCOMING_CALL;
        } else if (value.equals("4")) {
            this.phoneStatus = EPhoneStatus.CALLING_OUT;
        } else if (value.equals("5")) {
            this.phoneStatus = EPhoneStatus.TALKING;
        } else if (value.equals("6")) {
            this.phoneStatus = EPhoneStatus.MULTI_TALKING;
        }
    }

    public void setPhoneStatus(String value) {
        if (value == null) {
            this.phoneStatus = EPhoneStatus.UNCONNECT;
        } else if (value.equals("0")) {
            this.phoneStatus = EPhoneStatus.INITIALIZING;
        } else if (value.equals("1")) {
            this.phoneStatus = EPhoneStatus.UNCONNECT;
        } else if (value.equals("2")) {
            this.phoneStatus = EPhoneStatus.CONNECTING;
        } else if (value.equals("3")) {
            this.phoneStatus = EPhoneStatus.CONNECTED;
        } else if (value.equals("4")) {
            this.phoneStatus = EPhoneStatus.CALLING_OUT;
        } else if (value.equals("5")) {
            this.phoneStatus = EPhoneStatus.INCOMING_CALL;
        } else if (value.equals("6")) {
            this.phoneStatus = EPhoneStatus.TALKING;
        } else {
            this.phoneStatus = EPhoneStatus.UNCONNECT;
        }
    }

    public void setPhoneStatus(EPhoneStatus phoneStatus2) {
        this.phoneStatus = phoneStatus2;
    }

    public EPhoneStatus getPhoneStatus() {
        return this.phoneStatus;
    }
}
