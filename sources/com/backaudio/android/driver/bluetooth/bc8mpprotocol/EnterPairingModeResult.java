package com.backaudio.android.driver.bluetooth.bc8mpprotocol;

public class EnterPairingModeResult extends AbstractLineProtocol {
    private boolean isSuccess = false;

    public EnterPairingModeResult(String key, String value) {
        super(key, value);
        try {
            if (value.equals("0")) {
                this.isSuccess = true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public boolean isSuccess() {
        return this.isSuccess;
    }
}
