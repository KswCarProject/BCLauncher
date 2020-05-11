package com.backaudio.android.driver;

import android.util.Log;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SerialPortMainboardIO {
    private static SerialPortMainboardIO instance = null;
    private final String TAG = "driverlog";
    private Logger logger = LoggerFactory.getLogger(SerialPortMainboardIO.class);
    private FileInputStream mFileInputStream = null;
    private FileOutputStream mFileOutputStream = null;

    public static SerialPortMainboardIO getInstance() {
        if (instance == null) {
            synchronized (SerialPortMainboardIO.class) {
                if (instance == null) {
                    instance = new SerialPortMainboardIO();
                }
            }
        }
        return instance;
    }

    public SerialPortMainboardIO() {
        init(this);
    }

    public int read(byte[] buffer) throws IOException {
        if (this.mFileInputStream == null) {
            while (this.mFileInputStream == null) {
                init(this);
                if (this.mFileInputStream == null) {
                    Log.e("driverlog", "/dev/BT_serial cannot open");
                }
            }
        }
        return this.mFileInputStream.read(buffer);
    }

    public void write(byte[] responseBuffer) {
        Log.d("driverlog", "bluetoothprot: write->[" + Utils.byteArrayToHexString(responseBuffer, 0, responseBuffer.length) + "]>");
        synchronized (SerialPortMainboardIO.class) {
            if (this.mFileOutputStream == null) {
                init(this);
                if (this.mFileOutputStream == null) {
                    Log.e("driverlog", "/dev/BT_serial cannot open");
                    return;
                }
            }
            try {
                this.mFileOutputStream.write(responseBuffer);
                this.mFileOutputStream.flush();
            } catch (Exception e) {
                Log.e("driverlog", "/dev/BT_serial write error " + e.getMessage());
            }
            try {
                Thread.sleep(40);
            } catch (InterruptedException e2) {
            }
            return;
        }
    }

    private void init(SerialPortMainboardIO serialPortMainboardIO) {
        int count = 0;
        while (true) {
            try {
                LibSerialPort serialPort = new LibSerialPort(new File("/dev/BT_serial"), 115200, 0);
                serialPortMainboardIO.mFileInputStream = serialPort.getmFileInputStream();
                serialPortMainboardIO.mFileOutputStream = serialPort.getmFileOutputStream();
                return;
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
}
