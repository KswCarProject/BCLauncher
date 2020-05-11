package com.backaudio.android.driver;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class LibSerialPort {
    private static List<File> openedSerialPort = new ArrayList();
    private FileDescriptor mFd;
    private FileInputStream mFileInputStream;
    private FileOutputStream mFileOutputStream;

    private native FileDescriptor open(String str, int i, int i2);

    public native void close(FileDescriptor fileDescriptor);

    static {
        System.loadLibrary("SerialPort");
    }

    public LibSerialPort(File device, int baudrate, int flags) throws Exception {
        boolean alreadyOpened = false;
        if (!openedSerialPort.contains(device)) {
            synchronized (LibSerialPort.class) {
                if (!openedSerialPort.contains(device)) {
                    this.mFd = open(device.getAbsolutePath(), baudrate, flags);
                    if (this.mFd == null) {
                        throw new Exception("cannot open serialport:" + device.getAbsolutePath());
                    }
                    this.mFileInputStream = new FileInputStream(this.mFd);
                    this.mFileOutputStream = new FileOutputStream(this.mFd);
                    openedSerialPort.add(device);
                } else {
                    alreadyOpened = true;
                }
            }
        } else {
            alreadyOpened = true;
        }
        if (alreadyOpened) {
            throw new Exception("serialport is already opened! cannot open now");
        }
    }

    public FileInputStream getmFileInputStream() {
        return this.mFileInputStream;
    }

    public FileOutputStream getmFileOutputStream() {
        return this.mFileOutputStream;
    }
}
