package com.backaudio.android.driver.bluetooth;

public interface IBluetoothProtocolAnalyzer {
    void push(byte[] bArr, int i, int i2);

    void reset();

    void setEventHandler(IBluetoothEventHandler iBluetoothEventHandler);
}
