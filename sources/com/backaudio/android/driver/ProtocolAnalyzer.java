package com.backaudio.android.driver;

import android.support.v4.media.TransportMediator;
import android.util.Log;
import java.io.ByteArrayOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProtocolAnalyzer {
    static final Object eventLock = new Object();
    private final String TAG = "driverlog";
    int canCurrentProtocolLength = 0;
    ByteArrayOutputStream canProtocolBuffer = new ByteArrayOutputStream();
    int canProtocolLength = 0;
    int currentProtocolLength = 0;
    boolean iDown;
    private Logger logger = LoggerFactory.getLogger(ProtocolAnalyzer.class);
    ByteArrayOutputStream protocolBuffer = new ByteArrayOutputStream();
    int protocolLength = 0;
    private final byte[] tmp = new byte[8];
    int touch_x;
    int touch_y;

    /* JADX INFO: finally extract failed */
    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void analyzeCanbox(byte[] r10) {
        /*
            r9 = this;
            r4 = 5
        L_0x0001:
            int r5 = r10.length
            int r5 = r5 + -1
            if (r4 < r5) goto L_0x0007
            return
        L_0x0007:
            byte r0 = r10[r4]     // Catch:{ Exception -> 0x005c }
            int r5 = r9.canCurrentProtocolLength     // Catch:{ Exception -> 0x005c }
            switch(r5) {
                case 0: goto L_0x0044;
                case 1: goto L_0x0080;
                case 2: goto L_0x0094;
                default: goto L_0x000e;
            }     // Catch:{ Exception -> 0x005c }
        L_0x000e:
            int r5 = r9.canCurrentProtocolLength     // Catch:{ Exception -> 0x005c }
            int r6 = r9.canProtocolLength     // Catch:{ Exception -> 0x005c }
            if (r5 >= r6) goto L_0x00d8
            java.io.ByteArrayOutputStream r5 = r9.canProtocolBuffer     // Catch:{ Exception -> 0x005c }
            r6 = 1
            byte[] r6 = new byte[r6]     // Catch:{ Exception -> 0x005c }
            r7 = 0
            r6[r7] = r0     // Catch:{ Exception -> 0x005c }
            r7 = 0
            r8 = 1
            r5.write(r6, r7, r8)     // Catch:{ Exception -> 0x005c }
            int r5 = r9.canCurrentProtocolLength     // Catch:{ Exception -> 0x005c }
            int r5 = r5 + 1
            r9.canCurrentProtocolLength = r5     // Catch:{ Exception -> 0x005c }
            int r5 = r9.canCurrentProtocolLength     // Catch:{ Exception -> 0x005c }
            int r6 = r9.canProtocolLength     // Catch:{ Exception -> 0x005c }
            if (r5 != r6) goto L_0x0041
            java.io.ByteArrayOutputStream r5 = r9.canProtocolBuffer     // Catch:{ Exception -> 0x00ba }
            byte[] r5 = r5.toByteArray()     // Catch:{ Exception -> 0x00ba }
            r9.newProtocol(r5)     // Catch:{ Exception -> 0x00ba }
            java.io.ByteArrayOutputStream r5 = r9.canProtocolBuffer     // Catch:{ Exception -> 0x005c }
            r5.reset()     // Catch:{ Exception -> 0x005c }
            r5 = 0
            r9.canCurrentProtocolLength = r5     // Catch:{ Exception -> 0x005c }
            r5 = 0
            r9.canProtocolLength = r5     // Catch:{ Exception -> 0x005c }
        L_0x0041:
            int r4 = r4 + 1
            goto L_0x0001
        L_0x0044:
            r5 = 46
            if (r0 != r5) goto L_0x0061
            java.io.ByteArrayOutputStream r5 = r9.canProtocolBuffer     // Catch:{ Exception -> 0x005c }
            r6 = 1
            byte[] r6 = new byte[r6]     // Catch:{ Exception -> 0x005c }
            r7 = 0
            r6[r7] = r0     // Catch:{ Exception -> 0x005c }
            r7 = 0
            r8 = 1
            r5.write(r6, r7, r8)     // Catch:{ Exception -> 0x005c }
            int r5 = r9.canCurrentProtocolLength     // Catch:{ Exception -> 0x005c }
            int r5 = r5 + 1
            r9.canCurrentProtocolLength = r5     // Catch:{ Exception -> 0x005c }
            goto L_0x0041
        L_0x005c:
            r2 = move-exception
            r2.printStackTrace()
            goto L_0x0041
        L_0x0061:
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x005c }
            java.lang.String r6 = "canbox: require 0x2E or 0xFF but "
            r5.<init>(r6)     // Catch:{ Exception -> 0x005c }
            java.lang.String r6 = java.lang.Integer.toHexString(r0)     // Catch:{ Exception -> 0x005c }
            java.lang.StringBuilder r5 = r5.append(r6)     // Catch:{ Exception -> 0x005c }
            java.lang.String r6 = "\n"
            java.lang.StringBuilder r5 = r5.append(r6)     // Catch:{ Exception -> 0x005c }
            java.lang.String r1 = r5.toString()     // Catch:{ Exception -> 0x005c }
            java.lang.String r5 = "driverlog"
            android.util.Log.e(r5, r1)     // Catch:{ Exception -> 0x005c }
            goto L_0x0041
        L_0x0080:
            java.io.ByteArrayOutputStream r5 = r9.canProtocolBuffer     // Catch:{ Exception -> 0x005c }
            r6 = 1
            byte[] r6 = new byte[r6]     // Catch:{ Exception -> 0x005c }
            r7 = 0
            r6[r7] = r0     // Catch:{ Exception -> 0x005c }
            r7 = 0
            r8 = 1
            r5.write(r6, r7, r8)     // Catch:{ Exception -> 0x005c }
            int r5 = r9.canCurrentProtocolLength     // Catch:{ Exception -> 0x005c }
            int r5 = r5 + 1
            r9.canCurrentProtocolLength = r5     // Catch:{ Exception -> 0x005c }
            goto L_0x0041
        L_0x0094:
            r9.canProtocolLength = r0     // Catch:{ Exception -> 0x005c }
            int r5 = r9.canProtocolLength     // Catch:{ Exception -> 0x005c }
            if (r5 >= 0) goto L_0x00a0
            int r5 = r9.canProtocolLength     // Catch:{ Exception -> 0x005c }
            int r5 = r5 + 256
            r9.canProtocolLength = r5     // Catch:{ Exception -> 0x005c }
        L_0x00a0:
            int r5 = r9.canProtocolLength     // Catch:{ Exception -> 0x005c }
            int r5 = r5 + 4
            r9.canProtocolLength = r5     // Catch:{ Exception -> 0x005c }
            java.io.ByteArrayOutputStream r5 = r9.canProtocolBuffer     // Catch:{ Exception -> 0x005c }
            r6 = 1
            byte[] r6 = new byte[r6]     // Catch:{ Exception -> 0x005c }
            r7 = 0
            r6[r7] = r0     // Catch:{ Exception -> 0x005c }
            r7 = 0
            r8 = 1
            r5.write(r6, r7, r8)     // Catch:{ Exception -> 0x005c }
            int r5 = r9.canCurrentProtocolLength     // Catch:{ Exception -> 0x005c }
            int r5 = r5 + 1
            r9.canCurrentProtocolLength = r5     // Catch:{ Exception -> 0x005c }
            goto L_0x0041
        L_0x00ba:
            r2 = move-exception
            r2.printStackTrace()     // Catch:{ all -> 0x00cb }
            java.io.ByteArrayOutputStream r5 = r9.canProtocolBuffer     // Catch:{ Exception -> 0x005c }
            r5.reset()     // Catch:{ Exception -> 0x005c }
            r5 = 0
            r9.canCurrentProtocolLength = r5     // Catch:{ Exception -> 0x005c }
            r5 = 0
            r9.canProtocolLength = r5     // Catch:{ Exception -> 0x005c }
            goto L_0x0041
        L_0x00cb:
            r5 = move-exception
            java.io.ByteArrayOutputStream r6 = r9.canProtocolBuffer     // Catch:{ Exception -> 0x005c }
            r6.reset()     // Catch:{ Exception -> 0x005c }
            r6 = 0
            r9.canCurrentProtocolLength = r6     // Catch:{ Exception -> 0x005c }
            r6 = 0
            r9.canProtocolLength = r6     // Catch:{ Exception -> 0x005c }
            throw r5     // Catch:{ Exception -> 0x005c }
        L_0x00d8:
            int r5 = r9.canCurrentProtocolLength     // Catch:{ Exception -> 0x005c }
            int r6 = r9.canProtocolLength     // Catch:{ Exception -> 0x005c }
            if (r5 <= r6) goto L_0x0041
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x005c }
            java.lang.String r6 = "canbox: shit:"
            r5.<init>(r6)     // Catch:{ Exception -> 0x005c }
            java.lang.String r6 = java.lang.Integer.toHexString(r0)     // Catch:{ Exception -> 0x005c }
            java.lang.StringBuilder r5 = r5.append(r6)     // Catch:{ Exception -> 0x005c }
            java.lang.String r6 = "\n"
            java.lang.StringBuilder r5 = r5.append(r6)     // Catch:{ Exception -> 0x005c }
            java.lang.String r3 = r5.toString()     // Catch:{ Exception -> 0x005c }
            java.lang.String r5 = "driverlog"
            android.util.Log.e(r5, r3)     // Catch:{ Exception -> 0x005c }
            goto L_0x0041
        */
        throw new UnsupportedOperationException("Method not decompiled: com.backaudio.android.driver.ProtocolAnalyzer.analyzeCanbox(byte[]):void");
    }

    /* JADX INFO: finally extract failed */
    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void analyzeMcu(byte[] r10, int r11, int r12) {
        /*
            r9 = this;
            r3 = r11
            r4 = 0
        L_0x0002:
            int r5 = r10.length
            if (r3 >= r5) goto L_0x0007
            if (r4 < r12) goto L_0x0008
        L_0x0007:
            return
        L_0x0008:
            byte r0 = r10[r3]     // Catch:{ Exception -> 0x005f }
            int r5 = r9.currentProtocolLength     // Catch:{ Exception -> 0x005f }
            switch(r5) {
                case 0: goto L_0x0047;
                case 1: goto L_0x0083;
                case 2: goto L_0x00bf;
                default: goto L_0x000f;
            }     // Catch:{ Exception -> 0x005f }
        L_0x000f:
            int r5 = r9.currentProtocolLength     // Catch:{ Exception -> 0x005f }
            int r6 = r9.protocolLength     // Catch:{ Exception -> 0x005f }
            if (r5 >= r6) goto L_0x0104
            java.io.ByteArrayOutputStream r5 = r9.protocolBuffer     // Catch:{ Exception -> 0x005f }
            r6 = 1
            byte[] r6 = new byte[r6]     // Catch:{ Exception -> 0x005f }
            r7 = 0
            r6[r7] = r0     // Catch:{ Exception -> 0x005f }
            r7 = 0
            r8 = 1
            r5.write(r6, r7, r8)     // Catch:{ Exception -> 0x005f }
            int r5 = r9.currentProtocolLength     // Catch:{ Exception -> 0x005f }
            int r5 = r5 + 1
            r9.currentProtocolLength = r5     // Catch:{ Exception -> 0x005f }
            int r5 = r9.currentProtocolLength     // Catch:{ Exception -> 0x005f }
            int r6 = r9.protocolLength     // Catch:{ Exception -> 0x005f }
            if (r5 != r6) goto L_0x0042
            java.io.ByteArrayOutputStream r5 = r9.protocolBuffer     // Catch:{ Exception -> 0x00e6 }
            byte[] r5 = r5.toByteArray()     // Catch:{ Exception -> 0x00e6 }
            r9.newMcuProtocol(r5)     // Catch:{ Exception -> 0x00e6 }
            java.io.ByteArrayOutputStream r5 = r9.protocolBuffer     // Catch:{ Exception -> 0x005f }
            r5.reset()     // Catch:{ Exception -> 0x005f }
            r5 = 0
            r9.currentProtocolLength = r5     // Catch:{ Exception -> 0x005f }
            r5 = 0
            r9.protocolLength = r5     // Catch:{ Exception -> 0x005f }
        L_0x0042:
            int r3 = r3 + 1
            int r4 = r4 + 1
            goto L_0x0002
        L_0x0047:
            r5 = -86
            if (r0 != r5) goto L_0x0064
            java.io.ByteArrayOutputStream r5 = r9.protocolBuffer     // Catch:{ Exception -> 0x005f }
            r6 = 1
            byte[] r6 = new byte[r6]     // Catch:{ Exception -> 0x005f }
            r7 = 0
            r6[r7] = r0     // Catch:{ Exception -> 0x005f }
            r7 = 0
            r8 = 1
            r5.write(r6, r7, r8)     // Catch:{ Exception -> 0x005f }
            int r5 = r9.currentProtocolLength     // Catch:{ Exception -> 0x005f }
            int r5 = r5 + 1
            r9.currentProtocolLength = r5     // Catch:{ Exception -> 0x005f }
            goto L_0x0042
        L_0x005f:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x0042
        L_0x0064:
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x005f }
            java.lang.String r6 = "mcu: require 0xAA but "
            r5.<init>(r6)     // Catch:{ Exception -> 0x005f }
            java.lang.String r6 = java.lang.Integer.toHexString(r0)     // Catch:{ Exception -> 0x005f }
            java.lang.StringBuilder r5 = r5.append(r6)     // Catch:{ Exception -> 0x005f }
            java.lang.String r6 = "\n"
            java.lang.StringBuilder r5 = r5.append(r6)     // Catch:{ Exception -> 0x005f }
            java.lang.String r2 = r5.toString()     // Catch:{ Exception -> 0x005f }
            java.lang.String r5 = "driverlog"
            android.util.Log.d(r5, r2)     // Catch:{ Exception -> 0x005f }
            goto L_0x0042
        L_0x0083:
            r5 = 85
            if (r0 != r5) goto L_0x009b
            java.io.ByteArrayOutputStream r5 = r9.protocolBuffer     // Catch:{ Exception -> 0x005f }
            r6 = 1
            byte[] r6 = new byte[r6]     // Catch:{ Exception -> 0x005f }
            r7 = 0
            r6[r7] = r0     // Catch:{ Exception -> 0x005f }
            r7 = 0
            r8 = 1
            r5.write(r6, r7, r8)     // Catch:{ Exception -> 0x005f }
            int r5 = r9.currentProtocolLength     // Catch:{ Exception -> 0x005f }
            int r5 = r5 + 1
            r9.currentProtocolLength = r5     // Catch:{ Exception -> 0x005f }
            goto L_0x0042
        L_0x009b:
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x005f }
            java.lang.String r6 = "require 0x55 but "
            r5.<init>(r6)     // Catch:{ Exception -> 0x005f }
            java.lang.String r6 = java.lang.Integer.toHexString(r0)     // Catch:{ Exception -> 0x005f }
            java.lang.StringBuilder r5 = r5.append(r6)     // Catch:{ Exception -> 0x005f }
            java.lang.String r6 = "\n"
            java.lang.StringBuilder r5 = r5.append(r6)     // Catch:{ Exception -> 0x005f }
            java.lang.String r2 = r5.toString()     // Catch:{ Exception -> 0x005f }
            java.lang.String r5 = "driverlog"
            android.util.Log.d(r5, r2)     // Catch:{ Exception -> 0x005f }
            java.io.ByteArrayOutputStream r5 = r9.protocolBuffer     // Catch:{ Exception -> 0x005f }
            r5.reset()     // Catch:{ Exception -> 0x005f }
            goto L_0x0042
        L_0x00bf:
            r9.protocolLength = r0     // Catch:{ Exception -> 0x005f }
            int r5 = r9.protocolLength     // Catch:{ Exception -> 0x005f }
            if (r5 >= 0) goto L_0x00cb
            int r5 = r9.protocolLength     // Catch:{ Exception -> 0x005f }
            int r5 = r5 + 256
            r9.protocolLength = r5     // Catch:{ Exception -> 0x005f }
        L_0x00cb:
            int r5 = r9.protocolLength     // Catch:{ Exception -> 0x005f }
            int r5 = r5 + 4
            r9.protocolLength = r5     // Catch:{ Exception -> 0x005f }
            java.io.ByteArrayOutputStream r5 = r9.protocolBuffer     // Catch:{ Exception -> 0x005f }
            r6 = 1
            byte[] r6 = new byte[r6]     // Catch:{ Exception -> 0x005f }
            r7 = 0
            r6[r7] = r0     // Catch:{ Exception -> 0x005f }
            r7 = 0
            r8 = 1
            r5.write(r6, r7, r8)     // Catch:{ Exception -> 0x005f }
            int r5 = r9.currentProtocolLength     // Catch:{ Exception -> 0x005f }
            int r5 = r5 + 1
            r9.currentProtocolLength = r5     // Catch:{ Exception -> 0x005f }
            goto L_0x0042
        L_0x00e6:
            r1 = move-exception
            r1.printStackTrace()     // Catch:{ all -> 0x00f7 }
            java.io.ByteArrayOutputStream r5 = r9.protocolBuffer     // Catch:{ Exception -> 0x005f }
            r5.reset()     // Catch:{ Exception -> 0x005f }
            r5 = 0
            r9.currentProtocolLength = r5     // Catch:{ Exception -> 0x005f }
            r5 = 0
            r9.protocolLength = r5     // Catch:{ Exception -> 0x005f }
            goto L_0x0042
        L_0x00f7:
            r5 = move-exception
            java.io.ByteArrayOutputStream r6 = r9.protocolBuffer     // Catch:{ Exception -> 0x005f }
            r6.reset()     // Catch:{ Exception -> 0x005f }
            r6 = 0
            r9.currentProtocolLength = r6     // Catch:{ Exception -> 0x005f }
            r6 = 0
            r9.protocolLength = r6     // Catch:{ Exception -> 0x005f }
            throw r5     // Catch:{ Exception -> 0x005f }
        L_0x0104:
            int r5 = r9.currentProtocolLength     // Catch:{ Exception -> 0x005f }
            int r6 = r9.protocolLength     // Catch:{ Exception -> 0x005f }
            if (r5 <= r6) goto L_0x0042
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x005f }
            java.lang.String r6 = "mcu: shit:"
            r5.<init>(r6)     // Catch:{ Exception -> 0x005f }
            java.lang.String r6 = java.lang.Integer.toHexString(r0)     // Catch:{ Exception -> 0x005f }
            java.lang.StringBuilder r5 = r5.append(r6)     // Catch:{ Exception -> 0x005f }
            java.lang.String r6 = "\n"
            java.lang.StringBuilder r5 = r5.append(r6)     // Catch:{ Exception -> 0x005f }
            java.lang.String r2 = r5.toString()     // Catch:{ Exception -> 0x005f }
            java.lang.String r5 = "driverlog"
            android.util.Log.d(r5, r2)     // Catch:{ Exception -> 0x005f }
            goto L_0x0042
        */
        throw new UnsupportedOperationException("Method not decompiled: com.backaudio.android.driver.ProtocolAnalyzer.analyzeMcu(byte[], int, int):void");
    }

    public void pushMcu(byte[] buffer, int index, int len) {
        String temp = "mcu:recv:<[" + Utils.byteArrayToHexString(buffer, index, len) + "]>";
        Log.d("driverlog", temp);
        this.logger.debug(temp);
        analyzeMcu(buffer, index, len);
    }

    private void pushCanbox(byte[] buffer) {
        synchronized (ProtocolAnalyzer.class) {
            String temp = "canbox:recv:<[" + Utils.byteArrayToHexString(buffer) + "]>";
            Log.d("driverlog", temp);
            this.logger.debug(temp);
            analyzeCanbox(buffer);
        }
    }

    private void newMcuProtocol(byte[] buffer) {
        Log.d("driverlog", "mcuParse:[" + Utils.byteArrayToHexString(buffer) + "]");
        if (buffer == null || buffer.length < 5) {
            Log.d("driverlog", "mcu: ProtocolAnalyzer invalid mcu buffer, drop");
        } else if (!checkMcu(buffer)) {
            Log.e("driverlog", "checkMcu failed");
        } else if (buffer[3] == 1 && buffer[4] == 0) {
            Mainboard.getInstance().analyzeMcuEnterAccOrWakeup(buffer);
        } else if (buffer[4] == 17) {
            Mainboard.getInstance().analyzeMcuInfoPro(buffer);
        } else if (buffer[3] != 1 || buffer[4] != 62) {
            if (buffer[3] == -30) {
                Mainboard.getInstance().analyzeMcuUpgradeStatePro(buffer);
            } else if (buffer[3] == -28) {
                Mainboard.getInstance().analyzeMcuUpgradeDateByindexPro(buffer);
            } else if (buffer[3] == 1 && buffer[4] == 80) {
                pushCanbox(buffer);
            } else if (buffer[3] == 1 && buffer[4] == 75) {
                Mainboard.getInstance().analyzeShowOrHideCarLayer(buffer);
            } else if (buffer[3] == 1 && buffer[4] == 91) {
                Mainboard.getInstance().analyzeReverseTpye(buffer);
            } else if (buffer[3] == 1 && buffer[4] == 98) {
                Mainboard.getInstance().analyzeStoreDateFromMCU(buffer);
            } else if (buffer[3] == 1 && buffer[4] == 102) {
                Mainboard.getInstance().analyzeBenzTpye(buffer);
            } else if (buffer[3] == 1 && buffer[4] == 106) {
                Mainboard.getInstance().analyzeBenzSize(buffer);
            } else if (buffer[3] == 1 && buffer[4] == 104) {
                Mainboard.getInstance().analyzeReverseMediaSet(buffer);
            } else if (buffer[3] == 1 && buffer[4] == 111) {
                Mainboard.getInstance().analyzeLanguageMediaSet(buffer);
            } else if (buffer[3] == 1 && buffer[4] == 112) {
                Mainboard.getInstance().analyzeGestureHand(buffer);
            } else if (buffer[3] == 1 && buffer[4] == 115) {
                Mainboard.getInstance().analyzeDVState(buffer);
            }
        }
    }

    private void newProtocol(byte[] buffer) {
        Log.d("driverlog", "canboxParse:[" + Utils.byteArrayToHexString(buffer) + "]");
        if (buffer == null || buffer.length < 5) {
            Log.e("driverlog", "canboxParse invalid canbox buffer, drop");
        } else if (!checkCanbox(buffer)) {
            Log.e("driverlog", "error check");
        } else {
            Mainboard.getInstance().logcatCanbox(Utils.byteArrayToHexString(buffer));
            switch (buffer[1]) {
                case -30:
                    Mainboard.getInstance().analyzeUpgradeStatePro(buffer);
                    return;
                case -28:
                    Mainboard.getInstance().analyzeUpgradeDateByindexPro(buffer);
                    return;
                case 1:
                    Mainboard.getInstance().analyzeCarBasePro(buffer);
                    return;
                case 2:
                    Mainboard.getInstance().analyzeIdriverPro(buffer);
                    return;
                case 3:
                    Mainboard.getInstance().analyzeAirInfo(buffer);
                    return;
                case 6:
                    Mainboard.getInstance().analyzeCarBasePro1(buffer);
                    return;
                case 7:
                    Mainboard.getInstance().analyzeCarRunningInfoPro1(buffer);
                    return;
                case 8:
                    Mainboard.getInstance().analyzeTimePro(buffer);
                    return;
                case 18:
                    Mainboard.getInstance().analyzeOriginalCarPro(buffer);
                    return;
                case 44:
                    Mainboard.getInstance().analyzeAUXStatus(buffer);
                    return;
                case 53:
                    Mainboard.getInstance().analyzeCarRunningInfoPro(buffer);
                    return;
                case 54:
                    Mainboard.getInstance().analyzeCarRunningInfoPro2(buffer);
                    return;
                case TransportMediator.KEYCODE_MEDIA_PAUSE:
                    Mainboard.getInstance().analyzeCanboxPro(buffer);
                    return;
                default:
                    return;
            }
        }
    }

    public boolean checkCanbox(byte[] buffer) {
        if (buffer.length < 5) {
            Log.e("driverlog", "canboxParse_checkMcu buffer too short");
            return false;
        } else if (buffer[2] != ((byte) (buffer.length - 4))) {
            return false;
        } else {
            byte checksum = buffer[1];
            for (int i = 2; i < buffer.length - 1; i++) {
                checksum = (byte) (buffer[i] + checksum);
            }
            if (((byte) (checksum ^ 255)) == buffer[buffer.length - 1]) {
                return true;
            }
            return false;
        }
    }

    private boolean checkMcu(byte[] buffer) {
        if (buffer.length < 4) {
            Log.e("driverlog", "mcuParse_checkMcu buffer too short");
            return false;
        } else if (buffer[2] != ((byte) (buffer.length - 4))) {
            return false;
        } else {
            byte checksum = buffer[2];
            for (int i = 3; i < buffer.length - 1; i++) {
                checksum = (byte) (buffer[i] + checksum);
            }
            if (checksum == buffer[buffer.length - 1]) {
                return true;
            }
            return false;
        }
    }

    public void pushEvent(byte[] buffer, int off, int len) {
        int size = buffer.length / 2;
        synchronized (eventLock) {
            System.arraycopy(buffer, size, this.tmp, 0, this.tmp.length);
            handleEvent(this.tmp, off, len);
        }
    }

    private void handleEvent(byte[] buffer, int off, int len) {
        int type = ((buffer[1] & 255) << 8) + (buffer[0] & 255);
        int code = ((buffer[3] & 255) << 8) + (buffer[2] & 255);
        int value = ((buffer[5] & 255) << 8) + (buffer[4] & 255);
        if (type == ETouch.EV_ABS.getCode()) {
            if (code == ETouch.ABS_X.getCode()) {
                this.touch_x = value;
            } else if (code == ETouch.ABS_Y.getCode()) {
                this.touch_y = value;
            }
        } else if (type != ETouch.EV_KEY.getCode()) {
            ETouch.EV_SYN.getCode();
        } else if (code == ETouch.BTN_TOUCH.getCode() && value == 0) {
            this.logger.debug("button handleEvent touch_x = " + this.touch_x + ",touch_y = " + this.touch_y);
            Mainboard.getInstance().sendCoordinateToMcu(this.touch_x, this.touch_y, 1);
        }
    }

    public enum ETouch {
        EV_SYN(0),
        EV_KEY(1),
        EV_ABS(3),
        BTN_TOUCH(330),
        ABS_X(53),
        ABS_Y(54),
        SYN_REPORT(2);
        
        private int code;

        private ETouch(int temp) {
            this.code = temp;
        }

        public int getCode() {
            return this.code;
        }
    }
}
