package com.backaudio.android.driver;

import android.util.Log;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SerialPortMcuIO {
    private static SerialPortMcuIO instance = null;
    private final String TAG = "driverlog";
    private Logger logger = LoggerFactory.getLogger(SerialPortMcuIO.class);
    private FileInputStream mFileInputStream = null;
    private FileOutputStream mFileOutputStream = null;

    public static SerialPortMcuIO getInstance() {
        if (instance == null) {
            synchronized (SerialPortMcuIO.class) {
                if (instance == null) {
                    instance = new SerialPortMcuIO();
                }
            }
        }
        return instance;
    }

    public SerialPortMcuIO() {
        init();
    }

    private void init() {
        int count = 0;
        while (true) {
            try {
                LibSerialPort serialPort = new LibSerialPort(new File("/dev/ttyMT3"), 115200, 0);
                this.mFileInputStream = serialPort.getmFileInputStream();
                this.mFileOutputStream = serialPort.getmFileOutputStream();
            } catch (Exception e) {
                count++;
                if (count <= 2) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                } else {
                    return;
                }
            }
        }
    }

    public int read(byte[] buffer) throws IOException {
        if (this.mFileInputStream == null) {
            while (this.mFileInputStream == null) {
                init();
                if (this.mFileInputStream == null) {
                    Log.e("driverlog", "/dev/ttyMT3 cannot open serial port");
                }
            }
        }
        return this.mFileInputStream.read(buffer);
    }

    public void write(byte[] responseBuffer) {
        this.logger.debug("mcu: write->[" + Utils.byteArrayToHexString(responseBuffer, 0, responseBuffer.length) + "]>");
        synchronized (SerialPortMcuIO.class) {
            if (this.mFileOutputStream == null) {
                init();
                if (this.mFileOutputStream == null) {
                    Log.e("driverlog", "/dev/ttyMT3 cannot open serial port");
                    return;
                }
            }
            try {
                this.mFileOutputStream.write(responseBuffer);
                this.mFileOutputStream.flush();
            } catch (Exception e) {
                Log.e("driverlog", "/dev/ttyMT3 cannot write error: " + e.getMessage());
            }
            try {
                Thread.sleep(50);
            } catch (InterruptedException e2) {
            }
            return;
        }
    }
}
