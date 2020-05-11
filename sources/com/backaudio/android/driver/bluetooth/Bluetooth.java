package com.backaudio.android.driver.bluetooth;

import com.backaudio.android.driver.Mainboard;
import com.backaudio.android.driver.Utils;
import com.sun.mail.iap.Response;
import com.touchus.benchilauncher.views.MenuSlide;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;

public class Bluetooth {
    private static /* synthetic */ int[] $SWITCH_TABLE$com$backaudio$android$driver$bluetooth$EVirtualButton = null;
    private static final String GPIO_BT = "/sys/bus/platform/drivers/unibroad_gpio_control/gpio_bt";
    private static Bluetooth instance = null;
    private static boolean isRenGao = false;
    private static Logger logger = LoggerFactory.getLogger(Bluetooth.class);
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.US);
    private IBluetoothProtocolAnalyzer bluetoothProtocolAnalyzer;
    private List<IBluetoothEventHandler> eventHandlers;

    static /* synthetic */ int[] $SWITCH_TABLE$com$backaudio$android$driver$bluetooth$EVirtualButton() {
        int[] iArr = $SWITCH_TABLE$com$backaudio$android$driver$bluetooth$EVirtualButton;
        if (iArr == null) {
            iArr = new int[EVirtualButton.values().length];
            try {
                iArr[EVirtualButton.ASTERISK.ordinal()] = 11;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[EVirtualButton.EIGHT.ordinal()] = 8;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[EVirtualButton.FIVE.ordinal()] = 5;
            } catch (NoSuchFieldError e3) {
            }
            try {
                iArr[EVirtualButton.FOUR.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                iArr[EVirtualButton.NINE.ordinal()] = 9;
            } catch (NoSuchFieldError e5) {
            }
            try {
                iArr[EVirtualButton.ONE.ordinal()] = 1;
            } catch (NoSuchFieldError e6) {
            }
            try {
                iArr[EVirtualButton.SEVEN.ordinal()] = 7;
            } catch (NoSuchFieldError e7) {
            }
            try {
                iArr[EVirtualButton.SIX.ordinal()] = 6;
            } catch (NoSuchFieldError e8) {
            }
            try {
                iArr[EVirtualButton.THREE.ordinal()] = 3;
            } catch (NoSuchFieldError e9) {
            }
            try {
                iArr[EVirtualButton.TWO.ordinal()] = 2;
            } catch (NoSuchFieldError e10) {
            }
            try {
                iArr[EVirtualButton.WELL.ordinal()] = 12;
            } catch (NoSuchFieldError e11) {
            }
            try {
                iArr[EVirtualButton.ZERO.ordinal()] = 10;
            } catch (NoSuchFieldError e12) {
            }
            $SWITCH_TABLE$com$backaudio$android$driver$bluetooth$EVirtualButton = iArr;
        }
        return iArr;
    }

    private Bluetooth() throws Exception {
        this.eventHandlers = null;
        this.bluetoothProtocolAnalyzer = null;
        this.eventHandlers = new ArrayList();
        if (isRenGao) {
            this.bluetoothProtocolAnalyzer = new Bc8mpBluetoothProtocolAnalyzer();
        } else {
            this.bluetoothProtocolAnalyzer = new BluetoothProtocolAnalyzer2();
        }
    }

    public static Bluetooth getInstance() throws Exception {
        synchronized (Bluetooth.class) {
            if (instance == null) {
                instance = new Bluetooth();
            }
        }
        return instance;
    }

    public void push(byte[] buffer) {
        logger.debug("rec cmd:" + Utils.byteArrayToHexString(buffer, 0, buffer.length));
        logger.debug("push--------------");
    }

    public void addEventHandler(IBluetoothEventHandler handler) {
        if (handler != null && handler != null && !this.eventHandlers.contains(handler)) {
            this.eventHandlers.add(handler);
            this.bluetoothProtocolAnalyzer.setEventHandler(handler);
        }
    }

    private void ioWrite(String cmd) throws Exception {
        byte[] buffer = cmd.getBytes();
        logger.debug("send cmd:" + new String(buffer));
        writeBluetooth(buffer);
    }

    /* access modifiers changed from: protected */
    public byte[] wrap(byte[] buffer) {
        byte[] mcuProtocol = new byte[(buffer.length + 4)];
        mcuProtocol[0] = (byte) (buffer.length + 2);
        mcuProtocol[1] = 1;
        mcuProtocol[2] = 15;
        byte check = (byte) (((byte) (mcuProtocol[1] + mcuProtocol[2])) + mcuProtocol[0]);
        for (int i = 0; i < buffer.length; i++) {
            mcuProtocol[i + 3] = buffer[i];
            check = (byte) (buffer[i] + check);
        }
        mcuProtocol[buffer.length + 3] = check;
        return mcuProtocol;
    }

    public void writeBluetooth(byte[] buffer) throws Exception {
        Mainboard.getInstance().writeBlueTooth(buffer);
        logger.debug("bluetoothprotocal write::" + new String(buffer));
    }

    public void clearPairingList() throws Exception {
        if (isRenGao) {
            ioWrite("AT+DLPD=000000000000");
        } else {
            ioWrite("AT#CV\r\n");
        }
    }

    public void readVersionSync() throws Exception {
        if (isRenGao) {
            ioWrite("AT+GVER\r\n");
        } else {
            ioWrite("AT#MY\r\n");
        }
    }

    public void setBTVolume(int volume) throws Exception {
        if (!isRenGao) {
            ioWrite("AT#VF" + volume + "\r\n");
        }
    }

    public void readDeviceAddr() throws Exception {
        if (isRenGao) {
            ioWrite("AT+GLBA\r\n");
        } else {
            ioWrite("AT#DF\r\n");
        }
    }

    public void readDevicePIN() throws Exception {
        if (!isRenGao) {
            ioWrite("AT#MN\r\n");
        }
    }

    public void readDeviceNameSync() throws Exception {
        if (isRenGao) {
            ioWrite("AT+GLDN\r\n");
        } else {
            ioWrite("AT#MM\r\n");
        }
    }

    public void readPairingListSync() throws Exception {
        if (isRenGao) {
            ioWrite("AT+LSPD\r\n");
        } else {
            ioWrite("AT#MX\r\n");
        }
    }

    public void setBluetoothMusicMute(boolean isMute) throws Exception {
        if (isRenGao) {
            return;
        }
        if (isMute) {
            ioWrite("AT#VA\r\n");
        } else {
            ioWrite("AT#VB\r\n");
        }
    }

    public void enterPairingModeSync() throws Exception {
        if (isRenGao) {
            ioWrite("AT+EPRM=1\r\n");
        } else {
            ioWrite("AT#CA\r\n");
        }
    }

    public void leavePairingModeSync() throws Exception {
        if (isRenGao) {
            ioWrite("AT+EPRM=0\r\n");
        } else {
            ioWrite("AT#CB\r\n");
        }
    }

    public void hangUpThePhone() throws Exception {
        if (isRenGao) {
            ioWrite("AT+HFCHUP\r\n");
        } else {
            ioWrite("AT#CF\r\n");
        }
    }

    public void answerThePhone() throws Exception {
        if (isRenGao) {
            ioWrite("AT+HFANSW\r\n");
        } else {
            ioWrite("AT#CE\r\n");
        }
    }

    public void call(String phone) throws Exception {
        if (isRenGao) {
            ioWrite("AT+HFDIAL=" + phone + "\r\n");
        } else {
            ioWrite("AT#CW" + phone + "\r\n");
        }
    }

    public void readMediaStatus() throws Exception {
        if (isRenGao) {
            ioWrite("AT+A2DPSTAT\r\n");
        } else {
            ioWrite("AT#MV\r\n");
        }
    }

    public void readMediaInfo() throws Exception {
        if (!isRenGao) {
            ioWrite("AT#MK\r\n");
        }
    }

    public void pausePlaySync(boolean iPlay) throws Exception {
        if (isRenGao) {
            ioWrite("AT+PP\r\n");
        } else if (iPlay) {
            ioWrite("AT#MS\r\n");
        } else {
            ioWrite("AT#MB\r\n");
        }
    }

    public void pauseSync() throws Exception {
        if (!isRenGao) {
            ioWrite("AT#MB\r\n");
        }
    }

    public void stopPlay() throws Exception {
        if (isRenGao) {
            ioWrite("AT+STOP\r\n");
        } else {
            ioWrite("AT#MC\r\n");
        }
    }

    public void playNext() throws Exception {
        if (isRenGao) {
            ioWrite("AT+FWD\r\n");
        } else {
            ioWrite("AT#MD\r\n");
        }
    }

    public void playPrev() throws Exception {
        if (isRenGao) {
            ioWrite("AT+BACK\r\n");
        } else {
            ioWrite("AT#ME\r\n");
        }
    }

    public void downloadPhoneBookSync(int type) throws Exception {
        if (isRenGao) {
            switch (type) {
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                    ioWrite("AT+PBDOWN=" + type + "\r\n");
                    return;
                default:
                    ioWrite("AT+PBDOWN=1\r\n");
                    return;
            }
        } else {
            switch (type) {
                case 1:
                    ioWrite("AT#PA\r\n");
                    return;
                case 2:
                    ioWrite("AT#PH\r\n");
                    return;
                case 3:
                    ioWrite("AT#PI\r\n");
                    return;
                case 4:
                    ioWrite("AT#PJ\r\n");
                    return;
                case 5:
                    ioWrite("AT#PX\r\n");
                    return;
                default:
                    ioWrite("AT#PA\r\n");
                    return;
            }
        }
    }

    public void cancelDownloadPhoneBook() throws Exception {
        if (isRenGao) {
            ioWrite("AT+PBABORT\r\n");
        } else {
            ioWrite("AT#PS\r\n");
        }
    }

    public void connectDeviceSync(String address) throws Exception {
        String address2 = address.replace("\r\n", "");
        if (isRenGao) {
            ioWrite("AT+CPDL=" + address2 + "\r\n");
        } else {
            ioWrite("AT#CC" + address2 + "\r\n");
        }
    }

    public void incVolume() throws Exception {
        if (isRenGao) {
            ioWrite("AT+VUP\r\n");
        } else {
            ioWrite("AT#CK\r\n");
        }
    }

    public void decVolume() throws Exception {
        if (isRenGao) {
            ioWrite("AT+VDN\r\n");
        } else {
            ioWrite("AT#CL\r\n");
        }
    }

    public void tryToDownloadPhoneBook() throws Exception {
        if (isRenGao) {
            ioWrite("AT+PBCONN\r\n");
        } else {
            ioWrite("AT#PA\r\n");
        }
    }

    public void readPhoneStatusSync() throws Exception {
        if (isRenGao) {
            ioWrite("AT+HFPSTAT\r\n");
        } else {
            ioWrite("AT#CY\r\n");
        }
    }

    public void removeDevice(String address) throws Exception {
        if (isRenGao) {
            ioWrite("AT+DLPD=" + address.replace("\r\n", "") + "\r\n");
            return;
        }
        ioWrite("AT#CV\r\n");
    }

    public void switchPhoneDevice() throws Exception {
        if (isRenGao) {
            ioWrite("AT+HFADTS\r\n");
        } else {
            ioWrite("AT#CO\r\n");
        }
    }

    public void switchBtDevice() throws Exception {
        if (isRenGao) {
            ioWrite("AT+HFADTS\r\n");
        } else {
            ioWrite("AT#CP\r\n");
        }
    }

    public void switchDevice(boolean iPhone) throws Exception {
        if (isRenGao) {
            ioWrite("AT+HFADTS\r\n");
        } else if (iPhone) {
            ioWrite("AT#CO\r\n");
        } else {
            ioWrite("AT#CP\r\n");
        }
    }

    public void SetBoot() throws Exception {
        if (isRenGao) {
            ioWrite("AT+BOOT\r\n");
        } else {
            ioWrite("AT#CC\r\n");
        }
    }

    public void setBTEnterACC(boolean isAccOff) throws Exception {
        if (isRenGao) {
            return;
        }
        if (isAccOff) {
            ioWrite("AT#CZ0\r\n");
        } else {
            ioWrite("AT#CZ1\r\n");
        }
    }

    public void disconnectCurrentDevice() throws Exception {
        if (isRenGao) {
            ioWrite("AT+DSCA\r\n");
        } else {
            ioWrite("AT#CD\r\n");
        }
    }

    public void queryPhoneStatus() throws Exception {
        if (isRenGao) {
            ioWrite("AT+HFPSTAT\r\n");
        } else {
            ioWrite("AT#CY\r\n");
        }
    }

    public void setDeviceName(String deviceName) throws Exception {
        String deviceName2 = deviceName.replace("\r\n", "");
        if (isRenGao) {
            ioWrite("AT+SLDN=" + deviceName2 + "\r\n");
        } else {
            ioWrite("AT#MM" + deviceName2 + "\r\n");
        }
    }

    private void sendVirutalButton(EVirtualButton button, String prefix, String suffix) throws Exception {
        switch ($SWITCH_TABLE$com$backaudio$android$driver$bluetooth$EVirtualButton()[button.ordinal()]) {
            case 1:
                ioWrite(String.valueOf(prefix) + "1" + suffix);
                return;
            case 2:
                ioWrite(String.valueOf(prefix) + "2" + suffix);
                return;
            case 3:
                ioWrite(String.valueOf(prefix) + "3" + suffix);
                return;
            case 4:
                ioWrite(String.valueOf(prefix) + "4" + suffix);
                return;
            case 5:
                ioWrite(String.valueOf(prefix) + "5" + suffix);
                return;
            case 6:
                ioWrite(String.valueOf(prefix) + "6" + suffix);
                return;
            case 7:
                ioWrite(String.valueOf(prefix) + "7" + suffix);
                return;
            case 8:
                ioWrite(String.valueOf(prefix) + "8" + suffix);
                return;
            case 9:
                ioWrite(String.valueOf(prefix) + "9" + suffix);
                return;
            case 10:
                ioWrite(String.valueOf(prefix) + "0" + suffix);
                return;
            case MenuSlide.STATE_UP222 /*11*/:
                ioWrite(String.valueOf(prefix) + Marker.ANY_MARKER + suffix);
                return;
            case Response.BAD /*12*/:
                ioWrite(String.valueOf(prefix) + "#" + suffix);
                return;
            default:
                return;
        }
    }

    public void pressVirutalButton(EVirtualButton button) throws Exception {
        if (isRenGao) {
            sendVirutalButton(button, "AT+HFDTMF=", "\r\n");
        } else {
            sendVirutalButton(button, "AT#CX", "\r\n");
        }
    }
}
