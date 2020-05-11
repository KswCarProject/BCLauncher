package com.backaudio.android.driver.bluetooth.bc8mpprotocol;

public class DeviceNameProtocol extends AbstractLineProtocol {
    private String MACAddr;
    private String PIN;
    private String deviceName;
    private boolean success = false;

    public DeviceNameProtocol(String key, String value) {
        super(key, value);
        try {
            if (value.charAt(0) == '0') {
                this.deviceName = value.substring(value.indexOf(44) + 1);
                this.success = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isSuccess() {
        return this.success;
    }

    public String getDeviceName() {
        return this.deviceName;
    }

    public String getPIN() {
        return this.PIN;
    }

    public String getMACAdress() {
        return this.MACAddr;
    }

    public void setDeviceName(String deviceName2) {
        this.success = true;
        this.deviceName = deviceName2;
    }

    public void setPIN(String pIN) {
        this.PIN = pIN;
    }

    public void setMACAddr(String MACAddr2) {
        this.MACAddr = MACAddr2;
    }

    public String toString() {
        return "DeviceNameProtocol [deviceName=" + this.deviceName + ", PIN=" + this.PIN + ", MACAddr=" + this.MACAddr + "]";
    }
}
