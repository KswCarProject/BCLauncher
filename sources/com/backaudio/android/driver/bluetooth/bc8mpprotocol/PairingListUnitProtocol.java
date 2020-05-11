package com.backaudio.android.driver.bluetooth.bc8mpprotocol;

public class PairingListUnitProtocol extends AbstractLineProtocol {
    private String deviceAddress;
    private String deviceName;

    public PairingListUnitProtocol(String key, String value) {
        super(key, value);
        try {
            this.deviceAddress = value.substring(value.indexOf(44) + 1, value.lastIndexOf(44));
            this.deviceName = value.substring(value.lastIndexOf(",\"") + 2, value.lastIndexOf(34));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getDeviceAddress() {
        return this.deviceAddress;
    }

    public String getDeviceName() {
        return this.deviceName;
    }
}
