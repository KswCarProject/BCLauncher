package com.backaudio.android.driver.bluetooth;

public class StandardSerialPort {
    public native int close();

    public native int open(String str, int i, int i2, int i3, int i4, int i5, int i6);

    public native int read(byte[] bArr, int i);

    public native int write(byte[] bArr);

    public native int writeLength(byte[] bArr, int i);

    static {
        try {
            System.loadLibrary("StandardSerialPort");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
