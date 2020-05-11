package com.backaudio.android.driver.bluetooth.bc8mpprotocol;

public class AnswerPhoneResult extends AbstractLineProtocol {
    private boolean isSuccess = false;

    public AnswerPhoneResult(String key, String value) {
        super(key, value);
        if (value.equals("0")) {
            this.isSuccess = true;
        }
    }

    public boolean isSuccess() {
        return this.isSuccess;
    }
}
