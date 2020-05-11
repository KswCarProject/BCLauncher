package com.backaudio.android.driver;

import android.os.Process;
import android.support.v4.media.TransportMediator;
import android.support.v4.view.MotionEventCompat;
import android.text.TextUtils;
import android.util.Log;
import com.backaudio.android.driver.beans.AirInfo;
import com.backaudio.android.driver.beans.CarBaseInfo;
import com.backaudio.android.driver.beans.CarRunInfo;
import com.backaudio.android.driver.bluetooth.BluetoothProtocolAnalyzer2;
import com.backaudio.android.driver.bluetooth.IBluetoothEventHandler;
import com.backaudio.android.driver.bluetooth.IBluetoothProtocolAnalyzer;
import com.touchus.publicutils.sysconst.BenzModel;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Mainboard {
    private static Mainboard instance = null;
    String CANBOX;
    String MCU;
    private CarBaseInfo baseInfo;
    /* access modifiers changed from: private */
    public IBluetoothProtocolAnalyzer bluetoothProtocolAnalyzer;
    private boolean iAirMenu;
    private boolean iForbidGesture;
    boolean isGesture09;
    private boolean isLeftRudder;
    /* access modifiers changed from: private */
    public boolean isRecvThreadStarted;
    private IMainboardEventLisenner mainboardEventLisenner;
    /* access modifiers changed from: private */
    public SerialPortMainboardIO mainboardIO;
    /* access modifiers changed from: private */
    public SerialPortMcuIO mcuIO;
    /* access modifiers changed from: private */
    public ProtocolAnalyzer mcuProtocolAnalyzer;
    private Thread recvThread;
    private CarRunInfo runInfo;
    StringBuffer soundValue;
    Timer timer;

    private Mainboard() {
        this.mainboardIO = null;
        this.mcuIO = null;
        this.isRecvThreadStarted = false;
        this.recvThread = null;
        this.mcuProtocolAnalyzer = null;
        this.mainboardEventLisenner = null;
        this.runInfo = new CarRunInfo();
        this.baseInfo = new CarBaseInfo();
        this.iAirMenu = false;
        this.iForbidGesture = false;
        this.isLeftRudder = true;
        this.CANBOX = "listenlogwritecan";
        this.MCU = "listenlogwritemcu";
        this.soundValue = new StringBuffer();
        this.isGesture09 = false;
        this.mainboardIO = SerialPortMainboardIO.getInstance();
        this.bluetoothProtocolAnalyzer = new BluetoothProtocolAnalyzer2();
        this.isRecvThreadStarted = true;
        new Thread(new Runnable() {
            public void run() {
                Process.setThreadPriority(-19);
                byte[] buffer = new byte[16];
                while (Mainboard.this.isRecvThreadStarted) {
                    try {
                        File f = new File("/dev/input/event2");
                        if (!f.exists() || !f.canRead()) {
                            try {
                                Thread.sleep(3000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        } else {
                            FileInputStream in = new FileInputStream(f);
                            while (Mainboard.this.isRecvThreadStarted) {
                                try {
                                    Mainboard.this.mcuProtocolAnalyzer.pushEvent(buffer, 0, in.read(buffer, 0, buffer.length));
                                } catch (IOException e2) {
                                    e2.printStackTrace();
                                    try {
                                        Thread.sleep(2000);
                                    } catch (InterruptedException e1) {
                                        e1.printStackTrace();
                                    }
                                }
                            }
                            in.close();
                        }
                    } catch (Exception e3) {
                        e3.printStackTrace();
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e12) {
                            e12.printStackTrace();
                        }
                    }
                }
            }
        }, "event2Thread").start();
        this.recvThread = new Thread(new Runnable() {
            public void run() {
                Process.setThreadPriority(-19);
                byte[] buffer = new byte[200];
                while (Mainboard.this.isRecvThreadStarted) {
                    try {
                        int size = Mainboard.this.mainboardIO.read(buffer);
                        if (size > 0) {
                            Mainboard.this.bluetoothProtocolAnalyzer.push(buffer, 0, size);
                        } else {
                            Thread.sleep(2);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }, "btThread");
        this.isRecvThreadStarted = true;
        this.recvThread.start();
        this.mcuIO = SerialPortMcuIO.getInstance();
        this.mcuProtocolAnalyzer = new ProtocolAnalyzer();
        new Thread(new Runnable() {
            public void run() {
                Process.setThreadPriority(-19);
                byte[] buffer = new byte[200];
                while (Mainboard.this.isRecvThreadStarted) {
                    try {
                        int size = Mainboard.this.mcuIO.read(buffer);
                        if (size > 0) {
                            Mainboard.this.mcuProtocolAnalyzer.pushMcu(buffer, 0, size);
                        } else {
                            Thread.sleep(2);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }, "mcuThread").start();
    }

    public static Mainboard getInstance() {
        if (instance == null) {
            synchronized (Mainboard.class) {
                if (instance == null) {
                    instance = new Mainboard();
                }
            }
        }
        return instance;
    }

    public void addEventHandler(IBluetoothEventHandler handler) {
        if (handler != null) {
            this.bluetoothProtocolAnalyzer.setEventHandler(handler);
        }
    }

    public void writeToMcu(byte[] mcuProtocol) {
        this.mcuIO.write(mcuProtocol);
    }

    public synchronized void writeCanbox(byte[] buff) {
        byte[] canboxProtocol = wrapCanbox(buff);
        if (!(this.mainboardIO == null || canboxProtocol == null)) {
            Log.d("driverlog", "canboxProtocol write = " + Utils.byteArrayToHexString(canboxProtocol));
            this.mcuIO.write(canboxProtocol);
        }
    }

    public synchronized void writeBlueTooth(byte[] btProtocol) {
        this.mainboardIO.write(btProtocol);
    }

    public void setMainboardEventLisenner(IMainboardEventLisenner mainboardEventLisenner2) {
        this.mainboardEventLisenner = mainboardEventLisenner2;
    }

    public void setRudderType(int type) {
        this.isLeftRudder = type == 0;
    }

    public void enterIntoAcc() {
        byte[] buffer = new byte[6];
        buffer[0] = -86;
        buffer[1] = 85;
        buffer[2] = 2;
        buffer[3] = 1;
        buffer[4] = 1;
        buffer[5] = getCheckMcuByte(buffer);
        writeToMcu(buffer);
    }

    public void wakeupMcu() {
        byte[] buffer = new byte[6];
        buffer[0] = -86;
        buffer[1] = 85;
        buffer[2] = 2;
        buffer[3] = 1;
        buffer[4] = 2;
        buffer[5] = getCheckMcuByte(buffer);
        writeToMcu(buffer);
    }

    public void openOrCloseMediaRelay(boolean iOpen) {
        byte b = 1;
        byte[] buffer = new byte[7];
        buffer[0] = -86;
        buffer[1] = 85;
        buffer[2] = 3;
        buffer[3] = 1;
        buffer[4] = 63;
        buffer[5] = 1;
        if (!iOpen) {
            b = 2;
        }
        buffer[5] = b;
        buffer[6] = getCheckMcuByte(buffer);
        writeToMcu(buffer);
        Log.e(this.MCU, ">>>\topenOrCloseRelay:\t" + iOpen);
    }

    public void openOrCloseRelay(boolean iOpen) {
        byte b = 1;
        byte[] buffer = new byte[7];
        buffer[0] = -86;
        buffer[1] = 85;
        buffer[2] = 3;
        buffer[3] = 1;
        buffer[4] = 64;
        buffer[5] = 1;
        if (!iOpen) {
            b = 2;
        }
        buffer[5] = b;
        buffer[6] = getCheckMcuByte(buffer);
        writeToMcu(buffer);
        Log.e(this.MCU, ">>>\topenOrCloseRelay:\t" + iOpen);
    }

    /*  JADX ERROR: JadxRuntimeException in pass: InitCodeVariables
        jadx.core.utils.exceptions.JadxRuntimeException: Several immutable types in one variable: [int, byte], vars: [r1v0 ?, r1v1 ?, r1v4 ?]
        	at jadx.core.dex.visitors.InitCodeVariables.setCodeVarType(InitCodeVariables.java:102)
        	at jadx.core.dex.visitors.InitCodeVariables.setCodeVar(InitCodeVariables.java:78)
        	at jadx.core.dex.visitors.InitCodeVariables.initCodeVar(InitCodeVariables.java:69)
        	at jadx.core.dex.visitors.InitCodeVariables.initCodeVars(InitCodeVariables.java:51)
        	at jadx.core.dex.visitors.InitCodeVariables.visit(InitCodeVariables.java:32)
        */
    public void powerAudio(boolean r7) {
        /*
            r6 = this;
            r1 = 7
            r5 = 3
            r4 = 1
            byte[] r0 = new byte[r1]
            r2 = 0
            r3 = -86
            r0[r2] = r3
            r2 = 85
            r0[r4] = r2
            r2 = 2
            r0[r2] = r5
            r0[r5] = r4
            r2 = 4
            r3 = 14
            r0[r2] = r3
            r2 = 5
            if (r7 == 0) goto L_0x001d
            r1 = 8
        L_0x001d:
            r0[r2] = r1
            r1 = 6
            byte r2 = r6.getCheckMcuByte(r0)
            r0[r1] = r2
            r6.writeToMcu(r0)
            java.lang.String r1 = r6.MCU
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            java.lang.String r3 = ">>>\tpowerAudio isOpen = "
            r2.<init>(r3)
            java.lang.StringBuilder r2 = r2.append(r7)
            java.lang.String r2 = r2.toString()
            android.util.Log.e(r1, r2)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.backaudio.android.driver.Mainboard.powerAudio(boolean):void");
    }

    public void enterOrOutMute(boolean iEnter) {
        byte b = 1;
        byte[] buffer = new byte[7];
        buffer[0] = -86;
        buffer[1] = 85;
        buffer[2] = 3;
        buffer[3] = 1;
        buffer[4] = 65;
        buffer[5] = 1;
        if (!iEnter) {
            b = 0;
        }
        buffer[5] = b;
        buffer[6] = getCheckMcuByte(buffer);
        writeToMcu(buffer);
        Log.e(this.MCU, ">>>\tenterOrOutMute:\t" + iEnter);
    }

    public void setAllHornSoundValue(int main, int fLeft, int fRight, int rLeft, int rRight) {
        StringBuffer soundValue1 = new StringBuffer().append(main).append(fLeft).append(fRight).append(rLeft).append(rRight);
        boolean isupdate = TextUtils.equals(this.soundValue.toString(), soundValue1.toString());
        Log.e(this.MCU, ">>>\tsetAllHornSoundValue: isupdate = " + isupdate + ",soundValue.toString() = " + this.soundValue.toString());
        if (!isupdate) {
            this.soundValue = soundValue1;
            byte[] buffer = new byte[11];
            buffer[0] = -86;
            buffer[1] = 85;
            buffer[2] = 7;
            buffer[3] = 1;
            buffer[4] = 60;
            buffer[5] = (byte) main;
            buffer[6] = (byte) fLeft;
            buffer[7] = (byte) fRight;
            buffer[8] = (byte) rLeft;
            buffer[9] = (byte) rRight;
            buffer[10] = getCheckMcuByte(buffer);
            writeToMcu(buffer);
            Log.e(this.MCU, ">>>\tsetAllHornSoundValue:\tmain = " + main + ",fLeft = " + fLeft + ",fRight = " + fRight + ",rLeft = " + rLeft + ",rRight = " + rRight);
        }
    }

    public void setDVSoundValue(int main, int fLeft, int fRight, int rLeft, int rRight) {
        byte[] buffer = new byte[11];
        buffer[0] = -86;
        buffer[1] = 85;
        buffer[2] = 7;
        buffer[3] = 1;
        buffer[4] = 99;
        buffer[5] = (byte) main;
        buffer[6] = (byte) fLeft;
        buffer[7] = (byte) fRight;
        buffer[8] = (byte) rLeft;
        buffer[9] = (byte) rRight;
        buffer[10] = getCheckMcuByte(buffer);
        writeToMcu(buffer);
        Log.e(this.MCU, ">>>\tsetDVSoundValue:\tDV main = " + main + ",fLeft = " + fLeft + ",fRight = " + fRight + ",rLeft = " + rLeft + ",rRight = " + rRight);
    }

    public void setSoundTypeValue(int trebleValue, int midtonesValue, int bassValue) {
        byte[] buffer = new byte[9];
        buffer[0] = -86;
        buffer[1] = 85;
        buffer[2] = 5;
        buffer[3] = 1;
        buffer[4] = 92;
        buffer[5] = 1;
        buffer[5] = (byte) trebleValue;
        buffer[6] = (byte) midtonesValue;
        buffer[7] = (byte) bassValue;
        buffer[8] = getCheckMcuByte(buffer);
        writeToMcu(buffer);
        Log.e(this.MCU, ">>>\tsetSoundTypeValue:\ttrebleValue = " + trebleValue + ",midtonesValue = " + midtonesValue + ",bassValue = " + bassValue);
    }

    public void getAllHornSoundValue() {
        byte[] buffer = new byte[7];
        buffer[0] = -86;
        buffer[1] = 85;
        buffer[2] = 3;
        buffer[3] = 1;
        buffer[4] = 61;
        buffer[5] = 1;
        buffer[6] = getCheckMcuByte(buffer);
        writeToMcu(buffer);
    }

    public void closeOrOpenScreen(boolean iClose) {
        if (iClose) {
            writeToMcu(new byte[]{-86, 85, 3, 1, 18, 5, 27});
        } else {
            writeToMcu(new byte[]{-86, 85, 3, 1, 18, 4, 26});
        }
        Log.e(this.MCU, ">>>\tcloseOrOpenScreen:\t" + iClose);
    }

    public void readyToStandby() {
        this.soundValue = new StringBuffer();
        byte[] bArr = new byte[7];
        bArr[0] = -86;
        bArr[1] = 85;
        bArr[2] = 3;
        bArr[3] = 1;
        bArr[5] = 3;
        bArr[6] = 7;
        writeToMcu(bArr);
        Log.e(this.MCU, ">>>\treadyToStandby");
    }

    public void readyToWork() {
        byte[] bArr = new byte[7];
        bArr[0] = -86;
        bArr[1] = 85;
        bArr[2] = 3;
        bArr[3] = 1;
        bArr[5] = 4;
        bArr[6] = 8;
        writeToMcu(bArr);
        Log.e(this.MCU, ">>>\treadyToWork");
    }

    public void getMCUVersionDate() {
        writeToMcu(new byte[]{-86, 85, 3, 1, 16, 1, 21});
        Log.e(this.MCU, ">>>\tgetMCUVersionDate");
    }

    public void getMCUVersionNumber() {
        writeToMcu(new byte[]{-86, 85, 3, 1, 17, 1, 22});
        Log.e(this.MCU, ">>>\tgetMCUVersionNumber");
    }

    public void showCarLayer(ECarLayer eCarLayer) {
        writeToMcu(new byte[]{-86, 85, 3, 1, 74, eCarLayer.getCode(), (byte) (eCarLayer.getCode() + 78)});
        Log.e(this.MCU, ">>>\tshowCarLayer == " + eCarLayer);
    }

    public void sendRecordToMcu() {
        writeToMcu(new byte[]{-86, 85, 2, 1, 84, 87});
    }

    public void sendCoordinateToMcu(int x, int y, int status) {
        byte data0;
        this.mainboardEventLisenner.obtainCoordinate(x, y);
        String tmp = Integer.toHexString(x);
        String h = "";
        String l = tmp;
        if (tmp.length() > 2) {
            h = tmp.substring(0, tmp.length() - 2);
            l = tmp.substring(tmp.length() - 2);
        }
        if (TextUtils.isEmpty(h)) {
            data0 = 0;
        } else {
            data0 = (byte) Integer.parseInt(h, 16);
        }
        byte data1 = (byte) Integer.parseInt(l, 16);
        String tmp2 = Integer.toHexString(y);
        String h2 = "";
        String l2 = tmp2;
        if (tmp2.length() > 2) {
            h2 = tmp2.substring(0, tmp2.length() - 2);
            l2 = tmp2.substring(tmp2.length() - 2);
        }
        byte data2 = TextUtils.isEmpty(h2) ? 0 : (byte) Integer.parseInt(h2, 16);
        byte data3 = (byte) Integer.parseInt(l2, 16);
        writeToMcu(new byte[]{-86, 85, 7, 1, 73, data0, data1, data2, data3, (byte) status, (byte) (data0 + 81 + data1 + data2 + data3 + status)});
        Log.e(this.MCU, ">>>\tsendCoordinateToMcu x = " + x + ",y = " + y + ",data0 = " + data0 + ",data1 = " + data1 + ",data2 = " + data2 + ",data3 = " + data3 + ",data4 = " + status);
    }

    public void getBrightnessValue() {
        writeToMcu(new byte[]{-86, 85, 3, 1, 2, -1, 5});
        Log.e(this.MCU, ">>>\tgetBrightnessValue");
    }

    public void setBrightnessToMcu(int value) {
        byte[] buffer = new byte[7];
        buffer[0] = -86;
        buffer[1] = 85;
        buffer[2] = 3;
        buffer[3] = 1;
        buffer[4] = 2;
        buffer[5] = (byte) value;
        buffer[6] = getCheckMcuByte(buffer);
        writeToMcu(buffer);
        Log.e(this.MCU, ">>>\tsetBrightnessToMcu value = " + value);
    }

    public void sendReverseSetToMcu(int parkingType) {
        switch (parkingType) {
            case 0:
                writeToMcu(new byte[]{-86, 85, 4, 1, 91, 1, 1, 98});
                break;
            case 1:
                byte[] bArr = new byte[8];
                bArr[0] = -86;
                bArr[1] = 85;
                bArr[2] = 4;
                bArr[3] = 1;
                bArr[4] = 91;
                bArr[5] = 1;
                bArr[7] = 97;
                writeToMcu(bArr);
                break;
            case 2:
                writeToMcu(new byte[]{-86, 85, 4, 1, 91, 1, 2, 99});
                break;
        }
        Log.e(this.MCU, ">>>\tsendReverseSetToMcu type = " + parkingType);
    }

    public void sendReverseMediaSetToMcu(int type) {
        int i = 0;
        byte[] buffer = new byte[7];
        buffer[0] = -86;
        buffer[1] = 85;
        buffer[2] = 3;
        buffer[3] = 1;
        buffer[4] = 103;
        EReverserMediaSet source = EReverserMediaSet.NOMAL;
        EReverserMediaSet[] values = EReverserMediaSet.values();
        int length = values.length;
        while (true) {
            if (i >= length) {
                break;
            }
            EReverserMediaSet temp = values[i];
            if (temp.getCode() == type) {
                source = temp;
                break;
            }
            i++;
        }
        buffer[5] = source.getCode();
        buffer[6] = getCheckMcuByte(buffer);
        writeToMcu(buffer);
        Log.e(this.MCU, ">>>\tsendReverseMediaSetToMcu type = " + source);
    }

    public void sendLanguageSetToMcu(int type) {
        int i = 0;
        byte[] buffer = new byte[7];
        buffer[0] = -86;
        buffer[1] = 85;
        buffer[2] = 3;
        buffer[3] = 1;
        buffer[4] = 110;
        ELanguage source = ELanguage.US;
        ELanguage[] values = ELanguage.values();
        int length = values.length;
        while (true) {
            if (i >= length) {
                break;
            }
            ELanguage temp = values[i];
            if (temp.getCode() == type) {
                source = temp;
                break;
            }
            i++;
        }
        buffer[5] = source.getCode();
        buffer[6] = getCheckMcuByte(buffer);
        writeToMcu(buffer);
        Log.e(this.MCU, ">>>\tsendLanguageSetToMcu type = " + source);
    }

    public void getLanguageSetFromMcu() {
        writeToMcu(new byte[]{-86, 85, 3, 1, 110, -1, 113});
        Log.e(this.MCU, ">>>\tgetLanguageSetFromMcu ");
    }

    public void getReverseSetFromMcu() {
        byte[] bArr = new byte[7];
        bArr[0] = -86;
        bArr[1] = 85;
        bArr[2] = 3;
        bArr[3] = 1;
        bArr[4] = 91;
        bArr[6] = 95;
        writeToMcu(bArr);
        Log.e(this.MCU, ">>>\tgetReverseSetFromMcu ");
    }

    public void getReverseMediaSetFromMcu() {
        writeToMcu(new byte[]{-86, 85, 3, 1, 103, -1, 106});
        Log.e(this.MCU, ">>>\tgetReverseMediaSetFromMcu ");
    }

    public void setBenzType(BenzModel.EBenzTpye benzType) {
        byte[] buffer = new byte[7];
        buffer[0] = -86;
        buffer[1] = 85;
        buffer[2] = 3;
        buffer[3] = 1;
        buffer[4] = 101;
        buffer[5] = benzType.getCode();
        buffer[6] = getCheckMcuByte(buffer);
        writeToMcu(buffer);
        Log.e(this.MCU, ">>>\tsetBenzType type = " + benzType);
    }

    public void setBenzSize(int benzSize) {
        byte[] buffer = new byte[7];
        buffer[0] = -86;
        buffer[1] = 85;
        buffer[2] = 3;
        buffer[3] = 1;
        buffer[4] = 105;
        buffer[5] = (byte) benzSize;
        buffer[6] = getCheckMcuByte(buffer);
        writeToMcu(buffer);
        Log.e(this.MCU, ">>>\tsetBenzSize type = " + (benzSize + 1));
    }

    public void getBenzType() {
        byte[] bArr = new byte[7];
        bArr[0] = -86;
        bArr[1] = 85;
        bArr[2] = 3;
        bArr[3] = 1;
        bArr[4] = 100;
        bArr[6] = 104;
        writeToMcu(bArr);
        Log.e(this.MCU, ">>>\tgetBenzType ");
    }

    public void getBenzSize() {
        writeToMcu(new byte[]{-86, 85, 3, 1, 105, -1, 108});
        Log.e(this.MCU, ">>>\tgetBenzSize ");
    }

    public void setDVAudio(boolean isDV) {
        int i = 2;
        byte[] buffer = new byte[7];
        buffer[0] = -86;
        buffer[1] = 85;
        buffer[2] = 3;
        buffer[3] = 1;
        buffer[4] = 5;
        if (!isDV) {
            i = 1;
        }
        buffer[5] = (byte) i;
        buffer[6] = getCheckMcuByte(buffer);
        writeToMcu(buffer);
        Log.e(this.MCU, ">>>\tsetDVAudio isDV = " + isDV);
    }

    public void getStoreDataFromMcu() {
        writeToMcu(new byte[]{-86, 85, 3, 1, 96, 1, 101});
        Log.e(this.MCU, ">>>\tgetStoreDataFromMcu  ");
    }

    public void sendStoreDataToMcu(byte[] date) {
        ByteArrayOutputStream tempBuffer = new ByteArrayOutputStream();
        tempBuffer.write(new byte[]{-86, 85, -126, 1, 97}, 0, 5);
        if (date.length < 128) {
            tempBuffer.write(date, 0, date.length);
            byte[] tmp = new byte[(128 - date.length)];
            tempBuffer.write(tmp, 0, tmp.length);
        } else {
            tempBuffer.write(date, 0, 128);
        }
        tempBuffer.write(new byte[1], 0, 1);
        byte temp = getCheckMcuByte(tempBuffer.toByteArray());
        byte[] tempByte = tempBuffer.toByteArray();
        tempByte[tempByte.length - 1] = temp;
        writeToMcu(tempByte);
        Log.e(this.MCU, ">>>\tsendStoreDataToMcu data = " + Utils.byteArrayToHexString(tempByte));
    }

    public void setEffect(int[] data) {
        byte[] buffer = new byte[13];
        buffer[0] = -86;
        buffer[1] = 85;
        buffer[2] = 9;
        buffer[3] = 1;
        buffer[4] = 113;
        for (int i = 0; i < data.length; i++) {
            buffer[i + 5] = (byte) data[i];
        }
        buffer[12] = getCheckMcuByte(buffer);
        writeToMcu(buffer);
        Log.e(this.MCU, ">>>\tsetEffect data = " + data.toString());
    }

    public void connectOrDisConnectCanbox(boolean iConnect) {
        byte[] buffer = new byte[5];
        buffer[0] = 46;
        buffer[1] = -127;
        buffer[2] = 1;
        if (iConnect) {
            buffer[3] = 1;
        } else {
            buffer[3] = 0;
        }
        buffer[4] = getCheckCanboxByte(buffer);
        writeCanbox(buffer);
        Log.e(this.CANBOX, ">>>\tconnectOrDisConnectCanbox:\t" + iConnect);
    }

    public void getControlInfo(boolean isOriginal, EControlSource soure) {
        byte[] buffer = new byte[13];
        buffer[0] = 46;
        buffer[1] = -126;
        buffer[2] = 9;
        if (isOriginal) {
            buffer[3] = 2;
        } else {
            buffer[3] = 1;
        }
        buffer[4] = EControlSource.RADIO.getCode();
        buffer[12] = getCheckCanboxByte(buffer);
        Log.e(this.CANBOX, ">>>\tgetControlInfo:isOriginal = " + isOriginal + "\tEControlSource--" + soure.toString());
    }

    public void getModeInfo(EModeInfo mode) {
        byte[] buffer = new byte[5];
        buffer[0] = 46;
        buffer[1] = -1;
        buffer[2] = 1;
        buffer[3] = mode.getCode();
        buffer[4] = getCheckCanboxByte(buffer);
        writeCanbox(buffer);
        Log.e(this.CANBOX, ">>>\tgetModeInfo:EModeInfo--" + mode.toString());
    }

    public void requestAUXOperate(EAUXOperate eOperate) {
        byte[] buffer = new byte[8];
        buffer[0] = 46;
        buffer[1] = -89;
        buffer[2] = 4;
        buffer[3] = 89;
        buffer[4] = 84;
        buffer[5] = eOperate.getCode();
        buffer[7] = getCheckCanboxByte(buffer);
        writeCanbox(buffer);
        Log.e(this.CANBOX, ">>>\trequestAUXOperate --" + eOperate);
    }

    public void forceRequestBTAudio(boolean isForce, BenzModel.EBenzTpye eBenzTpye, boolean isSquare, boolean hasNavi, boolean isNotBT, int usbNum) {
        byte[] buffer;
        boolean z = false;
        if (isForce) {
            buffer = new byte[6];
            buffer[0] = 46;
            buffer[1] = -91;
            buffer[2] = 2;
            buffer[3] = 2;
        } else {
            buffer = new byte[6];
            buffer[0] = 46;
            buffer[1] = -91;
            buffer[2] = 2;
            buffer[3] = 1;
        }
        if (BenzModel.benzCan == BenzModel.EBenzCAN.ZMYT) {
            if (isSquare) {
                buffer[4] = 64;
                if (usbNum == 1 && !isNotBT) {
                    buffer[4] = 65;
                } else if (usbNum == 2 && !isNotBT) {
                    buffer[4] = 66;
                } else if (usbNum == 1 && isNotBT) {
                    buffer[4] = 69;
                } else if (usbNum == 2 && isNotBT) {
                    buffer[4] = 70;
                }
            } else {
                buffer[4] = Byte.MIN_VALUE;
                if (!hasNavi && isNotBT) {
                    buffer[4] = -127;
                } else if (hasNavi && isNotBT) {
                    buffer[4] = -126;
                } else if (!hasNavi && !isNotBT) {
                    buffer[4] = -125;
                } else if (hasNavi && !isNotBT) {
                    buffer[4] = -124;
                }
            }
        } else if (BenzModel.benzCan == BenzModel.EBenzCAN.WCL) {
            if (isNotBT && isSquare) {
                buffer[4] = 1;
            } else if (!isNotBT || isSquare) {
                buffer[4] = 0;
            } else {
                buffer[4] = 2;
            }
        }
        buffer[5] = getCheckCanboxByte(buffer);
        writeCanbox(buffer);
        String str = this.CANBOX;
        StringBuilder append = new StringBuilder(">>>ZMYT\tforceRequestBTAudio-- ").append(isForce).append(",eBenzTpye = ").append(eBenzTpye).append(",RadioType isSquare = ").append(isSquare).append(",hasNavi = ").append(hasNavi).append(",isBT = ");
        if (!isNotBT) {
            z = true;
        }
        Log.e(str, append.append(z).append(",usbNum = ").append(usbNum).toString());
    }

    public void forceRequestBTAudio2(boolean isForce, BenzModel.EBenzTpye eBenzTpye, boolean isSquare, boolean hasNavi, boolean isNotBT, int usbNum) {
        byte[] buffer;
        int i = 2;
        boolean z = true;
        if (isForce) {
            buffer = new byte[7];
            buffer[0] = 46;
            buffer[1] = -91;
            buffer[2] = 3;
            buffer[3] = 2;
        } else {
            buffer = new byte[7];
            buffer[0] = 46;
            buffer[1] = -91;
            buffer[2] = 3;
            buffer[3] = 1;
        }
        if (isNotBT && isSquare) {
            buffer[4] = 1;
        } else if (!isNotBT || isSquare) {
            buffer[4] = 0;
        } else {
            buffer[4] = 2;
            if (!hasNavi) {
                i = 1;
            }
            buffer[5] = (byte) i;
        }
        buffer[6] = getCheckCanboxByte(buffer);
        writeCanbox(buffer);
        String str = this.CANBOX;
        StringBuilder append = new StringBuilder(">>>LZ\tforceRequestBTAudio-- ").append(isForce).append(",eBenzTpye = ").append(eBenzTpye).append(",RadioType isSquare = ").append(isSquare).append(",hasNavi = ").append(hasNavi).append(",isBT = ");
        if (isNotBT) {
            z = false;
        }
        Log.e(str, append.append(z).append(",usbNum = ").append(usbNum).toString());
    }

    public void setOldCBTAudio() {
        if (BenzModel.benzCan == BenzModel.EBenzCAN.ZMYT) {
            byte[] buffer = new byte[6];
            buffer[0] = 46;
            buffer[1] = -91;
            buffer[2] = 2;
            buffer[3] = 2;
            buffer[4] = Byte.MIN_VALUE;
            buffer[5] = getCheckCanboxByte(buffer);
            writeCanbox(buffer);
            Log.e(this.CANBOX, ">>>\tsetOldCBTAudio-- ");
        }
    }

    public void forcePress() {
        byte[] buffer = new byte[6];
        buffer[0] = 46;
        buffer[1] = -90;
        buffer[2] = 2;
        buffer[3] = 1;
        buffer[5] = getCheckCanboxByte(buffer);
        writeCanbox(buffer);
        Log.e(this.CANBOX, ">>>\tforcePress-- ");
    }

    public void requestUpgradeCanbox(int total_packet) {
        byte b = 0;
        byte[] buffer = new byte[6];
        buffer[0] = 46;
        buffer[1] = -31;
        buffer[2] = 2;
        String fmString = Integer.toHexString(total_packet);
        String h = "";
        String l = fmString;
        if (fmString.length() > 2) {
            h = fmString.substring(0, fmString.length() - 2);
            l = fmString.substring(fmString.length() - 2);
        }
        if (!TextUtils.isEmpty(h)) {
            b = (byte) Integer.parseInt(h, 16);
        }
        buffer[3] = b;
        buffer[4] = (byte) Integer.parseInt(l, 16);
        buffer[5] = getCheckCanboxByte(buffer);
        writeCanbox(buffer);
        Log.e(this.CANBOX, ">>>\trequestUpgradeCanbox");
    }

    public void sendUpdateCanboxInfoByIndex(int index, byte[] date) {
        ByteArrayOutputStream tempBuffer = new ByteArrayOutputStream();
        byte[] buffer = new byte[5];
        buffer[0] = 46;
        buffer[1] = -29;
        buffer[2] = -126;
        if (index >= 0) {
            String fmString = Integer.toHexString(index);
            String h = "";
            String l = fmString;
            if (fmString.length() > 2) {
                h = fmString.substring(0, fmString.length() - 2);
                l = fmString.substring(fmString.length() - 2);
            }
            buffer[3] = TextUtils.isEmpty(h) ? 0 : (byte) Integer.parseInt(h, 16);
            buffer[4] = (byte) Integer.parseInt(l, 16);
        } else {
            buffer[3] = 31;
            buffer[4] = 31;
        }
        tempBuffer.write(buffer, 0, 5);
        if (date.length < 128) {
            tempBuffer.write(date, 0, date.length);
            byte[] tmp = new byte[(128 - date.length)];
            tempBuffer.write(tmp, 0, tmp.length);
        } else {
            tempBuffer.write(date, 0, 128);
        }
        tempBuffer.write(new byte[1], 0, 1);
        byte temp = getCheckCanboxByte(tempBuffer.toByteArray());
        byte[] tempByte = tempBuffer.toByteArray();
        tempByte[tempByte.length - 1] = temp;
        writeCanbox(tempByte);
        Log.e(this.CANBOX, ">>>\tsendUpdateCanboxInfoByIndex;\tindex--" + index);
    }

    public void requestUpgradeMcu(byte[] updateInfo) {
        byte[] buffer = new byte[9];
        buffer[0] = -86;
        buffer[1] = 85;
        buffer[2] = 5;
        buffer[3] = -31;
        buffer[4] = updateInfo[0];
        buffer[5] = updateInfo[1];
        buffer[6] = updateInfo[2];
        buffer[7] = updateInfo[3];
        buffer[8] = getCheckMcuByte(buffer);
        writeToMcu(buffer);
        Log.e(this.CANBOX, ">>>\trequestUpgradeMCU");
    }

    public void sendUpgradeMcuHeaderInfo(int length) {
        byte b = 0;
        byte[] buffer = new byte[7];
        buffer[0] = -86;
        buffer[1] = 85;
        buffer[2] = 3;
        buffer[3] = -27;
        String fmString = Integer.toHexString(length);
        String h = "";
        String l = fmString;
        if (fmString.length() > 2) {
            h = fmString.substring(0, fmString.length() - 2);
            l = fmString.substring(fmString.length() - 2);
        }
        if (!TextUtils.isEmpty(h)) {
            b = (byte) Integer.parseInt(h, 16);
        }
        buffer[4] = b;
        buffer[5] = (byte) Integer.parseInt(l, 16);
        buffer[6] = getCheckMcuByte(buffer);
        writeToMcu(buffer);
        Log.e(this.CANBOX, ">>>\tsendUpgradeMcuHeaderInfo");
    }

    public void sendUpdateMcuInfoByIndex(int index, byte[] date) {
        ByteArrayOutputStream tempBuffer = new ByteArrayOutputStream();
        byte[] buffer = new byte[6];
        buffer[0] = -86;
        buffer[1] = 85;
        buffer[2] = -125;
        buffer[3] = -29;
        if (index >= 0) {
            String fmString = Integer.toHexString(index);
            String h = "";
            String l = fmString;
            if (fmString.length() > 2) {
                h = fmString.substring(0, fmString.length() - 2);
                l = fmString.substring(fmString.length() - 2);
            }
            buffer[4] = TextUtils.isEmpty(h) ? 0 : (byte) Integer.parseInt(h, 16);
            buffer[5] = (byte) Integer.parseInt(l, 16);
        } else {
            buffer[4] = 31;
            buffer[5] = 31;
        }
        tempBuffer.write(buffer, 0, 6);
        if (date.length < 128) {
            tempBuffer.write(date, 0, date.length);
            byte[] tmp = new byte[(128 - date.length)];
            tempBuffer.write(tmp, 0, tmp.length);
        } else {
            tempBuffer.write(date, 0, 128);
        }
        tempBuffer.write(new byte[1], 0, 1);
        byte temp = getCheckMcuByte(tempBuffer.toByteArray());
        byte[] tempByte = tempBuffer.toByteArray();
        tempByte[tempByte.length - 1] = temp;
        writeToMcu(tempByte);
        Log.e(this.CANBOX, ">>>\tsendUpdateMcuInfoByIndex;\tindex-- " + index);
    }

    /* access modifiers changed from: protected */
    public byte[] wrapCanbox(byte[] buffer) {
        if (buffer == null) {
            return null;
        }
        byte[] mcuProtocol = new byte[(buffer.length + 6)];
        mcuProtocol[0] = -86;
        mcuProtocol[1] = 85;
        mcuProtocol[2] = (byte) (buffer.length + 2);
        mcuProtocol[3] = 1;
        mcuProtocol[4] = 80;
        byte check = (byte) (mcuProtocol[2] + mcuProtocol[3] + mcuProtocol[4]);
        for (int i = 0; i < buffer.length; i++) {
            mcuProtocol[i + 5] = buffer[i];
            check = (byte) (buffer[i] + check);
        }
        mcuProtocol[buffer.length + 5] = check;
        return mcuProtocol;
    }

    private byte getCheckCanboxByte(byte[] buffer) {
        int lenght = buffer.length;
        byte check = buffer[1];
        for (int i = 2; i < lenght - 1; i++) {
            check = (byte) (buffer[i] + check);
        }
        return (byte) (check ^ 255);
    }

    private byte getCheckMcuByte(byte[] buffer) {
        byte check = buffer[2];
        for (int i = 3; i < buffer.length - 1; i++) {
            check = (byte) (buffer[i] + check);
        }
        return check;
    }

    public void analyzeMcuEnterAccOrWakeup(byte[] buffer) {
        if (buffer[5] == 1) {
            this.mainboardEventLisenner.onEnterStandbyMode();
        } else if (buffer[5] == 2) {
            ECarLayer eCarLayer = ECarLayer.ANDROID;
            byte b = buffer[6];
            ECarLayer[] values = ECarLayer.values();
            int length = values.length;
            int i = 0;
            while (true) {
                if (i >= length) {
                    break;
                }
                ECarLayer value = values[i];
                if (b == value.getCode()) {
                    eCarLayer = value;
                    break;
                }
                i++;
            }
            this.mainboardEventLisenner.onWakeUp(eCarLayer);
        }
    }

    public void analyzeMcuInfoPro(byte[] buffer) {
        this.mainboardEventLisenner.obtainVersionNumber(new String(Arrays.copyOfRange(buffer, 5, (buffer[2] - 2) + 5), Charset.forName("utf-8")));
    }

    public void analyzeAirInfo(byte[] buffer) {
        byte b1 = buffer[3];
        AirInfo info = new AirInfo();
        info.setiAirOpen(((b1 >> 7) & 1) == 1);
        info.setiACOpen(((b1 >> 6) & 1) == 1);
        info.setiInnerLoop(((b1 >> 5) & 1) == 0);
        info.setiAuto2(((b1 >> 4) & 1) == 1);
        info.setiAuto1(((b1 >> 3) & 1) == 1);
        info.setiDual(((b1 >> 2) & 1) == 1);
        info.setiMaxFrontWind(((b1 >> 1) & 1) == 1);
        info.setiRear((b1 & 1) == 1);
        byte b2 = buffer[4];
        info.setiFrontWind(((b2 >> 7) & 1) == 1);
        info.setiFlatWind(((b2 >> 6) & 1) == 1);
        info.setiDownWind(((b2 >> 5) & 1) == 1);
        info.setLevel(((double) (b2 & 7)) + ((b2 & 8) == 8 ? 0.5d : 0.0d));
        byte leftTemp = buffer[5];
        int rightTemp = buffer[5] == 255 ? -1 : buffer[6];
        if (this.isLeftRudder) {
            info.setLeftTemp(15.0d + (((double) leftTemp) * 0.5d));
            info.setRightTemp(15.0d + (((double) rightTemp) * 0.5d));
        } else {
            info.setRightTemp(15.0d + (((double) leftTemp) * 0.5d));
            info.setLeftTemp(15.0d + (((double) rightTemp) * 0.5d));
        }
        if (this.iAirMenu != (((buffer[7] >> 1) & 1) == 1)) {
            this.mainboardEventLisenner.onHandleIdriver(EIdriverEnum.HOME, EBtnStateEnum.BTN_DOWN);
        }
        this.mainboardEventLisenner.onAirInfo(info);
    }

    public void analyzeCarBasePro(byte[] buffer) {
        boolean iPowerOn;
        boolean iInP;
        boolean iInRevering;
        boolean iNearLightOpen;
        boolean iAccOpen = true;
        byte b1 = buffer[3];
        if (((b1 >> 4) & 1) == 1) {
            iPowerOn = true;
        } else {
            iPowerOn = false;
        }
        if (((b1 >> 3) & 1) == 1) {
            iInP = true;
        } else {
            iInP = false;
        }
        if (((b1 >> 2) & 1) == 1) {
            iInRevering = true;
        } else {
            iInRevering = false;
        }
        if (((b1 >> 1) & 1) == 1) {
            iNearLightOpen = true;
        } else {
            iNearLightOpen = false;
        }
        if ((b1 & 1) != 1) {
            iAccOpen = false;
        }
        this.baseInfo.setiPowerOn(iPowerOn);
        this.baseInfo.setiInP(iInP);
        this.baseInfo.setiInRevering(iInRevering);
        this.baseInfo.setiNearLightOpen(iNearLightOpen);
        this.baseInfo.setiAccOpen(iAccOpen);
        int curSpeed = buffer[7] & MotionEventCompat.ACTION_MASK;
        if (buffer[7] != 255) {
            this.runInfo.setCurSpeed(curSpeed);
        }
        this.baseInfo.setiFlash(false);
        this.mainboardEventLisenner.onCarBaseInfo(this.baseInfo);
        this.mainboardEventLisenner.onCarRunningInfo(this.runInfo);
    }

    public void analyzeCarBasePro1(byte[] buffer) {
        boolean iRightFrontOpen;
        boolean iLeftFrontOpen;
        boolean iRightBackOpen;
        boolean iLeftBackOpen;
        boolean iBack;
        boolean iFront;
        boolean iSafetyBelt = false;
        byte b1 = buffer[3];
        if (((b1 >> 7) & 1) == 1) {
            iRightFrontOpen = true;
        } else {
            iRightFrontOpen = false;
        }
        if (((b1 >> 6) & 1) == 1) {
            iLeftFrontOpen = true;
        } else {
            iLeftFrontOpen = false;
        }
        if (((b1 >> 5) & 1) == 1) {
            iRightBackOpen = true;
        } else {
            iRightBackOpen = false;
        }
        if (((b1 >> 4) & 1) == 1) {
            iLeftBackOpen = true;
        } else {
            iLeftBackOpen = false;
        }
        if (((b1 >> 3) & 1) == 1) {
            iBack = true;
        } else {
            iBack = false;
        }
        if (((b1 >> 2) & 1) == 1) {
            iFront = true;
        } else {
            iFront = false;
        }
        if (((b1 >> 1) & 1) == 1) {
            iSafetyBelt = true;
        }
        if (this.isLeftRudder) {
            this.baseInfo.setiRightFrontOpen(iRightFrontOpen);
            this.baseInfo.setiLeftFrontOpen(iLeftFrontOpen);
            this.baseInfo.setiRightBackOpen(iRightBackOpen);
            this.baseInfo.setiLeftBackOpen(iLeftBackOpen);
        } else {
            this.baseInfo.setiRightFrontOpen(iLeftFrontOpen);
            this.baseInfo.setiLeftFrontOpen(iRightFrontOpen);
            this.baseInfo.setiRightBackOpen(iLeftBackOpen);
            this.baseInfo.setiLeftBackOpen(iRightBackOpen);
        }
        this.baseInfo.setiBack(iBack);
        this.baseInfo.setiFront(iFront);
        this.baseInfo.setiSafetyBelt(iSafetyBelt);
        this.baseInfo.setiFlash(true);
        this.mainboardEventLisenner.onCarBaseInfo(this.baseInfo);
    }

    public void analyzeCarRunningInfoPro1(byte[] buffer) {
        this.runInfo.setCurSpeed(buffer[3] & MotionEventCompat.ACTION_MASK);
        this.runInfo.setRevolutions(((buffer[4] & MotionEventCompat.ACTION_MASK) << 8) + (buffer[5] & 255));
        this.mainboardEventLisenner.onCarRunningInfo(this.runInfo);
    }

    public void analyzeCarRunningInfoPro2(byte[] buffer) {
        this.runInfo.setCurSpeed(buffer[5] & MotionEventCompat.ACTION_MASK);
        this.runInfo.setRevolutions(((buffer[3] & MotionEventCompat.ACTION_MASK) << 8) + (buffer[4] & 255));
        this.mainboardEventLisenner.onCarRunningInfo(this.runInfo);
    }

    public void analyzeCanboxPro(byte[] buffer) {
        byte length = buffer[2];
        byte[] infoByte = Arrays.copyOfRange(buffer, 3, length + 3);
        Charset cs = Charset.forName("US-ASCII");
        ByteBuffer bb = ByteBuffer.allocate(length);
        bb.put(infoByte);
        bb.flip();
        this.mainboardEventLisenner.onCanboxInfo(new String(cs.decode(bb).array()));
    }

    public void analyzeIdriverPro(byte[] buffer) {
        if (buffer.length >= 6) {
            EIdriverEnum idriverValue = EIdriverEnum.NONE;
            byte b = buffer[3];
            EIdriverEnum[] values = EIdriverEnum.values();
            int length = values.length;
            int i = 0;
            while (true) {
                if (i >= length) {
                    break;
                }
                EIdriverEnum value = values[i];
                if (b == value.getCode()) {
                    idriverValue = value;
                    break;
                }
                i++;
            }
            EBtnStateEnum btnValue = EBtnStateEnum.BTN_DOWN;
            if (buffer[4] == 0) {
                btnValue = EBtnStateEnum.BTN_UP;
            } else if (buffer[4] == 2) {
                btnValue = EBtnStateEnum.BTN_LONG_PRESS;
            }
            this.mainboardEventLisenner.onHandleIdriver(idriverValue, btnValue);
        }
    }

    public void analyzeTimePro(byte[] buffer) {
        this.mainboardEventLisenner.onTime(buffer[3] + 2000, buffer[4], buffer[5], ((buffer[6] >> 7) & 1) == 1 ? 12 : 24, buffer[6] & TransportMediator.KEYCODE_MEDIA_PAUSE, buffer[7], 0);
    }

    public void analyzeOriginalCarPro(byte[] buffer) {
        boolean iOriginal = true;
        byte isAux = buffer[4];
        switch (buffer[3]) {
            case 1:
                iOriginal = false;
                break;
            case 2:
                iOriginal = true;
                break;
        }
        EControlSource source = EControlSource.RADIO;
        EControlSource[] values = EControlSource.values();
        int length = values.length;
        int i = 0;
        while (true) {
            if (i < length) {
                EControlSource temp = values[i];
                if (temp.getCode() == isAux) {
                    source = temp;
                } else {
                    i++;
                }
            }
        }
        this.mainboardEventLisenner.onOriginalCarView(source, iOriginal);
    }

    public void analyzeCarRunningInfoPro(byte[] buffer) {
        byte b = buffer[3];
        boolean foglight = (b & 1) == 1;
        boolean iNearLightOpen = ((b >> 1) & 1) == 1;
        boolean iDistantLightOpen = ((b >> 2) & 1) == 1;
        this.baseInfo.ichange = false;
        this.baseInfo.setFoglight(foglight);
        this.baseInfo.setiNearLightOpen(iNearLightOpen);
        this.baseInfo.setiDistantLightOpen(iDistantLightOpen);
        this.baseInfo.setiBrake((buffer[4] & 1) == 1);
        this.baseInfo.setiFootBrake(((buffer[4] >> 1) & 1) == 1);
        int mileage = ((buffer[5] & MotionEventCompat.ACTION_MASK) << 8) + (buffer[6] & 255);
        int revolutions = ((buffer[7] & MotionEventCompat.ACTION_MASK) << 8) + (buffer[8] & 255);
        this.runInfo.setMileage(mileage);
        if (!(buffer[7] == 255 && buffer[8] == 255)) {
            this.runInfo.setRevolutions(revolutions);
        }
        int temp = buffer[9] & MotionEventCompat.ACTION_MASK;
        long totalMileage = (long) (((buffer[10] & MotionEventCompat.ACTION_MASK) << 24) + ((buffer[11] & MotionEventCompat.ACTION_MASK) << 16) + ((buffer[12] & MotionEventCompat.ACTION_MASK) << 8) + (buffer[13] & MotionEventCompat.ACTION_MASK));
        this.runInfo.setOutsideTemp(((double) (((float) temp) / 2.0f)) - 40.0d);
        this.runInfo.setTotalMileage(totalMileage);
        this.baseInfo.setiFlash(false);
        if (this.baseInfo.ichange) {
            this.mainboardEventLisenner.onCarBaseInfo(this.baseInfo);
        }
        this.mainboardEventLisenner.onCarRunningInfo(this.runInfo);
    }

    public void analyzeAUXStatus(byte[] buffer) {
        byte type = buffer[3];
        EAUXStutas source = EAUXStutas.ACTIVATING;
        EAUXStutas[] values = EAUXStutas.values();
        int length = values.length;
        int i = 0;
        while (true) {
            if (i >= length) {
                break;
            }
            EAUXStutas temp = values[i];
            if (temp.getCode() == type) {
                source = temp;
                break;
            }
            i++;
        }
        this.mainboardEventLisenner.onAUXActivateStutas(source);
    }

    public void analyzeMcuUpgradeStatePro(byte[] buffer) {
        byte b = buffer[4];
        EUpgrade value = EUpgrade.ERROR;
        EUpgrade[] values = EUpgrade.values();
        int length = values.length;
        int i = 0;
        while (true) {
            if (i >= length) {
                break;
            }
            EUpgrade e = values[i];
            if (e.getCode() == b) {
                value = e;
                break;
            }
            i++;
        }
        this.mainboardEventLisenner.onMcuUpgradeState(value);
    }

    public void analyzeHornSoundValue(byte[] buffer) {
        byte b = buffer[4];
        this.mainboardEventLisenner.onHornSoundValue(buffer[5], buffer[6], buffer[7], buffer[8]);
    }

    public void analyzeMcuUpgradeDateByindexPro(byte[] buffer) {
        this.mainboardEventLisenner.onMcuUpgradeForGetDataByIndex(((buffer[4] & MotionEventCompat.ACTION_MASK) << 8) + (buffer[5] & MotionEventCompat.ACTION_MASK));
    }

    public void analyzeShowOrHideCarLayer(byte[] buffer) {
        ECarLayer eCarLayer = ECarLayer.ANDROID;
        byte b = buffer[5];
        ECarLayer[] values = ECarLayer.values();
        int length = values.length;
        int i = 0;
        while (true) {
            if (i >= length) {
                break;
            }
            ECarLayer value = values[i];
            if (b == value.getCode()) {
                eCarLayer = value;
                break;
            }
            i++;
        }
        this.mainboardEventLisenner.onShowOrHideCarLayer(eCarLayer);
    }

    public void analyzeReverseTpye(byte[] buffer) {
        EReverseTpye eReverseTpye = EReverseTpye.ORIGINAL_REVERSE;
        switch (buffer[5]) {
            case 0:
                eReverseTpye = EReverseTpye.ADD_REVERSE;
                break;
            case 1:
                eReverseTpye = EReverseTpye.ORIGINAL_REVERSE;
                break;
            case 2:
                eReverseTpye = EReverseTpye.REVERSE_360;
                break;
        }
        this.mainboardEventLisenner.obtainReverseType(eReverseTpye);
    }

    public void analyzeReverseMediaSet(byte[] buffer) {
        EReverserMediaSet eMediaSet = EReverserMediaSet.NOMAL;
        switch (buffer[5]) {
            case 0:
                eMediaSet = EReverserMediaSet.MUTE;
                break;
            case 1:
                eMediaSet = EReverserMediaSet.FLAT;
                break;
            case 2:
                eMediaSet = EReverserMediaSet.NOMAL;
                break;
        }
        this.mainboardEventLisenner.obtainReverseMediaSet(eMediaSet);
    }

    public void analyzeLanguageMediaSet(byte[] buffer) {
        ELanguage eLanguage = ELanguage.US;
        switch (buffer[5]) {
            case 0:
                eLanguage = ELanguage.SIMPLIFIED_CHINESE;
                break;
            case 1:
                eLanguage = ELanguage.TRADITIONAL_CHINESE;
                break;
            case 2:
                eLanguage = ELanguage.US;
                break;
        }
        this.mainboardEventLisenner.obtainLanguageMediaSet(eLanguage);
    }

    public void analyzeBenzTpye(byte[] buffer) {
        BenzModel.EBenzTpye eBenzTpye = BenzModel.EBenzTpye.C;
        byte b = buffer[5];
        BenzModel.EBenzTpye[] values = BenzModel.EBenzTpye.values();
        int length = values.length;
        int i = 0;
        while (true) {
            if (i >= length) {
                break;
            }
            BenzModel.EBenzTpye value = values[i];
            if (b == value.getCode()) {
                eBenzTpye = value;
                break;
            }
            i++;
        }
        this.mainboardEventLisenner.obtainBenzType(eBenzTpye);
    }

    public void analyzeBenzSize(byte[] buffer) {
        this.mainboardEventLisenner.obtainBenzSize(buffer[5]);
    }

    public void analyzeBrightnessValue(byte[] buffer) {
        this.mainboardEventLisenner.obtainBrightness(buffer[5]);
    }

    public void analyzeUpgradeStatePro(byte[] buffer) {
        byte b = buffer[3];
        ECanboxUpgrade value = ECanboxUpgrade.ERROR;
        ECanboxUpgrade[] values = ECanboxUpgrade.values();
        int length = values.length;
        int i = 0;
        while (true) {
            if (i >= length) {
                break;
            }
            ECanboxUpgrade e = values[i];
            if (e.getCode() == b) {
                value = e;
                break;
            }
            i++;
        }
        this.mainboardEventLisenner.onCanboxUpgradeState(value);
    }

    public void analyzeUpgradeDateByindexPro(byte[] buffer) {
        this.mainboardEventLisenner.onCanboxUpgradeForGetDataByIndex(((buffer[3] & MotionEventCompat.ACTION_MASK) << 8) + (buffer[4] & MotionEventCompat.ACTION_MASK));
    }

    public void logcatCanbox(String str) {
        this.mainboardEventLisenner.logcatCanbox(str);
    }

    public void analyzeStoreDateFromMCU(byte[] buffer) {
        List<Byte> datalist = new ArrayList<>();
        for (int i = 5; i < buffer.length - 1; i++) {
            datalist.add(Byte.valueOf(buffer[i]));
        }
        this.mainboardEventLisenner.obtainStoreData(datalist);
    }

    public void analyzeDVState(byte[] buffer) {
        boolean isPlaying = true;
        if (buffer[5] != 1) {
            isPlaying = false;
        }
        this.mainboardEventLisenner.obtainDVState(isPlaying);
    }

    public void analyzeGestureHand(byte[] buffer) {
        if (!this.iForbidGesture) {
            EIdriverEnum idriverValue = EIdriverEnum.NONE;
            switch (buffer[5]) {
                case 1:
                    idriverValue = EIdriverEnum.PRESS;
                    break;
                case 2:
                    idriverValue = EIdriverEnum.BACK;
                    break;
                case 3:
                    idriverValue = EIdriverEnum.UP;
                    break;
                case 4:
                    idriverValue = EIdriverEnum.DOWN;
                    break;
                case 7:
                    idriverValue = EIdriverEnum.TURN_RIGHT;
                    break;
                case 8:
                    idriverValue = EIdriverEnum.TURN_LEFT;
                    break;
                case 9:
                    if (this.isGesture09) {
                        this.isGesture09 = false;
                        idriverValue = EIdriverEnum.SPEECH;
                        break;
                    } else {
                        this.isGesture09 = true;
                        final Timer timer2 = new Timer();
                        timer2.schedule(new TimerTask() {
                            public void run() {
                                Mainboard.this.isGesture09 = false;
                                timer2.cancel();
                            }
                        }, 3000);
                        return;
                    }
            }
            EBtnStateEnum btnValue = EBtnStateEnum.BTN_DOWN;
            if (idriverValue != EIdriverEnum.SPEECH) {
                this.isGesture09 = false;
            }
            this.mainboardEventLisenner.onHandleIdriver(idriverValue, btnValue);
        }
    }

    public enum EControlSource {
        POWER_OFF(0),
        RADIO(1),
        DISC(2),
        TV(3),
        GPS(4),
        BT(5),
        iPod(6),
        AUX(7),
        USB(8),
        SD(9),
        DVR(10),
        MENU(11),
        CAMERA(12),
        TPMS(13),
        MOBLIE_WEB(14);
        
        private int code;

        private EControlSource(int value) {
            this.code = value;
        }

        public byte getCode() {
            return (byte) this.code;
        }
    }

    public enum EModeInfo {
        BASE_INFO(1),
        AIR_CONDITION(3),
        REAR_RADAR(4),
        FRONT_RADAR(5),
        CAR_DOOR(6),
        ORIGINAL_TIME(8),
        WHEEL_ANGLE(10),
        ORIGINAL_SOURCE(18),
        DASHBOARD_INFO(53),
        CANBOX_INFO(TransportMediator.KEYCODE_MEDIA_PAUSE);
        
        private int code;

        private EModeInfo(int value) {
            this.code = value;
        }

        public byte getCode() {
            return (byte) this.code;
        }
    }

    public enum EIdriverEnum {
        NONE(0),
        VOL_ADD(1),
        VOL_DES(2),
        NEXT(3),
        PREV(4),
        MODE(5),
        MUTE(6),
        BT(7),
        PICK_UP(8),
        HANG_UP(9),
        UP(10),
        DOWN(11),
        HOME(12),
        PRESS(13),
        RIGHT(14),
        LEFT(15),
        BACK(16),
        K_VOL_ADD(17),
        K_VOL_DES(18),
        RADIO(21),
        NAVI(22),
        MEDIA(23),
        POWER_OFF(24),
        SPEECH(25),
        ESC(26),
        K_HOME(27),
        SPEAKOFF(28),
        STAR_BTN(32),
        CARSET(33),
        CALL_HELP(34),
        CALL_SOS(35),
        CALL_FIX(36),
        TURN_RIGHT(64),
        TURN_LEFT(65);
        
        private int code;

        private EIdriverEnum(int value) {
            this.code = value;
        }

        public byte getCode() {
            return (byte) this.code;
        }
    }

    public enum EEqualizer {
        TREBLE(2),
        MIDTONES(1),
        BASS(0);
        
        private int code;

        private EEqualizer(int value) {
            this.code = value;
        }

        public byte getCode() {
            return (byte) this.code;
        }
    }

    public enum EBtnStateEnum {
        BTN_UP(0),
        BTN_DOWN(1),
        BTN_LONG_PRESS(2);
        
        private int code;

        private EBtnStateEnum(int value) {
            this.code = value;
        }

        public byte getCode() {
            return (byte) this.code;
        }
    }

    public enum EUpgrade {
        ERROR(0),
        GET_HERDER(1),
        UPGRADING(2),
        FINISH(3);
        
        private int code;

        private EUpgrade(int temp) {
            this.code = temp;
        }

        public byte getCode() {
            return (byte) this.code;
        }
    }

    public enum ECanboxUpgrade {
        ERROR(0),
        READY_UPGRADING(1),
        FINISH(2);
        
        private int code;

        private ECanboxUpgrade(int temp) {
            this.code = temp;
        }

        public byte getCode() {
            return (byte) this.code;
        }
    }

    public enum EReverseTpye {
        ADD_REVERSE(0),
        ORIGINAL_REVERSE(1),
        REVERSE_360(2);
        
        private int code;

        private EReverseTpye(int temp) {
            this.code = temp;
        }

        public byte getCode() {
            return (byte) this.code;
        }
    }

    public enum ECarLayer {
        ANDROID(16),
        RECORDER(32),
        DV(33),
        REVERSE_360(48),
        ORIGINAL(64),
        BT_CONNECT(65),
        ORIGINAL_REVERSE(66),
        SCREEN_OFF(96),
        REVERSE(80),
        QUERY(MotionEventCompat.ACTION_MASK);
        
        private int code;

        private ECarLayer(int temp) {
            this.code = temp;
        }

        public byte getCode() {
            return (byte) this.code;
        }
    }

    public enum EReverserMediaSet {
        MUTE(0),
        FLAT(1),
        NOMAL(2),
        QUERY(MotionEventCompat.ACTION_MASK);
        
        private int code;

        private EReverserMediaSet(int temp) {
            this.code = temp;
        }

        public byte getCode() {
            return (byte) this.code;
        }
    }

    public enum EAUXStutas {
        ACTIVATING(0),
        SUCCEED(1),
        FAILED(2);
        
        private int code;

        private EAUXStutas(int temp) {
            this.code = temp;
        }

        public byte getCode() {
            return (byte) this.code;
        }
    }

    public enum EAUXOperate {
        ACTIVATE(1),
        DEACTIVATE(2);
        
        private int code;

        private EAUXOperate(int temp) {
            this.code = temp;
        }

        public byte getCode() {
            return (byte) this.code;
        }
    }

    public enum ELanguage {
        SIMPLIFIED_CHINESE(0),
        TRADITIONAL_CHINESE(1),
        US(2);
        
        private int code;

        private ELanguage(int temp) {
            this.code = temp;
        }

        public byte getCode() {
            return (byte) this.code;
        }
    }

    public enum GestureType {
        RIGHT(1),
        LEFT(2),
        UP(3),
        DOWN(4),
        FONT(5),
        BACK(6);
        
        private int code;

        private GestureType(int temp) {
            this.code = temp;
        }

        public byte getCode() {
            return (byte) this.code;
        }
    }

    public void setiForbidGesture(boolean iForbidGesture2) {
        this.iForbidGesture = iForbidGesture2;
    }
}
