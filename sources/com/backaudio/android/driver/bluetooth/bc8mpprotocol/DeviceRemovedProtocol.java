package com.backaudio.android.driver.bluetooth.bc8mpprotocol;

public class DeviceRemovedProtocol extends AbstractLineProtocol {
    private String address;

    public DeviceRemovedProtocol(String key, String value) {
        super(key, value);
        try {
            this.address = value.substring(44);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getAddress() {
        return this.address;
    }
}
