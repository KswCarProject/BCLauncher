package com.backaudio.android.driver.bluetooth;

import com.backaudio.android.driver.Utils;
import com.backaudio.android.driver.bluetooth.bc8mpprotocol.CallOutResult;
import com.backaudio.android.driver.bluetooth.bc8mpprotocol.CallingOutProtocol;
import com.backaudio.android.driver.bluetooth.bc8mpprotocol.DeviceNameProtocol;
import com.backaudio.android.driver.bluetooth.bc8mpprotocol.DeviceSwitchedProtocol;
import com.backaudio.android.driver.bluetooth.bc8mpprotocol.EPhoneStatus;
import com.backaudio.android.driver.bluetooth.bc8mpprotocol.EnterPairingModeResult;
import com.backaudio.android.driver.bluetooth.bc8mpprotocol.HangUpPhoneResult;
import com.backaudio.android.driver.bluetooth.bc8mpprotocol.IncomingCallProtocol;
import com.backaudio.android.driver.bluetooth.bc8mpprotocol.PhoneStatusProtocol;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import org.apache.log4j.net.SyslogAppender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BluetoothProtocolAnalyzer2 implements IBluetoothProtocolAnalyzer {
    private static final String TAG = "tag-bt2";
    private static Logger logger = LoggerFactory.getLogger(BluetoothProtocolAnalyzer2.class);
    public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.US);
    private ByteArrayOutputStream baos = new ByteArrayOutputStream();
    DeviceNameProtocol dnp = new DeviceNameProtocol((String) null, "");
    private IBluetoothEventHandler eventHandler = null;
    private byte mNextExpectedValue = -1;
    private boolean mValidProtocol = false;
    public FileOutputStream out = null;

    public void setEventHandler(IBluetoothEventHandler eventHandler2) {
        logger.debug("tag-bt2            handler");
        this.eventHandler = eventHandler2;
    }

    public BluetoothProtocolAnalyzer2() {
        try {
            this.out = new FileOutputStream("/data/data/com.touchus.benchilauncher/bluetooth2.txt", true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void push(byte[] buffer) {
        int len = buffer.length;
        for (int i = 0; i < len; i++) {
            if (buffer[i] == 13) {
                this.mNextExpectedValue = 10;
            } else if (buffer[i] != 10) {
                this.mNextExpectedValue = -1;
            } else if (this.mNextExpectedValue == 10) {
                this.mValidProtocol = true;
            }
            if (buffer[i] == -1) {
                buffer[i] = 10;
            }
            this.baos.write(buffer, i, 1);
            if (this.mValidProtocol) {
                try {
                    analyze(this.baos.toByteArray());
                } catch (Exception e) {
                    logger.debug("tag-bt2analyze error" + e);
                } finally {
                    reset();
                }
            }
        }
    }

    private void analyze(byte[] buffer) {
        logger.debug("tag-bt2 hexstring: " + new String(buffer));
        if (this.out != null) {
            synchronized (this.out) {
                try {
                    this.out.write(buffer);
                    this.out.write(("\t" + sdf.format(new Date()) + "\r\n").getBytes());
                    this.out.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        if (this.eventHandler == null || buffer == null || buffer.length < 4) {
            logger.debug("tag-bt2--------invalid--------");
            return;
        }
        char ch1 = (char) buffer[0];
        char ch2 = (char) buffer[1];
        switch (ch1) {
            case 'D':
                handleStartD(ch2, buffer);
                return;
            case SyslogAppender.LOG_CRON /*72*/:
                logger.debug("tag-bt2---start H----" + new String(buffer, 0, buffer.length - 2));
                return;
            case 'I':
                handleStartI(ch2, buffer);
                return;
            case 'M':
                handleStartM(ch2, buffer);
                return;
            case 'N':
                logger.debug("tag-bt2---start N----" + new String(buffer, 0, buffer.length - 2));
                return;
            case 'O':
                logger.debug("tag-bt2---start O----" + new String(buffer, 0, buffer.length - 2));
                return;
            case SyslogAppender.LOG_AUTHPRIV /*80*/:
                handleStartP(ch2, buffer);
                return;
            case 'S':
                logger.debug("tag-bt2---start S----" + new String(buffer, 0, buffer.length - 2));
                return;
            case 'T':
                if (ch2 == '1' || ch2 == '0') {
                    logger.debug("tag-bt2T: 车机（蓝牙/LOCAL）出声音");
                    this.eventHandler.ondeviceSwitchedProtocol(new DeviceSwitchedProtocol((String) null, String.valueOf(ch2)));
                    return;
                }
                return;
            default:
                return;
        }
    }

    private void handleStartD(char ch, byte[] buffer) {
        int len = buffer.length;
        switch (ch) {
            case 'B':
                String value = new String(buffer, 2, len - 4);
                this.dnp.setMACAddr(value);
                this.eventHandler.onDeviceName(this.dnp);
                logger.debug("tag-bt2MN: MACAddr() PIN = " + value);
                return;
            default:
                return;
        }
    }

    private void handleStartI(char ch, byte[] buffer) {
        int len = buffer.length;
        switch (ch) {
            case 'A':
                logger.debug("tag-bt2IA: HFP断开");
                PhoneStatusProtocol psp = new PhoneStatusProtocol((String) null, "");
                psp.setPhoneStatus(EPhoneStatus.UNCONNECT);
                this.eventHandler.onPhoneStatus(psp);
                return;
            case 'B':
                logger.debug("tag-bt2IB: HFP连接成功");
                PhoneStatusProtocol psp2 = new PhoneStatusProtocol((String) null, "");
                psp2.setPhoneStatus(EPhoneStatus.CONNECTED);
                this.eventHandler.onPhoneStatus(psp2);
                return;
            case 'C':
                this.eventHandler.onCallOut(new CallOutResult((String) null, "0"));
                logger.debug("tag-bt2IC: onCallOut() ");
                return;
            case 'D':
                String number = new String(buffer, 2, len - 4);
                this.eventHandler.onIncomingCall(new IncomingCallProtocol((String) null, number));
                logger.debug("tag-bt2ID: onIncomingCall() " + number);
                return;
            case 'E':
                logger.debug("tag-bt2三方来电：" + new String(buffer, 2, len - 4));
                return;
            case 'F':
                this.eventHandler.onHangUpPhone(new HangUpPhoneResult((String) null, "0"));
                logger.debug("tag-bt2IF: 挂机------------");
                return;
            case 'G':
                logger.debug("tag-bt2IG: 已接通-----------");
                PhoneStatusProtocol psp3 = new PhoneStatusProtocol((String) null, "");
                psp3.setPhoneStatus(EPhoneStatus.TALKING);
                this.eventHandler.onPhoneStatus(psp3);
                return;
            case 'I':
                logger.debug("tag-bt2II: 进入配对");
                this.eventHandler.onPairingModeResult(new EnterPairingModeResult((String) null, "0"));
                return;
            case 'J':
                logger.debug("tag-bt2IJ: 退出配对");
                this.eventHandler.onPairingModeResult(new EnterPairingModeResult((String) null, "1"));
                return;
            case 'O':
                logger.debug("tag-bt2打开/关闭咪头");
                return;
            case 'R':
                String number2 = new String(buffer, 2, len - 4);
                this.eventHandler.onPhoneCallingOut(new CallingOutProtocol((String) null, number2));
                logger.debug("tag-bt2IR: onPhoneCallingOut() " + number2);
                return;
            case 'S':
                logger.debug("tag-bt2IS: 蓝牙初始化完成");
                return;
            case 'V':
                logger.debug("tag-bt2IV: HFP连接中");
                PhoneStatusProtocol psp4 = new PhoneStatusProtocol((String) null, "");
                psp4.setPhoneStatus(EPhoneStatus.CONNECTING);
                this.eventHandler.onPhoneStatus(psp4);
                return;
            default:
                return;
        }
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r9v1, resolved type: byte} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r9v3, resolved type: byte} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r9v4, resolved type: byte} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void handleStartM(char r34, byte[] r35) {
        /*
            r33 = this;
            r0 = r35
            int r12 = r0.length
            switch(r34) {
                case 65: goto L_0x0007;
                case 66: goto L_0x002e;
                case 67: goto L_0x005b;
                case 68: goto L_0x007b;
                case 69: goto L_0x0006;
                case 70: goto L_0x0006;
                case 71: goto L_0x009c;
                case 72: goto L_0x0006;
                case 73: goto L_0x02ca;
                case 74: goto L_0x0006;
                case 75: goto L_0x0006;
                case 76: goto L_0x0006;
                case 77: goto L_0x018b;
                case 78: goto L_0x01d2;
                case 79: goto L_0x0006;
                case 80: goto L_0x0006;
                case 81: goto L_0x0006;
                case 82: goto L_0x0006;
                case 83: goto L_0x0006;
                case 84: goto L_0x0006;
                case 85: goto L_0x00e3;
                case 86: goto L_0x0006;
                case 87: goto L_0x0146;
                case 88: goto L_0x0219;
                case 89: goto L_0x0121;
                default: goto L_0x0006;
            }
        L_0x0006:
            return
        L_0x0007:
            org.slf4j.Logger r29 = logger
            java.lang.String r30 = "tag-bt2MA: 音乐暂停中/连接成功"
            r29.debug(r30)
            com.backaudio.android.driver.bluetooth.bc8mpprotocol.MediaPlayStatusProtocol r15 = new com.backaudio.android.driver.bluetooth.bc8mpprotocol.MediaPlayStatusProtocol
            java.lang.String r29 = ""
            java.lang.String r30 = ""
            r0 = r29
            r1 = r30
            r15.<init>(r0, r1)
            com.backaudio.android.driver.bluetooth.bc8mpprotocol.EMediaPlayStatus r29 = com.backaudio.android.driver.bluetooth.bc8mpprotocol.EMediaPlayStatus.PAUSE
            r0 = r29
            r15.setMediaPlayStatus(r0)
            r0 = r33
            com.backaudio.android.driver.bluetooth.IBluetoothEventHandler r0 = r0.eventHandler
            r29 = r0
            r0 = r29
            r0.onMediaPlayStatus(r15)
            goto L_0x0006
        L_0x002e:
            org.slf4j.Logger r29 = logger
            java.lang.String r30 = "tag-bt2MB: 音乐播放中"
            r29.debug(r30)
            com.backaudio.android.driver.bluetooth.bc8mpprotocol.MediaPlayStatusProtocol r16 = new com.backaudio.android.driver.bluetooth.bc8mpprotocol.MediaPlayStatusProtocol
            java.lang.String r29 = ""
            java.lang.String r30 = ""
            r0 = r16
            r1 = r29
            r2 = r30
            r0.<init>(r1, r2)
            com.backaudio.android.driver.bluetooth.bc8mpprotocol.EMediaPlayStatus r29 = com.backaudio.android.driver.bluetooth.bc8mpprotocol.EMediaPlayStatus.PLAYING
            r0 = r16
            r1 = r29
            r0.setMediaPlayStatus(r1)
            r0 = r33
            com.backaudio.android.driver.bluetooth.IBluetoothEventHandler r0 = r0.eventHandler
            r29 = r0
            r0 = r29
            r1 = r16
            r0.onMediaPlayStatus(r1)
            goto L_0x0006
        L_0x005b:
            org.slf4j.Logger r29 = logger
            java.lang.String r30 = "tag-bt2MC: 车机（蓝牙/LOCAL）出声音"
            r29.debug(r30)
            com.backaudio.android.driver.bluetooth.bc8mpprotocol.DeviceSwitchedProtocol r6 = new com.backaudio.android.driver.bluetooth.bc8mpprotocol.DeviceSwitchedProtocol
            r29 = 0
            java.lang.String r30 = "0"
            r0 = r29
            r1 = r30
            r6.<init>(r0, r1)
            r0 = r33
            com.backaudio.android.driver.bluetooth.IBluetoothEventHandler r0 = r0.eventHandler
            r29 = r0
            r0 = r29
            r0.ondeviceSwitchedProtocol(r6)
            goto L_0x0006
        L_0x007b:
            org.slf4j.Logger r29 = logger
            java.lang.String r30 = "tag-bt2MD: 手机（REMOTE）出声音"
            r29.debug(r30)
            com.backaudio.android.driver.bluetooth.bc8mpprotocol.DeviceSwitchedProtocol r7 = new com.backaudio.android.driver.bluetooth.bc8mpprotocol.DeviceSwitchedProtocol
            r29 = 0
            java.lang.String r30 = "1"
            r0 = r29
            r1 = r30
            r7.<init>(r0, r1)
            r0 = r33
            com.backaudio.android.driver.bluetooth.IBluetoothEventHandler r0 = r0.eventHandler
            r29 = r0
            r0 = r29
            r0.ondeviceSwitchedProtocol(r7)
            goto L_0x0006
        L_0x009c:
            r29 = 2
            byte r29 = r35[r29]
            int r26 = r29 + -48
            java.lang.String r27 = java.lang.String.valueOf(r26)
            com.backaudio.android.driver.bluetooth.bc8mpprotocol.PhoneStatusProtocol r21 = new com.backaudio.android.driver.bluetooth.bc8mpprotocol.PhoneStatusProtocol
            r29 = 0
            java.lang.String r30 = ""
            r0 = r21
            r1 = r29
            r2 = r30
            r0.<init>(r1, r2)
            r0 = r21
            r1 = r27
            r0.setPhoneStatus((java.lang.String) r1)
            r0 = r33
            com.backaudio.android.driver.bluetooth.IBluetoothEventHandler r0 = r0.eventHandler
            r29 = r0
            r0 = r29
            r1 = r21
            r0.onPhoneStatus(r1)
            org.slf4j.Logger r29 = logger
            java.lang.StringBuilder r30 = new java.lang.StringBuilder
            java.lang.String r31 = "tag-bt2MG: onPhoneStatus() "
            r30.<init>(r31)
            com.backaudio.android.driver.bluetooth.bc8mpprotocol.EPhoneStatus r31 = r21.getPhoneStatus()
            java.lang.StringBuilder r30 = r30.append(r31)
            java.lang.String r30 = r30.toString()
            r29.debug(r30)
            goto L_0x0006
        L_0x00e3:
            r29 = 2
            byte r29 = r35[r29]
            int r23 = r29 + -48
            org.slf4j.Logger r29 = logger
            java.lang.StringBuilder r30 = new java.lang.StringBuilder
            java.lang.String r31 = "tag-bt2MU: "
            r30.<init>(r31)
            r0 = r30
            r1 = r23
            java.lang.StringBuilder r30 = r0.append(r1)
            java.lang.String r30 = r30.toString()
            r29.debug(r30)
            com.backaudio.android.driver.bluetooth.bc8mpprotocol.MediaPlayStatusProtocol r18 = new com.backaudio.android.driver.bluetooth.bc8mpprotocol.MediaPlayStatusProtocol
            java.lang.String r29 = "A2DPSTAT"
            java.lang.String r30 = java.lang.String.valueOf(r23)
            r0 = r18
            r1 = r29
            r2 = r30
            r0.<init>(r1, r2)
            r0 = r33
            com.backaudio.android.driver.bluetooth.IBluetoothEventHandler r0 = r0.eventHandler
            r29 = r0
            r0 = r29
            r1 = r18
            r0.onMediaPlayStatus(r1)
            goto L_0x0006
        L_0x0121:
            org.slf4j.Logger r29 = logger
            java.lang.String r30 = "tag-bt2MY: A2DP断开"
            r29.debug(r30)
            com.backaudio.android.driver.bluetooth.bc8mpprotocol.MediaPlayStatusProtocol r17 = new com.backaudio.android.driver.bluetooth.bc8mpprotocol.MediaPlayStatusProtocol
            java.lang.String r29 = "A2DPSTAT"
            java.lang.String r30 = "0"
            r0 = r17
            r1 = r29
            r2 = r30
            r0.<init>(r1, r2)
            r0 = r33
            com.backaudio.android.driver.bluetooth.IBluetoothEventHandler r0 = r0.eventHandler
            r29 = r0
            r0 = r29
            r1 = r17
            r0.onMediaPlayStatus(r1)
            goto L_0x0006
        L_0x0146:
            java.lang.String r27 = new java.lang.String
            r29 = 2
            int r30 = r12 + -4
            r0 = r27
            r1 = r35
            r2 = r29
            r3 = r30
            r0.<init>(r1, r2, r3)
            com.backaudio.android.driver.bluetooth.bc8mpprotocol.VersionProtocol r28 = new com.backaudio.android.driver.bluetooth.bc8mpprotocol.VersionProtocol
            r29 = 0
            r0 = r28
            r1 = r29
            r2 = r27
            r0.<init>(r1, r2)
            r0 = r33
            com.backaudio.android.driver.bluetooth.IBluetoothEventHandler r0 = r0.eventHandler
            r29 = r0
            r0 = r29
            r1 = r28
            r0.onVersion(r1)
            org.slf4j.Logger r29 = logger
            java.lang.StringBuilder r30 = new java.lang.StringBuilder
            java.lang.String r31 = "tag-bt2MW: onVersion() "
            r30.<init>(r31)
            r0 = r30
            r1 = r27
            java.lang.StringBuilder r30 = r0.append(r1)
            java.lang.String r30 = r30.toString()
            r29.debug(r30)
            goto L_0x0006
        L_0x018b:
            java.lang.String r27 = new java.lang.String
            r29 = 2
            int r30 = r12 + -4
            r0 = r27
            r1 = r35
            r2 = r29
            r3 = r30
            r0.<init>(r1, r2, r3)
            r0 = r33
            com.backaudio.android.driver.bluetooth.bc8mpprotocol.DeviceNameProtocol r0 = r0.dnp
            r29 = r0
            r0 = r29
            r1 = r27
            r0.setDeviceName(r1)
            r0 = r33
            com.backaudio.android.driver.bluetooth.IBluetoothEventHandler r0 = r0.eventHandler
            r29 = r0
            r0 = r33
            com.backaudio.android.driver.bluetooth.bc8mpprotocol.DeviceNameProtocol r0 = r0.dnp
            r30 = r0
            r29.onDeviceName(r30)
            org.slf4j.Logger r29 = logger
            java.lang.StringBuilder r30 = new java.lang.StringBuilder
            java.lang.String r31 = "tag-bt2MM: onDeviceName() name = "
            r30.<init>(r31)
            r0 = r30
            r1 = r27
            java.lang.StringBuilder r30 = r0.append(r1)
            java.lang.String r30 = r30.toString()
            r29.debug(r30)
            goto L_0x0006
        L_0x01d2:
            java.lang.String r27 = new java.lang.String
            r29 = 2
            int r30 = r12 + -4
            r0 = r27
            r1 = r35
            r2 = r29
            r3 = r30
            r0.<init>(r1, r2, r3)
            r0 = r33
            com.backaudio.android.driver.bluetooth.bc8mpprotocol.DeviceNameProtocol r0 = r0.dnp
            r29 = r0
            r0 = r29
            r1 = r27
            r0.setPIN(r1)
            r0 = r33
            com.backaudio.android.driver.bluetooth.IBluetoothEventHandler r0 = r0.eventHandler
            r29 = r0
            r0 = r33
            com.backaudio.android.driver.bluetooth.bc8mpprotocol.DeviceNameProtocol r0 = r0.dnp
            r30 = r0
            r29.onDeviceName(r30)
            org.slf4j.Logger r29 = logger
            java.lang.StringBuilder r30 = new java.lang.StringBuilder
            java.lang.String r31 = "tag-bt2MN: onDeviceName() PIN = "
            r30.<init>(r31)
            r0 = r30
            r1 = r27
            java.lang.StringBuilder r30 = r0.append(r1)
            java.lang.String r30 = r30.toString()
            r29.debug(r30)
            goto L_0x0006
        L_0x0219:
            r4 = 0
            r19 = 0
            r29 = 2
            byte r29 = r35[r29]
            if (r29 >= 0) goto L_0x0281
            r29 = 2
            byte r29 = r35[r29]
            r0 = r29
            int r9 = r0 + 256
        L_0x022a:
            java.lang.String r4 = new java.lang.String
            r29 = 3
            r30 = 12
            r0 = r35
            r1 = r29
            r2 = r30
            r4.<init>(r0, r1, r2)
            r29 = 48
            r0 = r29
            if (r9 != r0) goto L_0x0286
            com.backaudio.android.driver.bluetooth.bc8mpprotocol.ConnectedDeviceProtocol r5 = new com.backaudio.android.driver.bluetooth.bc8mpprotocol.ConnectedDeviceProtocol
            r29 = 0
            r0 = r29
            r5.<init>(r0, r4)
            java.lang.String r29 = new java.lang.String
            r30 = 15
            int r31 = r12 + -17
            r0 = r29
            r1 = r35
            r2 = r30
            r3 = r31
            r0.<init>(r1, r2, r3)
            r0 = r29
            r5.setDeviceName(r0)
            r0 = r33
            com.backaudio.android.driver.bluetooth.IBluetoothEventHandler r0 = r0.eventHandler
            r29 = r0
            r0 = r29
            r0.onConnectedDevice(r5)
            org.slf4j.Logger r29 = logger
            java.lang.StringBuilder r30 = new java.lang.StringBuilder
            java.lang.String r31 = "tag-bt2MX: address="
            r30.<init>(r31)
            r0 = r30
            java.lang.StringBuilder r30 = r0.append(r4)
            java.lang.String r30 = r30.toString()
            r29.debug(r30)
            goto L_0x0006
        L_0x0281:
            r29 = 2
            byte r9 = r35[r29]
            goto L_0x022a
        L_0x0286:
            java.lang.String r19 = new java.lang.String
            r29 = 15
            int r30 = r12 + -17
            r0 = r19
            r1 = r35
            r2 = r29
            r3 = r30
            r0.<init>(r1, r2, r3)
            r0 = r33
            com.backaudio.android.driver.bluetooth.IBluetoothEventHandler r0 = r0.eventHandler
            r29 = r0
            r0 = r29
            r1 = r19
            r0.onPairedDevice(r4, r1)
            org.slf4j.Logger r29 = logger
            java.lang.StringBuilder r30 = new java.lang.StringBuilder
            java.lang.String r31 = "tag-bt2MX: address="
            r30.<init>(r31)
            r0 = r30
            java.lang.StringBuilder r30 = r0.append(r4)
            java.lang.String r31 = " name="
            java.lang.StringBuilder r30 = r30.append(r31)
            r0 = r30
            r1 = r19
            java.lang.StringBuilder r30 = r0.append(r1)
            java.lang.String r30 = r30.toString()
            r29.debug(r30)
            goto L_0x0006
        L_0x02ca:
            java.lang.String r29 = new java.lang.String
            r30 = 2
            int r31 = r12 + -4
            r0 = r29
            r1 = r35
            r2 = r30
            r3 = r31
            r0.<init>(r1, r2, r3)
            java.lang.String r30 = "\n"
            java.lang.String[] r10 = r29.split(r30)
            int r11 = r10.length
            com.backaudio.android.driver.bluetooth.bc8mpprotocol.MediaInfoProtocol r14 = new com.backaudio.android.driver.bluetooth.bc8mpprotocol.MediaInfoProtocol
            r14.<init>()
            r29 = 1
            r0 = r29
            r14.setAnalyzed(r0)
            if (r11 <= 0) goto L_0x0377
            r29 = 0
            r29 = r10[r29]
        L_0x02f4:
            r0 = r29
            r14.setTitle(r0)
            r29 = 1
            r0 = r29
            if (r11 <= r0) goto L_0x037b
            r29 = 1
            r29 = r10[r29]
        L_0x0303:
            r0 = r29
            r14.setArtist(r0)
            r24 = 0
            r20 = 0
            r25 = 0
            r29 = 2
            r0 = r29
            if (r11 <= r0) goto L_0x037e
            r29 = 2
            r29 = r10[r29]     // Catch:{ NumberFormatException -> 0x0381 }
        L_0x0318:
            int r24 = java.lang.Integer.parseInt(r29)     // Catch:{ NumberFormatException -> 0x0381 }
        L_0x031c:
            r29 = 3
            r0 = r29
            if (r11 <= r0) goto L_0x0386
            r29 = 3
            r29 = r10[r29]     // Catch:{ NumberFormatException -> 0x0389 }
        L_0x0326:
            int r20 = java.lang.Integer.parseInt(r29)     // Catch:{ NumberFormatException -> 0x0389 }
        L_0x032a:
            r29 = 4
            r0 = r29
            if (r11 <= r0) goto L_0x038e
            r29 = 4
            r29 = r10[r29]     // Catch:{ NumberFormatException -> 0x0391 }
        L_0x0334:
            int r25 = java.lang.Integer.parseInt(r29)     // Catch:{ NumberFormatException -> 0x0391 }
        L_0x0338:
            r0 = r24
            r14.setPlayTime(r0)
            r0 = r20
            r14.setNumber(r0)
            r0 = r25
            r14.setTotalNumber(r0)
            r0 = r33
            com.backaudio.android.driver.bluetooth.IBluetoothEventHandler r0 = r0.eventHandler
            r29 = r0
            r0 = r29
            r0.onMediaInfo(r14)
            java.lang.String r13 = ""
            int r0 = r10.length
            r30 = r0
            r29 = 0
        L_0x0359:
            r0 = r29
            r1 = r30
            if (r0 < r1) goto L_0x0396
            org.slf4j.Logger r29 = logger
            java.lang.StringBuilder r30 = new java.lang.StringBuilder
            java.lang.String r31 = "tag-bt2MI: -+- "
            r30.<init>(r31)
            r0 = r30
            java.lang.StringBuilder r30 = r0.append(r13)
            java.lang.String r30 = r30.toString()
            r29.debug(r30)
            goto L_0x0006
        L_0x0377:
            java.lang.String r29 = "UNKNOWN"
            goto L_0x02f4
        L_0x037b:
            java.lang.String r29 = "UNKNOWN"
            goto L_0x0303
        L_0x037e:
            java.lang.String r29 = "-1"
            goto L_0x0318
        L_0x0381:
            r8 = move-exception
            r8.printStackTrace()
            goto L_0x031c
        L_0x0386:
            java.lang.String r29 = "-1"
            goto L_0x0326
        L_0x0389:
            r8 = move-exception
            r8.printStackTrace()
            goto L_0x032a
        L_0x038e:
            java.lang.String r29 = "-1"
            goto L_0x0334
        L_0x0391:
            r8 = move-exception
            r8.printStackTrace()
            goto L_0x0338
        L_0x0396:
            r22 = r10[r29]
            java.lang.StringBuilder r31 = new java.lang.StringBuilder
            java.lang.String r32 = java.lang.String.valueOf(r13)
            r31.<init>(r32)
            r0 = r31
            r1 = r22
            java.lang.StringBuilder r31 = r0.append(r1)
            java.lang.String r32 = " "
            java.lang.StringBuilder r31 = r31.append(r32)
            java.lang.String r13 = r31.toString()
            int r29 = r29 + 1
            goto L_0x0359
        */
        throw new UnsupportedOperationException("Method not decompiled: com.backaudio.android.driver.bluetooth.BluetoothProtocolAnalyzer2.handleStartM(char, byte[]):void");
    }

    private void handleStartP(char ch, byte[] buffer) {
        int len = buffer.length;
        switch (ch) {
            case SyslogAppender.LOG_LPR /*48*/:
                logger.debug("tag-bt2P0: onPairingModeResult() fail");
                this.eventHandler.onPairingModeResult(new EnterPairingModeResult((String) null, (String) null));
                return;
            case '1':
                logger.debug("tag-bt2P1: onPairingModeResult() success");
                this.eventHandler.onPairingModeResult(new EnterPairingModeResult((String) null, "0"));
                return;
            case 'A':
                logger.debug("tag-bt2PA: " + new String(buffer));
                return;
            case 'B':
                String[] kv = new String(buffer, 2, len - 4).split("\n");
                if (kv == null || kv.length < 2) {
                    logger.debug("tag-bt2PB: invalid protocol name=" + kv[0]);
                    return;
                }
                String name = kv[0];
                String number = kv[1];
                this.eventHandler.onPhoneBook(name, number);
                logger.debug("tag-bt2PB: name=" + name + " number=" + number);
                return;
            case 'C':
                logger.debug("tag-bt2PC: onFinishDownloadPhoneBook()");
                this.eventHandler.onFinishDownloadPhoneBook();
                return;
            case 'D':
                String[] kv1 = new String(buffer, 2, len - 4).split("\n");
                if (kv1 == null || kv1.length < 2) {
                    logger.debug("tag-bt2PD: invalid protocol name=" + kv1[0]);
                    return;
                }
                String name2 = kv1[0];
                String number2 = kv1[1];
                this.eventHandler.onPhoneBook((String) null, (String) null);
                logger.debug("tag-bt2PD: name=" + name2 + " number=" + number2);
                return;
            default:
                return;
        }
    }

    public void reset() {
        this.mNextExpectedValue = -1;
        this.mValidProtocol = false;
        this.baos.reset();
    }

    public void push(byte[] buffer, int off, int len) {
        byte[] newbuffer = Arrays.copyOf(buffer, len);
        logger.debug("tag-bt2 bluetoothprot recv::" + Utils.byteArrayToHexString(newbuffer, 0, newbuffer.length));
        try {
            Utils.saveLogLine(Utils.byteArrayToHexString(newbuffer, 0, newbuffer.length), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        push(newbuffer);
    }
}
