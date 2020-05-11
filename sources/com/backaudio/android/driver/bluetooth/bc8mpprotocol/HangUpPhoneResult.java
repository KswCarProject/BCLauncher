package com.backaudio.android.driver.bluetooth.bc8mpprotocol;

public class HangUpPhoneResult extends AbstractLineProtocol {
    private boolean isSuccess = false;

    public HangUpPhoneResult(String key, String value) {
        super(key, value);
        if (value.equals("0")) {
            this.isSuccess = true;
        }
    }

    public boolean isSuccess() {
        return this.isSuccess;
    }
}
