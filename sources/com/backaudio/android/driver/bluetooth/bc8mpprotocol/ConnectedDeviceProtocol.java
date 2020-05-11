package com.backaudio.android.driver.bluetooth.bc8mpprotocol;

public class ConnectedDeviceProtocol extends AbstractLineProtocol {
    private String deviceAddress;
    private String deviceName;

    public ConnectedDeviceProtocol(String key, String value) {
        super(key, value);
        this.deviceAddress = value;
    }

    public String getDeviceAddress() {
        return this.deviceAddress;
    }

    public String getDeviceName() {
        return this.deviceName;
    }

    public void setDeviceName(String deviceName2) {
        this.deviceName = deviceName2;
    }
}
