package com.backaudio.android.driver;

import android.os.Environment;
import android.support.v4.view.MotionEventCompat;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {
    public static String AUDIO_SOURCE = (String.valueOf(PATH) + "/audio_source.prop");
    public static String PATH = (String.valueOf(Environment.getExternalStorageDirectory().getAbsolutePath()) + "/unibroad/driver");
    public static final int TIME_OUT = 1000;
    public static String VIDEO_SOURCE = (String.valueOf(PATH) + "/video_source.prop");
    public static final String aux_connect_flag = "/sys/bus/platform/drivers/image_sensor/tw8836_reg";
    public static final String collison_happen = "/sys/bus/i2c/drivers/DA380/da380_flag";
    public static final String collison_level = "/sys/bus/i2c/drivers/DA380/da380_sensitivity";
    private static final String droid_up = "/sys/bus/platform/drivers/unibroad_gpio_control/gpio_droid_up";
    private static boolean flag = false;
    public static final String gpio_radar = "/sys/bus/platform/drivers/unibroad_gpio_control/gpio_rada";
    private static String path = Environment.getExternalStorageDirectory().getAbsolutePath();
    public static final String radio_frequency = "/sys/bus/i2c/drivers/qn8027/qn8027_mode";
    public static final String usb_protocol = "/sys/bus/platform/drivers/mt_usb/mt_usb/usb_protocol";

    public static final void closeEdog() {
        echoFile("0", gpio_radar);
    }

    public static final void openEdog() {
        echoFile("1", gpio_radar);
    }

    public static final void closeRadioFrequency() {
        echoFile("0", radio_frequency);
    }

    public static final void openRadioFrequency() {
        echoFile("1", radio_frequency);
    }

    public static final void setCollisonCheckLevel(String level) {
        echoFile(level, collison_level);
    }

    public static final void setCollisonHappenState(String state) {
        echoFile(state, collison_happen);
    }

    public static final String getCollisonHappenState() {
        return catFile(collison_happen);
    }

    public static final String getAuxConnect() {
        return catFile(aux_connect_flag);
    }

    public static void switch2AUXSource() {
        echoFile("ff 01", aux_connect_flag);
        echoFile("02 4c", aux_connect_flag);
    }

    public static void switch2ReversingSource() {
        echoFile("ff 01", aux_connect_flag);
        echoFile("02 44", aux_connect_flag);
    }

    public static void switch2NoSource() {
        echoFile("ff 01", aux_connect_flag);
    }

    public static void settingReveringLight(String value) {
        echoFile("ff 00", aux_connect_flag);
        echoFile(value, aux_connect_flag);
    }

    public static final void readToWork() {
        echoFile("1", droid_up);
    }

    /* JADX WARNING: Removed duplicated region for block: B:14:0x0030 A[SYNTHETIC, Splitter:B:14:0x0030] */
    /* JADX WARNING: Removed duplicated region for block: B:19:0x0038 A[SYNTHETIC, Splitter:B:19:0x0038] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean saveSource(java.lang.String r6, java.lang.String r7) {
        /*
            r2 = 0
            java.io.File r1 = new java.io.File     // Catch:{ Exception -> 0x002d, all -> 0x0035 }
            r1.<init>(r6)     // Catch:{ Exception -> 0x002d, all -> 0x0035 }
            boolean r4 = r1.exists()     // Catch:{ Exception -> 0x002d, all -> 0x0035 }
            if (r4 != 0) goto L_0x0016
            java.io.File r4 = r1.getParentFile()     // Catch:{ Exception -> 0x002d, all -> 0x0035 }
            r4.mkdirs()     // Catch:{ Exception -> 0x002d, all -> 0x0035 }
            r1.createNewFile()     // Catch:{ Exception -> 0x002d, all -> 0x0035 }
        L_0x0016:
            java.io.FileOutputStream r3 = new java.io.FileOutputStream     // Catch:{ Exception -> 0x002d, all -> 0x0035 }
            r3.<init>(r1)     // Catch:{ Exception -> 0x002d, all -> 0x0035 }
            byte[] r4 = r7.getBytes()     // Catch:{ Exception -> 0x0045, all -> 0x0042 }
            r3.write(r4)     // Catch:{ Exception -> 0x0045, all -> 0x0042 }
            r3.flush()     // Catch:{ Exception -> 0x0045, all -> 0x0042 }
            if (r3 == 0) goto L_0x002a
            r3.close()     // Catch:{ Exception -> 0x0040 }
        L_0x002a:
            r4 = 1
            r2 = r3
        L_0x002c:
            return r4
        L_0x002d:
            r0 = move-exception
        L_0x002e:
            if (r2 == 0) goto L_0x0033
            r2.close()     // Catch:{ Exception -> 0x003c }
        L_0x0033:
            r4 = 0
            goto L_0x002c
        L_0x0035:
            r4 = move-exception
        L_0x0036:
            if (r2 == 0) goto L_0x003b
            r2.close()     // Catch:{ Exception -> 0x003e }
        L_0x003b:
            throw r4
        L_0x003c:
            r4 = move-exception
            goto L_0x0033
        L_0x003e:
            r5 = move-exception
            goto L_0x003b
        L_0x0040:
            r4 = move-exception
            goto L_0x002a
        L_0x0042:
            r4 = move-exception
            r2 = r3
            goto L_0x0036
        L_0x0045:
            r0 = move-exception
            r2 = r3
            goto L_0x002e
        */
        throw new UnsupportedOperationException("Method not decompiled: com.backaudio.android.driver.Utils.saveSource(java.lang.String, java.lang.String):boolean");
    }

    /* JADX WARNING: Removed duplicated region for block: B:30:0x0046 A[SYNTHETIC, Splitter:B:30:0x0046] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String getSource(java.lang.String r8) {
        /*
            r3 = 0
            java.io.File r2 = new java.io.File     // Catch:{ Exception -> 0x003a, all -> 0x0043 }
            r2.<init>(r8)     // Catch:{ Exception -> 0x003a, all -> 0x0043 }
            boolean r6 = r2.exists()     // Catch:{ Exception -> 0x003a, all -> 0x0043 }
            if (r6 != 0) goto L_0x0014
            if (r3 == 0) goto L_0x0011
            r3.close()     // Catch:{ Exception -> 0x004a }
        L_0x0011:
            java.lang.String r6 = ""
        L_0x0013:
            return r6
        L_0x0014:
            java.io.FileInputStream r4 = new java.io.FileInputStream     // Catch:{ Exception -> 0x003a, all -> 0x0043 }
            r4.<init>(r2)     // Catch:{ Exception -> 0x003a, all -> 0x0043 }
            int r6 = r4.available()     // Catch:{ Exception -> 0x0057, all -> 0x0054 }
            byte[] r0 = new byte[r6]     // Catch:{ Exception -> 0x0057, all -> 0x0054 }
            int r5 = r4.read(r0)     // Catch:{ Exception -> 0x0057, all -> 0x0054 }
            if (r5 != 0) goto L_0x002e
            if (r4 == 0) goto L_0x002a
            r4.close()     // Catch:{ Exception -> 0x004c }
        L_0x002a:
            java.lang.String r6 = ""
            r3 = r4
            goto L_0x0013
        L_0x002e:
            java.lang.String r6 = new java.lang.String     // Catch:{ Exception -> 0x0057, all -> 0x0054 }
            r6.<init>(r0)     // Catch:{ Exception -> 0x0057, all -> 0x0054 }
            if (r4 == 0) goto L_0x0038
            r4.close()     // Catch:{ Exception -> 0x004e }
        L_0x0038:
            r3 = r4
            goto L_0x0013
        L_0x003a:
            r1 = move-exception
        L_0x003b:
            if (r3 == 0) goto L_0x0040
            r3.close()     // Catch:{ Exception -> 0x0050 }
        L_0x0040:
            java.lang.String r6 = ""
            goto L_0x0013
        L_0x0043:
            r6 = move-exception
        L_0x0044:
            if (r3 == 0) goto L_0x0049
            r3.close()     // Catch:{ Exception -> 0x0052 }
        L_0x0049:
            throw r6
        L_0x004a:
            r6 = move-exception
            goto L_0x0011
        L_0x004c:
            r6 = move-exception
            goto L_0x002a
        L_0x004e:
            r7 = move-exception
            goto L_0x0038
        L_0x0050:
            r6 = move-exception
            goto L_0x0040
        L_0x0052:
            r7 = move-exception
            goto L_0x0049
        L_0x0054:
            r6 = move-exception
            r3 = r4
            goto L_0x0044
        L_0x0057:
            r1 = move-exception
            r3 = r4
            goto L_0x003b
        */
        throw new UnsupportedOperationException("Method not decompiled: com.backaudio.android.driver.Utils.getSource(java.lang.String):java.lang.String");
    }

    public static String byteArrayToHexString(byte[] byteArray, int index, int len) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (byteArray == null || byteArray.length <= 0) {
            return null;
        }
        int i = index;
        int j = 0;
        while (i < byteArray.length && j < len) {
            String hv = Integer.toHexString(byteArray[i] & MotionEventCompat.ACTION_MASK);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
            i++;
            j++;
        }
        return stringBuilder.toString();
    }

    public static String byteArrayToHexString(byte[] byteArray) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (byteArray == null || byteArray.length <= 0) {
            return null;
        }
        for (byte b : byteArray) {
            String hv = Integer.toHexString(b & MotionEventCompat.ACTION_MASK);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    public static byte[] calcCheckSum(byte[] buffer) {
        if (buffer.length < 5) {
            System.out.println("invalid buffer length:" + byteArrayToHexString(buffer));
        }
        byte check = buffer[2];
        for (int i = 3; i < buffer.length - 1; i++) {
            check = (byte) (buffer[i] + check);
        }
        buffer[buffer.length - 1] = check;
        return buffer;
    }

    public static int stringToByte(String in, byte[] b) throws Exception {
        if (b.length < in.length() / 2) {
            throw new Exception("byte array too small");
        }
        int j = 0;
        StringBuffer buf = new StringBuffer(2);
        int i = 0;
        while (i < in.length()) {
            buf.insert(0, in.charAt(i));
            buf.insert(1, in.charAt(i + 1));
            int t = Integer.parseInt(buf.toString(), 16);
            System.out.println("byte hex value:" + t);
            b[j] = (byte) t;
            buf.delete(0, 2);
            i = i + 1 + 1;
            j++;
        }
        return j;
    }

    public static int hex2int(char c) {
        return "0123456789abcdef".indexOf(c);
    }

    /* JADX WARNING: Removed duplicated region for block: B:18:0x0051 A[SYNTHETIC, Splitter:B:18:0x0051] */
    /* JADX WARNING: Removed duplicated region for block: B:23:0x0059 A[SYNTHETIC, Splitter:B:23:0x0059] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean echoFile(java.lang.String r7, java.lang.String r8) {
        /*
            java.lang.String r4 = "driverlog"
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            java.lang.String r6 = "echoFile: "
            r5.<init>(r6)
            java.lang.StringBuilder r5 = r5.append(r7)
            java.lang.String r6 = " path: "
            java.lang.StringBuilder r5 = r5.append(r6)
            java.lang.StringBuilder r5 = r5.append(r8)
            java.lang.String r5 = r5.toString()
            android.util.Log.d(r4, r5)
            r2 = 0
            java.io.File r1 = new java.io.File     // Catch:{ Exception -> 0x0047 }
            r1.<init>(r8)     // Catch:{ Exception -> 0x0047 }
            boolean r4 = r1.exists()     // Catch:{ Exception -> 0x0047 }
            if (r4 == 0) goto L_0x005d
            boolean r4 = r1.canWrite()     // Catch:{ Exception -> 0x0047 }
            if (r4 == 0) goto L_0x005d
            java.io.FileOutputStream r3 = new java.io.FileOutputStream     // Catch:{ Exception -> 0x0047 }
            r3.<init>(r1)     // Catch:{ Exception -> 0x0047 }
            byte[] r4 = r7.getBytes()     // Catch:{ Exception -> 0x006e, all -> 0x006b }
            r3.write(r4)     // Catch:{ Exception -> 0x006e, all -> 0x006b }
            r3.flush()     // Catch:{ Exception -> 0x006e, all -> 0x006b }
            if (r3 == 0) goto L_0x0044
            r3.close()     // Catch:{ Exception -> 0x0065 }
        L_0x0044:
            r4 = 1
            r2 = r3
        L_0x0046:
            return r4
        L_0x0047:
            r0 = move-exception
        L_0x0048:
            java.lang.String r4 = "driverlog"
            java.lang.String r5 = "echofile err----------"
            android.util.Log.d(r4, r5)     // Catch:{ all -> 0x0056 }
            if (r2 == 0) goto L_0x0054
            r2.close()     // Catch:{ Exception -> 0x0067 }
        L_0x0054:
            r4 = 0
            goto L_0x0046
        L_0x0056:
            r4 = move-exception
        L_0x0057:
            if (r2 == 0) goto L_0x005c
            r2.close()     // Catch:{ Exception -> 0x0069 }
        L_0x005c:
            throw r4
        L_0x005d:
            if (r2 == 0) goto L_0x0054
            r2.close()     // Catch:{ Exception -> 0x0063 }
            goto L_0x0054
        L_0x0063:
            r4 = move-exception
            goto L_0x0054
        L_0x0065:
            r4 = move-exception
            goto L_0x0044
        L_0x0067:
            r4 = move-exception
            goto L_0x0054
        L_0x0069:
            r5 = move-exception
            goto L_0x005c
        L_0x006b:
            r4 = move-exception
            r2 = r3
            goto L_0x0057
        L_0x006e:
            r0 = move-exception
            r2 = r3
            goto L_0x0048
        */
        throw new UnsupportedOperationException("Method not decompiled: com.backaudio.android.driver.Utils.echoFile(java.lang.String, java.lang.String):boolean");
    }

    /* JADX WARNING: Removed duplicated region for block: B:15:0x0033 A[SYNTHETIC, Splitter:B:15:0x0033] */
    /* JADX WARNING: Removed duplicated region for block: B:20:0x003b A[SYNTHETIC, Splitter:B:20:0x003b] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String catFile(java.lang.String r8) {
        /*
            r2 = 0
            java.io.File r1 = new java.io.File     // Catch:{ Exception -> 0x0030, all -> 0x0038 }
            r1.<init>(r8)     // Catch:{ Exception -> 0x0030, all -> 0x0038 }
            boolean r6 = r1.exists()     // Catch:{ Exception -> 0x0030, all -> 0x0038 }
            if (r6 == 0) goto L_0x003f
            boolean r6 = r1.canRead()     // Catch:{ Exception -> 0x0030, all -> 0x0038 }
            if (r6 == 0) goto L_0x003f
            java.io.FileInputStream r3 = new java.io.FileInputStream     // Catch:{ Exception -> 0x0030, all -> 0x0038 }
            r3.<init>(r1)     // Catch:{ Exception -> 0x0030, all -> 0x0038 }
            int r6 = r3.available()     // Catch:{ Exception -> 0x0050, all -> 0x004d }
            byte[] r0 = new byte[r6]     // Catch:{ Exception -> 0x0050, all -> 0x004d }
            r6 = 0
            int r7 = r0.length     // Catch:{ Exception -> 0x0050, all -> 0x004d }
            int r4 = r3.read(r0, r6, r7)     // Catch:{ Exception -> 0x0050, all -> 0x004d }
            java.lang.String r5 = new java.lang.String     // Catch:{ Exception -> 0x0050, all -> 0x004d }
            r6 = 0
            r5.<init>(r0, r6, r4)     // Catch:{ Exception -> 0x0050, all -> 0x004d }
            if (r3 == 0) goto L_0x002e
            r3.close()     // Catch:{ Exception -> 0x0047 }
        L_0x002e:
            r2 = r3
        L_0x002f:
            return r5
        L_0x0030:
            r6 = move-exception
        L_0x0031:
            if (r2 == 0) goto L_0x0036
            r2.close()     // Catch:{ Exception -> 0x0049 }
        L_0x0036:
            r5 = 0
            goto L_0x002f
        L_0x0038:
            r6 = move-exception
        L_0x0039:
            if (r2 == 0) goto L_0x003e
            r2.close()     // Catch:{ Exception -> 0x004b }
        L_0x003e:
            throw r6
        L_0x003f:
            if (r2 == 0) goto L_0x0036
            r2.close()     // Catch:{ Exception -> 0x0045 }
            goto L_0x0036
        L_0x0045:
            r6 = move-exception
            goto L_0x0036
        L_0x0047:
            r6 = move-exception
            goto L_0x002e
        L_0x0049:
            r6 = move-exception
            goto L_0x0036
        L_0x004b:
            r7 = move-exception
            goto L_0x003e
        L_0x004d:
            r6 = move-exception
            r2 = r3
            goto L_0x0039
        L_0x0050:
            r6 = move-exception
            r2 = r3
            goto L_0x0031
        */
        throw new UnsupportedOperationException("Method not decompiled: com.backaudio.android.driver.Utils.catFile(java.lang.String):java.lang.String");
    }

    /* JADX WARNING: Removed duplicated region for block: B:29:0x00ae  */
    /* JADX WARNING: Removed duplicated region for block: B:31:0x00b3  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void bash(java.lang.String r10) throws java.lang.Exception {
        /*
            java.lang.Runtime r6 = java.lang.Runtime.getRuntime()
            r7 = 0
            java.io.File r8 = new java.io.File
            java.lang.String r9 = "/system/bin"
            r8.<init>(r9)
            java.lang.Process r3 = r6.exec(r10, r7, r8)
            java.io.InputStream r2 = r3.getInputStream()
            r4 = 0
            r0 = 0
            java.io.BufferedReader r5 = new java.io.BufferedReader     // Catch:{ Exception -> 0x0062, all -> 0x00ab }
            java.io.InputStreamReader r6 = new java.io.InputStreamReader     // Catch:{ Exception -> 0x0062, all -> 0x00ab }
            java.io.InputStream r7 = r3.getErrorStream()     // Catch:{ Exception -> 0x0062, all -> 0x00ab }
            r6.<init>(r7)     // Catch:{ Exception -> 0x0062, all -> 0x00ab }
            r5.<init>(r6)     // Catch:{ Exception -> 0x0062, all -> 0x00ab }
        L_0x0024:
            java.lang.String r0 = r5.readLine()     // Catch:{ Exception -> 0x008b, all -> 0x00c2 }
            if (r0 != 0) goto L_0x006e
            java.io.BufferedReader r4 = new java.io.BufferedReader     // Catch:{ Exception -> 0x008b, all -> 0x00c2 }
            java.io.InputStreamReader r6 = new java.io.InputStreamReader     // Catch:{ Exception -> 0x008b, all -> 0x00c2 }
            r6.<init>(r2)     // Catch:{ Exception -> 0x008b, all -> 0x00c2 }
            r4.<init>(r6)     // Catch:{ Exception -> 0x008b, all -> 0x00c2 }
        L_0x0034:
            java.lang.String r0 = r4.readLine()     // Catch:{ Exception -> 0x0062, all -> 0x00ab }
            if (r0 != 0) goto L_0x008e
            r3.waitFor()     // Catch:{ Exception -> 0x0062, all -> 0x00ab }
            int r1 = r3.exitValue()     // Catch:{ Exception -> 0x0062, all -> 0x00ab }
            if (r1 == 0) goto L_0x00b7
            java.lang.Exception r6 = new java.lang.Exception     // Catch:{ Exception -> 0x0062, all -> 0x00ab }
            java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0062, all -> 0x00ab }
            java.lang.String r8 = "cmd ["
            r7.<init>(r8)     // Catch:{ Exception -> 0x0062, all -> 0x00ab }
            java.lang.StringBuilder r7 = r7.append(r10)     // Catch:{ Exception -> 0x0062, all -> 0x00ab }
            java.lang.String r8 = "]exit:"
            java.lang.StringBuilder r7 = r7.append(r8)     // Catch:{ Exception -> 0x0062, all -> 0x00ab }
            java.lang.StringBuilder r7 = r7.append(r1)     // Catch:{ Exception -> 0x0062, all -> 0x00ab }
            java.lang.String r7 = r7.toString()     // Catch:{ Exception -> 0x0062, all -> 0x00ab }
            r6.<init>(r7)     // Catch:{ Exception -> 0x0062, all -> 0x00ab }
            throw r6     // Catch:{ Exception -> 0x0062, all -> 0x00ab }
        L_0x0062:
            r6 = move-exception
        L_0x0063:
            if (r2 == 0) goto L_0x0068
            r2.close()
        L_0x0068:
            if (r4 == 0) goto L_0x006d
            r4.close()
        L_0x006d:
            return
        L_0x006e:
            java.lang.String r6 = ""
            java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x008b, all -> 0x00c2 }
            java.lang.String r8 = java.lang.String.valueOf(r10)     // Catch:{ Exception -> 0x008b, all -> 0x00c2 }
            r7.<init>(r8)     // Catch:{ Exception -> 0x008b, all -> 0x00c2 }
            java.lang.String r8 = "\r\n"
            java.lang.StringBuilder r7 = r7.append(r8)     // Catch:{ Exception -> 0x008b, all -> 0x00c2 }
            java.lang.StringBuilder r7 = r7.append(r0)     // Catch:{ Exception -> 0x008b, all -> 0x00c2 }
            java.lang.String r7 = r7.toString()     // Catch:{ Exception -> 0x008b, all -> 0x00c2 }
            android.util.Log.d(r6, r7)     // Catch:{ Exception -> 0x008b, all -> 0x00c2 }
            goto L_0x0024
        L_0x008b:
            r6 = move-exception
            r4 = r5
            goto L_0x0063
        L_0x008e:
            java.lang.String r6 = ""
            java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0062, all -> 0x00ab }
            java.lang.String r8 = java.lang.String.valueOf(r10)     // Catch:{ Exception -> 0x0062, all -> 0x00ab }
            r7.<init>(r8)     // Catch:{ Exception -> 0x0062, all -> 0x00ab }
            java.lang.String r8 = "\r\n"
            java.lang.StringBuilder r7 = r7.append(r8)     // Catch:{ Exception -> 0x0062, all -> 0x00ab }
            java.lang.StringBuilder r7 = r7.append(r0)     // Catch:{ Exception -> 0x0062, all -> 0x00ab }
            java.lang.String r7 = r7.toString()     // Catch:{ Exception -> 0x0062, all -> 0x00ab }
            android.util.Log.d(r6, r7)     // Catch:{ Exception -> 0x0062, all -> 0x00ab }
            goto L_0x0034
        L_0x00ab:
            r6 = move-exception
        L_0x00ac:
            if (r2 == 0) goto L_0x00b1
            r2.close()
        L_0x00b1:
            if (r4 == 0) goto L_0x00b6
            r4.close()
        L_0x00b6:
            throw r6
        L_0x00b7:
            if (r2 == 0) goto L_0x00bc
            r2.close()
        L_0x00bc:
            if (r4 == 0) goto L_0x006d
            r4.close()
            goto L_0x006d
        L_0x00c2:
            r6 = move-exception
            r4 = r5
            goto L_0x00ac
        */
        throw new UnsupportedOperationException("Method not decompiled: com.backaudio.android.driver.Utils.bash(java.lang.String):void");
    }

    /* JADX WARNING: Removed duplicated region for block: B:29:0x00a6  */
    /* JADX WARNING: Removed duplicated region for block: B:31:0x00ab  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void system(java.lang.String r9) throws java.lang.Exception {
        /*
            java.lang.Runtime r6 = java.lang.Runtime.getRuntime()
            java.lang.Process r3 = r6.exec(r9)
            java.io.InputStream r2 = r3.getInputStream()
            r4 = 0
            r0 = 0
            java.io.BufferedReader r5 = new java.io.BufferedReader     // Catch:{ Exception -> 0x005a, all -> 0x00a3 }
            java.io.InputStreamReader r6 = new java.io.InputStreamReader     // Catch:{ Exception -> 0x005a, all -> 0x00a3 }
            java.io.InputStream r7 = r3.getErrorStream()     // Catch:{ Exception -> 0x005a, all -> 0x00a3 }
            r6.<init>(r7)     // Catch:{ Exception -> 0x005a, all -> 0x00a3 }
            r5.<init>(r6)     // Catch:{ Exception -> 0x005a, all -> 0x00a3 }
        L_0x001c:
            java.lang.String r0 = r5.readLine()     // Catch:{ Exception -> 0x0083, all -> 0x00ba }
            if (r0 != 0) goto L_0x0066
            java.io.BufferedReader r4 = new java.io.BufferedReader     // Catch:{ Exception -> 0x0083, all -> 0x00ba }
            java.io.InputStreamReader r6 = new java.io.InputStreamReader     // Catch:{ Exception -> 0x0083, all -> 0x00ba }
            r6.<init>(r2)     // Catch:{ Exception -> 0x0083, all -> 0x00ba }
            r4.<init>(r6)     // Catch:{ Exception -> 0x0083, all -> 0x00ba }
        L_0x002c:
            java.lang.String r0 = r4.readLine()     // Catch:{ Exception -> 0x005a, all -> 0x00a3 }
            if (r0 != 0) goto L_0x0086
            r3.waitFor()     // Catch:{ Exception -> 0x005a, all -> 0x00a3 }
            int r1 = r3.exitValue()     // Catch:{ Exception -> 0x005a, all -> 0x00a3 }
            if (r1 == 0) goto L_0x00af
            java.lang.Exception r6 = new java.lang.Exception     // Catch:{ Exception -> 0x005a, all -> 0x00a3 }
            java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x005a, all -> 0x00a3 }
            java.lang.String r8 = "cmd ["
            r7.<init>(r8)     // Catch:{ Exception -> 0x005a, all -> 0x00a3 }
            java.lang.StringBuilder r7 = r7.append(r9)     // Catch:{ Exception -> 0x005a, all -> 0x00a3 }
            java.lang.String r8 = "]exit:"
            java.lang.StringBuilder r7 = r7.append(r8)     // Catch:{ Exception -> 0x005a, all -> 0x00a3 }
            java.lang.StringBuilder r7 = r7.append(r1)     // Catch:{ Exception -> 0x005a, all -> 0x00a3 }
            java.lang.String r7 = r7.toString()     // Catch:{ Exception -> 0x005a, all -> 0x00a3 }
            r6.<init>(r7)     // Catch:{ Exception -> 0x005a, all -> 0x00a3 }
            throw r6     // Catch:{ Exception -> 0x005a, all -> 0x00a3 }
        L_0x005a:
            r6 = move-exception
        L_0x005b:
            if (r2 == 0) goto L_0x0060
            r2.close()
        L_0x0060:
            if (r4 == 0) goto L_0x0065
            r4.close()
        L_0x0065:
            return
        L_0x0066:
            java.lang.String r6 = ""
            java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0083, all -> 0x00ba }
            java.lang.String r8 = java.lang.String.valueOf(r9)     // Catch:{ Exception -> 0x0083, all -> 0x00ba }
            r7.<init>(r8)     // Catch:{ Exception -> 0x0083, all -> 0x00ba }
            java.lang.String r8 = "\r\n"
            java.lang.StringBuilder r7 = r7.append(r8)     // Catch:{ Exception -> 0x0083, all -> 0x00ba }
            java.lang.StringBuilder r7 = r7.append(r0)     // Catch:{ Exception -> 0x0083, all -> 0x00ba }
            java.lang.String r7 = r7.toString()     // Catch:{ Exception -> 0x0083, all -> 0x00ba }
            android.util.Log.d(r6, r7)     // Catch:{ Exception -> 0x0083, all -> 0x00ba }
            goto L_0x001c
        L_0x0083:
            r6 = move-exception
            r4 = r5
            goto L_0x005b
        L_0x0086:
            java.lang.String r6 = ""
            java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x005a, all -> 0x00a3 }
            java.lang.String r8 = java.lang.String.valueOf(r9)     // Catch:{ Exception -> 0x005a, all -> 0x00a3 }
            r7.<init>(r8)     // Catch:{ Exception -> 0x005a, all -> 0x00a3 }
            java.lang.String r8 = "\r\n"
            java.lang.StringBuilder r7 = r7.append(r8)     // Catch:{ Exception -> 0x005a, all -> 0x00a3 }
            java.lang.StringBuilder r7 = r7.append(r0)     // Catch:{ Exception -> 0x005a, all -> 0x00a3 }
            java.lang.String r7 = r7.toString()     // Catch:{ Exception -> 0x005a, all -> 0x00a3 }
            android.util.Log.d(r6, r7)     // Catch:{ Exception -> 0x005a, all -> 0x00a3 }
            goto L_0x002c
        L_0x00a3:
            r6 = move-exception
        L_0x00a4:
            if (r2 == 0) goto L_0x00a9
            r2.close()
        L_0x00a9:
            if (r4 == 0) goto L_0x00ae
            r4.close()
        L_0x00ae:
            throw r6
        L_0x00af:
            if (r2 == 0) goto L_0x00b4
            r2.close()
        L_0x00b4:
            if (r4 == 0) goto L_0x0065
            r4.close()
            goto L_0x0065
        L_0x00ba:
            r6 = move-exception
            r4 = r5
            goto L_0x00a4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.backaudio.android.driver.Utils.system(java.lang.String):void");
    }

    public static String saveLogLine(String line, boolean isbyte) throws IOException {
        String logPath;
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String fileName = "log-" + formatter.format(new Date()).substring(0, 10) + "-" + (((System.currentTimeMillis() / 1000) / 60) / 60) + ".log";
        if (isbyte) {
            logPath = String.valueOf(path) + "/unibroad/benzbluetoothlog_byte/";
        } else {
            logPath = String.valueOf(path) + "/unibroad/benzbluetoothlog_line/";
        }
        if (Environment.getExternalStorageState().equals("mounted")) {
            File dir = new File(logPath);
            if (logFileSize(dir) > 5.0d) {
                deleteDirectory(logPath);
            }
            if (!dir.exists()) {
                dir.mkdirs();
            }
            FileOutputStream fos = new FileOutputStream(String.valueOf(logPath) + fileName, true);
            fos.write(line.toString().getBytes());
            fos.write(new byte[]{13, 10});
            fos.close();
        }
        return fileName;
    }

    public static double logFileSize(File dir) {
        if (!dir.exists()) {
            return 0.0d;
        }
        if (!dir.isDirectory()) {
            return (((double) dir.length()) / 1024.0d) / 1024.0d;
        }
        double size = 0.0d;
        for (File f : dir.listFiles()) {
            size += logFileSize(f);
        }
        return size;
    }

    public static boolean deleteDirectory(String sPath) {
        if (!sPath.endsWith(File.separator)) {
            sPath = String.valueOf(sPath) + File.separator;
        }
        File dirFile = new File(sPath);
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return false;
        }
        flag = true;
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()) {
                flag = deleteFile(files[i].getAbsolutePath());
                if (!flag) {
                    break;
                }
            } else {
                flag = deleteDirectory(files[i].getAbsolutePath());
                if (!flag) {
                    break;
                }
            }
        }
        if (!flag || !dirFile.delete()) {
            return false;
        }
        return true;
    }

    public static boolean deleteFile(String sPath) {
        File file = new File(sPath);
        if (file.isFile() && file.exists()) {
            file.delete();
            flag = true;
        }
        return flag;
    }
}
