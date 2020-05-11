package com.backaudio.android.driver.bluetooth.bc8mpprotocol;

public class CallOutResult extends AbstractLineProtocol {
    private boolean isSuccess = false;

    public CallOutResult(String key, String value) {
        super(key, value);
        if (value.equals("0")) {
            this.isSuccess = true;
        }
    }

    public boolean isSuccess() {
        return this.isSuccess;
    }
}
