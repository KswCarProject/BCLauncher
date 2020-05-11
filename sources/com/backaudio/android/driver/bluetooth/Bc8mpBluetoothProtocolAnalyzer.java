package com.backaudio.android.driver.bluetooth;

import com.backaudio.android.driver.Utils;
import com.backaudio.android.driver.bluetooth.bc8mpprotocol.AnswerPhoneResult;
import com.backaudio.android.driver.bluetooth.bc8mpprotocol.BaseMultilineProtocol;
import com.backaudio.android.driver.bluetooth.bc8mpprotocol.CallOutResult;
import com.backaudio.android.driver.bluetooth.bc8mpprotocol.CallingOutProtocol;
import com.backaudio.android.driver.bluetooth.bc8mpprotocol.ConnectedDeviceProtocol;
import com.backaudio.android.driver.bluetooth.bc8mpprotocol.DeviceNameProtocol;
import com.backaudio.android.driver.bluetooth.bc8mpprotocol.DeviceRemovedProtocol;
import com.backaudio.android.driver.bluetooth.bc8mpprotocol.DeviceSwitchedProtocol;
import com.backaudio.android.driver.bluetooth.bc8mpprotocol.EnterPairingModeResult;
import com.backaudio.android.driver.bluetooth.bc8mpprotocol.HangUpPhoneResult;
import com.backaudio.android.driver.bluetooth.bc8mpprotocol.IncomingCallProtocol;
import com.backaudio.android.driver.bluetooth.bc8mpprotocol.MediaInfoProtocol;
import com.backaudio.android.driver.bluetooth.bc8mpprotocol.MediaInfoUnitProtocol;
import com.backaudio.android.driver.bluetooth.bc8mpprotocol.MediaPlayStatusProtocol;
import com.backaudio.android.driver.bluetooth.bc8mpprotocol.MediaStatusProtocol;
import com.backaudio.android.driver.bluetooth.bc8mpprotocol.PairingListProtocol;
import com.backaudio.android.driver.bluetooth.bc8mpprotocol.PairingListUnitProtocol;
import com.backaudio.android.driver.bluetooth.bc8mpprotocol.PhoneBookCtrlStatusProtocol;
import com.backaudio.android.driver.bluetooth.bc8mpprotocol.PhoneBookListProtocol;
import com.backaudio.android.driver.bluetooth.bc8mpprotocol.PhoneStatusProtocol;
import com.backaudio.android.driver.bluetooth.bc8mpprotocol.SetPlayStatusProtocol;
import com.backaudio.android.driver.bluetooth.bc8mpprotocol.VersionProtocol;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;

public class Bc8mpBluetoothProtocolAnalyzer implements IBluetoothProtocolAnalyzer {
    private static Logger logger = LoggerFactory.getLogger(Bc8mpBluetoothProtocolAnalyzer.class);
    private static List<String> playStatusTag = null;
    private IBluetoothEventHandler handler = null;
    private boolean isLineEndDetected = false;
    private boolean isLineStartDeteceted = false;
    private MediaInfoProtocol mediaInfoProtocol = null;
    private MediaInfoUnitProtocol mediaInfoUnitProtocol = null;
    private BaseMultilineProtocol mutilineProtocol = null;
    private byte nextExceptedValue = 0;
    private PhoneBookListProtocol phoneBookListProtocol = null;
    private PhoneBookListProtocol phoneBookUnit = null;
    private ByteArrayOutputStream protocolBuffer = new ByteArrayOutputStream();

    public void setEventHandler(IBluetoothEventHandler handler2) {
        this.handler = handler2;
    }

    public Bc8mpBluetoothProtocolAnalyzer() {
        playStatusTag = new ArrayList();
        playStatusTag.add("PP");
        playStatusTag.add("STOP");
        playStatusTag.add("FWD");
        playStatusTag.add("BACK");
    }

    public void push(byte[] buffer, int off, int len) {
        byte[] newbuffer = Arrays.copyOf(buffer, len);
        logger.debug("bluetoothprot recv::" + Utils.byteArrayToHexString(newbuffer, 0, newbuffer.length));
        try {
            Utils.saveLogLine(Utils.byteArrayToHexString(newbuffer, 0, newbuffer.length), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        push(newbuffer);
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v33, resolved type: byte} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v45, resolved type: byte} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v67, resolved type: byte} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v68, resolved type: byte} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v69, resolved type: byte} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v70, resolved type: byte} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void push(byte[] r15) {
        /*
            r14 = this;
            r13 = 2
            r12 = 13
            r11 = 10
            r10 = 1
            r9 = 0
            r1 = 0
            com.backaudio.android.driver.bluetooth.bc8mpprotocol.PhoneBookListProtocol r6 = r14.phoneBookUnit
            if (r6 == 0) goto L_0x0050
            com.backaudio.android.driver.bluetooth.bc8mpprotocol.PhoneBookListProtocol r6 = r14.phoneBookUnit
            int r6 = r6.getNeedDataLength()
            if (r6 <= 0) goto L_0x0050
            int r6 = r15.length
            com.backaudio.android.driver.bluetooth.bc8mpprotocol.PhoneBookListProtocol r7 = r14.phoneBookUnit
            int r7 = r7.getNeedDataLength()
            if (r6 > r7) goto L_0x0024
            com.backaudio.android.driver.bluetooth.bc8mpprotocol.PhoneBookListProtocol r6 = r14.phoneBookUnit
            int r7 = r15.length
            r6.push(r15, r9, r7)
        L_0x0023:
            return
        L_0x0024:
            com.backaudio.android.driver.bluetooth.bc8mpprotocol.PhoneBookListProtocol r6 = r14.phoneBookUnit
            int r0 = r6.getNeedDataLength()
            int r6 = r15.length
            com.backaudio.android.driver.bluetooth.bc8mpprotocol.PhoneBookListProtocol r7 = r14.phoneBookUnit
            int r7 = r7.getNeedDataLength()
            int r2 = r6 - r7
            byte[] r5 = new byte[r2]
            com.backaudio.android.driver.bluetooth.bc8mpprotocol.PhoneBookListProtocol r6 = r14.phoneBookUnit
            com.backaudio.android.driver.bluetooth.bc8mpprotocol.PhoneBookListProtocol r7 = r14.phoneBookUnit
            int r7 = r7.getNeedDataLength()
            r6.push(r15, r9, r7)
            r4 = 0
        L_0x0041:
            if (r4 < r2) goto L_0x0047
            r14.push(r5)
            goto L_0x0023
        L_0x0047:
            int r6 = r4 + r0
            byte r6 = r15[r6]
            r5[r4] = r6
            int r4 = r4 + 1
            goto L_0x0041
        L_0x0050:
            com.backaudio.android.driver.bluetooth.bc8mpprotocol.MediaInfoUnitProtocol r6 = r14.mediaInfoUnitProtocol
            if (r6 == 0) goto L_0x0067
            com.backaudio.android.driver.bluetooth.bc8mpprotocol.MediaInfoUnitProtocol r6 = r14.mediaInfoUnitProtocol
            int r6 = r6.getNeedDataLength()
            if (r6 <= 0) goto L_0x0067
        L_0x005c:
            int r6 = r15.length
            if (r1 >= r6) goto L_0x0067
            com.backaudio.android.driver.bluetooth.bc8mpprotocol.MediaInfoUnitProtocol r6 = r14.mediaInfoUnitProtocol
            int r6 = r6.getNeedDataLength()
            if (r6 > 0) goto L_0x007e
        L_0x0067:
            int r6 = r15.length
            if (r1 >= r6) goto L_0x0023
            boolean r6 = r14.isLineStartDeteceted
            if (r6 != 0) goto L_0x00b1
            byte r6 = r15[r1]
            if (r6 != r12) goto L_0x0086
            r14.isLineStartDeteceted = r10
            java.io.ByteArrayOutputStream r6 = r14.protocolBuffer
            r6.write(r15, r1, r10)
            r14.nextExceptedValue = r11
        L_0x007b:
            int r1 = r1 + 1
            goto L_0x0067
        L_0x007e:
            com.backaudio.android.driver.bluetooth.bc8mpprotocol.MediaInfoUnitProtocol r6 = r14.mediaInfoUnitProtocol
            r6.push(r15, r1, r10)
            int r1 = r1 + 1
            goto L_0x005c
        L_0x0086:
            r14.isLineStartDeteceted = r9
            org.slf4j.Logger r7 = logger
            java.lang.StringBuilder r8 = new java.lang.StringBuilder
            java.lang.String r6 = "skip:"
            r8.<init>(r6)
            byte r6 = r15[r1]
            if (r6 <= 0) goto L_0x00ac
            byte r6 = r15[r1]
        L_0x0097:
            java.lang.String r6 = java.lang.Integer.toHexString(r6)
            java.lang.StringBuilder r6 = r8.append(r6)
            java.lang.String r6 = r6.toString()
            r7.error(r6)
            java.io.ByteArrayOutputStream r6 = r14.protocolBuffer
            r6.reset()
            goto L_0x007b
        L_0x00ac:
            byte r6 = r15[r1]
            int r6 = r6 + 256
            goto L_0x0097
        L_0x00b1:
            byte r6 = r14.nextExceptedValue
            if (r6 == 0) goto L_0x00f6
            boolean r6 = r14.isLineEndDetected
            if (r6 != 0) goto L_0x00f6
            byte r6 = r14.nextExceptedValue
            byte r7 = r15[r1]
            if (r6 != r7) goto L_0x00c7
            java.io.ByteArrayOutputStream r6 = r14.protocolBuffer
            r6.write(r15, r1, r10)
            r14.nextExceptedValue = r9
            goto L_0x007b
        L_0x00c7:
            byte r6 = r15[r1]
            if (r6 == r12) goto L_0x007b
            r14.isLineStartDeteceted = r9
            org.slf4j.Logger r7 = logger
            java.lang.StringBuilder r8 = new java.lang.StringBuilder
            java.lang.String r6 = "skip:"
            r8.<init>(r6)
            byte r6 = r15[r1]
            if (r6 <= 0) goto L_0x00f1
            byte r6 = r15[r1]
        L_0x00dc:
            java.lang.String r6 = java.lang.Integer.toHexString(r6)
            java.lang.StringBuilder r6 = r8.append(r6)
            java.lang.String r6 = r6.toString()
            r7.error(r6)
            java.io.ByteArrayOutputStream r6 = r14.protocolBuffer
            r6.reset()
            goto L_0x007b
        L_0x00f1:
            byte r6 = r15[r1]
            int r6 = r6 + 256
            goto L_0x00dc
        L_0x00f6:
            boolean r6 = r14.isLineEndDetected
            if (r6 != 0) goto L_0x0110
            byte r6 = r15[r1]
            if (r6 == r12) goto L_0x0105
            java.io.ByteArrayOutputStream r6 = r14.protocolBuffer
            r6.write(r15, r1, r10)
            goto L_0x007b
        L_0x0105:
            r14.isLineEndDetected = r10
            java.io.ByteArrayOutputStream r6 = r14.protocolBuffer
            r6.write(r15, r1, r10)
            r14.nextExceptedValue = r11
            goto L_0x007b
        L_0x0110:
            byte r6 = r14.nextExceptedValue
            if (r6 != r11) goto L_0x0179
            java.io.ByteArrayOutputStream r6 = r14.protocolBuffer
            r6.write(r15, r1, r10)
            java.lang.String r6 = new java.lang.String
            java.io.ByteArrayOutputStream r7 = r14.protocolBuffer
            byte[] r7 = r7.toByteArray()
            r6.<init>(r7)
            java.lang.String r7 = "\r\n\r\n"
            boolean r6 = r6.equals(r7)
            if (r6 == 0) goto L_0x014a
            org.slf4j.Logger r6 = logger
            java.lang.String r7 = "0d0a0d0a error protocol,try to repare"
            r6.error(r7)
            java.io.ByteArrayOutputStream r6 = r14.protocolBuffer
            r6.reset()
            java.io.ByteArrayOutputStream r6 = r14.protocolBuffer
            byte[] r7 = new byte[r13]
            r7 = {13, 10} // fill-array
            r6.write(r7, r9, r13)
            r14.isLineEndDetected = r9
            r14.isLineStartDeteceted = r10
            r14.nextExceptedValue = r9
            goto L_0x007b
        L_0x014a:
            java.io.ByteArrayOutputStream r6 = r14.protocolBuffer
            byte[] r6 = r6.toByteArray()
            r14.newLineProtocol(r6)
            r14.isLineStartDeteceted = r9
            r14.isLineEndDetected = r9
            r14.nextExceptedValue = r9
            java.io.ByteArrayOutputStream r6 = r14.protocolBuffer
            r6.reset()
            int r6 = r15.length
            int r6 = r6 - r1
            int r2 = r6 + -1
            if (r2 <= 0) goto L_0x007b
            byte[] r5 = new byte[r2]
            r3 = 0
        L_0x0167:
            if (r3 < r2) goto L_0x016e
            r14.push(r5)
            goto L_0x0023
        L_0x016e:
            int r6 = r3 + r1
            int r6 = r6 + 1
            byte r6 = r15[r6]
            r5[r3] = r6
            int r3 = r3 + 1
            goto L_0x0167
        L_0x0179:
            org.slf4j.Logger r6 = logger
            java.lang.StringBuilder r7 = new java.lang.StringBuilder
            java.lang.String r8 = "protocol error, drop:"
            r7.<init>(r8)
            byte r8 = r15[r1]
            java.lang.String r8 = java.lang.Integer.toHexString(r8)
            java.lang.StringBuilder r7 = r7.append(r8)
            java.lang.String r7 = r7.toString()
            r6.debug(r7)
            r14.isLineStartDeteceted = r9
            r14.isLineEndDetected = r9
            r14.nextExceptedValue = r9
            java.io.ByteArrayOutputStream r6 = r14.protocolBuffer
            r6.reset()
            goto L_0x007b
        */
        throw new UnsupportedOperationException("Method not decompiled: com.backaudio.android.driver.bluetooth.Bc8mpBluetoothProtocolAnalyzer.push(byte[]):void");
    }

    private boolean newLineProtocol(byte[] byteArray) {
        String key;
        String line = new String(byteArray);
        if (line == null || line.length() == 0) {
            return false;
        }
        if (!line.contains("HFPSTAT")) {
            try {
                Utils.saveLogLine(line, false);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String value = null;
        try {
            if (line.indexOf(61) != -1) {
                key = line.substring(line.indexOf(Marker.ANY_NON_NULL_MARKER) + 1, line.indexOf("="));
                value = line.substring(line.indexOf("=") + 1, line.lastIndexOf(13));
                logger.debug("bluetoothprotocal line recv::" + key + "=" + value);
            } else {
                key = line.substring(line.indexOf(Marker.ANY_NON_NULL_MARKER) + 1, line.lastIndexOf(13));
            }
            if (key.contains("PBDN")) {
                if (key.equals("PBDNDATA")) {
                    if (this.phoneBookListProtocol == null) {
                        this.phoneBookListProtocol = new PhoneBookListProtocol(key, value);
                    }
                    if (this.phoneBookUnit == null) {
                        this.phoneBookUnit = new PhoneBookListProtocol(key, value);
                    } else if (this.phoneBookUnit.getNeedDataLength() == 0) {
                        this.phoneBookListProtocol.addUnit(this.phoneBookUnit);
                        this.phoneBookUnit = new PhoneBookListProtocol(key, value);
                    } else {
                        logger.debug("phonebook error,needData:" + this.phoneBookUnit.getNeedDataLength());
                    }
                    return true;
                } else if (key.equals("PBDNEND")) {
                    if (!(this.phoneBookListProtocol == null || this.phoneBookUnit == null)) {
                        if (this.phoneBookUnit.getNeedDataLength() != 0) {
                            logger.debug("phonebook error,needData:" + this.phoneBookUnit.getNeedDataLength());
                        } else {
                            this.phoneBookListProtocol.addUnit(this.phoneBookUnit);
                            this.phoneBookUnit.reset();
                        }
                        this.handler.onPhoneBookList(this.phoneBookListProtocol);
                    }
                    this.phoneBookListProtocol = null;
                    this.phoneBookUnit = null;
                    return true;
                }
            }
            if (key.equals("PREND")) {
                this.handler.onPairingModeEnd();
                return true;
            } else if (key.equals("HFPOCID")) {
                this.handler.onPhoneCallingOut(new CallingOutProtocol(key, value));
                return true;
            } else if (key.equals("HFPAUDO")) {
                this.handler.ondeviceSwitchedProtocol(new DeviceSwitchedProtocol(key, value));
                return true;
            } else if (key.equals("GVER")) {
                this.handler.onVersion(new VersionProtocol(key, value));
                return true;
            } else if (key.equals("GLDN")) {
                this.handler.onDeviceName(new DeviceNameProtocol(key, value));
                return true;
            } else if (key.equals("EPRM")) {
                this.handler.onPairingModeResult(new EnterPairingModeResult(key, value));
                return true;
            } else if (playStatusTag.contains(key)) {
                this.handler.onSetPlayStatus(new SetPlayStatusProtocol(key, value));
                return true;
            } else if (key.equals("HFPCLID")) {
                this.handler.onIncomingCall(new IncomingCallProtocol(key, value));
                return true;
            } else if (key.equals("HFPSTAT")) {
                this.handler.onPhoneStatus(new PhoneStatusProtocol(key, value));
                return true;
            } else if (key.equals("HFANSW")) {
                this.handler.onAnswerPhone(new AnswerPhoneResult(key, value));
                return true;
            } else if (key.equals("HFCHUP")) {
                this.handler.onHangUpPhone(new HangUpPhoneResult(key, value));
                return true;
            } else if (key.equals("HFDIAL")) {
                this.handler.onCallOut(new CallOutResult(key, value));
                return true;
            } else if (key.equals("AVRCPSTAT")) {
                this.handler.onMediaStatus(new MediaStatusProtocol(key, value));
                return true;
            } else if (key.equals("A2DPSTAT") || key.equals("PLAYSTAT")) {
                this.handler.onMediaPlayStatus(new MediaPlayStatusProtocol(key, value));
                return true;
            } else if (key.equals("PBSTAT")) {
                this.handler.onPhoneBookCtrlStatus(new PhoneBookCtrlStatusProtocol(key, value));
                return true;
            } else if (key.equals("CCDA")) {
                this.handler.onConnectedDevice(new ConnectedDeviceProtocol(key, value));
                return true;
            } else if (key.equals("DLPD")) {
                this.handler.onDeviceRemoved(new DeviceRemovedProtocol(key, value));
                return true;
            } else if (!key.contains("LSPD")) {
                if (key.contains("MEDIAINFO")) {
                    if (key.equals("MEDIAINFOSTART")) {
                        this.mediaInfoProtocol = new MediaInfoProtocol();
                        return true;
                    } else if (key.equals("MEDIAINFOEND")) {
                        if (this.mediaInfoProtocol != null) {
                            this.handler.onMediaInfo(this.mediaInfoProtocol);
                            this.mediaInfoProtocol = null;
                            this.mediaInfoUnitProtocol = null;
                        }
                        return true;
                    } else if (key.equals("MEDIAINFO")) {
                        this.mediaInfoUnitProtocol = new MediaInfoUnitProtocol(key, value);
                        this.mediaInfoProtocol.addUnit(this.mediaInfoUnitProtocol);
                        return true;
                    }
                }
                return false;
            } else if (key.equals("LSPDSTART")) {
                this.mutilineProtocol = new BaseMultilineProtocol();
                return true;
            } else if (key.equals("LSPD")) {
                if (this.mutilineProtocol != null) {
                    this.mutilineProtocol.addUnit(new PairingListUnitProtocol(key, value));
                }
                return true;
            } else {
                if (key.equals("LSPDEND") && this.mutilineProtocol != null) {
                    this.handler.onPairingList(new PairingListProtocol(this.mutilineProtocol));
                    this.mutilineProtocol = null;
                }
                return true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void reset() {
    }
}
